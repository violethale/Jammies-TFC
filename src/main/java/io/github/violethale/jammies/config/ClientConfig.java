package io.github.violethale.jammies.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig {

    public static final ModConfigSpec CLIENT_CONFIG;

    public static ModConfigSpec.BooleanValue enableTooltips;
    public static ModConfigSpec.BooleanValue shiftToolTips;
    public static ModConfigSpec.EnumValue<ToolTipStyle> toolTipStyle;

    static  {
        final ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.push("Jam Tooltip Display");

        enableTooltips = builder.comment("Show tooltips on Jams")
                .define("enableTooltips", true);
        shiftToolTips = builder.comment("Show tooltips on Jams only when the player holds SHIFT")
                .define("shiftToolTips", false);

        toolTipStyle = builder.comment(
                "The tooltip display style: ",
                " NORMAL - Displays the lid on one line and return chance on the second line",
                " SAME_LINE - Displays the lid and return chance on one line",
                " SHORT - Displays the lid and return chance on one line with no words",
                "Default - NORMAL"
        ).defineEnum("toolTipStyle", ToolTipStyle.NORMAL);

        builder.pop();

        CLIENT_CONFIG = builder.build();
    }
}
