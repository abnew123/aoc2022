package aoc2022;

import java.io.FileNotFoundException;
import java.util.*;

public class Day12 extends DayTemplate {
	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		ArrayList<String> lines = new ArrayList<>();
		while (in.hasNextLine()) {
			lines.add(in.nextLine());
		}
		int R = lines.size(), C = lines.get(0).length(), start = 0, end = 0;
		int[] h = new int[R * C], d = new int[R * C];
		Arrays.fill(d, -1);
		for (int r = 0; r < R; r++) {
			for (int c = 0; c < C; c++) {
				int i = r * C + c;
				char ch = lines.get(r).charAt(c);
				h[i] = ch - 'a';
				if (ch == 'S') {
					start = i;
					h[i] = 0;
				}
				if (ch == 'E') {
					end = i;
					h[i] = 25;
				}
			}
		}
		ArrayDeque<Integer> q = new ArrayDeque<>();
		q.add(end);
		d[end] = 0;
		int[] dirs = {1, -1, C, -C};
		while (!q.isEmpty()) {
			int p = q.remove();
			for (int x : dirs) {
				int n = p + x;
				if (n >= 0 && n < h.length && d[n] < 0 && h[n] >= h[p] - 1
						&& !(x == 1 && p % C == C - 1 || x == -1 && p % C == 0)) {
					d[n] = d[p] + 1;
					q.add(n);
				}
			}
		}
		if (part1) {
			return "" + d[start];
		}
		int ans = 1 << 30;
		for (int i = 0; i < h.length; i++) {
			if (h[i] == 0 && d[i] >= 0) {
				ans = Math.min(ans, d[i]);
			}
		}
		return "" + ans;
	}
}
