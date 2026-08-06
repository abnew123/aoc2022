package aoc2022;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Grove Positioning System, as a blocked order-statistic sequence over flat
 * primitive arrays (a tiered vector). The mixed order lives in one int[] of
 * fixed-stride blocks plus a per-id physical-block index; block order and
 * block sizes live in two small parallel int[]s that are walked sequentially.
 * Within-block offsets are never maintained eagerly - they are recovered by a
 * short contiguous scan - so every remove/insert is a pure System.arraycopy
 * shift with O(1) bookkeeping writes instead of per-element index rewrites or
 * pointer-chasing block walks. All arithmetic is primitive long; rotations are
 * reduced mod (n - 1) with modular multiplication so no product can overflow.
 */
public class Day20 extends DayTemplate {
	private static final long DECRYPTION_KEY = 811589153L;
	private static final int BLOCK_SHIFT = 7;
	private static final int BLOCK_CAPACITY = 1 << BLOCK_SHIFT;
	private static final int INITIAL_BLOCK_SIZE = BLOCK_CAPACITY / 2;
	private static final int MERGE_THRESHOLD = 32;

	public String solve(boolean part1, Scanner in) throws FileNotFoundException {
		long[] input = parse(in);
		return Long.toString(
				mix(input, part1 ? 1L : DECRYPTION_KEY, part1 ? 1 : 10));
	}

	@Override
	public String[] fullSolve(Scanner in) throws FileNotFoundException {
		long[] input = parse(in);
		return new String[] {
				Long.toString(mix(input, 1L, 1)),
				Long.toString(mix(input, DECRYPTION_KEY, 10))
		};
	}

	/** Slurps the stream once and parses signed decimals with a digit scan. */
	private long[] parse(Scanner in) {
		String text = in.useDelimiter("\\A").hasNext() ? in.next() : "";
		int length = text.length();
		long[] values = new long[1024];
		int count = 0;
		int at = 0;
		while (at < length) {
			char c = text.charAt(at);
			if (c != '-' && (c < '0' || c > '9')) {
				at++;
				continue;
			}
			boolean negative = c == '-';
			if (negative) {
				at++;
			}
			long value = 0;
			while (at < length && (c = text.charAt(at)) >= '0' && c <= '9') {
				value = value * 10 + (c - '0');
				at++;
			}
			if (count == values.length) {
				values = Arrays.copyOf(values, count * 2);
			}
			values[count++] = negative ? -value : value;
		}
		return Arrays.copyOf(values, count);
	}

