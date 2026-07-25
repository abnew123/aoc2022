package src.solutions;

import src.meta.DayTemplate;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day09 extends DayTemplate {

	public String[] fullSolve(Scanner in) {
		// The two parts share nothing but the input lines: the rope lengths differ, so
		// each part needs its own positions/visited grid.
		List<String> lines = readLines(in);
		return new String[] { "" + simulate(lines, 2), "" + simulate(lines, 10) };
	}

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		return "" + simulate(readLines(in), part1 ? 2 : 10);
	}

	private List<String> readLines(Scanner in) {
		List<String> lines = new ArrayList<>();
		while (in.hasNext()) {
			lines.add(in.nextLine());
		}
		return lines;
	}

	private int simulate(List<String> lines, int size) {
		int answer = 0;
		int[] positionsX = new int[size];
		int[] positionsY = new int[size];
		boolean[][] grid = new boolean[1000][1000];
		grid[positionsX[size - 1] + 500][positionsY[size - 1] + 500] = true;
		int[] xdiff = new int[] { -1, 1, 0, 0 };
		int[] ydiff = new int[] { 0, 0, -1, 1 };
		for (String line : lines) {
			String[] parts = line.split(" ");
			int val = Integer.parseInt(parts[1]);
			int dir = 0;
			switch (parts[0]) {
			case "U":
				dir = 3;
				break;
			case "D":
				dir = 2;
				break;
			case "L":
				dir = 0;
				break;
			case "R":
				dir = 1;
				break;
			}
			for (int i = 0; i < val; i++) {
				positionsX[0] += xdiff[dir];
				positionsY[0] += ydiff[dir];
				for (int j = 1; j < size; j++) {
					boolean touching = true;
					touching &= Math.abs(positionsX[j - 1] - positionsX[j]) < 2;
					touching &= Math.abs(positionsY[j - 1] - positionsY[j]) < 2;
					if (!touching) {
						if (positionsX[j - 1] > positionsX[j]) {
							positionsX[j]++;
						}
						if (positionsX[j - 1] < positionsX[j]) {
							positionsX[j]--;
						}
						if (positionsY[j - 1] < positionsY[j]) {
							positionsY[j]--;
						}
						if (positionsY[j - 1] > positionsY[j]) {
							positionsY[j]++;
						}
					}
				}
				grid[positionsX[size - 1] + 500][positionsY[size - 1] + 500] = true;
				;
			}
		}
		for (boolean[] row : grid) {
			for (boolean square : row) {
				answer += square ? 1 : 0;
			}
		}
		return answer;
	}
}
