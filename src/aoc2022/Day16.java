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

	/**
	 * One slurp, manual scan. Line fields are the single-space-separated tokens
	 * of the incumbent parse: token 1 is the valve name, token 4 carries
	 * "rate=N;", tokens 9+ are exit names (first two characters). Whitespace-only
	 * lines are skipped. Valve ids are file order; duplicate names resolve to the
	 * last occurrence.
	 */
	private ValveNetwork parse(Scanner in) {
		String text = in.useDelimiter("\\A").hasNext() ? in.next() : "";
		int n = text.length();
		String[] names = new String[16];
		int[] flows = new int[16];
		String[][] exits = new String[16][];
		int valveCount = 0;
		int i = 0;
		while (i < n) {
			int lineStart = i;
			while (i < n && text.charAt(i) != '\n' && text.charAt(i) != '\r') {
				i++;
			}
			int lineEnd = i;
			if (i < n) {
				if (text.charAt(i) == '\r' && i + 1 < n && text.charAt(i + 1) == '\n') {
					i++;
				}
				i++;
			}
			boolean blank = true;
			for (int k = lineStart; k < lineEnd; k++) {
				if (!Character.isWhitespace(text.charAt(k))) {
					blank = false;
					break;
				}
			}
			if (blank) {
				continue;
			}
			String[] tokens = new String[12];
			int tokenCount = 0;
			int tokenStart = lineStart;
			for (int k = lineStart; k <= lineEnd; k++) {
				if (k == lineEnd || text.charAt(k) == ' ') {
					if (tokenCount == tokens.length) {
						tokens = Arrays.copyOf(tokens, tokens.length * 2);
					}
					tokens[tokenCount++] = text.substring(tokenStart, k);
					tokenStart = k + 1;
				}
			}
			while (tokenCount > 0 && tokens[tokenCount - 1].isEmpty()) {
				tokenCount--;
			}
			if (tokenCount < 5) {
				throw new IllegalArgumentException("Malformed valve line");
			}
			String flowToken = tokens[4];
			int flow = Integer.parseInt(flowToken.substring(5, flowToken.length() - 1));
			String[] valveExits = new String[Math.max(0, tokenCount - 9)];
			for (int t = 9; t < tokenCount; t++) {
				valveExits[t - 9] = tokens[t].substring(0, 2);
			}
			if (valveCount == names.length) {
				names = Arrays.copyOf(names, valveCount * 2);
				flows = Arrays.copyOf(flows, valveCount * 2);
				exits = Arrays.copyOf(exits, valveCount * 2);
			}
			names[valveCount] = tokens[1];
			flows[valveCount] = flow;
			exits[valveCount] = valveExits;
			valveCount++;
		}
		int[][] adjacency = new int[valveCount][];
		for (int v = 0; v < valveCount; v++) {
			int[] ids = new int[exits[v].length];
			for (int e = 0; e < ids.length; e++) {
				ids[e] = idOf(names, valveCount, exits[v][e]);
			}
			adjacency[v] = ids;
		}
		int flowIdCount = 0;
		for (int v = 0; v < valveCount; v++) {
			if (flows[v] > 0) {
				flowIdCount++;
			}
		}
		int[] flowIds = new int[flowIdCount];
		int next = 0;
		for (int v = 0; v < valveCount; v++) {
			if (flows[v] > 0) {
				flowIds[next++] = v;
			}
		}
		// Stable insertion sort: flow descending, ties by name ascending.
		for (int a = 1; a < flowIdCount; a++) {
			int id = flowIds[a];
			int b = a - 1;
			while (b >= 0 && sortsBefore(id, flowIds[b], flows, names)) {
				flowIds[b + 1] = flowIds[b];
				b--;
			}
			flowIds[b + 1] = id;
		}
		int[] useful = new int[1 + flowIdCount];
		useful[0] = idOf(names, valveCount, "AA");
		for (int u = 0; u < flowIdCount; u++) {
			useful[u + 1] = flowIds[u];
		}
		if (useful.length - 1 > 62) {
			throw new IllegalStateException("Too many flow valves for bitmask search: " + (useful.length - 1));
		}
		int[][] distances = new int[useful.length][useful.length];
		int[] rates = new int[useful.length];
		for (int a = 0; a < useful.length; a++) {
			int[] fromValve = distancesFrom(useful[a], adjacency);
			for (int b = 0; b < useful.length; b++) {
				distances[a][b] = fromValve[useful[b]];
			}
			rates[a] = flows[useful[a]];
		}
		return new ValveNetwork(distances, rates);
	}

	/** Last matching id, mirroring the incumbent's map overwrite on duplicate names. */
	private int idOf(String[] names, int valveCount, String name) {
		for (int v = valveCount - 1; v >= 0; v--) {
			if (names[v].equals(name)) {
				return v;
			}
		}
		throw new IllegalArgumentException("Unknown valve name");
	}

	private boolean sortsBefore(int left, int right, int[] flows, String[] names) {
		if (flows[left] != flows[right]) {
			return flows[left] > flows[right];
		}
		return names[left].compareTo(names[right]) < 0;
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
