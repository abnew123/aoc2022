package aoc2022;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day01 extends DayTemplate {

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		long[] answers = totals(in);
		return Long.toString(part1 ? answers[0] : answers[1]);
	}

	@Override
	public String[] fullSolve(Scanner in) throws FileNotFoundException {
		long[] answers = totals(in);
		return new String[] { Long.toString(answers[0]), Long.toString(answers[1]) };
	}

	/**
	 * Slurps the whole input once and makes a single linear pass over its
	 * characters, accumulating each elf's calorie total (a blank line is an elf
	 * boundary) while keeping the three largest totals in three registers.
	 * Carriage returns are ignored, so CRLF input works, and the scan appends
	 * two virtual newlines so a trailing line and a trailing elf group are
	 * closed by the same code path as interior ones.
	 *
	 * @return {@code { maximum total, sum of top three totals }}
	 */
	private long[] totals(Scanner in) {
		String input = in.useDelimiter("\\A").hasNext() ? in.next() : "";
		int length = input.length();
		long first = 0;
		long second = 0;
		long third = 0;
		long current = 0;
		long value = 0;
		boolean lineHasDigits = false;
		boolean elfHasCalories = false;
		for (int index = 0; index <= length + 1; index++) {
			char c = index < length ? input.charAt(index) : '\n';
			if (c >= '0' && c <= '9') {
				value = value * 10 + (c - '0');
				lineHasDigits = true;
			} else if (c == '\n') {
				if (lineHasDigits) {
					current += value;
					value = 0;
					lineHasDigits = false;
					elfHasCalories = true;
				} else if (elfHasCalories) {
					if (current > first) {
						third = second;
						second = first;
						first = current;
					} else if (current > second) {
						third = second;
						second = current;
					} else if (current > third) {
						third = current;
					}
					current = 0;
					elfHasCalories = false;
				}
			}
		}
		return new long[] { first, first + second + third };
	}
}
