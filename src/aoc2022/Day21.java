package aoc2022;

import java.io.FileNotFoundException;
import java.util.*;

public class Day21 extends DayTemplate {
	Map<String, Job> m;

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		m = new HashMap<>();
		while (in.hasNext()) {
			String[] a = in.nextLine().split(": | ");
			m.put(a[0], a.length == 2 ? new Job(Long.parseLong(a[1]), null, null, '?')
					: new Job(Long.MIN_VALUE, a[1], a[3], a[2].charAt(0)));
		}
		if (part1) {
			return "" + eval("root");
		}
		Job root = m.get("root");
		Long left = maybe(root.a), right = maybe(root.b);
		return "" + (left == null ? need(root.a, right) : need(root.b, left));
	}

	long eval(String s) {
		Job j = m.get(s);
		return j.v != Long.MIN_VALUE ? j.v : calc(j.op, eval(j.a), eval(j.b));
	}

	Long maybe(String s) {
		if (s.equals("humn")) {
			return null;
		}
		Job j = m.get(s);
		if (j.v != Long.MIN_VALUE) {
			return j.v;
		}
		Long a = maybe(j.a), b = maybe(j.b);
		return a == null || b == null ? null : calc(j.op, a, b);
	}

	long need(String s, long target) {
		if (s.equals("humn")) {
			return target;
		}
		Job j = m.get(s);
		Long a = maybe(j.a), b = maybe(j.b);
		if (a == null) {
			return need(j.a, switch (j.op) {
			case '+' -> target - b;
			case '-' -> target + b;
			case '*' -> target / b;
			default -> target * b;
			});
		}
		return need(j.b, switch (j.op) {
		case '+' -> target - a;
		case '-' -> a - target;
		case '*' -> target / a;
		default -> a / target;
		});
	}

	long calc(char op, long a, long b) {
		return switch (op) {
		case '+' -> a + b;
		case '-' -> a - b;
		case '*' -> a * b;
		default -> a / b;
		};
	}

	record Job(long v, String a, String b, char op) {
	}
}
