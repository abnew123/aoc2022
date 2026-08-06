package aoc2022;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Day24 extends DayTemplate {
	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		Basin basin = new Basin(readLines(in));
		int trip1 = basin.travel(true, 0);
		if (part1) {
			return "" + trip1;
		}
		int trip2 = basin.travel(false, trip1);
		int trip3 = basin.travel(true, trip2);
		return "" + trip3;
	}

	@Override
	public String[] fullSolve(Scanner in) throws FileNotFoundException {
		Basin basin = new Basin(readLines(in));
		int trip1 = basin.travel(true, 0);
		int trip2 = basin.travel(false, trip1);
		int trip3 = basin.travel(true, trip2);
		return new String[] { trip1 + "", trip3 + "" };
	}

	private static List<String> readLines(Scanner in) {
		List<String> lines = new ArrayList<>();
		while (in.hasNextLine()) {
			lines.add(in.nextLine());
		}
		return lines;
	}

	/**
	 * Time-expanded bitset reachability over interior cells only. Each interior
	 * row is a multi-word bit mask (bit c = interior column c), so any valley
	 * width is supported. Blizzard occupancy is assembled per minute from two
	 * small phase tables instead of a full period-expanded blocked grid:
	 * horizontal blizzards repeat with period W, so W phase tables of H row
	 * masks are built by 1-bit rotations; vertical blizzards repeat with period
	 * H and only permute whole rows, so H phase tables are built by OR-ing
	 * row-shifted copies of the up/down masks. Each minute the reachable set is
	 * widened by the four shifts plus waiting and masked by free cells. The
	 * entry and exit cells sit outside the interior and are tracked as two
	 * booleans; they are never blocked and touch one interior cell each.
	 */
	private static final class Basin {
		private final int height;
		private final int width;
		private final int rowWords;
		private final long lastMask;
		private final int period;
		private final int topWord;
		private final long topBit;
		private final int bottomWord;
		private final long bottomBit;
		private final long[] horizontalPhases;
		private final long[] verticalPhases;
		private final long[] current;
		private final long[] next;

		Basin(List<String> lines) {
			int rows = lines.size();
			int cols = lines.get(0).length();
			height = rows - 2;
			width = cols - 2;
			rowWords = (width + 63) >>> 6;
			int topBits = width - ((rowWords - 1) << 6);
			lastMask = topBits == 64 ? -1L : (1L << topBits) - 1;
			period = lcm(width, height);
			int topCol = lines.get(0).indexOf('.') - 1;
			int bottomCol = lines.get(rows - 1).indexOf('.') - 1;
			topWord = topCol >>> 6;
			topBit = 1L << (topCol & 63);
			bottomWord = (height - 1) * rowWords + (bottomCol >>> 6);
			bottomBit = 1L << (bottomCol & 63);
			long[] right = new long[height * rowWords];
			long[] left = new long[height * rowWords];
			long[] down = new long[height * rowWords];
			long[] up = new long[height * rowWords];
			for (int r = 0; r < height; r++) {
				String line = lines.get(r + 1);
				int base = r * rowWords;
				for (int c = 0; c < width; c++) {
					char ch = line.charAt(c + 1);
					if (ch == '.') {
						continue;
					}
					int word = base + (c >>> 6);
					long bit = 1L << (c & 63);
					if (ch == '>') {
						right[word] |= bit;
					} else if (ch == '<') {
						left[word] |= bit;
					} else if (ch == 'v') {
						down[word] |= bit;
					} else if (ch == '^') {
						up[word] |= bit;
					}
				}
			}
			horizontalPhases = buildHorizontalPhases(right, left);
			verticalPhases = buildVerticalPhases(down, up);
			current = new long[height * rowWords];
			next = new long[height * rowWords];
		}

		/**
		 * horizontalPhases[p] holds, for phase p = t mod W, the union of '>' and
		 * '<' occupancy as H row masks. Built by rotating each row's phase-0
		 * masks one bit per phase. Bits at or above W in each row's last word
		 * are set so that AND-NOT with the assembled occupancy also clears any
		 * overflow bit produced by the move-right shift.
		 */
		private long[] buildHorizontalPhases(long[] right, long[] left) {
			int stride = height * rowWords;
			long[] phases = new long[width * stride];
			int topShift = (width - 1) & 63;
			for (int phase = 0; phase < width; phase++) {
				int offset = phase * stride;
				if (phase > 0) {
					for (int r = 0; r < height; r++) {
						rotateUpOneBit(right, r * rowWords, topShift);
						rotateDownOneBit(left, r * rowWords, topShift);
					}
				}
				for (int r = 0; r < height; r++) {
					int base = r * rowWords;
					for (int k = 0; k < rowWords; k++) {
						long occupancy = right[base + k] | left[base + k];
						if (k == rowWords - 1) {
							occupancy |= ~lastMask;
						}
						phases[offset + base + k] = occupancy;
					}
				}
			}
			return phases;
		}

		/** Cyclic 1-bit shift toward higher columns within W bits ('>' step). */
		private void rotateUpOneBit(long[] masks, int base, int topShift) {
			long carry = (masks[base + rowWords - 1] >>> topShift) & 1L;
			for (int k = 0; k < rowWords; k++) {
				long value = masks[base + k];
				masks[base + k] = (value << 1) | carry;
				carry = value >>> 63;
			}
			masks[base + rowWords - 1] &= lastMask;
		}

		/** Cyclic 1-bit shift toward lower columns within W bits ('<' step). */
		private void rotateDownOneBit(long[] masks, int base, int topShift) {
			long carry = masks[base] & 1L;
			for (int k = 0; k < rowWords - 1; k++) {
				masks[base + k] = (masks[base + k] >>> 1) | (masks[base + k + 1] << 63);
			}
			masks[base + rowWords - 1] = (masks[base + rowWords - 1] >>> 1) | (carry << topShift);
		}

		/**
		 * verticalPhases[p] holds, for phase p = t mod H, the union of 'v' and
		 * '^' occupancy. Vertical blizzards keep their column and cycle rows, so
		 * phase p is just the phase-0 row sets re-indexed by +-p rows.
		 */
		private long[] buildVerticalPhases(long[] down, long[] up) {
			int stride = height * rowWords;
			long[] phases = new long[height * stride];
			for (int phase = 0; phase < height; phase++) {
				int offset = phase * stride;
				for (int r = 0; r < height; r++) {
					int downRow = r - phase;
					if (downRow < 0) {
						downRow += height;
					}
					int upRow = r + phase;
					if (upRow >= height) {
						upRow -= height;
					}
					for (int k = 0; k < rowWords; k++) {
						phases[offset + r * rowWords + k] =
								down[downRow * rowWords + k] | up[upRow * rowWords + k];
					}
				}
			}
			return phases;
		}

		/**
		 * Returns the first minute the target door is reached, starting from the
		 * other door at startTime. The source door stays reachable forever
		 * (waiting there is always legal), so the search state is the interior
		 * bitset plus two door flags.
		 */
		int travel(boolean fromTop, int startTime) {
			long[] cur = current;
			long[] nxt = next;
			Arrays.fill(cur, 0L);
			boolean topReach = fromTop;
			boolean bottomReach = !fromTop;
			int stride = height * rowWords;
			long steps = (long) period * ((long) height * width + 2) + 1;
			int limit = (int) Math.min(Integer.MAX_VALUE - 8L, startTime + steps);
			for (int time = startTime + 1; time <= limit; time++) {
				for (int r = 0; r < height; r++) {
					int base = r * rowWords;
					int above = base - rowWords;
					int below = base + rowWords;
					for (int k = 0; k < rowWords; k++) {
						long value = cur[base + k];
						if (r > 0) {
							value |= cur[above + k];
						}
						if (r + 1 < height) {
							value |= cur[below + k];
						}
						nxt[base + k] = value;
					}
					long carry = 0;
					for (int k = 0; k < rowWords; k++) {
						long value = cur[base + k];
						nxt[base + k] |= (value << 1) | carry;
						carry = value >>> 63;
					}
					carry = 0;
					for (int k = rowWords - 1; k >= 0; k--) {
						long value = cur[base + k];
						nxt[base + k] |= (value >>> 1) | carry;
						carry = value << 63;
					}
				}
				if (topReach) {
					nxt[topWord] |= topBit;
				}
				if (bottomReach) {
					nxt[bottomWord] |= bottomBit;
				}
				int hOffset = (time % width) * stride;
				int vOffset = (time % height) * stride;
				for (int i = 0; i < stride; i++) {
					nxt[i] &= ~(horizontalPhases[hOffset + i] | verticalPhases[vOffset + i]);
				}
				boolean newTop = topReach || (cur[topWord] & topBit) != 0;
				boolean newBottom = bottomReach || (cur[bottomWord] & bottomBit) != 0;
				if (fromTop ? newBottom : newTop) {
					return time;
				}
				topReach = newTop;
				bottomReach = newBottom;
				long[] swap = cur;
				cur = nxt;
				nxt = swap;
			}
			return -1;
		}

		private static int lcm(int a, int b) {
			return a / gcd(a, b) * b;
		}

		private static int gcd(int a, int b) {
			while (b != 0) {
				int tmp = a % b;
				a = b;
				b = tmp;
			}
			return a;
		}
	}
}
