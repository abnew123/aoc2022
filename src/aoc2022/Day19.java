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

	// Each blueprint is exactly seven integers in statement order (id, then the
	// six costs) regardless of layout — the official example wraps one blueprint
	// across indented lines, real inputs use one line each. One slurp: line
	// boundaries are irrelevant to the digit scan because separators are never
	// digits, so scanning the whole text groups the numbers identically.
	private List<BluePrint> parse(Scanner in) {
		String text = in.useDelimiter("\\A").hasNext() ? in.next() : "";
		List<BluePrint> blueprints = new ArrayList<>();
		int[] nums = new int[7];
		int have = 0;
		for (int i = 0, n = text.length(); i < n; i++) {
			char c = text.charAt(i);
			if (c >= '0' && c <= '9') {
				int v = c - '0';
				while (i + 1 < n && (c = text.charAt(i + 1)) >= '0' && c <= '9') {
					v = v * 10 + (c - '0');
					i++;
				}
				nums[have++] = v;
				if (have == 7) {
					blueprints.add(new BluePrint(nums[0], nums[1], nums[2], nums[3], nums[4], nums[5], nums[6]));
					have = 0;
				}
			}
		}
		return blueprints;
	}

	private int part1(List<BluePrint> blueprints) {
		int answer = 0;
		for (BluePrint bp : blueprints) {
			answer += bp.id * bp.result(24);
		}
		return answer;
	}

	private int part2(List<BluePrint> blueprints) {
		int answer = 1;
		for (int i = 0; i < Math.min(3, blueprints.size()); i++) {
			answer *= blueprints.get(i).result(32);
		}
		return answer;
	}
}

/**
 * Bound-centric branch-and-bound. Branches on which robot to build next (idle
 * minutes are skipped arithmetically); every prune below is exact, so the
 * search provably returns the true optimum. Precursor gates tolerate zero-cost
 * blueprints, and part 2 uses at most the first three blueprints, so the
 * two-blueprint official example works:
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
	final int id;
	final int oreRobotOre;
	final int clayRobotOre;
	final int obsidianRobotOre;
	final int obsidianRobotClay;
	final int geodeRobotOre;
	final int geodeRobotObsidian;
	final int maxOreCost;
	int best;

	public BluePrint(int id, int oreOre, int clayOre, int obsOre, int obsClay, int geoOre, int geoObs) {
		this.id = id;
		oreRobotOre = oreOre;
		clayRobotOre = clayOre;
		obsidianRobotOre = obsOre;
		obsidianRobotClay = obsClay;
		geodeRobotOre = geoOre;
		geodeRobotObsidian = geoObs;
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

		if (geodeRobotObsidian == 0 || obsidianRobots > 0) {
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
		if (obsidianRobots < geodeRobotObsidian && (obsidianRobotClay == 0 || clayRobots > 0)) {
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
