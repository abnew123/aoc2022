package aoc2022;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day7 extends DayTemplate{
	
	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		long answer = 0;
		Node root = new Node("/");
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
						default:
							for(Node child: current.children) {
								if(child.name.equals(parts[2])) {
									current = child;
								}
							}
					}	
				}
				else {
					if(parts[1].equals("ls")) {
						//ls is a no-op
					}
				}
			}
			else {
				Node temp = new Node(parts[1]);
				temp.parent = current;
				if(parts[0].chars().allMatch( Character::isDigit )) {		
					temp.size = Long.parseLong(parts[0]);
					temp.computed = true;
					temp.directory = false;
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
		long size = 0;
		for(Node child: root.children) {
			if(child.computed) {
				size+=child.size;
			}
			else {
				getSizes(child);
				size+=child.size;
			}
		}
		root.size = size;
		root.computed = true;
	}
	
	public long traverse(Node node, long maxsize) {
		long answer = 0;
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
	
	public long traverse2(Node node, long missing, long cur) {
		if(node.size >= missing && node.size < cur) {
			cur = node.size;
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
	List<Node> children;
	long size;
	String name;
	boolean directory;
	
	public Node(String name) {
		this.name = name;
		size = 0;
		computed = false;
		children = new ArrayList<>();
	}
}