package aoc2022;

import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/** Generic flat-map and folded-cube walker. */
public class Day22 extends DayTemplate {

	private static final int[] ROW_STEP = {0, 1, 0, -1};
	private static final int[] COL_STEP = {1, 0, -1, 0};

	@Override
	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		Parsed parsed = parse(in);
		return Integer.toString(walk(parsed, !part1));
	}

	@Override
	public String[] fullSolve(Scanner in) throws FileNotFoundException {
		Parsed parsed = parse(in);
		return new String[] {
				Integer.toString(walk(parsed, false)),
				Integer.toString(walk(parsed, true))
		};
	}

	private static int walk(Parsed parsed, boolean cube) {
		char[][] map = parsed.map;
		int row = parsed.firstMapRow;
		int col = parsed.rowFirst[row];
		while (col <= parsed.rowLast[row] && map[row][col] != '.') {
			col++;
		}
		if (col > parsed.rowLast[row]) {
			throw new IllegalArgumentException("Top map row contains no open tile");
		}

		int direction = 0;
		String path = parsed.path;
		for (int index = 0; index < path.length();) {
			char instruction = path.charAt(index);
			if (instruction == 'L' || instruction == 'R') {
				direction = (direction + (instruction == 'R' ? 1 : 3)) & 3;
				index++;
				continue;
			}

			if (instruction < '0' || instruction > '9') {
				throw new IllegalArgumentException("Unexpected path character: " + instruction);
			}
			int distance = 0;
			while (index < path.length()) {
				char digit = path.charAt(index);
				if (digit < '0' || digit > '9') {
					break;
				}
				distance = distance * 10 + digit - '0';
				index++;
			}

			for (int step = 0; step < distance; step++) {
				int nextRow = row + ROW_STEP[direction];
				int nextCol = col + COL_STEP[direction];
				int nextDirection = direction;
				boolean outside = nextRow < 0 || nextRow >= parsed.rows
						|| nextCol < 0 || nextCol >= parsed.cols
						|| map[nextRow][nextCol] == ' ';

				if (outside) {
					if (!cube) {
						switch (direction) {
							case 0:
								nextCol = parsed.rowFirst[row];
								nextRow = row;
								break;
							case 1:
								nextRow = parsed.colFirst[col];
								nextCol = col;
								break;
							case 2:
								nextCol = parsed.rowLast[row];
								nextRow = row;
								break;
							default:
								nextRow = parsed.colLast[col];
								nextCol = col;
						}
					} else {
						Face from = parsed.faces[parsed.faceAt[row][col]];
						Transition transition = from.transitions[direction];
						Face to = parsed.faces[transition.face];
						int offset = (direction & 1) == 0 ? row - from.top : col - from.left;
						if (transition.reverse) {
							offset = parsed.side - 1 - offset;
						}
						nextDirection = transition.direction;
						switch (nextDirection) {
							case 0: // Enter through the target's left edge.
								nextRow = to.top + offset;
								nextCol = to.left;
								break;
							case 1: // Enter through the target's top edge.
								nextRow = to.top;
								nextCol = to.left + offset;
								break;
							case 2: // Enter through the target's right edge.
								nextRow = to.top + offset;
								nextCol = to.left + parsed.side - 1;
								break;
							default: // Enter through the target's bottom edge.
								nextRow = to.top + parsed.side - 1;
								nextCol = to.left + offset;
						}
					}
				}

				if (map[nextRow][nextCol] == '#') {
					break;
				}
				row = nextRow;
				col = nextCol;
				direction = nextDirection;
			}
		}
		return 1000 * (row + 1) + 4 * (col + 1) + direction;
	}

	private static Parsed parse(Scanner in) {
		List<String> lines = new ArrayList<>();
		while (in.hasNextLine()) {
			lines.add(in.nextLine());
		}
		int separator = 0;
		while (separator < lines.size() && !lines.get(separator).trim().isEmpty()) {
			separator++;
		}
		if (separator == lines.size()) {
			throw new IllegalArgumentException("Missing blank line before movement path");
		}
		int pathLine = separator + 1;
		while (pathLine < lines.size() && lines.get(pathLine).trim().isEmpty()) {
			pathLine++;
		}
		if (separator == 0 || pathLine == lines.size()) {
			throw new IllegalArgumentException("Incomplete map or movement path");
		}

		int rows = separator;
		int cols = 0;
		for (int row = 0; row < rows; row++) {
			cols = Math.max(cols, lines.get(row).length());
		}
		char[][] map = new char[rows][cols];
		int[] rowFirst = new int[rows];
		int[] rowLast = new int[rows];
		int[] colFirst = new int[cols];
		int[] colLast = new int[cols];
		Arrays.fill(rowFirst, cols);
		Arrays.fill(rowLast, -1);
		Arrays.fill(colFirst, rows);
		Arrays.fill(colLast, -1);
		int area = 0;
		int firstMapRow = rows;
		int minCol = cols;
		for (int row = 0; row < rows; row++) {
			Arrays.fill(map[row], ' ');
			String line = lines.get(row);
			for (int col = 0; col < line.length(); col++) {
				char cell = line.charAt(col);
				if (cell != '.' && cell != '#') {
					continue;
				}
				map[row][col] = cell;
				area++;
				firstMapRow = Math.min(firstMapRow, row);
				minCol = Math.min(minCol, col);
				rowFirst[row] = Math.min(rowFirst[row], col);
				rowLast[row] = Math.max(rowLast[row], col);
				colFirst[col] = Math.min(colFirst[col], row);
				colLast[col] = Math.max(colLast[col], row);
			}
		}
		if (area == 0 || area % 6 != 0) {
			throw new IllegalArgumentException("Map is not six equal cube faces");
		}
		int side = (int) Math.sqrt(area / 6);
		if (side <= 0 || 6 * side * side != area) {
			throw new IllegalArgumentException("Cube face area is not square");
		}

		Map<Long, Face> byBlock = new HashMap<>();
		List<Face> faceList = new ArrayList<>(6);
		for (int row = 0; row < rows; row++) {
			for (int col = rowFirst[row]; col <= rowLast[row]; col++) {
				if (map[row][col] == ' ') {
					continue;
				}
				int blockRow = (row - firstMapRow) / side;
				int blockCol = (col - minCol) / side;
				long key = blockKey(blockRow, blockCol);
				if (!byBlock.containsKey(key)) {
					Face face = new Face(faceList.size(), blockRow, blockCol,
							firstMapRow + blockRow * side, minCol + blockCol * side);
					byBlock.put(key, face);
					faceList.add(face);
				}
			}
		}
		if (faceList.size() != 6) {
			throw new IllegalArgumentException("Expected six aligned faces, found " + faceList.size());
		}

		Face[] faces = faceList.toArray(new Face[0]);
		int[][] faceAt = new int[rows][cols];
		for (int[] ids : faceAt) {
			Arrays.fill(ids, -1);
		}
		for (Face face : faces) {
			for (int row = face.top; row < face.top + side; row++) {
				for (int col = face.left; col < face.left + side; col++) {
					if (row < 0 || row >= rows || col < 0 || col >= cols || map[row][col] == ' ') {
						throw new IllegalArgumentException("Face blocks must be complete squares");
					}
					faceAt[row][col] = face.id;
				}
			}
		}

		orientFaces(faces, byBlock);
		buildTransitions(faces);
		return new Parsed(map, rows, cols, lines.get(pathLine).trim(), side, firstMapRow,
				rowFirst, rowLast, colFirst, colLast, faceAt, faces);
	}

	private static void orientFaces(Face[] faces, Map<Long, Face> byBlock) {
		Face root = faces[0];
		root.right = new Vector(1, 0, 0);
		root.down = new Vector(0, 1, 0);
		root.normal = new Vector(0, 0, 1);
		ArrayDeque<Face> queue = new ArrayDeque<>();
		queue.add(root);
		while (!queue.isEmpty()) {
			Face face = queue.removeFirst();
			for (int direction = 0; direction < 4; direction++) {
				int neighborRow = face.blockRow + ROW_STEP[direction];
				int neighborCol = face.blockCol + COL_STEP[direction];
				Face neighbor = byBlock.get(blockKey(neighborRow, neighborCol));
				if (neighbor == null) {
					continue;
				}
				Vector[] folded = fold(face, direction);
				if (neighbor.normal == null) {
					neighbor.right = folded[0];
					neighbor.down = folded[1];
					neighbor.normal = folded[2];
					queue.addLast(neighbor);
				} else if (!neighbor.right.equals(folded[0])
						|| !neighbor.down.equals(folded[1])
						|| !neighbor.normal.equals(folded[2])) {
					throw new IllegalArgumentException("Cube net folds inconsistently");
				}
			}
		}
		for (Face face : faces) {
			if (face.normal == null) {
				throw new IllegalArgumentException("Cube net is disconnected");
			}
		}
	}

	private static Vector[] fold(Face face, int direction) {
		switch (direction) {
			case 0:
				return new Vector[] {face.normal.negate(), face.down, face.right};
			case 1:
				return new Vector[] {face.right, face.normal.negate(), face.down};
			case 2:
				return new Vector[] {face.normal, face.down, face.right.negate()};
			default:
				return new Vector[] {face.right, face.normal, face.down.negate()};
		}
	}

	private static void buildTransitions(Face[] faces) {
		for (Face from : faces) {
			for (int direction = 0; direction < 4; direction++) {
				Vector movement = directionVector(from, direction);
				Face to = null;
				for (Face candidate : faces) {
					if (candidate.normal.equals(movement)) {
						to = candidate;
						break;
					}
				}
				if (to == null) {
					throw new IllegalArgumentException("Cube is missing a folded neighbor");
				}
				Vector newMovement = from.normal.negate();
				int newDirection = directionOf(to, newMovement);
				Vector oldOffsetAxis = (direction & 1) == 0 ? from.down : from.right;
				Vector newOffsetAxis = (newDirection & 1) == 0 ? to.down : to.right;
				boolean reverse;
				if (oldOffsetAxis.equals(newOffsetAxis)) {
					reverse = false;
				} else if (oldOffsetAxis.equals(newOffsetAxis.negate())) {
					reverse = true;
				} else {
					throw new IllegalArgumentException("Folded edge axes do not align");
				}
				from.transitions[direction] = new Transition(to.id, newDirection, reverse);
			}
		}
	}

	private static Vector directionVector(Face face, int direction) {
		switch (direction) {
			case 0:
				return face.right;
			case 1:
				return face.down;
			case 2:
				return face.right.negate();
			default:
				return face.down.negate();
		}
	}

	private static int directionOf(Face face, Vector vector) {
		for (int direction = 0; direction < 4; direction++) {
			if (directionVector(face, direction).equals(vector)) {
				return direction;
			}
		}
		throw new IllegalArgumentException("Movement is not tangent to target face");
	}

	private static long blockKey(int row, int col) {
		return ((long) row << 32) ^ (col & 0xffffffffL);
	}

	private static final class Parsed {
		final char[][] map;
		final int rows;
		final int cols;
		final String path;
		final int side;
		final int firstMapRow;
		final int[] rowFirst;
		final int[] rowLast;
		final int[] colFirst;
		final int[] colLast;
		final int[][] faceAt;
		final Face[] faces;

		Parsed(char[][] map, int rows, int cols, String path, int side, int firstMapRow,
				int[] rowFirst, int[] rowLast, int[] colFirst, int[] colLast,
				int[][] faceAt, Face[] faces) {
			this.map = map;
			this.rows = rows;
			this.cols = cols;
			this.path = path;
			this.side = side;
			this.firstMapRow = firstMapRow;
			this.rowFirst = rowFirst;
			this.rowLast = rowLast;
			this.colFirst = colFirst;
			this.colLast = colLast;
			this.faceAt = faceAt;
			this.faces = faces;
		}
	}

	private static final class Face {
		final int id;
		final int blockRow;
		final int blockCol;
		final int top;
		final int left;
		final Transition[] transitions = new Transition[4];
		Vector right;
		Vector down;
		Vector normal;

		Face(int id, int blockRow, int blockCol, int top, int left) {
			this.id = id;
			this.blockRow = blockRow;
			this.blockCol = blockCol;
			this.top = top;
			this.left = left;
		}
	}

	private static final class Transition {
		final int face;
		final int direction;
		final boolean reverse;

		Transition(int face, int direction, boolean reverse) {
			this.face = face;
			this.direction = direction;
			this.reverse = reverse;
		}
	}

	private static final class Vector {
		final int x;
		final int y;
		final int z;

		Vector(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		Vector negate() {
			return new Vector(-x, -y, -z);
		}

		@Override
		public boolean equals(Object other) {
			if (!(other instanceof Vector)) {
				return false;
			}
			Vector vector = (Vector) other;
			return x == vector.x && y == vector.y && z == vector.z;
		}

		@Override
		public int hashCode() {
			return (x + 1) * 9 + (y + 1) * 3 + z + 1;
		}
	}
}
