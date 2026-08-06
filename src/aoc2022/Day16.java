package aoc2022;

import java.io.FileNotFoundException;
import java.util.*;

public class Day16 extends DayTemplate {

	private static final int MAX_TABLE_BITS = 20;
	private static final int UNREACHABLE = Integer.MAX_VALUE / 4;

	@Override
	public String[] fullSolve(Scanner in) throws FileNotFoundException {
		ValveNetwork network = parse(in);
		return new String[] { part1(network) + "", part2(network) + "" };
	}

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		ValveNetwork network = parse(in);
		return (part1 ? part1(network) : part2(network)) + "";
	}

	private int part1(ValveNetwork network) {
		return boundedBest(0, 30, 0L, 0, 0, network.distances(), network.rates());
	}

	private int part2(ValveNetwork network) {
		int flowValves = network.rates().length - 1;
		if (flowValves <= MAX_TABLE_BITS) {
			int[] bestByMask = new int[1 << flowValves];
			recordBestMasks(0, 26, 0, 0, network.distances(), network.rates(), bestByMask);
			return bestTwoActorPressure(bestByMask);
		}
		Map<Long, Integer> bestByMask = new HashMap<>();
		recordBestMasksBig(0, 26, 0L, 0, network.distances(), network.rates(), bestByMask);
		return bestTwoActorPressureBig(bestByMask);
	}

	private ValveNetwork parse(Scanner in) {
		List<String> names = new ArrayList<>();
		List<Integer> flows = new ArrayList<>();
		List<List<String>> tunnels = new ArrayList<>();
		Map<String, Integer> idsByName = new HashMap<>();
		while (in.hasNext()) {
			String[] line = in.nextLine().split(" ");
			List<String> exits = new ArrayList<>();
			for (int i = 9; i < line.length; i++) {
				exits.add(line[i].substring(0, 2));
			}
			idsByName.put(line[1], names.size());
			names.add(line[1]);
			flows.add(Integer.parseInt(line[4].substring(5, line[4].length() - 1)));
			tunnels.add(exits);
		}
		int[][] adjacency = new int[names.size()][];
		for (int i = 0; i < names.size(); i++) {
			adjacency[i] = tunnels.get(i).stream().mapToInt(idsByName::get).toArray();
		}
		List<Integer> useful = new ArrayList<>();
		useful.add(idsByName.get("AA"));
		List<Integer> flowIds = new ArrayList<>();
		for (int i = 0; i < names.size(); i++) {
			if (flows.get(i) > 0) {
				flowIds.add(i);
			}
		}
		flowIds.sort(Comparator.comparing((Integer id) -> flows.get(id)).reversed()
				.thenComparing(names::get));
		useful.addAll(flowIds);
		if (useful.size() - 1 > 62) {
			throw new IllegalStateException("Too many flow valves for bitmask search: " + (useful.size() - 1));
		}
		int[][] distances = new int[useful.size()][useful.size()];
		int[] rates = new int[useful.size()];
		for (int i = 0; i < useful.size(); i++) {
			int[] fromValve = distancesFrom(useful.get(i), adjacency);
			for (int j = 0; j < useful.size(); j++) {
				distances[i][j] = fromValve[useful.get(j)];
			}
			rates[i] = flows.get(useful.get(i));
		}
		return new ValveNetwork(distances, rates);
	}

	private int[] distancesFrom(int start, int[][] adjacency) {
		int[] distances = new int[adjacency.length];
		Arrays.fill(distances, UNREACHABLE);
		int[] queue = new int[adjacency.length];
		int head = 0;
		int tail = 0;
		distances[start] = 0;
		queue[tail++] = start;
		while (head < tail) {
			int current = queue[head++];
			int nextDistance = distances[current] + 1;
			for (int next : adjacency[current]) {
				if (distances[next] == UNREACHABLE) {
					distances[next] = nextDistance;
					queue[tail++] = next;
				}
			}
		}
		return distances;
	}

	/**
	 * Branch-and-bound best pressure for a single actor. The bound assumes every
	 * still-closed valve is reached along its direct shortest path from the current
	 * valve, which no schedule can beat, so pruning never discards the optimum.
	 * Valves are ordered by descending flow so strong branches are explored first.
	 */
	private int boundedBest(int current, int timeLeft, long openMask, int pressure, int best, int[][] distances,
			int[] rates) {
		if (pressure > best) {
			best = pressure;
		}
		int bound = pressure;
		for (int next = 1; next < rates.length; next++) {
			if ((openMask & (1L << (next - 1))) == 0) {
				int nextTime = timeLeft - distances[current][next] - 1;
				if (nextTime > 0) {
					bound += rates[next] * nextTime;
				}
			}
		}
		if (bound <= best) {
			return best;
		}
		for (int next = 1; next < rates.length; next++) {
			long bit = 1L << (next - 1);
			int nextTime = timeLeft - distances[current][next] - 1;
			if ((openMask & bit) == 0 && nextTime > 0) {
				best = boundedBest(next, nextTime, openMask | bit, pressure + rates[next] * nextTime, best, distances,
						rates);
			}
		}
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

	private int bestTwoActorPressure(int[] bestByMask) {
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
		return answer;
	}

	private void recordBestMasksBig(int current, int timeLeft, long openMask, int pressure, int[][] distances,
			int[] rates, Map<Long, Integer> bestByMask) {
		bestByMask.merge(openMask, pressure, Math::max);
		for (int next = 1; next < rates.length; next++) {
			long bit = 1L << (next - 1);
			int nextTime = timeLeft - distances[current][next] - 1;
			if ((openMask & bit) == 0 && nextTime > 0) {
				recordBestMasksBig(next, nextTime, openMask | bit, pressure + rates[next] * nextTime, distances, rates,
						bestByMask);
			}
		}
	}

	private int bestTwoActorPressureBig(Map<Long, Integer> bestByMask) {
		int size = bestByMask.size();
		long[] rawMasks = new long[size];
		int[] rawValues = new int[size];
		Integer[] order = new Integer[size];
		int index = 0;
		for (Map.Entry<Long, Integer> entry : bestByMask.entrySet()) {
			rawMasks[index] = entry.getKey();
			rawValues[index] = entry.getValue();
			order[index] = index;
			index++;
		}
		Arrays.sort(order, (a, b) -> Integer.compare(rawValues[b], rawValues[a]));
		long[] masks = new long[size];
		int[] values = new int[size];
		for (int i = 0; i < size; i++) {
			masks[i] = rawMasks[order[i]];
			values[i] = rawValues[order[i]];
		}
		int answer = 0;
		for (int i = 0; i < size; i++) {
			if (values[i] * 2 <= answer) {
				break;
			}
			for (int j = i; j < size; j++) {
				if (values[i] + values[j] <= answer) {
					break;
				}
				if ((masks[i] & masks[j]) == 0) {
					answer = values[i] + values[j];
				}
			}
		}
		return answer;
	}

	private record ValveNetwork(int[][] distances, int[] rates) {
	}
}
