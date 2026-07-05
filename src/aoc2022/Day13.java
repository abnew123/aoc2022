package aoc2022;

import java.io.FileNotFoundException;
import java.util.*;

public class Day13 extends DayTemplate {
	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		ArrayList<Packet> packets = new ArrayList<>();
		int ans = 0, i = 1;
		while (in.hasNextLine()) {
			Packet a = packet(in.nextLine()), b = packet(in.nextLine());
			packets.add(a);
			packets.add(b);
			if (cmp(a.v, b.v) < 0) {
				ans += i;
			}
			i++;
			if (in.hasNextLine()) {
				in.nextLine();
			}
		}
		if (part1) {
			return "" + ans;
		}
		packets.add(packet("[[2]]"));
		packets.add(packet("[[6]]"));
		packets.sort((a, b) -> cmp(a.v, b.v));
		ans = 1;
		for (i = 0; i < packets.size(); i++) {
			if (packets.get(i).s.equals("[[2]]") || packets.get(i).s.equals("[[6]]")) {
				ans *= i + 1;
			}
		}
		return "" + ans;
	}

	Packet packet(String s) {
		return new Packet(read(s, new int[1]), s);
	}

	Object read(String s, int[] i) {
		if (s.charAt(i[0]) != '[') {
			int n = 0;
			while (i[0] < s.length() && Character.isDigit(s.charAt(i[0]))) {
				n = n * 10 + s.charAt(i[0]++) - '0';
			}
			return n;
		}
		ArrayList<Object> a = new ArrayList<>();
		for (i[0]++; s.charAt(i[0]) != ']';) {
			a.add(read(s, i));
			if (s.charAt(i[0]) == ',') {
				i[0]++;
			}
		}
		i[0]++;
		return a;
	}

	int cmp(Object a, Object b) {
		if (a instanceof Integer x && b instanceof Integer y) {
			return x - y;
		}
		if (a instanceof Integer) {
			a = List.of(a);
		}
		if (b instanceof Integer) {
			b = List.of(b);
		}
		List<?> x = (List<?>) a, y = (List<?>) b;
		for (int i = 0; i < x.size() && i < y.size(); i++) {
			int c = cmp(x.get(i), y.get(i));
			if (c != 0) {
				return c;
			}
		}
		return x.size() - y.size();
	}

	record Packet(Object v, String s) {
	}
}
