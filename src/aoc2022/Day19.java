package aoc2022;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day19 extends DayTemplate {

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		int answer = part1 ? 0 : 1;
		List<BluePrint> blueprints = new ArrayList<>();
		while (in.hasNext()) {
			String[] line = in.nextLine().split(" ");
			int o1 = Integer.parseInt(line[6]);
			int o2 = Integer.parseInt(line[12]);
			int o3 = Integer.parseInt(line[18]);
			int o4 = Integer.parseInt(line[27]);
			int c3 = Integer.parseInt(line[21]);
			int ob4 = Integer.parseInt(line[30]);
			blueprints.add(new BluePrint(o1, o2, o3, o4, c3, ob4));
		}
		for (int i = 0; i < (part1 ? blueprints.size() : 3); i++) {
			BluePrint.m = 0;
			if (part1) {
				answer += (i + 1) * blueprints.get(i).result(24, new int[] { 1, 0, 0, 0 }, new int[4]);
			} else {
				answer *= blueprints.get(i).result(32, new int[] { 1, 0, 0, 0 }, new int[4]);
			}
		}
		return "" + answer;
	}
}

class BluePrint {
	static int m = 0;
	int[] oreR = new int[4];
	int[] clayR = new int[4];
	int[] obsidianR = new int[4];
	int[] geodeR = new int[4];
	int maxOre;

	public BluePrint(int o1, int o2, int o3, int o4, int c3, int ob4) {
		oreR[0] = o1;
		clayR[0] = o2;
		obsidianR[0] = o3;
		geodeR[0] = o4;
		obsidianR[1] = c3;
		geodeR[2] = ob4;
		maxOre = Math.max(Math.max(oreR[0], clayR[0]), Math.max(obsidianR[0], geodeR[0]));
	}

	public int result(int min, int[] currR, int[] currRes) {
		if (min == 0) {
			if (currRes[3] > m) {
				m = currRes[3];
			}
			return currRes[3];
		}
		int highestPossible = currRes[3] + (currR[3] * min) + (min * (min + 1)) / 2;
		if (m >= highestPossible) {
			return -1;
		}
		int answer = 0;
		int neededTurns = needed(currRes, currR, geodeR);
		if (neededTurns < min) {
			answer = Math.max(answer,
					result(min - neededTurns - 1, new int[] { currR[0], currR[1], currR[2], currR[3] + 1 },
							step(neededTurns + 1, subtract(currRes, geodeR), currR)));
		}

		if (!(currRes[2] > min * (geodeR[2] - currR[2]))) {
			neededTurns = needed(currRes, currR, obsidianR);
			if (neededTurns < min) {
				answer = Math.max(answer,
						result(min - neededTurns - 1, new int[] { currR[0], currR[1], currR[2] + 1, currR[3] },
								step(neededTurns + 1, subtract(currRes, obsidianR), currR)));
			}
		}
		if (!(currRes[1] > min * (obsidianR[1] - currR[1]))) {
			neededTurns = needed(currRes, currR, clayR);
			if (neededTurns < min) {
				answer = Math.max(answer,
						result(min - neededTurns - 1, new int[] { currR[0], currR[1] + 1, currR[2], currR[3] },
								step(neededTurns + 1, subtract(currRes, clayR), currR)));
			}
		}
		if (!(currRes[0] > min * (maxOre - currR[0]))) {
			neededTurns = needed(currRes, currR, oreR);
			if (neededTurns < min) {
				answer = Math.max(answer,
						result(min - neededTurns - 1, new int[] { currR[0] + 1, currR[1], currR[2], currR[3] },
								step(neededTurns + 1, subtract(currRes, oreR), currR)));
			}
		}
		answer = Math.max(answer, currRes[3] + min * currR[3]);
		return answer;
	}

	public int[] subtract(int[] currRes, int[] R) {
		return step(-1, currRes, R);
	}

	public int[] step(int numSteps, int[] currRes, int[] R) {
		int[] newRes = new int[currRes.length];
		for (int i = 0; i < currRes.length; i++) {
			newRes[i] = currRes[i] + (numSteps * R[i]);
		}
		return newRes;
	}

	public int needed(int[] currRes, int[] R, int[] bot) {
		int max = 0;
		for (int i = 0; i < currRes.length; i++) {
			if (currRes[i] < bot[i]) {
				max = (int) Math.max(Math.ceil((bot[i] - currRes[i]) / (double) R[i]), max);
			}
		}
		return max;
	}
}

class Result {
	int time;
	int val;

	public Result(int time, int val) {
		this.time = time;
		this.val = val;
	}
}