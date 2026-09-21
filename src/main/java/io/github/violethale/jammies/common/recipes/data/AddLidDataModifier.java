package io.github.violethale.jammies.common.recipes.data;

import net.dries007.tfc.common.recipes.outputs.ItemStackModifier;
import net.dries007.tfc.common.recipes.outputs.ItemStackModifierType;
import io.github.violethale.jammies.common.component.LidDataComponent;
import io.github.violethale.jammies.registry.JammiesComponents;
import io.github.violethale.jammies.registry.JammiesItemStackModifiers;
import io.github.violethale.jammies.registry.JammiesTags;
import net.minecraft.world.item.ItemStack;

public enum AddLidDataModifier implements ItemStackModifier {
    INSTANCE;

    @Override
    public ItemStack apply(ItemStack itemStack, ItemStack input, Context context) {
        if (input.is(JammiesTags.Items.LIDS)) {
            LidDataComponent component = LidDataComponent.of(input);
            if (component != null) {
                itemStack.set(JammiesComponents.JAR_LID_COMPONENT, component);
            }
        }
        return itemStack;
    }

    @Override
    public ItemStackModifierType<?> type() {
        return JammiesItemStackModifiers.ADD_LID_MODIFIER.get();
    }
}
