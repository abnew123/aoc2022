package aoc2022;

import java.io.FileNotFoundException;
import java.util.*;

public class Day14 extends DayTemplate {
	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		HashSet<Long> rock = new HashSet<>();
		int max = 0;
		while (in.hasNextLine()) {
			String[] p = in.nextLine().split(" -> ");
			for (int i = 1; i < p.length; i++) {
				int[] a = xy(p[i - 1]), b = xy(p[i]);
				max = Math.max(max, Math.max(a[1], b[1]));
				for (int x = Math.min(a[0], b[0]); x <= Math.max(a[0], b[0]); x++) {
					for (int y = Math.min(a[1], b[1]); y <= Math.max(a[1], b[1]); y++) {
						rock.add(key(x, y));
					}
				}
			}
		}
		for (int ans = 0;; ans++) {
			int x = 500, y = 0;
			if (rock.contains(key(x, y))) {
				return "" + ans;
			}
			for (;;) {
				if (part1 && y > max) {
					return "" + ans;
				}
				if (free(rock, part1, x, y + 1, max)) {
					y++;
				} else if (free(rock, part1, x - 1, y + 1, max)) {
					x--;
					y++;
				} else if (free(rock, part1, x + 1, y + 1, max)) {
					x++;
					y++;
				} else {
					rock.add(key(x, y));
					break;
				}
			}
		}
	}

	int[] xy(String s) {
		String[] a = s.split(",");
		return new int[] {Integer.parseInt(a[0]), Integer.parseInt(a[1])};
	}

	boolean free(HashSet<Long> rock, boolean part1, int x, int y, int max) {
		return (part1 || y < max + 2) && !rock.contains(key(x, y));
	}

	long key(int x, int y) {
		return (long) x << 32 | y;
	}
}
