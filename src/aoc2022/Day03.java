package aoc2022;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Day03 extends DayTemplate {

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		int answer = 0;
		if (part1) {
			while (in.hasNext()) {
				answer += helper1(in.nextLine());
			}
		} else {
			while (in.hasNext()) {
				answer += helper2(in.nextLine(), in.nextLine(), in.nextLine());
			}
		}
		return "" + answer;
	}

	public int helper1(String rucksack) {
		char[] letters = rucksack.toCharArray();
		Set<Character> half1 = new HashSet<>();
		Set<Character> half2 = new HashSet<>();
		for (int i = 0; i < letters.length; i++) {
			if (i < letters.length / 2) {
				half1.add(letters[i]);
			} else {
				half2.add(letters[i]);
			}
		}
		half1.retainAll(half2);
		char dupe = half1.iterator().next();
		if (dupe == Character.toUpperCase(dupe)) {
			return 27 + (dupe - 'A');
		} else {
			return 1 + (dupe - 'a');
		}
	}

	public int helper2(String rucksack1, String rucksack2, String rucksack3) {
		char[] letters1 = rucksack1.toCharArray();
		char[] letters2 = rucksack2.toCharArray();
		char[] letters3 = rucksack3.toCharArray();
		Set<Character> elf1 = new HashSet<>();
		Set<Character> elf2 = new HashSet<>();
		Set<Character> elf3 = new HashSet<>();
		for (int i = 0; i < letters1.length; i++) {
			elf1.add(letters1[i]);
		}
		for (int i = 0; i < letters2.length; i++) {
			elf2.add(letters2[i]);
		}
		for (int i = 0; i < letters3.length; i++) {
			elf3.add(letters3[i]);
		}
		elf1.retainAll(elf2);
		elf1.retainAll(elf3);
		char dupe = elf1.iterator().next();
		if (dupe == Character.toUpperCase(dupe)) {
			return 27 + (dupe - 'A');
		} else {
			return 1 + (dupe - 'a');
		}
	}
}
