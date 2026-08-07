package aoc2022;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Day08 extends DayTemplate {

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		int[][] grid = parse(in);
		if (part1) {
			return "" + countVisible(grid);
		}
		return "" + bestScenicScore(grid);
	}

	@Override
	public String[] fullSolve(Scanner in) throws FileNotFoundException {
		int[][] grid = parse(in);
		int visibleTrees = 0;
		int bestScore = 0;
		for (int row = 0; row < grid.length; row++) {
			for (int col = 0; col < grid[0].length; col++) {
				if (checkVisibility(grid, row, col)) {
					visibleTrees++;
				}
				bestScore = Math.max(bestScore, calculateScore(grid, row, col));
			}
		}
		return new String[] { "" + visibleTrees, "" + bestScore };
	}

	private int[][] parse(Scanner in) {
		String input = in.useDelimiter("\\A").hasNext() ? in.next() : "";
		int[] lineStarts = new int[16];
		int[] lineEnds = new int[16];
		int lineCount = 0;
		int offset = 0;
		while (offset < input.length()) {
			int start = offset;
			while (offset < input.length() && input.charAt(offset) != '\n'
					&& input.charAt(offset) != '\r') {
				offset++;
			}
			int end = offset;
			if (offset < input.length()) {
				char ending = input.charAt(offset++);
				if (ending == '\r' && offset < input.length() && input.charAt(offset) == '\n') {
					offset++;
				}
			}
			if (lineCount == lineStarts.length) {
				lineStarts = Arrays.copyOf(lineStarts, lineCount * 2);
				lineEnds = Arrays.copyOf(lineEnds, lineCount * 2);
			}
			lineStarts[lineCount] = start;
			lineEnds[lineCount++] = end;
		}
		if (lineCount == 0) {
			throw new IndexOutOfBoundsException(0);
		}
		int rows = lineCount;
		int cols = lineEnds[0] - lineStarts[0];
		int[][] grid = new int[rows][cols];
		for (int row = 0; row < rows; row++) {
			int start = lineStarts[row];
			int length = lineEnds[row] - start;
			for (int col = 0; col < cols; col++) {
				if (col >= length) {
					throw new StringIndexOutOfBoundsException(col);
				}
				grid[row][col] = input.charAt(start + col) - '0';
			}
		}
		return grid;
	}

	private int countVisible(int[][] grid) {
		int visibleTrees = 0;
		for (int row = 0; row < grid.length; row++) {
			for (int col = 0; col < grid[0].length; col++) {
				if (checkVisibility(grid, row, col)) {
					visibleTrees++;
				}
			}
		}
		return visibleTrees;
	}

	private int bestScenicScore(int[][] grid) {
		int bestScore = 0;
		for (int row = 0; row < grid.length; row++) {
			for (int col = 0; col < grid[0].length; col++) {
				bestScore = Math.max(bestScore, calculateScore(grid, row, col));
			}
		}
		return bestScore;
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
