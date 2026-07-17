package aoc2022;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Day24 extends DayTemplate {
	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		List<String> lines = new ArrayList<>();
		while (in.hasNextLine()) {
			lines.add(in.nextLine());
		}
		int rows = lines.size();
		int cols = lines.get(0).length();
		int cells = rows * cols;
		boolean[] walls = new boolean[cells];
		int[] blizzardX = new int[cells];
		int[] blizzardY = new int[cells];
		byte[] blizzardDirections = new byte[cells];
		int blizzardCount = 0;
		int startX = -1;
		int startY = -1;
		boolean first = true;
		int endX = -1;
		int endY = -1;
		for (int i = 0; i < lines.size(); i++) {
			for (int j = 0; j < cols; j++) {
				char c = lines.get(i).charAt(j);
				if (c == '.') {
					if (first) {
						startX = i;
						startY = j;
						first = false;
					}
					endX = i;
					endY = j;
				} else if (c == '^' || c == '>' || c == 'v' || c == '<') {
					blizzardX[blizzardCount] = i;
					blizzardY[blizzardCount] = j;
					blizzardDirections[blizzardCount] = direction(c);
					blizzardCount++;
				} else if (c == '#') {
					walls[i * cols + j] = true;
				}
			}
		}
		Valley valley = buildBlockedStates(rows, cols, walls, blizzardX, blizzardY, blizzardDirections, blizzardCount);
		int start = startX * cols + startY;
		int end = endX * cols + endY;
		byte[] seen = new byte[valley.blocked().length];
		int trip1 = travel(start, end, 0, valley, seen, (byte) 1);
		if (part1) {
			return "" + trip1;
		}
		int trip2 = travel(end, start, trip1, valley, seen, (byte) 2);
		int trip3 = travel(start, end, trip2, valley, seen, (byte) 3);
		return "" + trip3;
	}

	@Override
	public String[] fullSolve(Scanner in) throws FileNotFoundException {
		List<String> lines = new ArrayList<>();
		while (in.hasNextLine()) {
			lines.add(in.nextLine());
		}
		int rows = lines.size();
		int cols = lines.get(0).length();
		int cells = rows * cols;
		boolean[] walls = new boolean[cells];
		int[] blizzardX = new int[cells];
		int[] blizzardY = new int[cells];
		byte[] blizzardDirections = new byte[cells];
		int blizzardCount = 0;
		int startX = -1;
		int startY = -1;
		boolean first = true;
		int endX = -1;
		int endY = -1;
		for (int i = 0; i < lines.size(); i++) {
			for (int j = 0; j < cols; j++) {
				char c = lines.get(i).charAt(j);
				if (c == '.') {
					if (first) {
						startX = i;
						startY = j;
						first = false;
					}
					endX = i;
					endY = j;
				} else if (c == '^' || c == '>' || c == 'v' || c == '<') {
					blizzardX[blizzardCount] = i;
					blizzardY[blizzardCount] = j;
					blizzardDirections[blizzardCount] = direction(c);
					blizzardCount++;
				} else if (c == '#') {
					walls[i * cols + j] = true;
				}
			}
		}
		PackedValley valley = buildPackedStates(rows, cols, walls, blizzardX, blizzardY, blizzardDirections,
				blizzardCount);
		int start = startX * cols + startY;
		int end = endX * cols + endY;
		long[] seen = new long[valley.blocked().length];
		int trip1 = packedTravel(start, end, 0, valley, seen);
		int trip2 = packedTravel(end, start, trip1, valley, seen);
		int trip3 = packedTravel(start, end, trip2, valley, seen);
		return new String[] { trip1 + "", trip3 + "" };
	}

	private PackedValley buildPackedStates(int rows, int cols, boolean[] walls, int[] blizzardX,
			int[] blizzardY, byte[] blizzardDirections, int blizzardCount) {
		int cells = rows * cols;
		int period = lcm(rows - 2, cols - 2);
		int words = (cells + 63) >>> 6;
		long[] wallBits = new long[words];
		for (int cell = 0; cell < cells; cell++) {
			if (walls[cell]) {
				wallBits[cell >>> 6] |= 1L << cell;
			}
		}
		long[] blocked = new long[Math.multiplyExact(period, words)];
		for (int time = 0; time < period; time++) {
			int offset = time * words;
			System.arraycopy(wallBits, 0, blocked, offset, words);
			for (int i = 0; i < blizzardCount; i++) {
				int x = blizzardX[i];
				int y = blizzardY[i];
				int cell = x * cols + y;
				blocked[offset + (cell >>> 6)] |= 1L << cell;
				if (blizzardDirections[i] == 0) {
					x--;
					if (x == 0) x = rows - 2;
				} else if (blizzardDirections[i] == 1) {
					y++;
					if (y == cols - 1) y = 1;
				} else if (blizzardDirections[i] == 2) {
					x++;
					if (x == rows - 1) x = 1;
				} else {
					y--;
					if (y == 0) y = cols - 2;
				}
				blizzardX[i] = x;
				blizzardY[i] = y;
			}
		}
		return new PackedValley(cols, cells, period, words, blocked);
	}

	private int packedTravel(int start, int end, int startTime, PackedValley valley, long[] seen) {
		int words = valley.words();
		long[] frontier = new long[words];
		long[] next = new long[words];
		Arrays.fill(seen, 0);
		frontier[start >>> 6] = 1L << start;
		seen[(startTime % valley.period()) * words + (start >>> 6)] |= 1L << start;
		int endWord = end >>> 6;
		long endBit = 1L << end;
		int tailBits = valley.cells() & 63;
		long tailMask = tailBits == 0 ? -1L : (1L << tailBits) - 1;
		int nextTime = startTime + 1;
		while (true) {
			Arrays.fill(next, 0);
			System.arraycopy(frontier, 0, next, 0, words);
			orShiftHigher(frontier, next, 1);
			orShiftLower(frontier, next, 1);
			orShiftHigher(frontier, next, valley.cols());
			orShiftLower(frontier, next, valley.cols());
			next[words - 1] &= tailMask;
			int offset = (nextTime % valley.period()) * words;
			boolean any = false;
			for (int word = 0; word < words; word++) {
				long reachable = next[word] & ~valley.blocked()[offset + word] & ~seen[offset + word];
				next[word] = reachable;
				seen[offset + word] |= reachable;
				any |= reachable != 0;
			}
			if ((next[endWord] & endBit) != 0) return nextTime;
			if (!any) return -1;
			long[] swap = frontier;
			frontier = next;
			next = swap;
			nextTime++;
		}
	}

	private void orShiftHigher(long[] source, long[] target, int distance) {
		int wordShift = distance >>> 6;
		int bitShift = distance & 63;
		for (int sourceWord = 0; sourceWord < source.length; sourceWord++) {
			long value = source[sourceWord];
			int targetWord = sourceWord + wordShift;
			if (targetWord < target.length) target[targetWord] |= value << bitShift;
			if (bitShift != 0 && targetWord + 1 < target.length) {
				target[targetWord + 1] |= value >>> (64 - bitShift);
			}
		}
	}

	private void orShiftLower(long[] source, long[] target, int distance) {
		int wordShift = distance >>> 6;
		int bitShift = distance & 63;
		for (int sourceWord = 0; sourceWord < source.length; sourceWord++) {
			long value = source[sourceWord];
			int targetWord = sourceWord - wordShift;
			if (targetWord >= 0) target[targetWord] |= value >>> bitShift;
			if (bitShift != 0 && targetWord - 1 >= 0) {
				target[targetWord - 1] |= value << (64 - bitShift);
			}
		}
	}

	private Valley buildBlockedStates(int rows, int cols, boolean[] walls, int[] blizzardX, int[] blizzardY,
			byte[] blizzardDirections, int blizzardCount) {
		int cells = rows * cols;
		int period = lcm(rows - 2, cols - 2);
		boolean[] blocked = new boolean[period * cells];
		for (int time = 0; time < period; time++) {
			int offset = time * cells;
			System.arraycopy(walls, 0, blocked, offset, cells);
			for (int i = 0; i < blizzardCount; i++) {
				int x = blizzardX[i];
				int y = blizzardY[i];
				blocked[offset + x * cols + y] = true;
				if (blizzardDirections[i] == 0) {
					x--;
					if (x == 0) {
						x = rows - 2;
					}
				} else if (blizzardDirections[i] == 1) {
					y++;
					if (y == cols - 1) {
						y = 1;
					}
				} else if (blizzardDirections[i] == 2) {
					x++;
					if (x == rows - 1) {
						x = 1;
					}
				} else {
					y--;
					if (y == 0) {
						y = cols - 2;
					}
				}
				blizzardX[i] = x;
				blizzardY[i] = y;
			}
		}
		return new Valley(cols, cells, period, blocked);
	}

	private int travel(int start, int end, int startTime, Valley valley, byte[] seen, byte seenMark) {
		int cols = valley.cols();
		int cells = valley.cells();
		int period = valley.period();
		boolean[] blocked = valley.blocked();
		int[] frontier = new int[cells];
		int[] next = new int[cells];
		int frontierSize = 1;
		int nextTime = startTime + 1;
		int offset = (nextTime % period) * cells;
		frontier[0] = start;
		seen[(startTime % period) * cells + start] = seenMark;
		while (frontierSize > 0) {
			int nextSize = 0;
			for (int i = 0; i < frontierSize; i++) {
				int position = frontier[i];
				int col = position % cols;
				int candidate;
				int seenIndex;
				if (position >= cols) {
					candidate = position - cols;
					if (candidate == end) {
						return nextTime;
					}
					seenIndex = offset + candidate;
					if (!blocked[seenIndex] && seen[seenIndex] != seenMark) {
						seen[seenIndex] = seenMark;
						next[nextSize++] = candidate;
					}
				}
				if (col > 0) {
					candidate = position - 1;
					if (candidate == end) {
						return nextTime;
					}
					seenIndex = offset + candidate;
					if (!blocked[seenIndex] && seen[seenIndex] != seenMark) {
						seen[seenIndex] = seenMark;
						next[nextSize++] = candidate;
					}
				}
				if (position + cols < cells) {
					candidate = position + cols;
					if (candidate == end) {
						return nextTime;
					}
					seenIndex = offset + candidate;
					if (!blocked[seenIndex] && seen[seenIndex] != seenMark) {
						seen[seenIndex] = seenMark;
						next[nextSize++] = candidate;
					}
				}
				if (col + 1 < cols) {
					candidate = position + 1;
					if (candidate == end) {
						return nextTime;
					}
					seenIndex = offset + candidate;
					if (!blocked[seenIndex] && seen[seenIndex] != seenMark) {
						seen[seenIndex] = seenMark;
						next[nextSize++] = candidate;
					}
				}
				seenIndex = offset + position;
				if (!blocked[seenIndex] && seen[seenIndex] != seenMark) {
					seen[seenIndex] = seenMark;
					next[nextSize++] = position;
				}
			}
			int[] tmp = frontier;
			frontier = next;
			next = tmp;
			frontierSize = nextSize;
			nextTime++;
			offset += cells;
			if (offset == blocked.length) {
				offset = 0;
			}
		}
		return -1;
	}

	private byte direction(char c) {
		if (c == '^') {
			return 0;
		}
		if (c == '>') {
			return 1;
		}
		if (c == 'v') {
			return 2;
		}
		return 3;
	}

	private int lcm(int a, int b) {
		return a / gcd(a, b) * b;
	}

	private int gcd(int a, int b) {
		while (b != 0) {
			int tmp = a % b;
			a = b;
			b = tmp;
		}
		return a;
	}

	private record Valley(int cols, int cells, int period, boolean[] blocked) {
	}

	private record PackedValley(int cols, int cells, int period, int words, long[] blocked) {
	}
}
