package aoc2022;

import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.Scanner;

public class Day04 extends DayTemplate {

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		Counts counts = count(in);
		return "" + (part1 ? counts.contained : counts.overlapping);
	}

	@Override
	public String[] fullSolve(Scanner in) throws FileNotFoundException {
		Counts counts = count(in);
		return new String[] { counts.contained + "", counts.overlapping + "" };
	}

	private Counts count(Scanner in) {
		String input = in.useDelimiter("\\A").hasNext() ? in.next() : "";
		long contained = 0;
		long overlapping = 0;
		int offset = 0;
		while (offset < input.length()) {
			int lineStart = offset;
			int lineEnd = lineEnd(input, offset);
			offset = afterLineBreak(input, lineEnd);
			if (lineStart == lineEnd) {
				continue;
			}
			int length = lineEnd - lineStart;
			int firstDash = relativeIndexOf(input, '-', 0, lineStart, lineEnd);
			int comma = relativeIndexOf(input, ',', firstDash + 1, lineStart, lineEnd);
			int secondDash = relativeIndexOf(input, '-', comma + 1, lineStart, lineEnd);
			if (firstDash < 1 || comma < firstDash + 2 || secondDash < comma + 2
					|| secondDash + 1 >= length) {
				throw new IllegalArgumentException(
						"Malformed assignment pair: " + input.substring(lineStart, lineEnd));
			}
			BigInteger firstStart = number(input, lineStart, lineStart + firstDash);
			BigInteger firstEnd = number(input, lineStart + firstDash + 1, lineStart + comma);
			BigInteger secondStart = number(input, lineStart + comma + 1, lineStart + secondDash);
			BigInteger secondEnd = number(input, lineStart + secondDash + 1, lineEnd);
			if (firstStart.compareTo(firstEnd) > 0 || secondStart.compareTo(secondEnd) > 0) {
				throw new IllegalArgumentException("Assignment ranges must be ascending");
			}
			if ((firstStart.compareTo(secondStart) <= 0 && firstEnd.compareTo(secondEnd) >= 0)
					|| (secondStart.compareTo(firstStart) <= 0 && secondEnd.compareTo(firstEnd) >= 0)) {
				contained++;
			}
			if (firstStart.compareTo(secondEnd) <= 0 && secondStart.compareTo(firstEnd) <= 0) {
				overlapping++;
			}
		}
		return new Counts(contained, overlapping);
	}

	private int lineEnd(String input, int start) {
		while (start < input.length() && input.charAt(start) != '\n'
				&& input.charAt(start) != '\r') {
			start++;
		}
		return start;
	}

	private int afterLineBreak(String input, int end) {
		if (end < input.length()) {
			char ending = input.charAt(end++);
			if (ending == '\r' && end < input.length() && input.charAt(end) == '\n') {
				end++;
			}
		}
		return end;
	}

	private int relativeIndexOf(String input, char target, int fromRelative, int lineStart,
			int lineEnd) {
		int index = lineStart + Math.max(fromRelative, 0);
		while (index < lineEnd && input.charAt(index) != target) {
			index++;
		}
		return index < lineEnd ? index - lineStart : -1;
	}

	private BigInteger number(String input, int start, int end) {
		while (start < end && input.charAt(start) <= ' ') {
			start++;
		}
		while (end > start && input.charAt(end - 1) <= ' ') {
			end--;
		}
		return new BigInteger(input.substring(start, end));
	}

	private record Counts(long contained, long overlapping) {
	}
}
