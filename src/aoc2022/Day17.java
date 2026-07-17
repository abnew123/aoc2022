package aoc2022;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Day17 extends DayTemplate {
	private static final int CHAMBER_WIDTH = 7;
	private static final long PART_1_ROCKS = 2022;
	private static final long PART_2_ROCKS = 1000000000000L;
	private static final Piece[] PIECES = {
			new Piece(new int[] { 0b1111 }, 4),
			new Piece(new int[] { 0b010, 0b111, 0b010 }, 3),
			new Piece(new int[] { 0b111, 0b100, 0b100 }, 3),
			new Piece(new int[] { 0b1, 0b1, 0b1, 0b1 }, 1),
			new Piece(new int[] { 0b11, 0b11 }, 2)
	};

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		boolean[] pushesLeft = parsePushes(in.nextLine());
		long goal = part1 ? PART_1_ROCKS : PART_2_ROCKS;
		return "" + simulate(goal, pushesLeft);
	}

	@Override
	public String[] fullSolve(Scanner in) {
		boolean[] pushesLeft = parsePushes(in.nextLine());
		long[] heights = simulate(PART_2_ROCKS, PART_1_ROCKS, pushesLeft);
		return new String[] { "" + heights[0], "" + heights[1] };
	}

	private boolean[] parsePushes(String pushes) {
		boolean[] pushesLeft = new boolean[pushes.length()];
		for (int i = 0; i < pushesLeft.length; i++) {
			pushesLeft[i] = pushes.charAt(i) == '<';
		}
		return pushesLeft;
	}

	private long simulate(long goal, boolean[] pushesLeft) {
		return simulate(goal, 0, pushesLeft)[1];
	}

	private long[] simulate(long goal, long checkpoint, boolean[] pushesLeft) {
		List<Integer> chamber = new ArrayList<>();
		Map<State, long[]> seen = new HashMap<>();
		int[] columnHeights = new int[CHAMBER_WIDTH];
		int jetIndex = 0;
		long rocks = 0;
		long skippedHeight = 0;
		long checkpointHeight = -1;
		long cycleRocks = 0;
		long cycleHeight = 0;
		boolean cycleConsumed = false;

		while (rocks < goal) {
			Piece piece = PIECES[(int) (rocks % PIECES.length)];
			int x = 2;
			int y = chamber.size() + 3;

			while (true) {
				int pushedX = x + (pushesLeft[jetIndex] ? -1 : 1);
				jetIndex = (jetIndex + 1) % pushesLeft.length;
				if (canMove(piece, pushedX, y, chamber)) {
					x = pushedX;
				}

				if (canMove(piece, x, y - 1, chamber)) {
					y--;
				} else {
					settle(piece, x, y, chamber, columnHeights);
					break;
				}
			}

			rocks++;
			if (rocks == checkpoint) {
				checkpointHeight = skippedHeight + chamber.size();
			}
			if (!cycleConsumed && cycleRocks == 0) {
				State state = new State((int) (rocks % PIECES.length), jetIndex, chamber, columnHeights);
				long height = skippedHeight + chamber.size();
				long[] previous = seen.get(state);
				if (previous == null) {
					seen.put(state, new long[] { rocks, height });
				} else {
					cycleRocks = rocks - previous[0];
					cycleHeight = height - previous[1];
				}
			}

			if (!cycleConsumed && cycleRocks > 0) {
				if (checkpoint > 0 && checkpointHeight < 0) {
					long cycles = (checkpoint - rocks) / cycleRocks;
					rocks += cycles * cycleRocks;
					skippedHeight += cycles * cycleHeight;
					if (rocks == checkpoint) {
						checkpointHeight = skippedHeight + chamber.size();
					}
				}

				if (checkpoint == 0 || checkpointHeight >= 0) {
					long cycles = (goal - rocks) / cycleRocks;
					rocks += cycles * cycleRocks;
					skippedHeight += cycles * cycleHeight;
					cycleConsumed = true;
				}
			}
		}

		return new long[] { checkpointHeight, skippedHeight + chamber.size() };
	}

	private boolean canMove(Piece piece, int x, int y, List<Integer> chamber) {
		if (x < 0 || x + piece.width > CHAMBER_WIDTH || y < 0) {
			return false;
		}
		for (int row = 0; row < piece.rows.length; row++) {
			int chamberY = y + row;
			if (chamberY < chamber.size() && ((piece.rows[row] << x) & chamber.get(chamberY)) != 0) {
				return false;
			}
		}
		return true;
	}

	private void settle(Piece piece, int x, int y, List<Integer> chamber, int[] columnHeights) {
		for (int row = 0; row < piece.rows.length; row++) {
			int chamberY = y + row;
			while (chamber.size() <= chamberY) {
				chamber.add(0);
			}
			// Each chamber row is a seven-bit mask; bit 0 is the left wall side.
			int shiftedRow = piece.rows[row] << x;
			chamber.set(chamberY, chamber.get(chamberY) | shiftedRow);
			for (int column = 0; column < CHAMBER_WIDTH; column++) {
				if ((shiftedRow & (1 << column)) != 0) {
					columnHeights[column] = Math.max(columnHeights[column], chamberY + 1);
				}
			}
		}
	}

	private static class Piece {
		private final int[] rows;
		private final int width;

		private Piece(int[] rows, int width) {
			this.rows = rows;
			this.width = width;
		}
	}

	private static class State {
		private final int pieceIndex;
		private final int jetIndex;
		private final int[] topRows;
		private final int[] columnDepths;

		private State(int pieceIndex, int jetIndex, List<Integer> chamber, int[] columnHeights) {
			this.pieceIndex = pieceIndex;
			this.jetIndex = jetIndex;
			int lowestReachableRow = min(columnHeights);
			this.topRows = new int[chamber.size() - lowestReachableRow];
			for (int i = 0; i < topRows.length; i++) {
				int y = chamber.size() - 1 - i;
				topRows[i] = chamber.get(y);
			}

			this.columnDepths = new int[CHAMBER_WIDTH];
			for (int x = 0; x < CHAMBER_WIDTH; x++) {
				columnDepths[x] = chamber.size() - columnHeights[x];
			}
		}

		private int min(int[] values) {
			int min = values[0];
			for (int value : values) {
				min = Math.min(min, value);
			}
			return min;
		}

		public boolean equals(Object other) {
			if (!(other instanceof State)) {
				return false;
			}
			State state = (State) other;
			return pieceIndex == state.pieceIndex
					&& jetIndex == state.jetIndex
					&& Arrays.equals(topRows, state.topRows)
					&& Arrays.equals(columnDepths, state.columnDepths);
		}

		public int hashCode() {
			int result = 31 * pieceIndex + jetIndex;
			result = 31 * result + Arrays.hashCode(topRows);
			result = 31 * result + Arrays.hashCode(columnDepths);
			return result;
		}
	}
}
