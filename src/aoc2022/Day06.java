package aoc2022;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Day06 extends DayTemplate {

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		int answer = 0;
		String input = in.nextLine();
		int codesize = part1 ? 4 : 14;
		for (int i = 0; i < input.length() - codesize + 1; i++) {
			Set<Character> code = new HashSet<>();
			for (int j = 0; j < codesize; j++) {
				code.add(input.charAt(i + j));
			}
			if (code.size() == codesize) {
				answer = i + codesize;
				break;
			}
		}
		return "" + answer;
	}
}
