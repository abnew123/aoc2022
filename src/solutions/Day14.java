package src.solutions;

import src.meta.DayTemplate;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day14 extends DayTemplate {

	public String[] fullSolve(Scanner in) {
		boolean grid[][] = new boolean[1000][1000];
		int maxy = 0;
		while (in.hasNext()) {
			String line = in.nextLine();
			maxy = Math.max(addPath(line, grid), maxy);
		}
		// Part 1 fills the grid with sand, so take an independent copy for part 2
		// before any of that mutation happens.
		boolean grid2[][] = new boolean[1000][1000];
		for (int i = 0; i < grid.length; i++) {
			grid2[i] = grid[i].clone();
		}
		int answer1 = 0;
		while (sand(grid, maxy)) {
			answer1++;
		}
		addPath("0," + (maxy + 2) + " -> 999," + (maxy + 2), grid2);
		int answer2 = 0;
		while (sand(grid2, maxy)) {
			answer2++;
		}
		return new String[] { "" + answer1, "" + answer2 };
	}

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		int answer = 0;
		boolean grid[][] = new boolean[1000][1000];
		int maxy = 0;
		while (in.hasNext()) {
			String line = in.nextLine();
			maxy = Math.max(addPath(line, grid), maxy);
		}
		if (!part1) {
			addPath("0," + (maxy + 2) + " -> 999," + (maxy + 2), grid);
		}
		while (sand(grid, maxy)) {
			answer++;
		}
		return "" + answer;
	}

	public int addPath(String line, boolean[][] grid) {
		int ret = 0;
		String[] coords = line.split(" -> ");
		for (int i = 1; i < coords.length; i++) {
			int x1 = Integer.parseInt(coords[i - 1].split(",")[0]);
			int y1 = Integer.parseInt(coords[i - 1].split(",")[1]);
			int x2 = Integer.parseInt(coords[i].split(",")[0]);
			int y2 = Integer.parseInt(coords[i].split(",")[1]);
			for (int j = Math.min(x1, x2); j <= Math.max(x1, x2); j++) {
				for (int k = Math.min(y1, y2); k <= Math.max(y1, y2); k++) {
					grid[j][k] = true;
				}
			}
			ret = Math.max(ret, Math.max(y1, y2));
		}
		return ret;
	}

	public boolean sand(boolean[][] grid, int maxy) {
		if (grid[500][0]) {
			return false;
		}
		int x = 500;
		int y = 0;
		while (y <= maxy + 3) {
			if (!grid[x][y + 1]) {
				y++;
				continue;
			}
			if (!grid[x - 1][y + 1]) {
				y++;
				x--;
				continue;
			}
			if (!grid[x + 1][y + 1]) {
				y++;
				x++;
				continue;
			}
			grid[x][y] = true;
			return true;
		}
		return false;
	}
}
