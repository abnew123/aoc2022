package aoc2022;

import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.Scanner;

public class Day02 extends DayTemplate {

	@Override
	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		Scores scores = score(in);
		return part1 ? scores.part1.toString() : scores.part2.toString();
	}

	@Override
	public String[] fullSolve(Scanner in) {
		Scores scores = score(in);
		return new String[] { scores.part1.toString(), scores.part2.toString() };
	}

	private Scores score(Scanner in) {
		String input = in.useDelimiter("\\A").hasNext() ? in.next() : "";
		ExactTotal part1 = new ExactTotal();
		ExactTotal part2 = new ExactTotal();
		int offset = 0;
		while (offset < input.length()) {
			int lineStart = offset;
			while (offset < input.length() && input.charAt(offset) != '\n'
					&& input.charAt(offset) != '\r') {
				offset++;
			}
			int lineEnd = offset;
			if (offset < input.length()) {
				char ending = input.charAt(offset++);
				if (ending == '\r' && offset < input.length() && input.charAt(offset) == '\n') {
					offset++;
				}
			}
			int index = skipWhitespace(input, lineStart, lineEnd);
			if (index == lineEnd) {
				continue;
			}
			int opponentStart = index;
			index = skipToken(input, index, lineEnd);
			if (index - opponentStart != 1) {
				throw malformed(input, lineStart, lineEnd);
			}
			char opponentCode = input.charAt(opponentStart);
			index = skipWhitespace(input, index, lineEnd);
			int responseStart = index;
			index = skipToken(input, index, lineEnd);
			if (index - responseStart != 1 || skipWhitespace(input, index, lineEnd) != lineEnd) {
				throw malformed(input, lineStart, lineEnd);
			}
			char responseCode = input.charAt(responseStart);
			if (opponentCode < 'A' || opponentCode > 'C'
					|| responseCode < 'X' || responseCode > 'Z') {
				throw malformed(input, lineStart, lineEnd);
			}

			int opponent = opponentCode - 'A';
			int response = responseCode - 'X';
			part1.add(response + 1
					+ (opponent == response ? 3 : (opponent + 1) % 3 == response ? 6 : 0));
			int chosen = response == 0 ? (opponent + 2) % 3
					: response == 2 ? (opponent + 1) % 3 : opponent;
			part2.add(response * 3 + chosen + 1);
		}
		return new Scores(part1, part2);
	}

	private int skipWhitespace(String input, int index, int end) {
		while (index < end && Character.isWhitespace(input.charAt(index))) {
			index++;
		}
		return index;
	}

	private int skipToken(String input, int index, int end) {
		while (index < end && !Character.isWhitespace(input.charAt(index))) {
			index++;
		}
		return index;
	}

	private IllegalArgumentException malformed(String input, int start, int end) {
		return new IllegalArgumentException(
				"Malformed strategy round: " + input.substring(start, end));
	}

	private static final class Scores {
		private final ExactTotal part1;
		private final ExactTotal part2;

		private Scores(ExactTotal part1, ExactTotal part2) {
			this.part1 = part1;
			this.part2 = part2;
		}
	}

	private static final class ExactTotal {
		private long value;
		private BigInteger largeValue;

		private void add(int amount) {
			if (largeValue != null) {
				largeValue = largeValue.add(BigInteger.valueOf(amount));
			} else if (value <= Long.MAX_VALUE - amount) {
				value += amount;
			} else {
				largeValue = BigInteger.valueOf(value).add(BigInteger.valueOf(amount));
			}
		}

		@Override
		public String toString() {
			return largeValue == null ? Long.toString(value) : largeValue.toString();
		}
	}
}
