package aoc2022;

import java.io.FileNotFoundException;
import java.util.*;

public class Day08 extends DayTemplate {
	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		ArrayList<String> lines = new ArrayList<>();
		while (in.hasNextLine()) {
			lines.add(in.nextLine());
		}
		int R = lines.size(), C = lines.get(0).length(), seen = 0, best = 0;
		int[] dr = {1, -1, 0, 0}, dc = {0, 0, 1, -1};
		for (int r = 0; r < R; r++) {
			for (int c = 0; c < C; c++) {
				int score = 1, visible = 0, h = lines.get(r).charAt(c);
				for (int d = 0; d < 4; d++) {
					int rr = r, cc = c, dist = 0;
					while (true) {
						rr += dr[d];
						cc += dc[d];
						if (rr < 0 || cc < 0 || rr == R || cc == C) {
							visible = 1;
							break;
						}
						dist++;
						if (lines.get(rr).charAt(cc) >= h) {
							break;
						}
					}
					score *= dist;
				}
				seen += visible;
				best = Math.max(best, score);
			}
		}
		return "" + (part1 ? seen : best);
	}
}
