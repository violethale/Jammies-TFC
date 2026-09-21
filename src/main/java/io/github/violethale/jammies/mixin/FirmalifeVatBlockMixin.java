package io.github.violethale.jammies.mixin;

import com.eerussianguy.firmalife.common.blockentities.VatBlockEntity;
import com.eerussianguy.firmalife.common.blocks.oven.VatBlock;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Helpers;
import io.github.violethale.jammies.registry.JammiesComponents;
import io.github.violethale.jammies.common.component.LidDataComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = VatBlock.class, remap = false)
public class FirmalifeVatBlockMixin {

    /**
     * Copy Lid Component into the result stack from the input stack
     */

    @Inject(
            method = "useItemOn",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    public void jammies$useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result, CallbackInfoReturnable<ItemInteractionResult> cir) {
        if (!(level.getBlockEntity(pos) instanceof VatBlockEntity vat)) {
            return;
        }
        if (vat.isBoiling()) {
            return;
        }
        if (!vat.hasOutput()) {
            return;
        }

        if (Helpers.isItem(stack, TFCItems.EMPTY_JAR_WITH_LID)) {
            LidDataComponent component = stack.get(JammiesComponents.JAR_LID_COMPONENT);

            stack.shrink(1);
            ItemStack output = vat.takeOutput();

            if (component != null) {
                output.set(JammiesComponents.JAR_LID_COMPONENT, component);
            }

            ItemHandlerHelper.giveItemToPlayer(player, output);
            cir.setReturnValue(ItemInteractionResult.sidedSuccess(level.isClientSide));

            return;
        }
        cir.setReturnValue(ItemInteractionResult.FAIL);
    }
}
