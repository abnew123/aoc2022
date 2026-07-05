package aoc2022;

import java.io.*;
import java.util.*;

public class Day23 extends DayTemplate {

	private static final int[] MOVE_ROWS = { -1, 1, 0, 0 };
	private static final int[] MOVE_COLS = { 0, 0, -1, 1 };

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
		private final int[] positions;
		private final int[] proposedPositions;
		private final int[] proposedDirections;
		private final int[] proposedElves;
		private final StampedGrid grid;

		Simulation(int[] rows, int[] cols) {
			this.rows = rows;
			this.cols = cols;
			this.positions = new int[rows.length];
			this.proposedPositions = new int[rows.length];
			this.proposedDirections = new int[rows.length];
			this.proposedElves = new int[rows.length];
			this.grid = new StampedGrid(rows, cols);
			this.grid.rebuildOccupied(rows, cols, positions);
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
			int proposedElfCount = 0;
			int firstDirection = round & 3;

			for (int i = 0; i < rows.length; i++) {
				int position = positions[i];

				if (!grid.hasAnyNeighbor(position)) {
					continue;
				}

				int direction = firstDirection;
				for (int attempt = 0; attempt < 4; attempt++) {
					if (grid.canMove(position, direction)) {
						int proposedPosition = grid.move(position, direction);
						proposedPositions[i] = proposedPosition;
						proposedDirections[i] = direction;
						proposedElves[proposedElfCount++] = i;
						grid.addProposal(proposedPosition);
						break;
					}
					direction = (direction + 1) & 3;
				}
			}

			boolean moved = false;
			for (int i = 0; i < proposedElfCount; i++) {
				int elf = proposedElves[i];
				if (grid.proposalCount(proposedPositions[elf]) == 1) {
					int direction = proposedDirections[elf];
					grid.moveOccupied(positions[elf], proposedPositions[elf]);
					positions[elf] = proposedPositions[elf];
					rows[elf] += MOVE_ROWS[direction];
					cols[elf] += MOVE_COLS[direction];
					moved = true;
				}
			}
			if (moved && grid.ensureOccupiedCovers(rows, cols)) {
				grid.rebuildOccupied(rows, cols, positions);
			}
			return moved;
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

		private int[] occupiedStamp;
		private int[] proposalStamp;
		private int[] proposalCounts;
		private int occupiedGeneration = 1;
		private int proposalGeneration = 1;
		private int baseRow;
		private int baseCol;
		private int height;
		private int width;

		StampedGrid(int[] rows, int[] cols) {
			resizeToFit(rows, cols, NEIGHBOR_MARGIN);
		}

		void rebuildOccupied(int[] rows, int[] cols, int[] positions) {
			ensureCovers(rows, cols, NEIGHBOR_MARGIN);
			occupiedGeneration = nextGeneration(occupiedGeneration, occupiedStamp);
			for (int i = 0; i < rows.length; i++) {
				int rowIndex = rows[i] - baseRow;
				int colIndex = cols[i] - baseCol;
				int position = rowIndex * width + colIndex;
				positions[i] = position;
				occupiedStamp[position] = occupiedGeneration;
			}
		}

		boolean ensureOccupiedCovers(int[] rows, int[] cols) {
			return ensureCovers(rows, cols, NEIGHBOR_MARGIN);
		}

		void clearProposals() {
			proposalGeneration = nextGeneration(proposalGeneration, proposalStamp);
		}

		boolean hasAnyNeighbor(int position) {
			int[] stamps = occupiedStamp;
			int generation = occupiedGeneration;
			int rowAbove = position - width;
			int rowBelow = position + width;
			return stamps[rowAbove - 1] == generation
					|| stamps[rowAbove] == generation
					|| stamps[rowAbove + 1] == generation
					|| stamps[position - 1] == generation
					|| stamps[position + 1] == generation
					|| stamps[rowBelow - 1] == generation
					|| stamps[rowBelow] == generation
					|| stamps[rowBelow + 1] == generation;
		}

		boolean canMove(int position, int direction) {
			int[] stamps = occupiedStamp;
			int generation = occupiedGeneration;
			switch (direction) {
			case 0:
				int north = position - width;
				return stamps[north - 1] != generation
						&& stamps[north] != generation
						&& stamps[north + 1] != generation;
			case 1:
				int south = position + width;
				return stamps[south - 1] != generation
						&& stamps[south] != generation
						&& stamps[south + 1] != generation;
			case 2:
				return stamps[position - width - 1] != generation
						&& stamps[position - 1] != generation
						&& stamps[position + width - 1] != generation;
			default:
				return stamps[position - width + 1] != generation
						&& stamps[position + 1] != generation
						&& stamps[position + width + 1] != generation;
			}
		}

		int move(int position, int direction) {
			switch (direction) {
			case 0:
				return position - width;
			case 1:
				return position + width;
			case 2:
				return position - 1;
			default:
				return position + 1;
			}
		}

		void addProposal(int position) {
			if (proposalStamp[position] != proposalGeneration) {
				proposalStamp[position] = proposalGeneration;
				proposalCounts[position] = 1;
			} else {
				proposalCounts[position]++;
			}
		}

		int proposalCount(int position) {
			if (proposalStamp[position] == proposalGeneration) {
				return proposalCounts[position];
			}
			return 0;
		}

		void moveOccupied(int oldPosition, int newPosition) {
			occupiedStamp[oldPosition] = 0;
			occupiedStamp[newPosition] = occupiedGeneration;
		}

		private boolean ensureCovers(int[] rows, int[] cols, int margin) {
			if (rows.length == 0) {
				return false;
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
					|| maxRow + margin >= baseRow + height
					|| minCol - margin < baseCol
					|| maxCol + margin >= baseCol + width) {
				resizeToFit(minRow, maxRow, minCol, maxCol, margin);
				return true;
			}
			return false;
		}

		private void resizeToFit(int[] rows, int[] cols, int margin) {
			if (rows.length == 0) {
				height = 8;
				width = 8;
				occupiedStamp = new int[height * width];
				proposalStamp = new int[height * width];
				proposalCounts = new int[height * width];
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
			height = rowSpan + padding * 2;
			width = colSpan + padding * 2;
			baseRow = minRow - margin - padding;
			baseCol = minCol - margin - padding;
			occupiedStamp = new int[height * width];
			proposalStamp = new int[height * width];
			proposalCounts = new int[height * width];
			occupiedGeneration = 1;
			proposalGeneration = 1;
		}

		private int nextGeneration(int generation, int[] stamps) {
			if (generation == Integer.MAX_VALUE) {
				Arrays.fill(stamps, 0);
				return 1;
			}
			return generation + 1;
		}
	}
}
