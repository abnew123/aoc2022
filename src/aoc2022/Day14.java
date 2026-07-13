package aoc2022;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day14 extends DayTemplate {

	private static final int SOURCE_X = 500;

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		Cave cave = parseCave(in);
		return "" + simulateSand(part1, cave.blocked, cave.minX, cave.maxY, cave.floorY);
	}

	@Override
	public String[] fullSolve(Scanner in) throws FileNotFoundException {
		Cave cave = parseCave(in);
		int[] answers = simulateBoth(cave.blocked, cave.minX, cave.maxY, cave.floorY);
		return new String[] {"" + answers[0], "" + answers[1]};
	}

	private Cave parseCave(Scanner in) {
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

		if (maxY > (Integer.MAX_VALUE - 3) / 2) {
			throw new IllegalArgumentException("Cave is too deep to index");
		}
		int floorY = maxY + 2;
		int radius = floorY - 1;
		long width = 2L * radius + 1;
		if (width > Integer.MAX_VALUE) {
			throw new IllegalArgumentException("Cave is too deep to index");
		}
		int minX = SOURCE_X - radius;
		int maxX = SOURCE_X + radius;

		boolean[][] blocked = new boolean[(int) width][floorY + 1];
		for (int[] path : paths) {
			addPath(path, blocked, minX, maxX);
		}
		return new Cave(blocked, minX, maxY, floorY);
	}

	private int[] parsePath(String line) {
		String[] coords = line.split(" -> ");
		int[] path = new int[coords.length * 2];
		for (int i = 0; i < coords.length; i++) {
			int comma = coords[i].indexOf(',');
			path[2 * i] = Integer.parseInt(coords[i].substring(0, comma));
			path[2 * i + 1] = Integer.parseInt(coords[i].substring(comma + 1));
			if (path[2 * i + 1] < 0) {
				throw new IllegalArgumentException("Rock depth must be nonnegative");
			}
		}
		return path;
	}

	private void addPath(int[] path, boolean[][] blocked, int minX, int maxX) {
		if (path.length == 2) {
			markRock(path[0], path[1], blocked, minX, maxX);
			return;
		}
		for (int i = 2; i < path.length; i += 2) {
			int x1 = path[i - 2];
			int y1 = path[i - 1];
			int x2 = path[i];
			int y2 = path[i + 1];
			if (y1 == y2) {
				int fromX = Math.max(minX, Math.min(x1, x2));
				int toX = Math.min(maxX, Math.max(x1, x2));
				for (int x = fromX; x <= toX; x++) {
					blocked[x - minX][y1] = true;
				}
			} else if (x1 == x2) {
				if (x1 < minX || x1 > maxX) {
					continue;
				}
				for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
					blocked[x1 - minX][y] = true;
				}
			} else {
				throw new IllegalArgumentException("Rock paths must be horizontal or vertical");
			}
		}
	}

	private void markRock(int x, int y, boolean[][] blocked, int minX, int maxX) {
		if (x >= minX && x <= maxX) {
			blocked[x - minX][y] = true;
		}
	}

	private int simulateSand(boolean part1, boolean[][] blocked, int minX, int maxY, int floorY) {
		int answer = 0;
		int sourceX = SOURCE_X - minX;
		int[] pathX = new int[floorY + 1];
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

	private int[] simulateBoth(boolean[][] blocked, int minX, int maxY, int floorY) {
		int settled = 0;
		int part1 = -1;
		int sourceX = SOURCE_X - minX;
		int[] pathX = new int[floorY + 1];
		int[] pathY = new int[pathX.length];
		int pathLength = 1;
		pathX[0] = sourceX;
		pathY[0] = 0;

		while (!blocked[sourceX][0]) {
			int x = pathX[pathLength - 1];
			int y = pathY[pathLength - 1];
			while (true) {
				if (part1 < 0 && y > maxY) {
					part1 = settled;
				}
				if (!isBlocked(blocked, false, x, y + 1, floorY)) {
					y++;
				} else if (!isBlocked(blocked, false, x - 1, y + 1, floorY)) {
					x--;
					y++;
				} else if (!isBlocked(blocked, false, x + 1, y + 1, floorY)) {
					x++;
					y++;
				} else {
					blocked[x][y] = true;
					settled++;
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
		return new int[] {part1 < 0 ? settled : part1, settled};
	}

	private boolean isBlocked(boolean[][] blocked, boolean part1, int x, int y, int floorY) {
		return (!part1 && y == floorY) || blocked[x][y];
	}

	private record Cave(boolean[][] blocked, int minX, int maxY, int floorY) {}
}
