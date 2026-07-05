package aoc2022;

import java.io.FileNotFoundException;
import java.util.ArrayList;
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
}
