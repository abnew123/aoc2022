package aoc2022;

import java.io.FileNotFoundException;
import java.util.*;

public class Day11 extends DayTemplate {
	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		ArrayList<ArrayDeque<Long>> q = new ArrayList<>();
		int[] div = new int[16], yes = new int[16], no = new int[16], val = new int[16];
		char[] op = new char[16];
		int mod = 1, n = 0;
		while (in.hasNextLine()) {
			in.nextLine();
			ArrayDeque<Long> items = new ArrayDeque<>();
			for (String s : in.nextLine().split(": ")[1].split(", ")) {
				items.add(Long.parseLong(s));
			}
			String[] o = in.nextLine().split(" ");
			op[n] = o[6].charAt(0);
			val[n] = o[7].equals("old") ? -1 : Integer.parseInt(o[7]);
			div[n] = num(in.nextLine());
			yes[n] = num(in.nextLine());
			no[n] = num(in.nextLine());
			if (in.hasNextLine()) {
				in.nextLine();
			}
			q.add(items);
			mod *= div[n++];
		}
		long[] count = new long[n];
		for (int r = part1 ? 20 : 10000; r-- > 0;) {
			for (int i = 0; i < n; i++) {
				while (!q.get(i).isEmpty()) {
					long x = q.get(i).remove(), y = val[i] < 0 ? x : val[i];
					x = op[i] == '+' ? x + y : x * y;
					x = part1 ? x / 3 : x % mod;
					q.get(x % div[i] == 0 ? yes[i] : no[i]).add(x);
					count[i]++;
				}
			}
		}
		Arrays.sort(count);
		return "" + count[n - 1] * count[n - 2];
	}

	int num(String s) {
		return Integer.parseInt(s.replaceAll("\\D+", ""));
	}
}
