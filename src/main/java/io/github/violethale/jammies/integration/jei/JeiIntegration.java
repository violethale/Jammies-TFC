package io.github.violethale.jammies.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import io.github.violethale.jammies.Jammies;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class JeiIntegration implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return Jammies.identifier("jei");
    }

    @Override
    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registry) {
        JammiesCraftingExtension.register(registry);
    }
}
