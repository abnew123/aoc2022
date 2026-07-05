package aoc2022;

import java.io.FileNotFoundException;
import java.util.*;

public class Day16 extends DayTemplate {

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		Map<String, Valve> valvesByName = new HashMap<>();
		while (in.hasNext()) {
			String[] line = in.nextLine().split(" ");
			List<String> tunnels = new ArrayList<>();
			for (int i = 9; i < line.length; i++) {
				tunnels.add(line[i].substring(0, 2));
			}
			valvesByName.put(line[1], new Valve(line[1], Integer.parseInt(line[4].substring(5, line[4].length() - 1)), tunnels));
		}
		List<Valve> usefulValves = new ArrayList<>();
		usefulValves.add(valvesByName.get("AA"));
		for (Valve valve : valvesByName.values()) {
			if (valve.flow > 0) {
				usefulValves.add(valve);
			}
		}
		usefulValves.subList(1, usefulValves.size()).sort(Comparator.comparing(valve -> valve.name));
		int[][] distances = usefulDistances(usefulValves, valvesByName);
		int[] rates = new int[usefulValves.size()];
		for (int i = 0; i < usefulValves.size(); i++) {
			rates[i] = usefulValves.get(i).flow;
		}
		if (part1) {
			return bestPressure(0, 30, 0, distances, rates, new HashMap<>()) + "";
		}
		int[] bestByMask = new int[1 << (rates.length - 1)];
		recordBestMasks(0, 26, 0, 0, distances, rates, bestByMask);
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

	private int[][] usefulDistances(List<Valve> usefulValves, Map<String, Valve> valvesByName) {
		int[][] distances = new int[usefulValves.size()][usefulValves.size()];
		for (int i = 0; i < usefulValves.size(); i++) {
			Map<String, Integer> fromValve = distancesFrom(usefulValves.get(i).name, valvesByName);
			for (int j = 0; j < usefulValves.size(); j++) {
				distances[i][j] = fromValve.get(usefulValves.get(j).name);
			}
		}
		return distances;
	}

	private Map<String, Integer> distancesFrom(String start, Map<String, Valve> valvesByName) {
		Map<String, Integer> distances = new HashMap<>();
		ArrayDeque<String> queue = new ArrayDeque<>();
		distances.put(start, 0);
		queue.add(start);
		while (!queue.isEmpty()) {
			String current = queue.poll();
			int nextDistance = distances.get(current) + 1;
			for (String next : valvesByName.get(current).tunnels) {
				if (!distances.containsKey(next)) {
					distances.put(next, nextDistance);
					queue.add(next);
				}
			}
		}
		return distances;
	}

	private int bestPressure(int current, int timeLeft, int openMask, int[][] distances, int[] rates, Map<Long, Integer> memo) {
		long key = (((long) current) << 48) | (((long) timeLeft) << 32) | openMask;
		if (memo.containsKey(key)) {
			return memo.get(key);
		}
		int best = 0;
		for (int next = 1; next < rates.length; next++) {
			int bit = 1 << (next - 1);
			int nextTime = timeLeft - distances[current][next] - 1;
			if ((openMask & bit) == 0 && nextTime > 0) {
				int released = rates[next] * nextTime;
				best = Math.max(best, released + bestPressure(next, nextTime, openMask | bit, distances, rates, memo));
			}
		}
		memo.put(key, best);
		return best;
	}

	private void recordBestMasks(int current, int timeLeft, int openMask, int pressure, int[][] distances, int[] rates,
			int[] bestByMask) {
		bestByMask[openMask] = Math.max(bestByMask[openMask], pressure);
		for (int next = 1; next < rates.length; next++) {
			int bit = 1 << (next - 1);
			int nextTime = timeLeft - distances[current][next] - 1;
			if ((openMask & bit) == 0 && nextTime > 0) {
				recordBestMasks(next, nextTime, openMask | bit, pressure + rates[next] * nextTime, distances, rates,
						bestByMask);
			}
		}
	}
}

class Valve {
	int flow;
	String name;
	List<String> tunnels = new ArrayList<>();

	public Valve(String name, int flow, List<String> tunnels) {
		this.name = name;
		this.flow = flow;
		this.tunnels.addAll(tunnels);
	}
}
