package aoc2022;

import java.io.*;
import java.util.*;

public class Day23 extends DayTemplate {

	private static final int[] NEIGHBOR_ROWS = { -1, -1, -1, 0, 0, 1, 1, 1 };
	private static final int[] NEIGHBOR_COLS = { -1, 0, 1, -1, 1, -1, 0, 1 };
	private static final int[] MOVE_ROWS = { -1, 1, 0, 0 };
	private static final int[] MOVE_COLS = { 0, 0, -1, 1 };
	private static final int[][] CHECK_ROWS = {
			{ -1, -1, -1 },
			{ 1, 1, 1 },
			{ -1, 0, 1 },
			{ -1, 0, 1 }
	};
	private static final int[][] CHECK_COLS = {
			{ -1, 0, 1 },
			{ -1, 0, 1 },
			{ -1, -1, -1 },
			{ 1, 1, 1 }
	};

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		List<String> lines = new ArrayList<>();
		int elfCount = 0;
		while (in.hasNextLine()) {
			String line = in.nextLine();
			lines.add(line);
			for (int col = 0; col < line.length(); col++) {
				if (line.charAt(col) == '#') {
					elfCount++;
				}
			}
		}

		int[] rows = new int[elfCount];
		int[] cols = new int[elfCount];
		int index = 0;
		for (int row = 0; row < lines.size(); row++) {
			String line = lines.get(row);
			for (int col = 0; col < line.length(); col++) {
				if (line.charAt(col) == '#') {
					rows[index] = row;
					cols[index] = col;
					index++;
				}
			}
		}

