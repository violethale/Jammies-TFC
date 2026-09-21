package io.github.violethale.jammies.common.recipes.data;

import net.dries007.tfc.common.component.food.FoodCapability;
import net.dries007.tfc.common.component.food.IFood;
import net.dries007.tfc.common.recipes.outputs.ItemStackModifier;
import net.dries007.tfc.common.recipes.outputs.ItemStackModifierType;
import io.github.violethale.jammies.registry.JammiesItemStackModifiers;
import net.minecraft.world.item.ItemStack;

public enum CopyExactDateModifier implements ItemStackModifier {
    INSTANCE;

    @Override
    public ItemStack apply(ItemStack itemStack, ItemStack input, Context context) {
        ItemStack returnStack = FoodCapability.updateFoodFromPrevious(input, itemStack);
        IFood iFood = FoodCapability.get(input);
        if (iFood != null) {
            FoodCapability.setCreationDate(returnStack, iFood.getCreationDate());
        }

        return returnStack;
    }

    @Override
    public ItemStackModifierType<?> type() {
        return JammiesItemStackModifiers.COPY_DATE_MODIFIER.get();
    }
}
