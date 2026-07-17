package aoc2022;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.Scanner;

public class MasterSolver {
	private static final String[] GOLFED_DAYS = "A B C D E F G H I J K L M N O P Q R S T U V W X Y".split(" ");
	private static boolean useGolfed;

	public static void main(String[] args) throws Exception {

		// inputs.
		useGolfed = false;
		int[] days = new int[] { };
		boolean[] parts = new boolean[] { true, false };

		// Do not change anything in the method below this comment

		for (int day : days) {
			String zeroFilledDay = (day < 10 ? "0" : "") + day;
			for (boolean part1 : parts) {
				File file = new File("./data/day" + zeroFilledDay + ".txt");
				Class<?> cls = Class.forName(className(day, zeroFilledDay));
				String answer = solve(cls, part1, file);
				System.out.println(
						"Day " + zeroFilledDay+ " part " + (part1 ? 1 : 2) + " solution: " + answer);
			}
		}
	}

	private static String className(int day, String zeroFilledDay) {
		return useGolfed ? "aoc2022." + GOLFED_DAYS[day - 1] : "aoc2022.Day" + zeroFilledDay;
	}

	private static String solve(Class<?> cls, boolean part1, File file) throws Exception {
		Object solver = cls.getDeclaredConstructor().newInstance();
		if (useGolfed) {
			Method m = cls.getDeclaredMethod("s", boolean.class, String.class);
			m.setAccessible(true);
			return (String) m.invoke(solver, part1, Files.readString(file.toPath()));
		}
		try (Scanner in = new Scanner(file)) {
			Method m = cls.getDeclaredMethod("solve", boolean.class, Scanner.class);
			return (String) m.invoke(solver, part1, in);
		}
	}
}
