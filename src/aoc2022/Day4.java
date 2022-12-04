package aoc2022;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day4 extends DayTemplate{
	
	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		int answer = 0;
		while(in.hasNext()) {
			String[] elfranges = in.nextLine().split(",");
			int elf1start = Integer.parseInt(elfranges[0].split("-")[0]);
			int elf1end = Integer.parseInt(elfranges[0].split("-")[1]);
			int elf2start = Integer.parseInt(elfranges[1].split("-")[0]);
			int elf2end = Integer.parseInt(elfranges[1].split("-")[1]);
			if(part1) {
				if((elf1start >= elf2start && elf2end >= elf1end) || (elf1start <= elf2start && elf2end <= elf1end)) {
					answer++;
				}
			}
			else {
				if((elf1start <= elf2end) && (elf2start <= elf1end)) {
					answer++;
				}
			}
		}
		return "" + answer;
	}
}
