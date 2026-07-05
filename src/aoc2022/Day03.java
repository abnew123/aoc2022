package aoc2022;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day03 extends DayTemplate {
	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		int ans = 0;
		if (part1) {
			while (in.hasNext()) {
				String s = in.nextLine();
				long a = mask(s.substring(0, s.length() / 2));
				ans += score(a & mask(s.substring(s.length() / 2)));
			}
		} else {
			while (in.hasNext()) {
				ans += score(mask(in.nextLine()) & mask(in.nextLine()) & mask(in.nextLine()));
			}
		}
		return "" + ans;
	}

	long mask(String s) {
		long m = 0;
		for (char c : s.toCharArray()) {
			m |= 1L << (c <= 'Z' ? c - 'A' + 27 : c - 'a' + 1);
		}
		return m;
	}

	int score(long m) {
		return Long.numberOfTrailingZeros(m);
	}
}
