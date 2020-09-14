package software.bernie.techarium.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.Direction;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fluids.FluidStack;
import software.bernie.techarium.machine.sideness.Side;

import javax.annotation.Nullable;

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



    //Serialize//Deserialize

    public static FluidStack deserializeFluid(JsonObject object) {
        String s = JSONUtils.getString(object, "fluidIn");
        Fluid fluid = Registry.FLUID.getValue(new ResourceLocation(s)).orElseThrow(() -> {
            return new JsonSyntaxException("Unknown fluid '" + s + "'");
        });
        int i = JSONUtils.getInt(object, "fluidAmount", 100);
        return new FluidStack(fluid, i);
    }

}
