package aoc2022;

import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Day21 extends DayTemplate {

	@Override
	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		Map<String, Monkey> monkeys = parse(in);
		return part1 ? evaluate("root", monkeys, new HashMap<>()).toString()
				: solveHuman(monkeys).toString();
	}

	@Override
	public String[] fullSolve(Scanner in) throws FileNotFoundException {
		Map<String, Monkey> monkeys = parse(in);
		BigInteger part1 = evaluate("root", monkeys, new HashMap<>());
		BigInteger part2 = solveHuman(monkeys);
		return new String[] {part1.toString(), part2.toString()};
	}

	private Map<String, Monkey> parse(Scanner in) {
		Map<String, Monkey> monkeys = new HashMap<>();
		while (in.hasNextLine()) {
			String line = in.nextLine();
			if (line.isBlank()) {
				continue;
			}
			int colon = line.indexOf(':');
			if (colon < 1) {
				throw new IllegalArgumentException("Invalid monkey job: " + line);
			}
			String name = line.substring(0, colon).trim();
			StringTokenizer job = new StringTokenizer(line.substring(colon + 1));
			if (!job.hasMoreTokens()) {
				throw new IllegalArgumentException("Missing job for " + name);
			}
			String first = job.nextToken();
			Monkey monkey;
			if (!job.hasMoreTokens()) {
				monkey = new Monkey(new BigInteger(first), null, null, '\0');
			} else {
				String operator = job.nextToken();
				if (operator.length() != 1 || !job.hasMoreTokens()) {
					throw new IllegalArgumentException("Invalid job for " + name);
				}
				String right = job.nextToken();
				if (job.hasMoreTokens() || "+-*/".indexOf(operator.charAt(0)) < 0) {
					throw new IllegalArgumentException("Invalid job for " + name);
				}
				monkey = new Monkey(null, first, right, operator.charAt(0));
			}
			if (monkeys.put(name, monkey) != null) {
				throw new IllegalArgumentException("Duplicate monkey " + name);
			}
		}
		return monkeys;
	}

	private BigInteger evaluate(String name, Map<String, Monkey> monkeys,
			Map<String, BigInteger> memo) {
		BigInteger cached = memo.get(name);
		if (cached != null) {
			return cached;
		}
		Monkey monkey = requireMonkey(name, monkeys);
		BigInteger result = monkey.value != null ? monkey.value
				: apply(monkey.operation, evaluate(monkey.left, monkeys, memo),
						evaluate(monkey.right, monkeys, memo));
		memo.put(name, result);
		return result;
	}

	private BigInteger solveHuman(Map<String, Monkey> monkeys) {
		Monkey root = requireMonkey("root", monkeys);
		if (root.value != null) {
			throw new IllegalArgumentException("Root must combine two monkey jobs");
		}
		Map<String, BigInteger> known = new HashMap<>();
		BigInteger left = tryEvaluate(root.left, monkeys, known);
		BigInteger right = tryEvaluate(root.right, monkeys, known);
		if ((left == null) == (right == null)) {
			throw new IllegalArgumentException("Exactly one root branch must depend on humn");
		}
		BigInteger human = left == null ? solveUnknown(root.left, right, monkeys, known)
				: solveUnknown(root.right, left, monkeys, known);
		Map<String, BigInteger> check = new HashMap<>();
		check.put("humn", human);
		if (!evaluate(root.left, monkeys, check).equals(evaluate(root.right, monkeys, check))) {
			throw new ArithmeticException("Solved humn value does not balance root");
		}
		return human;
	}

	private BigInteger tryEvaluate(String name, Map<String, Monkey> monkeys,
			Map<String, BigInteger> memo) {
		if (name.equals("humn")) {
			return null;
		}
		BigInteger cached = memo.get(name);
		if (cached != null) {
			return cached;
		}
		Monkey monkey = requireMonkey(name, monkeys);
		if (monkey.value != null) {
			memo.put(name, monkey.value);
			return monkey.value;
		}
		BigInteger left = tryEvaluate(monkey.left, monkeys, memo);
		BigInteger right = tryEvaluate(monkey.right, monkeys, memo);
		if (left == null || right == null) {
			return null;
		}
		BigInteger result = apply(monkey.operation, left, right);
		memo.put(name, result);
		return result;
	}

	private BigInteger solveUnknown(String name, BigInteger target,
			Map<String, Monkey> monkeys, Map<String, BigInteger> known) {
		if (name.equals("humn")) {
			return target;
		}
		Monkey monkey = requireMonkey(name, monkeys);
		if (monkey.value != null) {
			throw new IllegalArgumentException("Unknown path ended at " + name);
		}
		BigInteger left = tryEvaluate(monkey.left, monkeys, known);
		BigInteger right = tryEvaluate(monkey.right, monkeys, known);
		if ((left == null) == (right == null)) {
			throw new IllegalArgumentException("Exactly one branch must depend on humn at " + name);
		}
		if (left == null) {
			return solveUnknown(monkey.left, targetForLeft(monkey.operation, target, right),
					monkeys, known);
		}
		return solveUnknown(monkey.right, targetForRight(monkey.operation, target, left),
				monkeys, known);
	}

	private Monkey requireMonkey(String name, Map<String, Monkey> monkeys) {
		Monkey monkey = monkeys.get(name);
		if (monkey == null) {
			throw new IllegalArgumentException("Unknown monkey " + name);
		}
		return monkey;
	}

	private BigInteger apply(char operation, BigInteger left, BigInteger right) {
		return switch (operation) {
		case '+' -> left.add(right);
		case '-' -> left.subtract(right);
		case '*' -> left.multiply(right);
		case '/' -> left.divide(right);
		default -> throw new IllegalArgumentException("Unknown operation " + operation);
		};
	}

	private BigInteger targetForLeft(char operation, BigInteger target, BigInteger right) {
		return switch (operation) {
		case '+' -> target.subtract(right);
		case '-' -> target.add(right);
		case '*' -> divideExact(target, right);
		case '/' -> target.multiply(right);
		default -> throw new IllegalArgumentException("Unknown operation " + operation);
		};
	}

	private BigInteger targetForRight(char operation, BigInteger target, BigInteger left) {
		return switch (operation) {
		case '+' -> target.subtract(left);
		case '-' -> left.subtract(target);
		case '*' -> divideExact(target, left);
		case '/' -> divideExact(left, target);
		default -> throw new IllegalArgumentException("Unknown operation " + operation);
		};
	}

	private BigInteger divideExact(BigInteger dividend, BigInteger divisor) {
		BigInteger[] result = dividend.divideAndRemainder(divisor);
		if (result[1].signum() != 0) {
			throw new ArithmeticException("Monkey equation has no integer solution");
		}
		return result[0];
	}

	private record Monkey(BigInteger value, String left, String right, char operation) {}
}
