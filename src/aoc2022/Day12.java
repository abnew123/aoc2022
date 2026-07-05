package aoc2022;

import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day12 extends DayTemplate {

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		List<String> lines = new ArrayList<>();
		while (in.hasNextLine()) {
			lines.add(in.nextLine());
		}

		int rows = lines.size();
		int cols = lines.get(0).length();
		int[] heights = new int[rows * cols];
		int start = -1;
		int end = -1;
		for (int row = 0; row < rows; row++) {
			String line = lines.get(row);
			for (int col = 0; col < cols; col++) {
				char c = line.charAt(col);
				int index = row * cols + col;
				if (c == 'S') {
					start = index;
					heights[index] = 0;
				} else if (c == 'E') {
					end = index;
					heights[index] = 25;
				} else {
					heights[index] = c - 'a';
				}
			}
		}

		int[] distances = reverseDistancesFromEnd(heights, rows, cols, end);
		if (part1) {
			return "" + distances[start];
		}

		int best = Integer.MAX_VALUE;
		for (int i = 0; i < heights.length; i++) {
			if (heights[i] == 0 && distances[i] >= 0 && distances[i] < best) {
				best = distances[i];
			}
		}
		return "" + best;
	}

	private int[] reverseDistancesFromEnd(int[] heights, int rows, int cols, int end) {
		int[] distances = new int[heights.length];
		for (int i = 0; i < distances.length; i++) {
			distances[i] = -1;
		}

		ArrayDeque<Integer> queue = new ArrayDeque<>();
		distances[end] = 0;
		queue.add(end);
		while (!queue.isEmpty()) {
			int current = queue.poll();
			int row = current / cols;
			int col = current % cols;
			int nextDistance = distances[current] + 1;
			int currentHeight = heights[current];

			if (row > 0) {
				addIfReachable(currentHeight, current - cols, nextDistance, heights, distances, queue);
			}
			if (row + 1 < rows) {
				addIfReachable(currentHeight, current + cols, nextDistance, heights, distances, queue);
			}
			if (col > 0) {
				addIfReachable(currentHeight, current - 1, nextDistance, heights, distances, queue);
			}
			if (col + 1 < cols) {
				addIfReachable(currentHeight, current + 1, nextDistance, heights, distances, queue);
			}
		}
		return distances;
	}

	private void addIfReachable(int currentHeight, int next, int nextDistance, int[] heights, int[] distances,
			ArrayDeque<Integer> queue) {
		if (distances[next] == -1 && heights[next] >= currentHeight - 1) {
			distances[next] = nextDistance;
			queue.add(next);
		}
	}
}
