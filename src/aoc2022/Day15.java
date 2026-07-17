package aoc2022;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

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
		Sensor[] sensors = parse(in);
		return part1 ? Long.toString(excludedOnRow(sensors, TARGET_ROW))
				: Long.toString(tuningFrequency(sensors, SEARCH_LIMIT));
	}

	String[] fullSolveForLimits(Scanner in, long targetRow, long searchLimit) {
		return answers(parse(in), targetRow, searchLimit);
	}

	private String[] answers(Sensor[] sensors, long targetRow, long searchLimit) {
		return new String[] {Long.toString(excludedOnRow(sensors, targetRow)),
				Long.toString(tuningFrequency(sensors, searchLimit))};
	}

	private Sensor[] parse(Scanner in) {
		List<Sensor> sensors = new ArrayList<>();
		while (in.hasNextLine()) {
			String line = in.nextLine();
			if (line.isBlank()) {
				throw new IllegalArgumentException("Blank sensor row");
			}
			long[] values = numbers(line);
			long distance = Math.addExact(absDifference(values[0], values[2]),
					absDifference(values[1], values[3]));
			sensors.add(new Sensor(values[0], values[1], values[2], values[3], distance));
		}
		if (sensors.isEmpty()) {
			throw new IllegalArgumentException("Sensor report must not be empty");
		}
		return sensors.toArray(new Sensor[0]);
	}

	private long[] numbers(String line) {
		long[] values = new long[4];
		int count = 0;
		for (int index = 0; index < line.length();) {
			char current = line.charAt(index);
			if (current != '+' && current != '-' && !Character.isDigit(current)) {
				index++;
				continue;
			}
			int start = index++;
			while (index < line.length() && Character.isDigit(line.charAt(index))) {
				index++;
			}
			if (index == start + 1 && (current == '+' || current == '-')) {
				throw new IllegalArgumentException("Malformed sensor coordinate");
			}
			if (count == values.length) {
				throw new IllegalArgumentException("Too many sensor coordinates");
			}
			values[count++] = Long.parseLong(line, start, index, 10);
		}
		if (count != values.length) {
			throw new IllegalArgumentException("Expected four sensor coordinates");
		}
		return values;
	}

	private long absDifference(long left, long right) {
		long difference = Math.subtractExact(left, right);
		if (difference == Long.MIN_VALUE) {
			throw new ArithmeticException("Coordinate distance exceeds long range");
		}
		return Math.abs(difference);
	}

	private long excludedOnRow(Sensor[] sensors, long targetRow) {
		List<Range> ranges = new ArrayList<>(sensors.length);
		Set<Long> beacons = new HashSet<>();
		for (Sensor sensor : sensors) {
			if (sensor.beaconY == targetRow) {
				beacons.add(sensor.beaconX);
			}
			long rowDistance = absDifference(sensor.y, targetRow);
			if (rowDistance <= sensor.distance) {
				long reach = sensor.distance - rowDistance;
				ranges.add(new Range(Math.subtractExact(sensor.x, reach), Math.addExact(sensor.x, reach)));
			}
		}
		if (ranges.isEmpty()) {
			return 0;
		}
		ranges.sort(Comparator.comparingLong(Range::start).thenComparingLong(Range::end));
		List<Range> merged = new ArrayList<>();
		long start = ranges.get(0).start;
		long end = ranges.get(0).end;
		for (int index = 1; index < ranges.size(); index++) {
			Range next = ranges.get(index);
			if (next.start > end) {
				merged.add(new Range(start, end));
				start = next.start;
				end = next.end;
			} else if (next.end > end) {
				end = next.end;
			}
		}
		merged.add(new Range(start, end));

		long answer = 0;
		for (Range range : merged) {
			answer = Math.addExact(answer, Math.addExact(Math.subtractExact(range.end, range.start), 1));
		}
		for (long beacon : beacons) {
			for (Range range : merged) {
				if (beacon >= range.start && beacon <= range.end) {
					answer--;
					break;
				}
			}
		}
		return answer;
	}

	private long tuningFrequency(Sensor[] sensors, long limit) {
		if (limit < 0) {
			throw new IllegalArgumentException("Search limit must be nonnegative");
		}
		long[] positive = new long[sensors.length * 2];
		long[] negative = new long[sensors.length * 2];
		for (int index = 0; index < sensors.length; index++) {
			Sensor sensor = sensors[index];
			long outside = Math.addExact(sensor.distance, 1);
			long yMinusX = Math.subtractExact(sensor.y, sensor.x);
			long xPlusY = Math.addExact(sensor.x, sensor.y);
			positive[index * 2] = Math.addExact(yMinusX, outside);
			positive[index * 2 + 1] = Math.subtractExact(yMinusX, outside);
			negative[index * 2] = Math.addExact(xPlusY, outside);
			negative[index * 2 + 1] = Math.subtractExact(xPlusY, outside);
		}

		for (long rising : positive) {
			for (long falling : negative) {
				if (((rising ^ falling) & 1) == 0) {
					long x = Math.subtractExact(falling, rising) / 2;
					long y = Math.addExact(falling, rising) / 2;
					Point found = uncoveredCandidate(x, y, limit, sensors);
					if (found != null) {
						return frequency(found);
					}
				}
			}
		}
		for (long line : positive) {
			Point[] candidates = new Point[] {new Point(0, line),
					new Point(limit, Math.addExact(line, limit)), new Point(Math.negateExact(line), 0),
					new Point(Math.subtractExact(limit, line), limit)};
			for (Point candidate : candidates) {
				Point found = uncoveredCandidate(candidate.x, candidate.y, limit, sensors);
				if (found != null) {
					return frequency(found);
				}
			}
		}
		for (long line : negative) {
			Point[] candidates = new Point[] {new Point(0, line),
					new Point(limit, Math.subtractExact(line, limit)), new Point(line, 0),
					new Point(Math.subtractExact(line, limit), limit)};
			for (Point candidate : candidates) {
				Point found = uncoveredCandidate(candidate.x, candidate.y, limit, sensors);
				if (found != null) {
					return frequency(found);
				}
			}
		}
		for (Point corner : new Point[] {new Point(0, 0), new Point(0, limit),
				new Point(limit, 0), new Point(limit, limit)}) {
			Point found = uncoveredCandidate(corner.x, corner.y, limit, sensors);
			if (found != null) {
				return frequency(found);
			}
		}
		throw new IllegalStateException("No distress beacon found");
	}

	private Point uncoveredCandidate(long x, long y, long limit, Sensor[] sensors) {
		if (x < 0 || y < 0 || x > limit || y > limit) {
			return null;
		}
		Point candidate = new Point(x, y);
		return isUncovered(candidate, sensors) ? candidate : null;
	}

	private long frequency(Point point) {
		return Math.addExact(Math.multiplyExact(point.x, TUNING_MULTIPLIER), point.y);
	}

	private boolean isUncovered(Point point, Sensor[] sensors) {
		for (Sensor sensor : sensors) {
			long distance = Math.addExact(absDifference(point.x, sensor.x), absDifference(point.y, sensor.y));
			if (distance <= sensor.distance) {
				return false;
			}
		}
		return true;
	}

	private record Sensor(long x, long y, long beaconX, long beaconY, long distance) {}
	private record Range(long start, long end) {}
	private record Point(long x, long y) {}
}
