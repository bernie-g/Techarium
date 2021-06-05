package software.bernie.techarium.util.inventory;

import com.google.common.base.Objects;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * <p>Utilities for ItemStacks.</p>
 *
 * @see ItemStack
 */
@ParametersAreNonnullByDefault
public final class ItemStacks {

    /**
     * <p>Clone the given ItemStack, or return null if it is null.</p>
     *
     * @param stack the stack to clone
     * @return a copy of the ItemStack
     */
    public static ItemStack clone(@Nullable ItemStack stack) {
        return stack == null ? null : stack.copy();
    }

    /**
     * <p>Determine if the given ItemStacks are equal.</p>
     * <p>This method checks the Item, damage value and NBT data of the stack, it does not check stack sizes.</p>
     *
     * @param a an ItemStack
     * @param b an ItemStack
     * @return true if the ItemStacks are equal
     */
    public static boolean equal(@Nullable ItemStack a, @Nullable ItemStack b) {
        return a == b || (a != null && b != null && equalsImpl(a, b));
    }

    private static boolean equalsImpl(ItemStack a, ItemStack b) {
        return a.getItem() == b.getItem() && a.getDamageValue() == b.getDamageValue()
                && Objects.equal(a.getTag(), b.getTag());
    }

    /**
     * <p>Determine if the given ItemStacks are identical.</p>
     * <p>This method checks the Item, damage value, NBT data and stack size of the stacks.</p>
     *
     * @param a an ItemStack
     * @param b an ItemStack
     * @return true if the ItemStacks are identical
     */
    public static boolean identical(@Nullable ItemStack a, @Nullable ItemStack b) {
        return a == b || (a != null && b != null && equalsImpl(a, b) && a.getCount() == b.getCount());
    }


    public static final class InvalidStackDefinition extends Exception {

        InvalidStackDefinition(String message) {
            super(message);
        }

        InvalidStackDefinition(String message, Throwable cause) {
            super(message, cause);
        }

    }

    private static final Pattern definitionRegex = Pattern.compile(
            "^(?:([0-9]+)x)?(?:(?:\"([^\"]+)\")|([^@]+))(?:@([0-9]+))?$");

    private static final Pattern matcherRegex = Pattern.compile(
            "(?:(?:@(?:([^\\|\"]+)|(?:\"([^\"]+)\")))|(?:(?:\"([^\"]+)\")|([^@|]+))(?:@([0-9]+))?)(\\|)?");

    private static Predicate<ItemStack> combine(List<Predicate<ItemStack>> list) {
        //noinspection unchecked
        Predicate<ItemStack>[] arr = list.toArray(new Predicate[list.size()]);
        return (stack) -> {
            for (Predicate<ItemStack> predicate : arr) {
                if (predicate.test(stack)) {
                    return true;
                }
            }
            return false;
        };
    }

    private static int tryParseStackSize(@Nullable String s) throws InvalidStackDefinition {
        if (s == null) {
            return 1;
        } else {
            try {
                int stackSize = Integer.parseInt(s);
                if (stackSize < 0 || stackSize > 64) {
                    throw new InvalidStackDefinition(String.format("Stack size %s out of bounds", s));
                }
                return stackSize;
            } catch (NumberFormatException e) {
                throw new InvalidStackDefinition(String.format("Invalid stack size %s", s), e);
            }
        }
    }


    private static int tryParseMetadata(@Nullable String s, int def) throws InvalidStackDefinition {
        if (s == null) {
            return def;
        } else {
            try {
                int meta = Integer.parseInt(s);
                if (meta < Short.MIN_VALUE || meta > Short.MAX_VALUE) {
                    throw new InvalidStackDefinition(String.format("Metadata %s out of bounds", s));
                }
                return meta;
            } catch (NumberFormatException e) {
                throw new InvalidStackDefinition(String.format("Invalid metadata %s", s), e);
            }
        }
    }

    /**
     * <p>Tests if the first ItemStack can be fully merged into the second one.</p>
     *
     * @param from the ItemStack to merge, may be null
     * @param into the ItemStack to merge into, may be null
     * @return true if the first ItemStack can be fully merged into the second one
     */
    public static boolean fitsInto(@Nullable ItemStack from, @Nullable ItemStack into) {
        return from == null || into == null || fitsIntoImpl(from, into);
    }

    private static boolean fitsIntoImpl(ItemStack from, ItemStack into) {
        return equalsImpl(from, into) && from.getCount() + into.getCount() <= into.getMaxStackSize();
    }


