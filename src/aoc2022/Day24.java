package aoc2022;

import java.io.FileNotFoundException;
import java.util.*;

public class Day24 extends DayTemplate {
	char[][] g;
	int R, C;

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		ArrayList<String> lines = new ArrayList<>();
		while (in.hasNextLine()) {
			lines.add(in.nextLine());
		}
		R = lines.size();
		C = lines.get(0).length();
		g = new char[R][];
		for (int r = 0; r < R; r++) {
			g[r] = lines.get(r).toCharArray();
		}
		int start = lines.get(0).indexOf('.'), end = (R - 1) * C + lines.get(R - 1).indexOf('.');
		int t = trip(start, end, 0);
		return "" + (part1 ? t : trip(start, end, trip(end, start, t)));
	}

	int trip(int start, int end, int t) {
		HashSet<Integer> q = new HashSet<>();
		q.add(start);
		int[] d = {0, 1, -1, C, -C};
		for (;;) {
			HashSet<Integer> n = new HashSet<>();
			t++;
			for (int p : q) {
				for (int x : d) {
					int c = p + x;
					if (c == end) {
						return t;
					}
					if (c >= 0 && c < R * C && free(c, t)) {
						n.add(c);
					}
				}
			}
			q = n;
		}
	}

	boolean free(int p, int t) {
		int r = p / C, c = p % C;
		if (g[r][c] == '#') {
			return false;
		}
		return r == 0 || r == R - 1 || g[1 + Math.floorMod(r - 1 + t, R - 2)][c] != '^'
				&& g[1 + Math.floorMod(r - 1 - t, R - 2)][c] != 'v'
				&& g[r][1 + Math.floorMod(c - 1 + t, C - 2)] != '<'
				&& g[r][1 + Math.floorMod(c - 1 - t, C - 2)] != '>';
	}
}
