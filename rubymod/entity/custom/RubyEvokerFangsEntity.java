package net.tutorial.rubymod.entity.custom;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.level.Level;
import net.tutorial.rubymod.entity.ModEntities;

// 继承原版尖刺，复用它的伤害判定和咬合动画，只换成红色贴图
public class RubyEvokerFangsEntity extends EvokerFangs {

    public RubyEvokerFangsEntity(EntityType<? extends EvokerFangs> entityType, Level level) {
        super(entityType, level);
    }

    // 便捷生成：放在指定位置，朝向 yRot，设置主人
    public static RubyEvokerFangsEntity create(Level level, double x, double y, double z,
                                               float yRot, LivingEntity owner) {
        RubyEvokerFangsEntity fangs = new RubyEvokerFangsEntity(ModEntities.RUBY_EVOKER_FANGS.get(), level);
        fangs.setYRot(yRot * (180F / (float) Math.PI));
        fangs.setPos(x, y, z);
        fangs.setOwner(owner);
        return fangs;
    }
}
