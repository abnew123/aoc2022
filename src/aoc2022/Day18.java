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

	private Coordinates parse(Scanner in) {
		Coordinates cubes = new Coordinates();
		while (in.hasNextLine()) {
			String line = in.nextLine().trim();
			if (line.isEmpty()) {
				continue;
			}
			int firstComma = line.indexOf(',');
			int secondComma = line.indexOf(',', firstComma + 1);
			if (firstComma <= 0 || secondComma <= firstComma + 1 || secondComma == line.length() - 1
					|| line.indexOf(',', secondComma + 1) >= 0) {
				throw new IllegalArgumentException("Invalid cube coordinate: " + line);
			}
			long x = Long.parseLong(line.substring(0, firstComma).trim());
			long y = Long.parseLong(line.substring(firstComma + 1, secondComma).trim());
			long z = Long.parseLong(line.substring(secondComma + 1).trim());
			cubes.add(x, y, z);
		}
		return cubes;
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
