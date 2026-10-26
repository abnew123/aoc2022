package aoc2022;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Day18 extends DayTemplate {
	private static final byte LAVA = 1;
	private static final byte EXTERIOR = 2;
	private static final int MAX_ARRAY_LENGTH = Integer.MAX_VALUE - 8;

	@Override
	public String[] fullSolve(Scanner in) throws FileNotFoundException {
		SurfaceAreas areas = analyze(in);
		return new String[] { Long.toString(areas.total()), Long.toString(areas.exterior()) };
	}

	@Override
	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		SurfaceAreas areas = analyze(in);
		return Long.toString(part1 ? areas.total() : areas.exterior());
	}

	private SurfaceAreas analyze(Scanner in) {
		Coordinates cubes = parse(in);
		if (cubes.size == 0) {
			return new SurfaceAreas(0, 0);
		}

		Bounds bounds = Bounds.around(cubes);
		byte[] cells = new byte[bounds.cellCount];
		long totalFaces = 0;
		for (int i = 0; i < cubes.size; i++) {
			int index = bounds.index(cubes.x[i], cubes.y[i], cubes.z[i]);
			if (cells[index] == LAVA) {
				continue;
			}
			int touching = 0;
			if (cells[index - bounds.plane] == LAVA) {
				touching++;
			}
			if (cells[index + bounds.plane] == LAVA) {
				touching++;
			}
			if (cells[index - bounds.depth] == LAVA) {
				touching++;
			}
			if (cells[index + bounds.depth] == LAVA) {
				touching++;
			}
			if (cells[index - 1] == LAVA) {
				touching++;
			}
			if (cells[index + 1] == LAVA) {
				touching++;
			}
			totalFaces += 6 - 2L * touching;
			cells[index] = LAVA;
		}

		int[] queue = new int[bounds.cellCount];
		int head = 0;
		int tail = 1;
		queue[0] = 0;
		cells[0] = EXTERIOR;
		long exteriorFaces = 0;
		while (head < tail) {
			int index = queue[head++];
			int x = index / bounds.plane;
			int withinPlane = index - x * bounds.plane;
			int y = withinPlane / bounds.depth;
			int z = withinPlane - y * bounds.depth;

			if (x > 0) {
				int next = index - bounds.plane;
				if (cells[next] == LAVA) {
					exteriorFaces++;
				} else if (cells[next] == 0) {
					cells[next] = EXTERIOR;
					queue[tail++] = next;
				}
			}
			if (x + 1 < bounds.width) {
				int next = index + bounds.plane;
				if (cells[next] == LAVA) {
					exteriorFaces++;
				} else if (cells[next] == 0) {
					cells[next] = EXTERIOR;
					queue[tail++] = next;
				}
			}
			if (y > 0) {
				int next = index - bounds.depth;
				if (cells[next] == LAVA) {
					exteriorFaces++;
				} else if (cells[next] == 0) {
					cells[next] = EXTERIOR;
					queue[tail++] = next;
				}
			}
			if (y + 1 < bounds.height) {
				int next = index + bounds.depth;
				if (cells[next] == LAVA) {
					exteriorFaces++;
				} else if (cells[next] == 0) {
					cells[next] = EXTERIOR;
					queue[tail++] = next;
				}
			}
			if (z > 0) {
				int next = index - 1;
				if (cells[next] == LAVA) {
					exteriorFaces++;
				} else if (cells[next] == 0) {
					cells[next] = EXTERIOR;
					queue[tail++] = next;
				}
			}
			if (z + 1 < bounds.depth) {
				int next = index + 1;
				if (cells[next] == LAVA) {
					exteriorFaces++;
				} else if (cells[next] == 0) {
					cells[next] = EXTERIOR;
					queue[tail++] = next;
				}
			}
		}

		return new SurfaceAreas(totalFaces, exteriorFaces);
	}

	/**
	 * One slurp, manual scan. Each nonblank line is three signed decimal longs
	 * separated by commas, with optional surrounding whitespace; blank lines are
	 * skipped.
	 */
	private Coordinates parse(Scanner in) {
		String text = in.useDelimiter("\\A").hasNext() ? in.next() : "";
		Coordinates cubes = new Coordinates();
		long[] out = new long[1];
		int n = text.length();
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
			int p = skipSpaces(text, lineStart, lineEnd);
			if (p == lineEnd) {
				continue;
			}
			p = parseSignedLong(text, p, lineEnd, out);
			long x = out[0];
			p = skipSpaces(text, p, lineEnd);
			p = expectComma(text, p, lineEnd);
			p = skipSpaces(text, p, lineEnd);
			p = parseSignedLong(text, p, lineEnd, out);
			long y = out[0];
			p = skipSpaces(text, p, lineEnd);
			p = expectComma(text, p, lineEnd);
			p = skipSpaces(text, p, lineEnd);
			p = parseSignedLong(text, p, lineEnd, out);
			long z = out[0];
			p = skipSpaces(text, p, lineEnd);
			if (p != lineEnd) {
				throw new IllegalArgumentException("Invalid cube coordinate line");
			}
			cubes.add(x, y, z);
		}
		return cubes;
	}

	private int skipSpaces(String text, int from, int lineEnd) {
		int p = from;
		while (p < lineEnd && text.charAt(p) <= ' ') {
			p++;
		}
		return p;
	}

	private int expectComma(String text, int p, int lineEnd) {
		if (p == lineEnd || text.charAt(p) != ',') {
			throw new IllegalArgumentException("Invalid cube coordinate line");
		}
		return p + 1;
	}

	/** Parses one signed long into out[0]; accumulates negated so Long.MIN_VALUE parses exactly. */
	private int parseSignedLong(String text, int from, int lineEnd, long[] out) {
		int p = from;
		boolean negative = false;
		if (p < lineEnd && (text.charAt(p) == '+' || text.charAt(p) == '-')) {
			negative = text.charAt(p) == '-';
			p++;
		}
		long value = 0;
		boolean sawDigit = false;
		while (p < lineEnd) {
			char c = text.charAt(p);
			if (c < '0' || c > '9') {
				break;
			}
			int digit = c - '0';
			if (value < Long.MIN_VALUE / 10 || value * 10 < Long.MIN_VALUE + digit) {
				throw new NumberFormatException("Cube coordinate out of range");
			}
			value = value * 10 - digit;
			sawDigit = true;
			p++;
		}
		if (!sawDigit) {
			throw new NumberFormatException("Invalid cube coordinate");
		}
		if (!negative) {
			if (value == Long.MIN_VALUE) {
				throw new NumberFormatException("Cube coordinate out of range");
			}
			value = -value;
		}
		out[0] = value;
		return p;
	}

	private static final class Coordinates {
		private long[] x = new long[128];
		private long[] y = new long[128];
		private long[] z = new long[128];
		private int size;
		private long minX;
		private long maxX;
		private long minY;
		private long maxY;
		private long minZ;
		private long maxZ;

		void add(long nextX, long nextY, long nextZ) {
			if (size == x.length) {
				x = Arrays.copyOf(x, size * 2);
				y = Arrays.copyOf(y, size * 2);
				z = Arrays.copyOf(z, size * 2);
			}
			x[size] = nextX;
			y[size] = nextY;
			z[size] = nextZ;
			if (size == 0) {
				minX = maxX = nextX;
				minY = maxY = nextY;
				minZ = maxZ = nextZ;
			} else {
				minX = Math.min(minX, nextX);
				maxX = Math.max(maxX, nextX);
				minY = Math.min(minY, nextY);
				maxY = Math.max(maxY, nextY);
				minZ = Math.min(minZ, nextZ);
				maxZ = Math.max(maxZ, nextZ);
			}
			size++;
		}
	}

	private static final class Bounds {
		private final long minX;
		private final long minY;
		private final long minZ;
		private final int width;
		private final int height;
		private final int depth;
		private final int plane;
		private final int cellCount;

		private Bounds(long minX, long minY, long minZ, int width, int height, int depth, int plane,
				int cellCount) {
			this.minX = minX;
			this.minY = minY;
			this.minZ = minZ;
			this.width = width;
			this.height = height;
			this.depth = depth;
			this.plane = plane;
			this.cellCount = cellCount;
		}

		static Bounds around(Coordinates cubes) {
			try {
				long minX = Math.subtractExact(cubes.minX, 1);
				long maxX = Math.addExact(cubes.maxX, 1);
				long minY = Math.subtractExact(cubes.minY, 1);
				long maxY = Math.addExact(cubes.maxY, 1);
				long minZ = Math.subtractExact(cubes.minZ, 1);
				long maxZ = Math.addExact(cubes.maxZ, 1);
				long width = Math.addExact(Math.subtractExact(maxX, minX), 1);
				long height = Math.addExact(Math.subtractExact(maxY, minY), 1);
				long depth = Math.addExact(Math.subtractExact(maxZ, minZ), 1);
				long plane = Math.multiplyExact(height, depth);
				long cells = Math.multiplyExact(width, plane);
				if (width > Integer.MAX_VALUE || height > Integer.MAX_VALUE || depth > Integer.MAX_VALUE
						|| plane > Integer.MAX_VALUE || cells > MAX_ARRAY_LENGTH) {
					throw new IllegalArgumentException("Padded cube bounds are too large for flat array indexing");
				}
				return new Bounds(minX, minY, minZ, (int) width, (int) height, (int) depth, (int) plane,
						(int) cells);
			} catch (ArithmeticException exception) {
				throw new IllegalArgumentException("Padded cube bounds overflow long arithmetic", exception);
			}
		}

		int index(long x, long y, long z) {
			try {
				long offsetX = Math.subtractExact(x, minX);
				long offsetY = Math.subtractExact(y, minY);
				long offsetZ = Math.subtractExact(z, minZ);
				if (offsetX < 0 || offsetX >= width || offsetY < 0 || offsetY >= height || offsetZ < 0
						|| offsetZ >= depth) {
					throw new IllegalArgumentException("Cube coordinate lies outside derived bounds");
				}
				return (int) ((offsetX * height + offsetY) * depth + offsetZ);
			} catch (ArithmeticException exception) {
				throw new IllegalArgumentException("Cube index overflows long arithmetic", exception);
			}
		}
	}

	private record SurfaceAreas(long total, long exterior) {
	}
}
