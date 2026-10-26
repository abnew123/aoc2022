package aoc2022;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Day14 extends DayTemplate {

	private static final int SOURCE_X = 500;

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		int[] answers = solveBoth(in);
		return "" + answers[part1 ? 0 : 1];
	}

	@Override
	public String[] fullSolve(Scanner in) throws FileNotFoundException {
		int[] answers = solveBoth(in);
		return new String[] {"" + answers[0], "" + answers[1]};
	}

	/**
	 * Solves both parts in one pass over a bitset rock grid.
	 *
	 * Part 2: the terminal sand configuration equals the set of cells reachable from
	 * the source through non-rock cells by steps (dx in {-1,0,1}, dy = +1) above the
	 * floor, so the answer is one branchless row-by-row closure sweep with popcounts.
	 *
	 * Part 1 is order-sensitive: grains settle in exactly the post-order of a
	 * depth-first descent whose children are tried down, down-left, down-right, and
	 * the per-grain simulation ends when the first grain steps below the lowest rock.
	 * A DFS with an explicit stack that stops the first time it would push a cell in
	 * row maxY + 1 has therefore settled exactly the part 1 grain count.
	 */
	private static int[] solveBoth(Scanner in) {
		char[] text = (in.useDelimiter("\\A").hasNext() ? in.next() : "").toCharArray();

		// Linear parse: coordinate pairs into flat segment quads, tracking maxY.
		int[] segments = new int[256];
		int segmentEnd = 0;
		int maxY = 0;
		int pendingX = 0;
		boolean havePendingX = false;
		int prevX = 0;
		int prevY = 0;
		int linePoints = 0;
		int n = text.length;
		int i = 0;
		while (i <= n) {
			char c = i < n ? text[i] : '\n';
			if ((c >= '0' && c <= '9')
					|| (c == '-' && i + 1 < n && text[i + 1] >= '0' && text[i + 1] <= '9')) {
				boolean negative = c == '-';
				if (negative) {
					i++;
				}
				long value = 0;
				while (i < n && text[i] >= '0' && text[i] <= '9') {
					value = value * 10 + (text[i] - '0');
					if (value > Integer.MAX_VALUE) {
						throw new NumberFormatException("Coordinate out of range");
					}
					i++;
				}
				int number = (int) (negative ? -value : value);
				if (!havePendingX) {
					pendingX = number;
					havePendingX = true;
				} else {
					if (number < 0) {
						throw new IllegalArgumentException("Rock depth must be nonnegative");
					}
					if (number > maxY) {
						maxY = number;
					}
					if (linePoints > 0) {
						if (segmentEnd + 4 > segments.length) {
							segments = Arrays.copyOf(segments, segments.length * 2);
						}
						segments[segmentEnd++] = prevX;
						segments[segmentEnd++] = prevY;
						segments[segmentEnd++] = pendingX;
						segments[segmentEnd++] = number;
					}
					prevX = pendingX;
					prevY = number;
					linePoints++;
					havePendingX = false;
				}
				continue;
			}
			if (c == '\n' || c == '\r') {
				if (havePendingX) {
					throw new IllegalArgumentException("Malformed rock path");
				}
				if (linePoints == 1) {
					if (segmentEnd + 4 > segments.length) {
						segments = Arrays.copyOf(segments, segments.length * 2);
					}
					segments[segmentEnd++] = prevX;
					segments[segmentEnd++] = prevY;
					segments[segmentEnd++] = prevX;
					segments[segmentEnd++] = prevY;
				}
				linePoints = 0;
			}
			i++;
		}

		// Sand cells occupy rows 0 .. maxY + 1 (the floor is at maxY + 2). Every
		// reachable cell satisfies |x - SOURCE_X| <= y <= maxY + 1, so a grid of
		// half-width maxY + 2 keeps all reachable cells one full column away from
		// both edges and neighbour access needs no bounds checks.
		long rowsLong = (long) maxY + 2;
		long wordsPerRowLong = (2L * ((long) maxY + 2) + 1 + 63) >> 6;
		if (rowsLong * wordsPerRowLong > Integer.MAX_VALUE - 8) {
			throw new IllegalArgumentException("Cave is too deep to index");
		}
		int rows = (int) rowsLong;
		int halfWidth = maxY + 2;
		int cols = 2 * halfWidth + 1;
		int wordsPerRow = (int) wordsPerRowLong;
		int originX = SOURCE_X - halfWidth;
		int sourceCol = halfWidth;

		long[] rock = new long[rows * wordsPerRow];
		int maxCol = cols - 1;
		for (int s = 0; s < segmentEnd; s += 4) {
			int x1 = segments[s];
			int y1 = segments[s + 1];
			int x2 = segments[s + 2];
			int y2 = segments[s + 3];
			if (y1 == y2) {
				int from = Math.min(x1, x2);
				int to = Math.max(x1, x2);
				if (from < originX) {
					from = originX;
				}
				if (to > originX + maxCol) {
					to = originX + maxCol;
				}
				int base = y1 * wordsPerRow;
				for (int x = from; x <= to; x++) {
					int col = x - originX;
					rock[base + (col >>> 6)] |= 1L << col;
				}
			} else if (x1 == x2) {
				if (x1 < originX || x1 > originX + maxCol) {
					continue;
				}
				int col = x1 - originX;
				int word = col >>> 6;
				long bit = 1L << col;
				int from = Math.min(y1, y2);
				int to = Math.max(y1, y2);
				for (int y = from; y <= to; y++) {
					rock[y * wordsPerRow + word] |= bit;
				}
			} else {
				throw new IllegalArgumentException("Rock paths must be horizontal or vertical");
			}
		}

		if ((rock[sourceCol >>> 6] >>> sourceCol & 1) != 0) {
			return new int[] {0, 0};
		}

		// Part 1: explicit-stack DFS in grain order. The stack is always a path that
		// descends one row per entry, so the entry at depth d sits in row d.
		long[] settledBits = new long[rows * wordsPerRow];
		int[] stackCol = new int[rows];
		int bottomRow = rows - 1;
		settledBits[sourceCol >>> 6] |= 1L << sourceCol;
		stackCol[0] = sourceCol;
		int depth = 0;
		int settled = 0;
		int part1;
		while (true) {
			int col = stackCol[depth];
			int base = (depth + 1) * wordsPerRow;
			int child = -1;
			int word = base + (col >>> 6);
			if (((rock[word] | settledBits[word]) >>> col & 1) == 0) {
				child = col;
			} else {
				int left = col - 1;
				int leftWord = base + (left >>> 6);
				if (((rock[leftWord] | settledBits[leftWord]) >>> left & 1) == 0) {
					child = left;
				} else {
					int right = col + 1;
					int rightWord = base + (right >>> 6);
					if (((rock[rightWord] | settledBits[rightWord]) >>> right & 1) == 0) {
						child = right;
					}
				}
			}
			if (child >= 0) {
				if (depth + 1 == bottomRow) {
					// First grain past the lowest rock: part 1 is complete.
					part1 = settled;
					break;
				}
				settledBits[base + (child >>> 6)] |= 1L << child;
				stackCol[++depth] = child;
			} else {
				// All three lower neighbours blocked: the grain settles here.
				settled++;
				if (--depth < 0) {
					// Source sealed before any grain overflowed.
					part1 = settled;
					break;
				}
			}
		}

		// Part 2: reachability closure, one row at a time, 64 cells per word.
		long[] above = new long[wordsPerRow];
		long[] current = new long[wordsPerRow];
		above[sourceCol >>> 6] = 1L << sourceCol;
		int part2 = 1;
		for (int row = 1; row < rows; row++) {
			int base = row * wordsPerRow;
			for (int w = 0; w < wordsPerRow; w++) {
				long center = above[w];
				long spread = center | (center << 1) | (center >>> 1);
				if (w > 0) {
					spread |= above[w - 1] >>> 63;
				}
				if (w + 1 < wordsPerRow) {
					spread |= above[w + 1] << 63;
				}
				long reach = spread & ~rock[base + w];
				current[w] = reach;
				part2 += Long.bitCount(reach);
			}
			long[] swap = above;
			above = current;
			current = swap;
		}
		return new int[] {part1, part2};
	}
}
