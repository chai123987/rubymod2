package net.tutorial.rubymod.worldgen.biome;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.ParameterUtils;
import terrablender.api.Region;
import terrablender.api.RegionType;

import java.util.function.Consumer;

// TerraBlender 的核心：把红宝石群系按一组"气候坐标"塞进主世界。
// 这些 span(...) 范围大致对应温带、内陆的地块，权重在主类里注册时给。
public class RubyRegion extends Region {
    public RubyRegion(ResourceLocation name, int weight) {
        super(name, RegionType.OVERWORLD, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry,
                          Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        // 收窄到一个"温暖、内陆、地形平缓"的具体气候小区间。
        // 范围越窄、越具体 → 群系越成块连续，而不是到处零星插进别的群系。
        new ParameterUtils.ParameterPointListBuilder()
                .temperature(Climate.Parameter.span(0.1F, 0.3F))
                .humidity(Climate.Parameter.span(0.0F, 0.3F))
                .continentalness(Climate.Parameter.span(0.3F, 1.0F))
                .erosion(Climate.Parameter.span(-0.4F, 0.4F))
                .depth(Climate.Parameter.point(0.0F))
                .weirdness(Climate.Parameter.span(-0.2F, 0.2F))
                .build()
                .forEach(point -> mapper.accept(Pair.of(point, ModBiomes.RUBY_BIOME)));
    }
}
