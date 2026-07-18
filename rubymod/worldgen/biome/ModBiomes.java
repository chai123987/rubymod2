package net.tutorial.rubymod.worldgen.biome;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.tutorial.rubymod.RubyMod;

// 红宝石群系的"身份证"。群系长什么样写在 JSON 里
// （data/rubymod/worldgen/biome/ruby_biome.json），这里只留一个 key 供代码引用。
public class ModBiomes {
    public static final ResourceKey<Biome> RUBY_BIOME = ResourceKey.create(
            Registries.BIOME, new ResourceLocation(RubyMod.MOD_ID, "ruby_biome"));
}
