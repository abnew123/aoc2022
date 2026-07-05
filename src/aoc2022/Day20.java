package aoc2022;

import java.io.FileNotFoundException;
import java.util.*;

public class Day20 extends DayTemplate {
	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		ArrayList<Num> a = new ArrayList<>(), order = new ArrayList<>();
		for (int i = 0; in.hasNextLong(); i++) {
			Num n = new Num(in.nextLong() * (part1 ? 1 : 811589153L), i);
			a.add(n);
			order.add(n);
		}
		for (int r = part1 ? 1 : 10; r-- > 0;) {
			for (Num n : order) {
				int i = a.indexOf(n);
				a.remove(i);
				a.add(Math.floorMod(i + n.v, a.size()), n);
			}
		}
		int z = 0, s = a.size();
		while (a.get(z).v != 0) {
			z++;
		}
		return "" + (a.get((z + 1000) % s).v + a.get((z + 2000) % s).v + a.get((z + 3000) % s).v);
	}

	record Num(long v, int id) {
	}
}
