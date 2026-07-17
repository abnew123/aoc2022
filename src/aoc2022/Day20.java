package aoc2022;

import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.Scanner;

public class Day20 extends DayTemplate {
	// Implicit treap stored in parallel arrays: in-order traversal is the mixed
	// list, and parent pointers keep each mover's current index cheap to find.
	private BigInteger[] values;
	private int[] left;
	private int[] right;
	private int[] parent;
	private int[] size;
	private int[] priority;

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		BigInteger[] input = parse(in);
		return mix(input, part1 ? BigInteger.ONE : BigInteger.valueOf(811589153L), part1 ? 1 : 10).toString();
	}

	@Override
	public String[] fullSolve(Scanner in) throws FileNotFoundException {
		BigInteger[] input = parse(in);
		return new String[] {
				mix(input, BigInteger.ONE, 1).toString(),
				mix(input, BigInteger.valueOf(811589153L), 10).toString()
		};
	}

	private BigInteger[] parse(Scanner in) {
		BigInteger[] input = new BigInteger[1024];
		int count = 0;
		while (in.hasNext()) {
			if (count == input.length) {
				BigInteger[] grown = new BigInteger[input.length * 2];
				System.arraycopy(input, 0, grown, 0, input.length);
				input = grown;
			}
			input[count++] = new BigInteger(in.next());
		}
		BigInteger[] exact = new BigInteger[count];
		System.arraycopy(input, 0, exact, 0, count);
		return exact;
	}

	private BigInteger mix(BigInteger[] input, BigInteger key, int rounds) {
		int count = input.length;
		if (count == 0) {
			throw new IllegalArgumentException("No values to mix");
		}
		values = new BigInteger[count];
		left = new int[count];
		right = new int[count];
		parent = new int[count];
		size = new int[count];
		priority = new int[count];
		int root = -1;
		int zero = -1;
		for (int i = 0; i < count; i++) {
			BigInteger val = input[i];
			values[i] = val.multiply(key);
			left[i] = -1;
			right[i] = -1;
			parent[i] = -1;
			size[i] = 1;
			priority[i] = priority(i);
			if (val.signum() == 0) {
				if (zero >= 0) {
					throw new IllegalArgumentException("Multiple zero values");
				}
				zero = i;
			}
			root = merge(root, i);
		}
		if (zero < 0) {
			throw new IllegalArgumentException("Missing zero value");
		}
		if (count == 1) {
			return values[zero].multiply(BigInteger.valueOf(3));
		}
		int[] rotations = new int[count];
		BigInteger modulus = BigInteger.valueOf(count - 1L);
		for (int i = 0; i < count; i++) {
			rotations[i] = values[i].remainder(modulus).intValue();
		}
		for (int k = 0; k < rounds; k++) {
			for (int i = 0; i < count; i++) {
				int loc = indexOf(i);
				root = remove(root, loc);

				int sizeAfterRemoval = count - 1;
				int nextLoc = (int) Math.floorMod((long) loc + rotations[i], sizeAfterRemoval);
				root = insert(root, nextLoc, i);
			}
		}
		int offset = indexOf(zero);
		return values[get(root, (int) (((long) offset + 1000) % count))]
				.add(values[get(root, (int) (((long) offset + 2000) % count))])
				.add(values[get(root, (int) (((long) offset + 3000) % count))]);
	}

	private int insert(int root, int index, int mover) {
		long split = split(root, index);
		return merge(merge(splitLeft(split), mover), splitRight(split));
	}

	private int remove(int root, int index) {
		long beforeMover = split(root, index);
		long afterMover = split(splitRight(beforeMover), 1);
		int mover = splitLeft(afterMover);
		left[mover] = -1;
		right[mover] = -1;
		parent[mover] = -1;
		size[mover] = 1;
		return merge(splitLeft(beforeMover), splitRight(afterMover));
	}

	private int get(int root, int index) {
		while (true) {
			int leftSize = size(left[root]);
			if (index < leftSize) {
				root = left[root];
			} else if (index == leftSize) {
				return root;
			} else {
				index -= leftSize + 1;
				root = right[root];
			}
		}
	}

	private int indexOf(int mover) {
		int index = size(left[mover]);
		while (parent[mover] != -1) {
			int moverParent = parent[mover];
			if (mover == right[moverParent]) {
				index += size(left[moverParent]) + 1;
			}
			mover = moverParent;
		}
		return index;
	}

	private long split(int root, int leftCount) {
		if (root == -1) {
			return pack(-1, -1);
		}
		if (size(left[root]) >= leftCount) {
			long split = split(left[root], leftCount);
			left[root] = splitRight(split);
			setParent(left[root], root);
			refresh(root);
			setParent(splitLeft(split), -1);
			parent[root] = -1;
			return pack(splitLeft(split), root);
		}
		long split = split(right[root], leftCount - size(left[root]) - 1);
		right[root] = splitLeft(split);
		setParent(right[root], root);
		refresh(root);
		parent[root] = -1;
		setParent(splitRight(split), -1);
		return pack(root, splitRight(split));
	}

	private int merge(int leftRoot, int rightRoot) {
		if (leftRoot == -1) {
			setParent(rightRoot, -1);
			return rightRoot;
		}
		if (rightRoot == -1) {
			setParent(leftRoot, -1);
			return leftRoot;
		}
		if (priority[leftRoot] < priority[rightRoot]) {
			right[leftRoot] = merge(right[leftRoot], rightRoot);
			setParent(right[leftRoot], leftRoot);
			refresh(leftRoot);
			parent[leftRoot] = -1;
			return leftRoot;
		}
		left[rightRoot] = merge(leftRoot, left[rightRoot]);
		setParent(left[rightRoot], rightRoot);
		refresh(rightRoot);
		parent[rightRoot] = -1;
		return rightRoot;
	}

	private int size(int mover) {
		return mover == -1 ? 0 : size[mover];
	}

	private void refresh(int mover) {
		size[mover] = size(left[mover]) + 1 + size(right[mover]);
	}

	private void setParent(int mover, int moverParent) {
		if (mover != -1) {
			parent[mover] = moverParent;
		}
	}

	private long pack(int leftRoot, int rightRoot) {
		return ((long) (leftRoot + 1) << 32) | (rightRoot + 1L);
	}

	private int splitLeft(long split) {
		return (int) (split >>> 32) - 1;
	}

	private int splitRight(long split) {
		return (int) split - 1;
	}

	private int priority(int uuid) {
		// Deterministic hash priorities keep the treap balanced without randomness.
		int hash = uuid + 0x9e3779b9;
		hash ^= hash >>> 16;
		hash *= 0x85ebca6b;
		hash ^= hash >>> 13;
		hash *= 0xc2b2ae35;
		hash ^= hash >>> 16;
		return hash;
	}
}
