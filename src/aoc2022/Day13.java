package aoc2022;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Day13 extends DayTemplate {

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		int answer = 0;
		int index = 1;
		List<Packet> packets = new ArrayList<>();
		while (in.hasNext()) {
			Packet packet1 = new Packet(in.nextLine());
			Packet packet2 = new Packet(in.nextLine());
			packets.add(packet1);
			packets.add(packet2);
			in.nextLine();
			if (part1) {
				answer += (packet1.compareTo(packet2) > 0) ? index : 0;
				index++;
			}
		}
		if (!part1) {
			answer = 1;
			packets.add(new Packet("[[2]]"));
			packets.add(new Packet("[[6]]"));
			Collections.sort(packets);
			Collections.reverse(packets);
			for (int i = 0; i < packets.size(); i++) {
				if (packets.get(i).str.equals("[[2]]") || packets.get(i).str.equals("[[6]]")) {
					answer *= (i + 1);
				}
			}
		}
		return "" + answer;
	}
}

class Packet implements Comparable<Packet> {
	List<Packet> children;
	int val;
	boolean integer = true;
	String str;

	public Packet(String packet) {
		str = packet;
		children = new ArrayList<>();
		parse(packet, 0);
	}

	public int compareTo(Packet other) {
		if (integer && other.integer) {
			return other.val - val;
		}
		if (!integer && !other.integer) {
			for (int i = 0; i < Math.min(children.size(), other.children.size()); i++) {
				int val = children.get(i).compareTo(other.children.get(i));
				if (val != 0) {
					return val;
				}
			}
			return other.children.size() - children.size();
		}
		return integer ? compareIntegerToList(val, other) : compareListToInteger(this, other.val);
	}

	private int parse(String packet, int index) {
		if (packet.charAt(index) != '[') {
			int value = 0;
			while (index < packet.length() && Character.isDigit(packet.charAt(index))) {
				value = 10 * value + packet.charAt(index) - '0';
				index++;
			}
			val = value;
			integer = true;
			return index;
		}
		integer = false;
		index++;
		while (packet.charAt(index) != ']') {
			Packet child = new Packet();
			index = child.parse(packet, index);
			children.add(child);
			if (packet.charAt(index) == ',') {
				index++;
			}
		}
		return index + 1;
	}

	private Packet() {
		children = new ArrayList<>();
	}

	private int compareIntegerToList(int value, Packet list) {
		if (list.children.isEmpty()) {
			return -1;
		}
		int firstComparison = compareIntegerToPacket(value, list.children.get(0));
		return firstComparison != 0 ? firstComparison : list.children.size() - 1;
	}

	private int compareListToInteger(Packet list, int value) {
		if (list.children.isEmpty()) {
			return 1;
		}
		int firstComparison = comparePacketToInteger(list.children.get(0), value);
		return firstComparison != 0 ? firstComparison : 1 - list.children.size();
	}

	private int compareIntegerToPacket(int value, Packet other) {
		return other.integer ? other.val - value : compareIntegerToList(value, other);
	}

	private int comparePacketToInteger(Packet packet, int value) {
		return packet.integer ? value - packet.val : compareListToInteger(packet, value);
	}
}
