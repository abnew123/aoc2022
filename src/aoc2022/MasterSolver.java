package aoc2022;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Scanner;

public class MasterSolver {
	public static void main(String[] args) throws Exception {
		streamTime();
		int day = 1;
		boolean part1 = true;
		File file = new File("./data/day" + day + ".txt");
		Scanner in = new Scanner(file);
		Class<?> cls = Class.forName("aoc2022.Day" + day);
		Method m = cls.getDeclaredMethod("solve", boolean.class, Scanner.class);
		String answer = (String) m.invoke(cls.getDeclaredConstructor().newInstance(), part1, in);
		System.out.println("Day " + day + " part " + (part1?1:2) + " solution: " + answer);
		in.close();
		timer();
	}
	
	public static void timer() throws Exception {
		for(int i = 1; i <= 25; i++) {
			Long startTime = System.nanoTime();
			int day = i;
			for(int j = 0; j < 2; j++) {
				boolean part1 = j == 0;
				File file = new File("./data/day" + day + ".txt");
				Scanner in = new Scanner(file);
				Class<?> cls = Class.forName("aoc2022.Day" + day);
				Method m = cls.getDeclaredMethod("solve", boolean.class, Scanner.class);
				String answer = (String) m.invoke(cls.getDeclaredConstructor().newInstance(), part1, in);
				System.out.println("Day " + day + " part " + (part1?1:2) + " solution: " + answer);
				in.close();
			}
			Long endTime = System.nanoTime();
			System.out.println("Day " + day + " execution time (ms): " + (endTime - startTime)/ 1000000);
		}
	}
	
	public static void streamTime() throws Exception {
		Double totalTime = 0.0;
		for(int i = 1; i < 25; i++) {
			int day = i;
			for(int j = 0; j < 2; j++) {
				totalTime += (Double) Class.forName("aoc2022.Day" + day).getMethod("timer", boolean.class, Scanner.class).invoke(Class.forName("aoc2022.Day" + day).getDeclaredConstructor().newInstance(), j == 0, new Scanner(new File("./data/day" + day + ".txt")));;
			}
		}
		System.out.println("Total execution time (ms): " + totalTime);
	}
}
