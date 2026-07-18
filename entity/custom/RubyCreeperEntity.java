package net.tutorial.rubymod.entity.custom;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

// 红宝石苦力怕：继承原版苦力怕，自动拥有膨胀/闪烁/引信/爆炸全套动画与逻辑。
// 更大的爆炸 + 火焰效果在 ModForgeEvents 里通过爆炸事件实现。
public class RubyCreeperEntity extends Creeper {

    // 防止自定义爆炸递归触发的标记
    public boolean rubyExploded = false;

    public RubyCreeperEntity(EntityType<? extends Creeper> type, Level level) {
        super(type, level);
    }

    // 属性和原版苦力怕一致：20 点血
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }
}
