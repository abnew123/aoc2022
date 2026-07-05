package aoc2022;

import java.io.FileNotFoundException;
import java.util.*;

public class Day16 extends DayTemplate {
	int[][] dist;
	int[] rate;

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		Map<String, Integer> flow = new HashMap<>();
		Map<String, List<String>> edge = new HashMap<>();
		while (in.hasNext()) {
			String[] line = in.nextLine().split(" ");
			List<String> tunnels = new ArrayList<>();
			for (int i = 9; i < line.length; i++) {
				tunnels.add(line[i].substring(0, 2));
			}
			flow.put(line[1], Integer.parseInt(line[4].substring(5, line[4].length() - 1)));
			edge.put(line[1], tunnels);
		}
		ArrayList<String> use = new ArrayList<>();
		use.add("AA");
		for (String v : flow.keySet()) {
			if (flow.get(v) > 0) {
				use.add(v);
			}
		}
		Collections.sort(use.subList(1, use.size()));
		dist = new int[use.size()][use.size()];
		rate = new int[use.size()];
		for (int i = 0; i < use.size(); i++) {
			rate[i] = flow.get(use.get(i));
			Map<String, Integer> m = bfs(use.get(i), edge);
			for (int j = 0; j < use.size(); j++) {
				dist[i][j] = m.get(use.get(j));
			}
		}
		int maskCount = 1 << (rate.length - 1);
		if (part1) {
			return "" + dfs(0, 30, 0, new int[rate.length * 31 * maskCount], maskCount);
		}
		int[] bestByMask = new int[maskCount];
		fill(0, 26, 0, 0, bestByMask);
		int[] bestSubset = bestByMask.clone();
		for (int bit = 1; bit < bestSubset.length; bit <<= 1) {
			for (int mask = 0; mask < bestSubset.length; mask++) {
				if ((mask & bit) != 0) {
					bestSubset[mask] = Math.max(bestSubset[mask], bestSubset[mask ^ bit]);
				}
			}
		}
		int answer = 0;
		int allValves = bestByMask.length - 1;
		for (int mask = 0; mask < bestByMask.length; mask++) {
			answer = Math.max(answer, bestByMask[mask] + bestSubset[allValves ^ mask]);
		}
		return answer + "";
	}

	Map<String, Integer> bfs(String start, Map<String, List<String>> edge) {
		Map<String, Integer> d = new HashMap<>();
		ArrayDeque<String> queue = new ArrayDeque<>();
		d.put(start, 0);
		queue.add(start);
		while (!queue.isEmpty()) {
			String current = queue.remove();
			for (String next : edge.get(current)) {
				if (!d.containsKey(next)) {
					d.put(next, d.get(current) + 1);
					queue.add(next);
				}
			}
		}
		return d;
	}

	int dfs(int current, int timeLeft, int openMask, int[] memo, int maskCount) {
		int key = (current * 31 + timeLeft) * maskCount + openMask;
		if (memo[key] != 0) {
			return memo[key] - 1;
		}
		int best = 0;
		for (int next = 1; next < rate.length; next++) {
			int bit = 1 << (next - 1);
			int nextTime = timeLeft - dist[current][next] - 1;
			if ((openMask & bit) == 0 && nextTime > 0) {
				best = Math.max(best, rate[next] * nextTime + dfs(next, nextTime, openMask | bit, memo, maskCount));
			}
		}
		memo[key] = best + 1;
		return best;
	}

	void fill(int current, int timeLeft, int openMask, int pressure, int[] bestByMask) {
		bestByMask[openMask] = Math.max(bestByMask[openMask], pressure);
		for (int next = 1; next < rate.length; next++) {
			int bit = 1 << (next - 1);
			int nextTime = timeLeft - dist[current][next] - 1;
			if ((openMask & bit) == 0 && nextTime > 0) {
				fill(next, nextTime, openMask | bit, pressure + rate[next] * nextTime, bestByMask);
			}
		}
	}
}
