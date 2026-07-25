package src.meta;

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
	 * Times execution of an entire day.
	 * @param in
	 * Param for data fullSolve() will read.
	 * @return
	 * Time in milliseconds (not nanoseconds) for execution of the method.
	 * @throws FileNotFoundException
	 */
	public double dayTimer(Scanner in) throws FileNotFoundException{
		Long startTime = System.nanoTime();
		fullSolve(in);
		Long endTime = System.nanoTime();
		return (endTime - startTime)/1000000.0;
	}

	/**
	 * Solves both parts from a single pass over the input.
	 * Parsing and any part-agnostic precomputation is shared, so this is
	 * faster than calling solve() twice.
	 * @param in
	 * The solver will read data from this Scanner.
	 * @return
	 * A two element array: index 0 is the part 1 answer, index 1 is part 2.
	 * By default, returns an array of nulls, meaning the day has no
	 * single-pass implementation.
	 * @throws FileNotFoundException
	 */
	public String[] fullSolve(Scanner in) throws FileNotFoundException{
		return new String[2];
	}

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
