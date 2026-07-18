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

@Mod.EventBusSubscriber(modid = RubyMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.RUBY_GOLEM.get(), RubyGolemEntity.createAttributes().build());
        event.put(ModEntities.RUBY_CREEPER.get(), RubyCreeperEntity.createAttributes().build());
        event.put(ModEntities.RUBY_SKELETON.get(), RubySkeletonEntity.createAttributes().build());
        event.put(ModEntities.ADAMANTINE_RUBY_GOLEM.get(), AdamantineRubyGolemEntity.createAttributes().build());
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
    }
}
