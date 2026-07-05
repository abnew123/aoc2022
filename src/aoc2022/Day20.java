package aoc2022;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day20 extends DayTemplate {

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		Mover[] movers = new Mover[16];
		int count = 0;
		Mover root = null;
		while (in.hasNext()) {
			long val = Integer.parseInt(in.nextLine());
			Mover mover = new Mover(count, part1 ? val : val * 811589153);
			if (count == movers.length) {
				Mover[] grown = new Mover[movers.length * 2];
				System.arraycopy(movers, 0, grown, 0, movers.length);
				movers = grown;
			}
			movers[count++] = mover;
			root = merge(root, mover);
		}
		for (int k = 0; k < (part1 ? 1 : 10); k++) {
			for (int i = 0; i < count; i++) {
				Mover mover = movers[i];
				int loc = indexOf(mover);
				root = remove(root, loc);

				int sizeAfterRemoval = count - 1;
				int rotate = (int) (mover.val % sizeAfterRemoval);
				int nextLoc = Math.floorMod(loc + rotate, sizeAfterRemoval);
				root = insert(root, nextLoc, mover);
			}
		}
		int offset = 0;
		for (int j = 0; j < count; j++) {
			if (get(root, j).val == 0) {
				offset = j;
			}
		}
		return "" + (get(root, (offset + 1000) % count).val + get(root, (offset + 2000) % count).val
				+ get(root, (offset + 3000) % count).val);
	}

	/*
	 * Implicit treap: the in-order traversal is the mixed list. Each node stores
	 * its subtree size for indexed split/merge, plus a parent pointer so a mover's
	 * current index can be found without scanning the whole sequence.
	 */
	private static Mover insert(Mover root, int index, Mover mover) {
		Split split = split(root, index);
		return merge(merge(split.left, mover), split.right);
	}

	private static Mover remove(Mover root, int index) {
		Split beforeMover = split(root, index);
		Split afterMover = split(beforeMover.right, 1);
		afterMover.left.left = null;
		afterMover.left.right = null;
		afterMover.left.parent = null;
		afterMover.left.size = 1;
		return merge(beforeMover.left, afterMover.right);
	}

	private static Mover get(Mover root, int index) {
		while (true) {
			int leftSize = size(root.left);
			if (index < leftSize) {
				root = root.left;
			} else if (index == leftSize) {
				return root;
			} else {
				index -= leftSize + 1;
				root = root.right;
			}
		}
	}

	private static int indexOf(Mover mover) {
		int index = size(mover.left);
		while (mover.parent != null) {
			if (mover == mover.parent.right) {
				index += size(mover.parent.left) + 1;
			}
			mover = mover.parent;
		}
		return index;
	}

	private static Split split(Mover root, int leftCount) {
		if (root == null) {
			return new Split(null, null);
		}
		if (size(root.left) >= leftCount) {
			Split split = split(root.left, leftCount);
			root.left = split.right;
			setParent(root.left, root);
			refresh(root);
			setParent(split.left, null);
			setParent(root, null);
			return new Split(split.left, root);
		}
		Split split = split(root.right, leftCount - size(root.left) - 1);
		root.right = split.left;
		setParent(root.right, root);
		refresh(root);
		setParent(root, null);
		setParent(split.right, null);
		return new Split(root, split.right);
	}

	private static Mover merge(Mover left, Mover right) {
		if (left == null) {
			setParent(right, null);
			return right;
		}
		if (right == null) {
			setParent(left, null);
			return left;
		}
		if (left.priority < right.priority) {
			left.right = merge(left.right, right);
			setParent(left.right, left);
			refresh(left);
			setParent(left, null);
			return left;
		}
		right.left = merge(left, right.left);
		setParent(right.left, right);
		refresh(right);
		setParent(right, null);
		return right;
	}

	private static int size(Mover mover) {
		return mover == null ? 0 : mover.size;
	}

	private static void refresh(Mover mover) {
		mover.size = size(mover.left) + 1 + size(mover.right);
	}

	private static void setParent(Mover mover, Mover parent) {
		if (mover != null) {
			mover.parent = parent;
		}
	}

	private static int priority(int uuid) {
		// Deterministic hash priorities keep the treap balanced without randomness.
		int hash = uuid + 0x9e3779b9;
		hash ^= hash >>> 16;
		hash *= 0x85ebca6b;
		hash ^= hash >>> 13;
		hash *= 0xc2b2ae35;
		hash ^= hash >>> 16;
		return hash;
	}

	private static class Split {
		Mover left;
		Mover right;

		Split(Mover left, Mover right) {
			this.left = left;
			this.right = right;
		}
	}

	private static class Mover {
		final int priority;
		final long val;
		Mover left;
		Mover right;
		Mover parent;
		int size = 1;

		// The treap priority keeps indexed insert/remove balanced while the parent
		// pointer lets us find a mover's current sequence index without scanning.
		Mover(int uuid, long val) {
			this.priority = priority(uuid);
			this.val = val;
		}
	}
}
