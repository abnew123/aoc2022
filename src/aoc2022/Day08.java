package aoc2022;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day08 extends DayTemplate {

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		int visibleTrees = 0;
		int bestScore = 0;
		List<List<Integer>> grid = new ArrayList<>();
		while (in.hasNext()) {
			List<Integer> temp = new ArrayList<>();
			for (String s : in.nextLine().split("")) {
				temp.add(Integer.parseInt(s));
			}
			grid.add(temp);
		}
		for (int i = 0; i < grid.size(); i++) {
			for (int j = 0; j < grid.get(0).size(); j++) {
				if (checkVisibility(grid, i, j)) {
					visibleTrees++;
				}
				if(!part1) {
					bestScore = Math.max(bestScore, calculateScore(grid, i, j));
				}
			}
		}
		return "" + (part1 ? visibleTrees : bestScore);
	}

	public boolean checkVisibility(List<List<Integer>> grid, int i, int j) {
		if (i == 0 || j == 0 || (i == grid.size() - 1) || (j == grid.get(0).size() - 1)) {
			return true;
		}
		boolean visible = false;
		boolean directionVisible = true;
		for (int row = i + 1; row < grid.size(); row++) {
			if (grid.get(row).get(j) >= grid.get(i).get(j)) {
				directionVisible = false;
				break;
			}
		}
		visible = visible || directionVisible;
		directionVisible = true;
		for (int row = i - 1; row >= 0; row--) {
			if (grid.get(row).get(j) >= grid.get(i).get(j)) {
				directionVisible = false;
				break;
			}
		}
		visible = visible || directionVisible;
		directionVisible = true;
		for (int column = j + 1; column < grid.get(0).size(); column++) {
			if (grid.get(i).get(column) >= grid.get(i).get(j)) {
				directionVisible = false;
				break;
			}
		}
		visible = visible || directionVisible;
		directionVisible = true;
		for (int column = j - 1; column >= 0; column--) {
			if (grid.get(i).get(column) >= grid.get(i).get(j)) {
				directionVisible = false;
				break;
			}
		}
		visible = visible || directionVisible;
		return visible;
	}

	public int calculateScore(List<List<Integer>> grid, int i, int j) {
		int score = 1;
		int directionVisible = 0;
		for (int row = i + 1; row < grid.size(); row++) {
			directionVisible++;
			if (grid.get(row).get(j) >= grid.get(i).get(j)) {
				break;
			}
		}
		score *= directionVisible;
		directionVisible = 0;
		for (int row = i - 1; row >= 0; row--) {
			directionVisible++;
			if (grid.get(row).get(j) >= grid.get(i).get(j)) {
				break;
			}
		}
		score *= directionVisible;
		directionVisible = 0;
		for (int column = j + 1; column < grid.get(0).size(); column++) {
			directionVisible++;
			if (grid.get(i).get(column) >= grid.get(i).get(j)) {
				break;
			}
		}
		score *= directionVisible;
		directionVisible = 0;
		for (int column = j - 1; column >= 0; column--) {
			directionVisible++;
			if (grid.get(i).get(column) >= grid.get(i).get(j)) {

				break;
			}
		}
		score *= directionVisible;
		return score;
	}
}
