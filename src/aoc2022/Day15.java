package aoc2022;

import java.util.Arrays;
import java.util.Scanner;

public class Day15 extends DayTemplate {

	private static final long TARGET_ROW = 2_000_000L;
	private static final long SEARCH_LIMIT = 4_000_000L;
	private static final long TUNING_MULTIPLIER = 4_000_000L;

	@Override
	public String[] fullSolve(Scanner in) {
		return answers(parse(in), TARGET_ROW, SEARCH_LIMIT);
	}

	@Override
	public String solve(boolean part1, Scanner in) {
		long[] sensors = parse(in);
		return part1 ? Long.toString(excludedOnRow(sensors, TARGET_ROW))
				: Long.toString(tuningFrequency(sensors, SEARCH_LIMIT));
	}

	String[] fullSolveForLimits(Scanner in, long targetRow, long searchLimit) {
		return answers(parse(in), targetRow, searchLimit);
	}

	private String[] answers(long[] sensors, long targetRow, long searchLimit) {
		return new String[] {Long.toString(excludedOnRow(sensors, targetRow)),
				Long.toString(tuningFrequency(sensors, searchLimit))};
	}

	/**
	 * One slurp, manual scan. Returns a flat sensor table of five longs per
	 * sensor: x, y, beaconX, beaconY, distance. Each nonblank line must contain
	 * exactly four signed decimal coordinates; blank lines are rejected.
	 */
	private long[] parse(Scanner in) {
		String text = in.useDelimiter("\\A").hasNext() ? in.next() : "";
		int n = text.length();
		long[] sensors = new long[40];
		int size = 0;
		long[] values = new long[4];
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
			boolean blank = true;
			for (int k = lineStart; k < lineEnd; k++) {
				if (!Character.isWhitespace(text.charAt(k))) {
					blank = false;
					break;
				}
			}
			if (blank) {
				throw new IllegalArgumentException("Blank sensor row");
			}
			int count = 0;
			for (int k = lineStart; k < lineEnd;) {
				char current = text.charAt(k);
				if (current != '+' && current != '-' && (current < '0' || current > '9')) {
					k++;
					continue;
				}
				boolean negative = current == '-';
				if (current == '+' || current == '-') {
					k++;
				}
				// Accumulate negated so Long.MIN_VALUE parses exactly.
				long value = 0;
				boolean sawDigit = false;
				while (k < lineEnd) {
					char digitChar = text.charAt(k);
					if (digitChar < '0' || digitChar > '9') {
						break;
					}
					int digit = digitChar - '0';
					if (value < Long.MIN_VALUE / 10 || value * 10 < Long.MIN_VALUE + digit) {
						throw new NumberFormatException("Sensor coordinate out of range");
					}
					value = value * 10 - digit;
					sawDigit = true;
					k++;
				}
				if (!sawDigit) {
					throw new IllegalArgumentException("Malformed sensor coordinate");
				}
				if (!negative) {
					if (value == Long.MIN_VALUE) {
						throw new NumberFormatException("Sensor coordinate out of range");
					}
					value = -value;
				}
				if (count == values.length) {
					throw new IllegalArgumentException("Too many sensor coordinates");
				}
				values[count++] = value;
			}
			if (count != values.length) {
				throw new IllegalArgumentException("Expected four sensor coordinates");
			}
			long distance = Math.addExact(absDifference(values[0], values[2]),
					absDifference(values[1], values[3]));
			if (size + 5 > sensors.length) {
				sensors = Arrays.copyOf(sensors, sensors.length * 2);
			}
			sensors[size++] = values[0];
			sensors[size++] = values[1];
			sensors[size++] = values[2];
			sensors[size++] = values[3];
			sensors[size++] = distance;
		}
		if (size == 0) {
			throw new IllegalArgumentException("Sensor report must not be empty");
		}
		return size == sensors.length ? sensors : Arrays.copyOf(sensors, size);
	}

	private long absDifference(long left, long right) {
		long difference = Math.subtractExact(left, right);
		if (difference == Long.MIN_VALUE) {
			throw new ArithmeticException("Coordinate distance exceeds long range");
		}
		return Math.abs(difference);
	}

	private long excludedOnRow(long[] sensors, long targetRow) {
		int sensorCount = sensors.length / 5;
		long[] rangeStarts = new long[sensorCount];
		long[] rangeEnds = new long[sensorCount];
		int rangeCount = 0;
		long[] beacons = new long[sensorCount];
		int beaconCount = 0;
		for (int s = 0; s < sensors.length; s += 5) {
			if (sensors[s + 3] == targetRow) {
				long beaconX = sensors[s + 2];
				boolean seen = false;
				for (int b = 0; b < beaconCount; b++) {
					if (beacons[b] == beaconX) {
						seen = true;
						break;
					}
				}
				if (!seen) {
					beacons[beaconCount++] = beaconX;
				}
			}
			long rowDistance = absDifference(sensors[s + 1], targetRow);
			if (rowDistance <= sensors[s + 4]) {
				long reach = sensors[s + 4] - rowDistance;
				rangeStarts[rangeCount] = Math.subtractExact(sensors[s], reach);
				rangeEnds[rangeCount] = Math.addExact(sensors[s], reach);
				rangeCount++;
			}
		}
		if (rangeCount == 0) {
			return 0;
		}
		sortRanges(rangeStarts, rangeEnds, rangeCount);

		long[] mergedStarts = new long[rangeCount];
		long[] mergedEnds = new long[rangeCount];
		int mergedCount = 0;
		long start = rangeStarts[0];
		long end = rangeEnds[0];
		for (int r = 1; r < rangeCount; r++) {
			if (rangeStarts[r] > end) {
				mergedStarts[mergedCount] = start;
				mergedEnds[mergedCount] = end;
				mergedCount++;
				start = rangeStarts[r];
				end = rangeEnds[r];
			} else if (rangeEnds[r] > end) {
				end = rangeEnds[r];
			}
		}
		mergedStarts[mergedCount] = start;
		mergedEnds[mergedCount] = end;
		mergedCount++;

		long answer = 0;
		for (int r = 0; r < mergedCount; r++) {
			answer = Math.addExact(answer,
					Math.addExact(Math.subtractExact(mergedEnds[r], mergedStarts[r]), 1));
		}
		for (int b = 0; b < beaconCount; b++) {
			for (int r = 0; r < mergedCount; r++) {
				if (beacons[b] >= mergedStarts[r] && beacons[b] <= mergedEnds[r]) {
					answer--;
					break;
				}
			}
		}
		return answer;
	}

	/** Bottom-up merge sort of the range pairs by (start, end); deterministic and stable. */
	private void sortRanges(long[] starts, long[] ends, int count) {
		long[] tempStarts = new long[count];
		long[] tempEnds = new long[count];
		for (int width = 1; width < count; width *= 2) {
			for (int lo = 0; lo < count - width; lo += 2 * width) {
				int mid = lo + width;
				int hi = Math.min(lo + 2 * width, count);
				int left = lo;
				int right = mid;
				int out = lo;
				while (left < mid && right < hi) {
					if (starts[left] < starts[right]
							|| (starts[left] == starts[right] && ends[left] <= ends[right])) {
						tempStarts[out] = starts[left];
						tempEnds[out] = ends[left];
						left++;
					} else {
						tempStarts[out] = starts[right];
						tempEnds[out] = ends[right];
						right++;
					}
					out++;
				}
				while (left < mid) {
					tempStarts[out] = starts[left];
					tempEnds[out] = ends[left];
					left++;
					out++;
				}
				while (right < hi) {
					tempStarts[out] = starts[right];
					tempEnds[out] = ends[right];
					right++;
					out++;
				}
				for (int k = lo; k < hi; k++) {
					starts[k] = tempStarts[k];
					ends[k] = tempEnds[k];
				}
			}
		}
	}

	private long tuningFrequency(long[] sensors, long limit) {
		if (limit < 0) {
			throw new IllegalArgumentException("Search limit must be nonnegative");
		}
		int sensorCount = sensors.length / 5;
		long[] positive = new long[sensorCount * 2];
		long[] negative = new long[sensorCount * 2];
		for (int index = 0; index < sensorCount; index++) {
			long x = sensors[index * 5];
			long y = sensors[index * 5 + 1];
			long distance = sensors[index * 5 + 4];
			long outside = Math.addExact(distance, 1);
			long yMinusX = Math.subtractExact(y, x);
			long xPlusY = Math.addExact(x, y);
			positive[index * 2] = Math.addExact(yMinusX, outside);
			positive[index * 2 + 1] = Math.subtractExact(yMinusX, outside);
			negative[index * 2] = Math.addExact(xPlusY, outside);
			negative[index * 2 + 1] = Math.subtractExact(xPlusY, outside);
		}

		for (int r = 0; r < positive.length; r++) {
			long rising = positive[r];
			for (int f = 0; f < negative.length; f++) {
				long falling = negative[f];
				if (((rising ^ falling) & 1) == 0) {
					long x = Math.subtractExact(falling, rising) / 2;
					long y = Math.addExact(falling, rising) / 2;
					long frequency = frequencyIfUncovered(x, y, limit, sensors);
					if (frequency >= 0) {
						return frequency;
					}
				}
			}
		}
		for (int r = 0; r < positive.length; r++) {
			long line = positive[r];
			long secondY = Math.addExact(line, limit);
			long thirdX = Math.negateExact(line);
			long fourthX = Math.subtractExact(limit, line);
			long frequency = frequencyIfUncovered(0, line, limit, sensors);
			if (frequency < 0) {
				frequency = frequencyIfUncovered(limit, secondY, limit, sensors);
			}
			if (frequency < 0) {
				frequency = frequencyIfUncovered(thirdX, 0, limit, sensors);
			}
			if (frequency < 0) {
				frequency = frequencyIfUncovered(fourthX, limit, limit, sensors);
			}
			if (frequency >= 0) {
				return frequency;
			}
		}
		for (int f = 0; f < negative.length; f++) {
			long line = negative[f];
			long secondY = Math.subtractExact(line, limit);
			long fourthX = Math.subtractExact(line, limit);
			long frequency = frequencyIfUncovered(0, line, limit, sensors);
			if (frequency < 0) {
				frequency = frequencyIfUncovered(limit, secondY, limit, sensors);
			}
			if (frequency < 0) {
				frequency = frequencyIfUncovered(line, 0, limit, sensors);
			}
			if (frequency < 0) {
				frequency = frequencyIfUncovered(fourthX, limit, limit, sensors);
			}
			if (frequency >= 0) {
				return frequency;
			}
		}
		long frequency = frequencyIfUncovered(0, 0, limit, sensors);
		if (frequency < 0) {
			frequency = frequencyIfUncovered(0, limit, limit, sensors);
		}
		if (frequency < 0) {
			frequency = frequencyIfUncovered(limit, 0, limit, sensors);
		}
		if (frequency < 0) {
			frequency = frequencyIfUncovered(limit, limit, limit, sensors);
		}
		if (frequency >= 0) {
			return frequency;
		}
		throw new IllegalStateException("No distress beacon found");
	}

	/**
	 * Returns the tuning frequency when (x, y) lies inside the search box and no
	 * sensor covers it, otherwise -1 (valid frequencies are never negative).
	 */
	private long frequencyIfUncovered(long x, long y, long limit, long[] sensors) {
		if (x < 0 || y < 0 || x > limit || y > limit) {
			return -1;
		}
		for (int s = 0; s < sensors.length; s += 5) {
			long distance = Math.addExact(absDifference(x, sensors[s]),
					absDifference(y, sensors[s + 1]));
			if (distance <= sensors[s + 4]) {
				return -1;
			}
		}
		return Math.addExact(Math.multiplyExact(x, TUNING_MULTIPLIER), y);
	}
}
