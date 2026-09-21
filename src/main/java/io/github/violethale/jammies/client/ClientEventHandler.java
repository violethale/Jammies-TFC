package io.github.violethale.jammies.client;

import io.github.violethale.jammies.common.component.LidDataComponent;
import io.github.violethale.jammies.common.data.LidProperties;
import io.github.violethale.jammies.config.ClientConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.List;

public final class ClientEventHandler {

    public static void init(IEventBus modEventBus) {
        modEventBus.addListener(ClientEventHandler::onItemToolTip);
    }

    public static void onItemToolTip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        List<Component> toolTip = event.getToolTip();

        if (stack.isEmpty()) {
            return;
        }

        if (!ClientConfig.enableTooltips.get()) {
            return;
        }
        if (ClientConfig.shiftToolTips.get() && !Screen.hasShiftDown()) {
            if (hasTooltip(toolTip, "tfc.tooltip.hold_shift_for_nutrition_info")) {
                toolTip.remove(Component.translatable("tfc.tooltip.hold_shift_for_nutrition_info"));
                toolTip.add(1, Component.translatable("jammies.tooltip.hold_shift_for_more_info").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
                return;
            }
            toolTip.add(1, Component.translatable("jammies.tooltip.hold_shift_for_lid_info").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
            return;
        }

        LidDataComponent.addTooltipInfo(stack, toolTip);
        addLidTooltip(stack, toolTip);
    }

    public static void addLidTooltip(ItemStack stack, List<Component> toolTip) {
        for (LidProperties props : LidProperties.MANAGER.getValues()) {
            ItemStack[] items = props.lidItem().getItems();
            for (ItemStack lidStack : items) {
                if (!lidStack.isEmpty() && stack.is(lidStack.getItem())) {
                    float returnChance = LidProperties.getReturnChance(stack);
                    toolTip.add(1, Component.translatable("tooltip.jammies.lid.return_chance", String.format("%.0f%%", returnChance * 100)));
                }
            }
        }
    }

    private static boolean hasTooltip(List<Component> tooltip, String translationKey) {
        return tooltip.stream().anyMatch(c ->
                        c.getContents() instanceof TranslatableContents tc &&
                                tc.getKey().equals(translationKey)
                );
    }
}
