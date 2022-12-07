package aoc2022;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day7 extends DayTemplate{
	
	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		int answer = 0;
		Node root = new Node("/", null);
		Node current = root;
		while(in.hasNext()) {
			String line = in.nextLine();
			String[] parts = line.split(" ");
			if(line.startsWith("$")) {
				if(parts[1].equals("cd")) {
					switch(parts[2]) {
						case "/":
							current = root;
							break;
						case "..":
							if(current.name.equals(root.name)) {
								break;
							}
							current = current.parent;
							break;
						default:
							for(Node child: current.children) {
								if(child.name.equals(parts[2])) {
									current = child;
								}
							}
					}	
				}
			}
			else {
				Node temp = new Node(parts[1], current);
				if(parts[0].chars().allMatch( Character::isDigit )) {		
					temp.size = Integer.parseInt(parts[0]);
					temp.computed = true;
				}
				else {
					temp.directory = true;
				}
				current.children.add(temp);
			}
		}
		getSizes(root);
		answer = part1?traverse(root, 100000):traverse2(root, root.size - 40000000, root.size);
		return "" + answer;
	}
	public void getSizes(Node root) {
		int size = 0;
		for(Node child: root.children) {
			if(!child.computed) {
				getSizes(child);
			}
			size+=child.size;
		}
		root.size = size;
		root.computed = true;
	}
	
	public int traverse(Node node, int maxsize) {
		int answer = 0;
		if(node.size <= maxsize) {
			answer+=node.size;
		}
		for(Node child: node.children) {
			if(child.directory) {
				answer+= traverse(child, maxsize);
			}
		}
		return answer;
	}
	
	public int traverse2(Node node, int missing, int cur) {
		if(node.size >= missing) {
			cur = Math.min(cur, node.size);
		}
		for(Node child: node.children) {
			if(child.directory) {
				cur = Math.min(cur, traverse2(child, missing, cur));
			}
		}
		return cur;
	}
}

class Node{
	
	boolean computed;
	Node parent; 
	List<Node> children = new ArrayList<>();
	int size;
	String name;
	boolean directory;
	
	public Node(String name, Node parent) {
		this.name = name;
		this.parent = parent;
	}
}