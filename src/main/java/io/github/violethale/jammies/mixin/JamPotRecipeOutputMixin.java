package io.github.violethale.jammies.mixin;

import net.dries007.tfc.common.blockentities.IPotInventory;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.common.recipes.JamPotRecipe;
import net.dries007.tfc.util.Helpers;
import io.github.violethale.jammies.registry.JammiesComponents;
import io.github.violethale.jammies.common.component.LidDataComponent;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = JamPotRecipe.JamOutput.class, remap = false)
public abstract class JamPotRecipeOutputMixin {

    @Shadow @Final private ItemStack sealedStack;

    @Shadow @Final private ItemStack unsealedStack;

    /**
     * Copy Lid Component into the result stack from the input stack
     */

    @Inject(
            method = "onInteract",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void jammies$onInteract(IPotInventory entity, Player player, ItemStack clickedWith, CallbackInfoReturnable<ItemInteractionResult> cir) {
        if (Helpers.isItem(clickedWith, TFCItems.EMPTY_JAR_WITH_LID)) {
            LidDataComponent component = clickedWith.get(JammiesComponents.JAR_LID_COMPONENT);

            clickedWith.shrink(1);
            unsealedStack.shrink(1);

            ItemStack result = sealedStack.copy();
            result.setCount(1);

            if (component != null) {
                result.set(JammiesComponents.JAR_LID_COMPONENT, component);
            }
            ItemHandlerHelper.giveItemToPlayer(player, result);
            cir.setReturnValue(ItemInteractionResult.sidedSuccess(player.level().isClientSide));
            return;
        }
    }
}
