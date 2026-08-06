package aoc2022;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day19 extends DayTemplate {

	@Override
	public String[] fullSolve(Scanner in) throws FileNotFoundException {
		List<BluePrint> blueprints = parse(in);
		return new String[] { part1(blueprints) + "", part2(blueprints) + "" };
	}

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		List<BluePrint> blueprints = parse(in);
		return (part1 ? part1(blueprints) : part2(blueprints)) + "";
	}

	private List<BluePrint> parse(Scanner in) {
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
		return blueprints;
	}

	private int part1(List<BluePrint> blueprints) {
		int answer = 0;
		for (int i = 0; i < blueprints.size(); i++) {
			answer += (i + 1) * blueprints.get(i).result(24);
		}
		return answer;
	}

	private int part2(List<BluePrint> blueprints) {
		int answer = 1;
		for (int i = 0; i < 3; i++) {
			answer *= blueprints.get(i).result(32);
		}
		return answer;
	}
}

/**
 * Bound-centric branch-and-bound. Branches on which robot to build next (idle
 * minutes are skipped arithmetically); every prune below is exact, so the
 * search provably returns the true optimum:
 *
 * 1. Robot caps: never own more robots of a resource than the largest
 *    per-minute cost in that resource. With robots == cap, income alone covers
 *    any spend schedule (one build per minute, each build costs at most cap),
 *    so an extra robot can never enable anything.
 * 2. Useless-build time cutoffs: a robot finished with fewer minutes left than
 *    its product needs to reach a geode (geode 1, obsidian/ore 3, clay 5)
 *    cannot contribute and is never built.
 * 3. Saturation: once income covers a geode robot every minute (and one is
 *    affordable now), the optimum from that state is exactly
 *    geodes + geodeRobots*m + m(m-1)/2 - no recursion needed.
 * 4. Geometric bound: even one new geode robot per minute only adds
 *    m(m-1)/2 geodes. Costs one multiply; filters most nodes before 5.
 * 5. Ore-relaxed bound: simulate m minutes ignoring ore, granting a free clay
 *    robot every minute, and building obsidian/geode robots greedily (at most
 *    one per minute) from clay/obsidian pools. Clay is only ever spent on
 *    obsidian robots and obsidian only on geode robots, so this dominates
 *    every real schedule and is a valid, much tighter upper bound.
 *
 * Geode-first branch order finds strong incumbents early, which is what makes
 * bounds 4 and 5 bite. No memo table: after these prunes the tree is small
 * enough that hashing would cost more than it saves.
 */
class BluePrint {
	final int oreRobotOre;
	final int clayRobotOre;
	final int obsidianRobotOre;
	final int obsidianRobotClay;
	final int geodeRobotOre;
	final int geodeRobotObsidian;
	final int maxOreCost;
	int best;

	public BluePrint(int o1, int o2, int o3, int o4, int c3, int ob4) {
		oreRobotOre = o1;
		clayRobotOre = o2;
		obsidianRobotOre = o3;
		obsidianRobotClay = c3;
		geodeRobotOre = o4;
		geodeRobotObsidian = ob4;
		maxOreCost = Math.max(Math.max(oreRobotOre, clayRobotOre), Math.max(obsidianRobotOre, geodeRobotOre));
	}

	public int result(int minutes) {
		best = 0;
		search(minutes, 1, 0, 0, 0, 0, 0, 0, 0);
		return best;
	}

	private void search(int m, int oreRobots, int clayRobots, int obsidianRobots, int geodeRobots, int ore, int clay,
			int obsidian, int geodes) {
		int stop = geodes + geodeRobots * m;
		if (stop > best) {
			best = stop;
		}

		if (ore >= geodeRobotOre && obsidian >= geodeRobotObsidian && oreRobots >= geodeRobotOre
				&& obsidianRobots >= geodeRobotObsidian) {
			int exact = stop + m * (m - 1) / 2;
			if (exact > best) {
				best = exact;
			}
			return;
		}

		if (stop + m * (m - 1) / 2 <= best) {
			return;
		}
		if (!relaxedBoundBeatsBest(m, clayRobots, obsidianRobots, clay, obsidian, stop)) {
			return;
		}

		if (obsidianRobots > 0) {
			int wait = Math.max(turnsToAfford(ore, oreRobots, geodeRobotOre),
					turnsToAfford(obsidian, obsidianRobots, geodeRobotObsidian));
			int after = m - wait - 1;
			if (after >= 1) {
				int elapsed = wait + 1;
				search(after, oreRobots, clayRobots, obsidianRobots, geodeRobots + 1,
						ore + oreRobots * elapsed - geodeRobotOre, clay + clayRobots * elapsed,
						obsidian + obsidianRobots * elapsed - geodeRobotObsidian, geodes + geodeRobots * elapsed);
			}
		}
		if (obsidianRobots < geodeRobotObsidian && clayRobots > 0) {
			int wait = Math.max(turnsToAfford(ore, oreRobots, obsidianRobotOre),
					turnsToAfford(clay, clayRobots, obsidianRobotClay));
			int after = m - wait - 1;
			if (after >= 3) {
				int elapsed = wait + 1;
				search(after, oreRobots, clayRobots, obsidianRobots + 1, geodeRobots,
						ore + oreRobots * elapsed - obsidianRobotOre, clay + clayRobots * elapsed - obsidianRobotClay,
						obsidian + obsidianRobots * elapsed, geodes + geodeRobots * elapsed);
			}
		}
		if (clayRobots < obsidianRobotClay) {
			int wait = turnsToAfford(ore, oreRobots, clayRobotOre);
			int after = m - wait - 1;
			if (after >= 5) {
				int elapsed = wait + 1;
				search(after, oreRobots, clayRobots + 1, obsidianRobots, geodeRobots,
						ore + oreRobots * elapsed - clayRobotOre, clay + clayRobots * elapsed,
						obsidian + obsidianRobots * elapsed, geodes + geodeRobots * elapsed);
			}
		}
		if (oreRobots < maxOreCost) {
			int wait = turnsToAfford(ore, oreRobots, oreRobotOre);
			int after = m - wait - 1;
			if (after >= 3) {
				int elapsed = wait + 1;
				search(after, oreRobots + 1, clayRobots, obsidianRobots, geodeRobots,
						ore + oreRobots * elapsed - oreRobotOre, clay + clayRobots * elapsed,
						obsidian + obsidianRobots * elapsed, geodes + geodeRobots * elapsed);
			}
		}
	}

	private boolean relaxedBoundBeatsBest(int m, int clayRobots, int obsidianRobots, int clay, int obsidian, int base) {
		int bound = base;
		int clayPool = clay;
		int obsPool = obsidian;
		int clayRate = clayRobots;
		int obsRate = obsidianRobots;
		for (int k = m; k >= 1; k--) {
			boolean buildObs = clayPool >= obsidianRobotClay;
			if (obsPool >= geodeRobotObsidian) {
				obsPool -= geodeRobotObsidian;
				bound += k - 1;
				if (bound > best) {
					return true;
				}
			}
			if (buildObs) {
				clayPool -= obsidianRobotClay;
			}
			clayPool += clayRate;
			obsPool += obsRate;
			if (buildObs) {
				obsRate++;
			}
			clayRate++;
		}
		return bound > best;
	}

	private int turnsToAfford(int resource, int robots, int cost) {
		int need = cost - resource;
		if (need <= 0) {
			return 0;
		}
		return (need + robots - 1) / robots;
	}
}
