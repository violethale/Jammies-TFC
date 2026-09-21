package io.github.violethale.jammies.integration.emi;

import dev.emi.emi.api.recipe.EmiCraftingRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.dries007.tfc.compat.emi.EmiHelpers;
import io.github.violethale.jammies.common.recipes.JamJarUnsealingRecipe;
import io.github.violethale.jammies.common.data.LidProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EmiJarUnsealingRecipe extends EmiCraftingRecipe {
    private final @Nullable EmiIngredient jarIngredient;
    private final ItemStackProvider provider;
    private final List<EmiStack> possibleLids;

    public EmiJarUnsealingRecipe(ResourceLocation id, JamJarUnsealingRecipe recipe) {
        super(recipe.getIngredients().stream().map(EmiIngredient::of).toList(), EmiHelpers.nonDecayStack(recipe.getResultItem(EmiHelpers.registryAccess())), id);
        jarIngredient = EmiIngredient.of(recipe.getSealedJar());
        provider = recipe.getResult();

        possibleLids = new ArrayList<>();
        for (LidProperties props : LidProperties.MANAGER.getValues()) {
            ItemStack[] items = props.lidItem().getItems();
            for (ItemStack lidStack : items) {
                if (!lidStack.isEmpty()) {
                    possibleLids.add(EmiStack.of(lidStack.copy()));
                }
            }
        }
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 60, 18);
        widgets.addTexture(EmiTexture.SHAPELESS, 97, 0);

        for (int i = 0; i < 9; i++) {
            if (i < input.size()) {
                widgets.addSlot(input.get(i), (i % 3) * 18, (i / 3) * 18);
            } else {
                widgets.addSlot(EmiStack.of(ItemStack.EMPTY), i % 3 * 18, i / 3 * 18);
            }
        }

        widgets.addSlot(output, 95, 19).recipeContext(this);

        if (!possibleLids.isEmpty()) {
            EmiIngredient lidsIngredient = EmiIngredient.of(possibleLids);
            widgets.addSlot(lidsIngredient, 60, 0)
                    .drawBack(false);
        }
    }
}
