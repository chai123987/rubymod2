package net.tutorial.rubymod.worldgen.biome;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.tutorial.rubymod.block.ModBlocks;

// 决定红宝石群系"地面铺什么"：顶层红宝石草方块，下面泥土。
public class ModSurfaceRules {
    private static final SurfaceRules.RuleSource RUBY_GRASS =
            makeStateRule(ModBlocks.RUBY_GRASS_BLOCK.get());
    private static final SurfaceRules.RuleSource DIRT = makeStateRule(Blocks.DIRT);

    public static SurfaceRules.RuleSource makeRules() {
        SurfaceRules.ConditionSource isAtOrAboveWaterLevel = SurfaceRules.waterBlockCheck(-1, 0);

        // 表层(ON_FLOOR)铺红宝石草；只在紧贴地表的那几格(UNDER_FLOOR)铺泥土。
        // ⚠️ 泥土必须限制在 UNDER_FLOOR，否则整列石头都会被换成泥土（这正是矿洞变泥土的原因）。
        SurfaceRules.RuleSource grassThenDirt = SurfaceRules.sequence(
                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR,
                        SurfaceRules.ifTrue(isAtOrAboveWaterLevel, RUBY_GRASS)),
                SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, DIRT));

        // abovePreliminarySurface：只在"真正的地表附近"套用这套规则，
        // 这样深处矿洞的地面/墙壁不会被铺成草或泥土，石头照旧。
        SurfaceRules.RuleSource rubySurface = SurfaceRules.ifTrue(
                SurfaceRules.abovePreliminarySurface(), grassThenDirt);

        // 只在红宝石群系里套用上面这套地表
        return SurfaceRules.ifTrue(
                SurfaceRules.isBiome(ModBiomes.RUBY_BIOME),
                rubySurface);
    }

    private static SurfaceRules.RuleSource makeStateRule(Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }
}
