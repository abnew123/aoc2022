package aoc2022;

import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.Scanner;

public class Day25 extends DayTemplate {

	private static final BigInteger FIVE = BigInteger.valueOf(5);

	@Override
	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		return sum(in);
	}

	@Override
	public String[] fullSolve(Scanner in) {
		String answer = sum(in);
		return new String[] { answer, answer };
	}

	/** One slurp, manual scan: sums the trimmed nonblank lines as SNAFU numbers. */
	private String sum(Scanner in) {
		String text = in.useDelimiter("\\A").hasNext() ? in.next() : "";
		BigInteger total = BigInteger.ZERO;
		int n = text.length();
		int i = 0;
		while (i < n) {
			int lineStart = i;
			while (i < n && text.charAt(i) != '\n' && text.charAt(i) != '\r') {
				i++;
			}
			int lineEnd = i;
			if (i < n) {
				if (text.charAt(i) == '\r' && i + 1 < n && text.charAt(i + 1) == '\n') {
					i++;
				}
				i++;
			}
			while (lineStart < lineEnd && text.charAt(lineStart) <= ' ') {
				lineStart++;
			}
			while (lineEnd > lineStart && text.charAt(lineEnd - 1) <= ' ') {
				lineEnd--;
			}
			if (lineStart < lineEnd) {
				total = total.add(parseSnafu(text, lineStart, lineEnd));
			}
		}
		return formatSnafu(total);
	}

	private BigInteger parseSnafu(String number) {
		return parseSnafu(number, 0, number.length());
	}

	private BigInteger parseSnafu(String text, int from, int to) {
		BigInteger value = BigInteger.ZERO;
		for (int index = from; index < to; index++) {
			int digit = switch (text.charAt(index)) {
				case '2' -> 2;
				case '1' -> 1;
				case '0' -> 0;
				case '-' -> -1;
				case '=' -> -2;
				default -> throw new IllegalArgumentException("Invalid SNAFU digit");
			};
			value = value.multiply(FIVE).add(BigInteger.valueOf(digit));
		}
		return value;
	}

	private String formatSnafu(BigInteger value) {
		if (value.signum() == 0) {
			return "0";
		}
		StringBuilder result = new StringBuilder();
		while (value.signum() != 0) {
			BigInteger[] quotientAndRemainder = value.divideAndRemainder(FIVE);
			int digit = quotientAndRemainder[1].intValue();
			value = quotientAndRemainder[0];
			if (digit > 2) {
				digit -= 5;
				value = value.add(BigInteger.ONE);
			} else if (digit < -2) {
				digit += 5;
				value = value.subtract(BigInteger.ONE);
			}
			result.append(switch (digit) {
				case -2 -> '=';
				case -1 -> '-';
				case 0 -> '0';
				case 1 -> '1';
				case 2 -> '2';
				default -> throw new AssertionError("Unbalanced SNAFU digit");
			});
		}
		return result.reverse().toString();
	}

	public long helper(String line) {
		return parseSnafu(line).longValueExact();
	}
}
