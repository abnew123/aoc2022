package aoc2022;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day06 extends DayTemplate {

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		int[] markers = findMarkers(in.nextLine());
		return "" + markers[part1 ? 0 : 1];
	}

	@Override
	public String[] fullSolve(Scanner in) throws FileNotFoundException {
		int[] markers = findMarkers(in.nextLine());
		return new String[] { "" + markers[0], "" + markers[1] };
	}

	private int[] findMarkers(String input) {
		int[] markers = new int[2];
		int[] lastSeen = new int[Character.MAX_VALUE + 1];
		int distinctStart = 0;
		for (int index = 0; index < input.length(); index++) {
			char current = input.charAt(index);
			distinctStart = Math.max(distinctStart, lastSeen[current]);
			lastSeen[current] = index + 1;
			int distinctLength = index + 1 - distinctStart;
			if (markers[0] == 0 && distinctLength >= 4) {
				markers[0] = index + 1;
			}
			if (distinctLength >= 14) {
				markers[1] = index + 1;
				break;
			}
		}
		return markers;
	}
}
