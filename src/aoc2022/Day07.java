package aoc2022;

import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Day07 extends DayTemplate {

	private static final BigInteger PART1_LIMIT = BigInteger.valueOf(100_000);
	private static final BigInteger REQUIRED_OFFSET = BigInteger.valueOf(40_000_000);

	@Override
	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		Answers answers = analyze(in);
		return (part1 ? answers.part1 : answers.part2).toString();
	}

	@Override
	public String[] fullSolve(Scanner in) {
		Answers answers = analyze(in);
		return new String[] { answers.part1.toString(), answers.part2.toString() };
	}

	private Answers analyze(Scanner in) {
		String input = in.useDelimiter("\\A").hasNext() ? in.next() : "";
		List<Directory> directories = new ArrayList<>();
		Directory root = new Directory("/", null);
		directories.add(root);
		Directory current = root;

		int offset = 0;
		while (offset < input.length()) {
			int lineStart = offset;
			int lineEnd = lineEnd(input, offset);
			offset = afterLineBreak(input, lineEnd);
			int start = skipWhitespace(input, lineStart, lineEnd);
			if (start == lineEnd) {
				continue;
			}
			if (input.charAt(start) == '$') {
				current = command(input, lineStart, start + 1, lineEnd, current, root, directories);
			} else {
				listing(input, lineStart, start, lineEnd, current, directories);
			}
		}

		BigInteger part1 = BigInteger.ZERO;
		for (Directory directory : directories) {
			if (directory.size.compareTo(PART1_LIMIT) <= 0) {
				part1 = part1.add(directory.size);
			}
		}

		BigInteger missing = root.size.subtract(REQUIRED_OFFSET);
		if (missing.signum() <= 0) {
			return new Answers(part1, BigInteger.ZERO);
		}
		BigInteger part2 = null;
		for (Directory directory : directories) {
			if (directory.size.compareTo(missing) >= 0
					&& (part2 == null || directory.size.compareTo(part2) < 0)) {
				part2 = directory.size;
			}
		}
		if (part2 == null) {
			throw new IllegalArgumentException("No directory can free the required space");
		}
		return new Answers(part1, part2);
	}

	private Directory command(String input, int lineStart, int index, int lineEnd,
			Directory current, Directory root, List<Directory> directories) {
		index = skipWhitespace(input, index, lineEnd);
		int commandStart = index;
		index = skipToken(input, index, lineEnd);
		int commandLength = index - commandStart;
		index = skipWhitespace(input, index, lineEnd);
		if (commandLength == 2 && input.regionMatches(commandStart, "ls", 0, 2)
				&& index == lineEnd) {
			return current;
		}
		if (commandLength != 2 || !input.regionMatches(commandStart, "cd", 0, 2)
				|| index == lineEnd) {
			throw malformed(input, lineStart, lineEnd);
		}
		String destination = input.substring(index, lineEnd);
		if (destination.equals("/")) {
			return root;
		}
		if (destination.equals("..")) {
			return current.parent == null ? current : current.parent;
		}
		return current.directory(destination, directories);
	}

	private void listing(String input, int lineStart, int index, int lineEnd, Directory current,
			List<Directory> directories) {
		int firstEnd = skipToken(input, index, lineEnd);
		if (firstEnd == lineEnd) {
			throw malformed(input, lineStart, lineEnd);
		}
		int nameStart = skipWhitespace(input, firstEnd, lineEnd);
		if (nameStart == lineEnd) {
			throw malformed(input, lineStart, lineEnd);
		}
		String name = input.substring(nameStart, lineEnd);
		if (firstEnd - index == 3 && input.regionMatches(index, "dir", 0, 3)) {
			current.directory(name, directories);
			return;
		}
		BigInteger size;
		try {
			size = new BigInteger(input.substring(index, firstEnd));
		} catch (NumberFormatException exception) {
			throw new IllegalArgumentException(
					"Malformed terminal output: " + input.substring(lineStart, lineEnd), exception);
		}
		if (size.signum() < 0) {
			throw malformed(input, lineStart, lineEnd);
		}
		current.file(name, size);
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

	private int skipWhitespace(String input, int index, int end) {
		while (index < end && Character.isWhitespace(input.charAt(index))) {
			index++;
		}
		return index;
	}

	private int skipToken(String input, int index, int end) {
		while (index < end && !Character.isWhitespace(input.charAt(index))) {
			index++;
		}
		return index;
	}

	private IllegalArgumentException malformed(String input, int start, int end) {
		return new IllegalArgumentException(
				"Malformed terminal output: " + input.substring(start, end));
	}

	private static final class Directory {
		private final String name;
		private final Directory parent;
		private final Map<String, Directory> directories = new HashMap<>();
		private final Map<String, BigInteger> files = new HashMap<>();
		private BigInteger size = BigInteger.ZERO;

		private Directory(String name, Directory parent) {
			this.name = name;
			this.parent = parent;
		}

		private Directory directory(String childName, List<Directory> allDirectories) {
			if (files.containsKey(childName)) {
				throw new IllegalArgumentException("File and directory share name: " + childName);
			}
			Directory child = directories.get(childName);
			if (child == null) {
				child = new Directory(childName, this);
				directories.put(childName, child);
				allDirectories.add(child);
			}
			return child;
		}

		private void file(String fileName, BigInteger fileSize) {
			if (directories.containsKey(fileName)) {
				throw new IllegalArgumentException("File and directory share name: " + fileName);
			}
			BigInteger previous = files.putIfAbsent(fileName, fileSize);
			if (previous != null) {
				if (!previous.equals(fileSize)) {
					throw new IllegalArgumentException("Inconsistent size for file: " + fileName);
				}
				return;
			}
			for (Directory directory = this; directory != null; directory = directory.parent) {
				directory.size = directory.size.add(fileSize);
			}
		}
	}

	private static final class Answers {
		private final BigInteger part1;
		private final BigInteger part2;

		private Answers(BigInteger part1, BigInteger part2) {
			this.part1 = part1;
			this.part2 = part2;
		}
	}
}
