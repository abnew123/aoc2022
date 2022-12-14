package aoc2022;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

public class Day24 extends DayTemplate {
	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		List<String> lines = new ArrayList<>();
		Map<Integer, List<CoordTime>> blizzards = new HashMap<>();
		while (in.hasNext()) {
			lines.add(in.nextLine());
		}
		int width = lines.size();
		int height = lines.get(0).length();
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
			blizzards.put(i, advance(blizzards.get(i - 1), width, height));
		}
		int trip1 = helper(start, end, width, height, blizzards, 0);
		if (part1) {
			return "" + trip1;
		}
		int trip2 = helper(end, start, width, height, blizzards, trip1);
		int trip3 = helper(start, end, width, height, blizzards, trip2);
		return "" + trip3;
	}

	public boolean inBounds(CoordTime candidate, int width, int height) {
		if (candidate.direction == -1) {
			return (candidate.x >= 0 && candidate.y >= 0 && candidate.x < width && candidate.y < height);
		}
		return (candidate.x >= 1 && candidate.y >= 1 && candidate.x < width - 1 && candidate.y < height - 1);
	}

	public List<CoordTime> advance(List<CoordTime> current, int width, int height) {
		List<CoordTime> next = new ArrayList<>();
		for (CoordTime blizzard : current) {
			int deltax = (blizzard.direction == 1) ? -1 : (blizzard.direction == 3) ? 1 : 0;
			int deltay = (blizzard.direction == 4) ? -1 : (blizzard.direction == 2) ? 1 : 0;
			CoordTime candidate = new CoordTime(blizzard.x + deltax, blizzard.y + deltay, blizzard.time + 1,
					blizzard.direction);
			if (inBounds(candidate, width, height)) {
				next.add(candidate);
			} else {
				if (candidate.x == 0) {
					next.add(new CoordTime(width - 2, candidate.y, candidate.time, candidate.direction));
				}
				if (candidate.x == width - 1) {
					next.add(new CoordTime(1, candidate.y, candidate.time, candidate.direction));
				}
				if (candidate.y == 0) {
					next.add(new CoordTime(candidate.x, height - 2, candidate.time, candidate.direction));
				}
				if (candidate.y == height - 1) {
					next.add(new CoordTime(candidate.x, 1, candidate.time, candidate.direction));
				}
			}
		}
		return next;
	}

	public int helper(CoordTime start, CoordTime end, int width, int height, Map<Integer, List<CoordTime>> blizzards,
			int startTime) {
		Queue<CoordTime> bfs = new LinkedList<>();
		Queue<CoordTime> bfs2 = new LinkedList<>();
		int[][] blizz = new int[width][height];
		int[] deltax = new int[] { -1, 0, 1, 0, 0 };
		int[] deltay = new int[] { 0, -1, 0, 1, 0 };
		int timestamp = startTime;
		bfs.add(new CoordTime(start.x, start.y, startTime, -1));
		while (!bfs.isEmpty() || !bfs2.isEmpty()) {
			if(bfs.isEmpty()) {
				timestamp++;
				bfs.addAll(bfs2);
				bfs2 = new LinkedList<>();
				blizz = new int[width][height];
				List<CoordTime> nextBlizzards = blizzards.get(timestamp + 1);
				for (CoordTime c : nextBlizzards) {
					blizz[c.x][c.y] += 1;
				}
			}
			CoordTime next = bfs.poll();
			if (next.x == end.x && next.y == end.y) {
				return next.time;
			}
			for (int i = 0; i < deltax.length; i++) {
				CoordTime candidate = new CoordTime(next.x + deltax[i], next.y + deltay[i], next.time + 1,
						next.direction);
				if (inBounds(candidate, width, height) && blizz[candidate.x][candidate.y] == 0 
						&& !bfs2.contains(candidate)) {
					bfs2.add(candidate);
				}
			}
		}
		return -1;
	}
}

class CoordTime {
	int x;
	int y;
	int time;
	int direction;

	public CoordTime(int x, int y, int time, int direction) {
		this.x = x;
		this.y = y;
		this.time = time;
		this.direction = direction; // 1 = N, 2 = E, 3 = S, 4 = W, -1 = not a blizzard
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