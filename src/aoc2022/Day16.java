package aoc2022;

import java.io.*;
import java.util.*;

public class Day16 extends DayTemplate{
	
	int val = 0;
	int count = 0;
	
	
	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		int answer = 0;
		Valve startingValve = null;
		Valve startingValve2 = null;
		int numPossible = 0;
		int num = 0;
		List<String> open = new ArrayList<>();
		List<Valve> valves = new ArrayList<>();
		while(in.hasNext()) {
			String[] line = in.nextLine().split(" ");
			List<String> tunnels = new ArrayList<>();
			for(int i = 9 ; i <line.length; i++) {
				tunnels.add(line[i].substring(0,2));
			}
			Valve valve = new Valve(line[1], Integer.parseInt(line[4].substring(5, line[4].length() - 1)), tunnels);
			if(valve.name.equals("AA")) {
				startingValve = valve;
				startingValve2 = valve;
			}
			if(valve.flow > 0) {
				numPossible++;
				valve.designation = numPossible;
			}
			num++;
			valve.num = num;
			valves.add(valve);
		}
		int[] memo = new int[(int) Math.pow(2, numPossible + 1) * (valves.size() + 1)*(valves.size() + 1)];
		System.out.println(memo.length);
		answer = optimize(open,valves,startingValve,startingValve2, 15,numPossible,memo);
		return "" + answer;
	}
	
	public int optimize(List<String> open, List<Valve> valves, Valve startingValve, Valve startingValve2, int minLeft, int numPossible, int[] memo) {
		int loc = 1;
		for(String s: open) {
			for(Valve v: valves) {
				if(v.name.equals(s)) {
					loc += (int) Math.pow(2, v.designation);
				}
			}
		}
		int num2 = startingValve.num * (valves.size() + 1) + startingValve2.num;
		loc+= (num2 * Math.pow(2, numPossible + 1));
		if(memo[loc] > minLeft) {
			return 0;
		}
		if(minLeft > memo[loc]) {
			memo[loc] = minLeft;
		}
		count++;
		if(count%100000 == 0) {
			System.out.println(count);
		}
		if(minLeft == 0) {
			return 0;
		}
		int max = 0;
		int currSum = 0;
		for(String s: open) {
			for(Valve v: valves) {
				if(v.name.equals(s)) {
					currSum+= v.flow;
				}
			}
		}
		if(open.size()==numPossible) {
			return currSum * minLeft;
		}
		minLeft--;
		if(!startingValve.name.equals(startingValve2.name)) {
			if(!open.contains(startingValve2.name) && startingValve2.flow > 0) {
				if(!open.contains(startingValve.name) && startingValve.flow > 0) {
					List<String> newOpen = new ArrayList<>();
					newOpen.addAll(open);
					newOpen.add(startingValve.name);
					newOpen.add(startingValve2.name);
					max = Math.max(max,optimize(newOpen, valves, startingValve, startingValve2, minLeft, numPossible,memo));
				}
			}
		}
		for(String s: startingValve.tunnels) {
			for(Valve v: valves) {
				if(v.name.equals(s)) {
					if(!open.contains(startingValve2.name) && startingValve2.flow > 0) {
						List<String> newOpen = new ArrayList<>();
						newOpen.addAll(open);
						newOpen.add(startingValve2.name);
						max = Math.max(max,optimize(newOpen, valves, v, startingValve2, minLeft, numPossible,memo));
					}
				}
			}
		}
		
		for(String s: startingValve2.tunnels) {
			for(Valve v: valves) {
				if(v.name.equals(s)) {
					if(!open.contains(startingValve.name) && startingValve.flow > 0) {
						List<String> newOpen = new ArrayList<>();
						newOpen.addAll(open);
						newOpen.add(startingValve.name);
						max = Math.max(max,optimize(newOpen, valves, startingValve, v, minLeft, numPossible,memo));
					}
				}
			}
		}
		for(String s: startingValve.tunnels) {
			for(Valve v: valves) {
				if(v.name.equals(s)) {
					for(String s2: startingValve.tunnels) {
						for(Valve v2: valves) {
							if(v2.name.equals(s2)) {
								max = Math.max(max, optimize(open, valves, v, v2, minLeft, numPossible,memo));
							}
						}
					}
				}
			}
		}
		return currSum + max;
	}
	
	@Override
	public boolean exclude() {
		return true;
	}
}

class Valve{
	int flow;
	String name;
	List<String> tunnels = new ArrayList<>();
	int designation;
	int num;
	public Valve(String name, int flow, List<String> tunnels) {
		this.name = name;
		this.flow = flow;
		this.tunnels.addAll(tunnels);
	}
}