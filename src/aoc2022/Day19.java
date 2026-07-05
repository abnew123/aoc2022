package aoc2022;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Day19 extends DayTemplate {

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		int answer = part1 ? 0 : 1;
		List<BluePrint> blueprints = new ArrayList<>();
		while (in.hasNext()) {
			String[] line = in.nextLine().split(" ");
			int o1 = Integer.parseInt(line[6]);
			int o2 = Integer.parseInt(line[12]);
			int o3 = Integer.parseInt(line[18]);
			int o4 = Integer.parseInt(line[27]);
			int c3 = Integer.parseInt(line[21]);
			int ob4 = Integer.parseInt(line[30]);
			blueprints.add(new BluePrint(o1, o2, o3, o4, c3, ob4));
		}
		for (int i = 0; i < (part1 ? blueprints.size() : 3); i++) {
			if (part1) {
				answer += (i + 1) * blueprints.get(i).result(24);
			} else {
				answer *= blueprints.get(i).result(32);
			}
		}
		return "" + answer;
	}
}

class BluePrint {
	final int oreRobotOre;
	final int clayRobotOre;
	final int obsidianRobotOre;
	final int obsidianRobotClay;
	final int geodeRobotOre;
	final int geodeRobotObsidian;
	final int maxOreCost;
	final int maxClayCost;
	final int maxObsidianCost;
	int best;
	Map<State, Integer> seen;

	public BluePrint(int o1, int o2, int o3, int o4, int c3, int ob4) {
		oreRobotOre = o1;
		clayRobotOre = o2;
		obsidianRobotOre = o3;
		obsidianRobotClay = c3;
		geodeRobotOre = o4;
		geodeRobotObsidian = ob4;
		maxOreCost = Math.max(Math.max(oreRobotOre, clayRobotOre), Math.max(obsidianRobotOre, geodeRobotOre));
		maxClayCost = obsidianRobotClay;
		maxObsidianCost = geodeRobotObsidian;
	}

	public int result(int minutes) {
		best = 0;
		seen = new HashMap<>(minutes == 24 ? 4096 : 32768);
		search(minutes, 1, 0, 0, 0, 0, 0, 0, 0);
		return best;
	}

	private void search(int minutes, int oreRobots, int clayRobots, int obsidianRobots, int geodeRobots, int ore,
			int clay, int obsidian, int geodes) {
		best = Math.max(best, geodes + geodeRobots * minutes);
		if (minutes == 0) {
			return;
		}

		// Even if every remaining minute made a new geode robot, it could only add
		// minutes - 1, minutes - 2, ... geodes before time runs out.
		int optimistic = geodes + geodeRobots * minutes + (minutes * (minutes - 1)) / 2;
		if (optimistic <= best) {
			return;
		}

		ore = cappedResource(ore, oreRobots, maxOreCost, minutes);
		clay = cappedResource(clay, clayRobots, maxClayCost, minutes);
		obsidian = cappedResource(obsidian, obsidianRobots, maxObsidianCost, minutes);

		State state = new State(minutes, oreRobots, clayRobots, obsidianRobots, geodeRobots, ore, clay, obsidian);
		Integer previousGeodes = seen.get(state);
		if (previousGeodes != null && previousGeodes >= geodes) {
			return;
		}
		seen.put(state, geodes);

		buildGeodeRobot(minutes, oreRobots, clayRobots, obsidianRobots, geodeRobots, ore, clay, obsidian, geodes);
		if (obsidianRobots < maxObsidianCost) {
			buildObsidianRobot(minutes, oreRobots, clayRobots, obsidianRobots, geodeRobots, ore, clay, obsidian, geodes);
		}
		if (clayRobots < maxClayCost) {
			buildClayRobot(minutes, oreRobots, clayRobots, obsidianRobots, geodeRobots, ore, clay, obsidian, geodes);
		}
		if (oreRobots < maxOreCost) {
			buildOreRobot(minutes, oreRobots, clayRobots, obsidianRobots, geodeRobots, ore, clay, obsidian, geodes);
		}
	}

