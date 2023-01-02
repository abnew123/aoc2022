package aoc2022;

import java.io.FileNotFoundException;
import java.util.Scanner;

public abstract class DayTemplate {
	
	/**
	 * Times execution of the solve method
	 * @param part1
	 * Param for which day solve() will solve.
	 * @param in
	 * Param for data solve() will read.
	 * @return
	 * Time in milliseconds (not nanoseconds) for execution of the method.
	 * @throws FileNotFoundException
	 */
	public double timer(boolean part1, Scanner in) throws FileNotFoundException{
		Long startTime = System.nanoTime();
		solve(part1, in);
		Long endTime = System.nanoTime();
		return (endTime - startTime)/1000000.0;
	}
	
	/**
	 * Main solving method. 
	 * @param part1
	 * The solver will solve part 1 if param is set to true.
	 * The solver will solve part 2 if param is set to false.
	 * @param in
	 * The solver will read data from this Scanner.
	 * @return
	 * Returns answer in string format.
	 * @throws FileNotFoundException
	 */
	public abstract String solve(boolean part1, Scanner in) throws FileNotFoundException;
	
	/**
	 * Some classes require additional, non code steps (e.g. judge an image output).
	 * In those cases, we do not want to run the solver.
	 * @return
	 * By default, returns false.
	 * Subclasses can override in exceptional cases.
	 */
	public boolean exclude() {
		return false;
	}
}
