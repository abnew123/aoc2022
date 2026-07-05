package aoc2022;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Scanner;

public class MasterSolver {

	public static void main(String[] args) throws Exception {

		// inputs.
		boolean runTimer = true;
		boolean totalTimer = false;
		int[] days = new int[] { };
		boolean[] parts = new boolean[] { true, false };

		// Do not change anything in the method below this comment

		for (int day : days) {
			String zeroFilledDay = (day < 10 ? "0" : "") + day;
			for (boolean part1 : parts) {
				File file = new File("./data/day" + zeroFilledDay + ".txt");
				Scanner in = new Scanner(file);
				Class<?> cls = Class.forName("aoc2022.Day" + zeroFilledDay);
				Method m = cls.getDeclaredMethod("solve", boolean.class, Scanner.class);
				String answer = (String) m.invoke(cls.getDeclaredConstructor().newInstance(), part1, in);
				System.out.println(
						"Day " + zeroFilledDay+ " part " + (part1 ? 1 : 2) + " solution: " + answer);
				in.close();
			}
		}
		if (runTimer) {
			timer(totalTimer);
		}
	}

	/**
	 * New timer method. Supports modality
	 * 
	 * @param total     Timer will give the sum total execution time if param set to
	 *                  true. Timer will give individual days times by part if param
	 *                  is set to false. Note that even if param is set to false,
	 *                  total time will be given.
	 * @throws Exception
	 */

	public static void timer(boolean total) throws Exception {
		Double totalTime = 0.0;
		for (int day = 1; day <= 25; day++) {
			String zeroFilledDay = (day < 10 ? "0" : "") + day;
			Double time = (Double) Class.forName("aoc2022.Day" + zeroFilledDay)
					.getMethod("dayTimer", Scanner.class)
					.invoke(Class.forName("aoc2022.Day" + zeroFilledDay).getDeclaredConstructor().newInstance(),
							new Scanner(new File("./data/day" + zeroFilledDay + ".txt")));
			if (!total) {
				System.out.println("Day " + zeroFilledDay + " execution time: " + time);
			}
			totalTime += time;
		}
		System.out.println("Total execution time (ms): " + totalTime);
	}
}
