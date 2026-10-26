package aoc2022;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day03 extends DayTemplate {

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		String input = readAll(in);
		long answer = 0;
		int offset = 0;
		if (part1) {
			while (offset < input.length()) {
				int start = offset;
				int end = lineEnd(input, offset);
				offset = afterLineBreak(input, end);
				int length = end - start;
				if ((length & 1) != 0) {
					throw new IllegalArgumentException("Rucksack compartments must be equal-sized");
				}
				int middle = start + length / 2;
				answer += priority(mask(input, start, middle) & mask(input, middle, end));
			}
		} else {
			while (offset < input.length()) {
				int firstStart = offset;
				int firstEnd = lineEnd(input, offset);
				offset = afterLineBreak(input, firstEnd);
				if (offset >= input.length()) {
					throw new IllegalArgumentException("Rucksacks must form groups of three");
				}
				int secondStart = offset;
				int secondEnd = lineEnd(input, offset);
				offset = afterLineBreak(input, secondEnd);
				if (offset >= input.length()) {
					throw new IllegalArgumentException("Rucksacks must form groups of three");
				}
				int thirdStart = offset;
				int thirdEnd = lineEnd(input, offset);
				offset = afterLineBreak(input, thirdEnd);
				answer += priority(mask(input, firstStart, firstEnd)
						& mask(input, secondStart, secondEnd)
						& mask(input, thirdStart, thirdEnd));
			}
		}
		return "" + answer;
	}

	@Override
	public String[] fullSolve(Scanner in) throws FileNotFoundException {
		String input = readAll(in);
		long part1 = 0;
		long part2 = 0;
		long group = 0;
		int groupIndex = 0;
		int offset = 0;
		while (offset < input.length()) {
			int start = offset;
			int end = lineEnd(input, offset);
			offset = afterLineBreak(input, end);
			int length = end - start;
			if ((length & 1) != 0) {
				throw new IllegalArgumentException("Rucksack compartments must be equal-sized");
			}
			long left = 0;
			long right = 0;
			for (int i = start; i < end; i++) {
				long bit = itemBit(input.charAt(i));
				if (i - start < length / 2) {
					left |= bit;
				} else {
					right |= bit;
				}
			}
			part1 += priority(left & right);
			long all = left | right;
			group = groupIndex == 0 ? all : group & all;
			if (++groupIndex == 3) {
				part2 += priority(group);
				groupIndex = 0;
			}
		}
		if (groupIndex != 0) {
			throw new IllegalArgumentException("Rucksacks must form groups of three");
		}
		return new String[] { part1 + "", part2 + "" };
	}

	private String readAll(Scanner in) {
		return in.useDelimiter("\\A").hasNext() ? in.next() : "";
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

	private long mask(String items, int start, int end) {
		long mask = 0;
		for (int i = start; i < end; i++) {
			mask |= itemBit(items.charAt(i));
		}
		return mask;
	}

	private long itemBit(char item) {
		if (item >= 'a' && item <= 'z') {
			return 1L << (item - 'a');
		}
		if (item >= 'A' && item <= 'Z') {
			return 1L << (26 + item - 'A');
		}
		throw new IllegalArgumentException("Rucksack items must be letters");
	}

	private int priority(long items) {
		if (Long.bitCount(items) != 1) {
			throw new IllegalArgumentException("Expected exactly one shared item");
		}
		return Long.numberOfTrailingZeros(items) + 1;
	}
}
