package aoc2022;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Day15 extends DayTemplate {

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		long answer = 0;
		List<Coord> sensors = new ArrayList<>();
		List<Coord> beacons = new ArrayList<>();
		List<Integer> distances = new ArrayList<>();
		List<Integer> alreadyBeacons = new ArrayList<>();
		List<Range> ranges = new ArrayList<>();
		while (in.hasNext()) {
			String[] line = in.nextLine().split("=|,|:");
			int x1 = Integer.parseInt(line[1]);
			int y1 = Integer.parseInt(line[3]);
			int x2 = Integer.parseInt(line[5]);
			int y2 = Integer.parseInt(line[7]);
			if (y2 == 2000000 && !alreadyBeacons.contains(x2)) {
				alreadyBeacons.add(x2);
			}
			sensors.add(new Coord(x1, y1));
			beacons.add(new Coord(x2, y2));
			distances.add(Math.abs(x1 - x2) + Math.abs(y1 - y2));
		}
		if (part1) {
			for (int i = 0; i < sensors.size(); i++) {
				int dy = Math.abs(sensors.get(i).y - 2000000);
				if (dy <= distances.get(i)) {
					int dx = distances.get(i) - dy;
					ranges.add(new Range(sensors.get(i).x - dx, sensors.get(i).x + dx));
				}
			}
			Collections.sort(ranges);
			int s = ranges.get(0).start;
			int e = ranges.get(0).end;
			for (Range r : ranges) {
				if (r.start > e) {
					for(int b: alreadyBeacons) {
						if(b >= s && b <= e) {
							answer--;
						}
					}
					answer += e - s + 1;
					s = r.start;
					e = r.end;
				}
				if (r.end > e) {
					e = r.end;
				}
			}
			for(int b: alreadyBeacons) {
				if(b >= s && b <= e) {
					answer--;
				}
			}
			answer += e - s + 1;
		}
		if (!part1) {
			List<Integer> positiveLines = new ArrayList<>();
			List<Integer> negativeLines = new ArrayList<>();
			for (int i = 0; i < sensors.size(); i++) {
				positiveLines.add(sensors.get(i).y - sensors.get(i).x + distances.get(i) + 1);
				positiveLines.add(sensors.get(i).y - sensors.get(i).x - distances.get(i) - 1);
				negativeLines.add(sensors.get(i).x + sensors.get(i).y + distances.get(i) + 1);
				negativeLines.add(sensors.get(i).x + sensors.get(i).y - distances.get(i) - 1);
			}
			for (int a : positiveLines) {
				for (int b : negativeLines) {
					if ((a + b) % 2 == 0) {
						int x = (b - a) / 2;
						int y = (b + a) / 2;
						if (x >= 0 && y >= 0 && x <= 4000000 && y <= 4000000) {
							if (checkPossible(x, y, sensors, distances) >= 0) {
								return "" + (((long) x * 4000000) + y);
							}
						}
					}
				}
			}
		}
		return "" + answer;
	}

	public int checkPossible(int x, int y, List<Coord> sensors, List<Integer> distances) {
		int m = 1000000;
		for (int i = 0; i < sensors.size(); i++) {
			if (Math.abs(x - sensors.get(i).x) + Math.abs(y - sensors.get(i).y) <= distances.get(i)) {
				return -1;
			} else {
				m = Math.min(m, Math.abs(x - sensors.get(i).x) + Math.abs(y - sensors.get(i).y) - distances.get(i));
			}
		}
		return m - 1;
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

class Range implements Comparable<Range> {
	int start;
	int end;

	public Range(int s, int e) {
		start = s;
		end = e;
	}

	@Override
	public int compareTo(Range o) {
		Range other = (Range) o;
		if (other.start != start) {
			return start - other.start;
		} else {
			return end - other.end;
		}
	}

	@Override
	public String toString() {
		return start + " " + end;
	}
}