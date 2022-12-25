package aoc2022;

import java.io.*;
import java.util.*;

public class Day24 extends DayTemplate {

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		int answer = 0;
		List<String> lines = new ArrayList<>();
		Map<Integer, List<CoordTime>> blizzards = new HashMap<>();
		while (in.hasNext()) {
			lines.add(in.nextLine());
		}
		List<CoordTime> initBlizzards = new ArrayList<>();
		CoordTime start = null;
		boolean first = true;
		CoordTime end = null;
		for (int i = 0; i < lines.size(); i++) {
			for (int j = 0; j < lines.get(0).length(); j++) {
				if (lines.get(i).charAt(j) == '.') {
					if (first) {
						start = new CoordTime(i, j, 0, -1);
						first = false;
					}
					end = new CoordTime(i, j, 0, -1);
				}
				if (lines.get(i).charAt(j) == '#') {
					initBlizzards.add(new CoordTime(i, j, 0, -1));
				}
				if (lines.get(i).charAt(j) == '^') {
					initBlizzards.add(new CoordTime(i, j, 0, 1));
				}
				if (lines.get(i).charAt(j) == '>') {
					initBlizzards.add(new CoordTime(i, j, 0, 2));
				}
				if (lines.get(i).charAt(j) == 'v') {
					initBlizzards.add(new CoordTime(i, j, 0, 3));
				}
				if (lines.get(i).charAt(j) == '<') {
					initBlizzards.add(new CoordTime(i, j, 0, 4));
				}
			}
		}
		blizzards.put(0, initBlizzards);
		for (int i = 1; i < 1000; i++) {
			blizzards.put(i, advance(blizzards.get(i - 1), lines));
		}
		Queue<CoordTime> bfs = new LinkedList<>();
		bfs.add(start);
		boolean change = false;
		while (!bfs.isEmpty()) {
			CoordTime next = bfs.poll();
			if (next.x == end.x && next.y == end.y && next.leg % 2 == 0) {
				if (part1 || next.leg == 2) {
					answer = next.time;
					break;
				}
				change = true;
			}
			if (next.x == start.x && next.y == start.y && next.leg == 1) {
				change = true;
			}
			List<CoordTime> nextBlizzards = blizzards.get(next.time + 1);
			int[] deltax = new int[] { -1, 0, 1, 0, 0 };
			int[] deltay = new int[] { 0, -1, 0, 1, 0 };
			for (int i = 0; i < deltax.length; i++) {
				CoordTime candidate = new CoordTime(next.x + deltax[i], next.y + deltay[i], next.time + 1,
						next.direction);
				if (change) {
					candidate.leg = next.leg + 1;
				} else {
					candidate.leg = next.leg;
				}
				if (!nextBlizzards.contains(candidate) && inBounds(candidate, lines)) {
					boolean flag = true;
					for (CoordTime c : bfs) {
						if (c.equals(candidate) && c.leg == candidate.leg) {
							flag = false;
						}
					}
					if (flag) {
						bfs.add(candidate);
					}

				}
			}
			change = false;
		}
		return "" + answer;
	}

	public boolean inBounds(CoordTime candidate, List<String> lines) {
		if (candidate.direction == -1) {
			return (candidate.x >= 0 && candidate.y >= 0 && candidate.x < lines.size()
					&& candidate.y < lines.get(0).length());
		}
		return (candidate.x >= 1 && candidate.y >= 1 && candidate.x < lines.size() - 1
				&& candidate.y < lines.get(0).length() - 1);
	}

	public List<CoordTime> advance(List<CoordTime> current, List<String> lines) {
		List<CoordTime> next = new ArrayList<>();
		for (CoordTime blizzard : current) {
			int deltax = (blizzard.direction == 1) ? -1 : (blizzard.direction == 3) ? 1 : 0;
			int deltay = (blizzard.direction == 4) ? -1 : (blizzard.direction == 2) ? 1 : 0;
			CoordTime candidate = new CoordTime(blizzard.x + deltax, blizzard.y + deltay, blizzard.time + 1,
					blizzard.direction);
			if (inBounds(candidate, lines)) {
				next.add(candidate);
			} else {
				if (candidate.x == 0) {
					next.add(new CoordTime(lines.size() - 2, candidate.y, candidate.time, candidate.direction));
				}
				if (candidate.x == lines.size() - 1) {
					next.add(new CoordTime(1, candidate.y, candidate.time, candidate.direction));
				}
				if (candidate.y == 0) {
					next.add(
							new CoordTime(candidate.x, lines.get(0).length() - 2, candidate.time, candidate.direction));
				}
				if (candidate.y == lines.get(0).length() - 1) {
					next.add(new CoordTime(candidate.x, 1, candidate.time, candidate.direction));
				}
			}

		}
		return next;
	}
}

class CoordTime {
	int x;
	int y;
	int time;
	int direction;
	int leg;

	public CoordTime(int x, int y, int time, int direction) {
		this.x = x;
		this.y = y;
		this.time = time;
		this.direction = direction; // 1 = N, 2 = E, 3 = S, 4 = W, -1 = not a blizzard
		leg = 0;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || !other.getClass().equals(this.getClass())) {
			return false;
		}
		CoordTime o = (CoordTime) other;
		return o.x == x && o.y == y && o.time == time;
	}

	@Override
	public String toString() {
		return x + " " + y + " " + time + " " + direction;
	}
}