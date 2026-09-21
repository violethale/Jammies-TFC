package io.github.violethale.jammies.data.recipe;

import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import io.github.violethale.jammies.common.recipes.JamJarUnsealingRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class JamJarUnsealingRecipeBuilder implements RecipeBuilder {
    private final Ingredient jar;
    private final ItemStackProvider result;
    private final boolean alwaysReturnLid;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    private JamJarUnsealingRecipeBuilder(Ingredient jar, ItemStackProvider result, boolean alwaysReturnLid) {
        this.jar = jar;
        this.result = result;
        this.alwaysReturnLid = alwaysReturnLid;
    }

    public static JamJarUnsealingRecipeBuilder unSealing(Ingredient jar, ItemStackProvider result) {
        return new JamJarUnsealingRecipeBuilder(jar, result, false);
    }

    public static JamJarUnsealingRecipeBuilder unSealing(Ingredient jar, ItemStackProvider result, boolean alwaysReturnLid) {
        return new JamJarUnsealingRecipeBuilder(jar, result, alwaysReturnLid);
    }

    public JamJarUnsealingRecipeBuilder alwaysReturnLid() {
        return new JamJarUnsealingRecipeBuilder(this.jar, this.result, true);
    }

    @Override
    public RecipeBuilder unlockedBy(String criterionName, Criterion<?> criterion) {
        this.criteria.put(criterionName, criterion);
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String s) {
        return this;
    }

    @Override
    public Item getResult() {
        return result.stack().getItem();
    }

    protected Recipe<?> recipe() {
        return new JamJarUnsealingRecipe(jar, result, alwaysReturnLid);
    }

    @Override
    public void save(RecipeOutput output, ResourceLocation recipeId) {
        ensureValid(recipeId);

        Advancement.Builder advancement = output.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
                .rewards(AdvancementRewards.Builder.recipe(recipeId))
                .requirements(AdvancementRequirements.Strategy.OR);

        this.criteria.forEach(advancement::addCriterion);

        output.accept(recipeId, recipe(), advancement.build(recipeId.withPrefix("recipes/")));
    }

    protected void ensureValid(ResourceLocation recipeId) {
        if (criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + recipeId);
        }
    }
}
