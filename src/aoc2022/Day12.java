package aoc2022;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Day12 extends DayTemplate {

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		Answers answers = analyze(in);
		return "" + (part1 ? answers.fromStart : answers.fromAnyLowest);
	}

	@Override
	public String[] fullSolve(Scanner in) throws FileNotFoundException {
		Answers answers = analyze(in);
		return new String[]{"" + answers.fromStart, "" + answers.fromAnyLowest};
	}

	private Answers analyze(Scanner in) {
		String input = in.useDelimiter("\\A").hasNext() ? in.next() : "";
		int[] lineStarts = new int[16];
		int[] lineEnds = new int[16];
		int lineCount = 0;
		int offset = 0;
		while (offset < input.length()) {
			int lineStart = offset;
			while (offset < input.length() && input.charAt(offset) != '\n'
					&& input.charAt(offset) != '\r') {
				offset++;
			}
			int lineEnd = offset;
			if (offset < input.length()) {
				char ending = input.charAt(offset++);
				if (ending == '\r' && offset < input.length() && input.charAt(offset) == '\n') {
					offset++;
				}
			}
			if (lineCount == lineStarts.length) {
				lineStarts = Arrays.copyOf(lineStarts, lineCount * 2);
				lineEnds = Arrays.copyOf(lineEnds, lineCount * 2);
			}
			lineStarts[lineCount] = lineStart;
			lineEnds[lineCount++] = lineEnd;
		}
		if (lineCount == 0 || lineEnds[0] == lineStarts[0]) {
			throw new IllegalArgumentException("Height map must not be empty");
		}

		int rows = lineCount;
		int cols = lineEnds[0] - lineStarts[0];
		int[] heights = new int[Math.multiplyExact(rows, cols)];
		int start = -1;
		int end = -1;
		for (int row = 0; row < rows; row++) {
			int lineStart = lineStarts[row];
			if (lineEnds[row] - lineStart != cols) {
				throw new IllegalArgumentException("Height map must be rectangular");
			}
			for (int col = 0; col < cols; col++) {
				char c = input.charAt(lineStart + col);
				int index = row * cols + col;
				if (c == 'S') {
					if (start >= 0) {
						throw new IllegalArgumentException("Height map has multiple starts");
					}
					start = index;
					heights[index] = 0;
				} else if (c == 'E') {
					if (end >= 0) {
						throw new IllegalArgumentException("Height map has multiple ends");
					}
					end = index;
					heights[index] = 25;
				} else if (c >= 'a' && c <= 'z') {
					heights[index] = c - 'a';
				} else {
					throw new IllegalArgumentException("Invalid height: " + c);
				}
			}
		}
		if (start < 0 || end < 0) {
			throw new IllegalArgumentException("Height map requires one start and one end");
		}

		int[] distances = reverseDistancesFromEnd(heights, rows, cols, end);
		int best = Integer.MAX_VALUE;
		for (int i = 0; i < heights.length; i++) {
			if (heights[i] == 0 && distances[i] >= 0 && distances[i] < best) {
				best = distances[i];
			}
		}
		return new Answers(distances[start], best);
	}

	private int[] reverseDistancesFromEnd(int[] heights, int rows, int cols, int end) {
		int[] distances = new int[heights.length];
		Arrays.fill(distances, -1);

		int[] queue = new int[heights.length];
		int head = 0;
		int tail = 0;
		distances[end] = 0;
		queue[tail++] = end;
		while (head < tail) {
			int current = queue[head++];
			int row = current / cols;
			int col = current % cols;
			int nextDistance = distances[current] + 1;
			int currentHeight = heights[current];

			if (row > 0) {
				tail = addIfReachable(currentHeight, current - cols, nextDistance, heights, distances, queue, tail);
			}
			if (row + 1 < rows) {
				tail = addIfReachable(currentHeight, current + cols, nextDistance, heights, distances, queue, tail);
			}
			if (col > 0) {
				tail = addIfReachable(currentHeight, current - 1, nextDistance, heights, distances, queue, tail);
			}
			if (col + 1 < cols) {
				tail = addIfReachable(currentHeight, current + 1, nextDistance, heights, distances, queue, tail);
			}
		}
		return distances;
	}

	private int addIfReachable(int currentHeight, int next, int nextDistance, int[] heights, int[] distances,
			int[] queue, int tail) {
		if (distances[next] == -1 && heights[next] >= currentHeight - 1) {
			distances[next] = nextDistance;
			queue[tail++] = next;
		}
		return tail;
	}

	private record Answers(int fromStart, int fromAnyLowest) {}
}
