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
		long contained = 0;
		long overlapping = 0;
		while (in.hasNextLine()) {
			String line = in.nextLine();
			if (line.isEmpty()) {
				continue;
			}
			int firstDash = line.indexOf('-');
			int comma = line.indexOf(',', firstDash + 1);
			int secondDash = line.indexOf('-', comma + 1);
			if (firstDash < 1 || comma < firstDash + 2 || secondDash < comma + 2
					|| secondDash + 1 >= line.length()) {
				throw new IllegalArgumentException("Malformed assignment pair: " + line);
			}
			BigInteger firstStart = number(line, 0, firstDash);
			BigInteger firstEnd = number(line, firstDash + 1, comma);
			BigInteger secondStart = number(line, comma + 1, secondDash);
			BigInteger secondEnd = number(line, secondDash + 1, line.length());
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

	private BigInteger number(String line, int start, int end) {
		return new BigInteger(line.substring(start, end).trim());
	}

	private record Counts(long contained, long overlapping) {
	}
}
