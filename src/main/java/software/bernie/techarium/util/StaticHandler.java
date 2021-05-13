package software.bernie.techarium.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;
import software.bernie.techarium.machine.sideness.Side;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StaticHandler {

    public static Side getSideFromDirection(@Nullable Direction face,Direction rotated){
        if (face == null) {
            return null;
        } else if (face == Direction.UP) {
            return Side.UP;
        } else if (face == Direction.DOWN) {
            return Side.DOWN;
        } else if (rotated == face) {
            return Side.FRONT;
        } else if (rotated == face.getOpposite()) {
            return Side.BACK;
        } else if (rotated == face.rotateYCCW()) {
            return Side.LEFT;
        } else {
            return rotated == face.rotateY() ? Side.RIGHT : Side.DOWN;
        }
    }



    //Inventory Item Stream.
    public static Stream<ItemStack> getItemStackStream(IItemHandler inventory) {
        //Get slot indexes
        return IntStream.range(0, inventory.getSlots())
                //Map to each slot
                .mapToObj(inventory::getStackInSlot);
    }

    //Spawn ItemStacks
    public static void spawnItemStack(World worldIn, double x, double y, double z, ItemStack stack, Random rand) {
        double d0 = (double) EntityType.ITEM.getWidth();
        double d1 = 1.0D - d0;
        double d2 = d0 / 2.0D;
        double d3 = Math.floor(x) + rand.nextDouble() * d1 + d2;
        double d4 = Math.floor(y) + rand.nextDouble() * d1;
        double d5 = Math.floor(z) + rand.nextDouble() * d1 + d2;

        while (!stack.isEmpty()) {
            ItemEntity itementity = new ItemEntity(worldIn, d3, d4, d5, stack.split(rand.nextInt(21) + 10));
            float f = 0.05F;
            itementity.setMotion(rand.nextGaussian() * (double) 0.05F, rand.nextGaussian() * (double) 0.05F + (double) 0.2F, rand.nextGaussian() * (double) 0.05F);
            worldIn.addEntity(itementity);
        }
    }

}
