package aoc2022;

import java.io.*;
import java.util.*;

public class Day16 extends DayTemplate {

	static int[][] usefulMatrix;
	static List<Valve> usefulValves = new ArrayList<>();
	static List<String> usefulNames = new ArrayList<>();
	static int best;
	static int maxFlow;
	static Map<String, Integer> cache1;
	static Map<String, Integer> cache2;

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		usefulValves = new ArrayList<>();
		usefulNames = new ArrayList<>();
		best = 0;
		maxFlow = 0;
		cache1 = new HashMap<String, Integer>();
		cache2 = new HashMap<String, Integer>();
		int answer = 0;
		List<Valve> valves = new ArrayList<>();
		int numNonZero = 0;
		while (in.hasNext()) {
			String[] line = in.nextLine().split(" ");
			List<String> tunnels = new ArrayList<>();
			for (int i = 9; i < line.length; i++) {
				tunnels.add(line[i].substring(0, 2));
			}
			Valve valve = new Valve(line[1], Integer.parseInt(line[4].substring(5, line[4].length() - 1)), tunnels);
			valves.add(valve);
		}
		Collections.sort(valves, (a,b) ->  a.flow - b.flow);
		for(int i = 0; i < valves.size(); i++) {
			Valve valve = valves.get(i);
			if (valve.flow > 0 || valve.name.equals("AA")) {
				numNonZero++;
				usefulNames.add(valve.name);
				usefulValves.add(valve);
				maxFlow += valve.flow;
			}
		}
		int[][] matrix = new int[valves.size()][valves.size()];
		for (int i = 0; i < valves.size(); i++) {
			for (int j = 0; j < valves.size(); j++) {
				matrix[i][j] = 99999; // INF
				if (valves.get(i).tunnels.contains(valves.get(j).name)) {
					matrix[i][j] = 1;
				}
				if (i == j) {
					matrix[i][j] = 0;
				}
			}
		}
		for (int k = 0; k < valves.size(); k++) {
			for (int i = 0; i < valves.size(); i++) {
				for (int j = 0; j < valves.size(); j++) {
					if (matrix[i][k] + matrix[k][j] < matrix[i][j]) {
						matrix[i][j] = matrix[i][k] + matrix[k][j];
					}
				}
			}
		}
		usefulMatrix = new int[numNonZero][numNonZero];
		int index1 = -1;
		int index2 = -1;
		for (int i = 0; i < valves.size(); i++) {
			if (valves.get(i).name.equals("AA") || valves.get(i).flow > 0) {
				index1++;
			}
			index2 = -1;
			for (int j = 0; j < valves.size(); j++) {
				if (valves.get(j).name.equals("AA") || valves.get(j).flow > 0) {
					index2++;
				}
				if ((valves.get(i).name.equals("AA") || valves.get(i).flow > 0)
						&& (valves.get(j).name.equals("AA") || valves.get(j).flow > 0)) {
					usefulMatrix[index1][index2] = matrix[i][j];
				}
			}
		}
		if (part1) {
			answer = helper(30, 0, usefulNames.indexOf("AA"), 0, new ArrayList<Integer>(), 0);
		} else {
			answer = helper2(26, 0, usefulNames.indexOf("AA"), 0, new ArrayList<Integer>());
		}
		return "" + answer;
	}

	public int helper(int minLeft, int current, int index, int currentFlow, List<Integer> open, int old) {
		String hash = "";
		for(int i = 0; i < usefulValves.size();i++) {
			hash+= open.contains(i)?"0":"1";
		}
		hash+="|" + index + "|" + minLeft;
		if(cache2.containsKey(hash)) {
			return cache2.get(hash) + current;
		}
		if (minLeft == 0) {
			return current;
		}
		if ((best - current - old) > maxFlow * minLeft) {
			return -1;
		}
		int ans = current + currentFlow * minLeft;
		for (int j = 0; j < usefulMatrix[0].length; j++) {
			if (!open.contains(j)) {
				List<Integer> newOpen = new ArrayList<>();
				newOpen.addAll(open);
				newOpen.add(j);
				if (minLeft > usefulMatrix[index][j]) {
					int newVal = helper(minLeft - usefulMatrix[index][j] - 1,
							current + (usefulMatrix[index][j] + 1) * currentFlow, j,
							currentFlow + usefulValves.get(j).flow, newOpen, old);
					ans = Math.max(ans, newVal);
				}
			}
		}
		cache2.put(hash, ans - current);
		return ans;
	}

	public int helper2(int minLeft, int current, int index, int currentFlow, List<Integer> open) {
		String hash = "";
		for(int i = 0; i < usefulValves.size();i++) {
			hash+= open.contains(i)?"0":"1";
		}
		hash+="|" + index + "|" + minLeft;
		if(cache1.containsKey(hash)) {
			return current + cache1.get(hash);
		}
		if (minLeft == 0) {
			cache1.put(hash, helper(26, 0, usefulNames.indexOf("AA"), 0, open, current));
			return current + helper(26, 0, usefulNames.indexOf("AA"), 0, open, current);
		}
		int ans = current + currentFlow * minLeft
				+ helper(26, 0, usefulNames.indexOf("AA"), 0, open, current + currentFlow * minLeft);
		for (int j = 0; j < usefulMatrix[0].length; j++) {
			if (!open.contains(j)) {
				List<Integer> newOpen = new ArrayList<>();
				newOpen.addAll(open);
				newOpen.add(j);
				if (minLeft > usefulMatrix[index][j]) {
					int newVal = helper2(minLeft - usefulMatrix[index][j] - 1,
							current + (usefulMatrix[index][j] + 1) * currentFlow, j,
							currentFlow + usefulValves.get(j).flow, newOpen);
					if (newVal > ans) {
						ans = newVal;
					}
				}
			}
		}
		cache1.put(hash, ans - current);
		return ans;
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