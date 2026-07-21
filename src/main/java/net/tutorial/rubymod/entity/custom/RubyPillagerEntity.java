package net.tutorial.rubymod.entity.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RangedCrossbowAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.tutorial.rubymod.item.ModItems;

public class RubyPillagerEntity extends Pillager {

    // 弩/近战目标（和骷髅同一套切换思路）
    private final RangedCrossbowAttackGoal<RubyPillagerEntity> rubyCrossbowGoal =
            new RangedCrossbowAttackGoal<>(this, 1.0D, 8.0F);
    private final MeleeAttackGoal rubyMeleeGoal = new MeleeAttackGoal(this, 1.2D, false);
    private Boolean goalIsCrossbow = null;   // 当前装的是不是“弩目标”

    public RubyPillagerEntity(EntityType<? extends Pillager> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)      // 比原版掠夺者(24)高一点
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    // 这里只放“不涉及弩/近战字段”的目标（registerGoals 在父类构造期就被调用，
    // 那时弩/近战字段还没初始化，不能在这里用）。攻击目标交给 customServerAiStep 同步。
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, 0.6D));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 15.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 15.0F));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
    }

    // 一把“快速装填III”的弩 → 装填更快
    private ItemStack makeCrossbow() {
        ItemStack cb = new ItemStack(Items.CROSSBOW);
        cb.enchant(Enchantments.QUICK_CHARGE, 3);
        return cb;
    }

    // 根据手里拿的是弩还是斧，装上对应的攻击目标
    private void reassessCrossbowGoal() {
        if (this.level() == null || this.level().isClientSide) return;
        if (this.rubyCrossbowGoal == null || this.rubyMeleeGoal == null) return;
        this.goalSelector.removeGoal(this.rubyCrossbowGoal);
        this.goalSelector.removeGoal(this.rubyMeleeGoal);
        if (this.getMainHandItem().getItem() instanceof CrossbowItem) {
            this.goalSelector.addGoal(2, this.rubyCrossbowGoal);
        } else {
            this.goalSelector.addGoal(2, this.rubyMeleeGoal);
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();

        // 玩家靠近(<4格)拿红宝石斧近战；离远(>6格)切回弩（迟滞防抖）
        LivingEntity target = this.getTarget();
        if (target != null) {
            double d2 = this.distanceToSqr(target);
            boolean holdingCrossbow = this.getMainHandItem().getItem() instanceof CrossbowItem;
            if (holdingCrossbow && d2 < 16.0D) {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.RUBY_AXE.get()));
                this.setChargingCrossbow(false);
            } else if (!holdingCrossbow && d2 > 36.0D) {
                this.setItemSlot(EquipmentSlot.MAINHAND, makeCrossbow());
            }
        }

        // 攻击目标与手持武器保持一致（仅在武器类型变化时切换，保留弩的蓄力状态）
        boolean nowCrossbow = this.getMainHandItem().getItem() instanceof CrossbowItem;
        if (this.goalIsCrossbow == null || this.goalIsCrossbow != nowCrossbow) {
            this.reassessCrossbowGoal();
            this.goalIsCrossbow = nowCrossbow;
        }
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        MobSpawnType reason, SpawnGroupData data, CompoundTag tag) {
        SpawnGroupData result = super.finalizeSpawn(level, difficulty, reason, data, tag);
        this.setItemSlot(EquipmentSlot.MAINHAND, makeCrossbow());
        this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
        return result;
    }
}
