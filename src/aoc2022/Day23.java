package aoc2022;

import java.io.*;
import java.util.*;

public class Day23 extends DayTemplate {

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		int answer = 0;
		List<String> lines = new ArrayList<>();
		List<Coord2> elves = new ArrayList<>();
		while (in.hasNext()) {
			lines.add(in.nextLine());
		}
		for (int i = 0; i < lines.size(); i++) {
			for (int j = 0; j < lines.get(0).length(); j++) {
				if (lines.get(i).charAt(j) == '#') {
					elves.add(new Coord2(i, j));
				}
			}
		}
		if (part1) {
			helper2(elves, 10);
			int maxx, maxy, minx, miny;
			maxx = maxy = minx = miny = -1;
			for (int i = 0; i < elves.size(); i++) {
				maxx = (maxx == -1) ? elves.get(i).x : Math.max(maxx, elves.get(i).x);
				maxy = (maxy == -1) ? elves.get(i).y : Math.max(maxy, elves.get(i).y);
				minx = (minx == -1) ? elves.get(i).x : Math.min(minx, elves.get(i).x);
				miny = (miny == -1) ? elves.get(i).y : Math.min(miny, elves.get(i).y);
			}
			for (int i = minx; i <= maxx; i++) {
				for (int j = miny; j <= maxy; j++) {
					if (!elves.contains(new Coord2(i, j))) {
						answer++;
					}
				}
			}
		} else {
			answer = helper2(elves, -1);
		}
		return "" + answer;
	}

	public List<Coord2> helper(Coord2 elf, int round, int attempt) {
		List<Coord2> answer = new ArrayList<>();
		if ((round + attempt) % 4 == 0) {
			answer.add(new Coord2(elf.x - 1, elf.y - 1));
			answer.add(new Coord2(elf.x - 1, elf.y));
			answer.add(new Coord2(elf.x - 1, elf.y + 1));
		}
		if ((round + attempt) % 4 == 1) {
			answer.add(new Coord2(elf.x + 1, elf.y - 1));
			answer.add(new Coord2(elf.x + 1, elf.y));
			answer.add(new Coord2(elf.x + 1, elf.y + 1));
		}
		if ((round + attempt) % 4 == 2) {
			answer.add(new Coord2(elf.x + 1, elf.y - 1));
			answer.add(new Coord2(elf.x, elf.y - 1));
			answer.add(new Coord2(elf.x - 1, elf.y - 1));
		}
		if ((round + attempt) % 4 == 3) {
			answer.add(new Coord2(elf.x + 1, elf.y + 1));
			answer.add(new Coord2(elf.x, elf.y + 1));
			answer.add(new Coord2(elf.x - 1, elf.y + 1));
		}
		return answer;
	}

	public int helper2(List<Coord2> elves, int rounds) {
		if (rounds > 0) {
			for (int i = 0; i < rounds; i++) {
				Coord2[] proposed = new Coord2[elves.size()];
				for (int j = 0; j < elves.size(); j++) {
					int attempt = 0;
					List<Coord2> neighbors = new ArrayList<>();
					for (int k = 0; k < 4; k++) {
						neighbors.addAll(helper(elves.get(j), i, attempt + k));
					}
					boolean empty = true;
					for (Coord2 c : neighbors) {
						if (elves.contains(c)) {
							empty = false;
						}
					}
					if (empty) {
						proposed[j] = null;
						continue;
					}
					boolean flag = true;
					while (flag) {
						List<Coord2> possible = helper(elves.get(j), i, attempt);
						boolean empty2 = true;
						for (Coord2 c : possible) {
							if (elves.contains(c)) {
								empty2 = false;
							}
						}
						if (empty2) {
							proposed[j] = possible.get(1);
							flag = false;
						}
						attempt++;
						if (attempt == 5) {
							break;
						}
					}
				}
				boolean[] dupe = new boolean[proposed.length];
				for (int j = 0; j < proposed.length; j++) {
					for (int k = j + 1; k < proposed.length; k++) {
						if (proposed[j] != null && proposed[j].equals(proposed[k])) {
							dupe[j] = true;
							dupe[k] = true;
						}
					}
				}
				for (int j = 0; j < elves.size(); j++) {
					if (!dupe[j] && proposed[j] != null) {
						elves.set(j, proposed[j]);
					}
				}
			}
			return -1;
		}
		int counter = 0;
		boolean allNull = false;
		while(!allNull) {
			allNull = true;
			Coord2[] proposed = new Coord2[elves.size()];
			for (int j = 0; j < elves.size(); j++) {
				List<Coord2> neighbors = new ArrayList<>();
				for (int k = 0; k < 4; k++) {
					neighbors.addAll(helper(elves.get(j), counter, k));
				}
				boolean empty = true;
				for (Coord2 c : neighbors) {
					if (elves.contains(c)) {
						empty = false;
					}
				}
				if (empty) {
					proposed[j] = null;
					continue;
				}
				for(int k = 0; k < 4; k++) {
					List<Coord2> possible = helper(elves.get(j), counter, k);
					boolean empty2 = true;
					for (Coord2 c : possible) {
						if (elves.contains(c)) {
							empty2 = false;
						}
					}
					if (empty2) {
						proposed[j] = possible.get(1);
						break;
					}
				}
			}
			boolean[] dupe = new boolean[proposed.length];
			for (int j = 0; j < proposed.length; j++) {
				for (int k = j + 1; k < proposed.length; k++) {
					if (proposed[j] != null && proposed[j].equals(proposed[k])) {
						dupe[j] = true;
						dupe[k] = true;
					}
				}
			}
			for (int j = 0; j < elves.size(); j++) {
				if (!dupe[j] && proposed[j] != null) {
					elves.set(j, proposed[j]);
					allNull = false;
				}
			}
			counter++;
		}
		return counter;
	}
}

class Coord2 {
	int x;
	int y;

	public Coord2(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (other.getClass().equals(this.getClass())) {
			Coord2 o = (Coord2) other;
			return x == o.x && y == o.y;
		}
		return false;
	}
}