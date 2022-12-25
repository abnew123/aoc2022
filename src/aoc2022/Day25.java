package aoc2022;

import java.io.*;
import java.util.*;

public class Day25 extends DayTemplate {

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		long answer = 0;
		List<String> lines = new ArrayList<>();
		while (in.hasNext()) {
			lines.add(in.nextLine());
		}
		for (String line : lines) {
			answer += helper(line);
		}
		String result = "";
		while (answer > 0) {
			long digit = answer % 5;
			if (digit < 3) {
				result = digit + result;
			} else {
				result = ((digit == 3) ? "=" : "-") + result;
				answer += 5;
			}
			answer /= 5;
		}
		return "" + result;
	}

	public long helper(String line) {
		long val = 0;
		for (int i = 0; i < line.length(); i++) {
			if (line.charAt(i) == '-') {
				val -= 1;
			} else {
				if (line.charAt(i) == '=') {
					val -= 2;
				} else {
					val += Integer.parseInt(line.substring(i, i + 1));
				}
			}
			val *= 5;
		}
		return val /= 5;
	}
}
