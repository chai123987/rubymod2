package net.tutorial.rubymod.entity.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.tutorial.rubymod.item.ModItems;

public class RubyPlayerEntity extends Monster implements RangedAttackMob {

    // 弓(远) / 红宝石斧(近) 切换
    private final RangedBowAttackGoal<RubyPlayerEntity> bowGoal =
            new RangedBowAttackGoal<>(this, 1.0D, 20, 15.0F);
    private final MeleeAttackGoal meleeGoal = new MeleeAttackGoal(this, 1.2D, false);
    private Boolean goalIsBow = null;

    // BOSS 血条
    private final ServerBossEvent bossEvent =
            new ServerBossEvent(this.getDisplayName(),
                    BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);

    private int eatCooldown = 0;   // 吃金苹果冷却

    public RubyPlayerEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.xpReward = 30;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)        // 跟玩家一样
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)      // 加上红宝石斧会更高
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.4D)
                .add(Attributes.ARMOR, 4.0D);
    }

    // 不在这里碰 bow/melee 字段（构造期会先调用本方法，那时字段还没初始化）
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    private void reassessWeapon() {
        if (this.level() == null || this.level().isClientSide) return;
        if (this.bowGoal == null || this.meleeGoal == null) return;
        this.goalSelector.removeGoal(this.bowGoal);
        this.goalSelector.removeGoal(this.meleeGoal);
        if (this.getMainHandItem().getItem() instanceof BowItem) {
            this.goalSelector.addGoal(4, this.bowGoal);
        } else {
            this.goalSelector.addGoal(4, this.meleeGoal);
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();

        // 近(<4格)拿斧，远(>6格)拿弓
        LivingEntity target = this.getTarget();
        if (target != null) {
            double d2 = this.distanceToSqr(target);
            boolean holdingBow = this.getMainHandItem().getItem() instanceof BowItem;
            if (holdingBow && d2 < 16.0D) {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.RUBY_AXE.get()));
            } else if (!holdingBow && d2 > 36.0D) {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
            }
        }

        // 攻击目标与手持武器保持一致（仅在武器类型变化时切，保留拉弓蓄力状态）
        boolean nowBow = this.getMainHandItem().getItem() instanceof BowItem;
        if (this.goalIsBow == null || this.goalIsBow != nowBow) {
            this.reassessWeapon();
            this.goalIsBow = nowBow;
        }

        // 血量低于一半时吃金苹果回血（用效果模拟，有冷却）
        if (this.eatCooldown > 0) this.eatCooldown--;
        if (this.eatCooldown == 0 && this.getHealth() < this.getMaxHealth() * 0.5F) {
            this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 1));
            this.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 2400, 0));
            this.playSound(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
            this.playSound(SoundEvents.PLAYER_BURP, 0.8F, 1.0F);
            this.eatCooldown = 600;   // 30秒一次
        }

        // 更新 BOSS 血条
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }

    // 远程：射箭（和骷髅一样）
    @Override
    public void performRangedAttack(LivingEntity target, float velocity) {
        ItemStack arrowStack = new ItemStack(Items.ARROW);
        AbstractArrow arrow = ProjectileUtil.getMobArrow(this, arrowStack, velocity);
        double dx = target.getX() - this.getX();
        double dy = target.getY(0.3333333D) - arrow.getY();
        double dz = target.getZ() - this.getZ();
        double dist = Math.sqrt(dx * dx + dz * dz);
        arrow.shoot(dx, dy + dist * 0.2D, dz, 1.6F, 6.0F);
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F,
                1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(arrow);
    }

    // 近战命中：拿斧时把玩家的盾打掉（破盾）
    @Override
    public boolean doHurtTarget(Entity target) {
        boolean hurt = super.doHurtTarget(target);
        if (hurt && target instanceof Player player
                && this.getMainHandItem().getItem() instanceof AxeItem
                && player.isBlocking()) {
            player.getCooldowns().addCooldown(Items.SHIELD, 100);  // 盾进5秒冷却
            player.stopUsingItem();
            this.level().broadcastEntityEvent(player, (byte) 30);  // 破盾音效/粒子
        }
        return hurt;
    }

    // ===== BOSS 血条显示 =====
    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
    }

    @Override
    public void setCustomName(net.minecraft.network.chat.Component name) {
        super.setCustomName(name);
        this.bossEvent.setName(this.getDisplayName());
    }

    @Override
    public boolean removeWhenFarAway(double dist) {
        return false;   // BOSS 不会自然消失
    }

    // 出生：穿整套红宝石甲 + 默认拿弓
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        MobSpawnType reason, SpawnGroupData data, CompoundTag tag) {
        SpawnGroupData result = super.finalizeSpawn(level, difficulty, reason, data, tag);
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(ModItems.RUBY_HELMET.get()));
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(ModItems.RUBY_CHESTPLATE.get()));
        this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(ModItems.RUBY_LEGGINGS.get()));
        this.setItemSlot(EquipmentSlot.FEET, new ItemStack(ModItems.RUBY_BOOTS.get()));
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            this.setDropChance(slot, 0.0F);
        }
        return result;
    }
}
