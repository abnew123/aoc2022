package aoc2022;

import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day13 extends DayTemplate {

	@Override
	public String[] fullSolve(Scanner in) {
		Answers answers = analyze(in);
		return new String[] { answers.orderedPairs(), answers.decoderKey() };
	}

	@Override
	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		Answers answers = analyze(in);
		return part1 ? answers.orderedPairs() : answers.decoderKey();
	}

	private Answers analyze(Scanner in) {
		String input = in.useDelimiter("\\A").hasNext() ? in.next() : "";
		Packet dividerTwo = Packet.parse("[[2]]");
		Packet dividerSix = Packet.parse("[[6]]");
		BigInteger orderedPairs = BigInteger.ZERO;
		Packet left = null;
		long pairIndex = 1;
		long rankTwo = 1;
		long rankSix = 2;
		int offset = 0;
		while (offset < input.length()) {
			int lineStart = offset;
			while (offset < input.length() && input.charAt(offset) != '\n'
					&& input.charAt(offset) != '\r') {
				offset++;
			}
			int lineEnd = offset;
			if (offset < input.length()) {
				char ending = input.charAt(offset++);
				if (ending == '\r' && offset < input.length() && input.charAt(offset) == '\n') {
					offset++;
				}
			}
			if (!isBlank(input, lineStart, lineEnd)) {
				Packet packet = Packet.parse(input, lineStart, lineEnd);
				// Input packets precede the appended dividers in the puzzle's stable sort.
				if (packet.compareTo(dividerTwo) <= 0) {
					rankTwo++;
				}
				if (packet.compareTo(dividerSix) <= 0) {
					rankSix++;
				}
				if (left == null) {
					left = packet;
				} else {
					if (left.compareTo(packet) < 0) {
						orderedPairs = orderedPairs.add(BigInteger.valueOf(pairIndex));
					}
					pairIndex++;
					left = null;
				}
			}
		}
		if (left != null) {
			throw new IllegalArgumentException("Packet input must contain complete pairs");
		}
		BigInteger decoderKey = BigInteger.valueOf(rankTwo)
				.multiply(BigInteger.valueOf(rankSix));
		return new Answers(orderedPairs.toString(), decoderKey.toString());
	}

	private boolean isBlank(String input, int start, int end) {
		for (int i = start; i < end; i++) {
			if (!Character.isWhitespace(input.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	private record Answers(String orderedPairs, String decoderKey) {}
}

final class Packet implements Comparable<Packet> {
	private final List<Packet> children;
	private final long smallValue;
	private final BigInteger bigValue;

	private Packet(long smallValue, BigInteger bigValue, List<Packet> children) {
		this.smallValue = smallValue;
		this.bigValue = bigValue;
		this.children = children;
	}

	static Packet parse(String text) {
		return parse(text, 0, text.length());
	}

	static Packet parse(String text, int start, int end) {
		Parser parser = new Parser(text, start, end);
		Packet packet = parser.parsePacket();
		parser.skipWhitespace();
		if (!parser.atEnd()) {
			throw parser.error("Unexpected trailing character");
		}
		return packet;
	}

	@Override
	public int compareTo(Packet other) {
		if (children == null && other.children == null) {
			if (bigValue == null && other.bigValue == null) {
				return Long.compare(smallValue, other.smallValue);
			}
			BigInteger thisValue = bigValue == null
					? BigInteger.valueOf(smallValue) : bigValue;
			BigInteger otherValue = other.bigValue == null
					? BigInteger.valueOf(other.smallValue) : other.bigValue;
			return thisValue.compareTo(otherValue);
		}
		if (children != null && other.children != null) {
			int common = Math.min(children.size(), other.children.size());
			for (int i = 0; i < common; i++) {
				int comparison = children.get(i).compareTo(other.children.get(i));
				if (comparison != 0) {
					return comparison;
				}
			}
			return Integer.compare(children.size(), other.children.size());
		}
		return children == null
				? compareValueToList(this, other)
				: -compareValueToList(other, this);
	}

	private static int compareValueToList(Packet scalar, Packet list) {
		if (list.children.isEmpty()) {
			return 1;
		}
		int first = scalar.compareTo(list.children.get(0));
		return first != 0 ? first : Integer.compare(1, list.children.size());
	}

	private static final class Parser {
		private final String text;
		private final int end;
		private int index;

		private Parser(String text, int start, int end) {
			this.text = text;
			this.index = start;
			this.end = end;
		}

		private Packet parsePacket() {
			skipWhitespace();
			if (atEnd()) {
				throw error("Expected packet");
			}
			if (text.charAt(index) == '[') {
				index++;
				List<Packet> children = new ArrayList<>();
				skipWhitespace();
				if (!atEnd() && text.charAt(index) == ']') {
					index++;
					return new Packet(0, null, children);
				}
				while (true) {
					children.add(parsePacket());
					skipWhitespace();
					if (atEnd()) {
						throw error("Missing closing bracket");
					}
					char separator = text.charAt(index++);
					if (separator == ']') {
						return new Packet(0, null, children);
					}
					if (separator != ',') {
						throw error("Expected comma or closing bracket");
					}
				}
			}

			int start = index;
			if (text.charAt(index) == '+' || text.charAt(index) == '-') {
				index++;
			}
			int digits = index;
			while (!atEnd() && text.charAt(index) >= '0' && text.charAt(index) <= '9') {
				index++;
			}
			if (index == digits) {
				throw error("Expected integer");
			}
			try {
				return new Packet(Long.parseLong(text, start, index, 10), null, null);
			} catch (NumberFormatException e) {
				return new Packet(0, new BigInteger(text.substring(start, index)), null);
			}
		}

		private void skipWhitespace() {
			while (!atEnd() && Character.isWhitespace(text.charAt(index))) {
				index++;
			}
		}

		private boolean atEnd() {
			return index == end;
		}

		private IllegalArgumentException error(String message) {
			return new IllegalArgumentException(message + " at character " + index);
		}
	}
}
