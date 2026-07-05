package aoc2022;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day02 extends DayTemplate {

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		int answer = 0;
		while (in.hasNextLine()) {
			String line = in.nextLine();
			int opp = line.charAt(0) - 'A';
			int me = line.charAt(2) - 'X';
			if (part1) {
				answer += me + 1;
				answer += opp == me ? 3 : (opp + 1) % 3 == me ? 6 : 0;
			} else {
				answer += me * 3;
				answer += (me == 0 ? (opp + 2) % 3 : me == 2 ? (opp + 1) % 3 : opp) + 1;
			}
		}
		return "" + answer;
	}
}
