package aoc2022;

class $ {
	static int i(String s) { return new Integer(s); }
	static long l(String s) { return new Long(s); }
	static int a(int x) { return x < 0 ? -x : x; }
	static int x(int a, int b) { return a > b ? a : b; }
	static int n(int a, int b) { return a < b ? a : b; }
	static int f(int a, int b) { return Math.floorMod(a, b); }
	static int f(long a, int b) { return Math.floorMod(a, b); }
	static java.util.HashSet s() { return new java.util.HashSet(); }
	static java.util.HashMap h() { return new java.util.HashMap(); }
	static java.util.ArrayList a() { return new java.util.ArrayList(); }
	static java.util.ArrayDeque d() { return new java.util.ArrayDeque(); }
	static void o(int[] a, int b) { java.util.Arrays.fill(a, b); }
	static void o(long[] a) { java.util.Arrays.sort(a); }
	static void o(int[] a) { java.util.Arrays.sort(a); }
	static int z(long x) { return Long.numberOfTrailingZeros(x); }
	static int b(int x) { return Integer.bitCount(x); }
	static int g(int x) { return Integer.signum(x); }
	static StringBuffer r(String s) { return new StringBuffer(s).reverse(); }
	static java.util.Scanner c(String s) { return new java.util.Scanner(s).useDelimiter("\\D+"); }
}
