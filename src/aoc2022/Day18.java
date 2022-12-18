package aoc2022;

import java.io.*;
import java.util.*;

public class Day18 extends DayTemplate {

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		int answer = 0;
		int[][][] space = new int[25][25][25];
		while (in.hasNext()) {
			String[] line = in.nextLine().split(",");
			int x = Integer.parseInt(line[0]) + 1;
			int y = Integer.parseInt(line[1]) + 1;
			int z = Integer.parseInt(line[2]) + 1;
			space[x][y][z] = 1;
		}
		space[0][0][0] = 2;
		while (iterate(space, 2, 0, true, false)[0] > 0) {
			System.out.println("hi");
		}
		int[] checkSides = iterate(space, 1, 1, false, true);
		System.out.println(checkSides[0] + " " + checkSides[1]);
		answer += checkSides[0] + checkSides[1];
		if (!part1) {
			answer += iterate(space, 0, 1, false, true)[0];
		}
		return "" + answer;
	}

	public boolean inBounds(int x, int y, int z, int[][][] space) {
		return (x >= 0 && y >= 0 && z >= 0 && x < space.length && y < space[0].length && z < space[0][0].length);
	}

	public int[] iterate(int[][][] space, int target1, int target2, boolean change, boolean decrement) {
		int[] answer = new int[2];
		int[] deltax = new int[] { 1, -1, 0, 0, 0, 0 };
		int[] deltay = new int[] { 0, 0, 1, -1, 0, 0 };
		int[] deltaz = new int[] { 0, 0, 0, 0, 1, -1 };
		for (int i = 0; i < space.length; i++) {
			for (int j = 0; j < space[0].length; j++) {
				for (int k = 0; k < space[0][0].length; k++) {
					if (space[i][j][k] == target1) {
						answer[1] += 6;
						for (int l = 0; l < deltax.length; l++) {
							int newi = i + deltax[l];
							int newj = j + deltay[l];
							int newk = k + deltaz[l];
							if (inBounds(newi, newj, newk, space) && space[newi][newj][newk] == target2) {
								if (change && !decrement) {
									space[newi][newj][newk] = 2;
									answer[0]++;
								}
								if (decrement) {
									answer[0]--;
								}
							}
						}
					}
				}
			}
		}
		return answer;
	}
}
