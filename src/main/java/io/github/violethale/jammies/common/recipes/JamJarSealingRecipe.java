package io.github.violethale.jammies.common.recipes;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import io.github.violethale.jammies.registry.JammiesComponents;
import io.github.violethale.jammies.common.component.LidDataComponent;
import io.github.violethale.jammies.registry.JammiesRecipeSerializers;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class JamJarSealingRecipe implements CraftingRecipe {
    private final Ingredient lid;
    private final Ingredient jar;
    private final ItemStackProvider result;

    public JamJarSealingRecipe(Ingredient lid, Ingredient jar, ItemStackProvider result) {
        this.lid = lid;
        this.jar = jar;
        this.result = result;
    }

    @Override
    public CraftingBookCategory category() {
        return CraftingBookCategory.MISC;
    }

    @Override
    public boolean matches(CraftingInput craftingInput, Level level) {
        boolean hasLid = false, hasJar = false;
        int itemCount = 0;
        for (int i = 0; i < craftingInput.size(); i++) {
            ItemStack stack = craftingInput.getItem(i);
            if (!stack.isEmpty()) {
                itemCount++;
                if (lid.test(stack)) hasLid = true;
                else if (jar.test(stack)) hasJar = true;
            }
        }
        return itemCount == 2 && hasLid && hasJar;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider provider) {
        ItemStack lidStack = ItemStack.EMPTY;
        ItemStack jarStack = ItemStack.EMPTY;
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (lid.test(stack)) {
                lidStack = stack;
            } else if (jar.test(stack)) {
                jarStack = stack;
            }
        }
        ItemStack result = this.result.getSingleStack(jarStack);
        LidDataComponent component = LidDataComponent.of(lidStack);
        if (component != null) {
            result.set(JammiesComponents.JAR_LID_COMPONENT, component);
        }
        return result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(lid);
        ingredients.add(jar);
        return ingredients;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        return NonNullList.withSize(input.size(), ItemStack.EMPTY);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return this.result.stack();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return JammiesRecipeSerializers.JAM_SEALING_RECIPE.get();
    }


    public static class Serializer implements RecipeSerializer<JamJarSealingRecipe> {
        public static final MapCodec<JamJarSealingRecipe> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
                Ingredient.CODEC.fieldOf("lid").forGetter(c -> c.lid),
                Ingredient.CODEC.fieldOf("jar").forGetter(c -> c.jar),
                ItemStackProvider.CODEC.fieldOf("result").forGetter(c -> c.result)
        ).apply(i, JamJarSealingRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, JamJarSealingRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, c -> c.lid,
                Ingredient.CONTENTS_STREAM_CODEC, c -> c.jar,
                ItemStackProvider.STREAM_CODEC, c -> c.result,
                JamJarSealingRecipe::new
        );

        @Override
        public MapCodec<JamJarSealingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, JamJarSealingRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
