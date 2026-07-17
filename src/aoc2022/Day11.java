package aoc2022;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day11 extends DayTemplate {

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		List<FastMonkey> monkeys = new ArrayList<>();
		List<Long> startingItems = new ArrayList<>();
		int[] constants = new int[5];
		int modulo = 1;
		while (in.hasNextLine()) {
			String line = in.nextLine();
			if (line.startsWith("Monkey") || line.equals("")) {
				continue;
			}
			String[] parts = line.split(": ")[1].split(" ");
			if (line.startsWith("  Starting items")) {
				String[] items = line.split(": ")[1].split(", ");
				for (String item : items) {
					startingItems.add(Long.parseLong(item));
				}
			} else if (line.startsWith("  Operation")) {
				constants[0] = parts[4].equals("old") ? 2 : parts[3].equals("+") ? 0 : 1;
				constants[1] = Integer.parseInt(parts[4].equals("old") ? "0" : parts[4]);
			} else if (line.startsWith("  Test")) {
				constants[2] = Integer.parseInt(parts[2]);
			} else if (line.startsWith("    If true")) {
				constants[3] = Integer.parseInt(parts[3]);
			} else if (line.startsWith("    If false")) {
				constants[4] = Integer.parseInt(parts[3]);
				monkeys.add(new FastMonkey(startingItems, constants));
				modulo *= constants[2];
				startingItems.clear();
				constants = new int[5];
			}
		}

		FastMonkey[] monkeyArray = monkeys.toArray(new FastMonkey[0]);
		for (int round = 0; round < (part1 ? 20 : 10000); round++) {
			for (FastMonkey monkey : monkeyArray) {
				monkey.turn(monkeyArray, part1, modulo);
			}
		}

		long top = 0;
		long second = 0;
		for (FastMonkey monkey : monkeyArray) {
			if (monkey.counter > top) {
				second = top;
				top = monkey.counter;
			} else if (monkey.counter > second) {
				second = monkey.counter;
			}
		}
		return "" + (top * second);
	}
}

class FastMonkey {
	private long[] items;
	private int head;
	private int tail;
	private final int operation;
	private final int operand;
	private final int divisor;
	private final int trueTarget;
	private final int falseTarget;
	long counter;

	FastMonkey(List<Long> startingItems, int[] constants) {
		items = new long[Math.max(16, startingItems.size() * 2)];
		for (long item : startingItems) {
			items[tail++] = item;
		}
		operation = constants[0];
		operand = constants[1];
		divisor = constants[2];
		trueTarget = constants[3];
		falseTarget = constants[4];
	}

	void turn(FastMonkey[] monkeys, boolean part1, int modulo) {
		while (head < tail) {
			long item = items[head++];
			switch (operation) {
			case 0:
				item += operand;
				break;
			case 1:
				item *= operand;
				break;
			default:
				item *= item;
				break;
			}
			if (part1) {
				item /= 3;
			}
			item %= modulo;
			monkeys[item % divisor == 0 ? trueTarget : falseTarget].add(item);
			counter++;
		}
		head = 0;
		tail = 0;
	}

	private void add(long item) {
		if (tail == items.length) {
			long[] grown = new long[items.length * 2];
			System.arraycopy(items, head, grown, 0, tail - head);
			tail -= head;
			head = 0;
			items = grown;
		}
		items[tail++] = item;
	}
}
