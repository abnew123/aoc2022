package aoc2022;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Day1 extends DayTemplate{
	
	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		int temp = 0;
		List<Integer> elves = new ArrayList<>();
		while(in.hasNext()) {
			String a = in.nextLine();
			if(a.equals("")) {
				elves.add(temp);
				temp = 0;
			}
			else {
				temp+= Integer.parseInt(a);
			}
		}
		if(temp != 0) {
			elves.add(temp);
		}
		Collections.sort(elves,Collections.reverseOrder());
		return "" + (part1?elves.get(0):(elves.get(0) + elves.get(1) + elves.get(2)));
	}
	
}
