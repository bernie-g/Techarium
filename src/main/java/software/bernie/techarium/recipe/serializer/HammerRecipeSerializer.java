package software.bernie.techarium.recipe.serializer;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import software.bernie.techarium.recipe.recipe.HammerRecipe;
import software.bernie.techarium.util.JsonCodecUtils;
import software.bernie.techarium.util.Utils;
import javax.annotation.Nullable;

public class HammerRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<HammerRecipe> {

    @Override
    public HammerRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        Ingredient input0 = Utils.deserializeIngredient(json, "input0");
        Ingredient input1 = Utils.deserializeIngredient(json, "input1");
        Ingredient input2 = Utils.deserializeIngredient(json, "input2");
        ItemStack output = JsonCodecUtils.deserializeItemStack(json.get("output"));

        return new HammerRecipe(recipeId, input0, input1, input2, output);
    }

    @Nullable
    @Override
    public HammerRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
        Ingredient input0 = Ingredient.fromNetwork(buffer);
        Ingredient input1 = Ingredient.fromNetwork(buffer);
        Ingredient input2 = Ingredient.fromNetwork(buffer);
        ItemStack output = buffer.readItem();
        return new HammerRecipe(recipeId, input0, input1, input2, output);}

    @Override
    public void toNetwork(PacketBuffer buffer, HammerRecipe recipe) {
        recipe.getInput0().toNetwork(buffer);
        recipe.getInput1().toNetwork(buffer);
        recipe.getInput2().toNetwork(buffer);
        buffer.writeItem(recipe.getOutput());
    }
}
