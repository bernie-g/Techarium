package software.bernie.techarium.recipes.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mekanism.api.datagen.recipe.RecipeCriterion;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class TechariumRecipeBuilder<B extends TechariumRecipeBuilder<B>> implements IRecipe<IInventory> {
    protected final List<ICondition> conditions = new ArrayList();
    protected final Advancement.Builder advancementBuilder = Advancement.Builder.advancement();

    protected void validate(ResourceLocation id) {
    }

    public void build(Consumer<IFinishedRecipe> consumer, ResourceLocation id) {
        this.validate(id);
        if (this.hasCriteria()) {
            this.advancementBuilder.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe",
                    RecipeUnlockedTrigger.unlocked(id)).rewards(
                    net.minecraft.advancements.AdvancementRewards.Builder.recipe(id)).requirements(
                    IRequirementsStrategy.OR);
        }

        consumer.accept(this.getResult(id));
    }

    public B addCriterion(RecipeCriterion criterion) {
        return this.addCriterion(criterion.name, criterion.criterion);
    }

    public B addCriterion(String name, ICriterionInstance criterion) {
        this.advancementBuilder.addCriterion(name, criterion);
        return (B) this;
    }

    public B addCondition(ICondition condition) {
        this.conditions.add(condition);
        return (B) this;
    }

    protected boolean hasCriteria() {
        return !this.advancementBuilder.getCriteria().isEmpty();
    }

    protected abstract TechariumRecipeBuilder.Result getResult(ResourceLocation id);

    public abstract class Result implements IFinishedRecipe {

        private final ResourceLocation id;

        public Result(ResourceLocation id) {
            this.id = id;
        }

        public JsonObject serializeRecipe() {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("type", getType().getRegistryName().toString());
            if (!TechariumRecipeBuilder.this.conditions.isEmpty()) {
                JsonArray conditionsArray = new JsonArray();

                for (ICondition condition : TechariumRecipeBuilder.this.conditions) {
                    conditionsArray.add(CraftingHelper.serialize(condition));
                }

                jsonObject.add("conditions", conditionsArray);
            }

            this.serializeRecipeData(jsonObject);
            return jsonObject;
        }

        @Override
        public IRecipeSerializer<?> getType() {
            return TechariumRecipeBuilder.this.getSerializer();
        }

        @Nonnull
        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public JsonObject serializeAdvancement() {
            return TechariumRecipeBuilder.this.hasCriteria() ? TechariumRecipeBuilder.this.advancementBuilder.serializeToJson() : null;
        }

        @Override
        public ResourceLocation getAdvancementId() {
            return new ResourceLocation(this.id.getNamespace(), "recipes/" + this.id.getPath());
        }
    }
}
