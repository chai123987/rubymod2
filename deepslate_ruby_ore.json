package net.tutorial.rubymod.entity.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.tutorial.rubymod.item.ModItems;

public class RubySkeletonEntity extends Skeleton {

    // 自己的弓/近战目标（原版那两个字段是私有的，拿不到，所以自己建一套）
    // 弓的攻击间隔 15 < 原版 20，所以射得略快；伤害走原版逻辑，所以一样
    private final RangedBowAttackGoal<RubySkeletonEntity> rubyBowGoal =
            new RangedBowAttackGoal<>(this, 1.0D, 15, 15.0F);
    private final MeleeAttackGoal rubyMeleeGoal = new MeleeAttackGoal(this, 1.2D, false);

    public RubySkeletonEntity(EntityType<? extends Skeleton> type, Level level) {
        super(type, level);
        this.reassessWeaponGoal();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)   // 和原版骷髅一样
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    // 用我们自己的目标替换原版（原版字段私有取不到）
    @Override
    public void reassessWeaponGoal() {
        if (this.level() == null || this.level().isClientSide) return;
        if (this.rubyBowGoal == null || this.rubyMeleeGoal == null) return; // 构造期字段还没初始化
        this.goalSelector.removeGoal(this.rubyBowGoal);
        this.goalSelector.removeGoal(this.rubyMeleeGoal);
        if (this.getMainHandItem().getItem() instanceof BowItem) {
            this.goalSelector.addGoal(4, this.rubyBowGoal);
        } else {
            this.goalSelector.addGoal(4, this.rubyMeleeGoal);
        }
    }

    // 玩家靠近(<4格)切红宝石剑近战；离远(>6格)切回弓（带迟滞，避免来回抖）
    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        LivingEntity target = this.getTarget();
        if (target != null) {
            double d2 = this.distanceToSqr(target);
            boolean holdingBow = this.getMainHandItem().getItem() instanceof BowItem;
            if (holdingBow && d2 < 16.0D) {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.RUBY_SWORD.get()));
                this.reassessWeaponGoal();
            } else if (!holdingBow && d2 > 36.0D) {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
                this.reassessWeaponGoal();
            }
        }
    }

    // 生成：给弓 + 每个部位低概率穿甲
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        MobSpawnType reason, SpawnGroupData data, CompoundTag tag) {
        SpawnGroupData result = super.finalizeSpawn(level, difficulty, reason, data, tag);
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));

        RandomSource random = this.getRandom();
        // 不给头盔（戴头盔会挡住阳光导致不燃烧），白天露天可稳定着火
        EquipmentSlot[] slots = { EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET };
        Item[][] armor = {
                { ModItems.RUBY_CHESTPLATE.get(), Items.IRON_CHESTPLATE },
                { ModItems.RUBY_LEGGINGS.get(), Items.IRON_LEGGINGS },
                { ModItems.RUBY_BOOTS.get(), Items.IRON_BOOTS },
        };
        for (int i = 0; i < slots.length; i++) {
            if (random.nextFloat() < 0.10F) {
                Item[] choices = armor[i];
                this.setItemSlot(slots[i], new ItemStack(choices[random.nextInt(choices.length)]));
                this.setDropChance(slots[i], 0.05F);
            }
        }
        this.reassessWeaponGoal();
        return result;
    }
}
