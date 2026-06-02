package net.chabato.immersive_casting;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue ENABLE_GLYPH_CASTING = BUILDER
            .comment("Whether to enable spell casting (default: true)(require restart)")
            .define("enableGlyphCasting", true);

    public static final ModConfigSpec.BooleanValue ALLOW_LOCKED_GLYPHS = BUILDER
            .comment("Whether to allow casting locked(unlearned) glyphs (default: false)")
            .define("allowLockedGlyphs", false);

    public static final ModConfigSpec.IntValue PROJECTILE_ACCELERATION = BUILDER
            .comment("Spell using method projectile will be accelarated for N times")
            .defineInRange("projectileAcceleration", 9, 0, Integer.MAX_VALUE);

    static final ModConfigSpec SPEC = BUILDER.build();

    private static boolean validateItemName(final Object obj) {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }
}
