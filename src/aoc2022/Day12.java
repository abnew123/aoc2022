package aoc2022;

import java.io.*;
import java.util.*;

public class Day12 extends DayTemplate {

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		int answer = 0;
		List<List<Integer>> grid = new ArrayList<>();
		int x = 0;
		int y = 0;
		while (in.hasNext()) {
			char[] line = in.nextLine().toCharArray();
			List<Integer> intLine = new ArrayList<>();
			for (char l : line) {
				if (l == 'E') {
					intLine.add(100);
				} else {
					if (l == 'S') {
						intLine.add(-1);
					} else {
						intLine.add(l - 'a');
					}
				}
			}
			grid.add(intLine);
		}
		int[][] realGrid = new int[grid.size()][grid.get(0).size()];
		int[][] distances = new int[grid.size()][grid.get(0).size()];
		for (int i = 0; i < grid.size(); i++) {
			for (int j = 0; j < grid.get(0).size(); j++) {
				realGrid[i][j] = grid.get(i).get(j);
				distances[i][j] = 10000;
				if (grid.get(i).get(j) == 100) {
					x = i;
					y = j;
					realGrid[i][j] = 'z' - 'a';
				}
				if (grid.get(i).get(j) == -1) {
					realGrid[i][j] = 'a' - 'a';
					distances[i][j] = 0;
				}
			}
		}
		if (!part1) {
			for (int i = 0; i < realGrid.length; i++) {
				for (int j = 0; j < realGrid[0].length; j++) {
					if (realGrid[i][j] == 0) {
						distances[i][j] = 0;
					}
				}
			}
		}
		int cycles = 0; // to stop impossible cases
		while (distances[x][y] == 10000 && cycles < distances.length * distances[0].length) {
			for (int i = 0; i < realGrid.length; i++) {
				for (int j = 0; j < realGrid[0].length; j++) {
					if (distances[i][j] == 10000) {
						int current = realGrid[i][j];
						if (inBounds(i + 1, j, realGrid) && realGrid[i + 1][j] >= current - 1
								&& distances[i + 1][j] == cycles) {
							distances[i][j] = cycles + 1;
						}
						if (inBounds(i - 1, j, realGrid) && realGrid[i - 1][j] >= current - 1
								&& distances[i - 1][j] == cycles) {
							distances[i][j] = cycles + 1;
						}
						if (inBounds(i, j + 1, realGrid) && realGrid[i][j + 1] >= current - 1
								&& distances[i][j + 1] == cycles) {
							distances[i][j] = cycles + 1;
						}
						if (inBounds(i, j - 1, realGrid) && realGrid[i][j - 1] >= current - 1
								&& distances[i][j - 1] == cycles) {
							distances[i][j] = cycles + 1;
						}
					}
				}
			}
			cycles++;
		}
		answer = distances[x][y];
		return "" + answer;
	}

	public boolean inBounds(int x, int y, int[][] grid) {
		return x >= 0 && y >= 0 && x < grid.length && y < grid[0].length;
	}
}