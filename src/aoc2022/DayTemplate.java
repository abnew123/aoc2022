package aoc2022;

import java.io.*;
import java.util.*;

public abstract class DayTemplate {
	public double timer(boolean part1, Scanner in) throws FileNotFoundException{
		Long startTime = System.nanoTime();
		solve(part1, in);
		Long endTime = System.nanoTime();
		return (endTime - startTime)/1000000.0;
	}
	
	public abstract String solve(boolean part1, Scanner in) throws FileNotFoundException;
}
