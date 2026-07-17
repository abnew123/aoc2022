package aoc2022;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day03 extends DayTemplate {

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		long answer = 0;
		if (part1) {
			while (in.hasNextLine()) {
				answer += helper1(in.nextLine());
			}
		} else {
			while (in.hasNextLine()) {
				String first = in.nextLine();
				if (!in.hasNextLine()) {
					throw new IllegalArgumentException("Rucksacks must form groups of three");
				}
				String second = in.nextLine();
				if (!in.hasNextLine()) {
					throw new IllegalArgumentException("Rucksacks must form groups of three");
				}
				answer += helper2(first, second, in.nextLine());
			}
		}
		return "" + answer;
	}

	@Override
	public String[] fullSolve(Scanner in) throws FileNotFoundException {
		long part1 = 0;
		long part2 = 0;
		long group = 0;
		int groupIndex = 0;
		while (in.hasNextLine()) {
			String rucksack = in.nextLine();
			if ((rucksack.length() & 1) != 0) {
				throw new IllegalArgumentException("Rucksack compartments must be equal-sized");
			}
			long left = 0;
			long right = 0;
			for (int i = 0; i < rucksack.length(); i++) {
				long bit = itemBit(rucksack.charAt(i));
				if (i < rucksack.length() / 2) {
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

	public int helper1(String rucksack) {
		if ((rucksack.length() & 1) != 0) {
			throw new IllegalArgumentException("Rucksack compartments must be equal-sized");
		}
		int middle = rucksack.length() / 2;
		return priority(mask(rucksack, 0, middle) & mask(rucksack, middle, rucksack.length()));
	}

	public int helper2(String first, String second, String third) {
		return priority(mask(first, 0, first.length()) & mask(second, 0, second.length())
				& mask(third, 0, third.length()));
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