		Simulation simulation = new Simulation(rows, cols);
		if (part1) {
			simulation.runRounds(10);
			return "" + simulation.emptyGroundInBoundingBox();
		}
		return "" + simulation.firstRoundWithoutMovement();
	}

	private static final class Simulation {
		private final int[] rows;
		private final int[] cols;
		private final int[] proposedRows;
		private final int[] proposedCols;
		private final boolean[] hasProposal;
		private final StampedGrid grid;

		Simulation(int[] rows, int[] cols) {
			this.rows = rows;
			this.cols = cols;
			this.proposedRows = new int[rows.length];
			this.proposedCols = new int[rows.length];
			this.hasProposal = new boolean[rows.length];
			this.grid = new StampedGrid(rows, cols);
			this.grid.rebuildOccupied(rows, cols);
		}

		void runRounds(int rounds) {
			for (int round = 0; round < rounds; round++) {
				runRound(round);
			}
		}

		int firstRoundWithoutMovement() {
			for (int round = 0;; round++) {
				if (!runRound(round)) {
					return round + 1;
				}
			}
		}

		long emptyGroundInBoundingBox() {
			if (rows.length == 0) {
				return 0;
			}
			int minRow = rows[0];
			int maxRow = rows[0];
			int minCol = cols[0];
			int maxCol = cols[0];
			for (int i = 1; i < rows.length; i++) {
				minRow = Math.min(minRow, rows[i]);
				maxRow = Math.max(maxRow, rows[i]);
				minCol = Math.min(minCol, cols[i]);
				maxCol = Math.max(maxCol, cols[i]);
			}
			long area = (long) (maxRow - minRow + 1) * (maxCol - minCol + 1);
			return area - rows.length;
		}

		private boolean runRound(int round) {
			grid.clearProposals();
			Arrays.fill(hasProposal, false);

			for (int i = 0; i < rows.length; i++) {
				int row = rows[i];
				int col = cols[i];
				if (!hasAnyNeighbor(row, col)) {
					continue;
				}

				for (int attempt = 0; attempt < 4; attempt++) {
					int direction = (round + attempt) % 4;
					if (canMove(row, col, direction)) {
						int proposedRow = row + MOVE_ROWS[direction];
						int proposedCol = col + MOVE_COLS[direction];
						proposedRows[i] = proposedRow;
						proposedCols[i] = proposedCol;
						hasProposal[i] = true;
						grid.addProposal(proposedRow, proposedCol);
						break;
					}
				}
			}

			boolean moved = false;
			for (int i = 0; i < rows.length; i++) {
				if (hasProposal[i] && grid.proposalCount(proposedRows[i], proposedCols[i]) == 1) {
					rows[i] = proposedRows[i];
					cols[i] = proposedCols[i];
					moved = true;
				}
			}
			if (moved) {
				grid.rebuildOccupied(rows, cols);
			}
			return moved;
		}

		private boolean hasAnyNeighbor(int row, int col) {
			for (int i = 0; i < NEIGHBOR_ROWS.length; i++) {
				if (grid.isOccupied(row + NEIGHBOR_ROWS[i], col + NEIGHBOR_COLS[i])) {
					return true;
				}
			}
			return false;
		}

		private boolean canMove(int row, int col, int direction) {
			for (int i = 0; i < 3; i++) {
				if (grid.isOccupied(row + CHECK_ROWS[direction][i], col + CHECK_COLS[direction][i])) {
					return false;
				}
			}
			return true;
		}
	}

	/*
	 * The grid expands from the current elf bounds, so the simulation is not tied
	 * to the original fixed 300x300 puzzle-sized array. Stamps avoid clearing the
	 * full grid each round: a cell is occupied or proposed only when its stored
	 * stamp matches the current generation.
	 */
	private static final class StampedGrid {
		private static final int NEIGHBOR_MARGIN = 2;
		private static final int MIN_PADDING = 32;

		private int[][] occupiedStamp;
		private int[][] proposalStamp;
		private int[][] proposalCounts;
		private int occupiedGeneration = 1;
		private int proposalGeneration = 1;
		private int baseRow;
		private int baseCol;

		StampedGrid(int[] rows, int[] cols) {
			resizeToFit(rows, cols, NEIGHBOR_MARGIN);
		}

		void rebuildOccupied(int[] rows, int[] cols) {
			ensureCovers(rows, cols, NEIGHBOR_MARGIN);
			occupiedGeneration = nextGeneration(occupiedGeneration, occupiedStamp);
			for (int i = 0; i < rows.length; i++) {
				int rowIndex = rows[i] - baseRow;
				int colIndex = cols[i] - baseCol;
				occupiedStamp[rowIndex][colIndex] = occupiedGeneration;
			}
		}

		void clearProposals() {
			proposalGeneration = nextGeneration(proposalGeneration, proposalStamp);
		}

		boolean isOccupied(int row, int col) {
			int rowIndex = row - baseRow;
			int colIndex = col - baseCol;
			return rowIndex >= 0 && rowIndex < occupiedStamp.length
					&& colIndex >= 0 && colIndex < occupiedStamp[0].length
					&& occupiedStamp[rowIndex][colIndex] == occupiedGeneration;
		}

		void addProposal(int row, int col) {
			int rowIndex = row - baseRow;
			int colIndex = col - baseCol;
			if (proposalStamp[rowIndex][colIndex] != proposalGeneration) {
				proposalStamp[rowIndex][colIndex] = proposalGeneration;
				proposalCounts[rowIndex][colIndex] = 1;
			} else {
				proposalCounts[rowIndex][colIndex]++;
			}
		}

		int proposalCount(int row, int col) {
			int rowIndex = row - baseRow;
			int colIndex = col - baseCol;
			if (proposalStamp[rowIndex][colIndex] == proposalGeneration) {
				return proposalCounts[rowIndex][colIndex];
			}
			return 0;
		}

		private void ensureCovers(int[] rows, int[] cols, int margin) {
			if (rows.length == 0) {
				return;
			}
			int minRow = rows[0];
			int maxRow = rows[0];
			int minCol = cols[0];
			int maxCol = cols[0];
			for (int i = 1; i < rows.length; i++) {
				minRow = Math.min(minRow, rows[i]);
				maxRow = Math.max(maxRow, rows[i]);
				minCol = Math.min(minCol, cols[i]);
				maxCol = Math.max(maxCol, cols[i]);
			}
			if (occupiedStamp == null
					|| minRow - margin < baseRow
					|| maxRow + margin >= baseRow + occupiedStamp.length
					|| minCol - margin < baseCol
					|| maxCol + margin >= baseCol + occupiedStamp[0].length) {
				resizeToFit(minRow, maxRow, minCol, maxCol, margin);
			}
		}

		private void resizeToFit(int[] rows, int[] cols, int margin) {
			if (rows.length == 0) {
				occupiedStamp = new int[8][8];
				proposalStamp = new int[8][8];
				proposalCounts = new int[8][8];
				baseRow = -4;
				baseCol = -4;
				return;
			}

			int minRow = rows[0];
			int maxRow = rows[0];
			int minCol = cols[0];
			int maxCol = cols[0];
			for (int i = 1; i < rows.length; i++) {
				minRow = Math.min(minRow, rows[i]);
				maxRow = Math.max(maxRow, rows[i]);
				minCol = Math.min(minCol, cols[i]);
				maxCol = Math.max(maxCol, cols[i]);
			}
			resizeToFit(minRow, maxRow, minCol, maxCol, margin);
		}

		private void resizeToFit(int minRow, int maxRow, int minCol, int maxCol, int margin) {
			int rowSpan = maxRow - minRow + 1 + margin * 2;
			int colSpan = maxCol - minCol + 1 + margin * 2;
			int padding = Math.max(MIN_PADDING, Math.max(rowSpan, colSpan));
			int height = rowSpan + padding * 2;
			int width = colSpan + padding * 2;
			baseRow = minRow - margin - padding;
			baseCol = minCol - margin - padding;
			occupiedStamp = new int[height][width];
			proposalStamp = new int[height][width];
			proposalCounts = new int[height][width];
			occupiedGeneration = 1;
			proposalGeneration = 1;
		}

		private int nextGeneration(int generation, int[][] stamps) {
			if (generation == Integer.MAX_VALUE) {
				for (int row = 0; row < stamps.length; row++) {
					Arrays.fill(stamps[row], 0);
				}
				return 1;
			}
			return generation + 1;
		}
	}
}
