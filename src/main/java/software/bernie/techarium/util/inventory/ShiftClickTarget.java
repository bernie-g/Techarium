package software.bernie.techarium.util.inventory;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * <p>Instances of this class must <i>not</i> be reused.</p>
 *
 * @author diesieben07
 */
public abstract class ShiftClickTarget {

	/**
	 * <p>Tries to merge the stack with one of the Slots between {@code from} and {@code to} (inclusive).</p>
	 * <p>If {@code from} is greater than {@code to}, the range will be iterated in reverse ({@code to} to {@code from}).</p>
	 *
	 * @param from first slot
	 * @param to   last slot
	 * @return a ShiftClickTarget
	 */
	public static ShiftClickTarget range(int from, int to) {
		checkArgument(from >= 0 && to >= 0, "to and from must be >= 0");
		if (to == from) {
			return of(to);
		} else if (to > from) {
			return new RevRange(from, to);
		} else {
			return new Range(from, to);
		}
	}

	/**
	 * <p>Discard the shift-click, don't move the ItemStack at all.</p>
	 *
	 * @return a ShiftClickTarget
	 */
	public static ShiftClickTarget none() {
		return None.INSTANCE;
	}

	/**
	 * <p>Use the default behavior.</p>
	 *
	 * @return a ShiftClickTarget
	 */
	public static ShiftClickTarget standard() {
		return Standard.INSTANCE;
	}

	/**
	 * <p>Tries to merge the stack with the single given slot.</p>
	 *
	 * @param slot the slot
	 * @return a ShiftClickTarget
	 */
	public static ShiftClickTarget of(int slot) {
		return new One(slot);
	}

	/**
	 * <p>Tries to merge the stack with the given slots in the order provided.</p>
	 *
	 * @param slots the slots
	 * @return a ShiftClickTarget
	 */
	public static ShiftClickTarget of(int... slots) {
		int len = slots.length;
		if (len == 0) {
			return none();
		} else if (len == 1) {
			return of(slots[0]);
		}
		return new ForArray(slots);
	}

	ShiftClickTarget() {
	}

	abstract boolean hasNext();

	abstract int next();

	abstract void reset();

	boolean isStandard() {
		return false;
	}

	boolean isNone() {
		return false;
	}

	private static final class Range extends ShiftClickTarget {

		private final int from;
		private final int to;
		private int next;

		Range(int from, int to) {
			this.from = from;
			this.to = to;

			this.next = from;
		}

		@Override
		boolean hasNext() {
			return next <= to;
		}

		@Override
		int next() {
			return next++;
		}

		@Override
		void reset() {
			next = from;
		}
	}

	private static final class RevRange extends ShiftClickTarget {

		private final int from;
		private final int to;
		private int next;

		RevRange(int from, int to) {
			this.from = from;
			this.to = to;

			this.next = to;
		}

		@Override
		boolean hasNext() {
			return next >= from;
		}

		@Override
		int next() {
			return next--;
		}

		@Override
		void reset() {
			next = to;
		}

	}

	private static final class ForArray extends ShiftClickTarget {

		private final int[] slots;
		private int curr;

		ForArray(int[] slots) {
			this.slots = slots;
		}

		@Override
		boolean hasNext() {
			return curr != slots.length;
		}

		@Override
		int next() {
			return slots[curr++];
		}

		@Override
		void reset() {
			curr = 0;
		}
	}

	private static final class One extends ShiftClickTarget {

		private int slot;

		One(int slot) {
			this.slot = slot;
		}

		@Override
		boolean hasNext() {
			return (slot & (1 << 31)) == 0;
		}

		@Override
		int next() {
			int ret = slot;
			slot = ret | (1 << 31);
			return ret;
		}

		@Override
		void reset() {
			slot &= ~(1 << 31);
		}
	}

	private static final class None extends ShiftClickTarget {

		static final None INSTANCE = new None();

		@Override
		boolean hasNext() {
			return false;
		}

		@Override
		int next() {
			throw new AssertionError();
		}

		@Override
		void reset() {
		}

		@Override
		boolean isNone() {
			return true;
		}
	}

	private static final class Standard extends ShiftClickTarget {

		static final Standard INSTANCE = new Standard();

		@Override
		boolean hasNext() {
			throw new AssertionError();
		}

		@Override
		int next() {
			throw new AssertionError();
		}

		@Override
		void reset() {
			throw new AssertionError();
		}

		@Override
		boolean isStandard() {
			return true;
		}
	}

}
