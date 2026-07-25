package src.solutions;

import src.meta.DayTemplate;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day20 extends DayTemplate {

	public String[] fullSolve(Scanner in) {
		List<Long> vals = parse(in);
		// The mix mutates its list of Movers, so each part gets a freshly built one.
		return new String[] { mix(vals, true), mix(vals, false) };
	}

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		return mix(parse(in), part1);
	}

	private List<Long> parse(Scanner in) {
		List<Long> vals = new ArrayList<>();
		while (in.hasNext()) {
			vals.add((long) Integer.parseInt(in.nextLine()));
		}
		return vals;
	}

	private String mix(List<Long> vals, boolean part1) {
		List<Mover> movers = new ArrayList<>();
		int counter = 0;
		for (long val : vals) {
			movers.add(new Mover(counter, part1 ? val : val * 811589153));
			counter++;
		}
		for (int k = 0; k < (part1 ? 1 : 10); k++) {
			for (int i = 0; i < movers.size(); i++) {
				Mover mover = null;
				int loc = 0;
				for (int j = 0; j < movers.size(); j++) {
					if (movers.get(j).uuid == i) {
						mover = movers.get(j);
						loc = j;
					}
				}
				movers.remove(loc);
				int rotate = (int) (mover.val % (movers.size()));
				movers.add(((loc + rotate) % (movers.size()) + (movers.size())) % (movers.size()), mover);
			}
		}
		int offset = 0;
		for (int j = 0; j < movers.size(); j++) {
			if (movers.get(j).val == 0) {
				offset = j;
			}
		}
		return "" + (movers.get((offset + 1000) % movers.size()).val + movers.get((offset + 2000) % movers.size()).val
				+ movers.get((offset + 3000) % movers.size()).val);
	}
}

class Mover {
	int uuid;
	long val;

	public Mover(int uuid, long val) {
		this.uuid = uuid;
		this.val = val;
	}
}