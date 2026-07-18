package net.tutorial.rubymod.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.tutorial.rubymod.entity.ModEntities;

public class RubySpiderEntity extends Spider {

    public RubySpiderEntity(EntityType<? extends Spider> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 16.0D)      // 和原版蜘蛛一样
                .add(Attributes.MOVEMENT_SPEED, 0.42D)  // 比原版(0.3)更快
                .add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        // 用普通目标(不带原版蜘蛛的白天判定)，所以白天也会主动攻击玩家
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    // 靠近玩家时，在玩家脚下放蜘蛛网
    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        LivingEntity target = this.getTarget();
        if (target != null && this.distanceToSqr(target) < 36.0D) { // 6格内
            if (this.random.nextInt(60) == 0) {                     // 偶尔放一次
                BlockPos pos = target.blockPosition();
                if (this.level().getBlockState(pos).isAir()) {
                    this.level().setBlock(pos, Blocks.COBWEB.defaultBlockState(), 3);
                }
            }
        }
    }

    // 出生时高概率背一个红宝石骷髅（蜘蛛骑士）
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        MobSpawnType reason, SpawnGroupData data, CompoundTag tag) {
        SpawnGroupData result = super.finalizeSpawn(level, difficulty, reason, data, tag);

        if (reason != MobSpawnType.MOB_SUMMONED && !this.isVehicle()
                && this.getRandom().nextFloat() < 0.6F) {                // 60% 概率
            RubySkeletonEntity rider = ModEntities.RUBY_SKELETON.get().create(this.level());
            if (rider != null) {
                rider.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                rider.finalizeSpawn(level, difficulty, MobSpawnType.JOCKEY, null, null);
                rider.startRiding(this);
                level.addFreshEntity(rider);
            }
        }
        return result;
    }
}
