package io.github.violethale.jammies.integration.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import net.dries007.tfc.compat.jei.JEIIntegration;
import io.github.violethale.jammies.common.recipes.JamJarUnsealingRecipe;
import io.github.violethale.jammies.common.data.LidProperties;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.ArrayList;
import java.util.List;

public class JammiesCraftingExtension {
    public static void register(IVanillaCategoryExtensionRegistration registry) {
        registry.getCraftingCategory().addExtension(JamJarUnsealingRecipe.class, new ICraftingCategoryExtension<>() {
            @Override
            public void setRecipe(RecipeHolder<JamJarUnsealingRecipe> recipeHolder, IRecipeLayoutBuilder builder, ICraftingGridHelper helper, IFocusGroup focuses) {

                final JamJarUnsealingRecipe recipe = recipeHolder.value();
                final NonNullList<Ingredient> ingredients = recipe.getIngredients();
                final List<List<ItemStack>> inputs = ingredients.stream()
                        .map(ingredient -> List.of(ingredient.getItems()))
                        .toList();

                helper.createAndSetInputs(builder, JEIIntegration.ITEM_STACK, inputs, 0, 0);

                ItemStack output = recipe.getResultItem(null);
                helper.createAndSetOutputs(builder, JEIIntegration.ITEM_STACK, List.of(output));

                List<ItemStack> possibleLids = new ArrayList<>();
                for (LidProperties props : LidProperties.MANAGER.getValues()) {
                    ItemStack[] items = props.lidItem().getItems();
                    for (ItemStack lidStack : items) {
                        if (!lidStack.isEmpty()) {
                            possibleLids.add(lidStack.copy());
                        }
                    }
                }

                if (!possibleLids.isEmpty()) {
                    builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 60, 0)
                            .addItemStacks(possibleLids)
                            .setSlotName("lid_return");
                }
            }
        });
    }
}
