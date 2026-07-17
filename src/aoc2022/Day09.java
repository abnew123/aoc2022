package aoc2022;

import java.util.Scanner;

public class Day09 extends DayTemplate {

	public String solve(boolean part1, Scanner in) {
		long[] visits = simulate(in, part1 ? 2 : 10, false);
		return "" + visits[1];
	}

	@Override
	public String[] fullSolve(Scanner in) {
		long[] visits = simulate(in, 10, true);
		return new String[] { "" + visits[0], "" + visits[1] };
	}

	private long[] simulate(Scanner in, int knotCount, boolean trackKnotOne) {
		long[] x = new long[knotCount];
		long[] y = new long[knotCount];
		PointSet knotOneVisits = trackKnotOne ? new PointSet() : null;
		PointSet tailVisits = new PointSet();
		if (trackKnotOne) {
			knotOneVisits.add(0, 0);
		}
		tailVisits.add(0, 0);

		while (in.hasNextLine()) {
			String line = in.nextLine();
			int index = 0;
			while (index < line.length() && Character.isWhitespace(line.charAt(index))) {
				index++;
			}
			if (index == line.length()) {
				continue;
			}
			long dx = 0;
			long dy = 0;
			switch (line.charAt(index++)) {
			case 'U' -> dy = 1;
			case 'D' -> dy = -1;
			case 'L' -> dx = -1;
			case 'R' -> dx = 1;
			default -> throw new IllegalArgumentException("Invalid direction: " + line);
			}

			if (index == line.length() || !Character.isWhitespace(line.charAt(index))) {
				throw new IllegalArgumentException("Invalid motion: " + line);
			}
			while (index < line.length() && Character.isWhitespace(line.charAt(index))) {
				index++;
			}
			long steps = parseSteps(line, index);
			for (long step = 0; step < steps; step++) {
				x[0] = Math.addExact(x[0], dx);
				y[0] = Math.addExact(y[0], dy);
				for (int knot = 1; knot < knotCount; knot++) {
					long differenceX = x[knot - 1] - x[knot];
					long differenceY = y[knot - 1] - y[knot];
					if (differenceX < -1 || differenceX > 1 || differenceY < -1 || differenceY > 1) {
						x[knot] += Long.compare(differenceX, 0);
						y[knot] += Long.compare(differenceY, 0);
					}
				}
				if (trackKnotOne) {
					knotOneVisits.add(x[1], y[1]);
				}
				tailVisits.add(x[knotCount - 1], y[knotCount - 1]);
			}
		}

		return new long[] { trackKnotOne ? knotOneVisits.size() : 0, tailVisits.size() };
	}

	private long parseSteps(String line, int index) {
		long steps = 0;
		int start = index;
		while (index < line.length()) {
			char digit = line.charAt(index);
			if (digit < '0' || digit > '9') {
				break;
			}
			steps = Math.addExact(Math.multiplyExact(steps, 10), digit - '0');
			index++;
		}
		if (index == start) {
			throw new IllegalArgumentException("Invalid step count: " + line);
		}
		while (index < line.length() && Character.isWhitespace(line.charAt(index))) {
			index++;
		}
		if (index != line.length()) {
			throw new IllegalArgumentException("Invalid motion: " + line);
		}
		return steps;
	}

	private static final class PointSet {
		private long[] x = new long[1024];
		private long[] y = new long[1024];
		private boolean[] used = new boolean[1024];
		private int size;

		private boolean add(long pointX, long pointY) {
			int index = index(pointX, pointY, x.length);
			while (used[index]) {
				if (x[index] == pointX && y[index] == pointY) {
					return false;
				}
				index = (index + 1) & (x.length - 1);
			}
			if (size >= x.length / 2) {
				grow();
				index = index(pointX, pointY, x.length);
				while (used[index]) {
					index = (index + 1) & (x.length - 1);
				}
			}
			used[index] = true;
			x[index] = pointX;
			y[index] = pointY;
			size++;
			return true;
		}

		private void grow() {
			long[] oldX = x;
			long[] oldY = y;
			boolean[] oldUsed = used;
			if (oldX.length >= 1 << 30) {
				throw new IllegalStateException("Too many visited positions");
			}
			x = new long[oldX.length * 2];
			y = new long[x.length];
			used = new boolean[x.length];
			for (int i = 0; i < oldX.length; i++) {
				if (oldUsed[i]) {
					int index = index(oldX[i], oldY[i], x.length);
					while (used[index]) {
						index = (index + 1) & (x.length - 1);
					}
					used[index] = true;
					x[index] = oldX[i];
					y[index] = oldY[i];
				}
			}
		}

		private int index(long pointX, long pointY, int length) {
			long hash = pointX ^ Long.rotateLeft(pointY, 32);
			// SplitMix64 finalizer by Sebastiano Vigna (public domain):
			// https://prng.di.unimi.it/splitmix64.c
			hash = (hash ^ (hash >>> 30)) * 0xBF58476D1CE4E5B9L;
			hash = (hash ^ (hash >>> 27)) * 0x94D049BB133111EBL;
			hash ^= hash >>> 31;
			return (int) hash & (length - 1);
		}

		private int size() {
			return size;
		}
	}
}
