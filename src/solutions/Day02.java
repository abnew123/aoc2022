package src.solutions;

import src.meta.DayTemplate;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Day02 extends DayTemplate {

	public String[] fullSolve(Scanner in) {
		int answer1 = 0;
		int answer2 = 0;
		List<String> lines = new ArrayList<>();
		while (in.hasNext()) {
			lines.add(in.nextLine());
		}
		Map<String, Integer> RPS = Map.of("A", 0, "B", 1, "C", 2, "X", 3, "Y", 4, "Z", 5);
		for (String line : lines) {
			int opp = RPS.get(line.split(" ")[0]);
			int me = RPS.get(line.split(" ")[1]);
			answer1 += me % 3 + 1; // from throw
			answer1 += (opp % 3 == me % 3) ? 3 : ((opp + 1) % 3 == me % 3) ? 6 : 0; // from outcome
			answer2 += (me - 3) * 3; // from outcome
			answer2 += ((me == 3) ? (opp + 2) % 3 : (me == 5) ? (opp + 1) % 3 : opp) + 1;// from throw
		}
		return new String[] { "" + answer1, "" + answer2 };
	}

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		int answer = 0;
		List<String> lines = new ArrayList<>();
		while (in.hasNext()) {
			lines.add(in.nextLine());
		}
		Map<String, Integer> RPS = Map.of("A", 0, "B", 1, "C", 2, "X", 3, "Y", 4, "Z", 5);
		for (String line : lines) {
			int opp = RPS.get(line.split(" ")[0]);
			int me = RPS.get(line.split(" ")[1]);
			if (part1) {
				answer += me % 3 + 1; // from throw
				answer += (opp % 3 == me % 3) ? 3 : ((opp + 1) % 3 == me % 3) ? 6 : 0; // from outcome
			} else {
				answer += (me - 3) * 3; // from outcome
				answer += ((me == 3) ? (opp + 2) % 3 : (me == 5) ? (opp + 1) % 3 : opp) + 1;// from throw
			}
		}
		return "" + answer;
	}
}
