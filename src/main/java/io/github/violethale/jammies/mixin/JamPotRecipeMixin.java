package io.github.violethale.jammies.mixin;

import net.dries007.tfc.common.recipes.JamPotRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = JamPotRecipe.class, remap = false)
public class JamPotRecipeMixin {

    @Shadow @Final private ItemStack jarredStack;

    /**
     * Switch the JEI result from jarredStackWithLid to jarredStack
     */

    @Inject(
            method = "getResultItem",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void jammies$getResultItem(HolderLookup.Provider registries, CallbackInfoReturnable<ItemStack> cir) {
        cir.setReturnValue(jarredStack);
    }
}