	private long mix(long[] input, long key, int rounds) {
		int count = input.length;
		if (count == 0) {
			throw new IllegalArgumentException("No values to mix");
		}
		long[] values = new long[count];
		int zero = -1;
		for (int i = 0; i < count; i++) {
			values[i] = input[i] * key;
			if (input[i] == 0) {
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
			return values[zero] * 3;
		}

		int modulus = count - 1;
		long keyRemainder = key % modulus;
		int[] rotations = new int[count];
		for (int i = 0; i < count; i++) {
			rotations[i] = (int) (input[i] % modulus * keyRemainder % modulus);
		}

		BlockedSequence sequence = new BlockedSequence(count);
		for (int round = 0; round < rounds; round++) {
			for (int mover = 0; mover < count; mover++) {
				int index = sequence.removeAndIndex(mover);
				int destination = (int) Math.floorMod(
						(long) index + rotations[mover], modulus);
				sequence.insert(destination, mover);
			}
		}
		int zeroIndex = sequence.indexOf(zero);
		return values[sequence.get((zeroIndex + 1000) % count)]
				+ values[sequence.get((zeroIndex + 2000) % count)]
				+ values[sequence.get((zeroIndex + 3000) % count)];
	}

	/**
	 * Sequence of the ids 0..n-1 supporting indexed removal and insertion.
	 * Physical block b stores its ids at data[b << BLOCK_SHIFT ...]; block
	 * order and per-block sizes are the parallel arrays logicalPhys and
	 * logicalSize, so rank walks are sequential passes over a few hundred
	 * bytes of L1-resident ints rather than linked-node traversals.
	 */
	private static final class BlockedSequence {
		private final int[] blockOf;
		private int[] data;
		private int[] logicalPhys;
		private int[] logicalSize;
		private int[] freeBlocks;
		private int freeCount;
		private int physicalCount;
		private int blockCount;

		BlockedSequence(int size) {
			blockOf = new int[size];
			int initial = (size + INITIAL_BLOCK_SIZE - 1) / INITIAL_BLOCK_SIZE;
			int capacity = initial + 8;
			data = new int[capacity << BLOCK_SHIFT];
			logicalPhys = new int[capacity];
			logicalSize = new int[capacity];
			freeBlocks = new int[capacity];
			physicalCount = initial;
			blockCount = initial;
			for (int block = 0; block < initial; block++) {
				int from = block * INITIAL_BLOCK_SIZE;
				int to = Math.min(size, from + INITIAL_BLOCK_SIZE);
				int base = block << BLOCK_SHIFT;
				for (int id = from; id < to; id++) {
					data[base + id - from] = id;
					blockOf[id] = block;
				}
				logicalPhys[block] = block;
				logicalSize[block] = to - from;
			}
		}

		/** Removes id and returns the index it occupied before removal. */
		int removeAndIndex(int id) {
			int block = blockOf[id];
			int logical = 0;
			int index = 0;
			while (logicalPhys[logical] != block) {
				index += logicalSize[logical];
				logical++;
			}
			int base = block << BLOCK_SHIFT;
			int offset = 0;
			while (data[base + offset] != id) {
				offset++;
			}
			int sizeAfter = logicalSize[logical] - 1;
			System.arraycopy(data, base + offset + 1, data, base + offset,
					sizeAfter - offset);
			logicalSize[logical] = sizeAfter;
			if (sizeAfter == 0) {
				releaseBlock(block);
				removeLogical(logical);
			} else if (sizeAfter < MERGE_THRESHOLD) {
				mergeSmall(logical);
			}
			return index + offset;
		}

		void insert(int index, int id) {
			int logical = 0;
			while (index >= logicalSize[logical]) {
				index -= logicalSize[logical];
				logical++;
			}
			int size = logicalSize[logical];
			if (size == BLOCK_CAPACITY) {
				split(logical);
				size = logicalSize[logical];
				if (index > size) {
					index -= size;
					logical++;
					size = logicalSize[logical];
				}
			}
			int block = logicalPhys[logical];
			int base = block << BLOCK_SHIFT;
			System.arraycopy(data, base + index, data, base + index + 1,
					size - index);
			data[base + index] = id;
			blockOf[id] = block;
			logicalSize[logical] = size + 1;
		}

		int indexOf(int id) {
			int block = blockOf[id];
			int logical = 0;
			int index = 0;
			while (logicalPhys[logical] != block) {
				index += logicalSize[logical];
				logical++;
			}
			int base = block << BLOCK_SHIFT;
			int offset = 0;
			while (data[base + offset] != id) {
				offset++;
			}
			return index + offset;
		}

		int get(int index) {
			int logical = 0;
			while (index >= logicalSize[logical]) {
				index -= logicalSize[logical];
				logical++;
			}
			return data[(logicalPhys[logical] << BLOCK_SHIFT) + index];
		}

		private void split(int logical) {
			int left = logicalPhys[logical];
			int right = acquireBlock();
			int leftSize = BLOCK_CAPACITY / 2;
			int rightSize = logicalSize[logical] - leftSize;
			int rightBase = right << BLOCK_SHIFT;
			System.arraycopy(data, (left << BLOCK_SHIFT) + leftSize, data,
					rightBase, rightSize);
			for (int i = 0; i < rightSize; i++) {
				blockOf[data[rightBase + i]] = right;
			}
			logicalSize[logical] = leftSize;
			insertLogical(logical + 1, right, rightSize);
		}

		private void mergeSmall(int logical) {
			int size = logicalSize[logical];
			if (logical + 1 < blockCount
					&& size + logicalSize[logical + 1] <= BLOCK_CAPACITY) {
				merge(logical);
			} else if (logical > 0
					&& logicalSize[logical - 1] + size <= BLOCK_CAPACITY) {
				merge(logical - 1);
			}
		}

		/** Merges logical block {@code logical + 1} into {@code logical}. */
		private void merge(int logical) {
			int left = logicalPhys[logical];
			int right = logicalPhys[logical + 1];
			int leftSize = logicalSize[logical];
			int rightSize = logicalSize[logical + 1];
			int destination = (left << BLOCK_SHIFT) + leftSize;
			System.arraycopy(data, right << BLOCK_SHIFT, data, destination,
					rightSize);
			for (int i = 0; i < rightSize; i++) {
				blockOf[data[destination + i]] = left;
			}
			logicalSize[logical] = leftSize + rightSize;
			releaseBlock(right);
			removeLogical(logical + 1);
		}

		private void insertLogical(int logical, int block, int size) {
			if (blockCount == logicalPhys.length) {
				logicalPhys = Arrays.copyOf(logicalPhys, blockCount * 2);
				logicalSize = Arrays.copyOf(logicalSize, blockCount * 2);
			}
			System.arraycopy(logicalPhys, logical, logicalPhys, logical + 1,
					blockCount - logical);
			System.arraycopy(logicalSize, logical, logicalSize, logical + 1,
					blockCount - logical);
			logicalPhys[logical] = block;
			logicalSize[logical] = size;
			blockCount++;
		}

		private void removeLogical(int logical) {
			blockCount--;
			System.arraycopy(logicalPhys, logical + 1, logicalPhys, logical,
					blockCount - logical);
			System.arraycopy(logicalSize, logical + 1, logicalSize, logical,
					blockCount - logical);
		}

		private int acquireBlock() {
			if (freeCount > 0) {
				return freeBlocks[--freeCount];
			}
			if ((physicalCount << BLOCK_SHIFT) == data.length) {
				data = Arrays.copyOf(data, data.length * 2);
			}
			return physicalCount++;
		}

		private void releaseBlock(int block) {
			if (freeCount == freeBlocks.length) {
				freeBlocks = Arrays.copyOf(freeBlocks, freeCount * 2);
			}
			freeBlocks[freeCount++] = block;
		}
	}
}
