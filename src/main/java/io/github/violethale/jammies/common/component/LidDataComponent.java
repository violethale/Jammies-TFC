package io.github.violethale.jammies.common.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.violethale.jammies.Jammies;
import io.github.violethale.jammies.common.data.LidProperties;
import io.github.violethale.jammies.config.ClientConfig;
import io.github.violethale.jammies.registry.JammiesComponents;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record LidDataComponent(Item lidItem, float returnChance) {

    public static final Codec<LidDataComponent> CODEC = RecordCodecBuilder.create(i -> i.group(
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("lid_item").forGetter(c -> c.lidItem),
            Codec.FLOAT.fieldOf("return_chance").forGetter(c -> c.returnChance)
    ).apply(i, LidDataComponent::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, LidDataComponent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.registry(BuiltInRegistries.ITEM.key()), c -> c.lidItem,
            ByteBufCodecs.FLOAT, c -> c.returnChance,
            LidDataComponent::new
    );

    public static LidDataComponent of(ItemStack lidStack) {
        if (lidStack.isEmpty()) {
            Jammies.LOGGER.warn("Attempted to create LidComponent from empty ItemStack");
            return null;
        }
        float returnChance = LidProperties.getReturnChance(lidStack);
        return new LidDataComponent(lidStack.getItem(), returnChance);
    }

    public ItemStack toLidStack() {
        return new ItemStack(lidItem, 1);
    }

    public static void addTooltipInfo(ItemStack stack, List<Component> toolTip) {
        final @Nullable LidDataComponent component = stack.get(JammiesComponents.JAR_LID_COMPONENT);
        if (component != null) {
            ItemStack lidStack = component.toLidStack();

            String defaultTranslationKey = LidProperties.getTranslationKey(lidStack);
            float returnChance = component.returnChance();

            String translationKey = defaultTranslationKey;

            if (defaultTranslationKey.equals("jammies.generic.lid") || !I18n.exists(defaultTranslationKey)) {
                String itemTranslationKey = lidStack.getDescriptionId();

                if (I18n.exists(itemTranslationKey)) {
                    translationKey = itemTranslationKey;
                }
            }
            Component lidName = Component.translatable(translationKey);
            String percent = String.format("%.0f%%", returnChance * 100);

            switch (ClientConfig.toolTipStyle.get()) {
                case NORMAL -> {
                    toolTip.add(1, Component.translatable("tooltip.jammies.lid.normal", lidName));
                    toolTip.add(2, Component.translatable("tooltip.jammies.lid.return_chance", percent));
                }

                case SAME_LINE -> {
                    toolTip.add(1, Component.translatable("tooltip.jammies.lid.same_line", lidName, percent));
                }

                case SHORT -> {
                    toolTip.add(1, Component.translatable("tooltip.jammies.lid.short", lidName, percent));
                }
            }
        }
    }

}
