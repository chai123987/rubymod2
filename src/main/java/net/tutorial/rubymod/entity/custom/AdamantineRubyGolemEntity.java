package net.tutorial.rubymod.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
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
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.tutorial.rubymod.entity.ModEntities;
import net.tutorial.rubymod.item.ModItems;

public class AdamantineRubyGolemEntity extends Monster {

    // BOSS 血条
    private final ServerBossEvent bossEvent =
            new ServerBossEvent(this.getDisplayName(),
                    BossEvent.BossBarColor.RED,
                    BossEvent.BossBarOverlay.PROGRESS);

    // 是否已经召唤过援军（只触发一次）
    private boolean summonedReinforcements = false;

    public AdamantineRubyGolemEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.bossEvent.setDarkenScreen(true); // 出现时屏幕变暗，更有压迫感
        this.xpReward = 100; // 击杀给大量经验
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 300.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.FOLLOW_RANGE, 40.0D)
                .add(Attributes.ARMOR, 12.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D); // 几乎不会被击退
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 16.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    // 出生时穿上整套红宝石盔甲 + 手持红宝石剑
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        MobSpawnType reason, SpawnGroupData data, CompoundTag tag) {
        SpawnGroupData result = super.finalizeSpawn(level, difficulty, reason, data, tag);

        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.RUBY_SWORD.get()));
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(ModItems.RUBY_HELMET.get()));
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(ModItems.RUBY_CHESTPLATE.get()));
        this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(ModItems.RUBY_LEGGINGS.get()));
        this.setItemSlot(EquipmentSlot.FEET, new ItemStack(ModItems.RUBY_BOOTS.get()));

        // 身上的装备不会随机掉落（改用下面的自定义掉落）
        this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
        this.setDropChance(EquipmentSlot.HEAD, 0.0F);
        this.setDropChance(EquipmentSlot.CHEST, 0.0F);
        this.setDropChance(EquipmentSlot.LEGS, 0.0F);
        this.setDropChance(EquipmentSlot.FEET, 0.0F);

        return result;
    }

    // 近战攻击：在普通伤害之外，额外造成 5 点【无视盔甲】的伤害
    @Override
    public boolean doHurtTarget(Entity target) {
        boolean flag = super.doHurtTarget(target);
        if (flag && target instanceof LivingEntity living) {
            living.hurt(this.damageSources().magic(), 5.0F);
        }
        return flag;
    }

    @Override
    public void tick() {
        super.tick();
        // 血量低于一半时，召唤一波小弟（只触发一次）
        if (!this.level().isClientSide() && !this.summonedReinforcements
                && this.getHealth() < this.getMaxHealth() / 2.0F) {
            this.summonedReinforcements = true;
            this.summonReinforcements();
        }
    }

    private void summonReinforcements() {
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }
        summonOne(serverLevel, ModEntities.RUBY_SKELETON.get());
        summonOne(serverLevel, ModEntities.RUBY_SKELETON.get());
        summonOne(serverLevel, ModEntities.RUBY_CREEPER.get());
        summonOne(serverLevel, ModEntities.RUBY_GOLEM.get());
        summonOne(serverLevel, ModEntities.RUBY_GOLEM.get());

        // 召唤时的音效 + 提示
        this.level().playSound(null, this.blockPosition(),
                SoundEvents.WITHER_SPAWN, this.getSoundSource(), 1.0F, 1.0F);
    }

    private void summonOne(ServerLevel serverLevel, EntityType<? extends Mob> type) {
        Mob minion = type.create(serverLevel);
        if (minion == null) {
            return;
        }
        double x = this.getX() + (this.random.nextDouble() - 0.5) * 4.0;
        double z = this.getZ() + (this.random.nextDouble() - 0.5) * 4.0;
        minion.moveTo(x, this.getY(), z, this.getYRot(), 0.0F);
        minion.finalizeSpawn(serverLevel,
                serverLevel.getCurrentDifficultyAt(minion.blockPosition()),
                MobSpawnType.MOB_SUMMONED, null, null);
        // 让小弟去打 BOSS 当前的目标
        if (this.getTarget() != null) {
            minion.setTarget(this.getTarget());
        }
        serverLevel.addFreshEntity(minion);
    }

    // 死亡掉落：大量红宝石 + 钻石 + 绿宝石 + 金锭
    @Override
    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit) {
        super.dropCustomDeathLoot(source, looting, recentlyHit);
        this.spawnAtLocation(new ItemStack(ModItems.RUBY.get(), 10 + this.random.nextInt(11))); // 10~20
        this.spawnAtLocation(new ItemStack(Items.DIAMOND, 5 + this.random.nextInt(6)));          // 5~10
        this.spawnAtLocation(new ItemStack(Items.EMERALD, 5 + this.random.nextInt(6)));          // 5~10
        this.spawnAtLocation(new ItemStack(Items.GOLD_INGOT, 5 + this.random.nextInt(6)));       // 5~10
        // 还会掉一套它穿的红宝石装备
        this.spawnAtLocation(new ItemStack(ModItems.RUBY_SWORD.get()));
        this.spawnAtLocation(new ItemStack(ModItems.RUBY_HELMET.get()));
        this.spawnAtLocation(new ItemStack(ModItems.RUBY_CHESTPLATE.get()));
        this.spawnAtLocation(new ItemStack(ModItems.RUBY_LEGGINGS.get()));
        this.spawnAtLocation(new ItemStack(ModItems.RUBY_BOOTS.get()));
    }

    // BOSS 不会因为玩家走远而自动消失
    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }

    // ===== BOSS 血条：玩家看到/离开时显示或隐藏 =====
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
    protected void customServerAiStep() {
        super.customServerAiStep();
        // 让血条跟随血量
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }

    @Override
    public void setCustomName(Component name) {
        super.setCustomName(name);
        this.bossEvent.setName(this.getDisplayName());
    }

    // ===== 音效：用劫掠兽的吼叫 + 凋灵死亡音，和普通红宝石傀儡(僵尸音)完全不同 =====
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.RAVAGER_AMBIENT;
    }

    @Override
    public float getVoicePitch() {
        return 0.6F; // 压低音调，更厚重
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.RAVAGER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WITHER_DEATH; // 史诗级 BOSS 死亡音
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.RAVAGER_STEP, 1.0F, 0.7F);
    }
}
