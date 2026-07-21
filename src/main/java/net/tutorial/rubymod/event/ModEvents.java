package net.tutorial.rubymod.event;

import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.fml.common.Mod;
import net.tutorial.rubymod.RubyMod;
import net.tutorial.rubymod.entity.ModEntities;
import net.tutorial.rubymod.entity.custom.RubyCreeperEntity;
import net.tutorial.rubymod.entity.custom.RubySkeletonEntity;
import net.tutorial.rubymod.entity.custom.RubyGolemEntity;
import net.tutorial.rubymod.entity.custom.AdamantineRubyGolemEntity;
import net.tutorial.rubymod.entity.custom.RubySpiderEntity;
import net.tutorial.rubymod.entity.custom.RubyEvokerEntity;
import net.tutorial.rubymod.entity.custom.RubyPillagerEntity;
import net.tutorial.rubymod.entity.custom.RubyPlayerEntity;

@Mod.EventBusSubscriber(modid = RubyMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.RUBY_GOLEM.get(), RubyGolemEntity.createAttributes().build());
        event.put(ModEntities.RUBY_CREEPER.get(), RubyCreeperEntity.createAttributes().build());
        event.put(ModEntities.RUBY_SKELETON.get(), RubySkeletonEntity.createAttributes().build());
        event.put(ModEntities.ADAMANTINE_RUBY_GOLEM.get(), AdamantineRubyGolemEntity.createAttributes().build());
        event.put(ModEntities.RUBY_SPIDER.get(), RubySpiderEntity.createAttributes().build());
        event.put(ModEntities.RUBY_EVOKER.get(), RubyEvokerEntity.createAttributes().build());
        event.put(ModEntities.RUBY_PILLAGER.get(), RubyPillagerEntity.createAttributes().build());
        event.put(ModEntities.RUBY_PLAYER.get(), RubyPlayerEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
        // 怪物生成规则：在黑暗的地表生成
        event.register(ModEntities.RUBY_GOLEM.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules,
                SpawnPlacementRegisterEvent.Operation.AND);

        event.register(ModEntities.RUBY_CREEPER.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules,
                SpawnPlacementRegisterEvent.Operation.AND);

        event.register(ModEntities.RUBY_SKELETON.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules,
                SpawnPlacementRegisterEvent.Operation.AND);

        event.register(ModEntities.RUBY_SPIDER.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules,
                SpawnPlacementRegisterEvent.Operation.AND);

        event.register(ModEntities.RUBY_EVOKER.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules,
                SpawnPlacementRegisterEvent.Operation.AND);

        event.register(ModEntities.RUBY_PILLAGER.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules,
                SpawnPlacementRegisterEvent.Operation.AND);
        // 注意：红宝石玩家是BOSS，只能用刷怪蛋放，不注册自然生成
    }
}
