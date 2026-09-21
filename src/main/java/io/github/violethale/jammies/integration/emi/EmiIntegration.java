package io.github.violethale.jammies.integration.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import io.github.violethale.jammies.common.recipes.JamJarUnsealingRecipe;
import net.minecraft.world.item.crafting.RecipeType;

@EmiEntrypoint
public class EmiIntegration implements EmiPlugin {

    @Override
    public void register(EmiRegistry registry) {
        registry.getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING)
                .stream()
                .filter(holder -> holder.value() instanceof JamJarUnsealingRecipe)
                .forEach(holder -> {
                    JamJarUnsealingRecipe recipe = (JamJarUnsealingRecipe) holder.value();
                    registry.addRecipe(new EmiJarUnsealingRecipe(holder.id().withPath("/" + holder.id().getPath()), recipe));
                    registry.removeRecipes(holder.id());
                });

    }
}
