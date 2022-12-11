package aoc2022;

import java.io.*;
import java.util.*;

public class Day11 extends DayTemplate {

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		List<Monkey> monkeys = new ArrayList<>();
		List<Long> items = new ArrayList<>();
		int[] consts = new int[5];
		int modulo = 1;
		while (in.hasNext()) {
			String line = in.nextLine();
			if (line.startsWith("Monkey") || line.equals("")) {
				continue;
			}
			if (line.startsWith("  Starting items")) {
				String[] itms = line.split(": ")[1].split(", ");
				for (String itm : itms) {
					items.add(Long.parseLong(itm));
				}
			}
			String[] itms = line.split(": ")[1].split(" ");
			if (line.startsWith("  Operation")) {
				consts[0] = itms[4].equals("old") ? 2 : itms[3].equals("+") ? 0 : 1;
				consts[1] = Integer.parseInt(itms[4].equals("old") ? "0" : itms[4]);
			}
			if (line.startsWith("  Test")) {
				consts[2] = Integer.parseInt(itms[2]);
			}
			if (line.startsWith("    If true")) {
				consts[3] = Integer.parseInt(itms[3]);
			}
			if (line.startsWith("    If false")) {
				consts[4] = Integer.parseInt(itms[3]);
				monkeys.add(new Monkey(items, consts));
				modulo*=consts[2];
				items.clear();
				consts = new int[5];
			}
		}
		for (int i = 0; i < (part1 ? 20 : 10000); i++) {
			for (Monkey monkey : monkeys) {
				monkey.turn(monkeys, part1, modulo);
			}
		}
		List<Long> inspects = new ArrayList<>();
		for (Monkey monkey : monkeys) {
			inspects.add(monkey.counter);
		}
		Collections.sort(inspects);
		return "" + (inspects.get(inspects.size() - 2) * inspects.get(inspects.size() - 1));
	}
}

class Monkey {
	List<Long> items;
	int[] consts;
	long counter;

	public Monkey(List<Long> items, int[] consts) {
		this.items = new ArrayList<>();
		this.items.addAll(items);
		this.consts = consts;
		counter = 0;
	}

	public void turn(List<Monkey> monkeys, boolean part1, int modulo) {
		for (Long item : items) {
			switch (consts[0]) {
			case 0:
				item += consts[1];
				break;
			case 1:
				item *= consts[1];
				break;
			case 2:
				item *= item;
				break;
			}
			if (part1) {
				item /= 3;
			}
			item %= modulo;
			monkeys.get((item % consts[2] == 0)?consts[3]:consts[4]).items.add(item);
			counter++;
		}
		items.clear();
	}
}