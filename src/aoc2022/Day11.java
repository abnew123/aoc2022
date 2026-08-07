package aoc2022;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Day11 extends DayTemplate {

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		return simulate(parse(in), part1);
	}

	@Override
	public String[] fullSolve(Scanner in) throws FileNotFoundException {
		Parsed parsed = parse(in);
		return new String[] { simulate(parsed, true), simulate(parsed, false) };
	}

	private Parsed parse(Scanner in) {
		String input = in.useDelimiter("\\A").hasNext() ? in.next() : "";
		long[][] monkeyItems = new long[8][];
		int[][] monkeyConstants = new int[8][];
		int monkeyCount = 0;
		long[] startingItems = new long[16];
		int itemCount = 0;
		int[] constants = new int[5];
		int modulo = 1;
		int offset = 0;
		while (offset < input.length()) {
			int start = offset;
			int end = lineEnd(input, offset);
			offset = afterLineBreak(input, end);
			if (startsWith(input, start, end, "Monkey") || start == end) {
				continue;
			}
			long payloadRange = payload(input, start, end);
			int payloadStart = (int) (payloadRange >>> 32);
			int payloadEnd = (int) payloadRange;
			if (startsWith(input, start, end, "  Starting items")) {
				if (payloadStart == payloadEnd) {
					// Splitting an empty payload yields one empty piece; mirror its parse failure.
					Long.parseLong(input, payloadStart, payloadEnd, 10);
				}
				// First pass: find where the last non-empty ", "-separated piece ends,
				// mirroring String.split's removal of trailing empty pieces.
				int lastNonEmptyEnd = -1;
				int segmentStart = payloadStart;
				while (true) {
					int segmentEnd = segmentStart;
					while (segmentEnd < payloadEnd
							&& !itemSeparator(input, segmentEnd, payloadEnd)) {
						segmentEnd++;
					}
					if (segmentEnd > segmentStart) {
						lastNonEmptyEnd = segmentEnd;
					}
					if (segmentEnd == payloadEnd) {
						break;
					}
					segmentStart = segmentEnd + 2;
				}
				if (lastNonEmptyEnd >= 0) {
					segmentStart = payloadStart;
					while (true) {
						int segmentEnd = segmentStart;
						while (segmentEnd < lastNonEmptyEnd
								&& !itemSeparator(input, segmentEnd, payloadEnd)) {
							segmentEnd++;
						}
						long item = Long.parseLong(input, segmentStart, segmentEnd, 10);
						if (itemCount == startingItems.length) {
							startingItems = Arrays.copyOf(startingItems, itemCount * 2);
						}
						startingItems[itemCount++] = item;
						if (segmentEnd == lastNonEmptyEnd) {
							break;
						}
						segmentStart = segmentEnd + 2;
					}
				}
			} else if (startsWith(input, start, end, "  Operation")) {
				long token4 = token(input, payloadStart, payloadEnd, 4);
				int token4Start = (int) (token4 >>> 32);
				int token4End = (int) token4;
				if (token4End - token4Start == 3
						&& input.regionMatches(token4Start, "old", 0, 3)) {
					constants[0] = 2;
					constants[1] = 0;
				} else {
					long token3 = token(input, payloadStart, payloadEnd, 3);
					int token3Start = (int) (token3 >>> 32);
					int token3End = (int) token3;
					constants[0] = token3End - token3Start == 1
							&& input.charAt(token3Start) == '+' ? 0 : 1;
					constants[1] = Integer.parseInt(input, token4Start, token4End, 10);
				}
			} else if (startsWith(input, start, end, "  Test")) {
				long token2 = token(input, payloadStart, payloadEnd, 2);
				constants[2] = Integer.parseInt(input, (int) (token2 >>> 32), (int) token2, 10);
			} else if (startsWith(input, start, end, "    If true")) {
				long token3 = token(input, payloadStart, payloadEnd, 3);
				constants[3] = Integer.parseInt(input, (int) (token3 >>> 32), (int) token3, 10);
			} else if (startsWith(input, start, end, "    If false")) {
				long token3 = token(input, payloadStart, payloadEnd, 3);
				constants[4] = Integer.parseInt(input, (int) (token3 >>> 32), (int) token3, 10);
				if (monkeyCount == monkeyItems.length) {
					monkeyItems = Arrays.copyOf(monkeyItems, monkeyCount * 2);
					monkeyConstants = Arrays.copyOf(monkeyConstants, monkeyCount * 2);
				}
				monkeyItems[monkeyCount] = Arrays.copyOf(startingItems, itemCount);
				monkeyConstants[monkeyCount++] = constants;
				modulo *= constants[2];
				itemCount = 0;
				constants = new int[5];
			}
		}
		return new Parsed(monkeyItems, monkeyConstants, monkeyCount, modulo);
	}

	private String simulate(Parsed parsed, boolean part1) {
		FastMonkey[] monkeyArray = new FastMonkey[parsed.monkeyCount];
		for (int monkey = 0; monkey < monkeyArray.length; monkey++) {
			monkeyArray[monkey] = new FastMonkey(parsed.items[monkey], parsed.constants[monkey]);
		}
		for (int round = 0; round < (part1 ? 20 : 10000); round++) {
			for (FastMonkey monkey : monkeyArray) {
				monkey.turn(monkeyArray, part1, parsed.modulo);
			}
		}

		long top = 0;
		long second = 0;
		for (FastMonkey monkey : monkeyArray) {
			if (monkey.counter > top) {
				second = top;
				top = monkey.counter;
			} else if (monkey.counter > second) {
				second = monkey.counter;
			}
		}
		return "" + (top * second);
	}

	private int lineEnd(String input, int start) {
		while (start < input.length() && input.charAt(start) != '\n'
				&& input.charAt(start) != '\r') {
			start++;
		}
		return start;
	}

	private int afterLineBreak(String input, int end) {
		if (end < input.length()) {
			char ending = input.charAt(end++);
			if (ending == '\r' && end < input.length() && input.charAt(end) == '\n') {
				end++;
			}
		}
		return end;
	}

	private boolean startsWith(String input, int start, int end, String prefix) {
		return end - start >= prefix.length()
				&& input.regionMatches(start, prefix, 0, prefix.length());
	}

	/**
	 * Mirrors {@code line.split(": ")[1]}: returns the packed range between the
	 * first ": " and the following ": " (or the line end), and throws the same
	 * ArrayIndexOutOfBoundsException when element 1 does not exist, which
	 * includes the case where every piece after the first is empty because
	 * String.split removes trailing empty pieces.
	 */
	private long payload(String input, int lineStart, int lineEnd) {
		int payloadStart = -1;
		int payloadEnd = -1;
		int lastNonEmptySegment = -1;
		int segmentIndex = 0;
		int segmentStart = lineStart;
		while (true) {
			int separator = -1;
			for (int i = segmentStart; i + 1 < lineEnd; i++) {
				if (input.charAt(i) == ':' && input.charAt(i + 1) == ' ') {
					separator = i;
					break;
				}
			}
			int segmentEnd = separator < 0 ? lineEnd : separator;
			if (segmentIndex == 1) {
				payloadStart = segmentStart;
				payloadEnd = segmentEnd;
			}
			if (segmentEnd > segmentStart) {
				lastNonEmptySegment = segmentIndex;
			}
			if (separator < 0) {
				break;
			}
			segmentIndex++;
			segmentStart = separator + 2;
		}
		if (lastNonEmptySegment < 1) {
			throw new ArrayIndexOutOfBoundsException(1);
		}
		return ((long) payloadStart << 32) | payloadEnd;
	}

	/**
	 * Mirrors {@code payload.split(" ")[tokenIndex]} for tokenIndex >= 1: returns
	 * the packed range of the token, and throws the same
	 * ArrayIndexOutOfBoundsException when the token does not exist after
	 * String.split's removal of trailing empty tokens.
	 */
	private long token(String input, int payloadStart, int payloadEnd, int tokenIndex) {
		int tokenStart = -1;
		int tokenEnd = -1;
		int lastNonEmptyToken = -1;
		int index = 0;
		int start = payloadStart;
		while (true) {
			int end = start;
			while (end < payloadEnd && input.charAt(end) != ' ') {
				end++;
			}
			if (index == tokenIndex) {
				tokenStart = start;
				tokenEnd = end;
			}
			if (end > start) {
				lastNonEmptyToken = index;
			}
			if (end == payloadEnd) {
				break;
			}
			index++;
			start = end + 1;
		}
		if (lastNonEmptyToken < tokenIndex) {
			throw new ArrayIndexOutOfBoundsException(tokenIndex);
		}
		return ((long) tokenStart << 32) | tokenEnd;
	}

	private boolean itemSeparator(String input, int index, int payloadEnd) {
		return input.charAt(index) == ',' && index + 1 < payloadEnd
				&& input.charAt(index + 1) == ' ';
	}

	private static final class Parsed {
		private final long[][] items;
		private final int[][] constants;
		private final int monkeyCount;
		private final int modulo;

		private Parsed(long[][] items, int[][] constants, int monkeyCount, int modulo) {
			this.items = items;
			this.constants = constants;
			this.monkeyCount = monkeyCount;
			this.modulo = modulo;
		}
	}
}

