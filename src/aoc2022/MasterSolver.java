package aoc2022;

import java.io.File;
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
				try (Scanner in = new Scanner(file)) {
					String answer = SolverFactory.create(day).solve(part1, in);
					System.out.println(
							"Day " + zeroFilledDay+ " part " + (part1 ? 1 : 2) + " solution: " + answer);
				}
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
	 * @throws Exception when an input cannot be opened
	 */

	public static void timer(boolean total) throws Exception {
		Double totalTime = 0.0;
		for (int day = 1; day <= 25; day++) {
			String zeroFilledDay = (day < 10 ? "0" : "") + day;
			double time;
			try (Scanner scanner = new Scanner(new File("./data/day" + zeroFilledDay + ".txt"))) {
				time = SolverFactory.create(day).dayTimer(scanner);
			}
			if (!total) {
				System.out.println("Day " + zeroFilledDay + " execution time: " + time);
			}
			totalTime += time;
		}
		System.out.println("Total execution time (ms): " + totalTime);
	}
}
