package aoc2022;

import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.Scanner;

public class Day01 extends DayTemplate {

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		BigInteger[] answers = totals(in);
		return (part1 ? answers[0] : answers[1]).toString();
	}

	@Override
	public String[] fullSolve(Scanner in) throws FileNotFoundException {
		BigInteger[] answers = totals(in);
		return new String[] { answers[0].toString(), answers[1].toString() };
	}

	private BigInteger[] totals(Scanner in) {
		BigInteger[] top = new BigInteger[3];
		BigInteger current = BigInteger.ZERO;
		boolean hasCalories = false;
		while (in.hasNextLine()) {
			String line = in.nextLine();
			if (line.isEmpty()) {
				if (hasCalories) {
					offer(top, current);
					current = BigInteger.ZERO;
					hasCalories = false;
				}
			} else {
				current = current.add(new BigInteger(line));
				hasCalories = true;
			}
		}
		if (hasCalories) {
			offer(top, current);
		}

		BigInteger maximum = top[0] == null ? BigInteger.ZERO : top[0];
		BigInteger firstThree = BigInteger.ZERO;
		for (BigInteger total : top) {
			if (total != null) {
				firstThree = firstThree.add(total);
			}
		}
		return new BigInteger[] { maximum, firstThree };
	}

	private void offer(BigInteger[] top, BigInteger total) {
		for (int index = 0; index < top.length; index++) {
			if (top[index] == null || total.compareTo(top[index]) > 0) {
				for (int shift = top.length - 1; shift > index; shift--) {
					top[shift] = top[shift - 1];
				}
				top[index] = total;
				return;
			}
		}
	}
}
