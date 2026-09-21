package io.github.violethale.jammies.common.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.dries007.tfc.common.recipes.RecipeHelpers;
import net.dries007.tfc.util.collections.IndirectHashCollection;
import net.dries007.tfc.util.data.DataManager;
import io.github.violethale.jammies.Jammies;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

public record LidProperties(Ingredient lidItem, float returnChance, String translationKey) {

    public static final Codec<LidProperties> CODEC = RecordCodecBuilder.create(i -> i.group(
            Ingredient.CODEC.fieldOf("lid_item").forGetter(c -> c.lidItem),
            Codec.floatRange(0.0F, 1.0F).optionalFieldOf("return_chance", 1.0F).forGetter(c -> c.returnChance),
            Codec.STRING.optionalFieldOf("translation_key", "jammies.generic.lid").forGetter(c -> c.translationKey)
    ).apply(i, LidProperties::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, LidProperties> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, c -> c.lidItem,
            ByteBufCodecs.FLOAT, c -> c.returnChance,
            ByteBufCodecs.STRING_UTF8, c -> c.translationKey,
            LidProperties::new
    );

    public static final DataManager<LidProperties> MANAGER =  new DataManager<>(Jammies.identifier("lid"), CODEC, STREAM_CODEC);
    public static final IndirectHashCollection<Item, LidProperties> CACHE = IndirectHashCollection.create(c -> RecipeHelpers.itemKeys(c.lidItem), MANAGER::getValues);

    public boolean matches(Item item) {
        return lidItem.test(new ItemStack(item));
    }

    public static float getReturnChance(ItemStack stack) {
        LidProperties properties = get(stack);
        if (properties != null) {
            return properties.returnChance();
        }
        return 0.0F;
    }

    public static String getTranslationKey(ItemStack stack) {
        LidProperties properties = get(stack);
        if (properties != null) {
            return properties.translationKey();
        }
        return "jammies.generic.lid";
    }

    @Nullable
    public static LidProperties get(ItemStack stack) {
        for (LidProperties def : CACHE.getAll(stack.getItem())) {
            if (def.lidItem.test(stack)) {
                return def;
            }
        }
        return null;
    }

}
