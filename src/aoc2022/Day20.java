package aoc2022;

import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.Scanner;

public class Day20 extends DayTemplate {
	private static final int BLOCK_CAPACITY = 128;
	private static final int INITIAL_BLOCK_SIZE = 64;
	private static final int MERGE_THRESHOLD = 32;

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		BigInteger[] input = parse(in);
		return mix(input, part1 ? BigInteger.ONE : BigInteger.valueOf(811589153L),
				part1 ? 1 : 10).toString();
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
		BigInteger[] values = new BigInteger[count];
		int zero = -1;
		for (int i = 0; i < count; i++) {
			values[i] = input[i].multiply(key);
			if (input[i].signum() == 0) {
				if (zero >= 0) {
					throw new IllegalArgumentException("Multiple zero values");
				}
				zero = i;
			}
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

		BlockSequence sequence = new BlockSequence(count);
		for (int round = 0; round < rounds; round++) {
			for (int mover = 0; mover < count; mover++) {
				int index = sequence.indexOf(mover);
				sequence.remove(mover);
				int destination = (int) Math.floorMod(
						(long) index + rotations[mover], count - 1);
				sequence.insert(destination, mover);
			}
		}
		int zeroIndex = sequence.indexOf(zero);
		return values[sequence.get((zeroIndex + 1000) % count)]
				.add(values[sequence.get((zeroIndex + 2000) % count)])
				.add(values[sequence.get((zeroIndex + 3000) % count)]);
	}

	private static final class BlockSequence {
		private final Block[] location;
		private final int[] slot;
		private Block head;
		private Block tail;
		private int count;

		BlockSequence(int size) {
			location = new Block[size];
			slot = new int[size];
			for (int id = 0; id < size; id++) {
				append(id);
			}
		}

		int indexOf(int id) {
			Block target = location[id];
			int index = slot[id];
			for (Block block = head; block != target; block = block.next) {
				index += block.size;
			}
			return index;
		}

		int get(int index) {
			Block block = head;
			while (index >= block.size) {
				index -= block.size;
				block = block.next;
			}
			return block.ids[index];
		}

		void remove(int id) {
			Block block = location[id];
			int index = slot[id];
			int moved = block.size - index - 1;
			if (moved > 0) {
				System.arraycopy(block.ids, index + 1, block.ids, index, moved);
				for (int i = index; i < block.size - 1; i++) {
					slot[block.ids[i]] = i;
				}
			}
			block.size--;
			count--;
			location[id] = null;
			if (block.size == 0) {
				unlink(block);
			} else if (block.size < MERGE_THRESHOLD) {
				mergeSmall(block);
			}
		}

		void insert(int index, int id) {
			if (count == 0) {
				Block block = new Block();
				head = block;
				tail = block;
				block.ids[0] = id;
				block.size = 1;
				location[id] = block;
				slot[id] = 0;
				count = 1;
				return;
			}
			Block block = head;
			while (index >= block.size) {
				index -= block.size;
				block = block.next;
			}
			if (block.size == BLOCK_CAPACITY) {
				Block right = split(block);
				if (index > block.size) {
					index -= block.size;
					block = right;
				}
			}
			System.arraycopy(block.ids, index, block.ids, index + 1,
					block.size - index);
			for (int i = index + 1; i <= block.size; i++) {
				slot[block.ids[i]] = i;
			}
			block.ids[index] = id;
			block.size++;
			location[id] = block;
			slot[id] = index;
			count++;
		}

		private void append(int id) {
			if (tail == null || tail.size == INITIAL_BLOCK_SIZE) {
				Block block = new Block();
				if (tail == null) {
					head = block;
				} else {
					tail.next = block;
					block.previous = tail;
				}
				tail = block;
			}
			tail.ids[tail.size] = id;
			location[id] = tail;
			slot[id] = tail.size;
			tail.size++;
			count++;
		}

		private Block split(Block block) {
			Block right = new Block();
			int leftSize = BLOCK_CAPACITY / 2;
			int rightSize = block.size - leftSize;
			System.arraycopy(block.ids, leftSize, right.ids, 0, rightSize);
			block.size = leftSize;
			right.size = rightSize;
			for (int i = 0; i < rightSize; i++) {
				location[right.ids[i]] = right;
				slot[right.ids[i]] = i;
			}
			right.next = block.next;
			right.previous = block;
			if (block.next == null) {
				tail = right;
			} else {
				block.next.previous = right;
			}
			block.next = right;
			return right;
		}

		private void mergeSmall(Block block) {
			if (block.next != null
					&& block.size + block.next.size <= BLOCK_CAPACITY) {
				merge(block, block.next);
			} else if (block.previous != null
					&& block.previous.size + block.size <= BLOCK_CAPACITY) {
				merge(block.previous, block);
			}
		}

		private void merge(Block left, Block right) {
			int offset = left.size;
			System.arraycopy(right.ids, 0, left.ids, offset, right.size);
			for (int i = 0; i < right.size; i++) {
				int id = right.ids[i];
				location[id] = left;
				slot[id] = offset + i;
			}
			left.size += right.size;
			unlink(right);
		}

		private void unlink(Block block) {
			if (block.previous == null) {
				head = block.next;
			} else {
				block.previous.next = block.next;
			}
			if (block.next == null) {
				tail = block.previous;
			} else {
				block.next.previous = block.previous;
			}
		}
	}

	private static final class Block {
		final int[] ids = new int[BLOCK_CAPACITY];
		Block previous;
		Block next;
		int size;
	}
}
