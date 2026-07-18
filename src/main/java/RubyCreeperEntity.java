package net.tutorial.rubymod.item;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

// 自定义工具等级：红宝石
public enum ModToolTiers implements Tier {
    // 参数：挖掘等级, 耐久, 挖矿速度, 攻击加成, 附魔能力, 修复材料
    RUBY(3, 2000, 10.0F, 3.0F, 15, () -> Ingredient.of(ModItems.RUBY.get()));

    private final int level;
    private final int uses;
    private final float speed;
    private final float attackDamageBonus;
    private final int enchantmentValue;
    private final Supplier<Ingredient> repairIngredient;

    ModToolTiers(int level, int uses, float speed, float attackDamageBonus,
                 int enchantmentValue, Supplier<Ingredient> repairIngredient) {
        this.level = level;
        this.uses = uses;
        this.speed = speed;
        this.attackDamageBonus = attackDamageBonus;
        this.enchantmentValue = enchantmentValue;
        this.repairIngredient = repairIngredient;
    }

    @Override public int getUses() { return this.uses; }
    @Override public float getSpeed() { return this.speed; }
    @Override public float getAttackDamageBonus() { return this.attackDamageBonus; }
    @Override public int getLevel() { return this.level; }
    @Override public int getEnchantmentValue() { return this.enchantmentValue; }
    @Override public Ingredient getRepairIngredient() { return this.repairIngredient.get(); }
}
