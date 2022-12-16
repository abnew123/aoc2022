package aoc2022;

import java.io.*;
import java.util.*;

public class Day15 extends DayTemplate {

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		long answer = 0;
		List<Coord> sensors = new ArrayList<>();
		List<Coord> beacons = new ArrayList<>();
		List<Integer> distances = new ArrayList<>();
		List<Integer> alreadyBeacons = new ArrayList<>();
		while (in.hasNext()) {
			String[] line = in.nextLine().split("=|,|:");
			int x1 = Integer.parseInt(line[1]);
			int y1 = Integer.parseInt(line[3]);
			int x2 = Integer.parseInt(line[5]);
			int y2 = Integer.parseInt(line[7]);
			if (y2 == 2000000) {
				alreadyBeacons.add(x2);
			}
			sensors.add(new Coord(x1, y1));
			beacons.add(new Coord(x2, y2));
			distances.add(Math.abs(x1 - x2) + Math.abs(y1 - y2));
		}
		if (part1) {
			for (int x = -5000000; x < 5000000; x++) {
				if (alreadyBeacons.contains(x)) {
					continue;
				}
				boolean possible = checkPossible(x, 2000000, sensors, distances, 0);
				if (!possible) {
					answer++;
				}
			}
		}

		if (!part1) {
			List<Coord> possibilities = iterate(Collections.singletonList(new Coord(0, 0)), 4000000, 200 * 200, sensors,
					distances);
			List<Coord> possibilities2 = iterate(possibilities, 200 * 200, 200, sensors, distances);
			Coord ans = iterate(possibilities2, 200, 1, sensors, distances).get(0);
			answer = ((long) ans.x * 4000000) + ans.y;
		}
		return "" + answer;
	}

	public boolean checkPossible(int x, int y, List<Coord> sensors, List<Integer> distances, int wiggle) {
		for (int i = 0; i < sensors.size(); i++) {
			if (Math.abs(x - sensors.get(i).x) + Math.abs(y - sensors.get(i).y) + wiggle <= distances.get(i)) {
				return false;
			}
		}
		return true;
	}

	public List<Coord> iterate(List<Coord> possibilities, int block1, int block2, List<Coord> sensors,
			List<Integer> distances) {
		List<Coord> possibilities2 = new ArrayList<>();
		for (Coord c : possibilities) {
			for (int x = c.x; x < c.x + block1; x += block2) {
				for (int y = c.y; y < c.y + block1; y += block2) {
					boolean possible = checkPossible(x, y, sensors, distances, (block2 > 1) ? block2 * 2 : 0);
					if (possible) {
						possibilities2.add(new Coord(x, y));
					}
				}
			}
		}
		return possibilities2;
	}
}

class Coord {
	int x;
	int y;

	public Coord(int x1, int y1) {
		x = x1;
		y = y1;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (obj.getClass() != this.getClass()) {
			return false;
		}
		Coord o = (Coord) obj;
		return o.x == x && o.y == y;
	}
}