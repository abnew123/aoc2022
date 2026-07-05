package aoc2022;

import java.io.FileNotFoundException;
import java.util.*;

public class Day15 extends DayTemplate {
	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		ArrayList<S> s = new ArrayList<>();
		HashSet<Integer> beacons = new HashSet<>();
		while (in.hasNext()) {
			String[] line = in.nextLine().split("=|,|:");
			int x1 = Integer.parseInt(line[1]);
			int y1 = Integer.parseInt(line[3]);
			int x2 = Integer.parseInt(line[5]);
			int y2 = Integer.parseInt(line[7]);
			int d = Math.abs(x1 - x2) + Math.abs(y1 - y2);
			s.add(new S(x1, y1, d));
			if (y2 == 2000000) {
				beacons.add(x2);
			}
		}
		if (part1) {
			ArrayList<int[]> ranges = new ArrayList<>();
			for (S a : s) {
				int w = a.d - Math.abs(a.y - 2000000);
				if (w >= 0) {
					ranges.add(new int[] {a.x - w, a.x + w});
				}
			}
			ranges.sort(Comparator.comparingInt(a -> a[0]));
			long ans = 0;
			int l = ranges.get(0)[0], r = ranges.get(0)[1];
			for (int[] a : ranges) {
				if (a[0] > r) {
					ans += r - l + 1 - count(beacons, l, r);
					l = a[0];
				}
				r = Math.max(r, a[1]);
			}
			return "" + (ans + r - l + 1 - count(beacons, l, r));
		}
		ArrayList<Integer> pos = new ArrayList<>(), neg = new ArrayList<>();
		for (S a : s) {
			pos.add(a.y - a.x + a.d + 1);
			pos.add(a.y - a.x - a.d - 1);
			neg.add(a.x + a.y + a.d + 1);
			neg.add(a.x + a.y - a.d - 1);
		}
		for (int a : pos) {
			for (int b : neg) {
				if ((a + b) % 2 == 0) {
					int x = (b - a) / 2, y = (b + a) / 2;
					if (x >= 0 && y >= 0 && x <= 4000000 && y <= 4000000) {
						boolean ok = true;
						for (S z : s) {
							ok &= Math.abs(x - z.x) + Math.abs(y - z.y) > z.d;
						}
						if (ok) {
							return "" + ((long) x * 4000000 + y);
						}
					}
				}
			}
		}
		return "";
	}

	int count(HashSet<Integer> xs, int l, int r) {
		int n = 0;
		for (int x : xs) {
			if (x >= l && x <= r) {
				n++;
			}
		}
		return n;
	}

	record S(int x, int y, int d) {
	}
}
