package aoc2022;

import java.io.*;
import java.util.Map;
import java.util.*;

public class Day10 extends DayTemplate {
	private static final Map<String, Character> LETTERS = Map.ofEntries(
			Map.entry(".##..|#..#.|#..#.|####.|#..#.|#..#.", 'A'),
			Map.entry("###..|#..#.|#..#.|###..|#..#.|###..", 'B'),
			Map.entry(".##..|#..#.|#....|#....|#..#.|.##..", 'C'),
			Map.entry("####.|#....|###..|#....|#....|####.", 'E'),
			Map.entry("####.|#....|###..|#....|#....|#....", 'F'),
			Map.entry(".##..|#..#.|#....|#.##.|#..#.|.###.", 'G'),
			Map.entry("#..#.|#..#.|####.|#..#.|#..#.|#..#.", 'H'),
			Map.entry("..##.|...#.|...#.|...#.|#..#.|.##..", 'J'),
			Map.entry("#..#.|#.#..|##...|#.#..|#.#..|#..#.", 'K'),
			Map.entry("#....|#....|#....|#....|#....|####.", 'L'),
			Map.entry("###..|#..#.|#..#.|###..|#....|#....", 'P'),
			Map.entry("###..|#..#.|#..#.|###..|#.#..|#..#.", 'R'),
			Map.entry("#..#.|#..#.|#..#.|#..#.|#..#.|.##..", 'U'),
			Map.entry("####.|...#.|..#..|.#...|#....|####.", 'Z'));

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
			return readScreen(screen);
		}
	}

	private String readScreen(char[][] screen) {
		StringBuilder result = new StringBuilder();
		for (int letter = 0; letter < screen[0].length / 5; letter++) {
			StringBuilder glyph = new StringBuilder();
			for (int row = 0; row < screen.length; row++) {
				if (row > 0) {
					glyph.append('|');
				}
				for (int col = letter * 5; col < letter * 5 + 5; col++) {
					glyph.append(screen[row][col] == '█' ? '#' : '.');
				}
			}
			Character decoded = LETTERS.get(glyph.toString());
			if (decoded == null) {
				throw new IllegalStateException("Unknown CRT glyph: " + glyph);
			}
			result.append(decoded);
		}
		return result.toString();
	}
}
