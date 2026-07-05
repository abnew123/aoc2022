package aoc2022;

import java.io.FileNotFoundException;
import java.util.*;

public class Day17 extends DayTemplate {
	int[][] P = {{15}, {2, 7, 2}, {7, 4, 4}, {1, 1, 1, 1}, {3, 3}};

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		String jets = in.nextLine();
		ArrayList<Integer> cave = new ArrayList<>();
		HashMap<String, long[]> seen = new HashMap<>();
		int[] high = new int[7];
		long goal = part1 ? 2022 : 1000000000000L, rock = 0, extra = 0;
		int jet = 0;
		boolean skipped = false;
		while (rock < goal) {
			int pi = (int) (rock % 5), x = 2, y = cave.size() + 3;
			for (;;) {
				int nx = x + (jets.charAt(jet) == '<' ? -1 : 1);
				jet = (jet + 1) % jets.length();
				if (ok(P[pi], nx, y, cave)) {
					x = nx;
				}
				if (ok(P[pi], x, y - 1, cave)) {
					y--;
				} else {
					put(P[pi], x, y, cave, high);
					break;
				}
			}
			rock++;
			if (!skipped) {
				String key = key(rock, jet, cave, high);
				long[] old = seen.putIfAbsent(key, new long[] {rock, cave.size()});
				if (old != null) {
					long dr = rock - old[0], dh = cave.size() - old[1], n = (goal - rock) / dr;
					rock += n * dr;
					extra += n * dh;
					skipped = true;
				}
			}
		}
		return "" + (extra + cave.size());
	}

	boolean ok(int[] p, int x, int y, ArrayList<Integer> cave) {
		if (x < 0 || y < 0) {
			return false;
		}
		for (int r = 0; r < p.length; r++) {
			int row = p[r] << x;
			if ((row & ~127) != 0 || y + r < cave.size() && (row & cave.get(y + r)) != 0) {
				return false;
			}
		}
		return true;
	}

	void put(int[] p, int x, int y, ArrayList<Integer> cave, int[] high) {
		for (int r = 0; r < p.length; r++) {
			while (cave.size() <= y + r) {
				cave.add(0);
			}
			int row = p[r] << x;
			cave.set(y + r, cave.get(y + r) | row);
			for (int c = 0; c < 7; c++) {
				if ((row & 1 << c) != 0) {
					high[c] = Math.max(high[c], y + r + 1);
				}
			}
		}
	}

	String key(long rock, int jet, ArrayList<Integer> cave, int[] high) {
		int floor = cave.size();
		StringBuilder s = new StringBuilder(rock % 5 + "," + jet);
		for (int h : high) {
			floor = Math.min(floor, h);
			s.append(',').append(cave.size() - h);
		}
		for (int y = floor; y < cave.size(); y++) {
			s.append(',').append(cave.get(y));
		}
		return s.toString();
	}
}
