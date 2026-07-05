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
		boolean runTimer = true;
		boolean totalTimer = false;
		boolean exclusionTimer = true;
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
		if (runTimer) {
			timer(totalTimer, exclusionTimer);
		}
	}

	/**
	 * New timer method. Supports modality
	 * 
	 * @param total     Timer will give the sum total execution time if param set to
	 *                  true. Timer will give individual days times by part if param
	 *                  is set to false. Note that even if param is set to false,
	 *                  total time will be given.
	 * @param exclusion Timer will exclude days that return exceptions if param is
	 *                  set to true. Timer will execute all days if param is set to
	 *                  false.
	 * @throws Exception
	 */

	public static void timer(boolean total, boolean exclusion) throws Exception {
		Double totalTime = 0.0;
		for (int day = 1; day <= 25; day++) {
			String zeroFilledDay = (day < 10 ? "0" : "") + day;
			for (int part = 1; part <= 2; part++) {
				Class<?> cls = Class.forName(className(day, zeroFilledDay));
				File file = new File("./data/day" + zeroFilledDay + ".txt");
				if (!useGolfed && exclusion && (boolean) cls.getMethod("exclude")
						.invoke(cls.getDeclaredConstructor().newInstance())) {
					continue;
				}
				Double time;
				if (useGolfed) {
					long start = System.nanoTime();
					solve(cls, part == 1, file);
					time = (System.nanoTime() - start) / 1000000.0;
				} else {
					time = (Double) cls.getMethod("timer", boolean.class, Scanner.class)
							.invoke(cls.getDeclaredConstructor().newInstance(), part == 1, new Scanner(file));
				}
				if (!total) {
					System.out.println("Day " + zeroFilledDay + " part " + part + " execution time: " + time);
				}
				totalTime += time;
			}
		}
		System.out.println("Total execution time (ms): " + totalTime);
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
