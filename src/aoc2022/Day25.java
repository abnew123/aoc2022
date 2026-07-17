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

	private String sum(Scanner in) {
		BigInteger total = BigInteger.ZERO;
		while (in.hasNextLine()) {
			String line = in.nextLine().trim();
			if (!line.isEmpty()) {
				total = total.add(parseSnafu(line));
			}
		}
		return formatSnafu(total);
	}

	private BigInteger parseSnafu(String number) {
		BigInteger value = BigInteger.ZERO;
		for (int index = 0; index < number.length(); index++) {
			int digit = switch (number.charAt(index)) {
				case '2' -> 2;
				case '1' -> 1;
				case '0' -> 0;
				case '-' -> -1;
				case '=' -> -2;
				default -> throw new IllegalArgumentException(
						"Invalid SNAFU digit: " + number.charAt(index));
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
