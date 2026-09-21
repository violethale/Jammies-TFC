package io.github.violethale.jammies.registry;

import com.mojang.serialization.MapCodec;
import net.dries007.tfc.common.recipes.outputs.ItemStackModifier;
import net.dries007.tfc.common.recipes.outputs.ItemStackModifierType;
import net.dries007.tfc.common.recipes.outputs.ItemStackModifiers;
import io.github.violethale.jammies.Jammies;
import io.github.violethale.jammies.common.recipes.data.AddLidDataModifier;
import io.github.violethale.jammies.common.recipes.data.CopyExactDateModifier;
import io.github.violethale.jammies.common.recipes.data.CopyLidDataModifier;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class JammiesItemStackModifiers {
    public static final DeferredRegister<ItemStackModifierType<?>> ITEMSTACK_MODIFIER = DeferredRegister.create(ItemStackModifiers.KEY, Jammies.MOD_ID);

    public static final Supplier<ItemStackModifierType<CopyExactDateModifier>> COPY_DATE_MODIFIER = register("copy_date", CopyExactDateModifier.INSTANCE);
    public static final Supplier<ItemStackModifierType<CopyLidDataModifier>> COPY_LID_MODIFIER = register("copy_lid", CopyLidDataModifier.INSTANCE);
    public static final Supplier<ItemStackModifierType<AddLidDataModifier>> ADD_LID_MODIFIER = register("add_lid", AddLidDataModifier.INSTANCE);

    private static <T extends ItemStackModifier> Supplier<ItemStackModifierType<T>> register(String name, T singleInstance) {
        return ITEMSTACK_MODIFIER.register(name, () -> new ItemStackModifierType<>(MapCodec.unit(singleInstance), StreamCodec.unit(singleInstance)));
    }
}