	private void buildGeodeRobot(int minutes, int oreRobots, int clayRobots, int obsidianRobots, int geodeRobots,
			int ore, int clay, int obsidian, int geodes) {
		int wait = waitTime(ore, oreRobots, geodeRobotOre, obsidian, obsidianRobots, geodeRobotObsidian);
		if (wait < minutes) {
			int elapsed = wait + 1;
			search(minutes - elapsed, oreRobots, clayRobots, obsidianRobots, geodeRobots + 1,
					ore + oreRobots * elapsed - geodeRobotOre, clay + clayRobots * elapsed,
					obsidian + obsidianRobots * elapsed - geodeRobotObsidian, geodes + geodeRobots * elapsed);
		}
	}

	private void buildObsidianRobot(int minutes, int oreRobots, int clayRobots, int obsidianRobots, int geodeRobots,
			int ore, int clay, int obsidian, int geodes) {
		int wait = waitTime(ore, oreRobots, obsidianRobotOre, clay, clayRobots, obsidianRobotClay);
		if (wait < minutes) {
			int elapsed = wait + 1;
			search(minutes - elapsed, oreRobots, clayRobots, obsidianRobots + 1, geodeRobots,
					ore + oreRobots * elapsed - obsidianRobotOre, clay + clayRobots * elapsed - obsidianRobotClay,
					obsidian + obsidianRobots * elapsed, geodes + geodeRobots * elapsed);
		}
	}

	private void buildClayRobot(int minutes, int oreRobots, int clayRobots, int obsidianRobots, int geodeRobots, int ore,
			int clay, int obsidian, int geodes) {
		int wait = turnsToAfford(ore, oreRobots, clayRobotOre);
		if (wait < minutes) {
			int elapsed = wait + 1;
			search(minutes - elapsed, oreRobots, clayRobots + 1, obsidianRobots, geodeRobots,
					ore + oreRobots * elapsed - clayRobotOre, clay + clayRobots * elapsed,
					obsidian + obsidianRobots * elapsed, geodes + geodeRobots * elapsed);
		}
	}

	private void buildOreRobot(int minutes, int oreRobots, int clayRobots, int obsidianRobots, int geodeRobots, int ore,
			int clay, int obsidian, int geodes) {
		int wait = turnsToAfford(ore, oreRobots, oreRobotOre);
		if (wait < minutes) {
			int elapsed = wait + 1;
			search(minutes - elapsed, oreRobots + 1, clayRobots, obsidianRobots, geodeRobots,
					ore + oreRobots * elapsed - oreRobotOre, clay + clayRobots * elapsed,
					obsidian + obsidianRobots * elapsed, geodes + geodeRobots * elapsed);
		}
	}

	private int waitTime(int resourceA, int robotsA, int costA, int resourceB, int robotsB, int costB) {
		return Math.max(turnsToAfford(resourceA, robotsA, costA), turnsToAfford(resourceB, robotsB, costB));
	}

	private int turnsToAfford(int resource, int robots, int cost) {
		if (resource >= cost) {
			return 0;
		}
		if (robots == 0) {
			return Integer.MAX_VALUE;
		}
		return (cost - resource + robots - 1) / robots;
	}

	private int cappedResource(int resource, int robots, int maxSpend, int minutes) {
		return Math.min(resource, Math.max(0, maxSpend * minutes - robots * (minutes - 1)));
	}

	private static class State {
		final int minutes;
		final int oreRobots;
		final int clayRobots;
		final int obsidianRobots;
		final int geodeRobots;
		final int ore;
		final int clay;
		final int obsidian;

		State(int minutes, int oreRobots, int clayRobots, int obsidianRobots, int geodeRobots, int ore, int clay,
				int obsidian) {
			this.minutes = minutes;
			this.oreRobots = oreRobots;
			this.clayRobots = clayRobots;
			this.obsidianRobots = obsidianRobots;
			this.geodeRobots = geodeRobots;
			this.ore = ore;
			this.clay = clay;
			this.obsidian = obsidian;
		}

		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (!(obj instanceof State)) {
				return false;
			}
			State other = (State) obj;
			return minutes == other.minutes && oreRobots == other.oreRobots && clayRobots == other.clayRobots
					&& obsidianRobots == other.obsidianRobots && geodeRobots == other.geodeRobots && ore == other.ore
					&& clay == other.clay && obsidian == other.obsidian;
		}

		public int hashCode() {
			int hash = minutes;
			hash = 31 * hash + oreRobots;
			hash = 31 * hash + clayRobots;
			hash = 31 * hash + obsidianRobots;
			hash = 31 * hash + geodeRobots;
			hash = 31 * hash + ore;
			hash = 31 * hash + clay;
			return 31 * hash + obsidian;
		}
	}
}
