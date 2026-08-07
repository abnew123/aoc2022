package aoc2022;

import java.io.FileNotFoundException;
import java.util.Scanner;

public abstract class DayTemplate {

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
	 * Solves both parts from the same input snapshot. Days may override this when
	 * sharing parsed state or computation is useful.
	 */
	public String[] fullSolve(Scanner in) throws FileNotFoundException {
		String input = in.useDelimiter("\\A").hasNext() ? in.next() : "";
		try (Scanner part1Input = new Scanner(input);
			 Scanner part2Input = new Scanner(input)) {
			return new String[] {
					freshSolver().solve(true, part1Input),
					freshSolver().solve(false, part2Input)
			};
		}
	}

	private DayTemplate freshSolver() {
		try {
			return getClass().getDeclaredConstructor().newInstance();
		} catch (ReflectiveOperationException exception) {
			throw new IllegalStateException(
					"Solver must have an accessible no-argument constructor", exception);
		}
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
