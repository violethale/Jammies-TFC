package io.github.violethale.jammies.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class CommonConfig {
    public static final ModConfigSpec COMMON_CONFIG;

    public static ModConfigSpec.BooleanValue allowRightClickOpen;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.push("Jammies Config");

        allowRightClickOpen = builder.comment("Allow Jams to open when right-clicking them")
                .define("allowRightClickOpen", true);

        builder.pop();

        COMMON_CONFIG = builder.build();
    }
}
