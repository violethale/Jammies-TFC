package io.github.violethale.jammies.data;

import com.eerussianguy.firmalife.FirmaLife;
import com.eerussianguy.firmalife.common.FLHelpers;
import com.eerussianguy.firmalife.common.items.FLFood;
import com.eerussianguy.firmalife.common.items.FLItems;
import mod.traister101.datagenutils.data.recipe.tfc.AdvancedCraftingRecipeBuilder;
import net.dries007.tfc.common.component.food.FoodTraits;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.common.recipes.ingredients.AndIngredient;
import net.dries007.tfc.common.recipes.ingredients.NotRottenIngredient;
import net.dries007.tfc.common.recipes.outputs.AddTraitModifier;
import net.dries007.tfc.common.recipes.outputs.CopyFoodModifier;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.dries007.tfc.util.Helpers;
import net.kuba807.kubastfca.common.item.KubastfcaItems;
import net.kuba807.kubastfca.kubastfca;
import io.github.violethale.jammies.Jammies;
import io.github.violethale.jammies.common.recipes.data.CopyExactDateModifier;
import io.github.violethale.jammies.common.recipes.data.CopyLidDataModifier;
import io.github.violethale.jammies.data.recipe.JamJarSealingRecipeBuilder;
import io.github.violethale.jammies.data.recipe.JamJarUnsealingRecipeBuilder;
import io.github.violethale.jammies.registry.JammiesTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.concurrent.CompletableFuture;

public class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider implements IConditionBuilder {

    public RecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        // TFC Recipes
        TFCItems.UNSEALED_FRUIT_PRESERVES.forEach((food, item) ->
            sealingRecipe(item, TFCItems.FRUIT_PRESERVES.get(food), Jammies.identifier("crafting/jar/" + food.getSerializedName() + "_preserve"), output)
        );

        TFCItems.FRUIT_PRESERVES.forEach((food, item) ->
            unsealingRecipe(item, TFCItems.UNSEALED_FRUIT_PRESERVES.get(food), Helpers.identifier("crafting/jar/" + food.getSerializedName() + "_unsealed"), output)
        );

        JamJarSealingRecipeBuilder.sealing(
                        JammiesTags.Items.LIDS,
                        Ingredient.of(TFCItems.EMPTY_JAR),
                        ItemStackProvider.of(new ItemStack(TFCItems.EMPTY_JAR_WITH_LID)))
                .unlockedBy("has_jar", has(TFCItems.EMPTY_JAR))
                .save(output, Helpers.identifier("crafting/empty_jar_with_lid"));

        JamJarUnsealingRecipeBuilder.unSealing(
                Ingredient.of(TFCItems.EMPTY_JAR_WITH_LID),
                ItemStackProvider.of(new ItemStack(TFCItems.EMPTY_JAR)))
                .alwaysReturnLid()
                .unlockedBy("has_jar", has(TFCItems.EMPTY_JAR_WITH_LID))
                .save(output, Jammies.identifier("crafting/empty_jar"));

        // Firmalife Compat
        FLItems.UNSEALED_FRUIT_PRESERVES.forEach((food, item) ->
                sealingRecipe(item, FLItems.FRUIT_PRESERVES.get(food), Jammies.identifier("crafting/jar/" + food.getSerializedName() + "_preserve"),
                        output.withConditions(modLoaded(FirmaLife.MOD_ID)))
        );

        FLItems.FRUIT_PRESERVES.forEach((food, item) ->
                unsealingRecipe(item, FLItems.UNSEALED_FRUIT_PRESERVES.get(food), FLHelpers.identifier("crafting/jar/" + food.getSerializedName() + "_unsealed"),
                        output.withConditions(modLoaded(FirmaLife.MOD_ID)))
        );

        AdvancedCraftingRecipeBuilder.shaped("", ItemStackProvider.of(new ItemStack(FLItems.HONEY_JAR), AddTraitModifier.of(FoodTraits.CANNED), CopyLidDataModifier.INSTANCE))
                .pattern("XXX")
                .pattern("XYX")
                .pattern("XXX")
                .define('X', notRotten(Ingredient.of(FLItems.FOODS.get(FLFood.RAW_HONEY))))
                .inputItem('Y', TFCItems.EMPTY_JAR_WITH_LID, 1, 1)
                .unlockedBy("has_jar", has(TFCItems.JAR_LID))
                .save(output.withConditions(modLoaded(FirmaLife.MOD_ID)), FLHelpers.identifier("crafting/jarring_food/raw_honey"));

        // KubasTFC Compat
        sealingRecipe(KubastfcaItems.UNSEALED_MEAT_WEK, KubastfcaItems.MEAT_WEK, Jammies.identifier("crafting/jar/meat_close"),
                output.withConditions(modLoaded(kubastfca.MODID)));

        sealingRecipe(KubastfcaItems.UNSEALED_MIX_WEK, KubastfcaItems.MIX_WEK, Jammies.identifier("crafting/jar/mix_close"),
                output.withConditions(modLoaded(kubastfca.MODID)));

        sealingRecipe(KubastfcaItems.UNSEALED_VEGGIE_WEK, KubastfcaItems.VEGGIE_WEK, Jammies.identifier("crafting/jar/veggie_close"),
                output.withConditions(modLoaded(kubastfca.MODID)));

        unsealingRecipe(KubastfcaItems.MEAT_WEK, KubastfcaItems.UNSEALED_MEAT_WEK, ResourceLocation.fromNamespaceAndPath(kubastfca.MODID, "food/jar/meat_open"),
                output.withConditions(modLoaded(kubastfca.MODID)));

        unsealingRecipe(KubastfcaItems.MIX_WEK, KubastfcaItems.UNSEALED_MIX_WEK, ResourceLocation.fromNamespaceAndPath(kubastfca.MODID, "food/jar/mix_open"),
                output.withConditions(modLoaded(kubastfca.MODID)));

        unsealingRecipe(KubastfcaItems.VEGGIE_WEK, KubastfcaItems.UNSEALED_VEGGIE_WEK, ResourceLocation.fromNamespaceAndPath(kubastfca.MODID, "food/jar/veggie_open"),
                output.withConditions(modLoaded(kubastfca.MODID)));
    }

    private void sealingRecipe(ItemLike ingredient, ItemLike result, ResourceLocation rl, RecipeOutput output) {
        JamJarSealingRecipeBuilder.sealing(
                JammiesTags.Items.LIDS,
                notRotten(Ingredient.of(ingredient)),
                ItemStackProvider.of(new ItemStack(result), CopyExactDateModifier.INSTANCE)
            )
            .unlockedBy("has_jar", has(ingredient))
            .save(output, rl);
    }

    private void unsealingRecipe(ItemLike ingredient, ItemLike result, ResourceLocation rl, RecipeOutput output) {
        JamJarUnsealingRecipeBuilder.unSealing(
                Ingredient.of(ingredient),
                ItemStackProvider.of(new ItemStack(result), CopyFoodModifier.INSTANCE)
        )
        .unlockedBy("has_jar", has(ingredient))
        .save(output, rl);
    }

    private Ingredient notRotten(Ingredient food) {
        return AndIngredient.of(food, NotRottenIngredient.INSTANCE);
    }
}
