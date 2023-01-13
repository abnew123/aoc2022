package aoc2022;

import java.io.*;
import java.util.*;

public class Day22 extends DayTemplate {

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		int answer = 0;
		int[][] grid = new int[1000][1000];
		int xloc = 0;
		int yloc = 0;
		int dir = 0; // right = 0, down = 1, left = 2, up = 3
		List<String> instructions = new ArrayList<>();
		int counter = 0;
		while (in.hasNext()) {
			String line = in.nextLine();
			if (line.contains(".")) {
				for (int i = 0; i < line.length(); i++) {
					grid[counter][i] = (line.charAt(i) == '.') ? 1 : (line.charAt(i) == '#') ? 2 : 0;
				}
			} else {
				if (line.length() > 2) {
					String tmp = "";
					for (int i = 0; i < line.length(); i++) {
						if (line.charAt(i) == 'L' || line.charAt(i) == 'R') {
							instructions.add(tmp);
							tmp = "";
							instructions.add("" + line.charAt(i));
						} else {
							tmp += line.charAt(i);
						}

					}
					if (!tmp.equals("")) {
						instructions.add(tmp);
					}
				}
			}
			counter++;
		}
		for (int i = 0; i < grid[0].length; i++) {
			if (grid[xloc][i] == 1) {
				yloc = i;
				break;
			}
		}
		for (String instruction : instructions) {
			if (instruction.equals("L")) {
				dir = (dir + 3) % 4;
				continue;
			}
			if (instruction.equals("R")) {
				dir = (dir + 1) % 4;
				continue;
			}
			int distance = Integer.parseInt(instruction);
			int[] ydel = new int[] { 1, 0, -1, 0 };
			int[] xdel = new int[] { 0, 1, 0, -1 };
			for (int i = 0; i < distance; i++) {
				int origx = xloc;
				int origy = yloc;
				int origDir = dir;
				int goalx = xloc + xdel[dir];
				int goaly = yloc + ydel[dir];
				if (!part1) {
					if (get(grid, goalx, goaly) == 0) {
						int[] res = helper(xloc, yloc, dir);
						dir = res[0];
						xloc = res[1];
						yloc = res[2];
					} else {
						xloc += xdel[dir];
						yloc += ydel[dir];
					}
				}

				if (part1) {
					xloc += xdel[dir];
					yloc += ydel[dir];
					while (get(grid, xloc, yloc) == 0) {
						xloc += xdel[dir];
						yloc += ydel[dir];
					}
				}
				if (get(grid, xloc, yloc) == 2) {
					xloc = origx;
					yloc = origy;
					dir = origDir;
					break;
				}
			}
			xloc = ((xloc % grid.length) + grid.length) % grid.length;
			yloc = ((yloc % grid[0].length) + grid[0].length) % grid[0].length;
		}
		xloc++;
		yloc++;
		answer = (1000 * xloc) + (4 * yloc) + dir;
		return "" + answer;
	}

	public int[] helper(int xloc, int yloc, int dir) {
		if (xloc < 50) {
			if (yloc >= 50 && yloc < 100) {
				if (dir == 2) {// 2->5
					return new int[] { 0, 149 - xloc, yloc };
				}
				if (dir == 3) {// 2->6
					return new int[] { 0, yloc + 100, 0 };
				}
			}
			if (yloc >= 100) {
				if (dir == 0) {// 1->4
					return new int[] { 2, 149 - xloc, 99 };
				}
				if (dir == 1) {// 1->3
					return new int[] { 2, yloc - 50, 99 };
				}
				if (dir == 3) {// 1->6
					return new int[] { 3, 199, yloc - 100 };
				}
			}
		}
		if (xloc >= 50 && xloc < 100) {
			if (yloc == 50 && dir == 2) {// 3->5
				return new int[] { 1, 100, xloc - 50 };
			}
			if (yloc == 99 && dir == 0) {// 3->1
				return new int[] { 3, 49, xloc + 50 };
			}
		}
		if (xloc >= 100 && xloc < 150) {
			if (yloc < 50) {
				if (dir == 2) {// 5->2
					return new int[] { 0, 149 - xloc, 50 };
				}
				if (dir == 3) {// 5->3
					return new int[] { 0, yloc + 50, 50 };
				}
			}
			if (yloc >= 50) {
				if (dir == 0) {// 4->1
					return new int[] { 2, 149 - xloc, 149 };
				}
				if (dir == 1) {// 4->6
					return new int[] { 2, yloc + 100, 49 };
				}
			}
		}
		if (xloc >= 150) {
			if (dir == 0) {// 6->4
				return new int[] { 3, 149, xloc - 100 };
			}
			if (dir == 1) {// 6->1
				return new int[] { 1, 0, xloc - 100 };
			}
			if (dir == 2) {// 6->2
				return new int[] { 1, 0, xloc - 100 };
			}
		}
		return new int[] { -1, -1, -1 };
	}

	public int get(int[][] grid, int xloc, int yloc) {
		return grid[((xloc % grid.length) + grid.length) % grid.length][((yloc % grid[0].length) + grid[0].length)
				% grid[0].length];
	}
}
