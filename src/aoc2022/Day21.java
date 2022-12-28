package aoc2022;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day21 extends DayTemplate {

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		List<Monkey2> all = new ArrayList<>();
		List<Monkey2> yelled = new ArrayList<>();
		while (in.hasNext()) {
			String orig = in.nextLine();
			if (orig.contains("root") && !part1) {
				orig = orig.replace('+', '=');
				orig = orig.replace('-', '=');
				orig = orig.replace('/', '=');
				orig = orig.replace('*', '=');
			}
			all.add(new Monkey2(orig, part1));
		}
		return helper(all, yelled, part1) + "";
	}

	public long helper(List<Monkey2> all, List<Monkey2> yelled, boolean part1) {
		int solved = 0;
		boolean[] solves = new boolean[all.size()];
		while (solved < all.size()) {
			for (int i = all.size() - 1; i >= 0; i--) {
				if (solves[i]) {
					continue;
				}
				if (all.get(i).orig.length() <= 10) {
					if (!all.get(i).name.equals("humn") || part1) {
						solved++;
						solves[i] = true;
						continue;
					}
				}
				if (!part1 && all.get(i).name.equals("humn") && all.get(i).val != Long.MIN_VALUE) {
					return all.get(i).val;
				}
				if (!all.get(i).name.equals("humn")) {
					Monkey2 left = null;
					Monkey2 right = null;
					for (int j = 0; j < all.size(); j++) {
						if (all.get(j).name.equals(all.get(i).wait1)) {
							left = all.get(j);
						}
						if (all.get(j).name.equals(all.get(i).wait2)) {
							right = all.get(j);
						}
					}
					boolean valUnknown = (all.get(i).val == Long.MIN_VALUE);
					boolean leftUnknown = (left.val == Long.MIN_VALUE);
					boolean rightUnknown = (right.val == Long.MIN_VALUE);
					int known = valUnknown ? 0 : 1;
					known += (left.val == Long.MIN_VALUE) ? 0 : 1;
					known += (right.val == Long.MIN_VALUE) ? 0 : 1;
					if (known == 3) {
						solved++;
						solves[i] = true;
					}
					if (known == 2 || (!part1 && known == 1 && all.get(i).name.equals("root"))) {
						if (all.get(i).operation == 1) {
							if (leftUnknown) {
								left.val = all.get(i).val - right.val;
							}
							if (rightUnknown) {
								right.val = all.get(i).val - left.val;
							}
							if (valUnknown) {
								all.get(i).val = left.val + right.val;
							}

						}
						if (all.get(i).operation == 2) {
							if (leftUnknown) {
								left.val = all.get(i).val + right.val;
							}
							if (rightUnknown) {
								right.val = left.val - all.get(i).val;
							}
							if (valUnknown) {
								all.get(i).val = left.val - right.val;
							}
						}
						if (all.get(i).operation == 3) {
							if (leftUnknown) {
								left.val = all.get(i).val / right.val;
							}
							if (rightUnknown) {
								right.val = all.get(i).val / left.val;
							}
							if (valUnknown) {
								all.get(i).val = left.val * right.val;
							}
						}
						if (all.get(i).operation == 4) {
							if (leftUnknown) {
								left.val = all.get(i).val * right.val;
							}
							if (rightUnknown) {
								right.val = left.val / all.get(i).val;
							}
							if (valUnknown) {
								all.get(i).val = left.val / right.val;
							}
						}
						if (all.get(i).operation == 5) {
							if (leftUnknown) {
								left.val = right.val;
							}
							if (rightUnknown) {
								right.val = left.val;
							}
							solved++;
							solves[i] = true;
						}
					}
				}
			}
		}
		long answer = 0;
		for (int i = 0; i < all.size(); i++) {
			if (all.get(i).name.equals("root")) {
				answer = all.get(i).val;
			}
		}
		return answer;
	}
}

class Monkey2 {
	long val;
	String name;
	int operation; // +, -, * , /, =
	String wait1;
	String wait2;
	String orig;

	public Monkey2(String line, boolean part1) {
		orig = line;
		name = line.split(":")[0];
		if (line.length() > 10) {
			val = Long.MIN_VALUE;
			wait1 = line.split(" ")[1];
			wait2 = line.split(" ")[3];
			String op = line.split(" ")[2];
			if (op.equals("+")) {
				operation = 1;
			}
			if (op.equals("-")) {
				operation = 2;
			}
			if (op.equals("*")) {
				operation = 3;
			}
			if (op.equals("/")) {
				operation = 4;
			}
			if (op.equals("=")) {
				operation = 5;
			}
		} else {
			val = Long.parseLong(line.split(" ")[1]);
		}
		if (name.equals("humn") && !part1) {
			val = Long.MIN_VALUE;
		}
	}
}