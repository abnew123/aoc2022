package aoc2022;

import java.io.FileNotFoundException;
import java.util.*;

public class Day19 extends DayTemplate {
	int[] O, C, B, M;
	int best;
	HashMap<Long, Integer>[] seen;

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		ArrayList<int[]> blue = new ArrayList<>();
		while (in.hasNextLine()) {
			String[] s = in.nextLine().split(" ");
			blue.add(new int[] {num(s[6]), num(s[12]), num(s[18]), num(s[21]), num(s[27]), num(s[30])});
		}
		int ans = part1 ? 0 : 1;
		for (int i = 0; i < (part1 ? blue.size() : 3); i++) {
			int v = run(blue.get(i), part1 ? 24 : 32);
			if (part1) {
				ans += (i + 1) * v;
			} else {
				ans *= v;
			}
		}
		return "" + ans;
	}

	int run(int[] a, int time) {
		O = new int[] {a[0], a[1], a[2], a[4]};
		C = new int[] {0, 0, a[3], 0};
		B = new int[] {0, 0, 0, a[5]};
		M = new int[] {Math.max(Math.max(a[0], a[1]), Math.max(a[2], a[4])), a[3], a[5], 99};
		best = 0;
		seen = new HashMap[time + 1];
		for (int i = 0; i < seen.length; i++) {
			seen[i] = new HashMap<>();
		}
		dfs(time, 1, 0, 0, 0, 0, 0, 0, 0);
		return best;
	}

	void dfs(int t, int or, int cr, int br, int gr, int o, int c, int b, int g) {
		best = Math.max(best, g + gr * t);
		if (t == 0 || g + gr * t + t * (t - 1) / 2 <= best) {
			return;
		}
		o = cap(o, or, M[0], t);
		c = cap(c, cr, M[1], t);
		b = cap(b, br, M[2], t);
		long key = or | (long) cr << 6 | (long) br << 12 | (long) gr << 18 | (long) o << 24 | (long) c << 34
				| (long) b << 44;
		if (seen[t].getOrDefault(key, -1) >= g) {
			return;
		}
		seen[t].put(key, g);
		int[] r = {or, cr, br, gr}, have = {o, c, b}, bots = {or, cr, br};
		for (int k = 3; k >= 0; k--) {
			if (r[k] >= M[k]) {
				continue;
			}
			int w = Math.max(wait(o, or, O[k]), Math.max(wait(c, cr, C[k]), wait(b, br, B[k])));
			if (w < t) {
				int e = w + 1;
				dfs(t - e, or + (k == 0 ? 1 : 0), cr + (k == 1 ? 1 : 0), br + (k == 2 ? 1 : 0),
						gr + (k == 3 ? 1 : 0), o + or * e - O[k], c + cr * e - C[k], b + br * e - B[k],
						g + gr * e);
				if (k == 3 && w == 0) {
					return;
				}
			}
		}
	}

	int wait(int have, int bots, int cost) {
		return have >= cost ? 0 : bots == 0 ? 1 << 20 : (cost - have + bots - 1) / bots;
	}

	int cap(int have, int bots, int max, int t) {
		return Math.min(have, Math.max(0, max * t - bots * (t - 1)));
	}

	int num(String s) {
		return Integer.parseInt(s);
	}
}
