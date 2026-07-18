package net.tutorial.rubymod.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tutorial.rubymod.RubyMod;
import net.tutorial.rubymod.entity.custom.RubyCreeperEntity;

// FORGE 总线事件：把红宝石苦力怕的爆炸改成「更大范围 + 火焰」
@Mod.EventBusSubscriber(modid = RubyMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModForgeEvents {

    @SubscribeEvent
    public static void onExplosionStart(ExplosionEvent.Start event) {
        Level level = event.getLevel();
        if (level.isClientSide) return;

        if (event.getExplosion().getDirectSourceEntity() instanceof RubyCreeperEntity creeper
                && !creeper.rubyExploded) {
            creeper.rubyExploded = true;
            // 取消原版那次普通爆炸
            event.setCanceled(true);
            // 重新放一次更大、带火的爆炸（半径 6，原版是 3）
            level.explode(creeper, creeper.getX(), creeper.getY(), creeper.getZ(),
                    6.0F, true, Level.ExplosionInteraction.MOB);
            // 额外保证：把范围内的生物（含玩家）点燃 5 秒
            level.getEntitiesOfClass(LivingEntity.class, creeper.getBoundingBox().inflate(6.0D))
                    .forEach(e -> e.setSecondsOnFire(5));
        }
    }
}