class FastMonkey {
	private long[] items;
	private int head;
	private int tail;
	private final int operation;
	private final int operand;
	private final int divisor;
	private final int trueTarget;
	private final int falseTarget;
	long counter;

	FastMonkey(long[] startingItems, int[] constants) {
		items = new long[Math.max(16, startingItems.length * 2)];
		for (long item : startingItems) {
			items[tail++] = item;
		}
		operation = constants[0];
		operand = constants[1];
		divisor = constants[2];
		trueTarget = constants[3];
		falseTarget = constants[4];
	}

	void turn(FastMonkey[] monkeys, boolean part1, int modulo) {
		while (head < tail) {
			long item = items[head++];
			switch (operation) {
			case 0:
				item += operand;
				break;
			case 1:
				item *= operand;
				break;
			default:
				item *= item;
				break;
			}
			if (part1) {
				item /= 3;
			}
			item %= modulo;
			monkeys[item % divisor == 0 ? trueTarget : falseTarget].add(item);
			counter++;
		}
		head = 0;
		tail = 0;
	}

	private void add(long item) {
		if (tail == items.length) {
			long[] grown = new long[items.length * 2];
			System.arraycopy(items, head, grown, 0, tail - head);
			tail -= head;
			head = 0;
			items = grown;
		}
		items[tail++] = item;
	}
}
