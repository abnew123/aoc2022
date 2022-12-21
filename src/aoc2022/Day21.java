package aoc2022;

import java.io.*;
import java.util.*;

public class Day21 extends DayTemplate {

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		List<Monkey2> all = new ArrayList<>();
		List<Monkey2> yelled = new ArrayList<>();
		while (in.hasNext()) {
			all.add(new Monkey2(in.nextLine(), part1));
		}
		long[] res = helper(all, yelled, part1);
		if (res != null) {
			return "" + res[2];
		}
		int counter = 0;
		long low = 0;
		long high = 13238229598990L;
		while (counter < 100) {
			long med = (low + high) / 2;
			List<Monkey2> newAll = new ArrayList<>();
			for (int i = 0; i < all.size(); i++) {
				newAll.add(new Monkey2(all.get(i).orig, part1));
			}
			for (int i = 0; i < newAll.size(); i++) {
				if (newAll.get(i).name.equals("human")) {
					newAll.get(i).name = "humn";
					newAll.get(i).val = med;
				}
			}
			List<Monkey2> newYelled = new ArrayList<>();
			for (int i = 0; i < yelled.size(); i++) {
				newYelled.add(new Monkey2(yelled.get(i).orig, part1));
			}
			newYelled.addAll(yelled);
			long[] res2 = helper(newAll, newYelled, part1);
			if (res2[0] > res2[1]) {
				low = med;
			}
			if (res2[0] < res2[1]) {
				high = med;
			}
			if (res2[0] == res2[1]) {
				return "" + med;
			}
			counter++;
		}
		return "";
	}

	public long[] helper(List<Monkey2> all, List<Monkey2> yelled, boolean part1) {
		int size = all.size();
		int prevSize = -1;
		while (size != prevSize) {
			prevSize = all.size();
			for (int i = all.size() - 1; i >= 0; i--) {
				long left = Long.MIN_VALUE;
				long right = Long.MIN_VALUE;
				for (int j = 0; j < yelled.size(); j++) {
					if (yelled.get(j).name.equals(all.get(i).wait1)) {
						left = yelled.get(j).val;
					}
					if (yelled.get(j).name.equals(all.get(i).wait2)) {
						right = yelled.get(j).val;
					}
				}
				if (left != Long.MIN_VALUE && right != Long.MIN_VALUE) {
					if (all.get(i).operation == 1) {
						all.get(i).val = left + right;
					}
					if (all.get(i).operation == 2) {
						all.get(i).val = left - right;
					}
					if (all.get(i).operation == 3) {
						all.get(i).val = left * right;
					}
					if (all.get(i).operation == 4) {
						all.get(i).val = left / right;
					}
					if (all.get(i).name.equals("root")) {
						return new long[] { left, right, all.get(i).val };
					}
				}
				if (all.get(i).val != Long.MIN_VALUE && (part1 || !all.get(i).name.equals("human"))) {
					yelled.add(all.get(i));
					all.remove(i);
				}
			}
			size = all.size();
		}
		return null;
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
		if (name.equals("humn") && !part1) {
			name = "human";
		}
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
			if (name.equals("root") && !part1) {
				operation = 5;
			}
		} else {
			val = Long.parseLong(line.split(" ")[1]);
		}
	}
}