package io.github.violethale.jammies.registry;

import io.github.violethale.jammies.Jammies;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class JammiesTags {
    public static class Items {
        public static final TagKey<Item> LIDS = TagKey.create(Registries.ITEM, Jammies.identifier("lids"));
    }
}
