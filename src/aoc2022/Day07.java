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
		List<Directory> directories = new ArrayList<>();
		Directory root = new Directory("/", null);
		directories.add(root);
		Directory current = root;

		while (in.hasNextLine()) {
			String line = in.nextLine();
			int start = skipWhitespace(line, 0);
			if (start == line.length()) {
				continue;
			}
			if (line.charAt(start) == '$') {
				current = command(line, start + 1, current, root, directories);
			} else {
				listing(line, start, current, directories);
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

	private Directory command(String line, int index, Directory current, Directory root,
			List<Directory> directories) {
		index = skipWhitespace(line, index);
		int commandStart = index;
		index = skipToken(line, index);
		String command = line.substring(commandStart, index);
		index = skipWhitespace(line, index);
		if (command.equals("ls") && index == line.length()) {
			return current;
		}
		if (!command.equals("cd") || index == line.length()) {
			throw malformed(line);
		}
		String destination = line.substring(index);
		if (destination.equals("/")) {
			return root;
		}
		if (destination.equals("..")) {
			return current.parent == null ? current : current.parent;
		}
		return current.directory(destination, directories);
	}

	private void listing(String line, int index, Directory current,
			List<Directory> directories) {
		int firstEnd = skipToken(line, index);
		if (firstEnd == line.length()) {
			throw malformed(line);
		}
		String first = line.substring(index, firstEnd);
		int nameStart = skipWhitespace(line, firstEnd);
		if (nameStart == line.length()) {
			throw malformed(line);
		}
		String name = line.substring(nameStart);
		if (first.equals("dir")) {
			current.directory(name, directories);
			return;
		}
		BigInteger size;
		try {
			size = new BigInteger(first);
		} catch (NumberFormatException exception) {
			throw new IllegalArgumentException("Malformed terminal output: " + line, exception);
		}
		if (size.signum() < 0) {
			throw malformed(line);
		}
		current.file(name, size);
	}

	private int skipWhitespace(String line, int index) {
		while (index < line.length() && Character.isWhitespace(line.charAt(index))) {
			index++;
		}
		return index;
	}

	private int skipToken(String line, int index) {
		while (index < line.length() && !Character.isWhitespace(line.charAt(index))) {
			index++;
		}
		return index;
	}

	private IllegalArgumentException malformed(String line) {
		return new IllegalArgumentException("Malformed terminal output: " + line);
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
