package io.github.violethale.jammies.common.recipes;

import io.github.violethale.jammies.registry.JammiesComponents;
import io.github.violethale.jammies.common.component.LidDataComponent;
import io.github.violethale.jammies.config.CommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.Optional;

@EventBusSubscriber
public class RightClickUnsealingRecipe {

    @SubscribeEvent
    public static void rightClickUnsealingRecipe(PlayerInteractEvent.RightClickItem event) {
        if (!CommonConfig.allowRightClickOpen.get()) {
            return;
        }

        Level level = event.getLevel();
        InteractionHand hand = event.getHand();
        ItemStack heldItem = event.getItemStack();
        BlockPos pos = event.getPos();
        BlockState state = event.getLevel().getBlockState(pos);
        Player player = event.getEntity();

        if (level.isClientSide) {
            return;
        }
        if (hand != InteractionHand.MAIN_HAND) {
            return;
        }
        if (heldItem.isEmpty()) {
            return;
        }
        if (!state.isAir()) {
            return;
        }

        RecipeManager recipes = level.getRecipeManager();

        Optional<JamJarUnsealingRecipe> foundRecipe = recipes
                .getAllRecipesFor(RecipeType.CRAFTING)
                .stream()
                .map(RecipeHolder::value)
                .filter(recipe -> recipe instanceof JamJarUnsealingRecipe)
                .map(recipe -> (JamJarUnsealingRecipe) recipe)
                .filter(recipe -> recipe.matches(heldItem))
                .findFirst();

        if (foundRecipe.isPresent()) {
            JamJarUnsealingRecipe recipe = foundRecipe.get();

            ItemStack unsealedJar = recipe.getResult(heldItem);

            LidDataComponent component = heldItem.get(JammiesComponents.JAR_LID_COMPONENT);
            if (component != null) {
                ItemStack returnLid = recipe.getReturnLid(heldItem, level.random);
                if (!returnLid.isEmpty()) {
                    if (!player.getInventory().add(returnLid)) {
                        player.drop(returnLid, false);
                    }
                }
            }

            heldItem.shrink(1);
            if (!player.getInventory().add(unsealedJar)) {
                player.drop(unsealedJar, false);
            }

            player.swing(hand);
        }
    }

}
