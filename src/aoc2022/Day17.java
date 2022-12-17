package aoc2022;

import java.io.*;
import java.util.*;

public class Day17 extends DayTemplate {

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		int[][] chamber = new int[7][2100];
		String[] pushes = in.nextLine().split("");
		List<Integer> indices = new ArrayList<>();
		List<Integer> heights = new ArrayList<>();
		boolean[] left = new boolean[pushes.length];
		for (int i = 0; i < left.length; i++) {
			left[i] = pushes[i].equals("<");
		}
		long goal = part1?2022:1000000000000L;
		int jetIndex = 0;
		int lowest = 0;
		int lowestOffset = 0;
		int prev = 0;
		
		return "" + 0;
	}
	public long helper(int num) {
		for (int i = 0; i < num; i++) {
			switch (i % 5) {
			case 0:
				chamber[2][lowest + 3] = 2;
				chamber[3][lowest + 3] = 2;
				chamber[4][lowest + 3] = 2;
				chamber[5][lowest + 3] = 2;
				break;
			case 1:
				chamber[3][lowest + 5] = 2;
				chamber[2][lowest + 4] = 2;
				chamber[3][lowest + 4] = 2;
				chamber[4][lowest + 4] = 2;
				chamber[3][lowest + 3] = 2;
				break;
			case 2:
				chamber[4][lowest + 5] = 2;
				chamber[4][lowest + 4] = 2;
				chamber[2][lowest + 3] = 2;
				chamber[3][lowest + 3] = 2;
				chamber[4][lowest + 3] = 2;
				break;
			case 3:
				chamber[2][lowest + 6] = 2;
				chamber[2][lowest + 5] = 2;
				chamber[2][lowest + 4] = 2;
				chamber[2][lowest + 3] = 2;
				break;
			case 4:
				chamber[2][lowest + 3] = 2;
				chamber[3][lowest + 3] = 2;
				chamber[2][lowest + 4] = 2;
				chamber[3][lowest + 4] = 2;
				break;
			}
			while(true) {
				jet(chamber, left[jetIndex]);
				jetIndex = (jetIndex + 1)%left.length;
				if(jetIndex == 0) {
					System.out.println(i);
				}
				if(resting(chamber)) {
					break;
				}
				down(chamber);
			}
			settle(chamber);
			if((i - 1732)%1740 == 0) {
				indices.add(jetIndex);
				heights.add(lowest + lowestOffset - prev);
				prev = lowest + lowestOffset;
			}
			lowest = findLowest(chamber);
			if(lowest > 2000) {
				lowestOffset += 1000;
				lowest-=1000;
				compact(chamber);
			}
		}
		System.out.println(heights);
		System.out.println(indices);
		return "" + (lowest + lowestOffset);
	}
	
	public void compact(int[][] chamber) {
		int[][] tmp = new int[chamber.length][chamber[0].length];
		for(int i = 1000; i < chamber[0].length;i++) {
			for(int j = 0; j < chamber.length; j++) {
				tmp[j][i-1000] = chamber[j][i];
			}
		}
		for(int i = 0; i < chamber[0].length;i++) {
			for(int j = 0; j < chamber.length; j++) {
				chamber[j][i] = tmp[j][i];
			}
		}
	}

	public int findLowest(int[][] chamber) {
		int lowest = 0;
		for (int i = 0; i < chamber[0].length; i++) {
			boolean exists = false;
			for (int j = 0; j < chamber.length; j++) {
				exists |= (chamber[j][i] > 0);
			}
			if (exists) {
				lowest++;
			}
			if (!exists) {
				return lowest;
			}
		}
		return lowest;
	}
	
	public boolean resting(int[][] chamber) {
		for (int j = 0; j < chamber.length; j++) {
			if(chamber[j][0] == 2) {
				return true;
			}
		}
		for (int i = 1; i < chamber[0].length; i++) {
			for (int j = 0; j < chamber.length; j++) {
				if(chamber[j][i] == 2) {
					if(chamber[j][i-1] ==1) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void down(int[][] chamber) {
		if(!resting(chamber)) {
			for (int i = 0; i < chamber[0].length; i++) {
				for (int j = 0; j < chamber.length; j++) {
					if(chamber[j][i] == 2) {
						chamber[j][i-1] = 2;
						chamber[j][i] = 0;
					}
				}
			}
				
		}
	}
	
	public void prettyprint(int[][] chamber) {
		for (int k = 50; k >=0; k--) {
			for (int j = 0; j < chamber.length; j++) {
				System.out.print(chamber[j][k]);
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();
	}
	
	public void jet(int[][] chamber, boolean left) {
		if(left) {
			for (int i = 0; i < chamber[0].length; i++) {
				if(chamber[0][i] == 2) {
					return;
				}
			}
			for (int i = 0; i < chamber[0].length; i++) {
				for (int j = 1; j < chamber.length; j++) {
					if(chamber[j][i] == 2) {
						if(chamber[j-1][i] == 1) {
							return;
						}
					}
				}
			}
		}
		else {
			for (int i = 0; i < chamber[0].length; i++) {
				if(chamber[chamber.length - 1][i] == 2) {
					return;
				}
			}
			for (int i = 0; i < chamber[0].length; i++) {
				for (int j = 0; j < chamber.length - 1; j++) {
					if(chamber[j][i] == 2) {
						if(chamber[j+1][i] == 1) {
							return;
						}
					}
				}
			}
		}
		for (int i = 0; i < chamber[0].length; i++) {
			if(left) {
				for (int j = 1; j < chamber.length; j++) {
					if(chamber[j][i] == 2) {
						chamber[j-1][i] = 2;
						chamber[j][i] = 0;
					}
				}
			}
			if(!left) {
				for (int j = chamber.length - 2; j >=0; j--) {
					if(chamber[j][i] == 2) {
						chamber[j+1][i] = 2;
						chamber[j][i] = 0;
					}
				}
			}
		}
			
		
		
	}
	
	public void settle(int[][] chamber){
		for (int i = 0; i < chamber[0].length; i++) {
			for (int j = 0; j < chamber.length; j++) {
				if(chamber[j][i] == 2) {
					chamber[j][i] = 1;
				}
			}
		}
			
	}
}
