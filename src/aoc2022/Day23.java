package aoc2022;

import java.io.*;
import java.util.*;

public class Day23 extends DayTemplate {

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		int answer = 0;
		List<String> lines = new ArrayList<>();
		List<Coord2> elves = new ArrayList<>();
		while (in.hasNext()) {
			lines.add(in.nextLine());
		}
		for (int i = 0; i < lines.size(); i++) {
			for (int j = 0; j < lines.get(0).length(); j++) {
				if (lines.get(i).charAt(j) == '#') {
					elves.add(new Coord2(i, j));
				}
			}
		}
		if (part1) {
			helper2(elves, 10);
			int maxx, maxy, minx, miny;
			maxx = maxy = minx = miny = -1;
			for (int i = 0; i < elves.size(); i++) {
				maxx = (maxx == -1) ? elves.get(i).x : Math.max(maxx, elves.get(i).x);
				maxy = (maxy == -1) ? elves.get(i).y : Math.max(maxy, elves.get(i).y);
				minx = (minx == -1) ? elves.get(i).x : Math.min(minx, elves.get(i).x);
				miny = (miny == -1) ? elves.get(i).y : Math.min(miny, elves.get(i).y);
			}
			for (int i = minx; i <= maxx; i++) {
				for (int j = miny; j <= maxy; j++) {
					if (!elves.contains(new Coord2(i, j))) {
						answer++;
					}
				}
			}
		} else {
			answer = helper2(elves, 2000);
			int maxx, maxy, minx, miny;
			maxx = maxy = minx = miny = -1;
			for (int i = 0; i < elves.size(); i++) {
				maxx = (maxx == -1) ? elves.get(i).x : Math.max(maxx, elves.get(i).x);
				maxy = (maxy == -1) ? elves.get(i).y : Math.max(maxy, elves.get(i).y);
				minx = (minx == -1) ? elves.get(i).x : Math.min(minx, elves.get(i).x);
				miny = (miny == -1) ? elves.get(i).y : Math.min(miny, elves.get(i).y);
			}
		}
		return "" + answer;
	}

	public List<Coord2> helper(Coord2 elf, int round, int attempt) {
		List<Coord2> answer = new ArrayList<>();
		if ((round + attempt) % 4 == 0) {
			answer.add(new Coord2(elf.x - 1, elf.y - 1));
			answer.add(new Coord2(elf.x - 1, elf.y));
			answer.add(new Coord2(elf.x - 1, elf.y + 1));
		}
		if ((round + attempt) % 4 == 1) {
			answer.add(new Coord2(elf.x + 1, elf.y - 1));
			answer.add(new Coord2(elf.x + 1, elf.y));
			answer.add(new Coord2(elf.x + 1, elf.y + 1));
		}
		if ((round + attempt) % 4 == 2) {
			answer.add(new Coord2(elf.x + 1, elf.y - 1));
			answer.add(new Coord2(elf.x, elf.y - 1));
			answer.add(new Coord2(elf.x - 1, elf.y - 1));
		}
		if ((round + attempt) % 4 == 3) {
			answer.add(new Coord2(elf.x + 1, elf.y + 1));
			answer.add(new Coord2(elf.x, elf.y + 1));
			answer.add(new Coord2(elf.x - 1, elf.y + 1));
		}
		return answer;
	}

	public int helper2(List<Coord2> elves, int rounds) {
		int[][] grid = new int[300][300];
		for (Coord2 elf : elves) {
			grid[elf.x + 100][elf.y + 100] = 1;
		}
		int[] xneighbors = new int[] { -1, 0, 1, -1, 1, -1, 0, 1 };
		int[] yneighbors = new int[] { -1, -1, -1, 0, 0, 1, 1, 1 };
		for (int i = 0; i < rounds; i++) {
			Map<Coord2, Integer> freq = new HashMap<>();
			boolean allNull = true;
			Coord2[] proposed = new Coord2[elves.size()];
			for (int j = 0; j < elves.size(); j++) {
				Coord2 elf = elves.get(j);
				boolean empty = true;
				for (int k = 0; k < xneighbors.length; k++) {
					if (grid[elf.x + xneighbors[k] + 100][elf.y + yneighbors[k] + 100] == 1) {
						empty = false;
					}
				}
				if (empty) {
					proposed[j] = null;
					continue;
				}
				for (int k = 0; k < 4; k++) {
					List<Coord2> possible = helper(elves.get(j), i, k);
					boolean empty2 = true;
					for (Coord2 c : possible) {
						if (grid[c.x + 100][c.y + 100] == 1) {
							empty2 = false;
						}
					}
					if (empty2) {
						proposed[j] = possible.get(1);
						if (freq.keySet().contains(proposed[j])) {
							freq.put(proposed[j], freq.get(proposed[j]) + 1);
						} else {
							freq.put(proposed[j], 1);
						}
						break;
					}
				}
			}
			for (int j = 0; j < proposed.length; j++) {
				if (proposed[j] != null && freq.get(proposed[j]) == 1) {
					elves.set(j, proposed[j]);
					allNull = false;
				}
			}

			if (allNull) {
				return i + 1;
			}
			grid = new int[300][300];
			for (Coord2 elf : elves) {
				grid[elf.x + 100][elf.y + 100] = 1;
			}
		}
		return -1;
	}
}

class Coord2 {
	int x;
	int y;

	public Coord2(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (other.getClass().equals(this.getClass())) {
			Coord2 o = (Coord2) other;
			return x == o.x && y == o.y;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return 1000 * x + y;
	}
}