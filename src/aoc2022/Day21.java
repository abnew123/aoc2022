package aoc2022;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Day21 extends DayTemplate {

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		Map<String, Monkey2> monkeys = new HashMap<>();
		while (in.hasNext()) {
			Monkey2 monkey = new Monkey2(in.nextLine(), part1);
			monkeys.put(monkey.name, monkey);
		}
		if (part1) {
			return evaluate("root", monkeys) + "";
		}
		Monkey2 root = monkeys.get("root");
		Long left = tryEvaluate(root.wait1, monkeys);
		Long right = tryEvaluate(root.wait2, monkeys);
		return (left == null ? solveUnknown(root.wait1, right, monkeys) : solveUnknown(root.wait2, left, monkeys)) + "";
	}

	private long evaluate(String name, Map<String, Monkey2> monkeys) {
		Monkey2 monkey = monkeys.get(name);
		if (monkey.val != Long.MIN_VALUE) {
			return monkey.val;
		}
		long left = evaluate(monkey.wait1, monkeys);
		long right = evaluate(monkey.wait2, monkeys);
		return apply(monkey.operation, left, right);
	}

	private Long tryEvaluate(String name, Map<String, Monkey2> monkeys) {
		if (name.equals("humn")) {
			return null;
		}
		Monkey2 monkey = monkeys.get(name);
		if (monkey.val != Long.MIN_VALUE) {
			return monkey.val;
		}
		Long left = tryEvaluate(monkey.wait1, monkeys);
		Long right = tryEvaluate(monkey.wait2, monkeys);
		if (left == null || right == null) {
			return null;
		}
		return apply(monkey.operation, left, right);
	}

	private long solveUnknown(String name, long target, Map<String, Monkey2> monkeys) {
		if (name.equals("humn")) {
			return target;
		}
		Monkey2 monkey = monkeys.get(name);
		Long left = tryEvaluate(monkey.wait1, monkeys);
		Long right = tryEvaluate(monkey.wait2, monkeys);
		if (left == null) {
			return solveUnknown(monkey.wait1, targetForLeft(monkey.operation, target, right), monkeys);
		}
		return solveUnknown(monkey.wait2, targetForRight(monkey.operation, target, left), monkeys);
	}

	private long apply(int operation, long left, long right) {
		return switch (operation) {
		case 1 -> left + right;
		case 2 -> left - right;
		case 3 -> left * right;
		case 4 -> left / right;
		default -> throw new IllegalArgumentException("Unknown operation " + operation);
		};
	}

	private long targetForLeft(int operation, long target, long right) {
		return switch (operation) {
		case 1 -> target - right;
		case 2 -> target + right;
		case 3 -> target / right;
		case 4 -> target * right;
		default -> throw new IllegalArgumentException("Unknown operation " + operation);
		};
	}

	private long targetForRight(int operation, long target, long left) {
		return switch (operation) {
		case 1 -> target - left;
		case 2 -> left - target;
		case 3 -> target / left;
		case 4 -> left / target;
		default -> throw new IllegalArgumentException("Unknown operation " + operation);
		};
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
