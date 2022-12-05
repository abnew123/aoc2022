package aoc2022;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class Day5 extends DayTemplate {

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		String answer = "";
		List<Stack<String>> stacks = new ArrayList<>();
		boolean phase1 = true;
		while (in.hasNext()) {
			String line = in.nextLine();
			if (phase1) {
				if (line.equals("")) {
					phase1 = false;
					for (int i = 0; i < stacks.size(); i++) {
						Stack<String> temp = stacks.get(i);
						Collections.reverse(temp);
						stacks.set(i, temp);
					}
					continue;
				}
				if (stacks.size() == 0) {
					for (int i = 0; i < line.length(); i += 4) {
						stacks.add(new Stack<String>());
					}
				}
				for (int i = 1; i < line.length(); i += 4) {
					if (line.charAt(i) != ' ' && !Character.isDigit(line.charAt(i))) {
						stacks.get((i - 1) / 4).push(line.substring(i, i + 1));
					}
				}
			}
			if (!phase1) {
				String[] parts = line.split(" ");
				int numcrates = Integer.parseInt(parts[1]);
				int startstack = Integer.parseInt(parts[3]) - 1;
				int endstack = Integer.parseInt(parts[5]) - 1;
				if (part1) {
					for (int i = 0; i < numcrates; i++) {
						stacks.get(endstack).push(stacks.get(startstack).pop());
					}
				} else {
					Stack<String> temp = new Stack<>();
					for (int i = 0; i < numcrates; i++) {
						temp.push(stacks.get(startstack).pop());
					}
					for (int i = 0; i < numcrates; i++) {
						stacks.get(endstack).push(temp.pop());
					}
				}
			}
		}
		for (int i = 0; i < stacks.size(); i++) {
			answer += stacks.get(i).pop();
		}
		return answer;
	}
}
