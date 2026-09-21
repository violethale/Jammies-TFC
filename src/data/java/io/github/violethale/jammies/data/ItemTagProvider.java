package io.github.violethale.jammies.data;

import com.eerussianguy.firmalife.common.items.FLItems;
import net.dries007.tfc.common.items.TFCItems;
import io.github.violethale.jammies.registry.JammiesTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class ItemTagProvider extends ItemTagsProvider {
    public ItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookupProvider, blockTags);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(JammiesTags.Items.LIDS)
                .addOptional(TFCItems.JAR_LID.getId())
                .addOptional(FLItems.STAINLESS_STEEL_JAR_LID.getId());
    }
}
