package src.solutions;

import src.meta.DayTemplate;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Day06 extends DayTemplate {

	public String[] fullSolve(Scanner in) {
		String input = in.nextLine();
		return new String[] { "" + findMarker(input, 4), "" + findMarker(input, 14) };
	}

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		String input = in.nextLine();
		int codesize = part1 ? 4 : 14;
		return "" + findMarker(input, codesize);
	}

	private int findMarker(String input, int codesize) {
		int answer = 0;
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
		return answer;
	}
}
