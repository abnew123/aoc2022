package aoc2022;

import java.io.*;
import java.util.*;

public class Day10 extends DayTemplate {
	static int[] G = {422148690, 959017564, 422068812, 1024344606, 1024344592, 422074958, 623856210,
			203491916, 625758866, 554189342, 959017488, 959017618, 623462988, 1008869918};
	static String L = "ABCEFGHJKLPRUZ";

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







			String ans = "";
			for (int ch = 0; ch < 8; ch++) {
				int g = 0;
				for (int r = 0; r < 6; r++) {
					for (int c = 0; c < 5; c++) {
						g = g * 2 + (screen[r][ch * 5 + c] == '█' ? 1 : 0);
					}
				}
				for (int i = 0; i < G.length; i++) {
					if (G[i] == g) {
						ans += L.charAt(i);
					}
				}
			}
			return ans;
		}
	}
}
