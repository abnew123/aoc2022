package aoc2022;

import java.util.Arrays;
import java.util.Scanner;

public class Day05 extends DayTemplate {

	@Override
	public String solve(boolean part1, Scanner in) {
		String[] answers = analyze(readAll(in));
		return part1 ? answers[0] : answers[1];
	}

	@Override
	public String[] fullSolve(Scanner in) {
		return analyze(readAll(in));
	}

	private String readAll(Scanner in) {
		return in.useDelimiter("\\A").hasNext() ? in.next() : "";
	}

	private String[] analyze(String input) {
		int[] lineStarts = new int[16];
		int[] lineEnds = new int[16];
		int lineCount = 0;
		int offset = 0;
		int moveOffset = -1;
		while (offset < input.length()) {
			int start = offset;
			while (offset < input.length() && input.charAt(offset) != '\n'
					&& input.charAt(offset) != '\r') {
				offset++;
			}
			int end = offset;
			if (offset < input.length()) {
				char lineEnding = input.charAt(offset++);
				if (lineEnding == '\r' && offset < input.length()
						&& input.charAt(offset) == '\n') {
					offset++;
				}
			}
			if (isBlank(input, start, end)) {
				moveOffset = offset;
				break;
			}
			if (lineCount == lineStarts.length) {
				lineStarts = Arrays.copyOf(lineStarts, lineCount * 2);
				lineEnds = Arrays.copyOf(lineEnds, lineCount * 2);
			}
			lineStarts[lineCount] = start;
			lineEnds[lineCount++] = end;
		}
		if (lineCount < 2) {
			throw new IllegalArgumentException("Missing crate drawing");
		}
		if (moveOffset < 0) {
			moveOffset = offset;
		}

		int numberStart = lineStarts[lineCount - 1];
		int numberEnd = lineEnds[lineCount - 1];
		int[] labels = new int[16];
		int[] columns = new int[16];
		int stackCount = 0;
		for (int i = numberStart; i < numberEnd;) {
			if (!Character.isDigit(input.charAt(i))) {
				i++;
				continue;
			}
			int start = i;
			long label = 0;
			while (i < numberEnd && Character.isDigit(input.charAt(i))) {
				label = label * 10 + input.charAt(i++) - '0';
				if (label > Integer.MAX_VALUE) {
					throw new IllegalArgumentException("Stack label is too large");
				}
			}
			if (stackCount == labels.length) {
				labels = Arrays.copyOf(labels, stackCount * 2);
				columns = Arrays.copyOf(columns, stackCount * 2);
			}
			labels[stackCount] = (int) label;
			columns[stackCount++] = ((start - numberStart) + (i - numberStart) - 1) >>> 1;
		}
		if (stackCount == 0) {
			throw new IllegalArgumentException("Missing stack labels");
		}
		labels = Arrays.copyOf(labels, stackCount);
		columns = Arrays.copyOf(columns, stackCount);

		int initialCapacity = Math.max(lineCount - 1, 1);
		char[][] reversed = new char[stackCount][initialCapacity];
		char[][] preserved = new char[stackCount][initialCapacity];
		int[] sizes = new int[stackCount];
		for (int row = lineCount - 2; row >= 0; row--) {
			int end = lineEnds[row];
			for (int i = lineStarts[row]; i + 2 < end; i++) {
				if (input.charAt(i) != '[' || input.charAt(i + 2) != ']') {
					continue;
				}
				int stack = nearestColumn(columns, i + 1 - lineStarts[row]);
				char crate = input.charAt(i + 1);
				reversed[stack][sizes[stack]] = crate;
				preserved[stack][sizes[stack]++] = crate;
				i += 2;
			}
		}

		Cursor cursor = new Cursor(input, moveOffset);
		while (cursor.hasMore()) {
			cursor.expect("move");
			int count = cursor.number();
			cursor.expect("from");
			int from = findStack(labels, cursor.number());
			cursor.expect("to");
			int to = findStack(labels, cursor.number());
			if (count > sizes[from]) {
				throw new IllegalArgumentException("Move exceeds source stack");
			}
			if (from == to || count == 0) {
				continue;
			}
			int fromSize = sizes[from];
			int toSize = sizes[to];
			reversed[to] = ensureCapacity(reversed[to], toSize + count);
			preserved[to] = ensureCapacity(preserved[to], toSize + count);
			for (int moved = 0; moved < count; moved++) {
				reversed[to][toSize + moved] = reversed[from][fromSize - 1 - moved];
			}
			System.arraycopy(preserved[from], fromSize - count,
					preserved[to], toSize, count);
			sizes[from] = fromSize - count;
			sizes[to] = toSize + count;
		}

		StringBuilder partOne = new StringBuilder(stackCount);
		StringBuilder partTwo = new StringBuilder(stackCount);
		for (int stack = 0; stack < stackCount; stack++) {
			if (sizes[stack] == 0) {
				throw new IllegalArgumentException("Final stack is empty");
			}
			partOne.append(reversed[stack][sizes[stack] - 1]);
			partTwo.append(preserved[stack][sizes[stack] - 1]);
		}
		return new String[]{partOne.toString(), partTwo.toString()};
	}

	private boolean isBlank(String input, int start, int end) {
		for (int i = start; i < end; i++) {
			if (!Character.isWhitespace(input.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	private int nearestColumn(int[] columns, int column) {
		int nearest = 0;
		int distance = Math.abs(columns[0] - column);
		for (int stack = 1; stack < columns.length; stack++) {
			int candidate = Math.abs(columns[stack] - column);
			if (candidate < distance) {
				distance = candidate;
				nearest = stack;
			}
		}
		return nearest;
	}

	private char[] ensureCapacity(char[] stack, int required) {
		if (required <= stack.length) {
			return stack;
		}
		return Arrays.copyOf(stack, Math.max(required, stack.length * 2));
	}

	private int findStack(int[] labels, int label) {
		for (int stack = 0; stack < labels.length; stack++) {
			if (labels[stack] == label) {
				return stack;
			}
		}
		throw new IllegalArgumentException("Unknown stack " + label);
	}

	private static final class Cursor {
		private final String input;
		private int offset;

		private Cursor(String input, int offset) {
			this.input = input;
			this.offset = offset;
		}

		private boolean hasMore() {
			skipWhitespace();
			return offset < input.length();
		}

		private void expect(String word) {
			skipWhitespace();
			if (!input.regionMatches(offset, word, 0, word.length())) {
				throw new IllegalArgumentException("Expected " + word);
			}
			offset += word.length();
			if (offset < input.length() && !Character.isWhitespace(input.charAt(offset))) {
				throw new IllegalArgumentException("Invalid move instruction");
			}
		}

		private int number() {
			skipWhitespace();
			if (offset == input.length() || !Character.isDigit(input.charAt(offset))) {
				throw new IllegalArgumentException("Expected number");
			}
			long number = 0;
			while (offset < input.length() && Character.isDigit(input.charAt(offset))) {
				number = number * 10 + input.charAt(offset++) - '0';
				if (number > Integer.MAX_VALUE) {
					throw new IllegalArgumentException("Number is too large");
				}
			}
			return (int) number;
		}

		private void skipWhitespace() {
			while (offset < input.length() && Character.isWhitespace(input.charAt(offset))) {
				offset++;
			}
		}
	}
}
