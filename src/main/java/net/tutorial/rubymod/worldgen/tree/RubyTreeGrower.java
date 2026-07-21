package net.tutorial.rubymod.worldgen.tree;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.tutorial.rubymod.RubyMod;

// 这个类就是「树苗 → 长成哪棵树」的桥。
// 它返回一个指向 configured_feature/ruby_tree.json 的 key，
// 玩家右键骨粉催熟树苗时，游戏就照那份 JSON 配方长树。
public class RubyTreeGrower extends AbstractTreeGrower {

    public static final ResourceKey<ConfiguredFeature<?, ?>> RUBY_TREE_KEY =
            ResourceKey.create(Registries.CONFIGURED_FEATURE,
                    new ResourceLocation(RubyMod.MOD_ID, "ruby_tree"));

    @Override
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean hasFlowers) {
        return RUBY_TREE_KEY;
    }
}
