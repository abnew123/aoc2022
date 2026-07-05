package aoc2022;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day14 extends DayTemplate {

	private static final int SOURCE_X = 500;

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		List<int[]> paths = new ArrayList<>();
		int maxY = 0;
		while (in.hasNextLine()) {
			String line = in.nextLine();
			int[] path = parsePath(line);
			paths.add(path);
			for (int i = 1; i < path.length; i += 2) {
				maxY = Math.max(maxY, path[i]);
			}
		}

		int floorY = maxY + 2;
		int minX = SOURCE_X - floorY - 3;
		int maxX = SOURCE_X + floorY + 3;
		for (int[] path : paths) {
			for (int i = 0; i < path.length; i += 2) {
				minX = Math.min(minX, path[i] - 2);
				maxX = Math.max(maxX, path[i] + 2);
			}
		}

		boolean[][] blocked = new boolean[maxX - minX + 1][floorY + 1];
		for (int[] path : paths) {
			addPath(path, blocked, minX);
		}
		return "" + simulateSand(part1, blocked, minX, maxY, floorY);
	}

	private int[] parsePath(String line) {
		String[] coords = line.split(" -> ");
		int[] path = new int[coords.length * 2];
		for (int i = 0; i < coords.length; i++) {
			int comma = coords[i].indexOf(',');
			path[2 * i] = Integer.parseInt(coords[i].substring(0, comma));
			path[2 * i + 1] = Integer.parseInt(coords[i].substring(comma + 1));
		}
		return path;
	}

	private void addPath(int[] path, boolean[][] blocked, int minX) {
		for (int i = 2; i < path.length; i += 2) {
			int x1 = path[i - 2];
			int y1 = path[i - 1];
			int x2 = path[i];
			int y2 = path[i + 1];
			for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
				for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
					blocked[x - minX][y] = true;
				}
			}
		}
	}

	private int simulateSand(boolean part1, boolean[][] blocked, int minX, int maxY, int floorY) {
		int answer = 0;
		int sourceX = SOURCE_X - minX;
		int[] pathX = new int[blocked.length * blocked[0].length];
		int[] pathY = new int[pathX.length];
		int pathLength = 1;
		pathX[0] = sourceX;
		pathY[0] = 0;

		while (!blocked[sourceX][0]) {
			int x = pathX[pathLength - 1];
			int y = pathY[pathLength - 1];
			while (true) {
				if (part1 && y > maxY) {
					return answer;
				}
				if (!isBlocked(blocked, part1, x, y + 1, floorY)) {
					y++;
				} else if (!isBlocked(blocked, part1, x - 1, y + 1, floorY)) {
					x--;
					y++;
				} else if (!isBlocked(blocked, part1, x + 1, y + 1, floorY)) {
					x++;
					y++;
				} else {
					blocked[x][y] = true;
					answer++;
					pathLength--;
					if (pathLength == 0) {
						pathLength = 1;
						pathX[0] = sourceX;
						pathY[0] = 0;
					}
					break;
				}
				pathX[pathLength] = x;
				pathY[pathLength] = y;
				pathLength++;
			}
		}
		return answer;
	}

	private boolean isBlocked(boolean[][] blocked, boolean part1, int x, int y, int floorY) {
		return (!part1 && y == floorY) || blocked[x][y];
	}
}
