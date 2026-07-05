package aoc2022;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day08 extends DayTemplate {

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		List<String> lines = new ArrayList<>();
		while (in.hasNextLine()) {
			lines.add(in.nextLine());
		}
		int rows = lines.size();
		int cols = lines.get(0).length();
		int[][] grid = new int[rows][cols];
		for (int row = 0; row < rows; row++) {
			String line = lines.get(row);
			for (int col = 0; col < cols; col++) {
				grid[row][col] = line.charAt(col) - '0';
			}
		}

		int visibleTrees = 0;
		int bestScore = 0;
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				if (checkVisibility(grid, row, col)) {
					visibleTrees++;
				}
				if (!part1) {
					bestScore = Math.max(bestScore, calculateScore(grid, row, col));
				}
			}
		}
		return "" + (part1 ? visibleTrees : bestScore);
	}

	private boolean checkVisibility(int[][] grid, int row, int col) {
		if (row == 0 || col == 0 || row == grid.length - 1 || col == grid[0].length - 1) {
			return true;
		}
		int height = grid[row][col];
		boolean visible = true;
		for (int r = row + 1; r < grid.length; r++) {
			if (grid[r][col] >= height) {
				visible = false;
				break;
			}
		}
		if (visible) {
			return true;
		}
		visible = true;
		for (int r = row - 1; r >= 0; r--) {
			if (grid[r][col] >= height) {
				visible = false;
				break;
			}
		}
		if (visible) {
			return true;
		}
		visible = true;
		for (int c = col + 1; c < grid[0].length; c++) {
			if (grid[row][c] >= height) {
				visible = false;
				break;
			}
		}
		if (visible) {
			return true;
		}
		for (int c = col - 1; c >= 0; c--) {
			if (grid[row][c] >= height) {
				return false;
			}
		}
		return true;
	}

	private int calculateScore(int[][] grid, int row, int col) {
		int height = grid[row][col];
		int score = 1;
		int distance = 0;
		for (int r = row + 1; r < grid.length; r++) {
			distance++;
			if (grid[r][col] >= height) {
				break;
			}
		}
		score *= distance;
		distance = 0;
		for (int r = row - 1; r >= 0; r--) {
			distance++;
			if (grid[r][col] >= height) {
				break;
			}
		}
		score *= distance;
		distance = 0;
		for (int c = col + 1; c < grid[0].length; c++) {
			distance++;
			if (grid[row][c] >= height) {
				break;
			}
		}
		score *= distance;
		distance = 0;
		for (int c = col - 1; c >= 0; c--) {
			distance++;
			if (grid[row][c] >= height) {
				break;
			}
		}
		return score * distance;
	}
}
