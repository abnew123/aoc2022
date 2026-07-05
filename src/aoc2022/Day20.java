package aoc2022;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day20 extends DayTemplate {
	// Implicit treap stored in parallel arrays: in-order traversal is the mixed
	// list, and parent pointers keep each mover's current index cheap to find.
	private long[] values;
	private int[] left;
	private int[] right;
	private int[] parent;
	private int[] size;
	private int[] priority;

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		values = new long[16];
		left = new int[16];
		right = new int[16];
		parent = new int[16];
		size = new int[16];
		priority = new int[16];
		int count = 0;
		int root = -1;
		int zero = -1;
		while (in.hasNext()) {
			long val = Long.parseLong(in.nextLine());
			if (count == values.length) {
				grow();
			}
			values[count] = part1 ? val : val * 811589153L;
			left[count] = -1;
			right[count] = -1;
			parent[count] = -1;
			size[count] = 1;
			priority[count] = priority(count);
			if (val == 0) {
				zero = count;
			}
			root = merge(root, count);
			count++;
		}
		for (int k = 0; k < (part1 ? 1 : 10); k++) {
			for (int i = 0; i < count; i++) {
				int loc = indexOf(i);
				root = remove(root, loc);

				int sizeAfterRemoval = count - 1;
				int rotate = (int) (values[i] % sizeAfterRemoval);
				int nextLoc = Math.floorMod(loc + rotate, sizeAfterRemoval);
				root = insert(root, nextLoc, i);
			}
		}
		int offset = indexOf(zero);
		return "" + (values[get(root, (offset + 1000) % count)] + values[get(root, (offset + 2000) % count)]
				+ values[get(root, (offset + 3000) % count)]);
	}

	private void grow() {
		int nextCapacity = values.length * 2;
		long[] grownValues = new long[nextCapacity];
		int[] grownLeft = new int[nextCapacity];
		int[] grownRight = new int[nextCapacity];
		int[] grownParent = new int[nextCapacity];
		int[] grownSize = new int[nextCapacity];
		int[] grownPriority = new int[nextCapacity];
		System.arraycopy(values, 0, grownValues, 0, values.length);
		System.arraycopy(left, 0, grownLeft, 0, left.length);
		System.arraycopy(right, 0, grownRight, 0, right.length);
		System.arraycopy(parent, 0, grownParent, 0, parent.length);
		System.arraycopy(size, 0, grownSize, 0, size.length);
		System.arraycopy(priority, 0, grownPriority, 0, priority.length);
		values = grownValues;
		left = grownLeft;
		right = grownRight;
		parent = grownParent;
		size = grownSize;
		priority = grownPriority;
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
