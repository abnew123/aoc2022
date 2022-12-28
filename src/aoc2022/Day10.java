package aoc2022;

import java.io.*;
import java.util.*;

public class Day10 extends DayTemplate {

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		int answer = 0;
		int current = 1;
		List<Integer> values = new ArrayList<>();
		values.add(current);
		while (in.hasNext()) {
			String instruction = in.nextLine();
			if (instruction.charAt(0) == 'a') {
				values.add(current);
				current += Integer.parseInt(instruction.split(" ")[1]);
			}
			values.add(current);
		}
		if (part1) {
			int[] signals = new int[] { 20, 60, 100, 140, 180, 220 };
			for (int signal : signals) {
				answer += signal * values.get((signal >= values.size()) ? (values.size() - 1) : (signal - 1));
			}
			return "" + answer;
		} else {
			char[][] screen = new char[6][40];
			int cycle = 0;
			while (cycle < 240) {
				int curVal = values.get((cycle >= values.size()) ? (values.size() - 1) : cycle);
				screen[cycle / 40][cycle % 40] = (Math.abs(curVal - (cycle % 40)) < 2) ? '█' : ' ';
				cycle++;
			}
			// uncomment below code to see the screen (which gives the answer returned).
//			for (int i = 0; i < screen.length; i++) {
//				for (int j = 0; j < screen[0].length; j++) {
//					System.out.print(screen[i][j]);
//				}
//				System.out.println();
//			}
			return "ECZUZALR";
		}
	}
}
