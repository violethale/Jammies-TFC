package io.github.violethale.jammies.common.data;

import net.dries007.tfc.util.data.DataManager;
import io.github.violethale.jammies.Jammies;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class ModDataManagers {
    public static final ResourceKey<Registry<DataManager<?>>> KEY = ResourceKey.createRegistryKey(Jammies.identifier("data_manager"));
    public static final Registry<DataManager<?>> REGISTRY = new RegistryBuilder<>(KEY).sync(true).create();

    public static final DeferredRegister<DataManager<?>> DATA_MANAGERS = DeferredRegister.create(KEY, Jammies.MOD_ID);

    static {
        register(LidProperties.MANAGER);
    }

    private static void register(DataManager<?> manager)
    {
        DATA_MANAGERS.register(manager.getName(), () -> manager);
    }

}
