package aoc2022;

import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day24 extends DayTemplate {
	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		List<String> lines = new ArrayList<>();
		while (in.hasNext()) {
			lines.add(in.nextLine());
		}
		int rows = lines.size();
		int cols = lines.get(0).length();
		List<Blizzard> blizzards = new ArrayList<>();
		int startX = -1;
		int startY = -1;
		boolean first = true;
		int endX = -1;
		int endY = -1;
		for (int i = 0; i < lines.size(); i++) {
			for (int j = 0; j < lines.get(0).length(); j++) {
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
					blizzards.add(new Blizzard(i, j, c));
				}
			}
		}
		boolean[][][] blocked = buildBlockedStates(lines, blizzards);
		int trip1 = travel(startX, startY, endX, endY, 0, blocked);
		if (part1) {
			return "" + trip1;
		}
		int trip2 = travel(endX, endY, startX, startY, trip1, blocked);
		int trip3 = travel(startX, startY, endX, endY, trip2, blocked);
		return "" + trip3;
	}

	private boolean[][][] buildBlockedStates(List<String> lines, List<Blizzard> blizzards) {
		int rows = lines.size();
		int cols = lines.get(0).length();
		int period = lcm(rows - 2, cols - 2);
		boolean[][][] blocked = new boolean[period][rows][cols];
		for (int time = 0; time < period; time++) {
			for (int x = 0; x < rows; x++) {
				for (int y = 0; y < cols; y++) {
					blocked[time][x][y] = lines.get(x).charAt(y) == '#';
				}
			}
			for (Blizzard blizzard : blizzards) {
				int x = blizzard.x();
				int y = blizzard.y();
				if (blizzard.direction() == '^') {
					x = 1 + mod(blizzard.x() - 1 - time, rows - 2);
				} else if (blizzard.direction() == 'v') {
					x = 1 + mod(blizzard.x() - 1 + time, rows - 2);
				} else if (blizzard.direction() == '<') {
					y = 1 + mod(blizzard.y() - 1 - time, cols - 2);
				} else if (blizzard.direction() == '>') {
					y = 1 + mod(blizzard.y() - 1 + time, cols - 2);
				}
				blocked[time][x][y] = true;
			}
		}
		return blocked;
	}

	private int travel(int startX, int startY, int endX, int endY, int startTime, boolean[][][] blocked) {
		int rows = blocked[0].length;
		int cols = blocked[0][0].length;
		int period = blocked.length;
		int[] dx = new int[] { -1, 0, 1, 0, 0 };
		int[] dy = new int[] { 0, -1, 0, 1, 0 };
		boolean[][][] visited = new boolean[period][rows][cols];
		ArrayDeque<int[]> queue = new ArrayDeque<>();
		queue.add(new int[] { startX, startY, startTime });
		visited[startTime % period][startX][startY] = true;
		while (!queue.isEmpty()) {
			int[] current = queue.poll();
			int nextTime = current[2] + 1;
			int timeIndex = nextTime % period;
			for (int move = 0; move < dx.length; move++) {
				int nextX = current[0] + dx[move];
				int nextY = current[1] + dy[move];
				if (nextX == endX && nextY == endY) {
					return nextTime;
				}
				if (inBounds(nextX, nextY, rows, cols) && !blocked[timeIndex][nextX][nextY]
						&& !visited[timeIndex][nextX][nextY]) {
					visited[timeIndex][nextX][nextY] = true;
					queue.add(new int[] { nextX, nextY, nextTime });
				}
			}
		}
		return -1;
	}

	private boolean inBounds(int x, int y, int rows, int cols) {
		return x >= 0 && y >= 0 && x < rows && y < cols;
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

	private int mod(int value, int divisor) {
		int result = value % divisor;
		return result < 0 ? result + divisor : result;
	}

	private record Blizzard(int x, int y, char direction) {
	}
}
