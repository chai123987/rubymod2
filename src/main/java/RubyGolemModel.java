package net.tutorial.rubymod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tutorial.rubymod.RubyMod;
import net.tutorial.rubymod.entity.custom.RubyCreeperEntity;
import net.tutorial.rubymod.entity.custom.RubySkeletonEntity;
import net.tutorial.rubymod.entity.custom.RubyGolemEntity;
import net.tutorial.rubymod.entity.custom.AdamantineRubyGolemEntity;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, RubyMod.MOD_ID);

    public static final RegistryObject<EntityType<RubyGolemEntity>> RUBY_GOLEM =
            ENTITY_TYPES.register("ruby_golem",
                    () -> EntityType.Builder.of(RubyGolemEntity::new, MobCategory.MONSTER)
                            .sized(0.6f, 1.95f) // 人形怪的碰撞箱
                            .fireImmune()       // 不怕火和岩浆
                            .build("ruby_golem"));

    public static final RegistryObject<EntityType<RubyCreeperEntity>> RUBY_CREEPER =
            ENTITY_TYPES.register("ruby_creeper",
                    () -> EntityType.Builder.of(RubyCreeperEntity::new, MobCategory.MONSTER)
                            .sized(0.6f, 1.7f) // 和原版苦力怕一样大
                            .build("ruby_creeper"));

    public static final RegistryObject<EntityType<RubySkeletonEntity>> RUBY_SKELETON =
            ENTITY_TYPES.register("ruby_skeleton",
                    () -> EntityType.Builder.of(RubySkeletonEntity::new, MobCategory.MONSTER)
                            .sized(0.6f, 1.99f) // 和原版骷髅一样大
                            .build("ruby_skeleton"));

    public static final RegistryObject<EntityType<AdamantineRubyGolemEntity>> ADAMANTINE_RUBY_GOLEM =
            ENTITY_TYPES.register("adamantine_ruby_golem",
                    () -> EntityType.Builder.of(AdamantineRubyGolemEntity::new, MobCategory.MONSTER)
                            .sized(1.4f, 3.0f) // BOSS 体型，和铁傀儡差不多大
                            .fireImmune()       // 不怕火和岩浆
                            .build("adamantine_ruby_golem"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
