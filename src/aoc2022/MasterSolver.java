package aoc2022;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Scanner;

public class MasterSolver {
	
	public static void main(String[] args) throws Exception {
		
		//inputs. 
		boolean runTimer = true;
		boolean totalTimer = false;
		boolean exclusionTimer = true;
		int[] days = new int[] {};
		boolean[] parts = new boolean[] {};
		
		//Do not change anything in the method below this comment
		
		for(int day: days) {
			for(boolean part1: parts) {
				File file = new File("./data/day" + day + ".txt");
				Scanner in = new Scanner(file);
				Class<?> cls = Class.forName("aoc2022.Day" + day);
				Method m = cls.getDeclaredMethod("solve", boolean.class, Scanner.class);
				String answer = (String) m.invoke(cls.getDeclaredConstructor().newInstance(), part1, in);
				System.out.println("Day " + day + " part " + (part1?1:2) + " solution: " + answer);
				in.close();
			}
		}
		if(runTimer) {
			timer(totalTimer, exclusionTimer);
		}
	}
	
//	/**
//	 * Old timer method. Significantly more verbose (and arguably readable than the stream based one that's now being used)
//	 * Single setting: will print each day's part 1 and part 2 execution times, then the day's total execution time. 
//	 */
//	
//	public static void timer_old() throws Exception {
//		for(int i = 1; i <= 25; i++) {
//			Long startTime = System.nanoTime();
//			int day = i;
//			for(int j = 0; j < 2; j++) {
//				boolean part1 = j == 0;
//				File file = new File("./data/day" + day + ".txt");
//				Scanner in = new Scanner(file);
//				Class<?> cls = Class.forName("aoc2022.Day" + day);
//				Method m = cls.getDeclaredMethod("solve", boolean.class, Scanner.class);
//				String answer = (String) m.invoke(cls.getDeclaredConstructor().newInstance(), part1, in);
//				System.out.println("Day " + day + " part " + (part1?1:2) + " solution: " + answer);
//				in.close();
//			}
//			Long endTime = System.nanoTime();
//			System.out.println("Day " + day + " execution time (ms): " + (endTime - startTime)/ 1000000);
//		}
//	}
	
	/**
	 * New timer method. Supports modality
	 * @param total
	 * Timer will give the sum total execution time if param set to true.
	 * Timer will give individual days times by part if param is set to false.
	 * Note that even if param is set to false, total time will be given.
	 * @param exclusion
	 * Timer will exclude days that return exceptions if param is set to true.
	 * Timer will execute all days if param is set to false.
	 * @throws Exception
	 */
	
	public static void timer(boolean total, boolean exclusion) throws Exception {
		Double totalTime = 0.0;
		for(int day = 1; day < 25; day++) {
			for(int part = 1; part <= 2; part++) {
				boolean exclude = (boolean) Class.forName("aoc2022.Day" + day).getMethod("exclude").invoke(Class.forName("aoc2022.Day" + day).getDeclaredConstructor().newInstance());
				if(exclusion && exclude) {
					continue;
				}
				Double time = (Double) Class.forName("aoc2022.Day" + day).getMethod("timer", boolean.class, Scanner.class).invoke(Class.forName("aoc2022.Day" + day).getDeclaredConstructor().newInstance(), part == 1, new Scanner(new File("./data/day" + day + ".txt")));
				if(!total) {
					System.out.println("Day " + day + " part " + part + " execution time: " + time);
				}
				totalTime+=time;
			}
		}
		System.out.println("Total execution time (ms): " + totalTime);
	}
}
