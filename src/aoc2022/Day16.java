package aoc2022;

import java.io.FileNotFoundException;
import java.util.*;

public class Day16 extends DayTemplate {

	int[][] usefulMatrix;
	List<Valve> usefulValves = new ArrayList<>();
	Map<Integer, Integer> cache = new HashMap<>();

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		int answer = 0;
		List<Valve> valves = new ArrayList<>();
		while (in.hasNext()) {
			String[] line = in.nextLine().split(" ");
			List<String> tunnels = new ArrayList<>();
			for (int i = 9; i < line.length; i++) {
				tunnels.add(line[i].substring(0, 2));
			}
			Valve valve = new Valve(line[1], Integer.parseInt(line[4].substring(5, line[4].length() - 1)), tunnels);
			valves.add(valve);
		}
		Collections.sort(valves, (a, b) -> a.name.compareTo(b.name));
		for (int i = 0; i < valves.size(); i++) {
			Valve valve = valves.get(i);
			if (valve.flow > 0 || valves.get(i).name.equals("AA")) {
				usefulValves.add(valve);
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
		usefulMatrix = new int[usefulValves.size()][usefulValves.size()];
		int index1 = -1;
		for (int i = 0; i < valves.size(); i++) {
			if (i == 0 || valves.get(i).flow > 0) {
				index1++;
			}
			int index2 = -1;
			for (int j = 0; j < valves.size(); j++) {
				if (j == 0 || valves.get(j).flow > 0) {
					index2++;
				}
				if ((i == 0 || valves.get(i).flow > 0) && (j == 0 || valves.get(j).flow > 0)) {
					usefulMatrix[index1][index2] = matrix[i][j];
				}
			}
		}
		helper(part1?30:26,0,0,0,0);
		if (part1) {
			for (Integer key : cache.keySet()) {
				if (cache.get(key) > answer) {
					answer = cache.get(key);
				}
			}
		} else {
			for (Integer key1 : cache.keySet()) {
				for (Integer key2 : cache.keySet()) {
					if ((key1 & key2) == 0) {
						if (cache.get(key1) + cache.get(key2) > answer) {
							answer = cache.get(key1) + cache.get(key2);
						}
					}
				}
			}
		}
		return "" + answer;
	}

	public void helper(int minLeft, int current, int index, int currentFlow, int open) {
		int end = current + currentFlow * minLeft;
		if (!cache.containsKey(open) || end > cache.get(open)) {
			cache.put(open, end);
		}
		for (int j = 1; j < usefulMatrix[0].length; j++) {
			if ((open % (1 << (j + 1)) == open % (1 << j)) && minLeft > usefulMatrix[index][j]) {
				helper(minLeft - usefulMatrix[index][j] - 1, current + (usefulMatrix[index][j] + 1) * currentFlow, j,
						currentFlow + usefulValves.get(j).flow, open + (1 << j));
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