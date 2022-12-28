package aoc2022;

import java.io.*;
import java.util.*;

public class Day22 extends DayTemplate {

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		int answer = 0;
		int[][] grid = new int[1000][1000];
		int xloc = 0;
		int yloc = 0;
		int dir = 0; // right = 0, down = 1, left = 2, up = 3
		List<String> instructions = new ArrayList<>();
		int counter = 0;
		while (in.hasNext()) {
			String line = in.nextLine();
			if (line.contains(".")) {
				for(int i = 0; i < line.length(); i++) {
					grid[counter][i] = (line.charAt(i) == '.')?1:(line.charAt(i) == '#')?2:0;
				}
			} else {
				if (line.length() > 2) {
					String tmp = "";
					for (int i = 0; i < line.length(); i++) {
						if (line.charAt(i) == 'L' || line.charAt(i) == 'R') {
							instructions.add(tmp);
							tmp = "";
							instructions.add("" + line.charAt(i));
						}
						else {
							tmp += line.charAt(i);
						}
						
					}
					if (!tmp.equals("")) {
						instructions.add(tmp);
					}
				}
			}
			counter++;
		}
		for(int i = 0; i < grid[0].length;i++) {
			if(grid[xloc][i] == 1) {
				yloc = i;
				break;
			}
		}
		for (String instruction : instructions) {
			if (instruction.equals("L")) {
				dir = (dir + 3) % 4;
				continue;
			}
			if (instruction.equals("R")) {
				dir = (dir + 1) % 4;
				continue;
			}
			int distance = Integer.parseInt(instruction);
			int[] ydel = new int[] {1,0,-1,0};
			int[] xdel = new int[] {0,1,0,-1};
			for(int i = 0; i < distance; i++) {
				int origx = xloc;
				int origy = yloc;
				int goalx = xloc + xdel[dir];
				int goaly = yloc + ydel[dir];
				if(!part1) {
					if(get(grid,goalx,goaly) == 0) {
						//19 possible ways to exit net
						if(goalx > 0 && goaly > 0) {
							goalx = 2;
							goaly = 2;
							dir = 5;
						}
						if(goalx > 0 && goaly > 0) {
							goalx = 2;
							goaly = 2;
							dir = 5;
						}
						if(goalx > 0 && goaly > 0) {
							goalx = 2;
							goaly = 2;
							dir = 5;
						}
						if(goalx > 0 && goaly > 0) {
							goalx = 2;
							goaly = 2;
							dir = 5;
						}
						if(goalx > 0 && goaly > 0) {
							goalx = 2;
							goaly = 2;
							dir = 5;
						}
						if(goalx > 0 && goaly > 0) {
							goalx = 2;
							goaly = 2;
							dir = 5;
						}
						if(goalx > 0 && goaly > 0) {
							goalx = 2;
							goaly = 2;
							dir = 5;
						}
						if(goalx > 0 && goaly > 0) {
							goalx = 2;
							goaly = 2;
							dir = 5;
						}
						if(goalx > 0 && goaly > 0) {
							goalx = 2;
							goaly = 2;
							dir = 5;
						}
						if(goalx > 0 && goaly > 0) {
							goalx = 2;
							goaly = 2;
							dir = 5;
						}
						if(goalx > 0 && goaly > 0) {
							goalx = 2;
							goaly = 2;
							dir = 5;
						}
						if(goalx > 0 && goaly > 0) {
							goalx = 2;
							goaly = 2;
							dir = 5;
						}
						if(goalx > 0 && goaly > 0) {
							goalx = 2;
							goaly = 2;
							dir = 5;
						}
						if(goalx > 0 && goaly > 0) {
							goalx = 2;
							goaly = 2;
							dir = 5;
						}
						if(goalx > 0 && goaly > 0) {
							goalx = 2;
							goaly = 2;
							dir = 5;
						}
						if(goalx > 0 && goaly > 0) {
							goalx = 2;
							goaly = 2;
							dir = 5;
						}
						if(goalx > 0 && goaly > 0) {
							goalx = 2;
							goaly = 2;
							dir = 5;
						}
						if(goalx > 0 && goaly > 0) {
							goalx = 2;
							goaly = 2;
							dir = 5;
						}
						if(goalx > 0 && goaly > 0) {
							goalx = 2;
							goaly = 2;
							dir = 5;
						}
					}
				}
				
				if(part1) {
					xloc += xdel[dir];
					yloc += ydel[dir];
					while(get(grid,xloc, yloc) == 0) {
						xloc += xdel[dir];
						yloc += ydel[dir];
					}
					if(get(grid,xloc,yloc) == 2) {
						xloc = origx;
						yloc = origy;
						break;
					}
				}
			}
			xloc = ((xloc%grid.length) + grid.length)%grid.length;
			yloc = ((yloc%grid[0].length) + grid[0].length)%grid[0].length;
		}
		
		xloc++;
		yloc++;
		answer = (1000 * xloc) + (4 * yloc) + dir;
		return "" + answer;
	}
	
	public int get(int[][] grid, int xloc, int yloc) {
		return grid[((xloc%grid.length) + grid.length)%grid.length][((yloc%grid[0].length) + grid[0].length)%grid[0].length];
	}
}
