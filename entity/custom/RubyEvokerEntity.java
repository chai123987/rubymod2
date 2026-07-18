package net.tutorial.rubymod.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.SpellcasterIllager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import net.tutorial.rubymod.entity.ModEntities;

public class RubyEvokerEntity extends SpellcasterIllager {

    public RubyEvokerEntity(EntityType<? extends RubyEvokerEntity> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 10;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.5D)
                .add(Attributes.FOLLOW_RANGE, 16.0D)
                .add(Attributes.MAX_HEALTH, 24.0D); // 和原版唤魔者一样
    }

    @Override
    protected void registerGoals() {
        // 不调用 super 的唤魔者 AI（那会加上恼鬼召唤和尖刺），自己装
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SpellcasterCastingSpellGoal());
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Player.class, 8.0F, 0.6D, 1.0D));
        this.goalSelector.addGoal(4, new RubySummonSpellGoal());
        this.goalSelector.addGoal(5, new RubyFangsSpellGoal());
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, 0.6D));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));

        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Raider.class)).setAlertOthers());
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, Player.class, true)).setUnseenMemoryTicks(300));
    }

    // 召唤一个小弟
    private void summonMinion(ServerLevel level, EntityType<? extends Mob> type) {
        BlockPos pos = this.blockPosition().offset(-2 + this.random.nextInt(5), 0, -2 + this.random.nextInt(5));
        Mob mob = type.create(level);
        if (mob != null) {
            mob.moveTo(pos, 0.0F, 0.0F);
            mob.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), MobSpawnType.MOB_SUMMONED, null, null);
            if (this.getTarget() != null) {
                mob.setTarget(this.getTarget());
            }
            level.addFreshEntity(mob);
        }
    }

    // 在指定位置生成一根红色尖刺
    private void createFang(double x, double z, double y, float yRot) {
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.addFreshEntity(
                    RubyEvokerFangsEntity.create(serverLevel, x, y, z, yRot, this));
        }
    }

    @Override
    public void applyRaidBuffs(int wave, boolean unused) {
        // 不参与袭击增益，留空
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.EVOKER_CELEBRATE;
    }

    @Override
    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.EVOKER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(net.minecraft.world.damagesource.DamageSource source) {
        return SoundEvents.EVOKER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.EVOKER_DEATH;
    }

    // ===== 自定义召唤法术：召唤红宝石骷髅 ×3 + 红宝石苦力怕 ×1 =====
    class RubySummonSpellGoal extends SpellcasterIllager.SpellcasterUseSpellGoal {

        @Override
        protected void performSpellCasting() {
            if (!(RubyEvokerEntity.this.level() instanceof ServerLevel serverLevel)) {
                return;
            }
            RubyEvokerEntity.this.summonMinion(serverLevel, ModEntities.RUBY_SKELETON.get());
            RubyEvokerEntity.this.summonMinion(serverLevel, ModEntities.RUBY_SKELETON.get());
            RubyEvokerEntity.this.summonMinion(serverLevel, ModEntities.RUBY_GOLEM.get());
            RubyEvokerEntity.this.summonMinion(serverLevel, ModEntities.RUBY_GOLEM.get());
        }

        @Override
        protected int getCastingTime() {
            return 100;
        }

        @Override
        protected int getCastingInterval() {
            return 340;
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }

        @Override
        protected SpellcasterIllager.IllagerSpell getSpell() {
            return SpellcasterIllager.IllagerSpell.SUMMON_VEX; // 借用召唤动画/粒子
        }
    }

    // ===== 尖刺法术：突发一排/一圈红色尖刺（和原版唤魔者的尖刺一样）=====
    class RubyFangsSpellGoal extends SpellcasterIllager.SpellcasterUseSpellGoal {

        @Override
        protected void performSpellCasting() {
            LivingEntity target = RubyEvokerEntity.this.getTarget();
            if (target == null) {
                return;
            }
            double baseY = Math.min(target.getY(), RubyEvokerEntity.this.getY());
            float angle = (float) Mth.atan2(target.getZ() - RubyEvokerEntity.this.getZ(),
                    target.getX() - RubyEvokerEntity.this.getX());

            if (RubyEvokerEntity.this.distanceToSqr(target) < 9.0D) {
                // 近身：身边扇形一圈
                for (int i = 0; i < 5; i++) {
                    float a = angle + (float) i * (float) Math.PI * 0.4F;
                    RubyEvokerEntity.this.createFang(
                            RubyEvokerEntity.this.getX() + (double) Mth.cos(a) * 1.5D,
                            RubyEvokerEntity.this.getZ() + (double) Mth.sin(a) * 1.5D, baseY, a);
                }
            } else {
                // 远处：朝目标方向射出一条直线
                for (int l = 0; l < 16; l++) {
                    double dist = 1.25D * (double) (l + 1);
                    RubyEvokerEntity.this.createFang(
                            RubyEvokerEntity.this.getX() + (double) Mth.cos(angle) * dist,
                            RubyEvokerEntity.this.getZ() + (double) Mth.sin(angle) * dist, baseY, angle);
                }
            }
        }

        @Override
        protected int getCastingTime() {
            return 40;
        }

        @Override
        protected int getCastingInterval() {
            return 100;
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        @Override
        protected SpellcasterIllager.IllagerSpell getSpell() {
            return SpellcasterIllager.IllagerSpell.FANGS;
        }
    }
}
