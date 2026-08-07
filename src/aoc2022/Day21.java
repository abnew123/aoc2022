package aoc2022;

import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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

	/**
	 * One slurp, manual scan. Each nonblank line is "name: job" where the job is
	 * either one literal number or "left op right"; job tokens are separated by
	 * whitespace. Blank lines are skipped.
	 */
	private Map<String, Monkey> parse(Scanner in) {
		String text = in.useDelimiter("\\A").hasNext() ? in.next() : "";
		Map<String, Monkey> monkeys = new HashMap<>();
		String[] tokens = new String[4];
		int n = text.length();
		int i = 0;
		while (i < n) {
			int lineStart = i;
			while (i < n && text.charAt(i) != '\n' && text.charAt(i) != '\r') {
				i++;
			}
			int lineEnd = i;
			if (i < n) {
				if (text.charAt(i) == '\r' && i + 1 < n && text.charAt(i + 1) == '\n') {
					i++;
				}
				i++;
			}
			boolean blank = true;
			for (int k = lineStart; k < lineEnd; k++) {
				if (!Character.isWhitespace(text.charAt(k))) {
					blank = false;
					break;
				}
			}
			if (blank) {
				continue;
			}
			int colon = -1;
			for (int k = lineStart; k < lineEnd; k++) {
				if (text.charAt(k) == ':') {
					colon = k;
					break;
				}
			}
			if (colon <= lineStart) {
				throw new IllegalArgumentException("Invalid monkey job");
			}
			String name = text.substring(lineStart, colon).trim();
			int tokenCount = 0;
			int k = colon + 1;
			while (k < lineEnd) {
				char c = text.charAt(k);
				if (c == ' ' || c == '\t' || c == '\f') {
					k++;
					continue;
				}
				int tokenStart = k;
				while (k < lineEnd) {
					c = text.charAt(k);
					if (c == ' ' || c == '\t' || c == '\f') {
						break;
					}
					k++;
				}
				if (tokenCount == tokens.length) {
					throw new IllegalArgumentException("Invalid monkey job");
				}
				tokens[tokenCount++] = text.substring(tokenStart, k);
			}
			Monkey monkey;
			if (tokenCount == 0) {
				throw new IllegalArgumentException("Missing monkey job");
			} else if (tokenCount == 1) {
				monkey = new Monkey(new BigInteger(tokens[0]), null, null, '\0');
			} else if (tokenCount == 3) {
				String operator = tokens[1];
				if (operator.length() != 1 || "+-*/".indexOf(operator.charAt(0)) < 0) {
					throw new IllegalArgumentException("Invalid monkey job");
				}
				monkey = new Monkey(null, tokens[0], tokens[2], operator.charAt(0));
			} else {
				throw new IllegalArgumentException("Invalid monkey job");
			}
			if (monkeys.put(name, monkey) != null) {
				throw new IllegalArgumentException("Duplicate monkey");
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
