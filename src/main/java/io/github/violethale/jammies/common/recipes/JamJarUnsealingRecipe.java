package io.github.violethale.jammies.common.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.dries007.tfc.common.recipes.RecipeHelpers;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import io.github.violethale.jammies.registry.JammiesComponents;
import io.github.violethale.jammies.common.component.LidDataComponent;
import io.github.violethale.jammies.registry.JammiesRecipeSerializers;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.ItemHandlerHelper;

public class JamJarUnsealingRecipe implements CraftingRecipe {
    private final Ingredient sealedJar;
    private final ItemStackProvider result;
    private final Boolean alwaysReturnLid;

    public JamJarUnsealingRecipe(Ingredient sealedJar, ItemStackProvider result, Boolean alwaysReturnLid) {
        this.sealedJar = sealedJar;
        this.result = result;
        this.alwaysReturnLid = alwaysReturnLid;
    }

    public JamJarUnsealingRecipe(Ingredient sealedJar, ItemStackProvider result) {
        this(sealedJar, result, false);
    }

    @Override
    public CraftingBookCategory category() {
        return CraftingBookCategory.MISC;
    }

    @Override
    public boolean matches(CraftingInput craftingInput, Level level) {
        boolean hasSealedJar = false;
        int itemCount = 0;
        for (int i = 0; i < craftingInput.size(); i++) {
            ItemStack stack = craftingInput.getItem(i);
            if (!stack.isEmpty()) {
                itemCount ++;
                if (sealedJar.test(stack)) {
                    hasSealedJar = true;
                }
            }
        }
        return itemCount == 1 && hasSealedJar;
    }

    public boolean matches(ItemStack stack) {
        return sealedJar.test(stack);
    }

    @Override
    public ItemStack assemble(CraftingInput craftingInput, HolderLookup.Provider provider) {
        ItemStack sealedJarStack = ItemStack.EMPTY;
        for (int i = 0; i < craftingInput.size(); i++) {
            ItemStack stack = craftingInput.getItem(i);
            if (sealedJar.test(stack)) {
                sealedJarStack = stack;
                break;
            }
        }
        return result.getSingleStack(sealedJarStack);
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY, sealedJar);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (!stack.isEmpty() && sealedJar.test(stack) && stack.has(JammiesComponents.JAR_LID_COMPONENT)) {
                Player player = RecipeHelpers.getCraftingPlayer();
                if (player != null) {
                    ItemStack lidReturn = getReturnLid(stack, player.getRandom());
                    if (!lidReturn.isEmpty())
                        ItemHandlerHelper.giveItemToPlayer(player, lidReturn);
                }
            }
        }
        return CraftingRecipe.super.getRemainingItems(input);
    }

    public ItemStack getReturnLid(ItemStack itemStack, RandomSource randomSource) {
        LidDataComponent component = itemStack.get(JammiesComponents.JAR_LID_COMPONENT);
        if (component == null) {
            return ItemStack.EMPTY;
        }
        Item lidItem = component.lidItem();
        float returnChance = component.returnChance();

        if (this.alwaysReturnLid || randomSource.nextFloat() < returnChance) {
            return new ItemStack(lidItem, 1);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 1;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return result.stack();
    }

    public ItemStack getResult(ItemStack input) {
        return result.getSingleStack(input);
    }

    public ItemStackProvider getResult() {
        return result;
    }

    public Ingredient getSealedJar() {
        return sealedJar;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return JammiesRecipeSerializers.JAM_UNSEALING_RECIPE.get();
    }

    public static class Serializer implements RecipeSerializer<JamJarUnsealingRecipe> {
        public static final MapCodec<JamJarUnsealingRecipe> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
                Ingredient.CODEC.fieldOf("jar").forGetter(c -> c.sealedJar),
                ItemStackProvider.CODEC.fieldOf("result").forGetter(c -> c.result),
                Codec.BOOL.optionalFieldOf("always_return_lid", false).forGetter(c -> c.alwaysReturnLid)
        ).apply(i, JamJarUnsealingRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, JamJarUnsealingRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, c -> c.sealedJar,
                ItemStackProvider.STREAM_CODEC, c -> c.result,
                ByteBufCodecs.BOOL, c -> c.alwaysReturnLid,
                JamJarUnsealingRecipe::new
        );

        @Override
        public MapCodec<JamJarUnsealingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, JamJarUnsealingRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }

}