    /**
     * <p>Tries to merge the two ItemStacks if they are {@linkplain #equal(ItemStack, ItemStack) equal}. See {@link #merge(ItemStack, ItemStack, boolean)} for details.</p>
     *
     * @param from the the ItemStack to transfer from
     * @param into the ItemStack to transfer into
     * @return the resulting ItemStack
     */
    public static ItemStack merge(@Nullable ItemStack from, @Nullable ItemStack into) {
        return merge(from, into, false);
    }

    /**
     * <p>Tries to merge the two ItemStacks.</p>
     * <p><ul>
     * <li>If {@code from} is null, returns {@code into}.</li>
     * <li>If {@code into} is null, sets {@code from}'s stackSize to 0 and returns a copy of the original {@code from}.</li>
     * <li>If neither {@code from} nor {@code into} are null and {@code force} is true or {@link #equal(ItemStack, ItemStack)}
     * returns true for {@code from} and {@code into} determines the number of items to transfer by {@code min(into.maxStackSize - into.stackSize, from.stackSize}.
     * Then increases {@code into.stackSize} by the number of items to transfer and decreases {@code from.stackSize} by the number of items to transfer. Then returns
     * {@code into}.</li>
     * <li>Otherwise does nothing and returns {@code into}.</li>
     * </ul></p>
     *
     * @param from  the ItemStack to transfer from
     * @param into  the ItemStack to transfer into
     * @param force whether to force the transfer even if {@link #equal(ItemStack, ItemStack)} returns false for the two ItemStacks
     * @return the resulting ItemStack
     */
    public static ItemStack merge(@Nullable ItemStack from, @Nullable ItemStack into, boolean force) {
        if (from == null) {
            return into;
        }

        if (into == null) {
            ItemStack result = from.copy();
            from.setCount(0);
            return result;
        }

        if (force || equalsImpl(from, into)) {
            int transferCount = Math.min(into.getMaxStackSize() - into.getCount(), from.getCount());
            from.setCount(from.getCount() - transferCount);
            into.setCount(into.getCount() + transferCount);
        }
        return into;
    }

    /**
     * <p>Utility method to get the Block associated with the Item in the given ItemStack.</p>
     *
     * @param stack the ItemStack
     * @return the Block associated with the ItemStack's Item or null if the Item has no associated Block
     */
    public static Block getBlock(ItemStack stack) {
        return Block.byItem(stack.getItem());
    }


    /**
     * <p>Decrease the size of the given stack by 1.</p>
     *
     * @param stack the stack
     * @return the decreased stack or null if the stack is now empty
     */
    @Nullable
    public static ItemStack decreaseSize(ItemStack stack) {
        return decreaseSize(stack, 1);
    }

    /**
     * <p>Decrease the size of the given stack by a maximum of {@code n}.</p>
     *
     * @param stack the stack
     * @param n     amount to decrease by
     * @return the decreased stack or null if the stack is now empty
     */
    public static ItemStack decreaseSize(ItemStack stack, int n) {
        int size = stack.getCount();
        if (n >= size) {
            return null;
        } else if (n > 0) {
            stack.setCount(size - n);
            return stack;
        } else {
            throw new IllegalArgumentException("Cannot decrease by " + n);
        }
    }

    /**
     * <p>Get the NBTTagCompound associated with the given ItemStack and initializes it if necessary.</p>
     *
     * @param stack the ItemStack
     * @return the NBTTagCompound associated with the ItemStack
     */
    public static CompoundNBT getNbt(ItemStack stack) {
        if (stack.getTag() == null) {
            stack.setTag(new CompoundNBT());
        }
        return stack.getTag();
    }


    /**
     * <p>Check if the given ItemStack contains the given Item.</p>
     *
     * @param stack the ItemStack or null
     * @param item  the Item
     * @return true if the ItemStack is not null and contains the given Item
     */
    public static boolean is(@Nullable ItemStack stack, Item item) {
        return stack != null && stack.getItem() == item;
    }

    /**
     * <p>Check if the given ItemStack contains the given Block.</p>
     *
     * @param stack the ItemStack
     * @param block the Block
     * @return true if the ItemStack is not null and contains the given Block
     */
    public static boolean is(@Nullable ItemStack stack, Block block) {
        return stack != null && getBlock(stack) == block;
    }

    private ItemStacks() {
    }
}
