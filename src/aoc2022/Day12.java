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
					if (part1) {
						distances[i][j] = 0;
					}
				}
			}
		}
		answer = shortestPath(x, y, realGrid, distances, Integer.MAX_VALUE);
		if (!part1) {
			for (int i = 0; i < realGrid.length; i++) {
				for (int j = 0; j < realGrid[0].length; j++) {
					if (realGrid[i][j] == 0) {
						distances[i][j] = 0;
						answer = Math.min(answer, shortestPath(x, y, realGrid, distances, answer));
						distances[i][j] = 10000;
					}
				}
			}
		}
		return "" + answer;
	}

	public boolean inBounds(int x, int y, int[][] grid) {
		return x >= 0 && y >= 0 && x < grid.length && y < grid[0].length;
	}

	public int shortestPath(int x, int y, int[][] realGrid1, int[][] distances1, int currentBest) {
		int[][] realGrid = new int[realGrid1.length][realGrid1[0].length];
		int[][] distances = new int[distances1.length][distances1[0].length];
		for (int i = 0; i < realGrid.length; i++) {
			for (int j = 0; j < realGrid[0].length; j++) {
				realGrid[i][j] = realGrid1[i][j];
				distances[i][j] = distances1[i][j];
			}
		}
		int cycles = 0; // to stop impossible cases
		while (distances[x][y] == 10000 && cycles < Math.min(currentBest, distances.length * distances[0].length)) {
			for (int i = 0; i < realGrid.length; i++) {
				for (int j = 0; j < realGrid[0].length; j++) {
					if (distances[i][j] == 10000) {
						int current = realGrid[i][j];
						if (inBounds(i + 1, j, realGrid) && realGrid[i + 1][j] >= current - 1) {
							distances[i][j] = Math.min(distances[i][j], distances[i + 1][j] + 1);
						}
						if (inBounds(i - 1, j, realGrid) && realGrid[i - 1][j] >= current - 1) {
							distances[i][j] = Math.min(distances[i][j], distances[i - 1][j] + 1);
						}
						if (inBounds(i, j + 1, realGrid) && realGrid[i][j + 1] >= current - 1) {
							distances[i][j] = Math.min(distances[i][j], distances[i][j + 1] + 1);
						}
						if (inBounds(i, j - 1, realGrid) && realGrid[i][j - 1] >= current - 1) {
							distances[i][j] = Math.min(distances[i][j], distances[i][j - 1] + 1);
						}
					}
				}
			}
			cycles++;
		}
		return distances[x][y];
	}
}