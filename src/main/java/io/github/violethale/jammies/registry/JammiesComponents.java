package io.github.violethale.jammies.registry;

import io.github.violethale.jammies.Jammies;
import io.github.violethale.jammies.common.component.LidDataComponent;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class JammiesComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Jammies.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<LidDataComponent>> JAR_LID_COMPONENT =
            DATA_COMPONENTS.register(
                    "lid", () ->
                        DataComponentType.<LidDataComponent>builder()
                            .persistent(LidDataComponent.CODEC)
                            .networkSynchronized(LidDataComponent.STREAM_CODEC)
                            .build()
            );
}
