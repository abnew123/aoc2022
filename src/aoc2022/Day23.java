package aoc2022;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Day23 extends DayTemplate {

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		BitboardSimulation simulation = new BitboardSimulation(readLines(in));
		if (part1) {
			simulation.runRounds(10);
			return "" + simulation.emptyGroundInBoundingBox();
		}
		return "" + simulation.runUntilNoMovement();
	}

	@Override
	public String[] fullSolve(Scanner in) throws FileNotFoundException {
		BitboardSimulation simulation = new BitboardSimulation(readLines(in));
		int firstRoundWithoutMovement = simulation.runRounds(10);
		long part1 = simulation.emptyGroundInBoundingBox();
		int part2 = firstRoundWithoutMovement != 0
				? firstRoundWithoutMovement
				: simulation.runUntilNoMovement();
		return new String[] { part1 + "", part2 + "" };
	}

	/** One slurp; manual line split (\n, \r\n and lone \r all end a line). */
	private static List<String> readLines(Scanner in) {
		String text = in.useDelimiter("\\A").hasNext() ? in.next() : "";
		List<String> lines = new ArrayList<>();
		int length = text.length();
		int position = 0;
		while (position < length) {
			int lineStart = position;
			while (position < length && text.charAt(position) != '\n' && text.charAt(position) != '\r') {
				position++;
			}
			lines.add(text.substring(lineStart, position));
			if (position < length) {
				if (text.charAt(position) == '\r' && position + 1 < length
						&& text.charAt(position + 1) == '\n') {
					position++;
				}
				position++;
			}
		}
		return lines;
	}

	/*
	 * Bitboard rewrite: occupancy is a long bitmask per 64 columns of each row
	 * (bit b of word w = column w * 64 + b). Every round is computed with whole
	 * row words at a time - horizontal dilations, the four rotated direction
	 * proposals, the shifted destination masks and the collision cancellations
	 * are all shift/AND/OR word operations, so up to 64 cells advance per
	 * instruction instead of one elf at a time through a stamped grid.
	 *
	 * Collision semantics: within one direction the source-to-destination map is
	 * injective (each destination cell has exactly one possible source cell), so
	 * a cell proposed by two or more elves always shows up as an intersection of
	 * two different direction destination masks. Cancelling the union of all six
	 * pairwise intersections therefore cancels every multiply-proposed cell
	 * exactly, with no counting or parity tricks - three-plus-way pileups (were
	 * the geometry ever to allow them) would still hit at least one pairwise
	 * intersection and every involved elf stays put.
	 *
	 * Active-window narrowing: an elf with no neighbour proposes nothing under
	 * every direction rotation, and a row whose three-row neighbourhood did not
	 * change keeps the same activity status, so rows outside the hull of
	 * {rows with active elves} union {rows within one row of a change} can
	 * neither propose nor receive arrivals. Each round therefore runs the same
	 * four passes over a contiguous window [winLo, winHi] instead of the whole
	 * occupied band, with the window re-derived every round from the observed
	 * activity and change hulls (activity can spread at most one row per
	 * round). Diffusion runs converge locally, so late rounds shrink the
	 * window to the last moving pocket while the loop structure - and hence
	 * JIT behaviour - stays identical to the full-band version. cur/next swap
	 * row pointers across the processed window only, so rows outside it keep
	 * their words without copying; lo/hi stay exact via edge scans; the column
	 * extent is tracked conservatively (expand-only) for growth checks and the
	 * exact bounding box is rescanned on demand for the part-1 count.
	 *
	 * The board keeps guard rows/columns and regrows (in 64-row / 64-column
	 * chunks) whenever the occupied bounding band drifts near an edge, so the
	 * grid adapts to any input size and any amount of diffusion. After a grow
	 * the window resets to the whole band.
	 */
	private static final class BitboardSimulation {
		private static final int ROW_MARGIN = 34;
		private static final int COL_MARGIN = 34;
		private static final int GROW_CHUNK_ROWS = 64;

		private int words;
		private int height;
		private long[][] cur;
		private long[][] next;
		private long[][] full;
		private long[][] propN;
		private long[][] propS;
		private long[][] propW;
		private long[][] propE;
		private long[][] coll;
		private long[] vert3;
		private long[] colOr;
		private int lo;
		private int hi;
		private int minCol;
		private int maxCol;
		private int elfCount;
		private int round;
		private int winLo;
		private int winHi;

		BitboardSimulation(List<String> lines) {
			int inputRows = lines.size();
			int inputCols = 0;
			for (String line : lines) {
				inputCols = Math.max(inputCols, line.length());
			}
			height = inputRows + 2 * ROW_MARGIN;
			words = (inputCols + 2 * COL_MARGIN + 63) >> 6;
			allocate();

			lo = Integer.MAX_VALUE;
			hi = Integer.MIN_VALUE;
			minCol = Integer.MAX_VALUE;
			maxCol = Integer.MIN_VALUE;
			for (int r = 0; r < inputRows; r++) {
				String line = lines.get(r);
				long[] row = cur[r + ROW_MARGIN];
				for (int c = 0; c < line.length(); c++) {
					if (line.charAt(c) == '#') {
						int col = c + COL_MARGIN;
						row[col >> 6] |= 1L << (col & 63);
						elfCount++;
						lo = Math.min(lo, r + ROW_MARGIN);
						hi = Math.max(hi, r + ROW_MARGIN);
						minCol = Math.min(minCol, col);
						maxCol = Math.max(maxCol, col);
					}
				}
			}
			if (elfCount == 0) {
				lo = height / 2;
				hi = lo;
				minCol = COL_MARGIN;
				maxCol = COL_MARGIN;
			}
			winLo = lo;
			winHi = hi;
		}

		private void allocate() {
			cur = new long[height][words];
			next = new long[height][words];
			full = new long[height][words];
			propN = new long[height][words];
			propS = new long[height][words];
			propW = new long[height][words];
			propE = new long[height][words];
			coll = new long[height][words];
			vert3 = new long[words];
			colOr = new long[words];
		}

		/**
		 * Runs the given number of rounds; returns the 1-based index of the first
		 * round without movement if one occurred within them, otherwise 0. All
		 * rounds are executed regardless (the direction rotation keeps advancing),
		 * matching the reference semantics.
		 */
		int runRounds(int rounds) {
			int firstStall = 0;
			for (int i = 0; i < rounds; i++) {
				if (!step() && firstStall == 0) {
					firstStall = round;
				}
			}
			return firstStall;
		}

		/** Continues from the current round; returns the first round with no movement (1-based). */
		int runUntilNoMovement() {
			while (step()) {
				// keep stepping
			}
			return round;
		}

		long emptyGroundInBoundingBox() {
			if (elfCount == 0) {
				return 0;
			}
			int exactMin = Integer.MAX_VALUE;
			int exactMax = Integer.MIN_VALUE;
			for (int r = lo; r <= hi; r++) {
				long[] c = cur[r];
				for (int w = 0; w < words; w++) {
					long bits = c[w];
					if (bits != 0) {
						int first = (w << 6) + Long.numberOfTrailingZeros(bits);
						int last = (w << 6) + 63 - Long.numberOfLeadingZeros(bits);
						if (first < exactMin) {
							exactMin = first;
						}
						if (last > exactMax) {
							exactMax = last;
						}
					}
				}
			}
			long area = (long) (hi - lo + 1) * (exactMax - exactMin + 1);
			return area - elfCount;
		}

		/** Executes one round; returns true if any elf moved. */
		private boolean step() {
			ensureCapacity();
			int rot = round & 3;
			int words = this.words;

			// The window holds every row that can propose this round (hull of
			// last round's active rows and one-row-dilated changes). Rows
			// outside it are static: inactive elves stay inactive while their
			// three-row neighbourhood is unchanged, whatever the rotation.
			int wLo = Math.max(winLo, lo - 1);
			int wHi = Math.min(winHi, hi + 1);
			if (wLo > wHi) {
				round++;
				return false;
			}

			// Pass A: horizontal dilation of every row pass B will read.
			for (int r = wLo - 2; r <= wHi + 2; r++) {
				long[] c = cur[r];
				long[] f = full[r];
				for (int w = 0; w < words; w++) {
					f[w] = c[w] | shl(c, w) | shr(c, w);
				}
			}

			// Pass B: active elves and the four direction proposals in rotated
			// priority; the hull of rows with any active elf feeds the next
			// window.
			int actLo = Integer.MAX_VALUE;
			int actHi = Integer.MIN_VALUE;
			for (int r = wLo - 1; r <= wHi + 1; r++) {
				long[] c = cur[r];
				long[] up = cur[r - 1];
				long[] dn = cur[r + 1];
				long[] fu = full[r - 1];
				long[] fd = full[r + 1];
				long[] v = vert3;
				for (int w = 0; w < words; w++) {
					v[w] = up[w] | c[w] | dn[w];
				}
				long[] pN = propN[r];
				long[] pS = propS[r];
				long[] pW = propW[r];
				long[] pE = propE[r];
				long actOr = 0;
				for (int w = 0; w < words; w++) {
					long horiz = shl(c, w) | shr(c, w);
					long act = c[w] & (fu[w] | fd[w] | horiz);
					actOr |= act;
					long bN = fu[w];
					long bS = fd[w];
					long bW = shl(v, w);
					long bE = shr(v, w);
					long rem = act;
					long prN = 0;
					long prS = 0;
					long prW = 0;
					long prE = 0;
					switch (rot) {
					case 0:
						prN = rem & ~bN; rem &= bN;
						prS = rem & ~bS; rem &= bS;
						prW = rem & ~bW; rem &= bW;
						prE = rem & ~bE;
						break;
					case 1:
						prS = rem & ~bS; rem &= bS;
						prW = rem & ~bW; rem &= bW;
						prE = rem & ~bE; rem &= bE;
						prN = rem & ~bN;
						break;
					case 2:
						prW = rem & ~bW; rem &= bW;
						prE = rem & ~bE; rem &= bE;
						prN = rem & ~bN; rem &= bN;
						prS = rem & ~bS;
						break;
					default:
						prE = rem & ~bE; rem &= bE;
						prN = rem & ~bN; rem &= bN;
						prS = rem & ~bS; rem &= bS;
						prW = rem & ~bW;
						break;
					}
					pN[w] = prN;
					pS[w] = prS;
					pW[w] = prW;
					pE[w] = prE;
				}
				if (actOr != 0) {
					if (r < actLo) {
						actLo = r;
					}
					actHi = r;
				}
			}

			// The window moves at most one row per round, so rows just outside
			// the freshly written range are cleared defensively before they are
			// read.
			zeroRow(propN, wLo - 2);
			zeroRow(propN, wHi + 2);
			zeroRow(propS, wLo - 2);
			zeroRow(propS, wHi + 2);
			zeroRow(coll, wLo - 2);
			zeroRow(coll, wHi + 2);

			// Pass C1: destination masks, collision cells, arrivals.
			for (int r = wLo - 1; r <= wHi + 1; r++) {
				long[] dNs = propN[r + 1];
				long[] dSs = propS[r - 1];
				long[] pW = propW[r];
				long[] pE = propE[r];
				long[] cl = coll[r];
				long[] nx = next[r];
				for (int w = 0; w < words; w++) {
					long dN = dNs[w];
					long dS = dSs[w];
					long dW = shr(pW, w);
					long dE = shl(pE, w);
					long clash = (dN & dS) | ((dN | dS) & (dW | dE)) | (dW & dE);
					cl[w] = clash;
					nx[w] = (dN | dS | dW | dE) & ~clash;
				}
			}

			// Pass C2: keep stayers, drop successful movers, track the change
			// hull, the moved flag and the processed columns.
			long movedOr = 0;
			int chgLo = Integer.MAX_VALUE;
			int chgHi = Integer.MIN_VALUE;
			Arrays.fill(colOr, 0L);
			for (int r = wLo - 1; r <= wHi + 1; r++) {
				long[] c = cur[r];
				long[] nx = next[r];
				long[] pN = propN[r];
				long[] pS = propS[r];
				long[] pW = propW[r];
				long[] pE = propE[r];
				long[] cu = coll[r - 1];
				long[] cd = coll[r + 1];
				long[] cs = coll[r];
				long diffOr = 0;
				for (int w = 0; w < words; w++) {
					long leavers = (pN[w] & ~cu[w])
							| (pS[w] & ~cd[w])
							| (pW[w] & ~shl(cs, w))
							| (pE[w] & ~shr(cs, w));
					movedOr |= leavers;
					long value = nx[w] | (c[w] & ~leavers);
					nx[w] = value;
					diffOr |= value ^ c[w];
					colOr[w] |= value;
				}
				if (diffOr != 0) {
					if (r < chgLo) {
						chgLo = r;
					}
					chgHi = r;
				}
			}

			// Swap the row pointers of the processed window; rows outside it
			// are untouched and keep their current words.
			for (int r = wLo - 1; r <= wHi + 1; r++) {
				long[] swap = cur[r];
				cur[r] = next[r];
				next[r] = swap;
			}

			// Next round's window: active rows may keep proposing under a new
			// rotation, and changes wake their one-row neighbourhood.
			int newLo = Math.min(actLo, chgLo - 1);
			int newHi = Math.max(actHi, chgHi + 1);
			winLo = newLo;
			winHi = newHi;

			round++;
			updateBounds();
			return movedOr != 0;
		}

		private void updateBounds() {
			int newLo = lo - 1;
			int limit = hi + 1;
			while (newLo <= limit && rowEmpty(cur[newLo])) {
				newLo++;
			}
			if (newLo <= limit) {
				int newHi = limit;
				while (rowEmpty(cur[newHi])) {
					newHi--;
				}
				lo = newLo;
				hi = newHi;
			}
			int w0 = 0;
			while (w0 < words && colOr[w0] == 0) {
				w0++;
			}
			if (w0 < words) {
				int seenMin = (w0 << 6) + Long.numberOfTrailingZeros(colOr[w0]);
				int w1 = words - 1;
				while (colOr[w1] == 0) {
					w1--;
				}
				int seenMax = (w1 << 6) + 63 - Long.numberOfLeadingZeros(colOr[w1]);
				// Expand-only: columns outside the processed window were not
				// scanned, so the tracked extent may only widen. It is exact
				// whenever it matters for growth, because any new extreme
				// column is created by a move inside the window.
				if (seenMin < minCol) {
					minCol = seenMin;
				}
				if (seenMax > maxCol) {
					maxCol = seenMax;
				}
			}
		}

		private void ensureCapacity() {
			int growTop = lo < 3 ? GROW_CHUNK_ROWS : 0;
			int growBottom = hi > height - 4 ? GROW_CHUNK_ROWS : 0;
			if (growTop != 0 || growBottom != 0) {
				growRows(growTop, growBottom);
			}
			int growWest = minCol < 3 ? 1 : 0;
			int growEast = maxCol > (words << 6) - 4 ? 1 : 0;
			if (growWest != 0 || growEast != 0) {
				growCols(growWest, growEast);
			}
		}

		private void growRows(int top, int bottom) {
			long[][] oldCur = cur;
			int oldHeight = height;
			height += top + bottom;
			allocate();
			for (int r = 0; r < oldHeight; r++) {
				cur[r + top] = oldCur[r];
			}
			lo += top;
			hi += top;
			winLo = lo;
			winHi = hi;
		}

		private void growCols(int westWords, int eastWords) {
			long[][] oldCur = cur;
			int oldWords = words;
			words += westWords + eastWords;
			allocate();
			for (int r = 0; r < height; r++) {
				System.arraycopy(oldCur[r], 0, cur[r], westWords, oldWords);
			}
			minCol += westWords << 6;
			maxCol += westWords << 6;
			winLo = lo;
			winHi = hi;
		}

		private void zeroRow(long[][] grid, int r) {
			Arrays.fill(grid[r], 0L);
		}

		private boolean rowEmpty(long[] row) {
			long or = 0;
			for (int w = 0; w < words; w++) {
				or |= row[w];
			}
			return or == 0;
		}

		/** Mask of cells whose west-neighbour bit is set in {@code row} (bits move toward higher columns). */
		private long shl(long[] row, int w) {
			long x = row[w] << 1;
			return w == 0 ? x : x | (row[w - 1] >>> 63);
		}

		/** Mask of cells whose east-neighbour bit is set in {@code row} (bits move toward lower columns). */
		private long shr(long[] row, int w) {
			long x = row[w] >>> 1;
			return w == words - 1 ? x : x | (row[w + 1] << 63);
		}
	}
}
