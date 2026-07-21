package net.tutorial.rubymod.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AggroStaffItem extends Item {

    // 存在服务器里（按玩家ID记），不存物品NBT —— 这样创造模式也不会被刷掉
    private static final Map<UUID, UUID> FIRST_TARGET = new HashMap<>();
    private static final Map<UUID, Boolean> SPECIES_MODE = new HashMap<>();

    public AggroStaffItem(Properties properties) {
        super(properties);
    }

    // 右键空气：潜行=切换模式；普通=清空当前选择
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            UUID pid = player.getUUID();
            if (player.isShiftKeyDown()) {
                boolean species = !SPECIES_MODE.getOrDefault(pid, false);
                SPECIES_MODE.put(pid, species);
                FIRST_TARGET.remove(pid);
                player.displayClientMessage(Component.literal(species
                        ? "§e仇恨法杖 → 种族模式（同类一起开打）"
                        : "§e仇恨法杖 → 单体模式（只让两只开打）"), true);
            } else {
                FIRST_TARGET.remove(pid);
                player.displayClientMessage(Component.literal("§7已清空选择"), true);
            }
        }
        return InteractionResultHolder.success(stack);
    }

    // 右键生物：第一次选中A，第二次让A和B打起来
    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player,
                                                  LivingEntity target, InteractionHand hand) {
        Level level = player.level();
        if (level.isClientSide) return InteractionResult.SUCCESS;

        if (!(target instanceof Mob)) {
            player.displayClientMessage(Component.literal("§c这个目标不能被指挥"), true);
            return InteractionResult.SUCCESS;
        }

        UUID pid = player.getUUID();
        UUID firstId = FIRST_TARGET.get(pid);

        // 还没选第一个 → 记下它
        if (firstId == null) {
            FIRST_TARGET.put(pid, target.getUUID());
            player.displayClientMessage(Component.literal(
                    "§a已选中：" + target.getName().getString() + " §7（再右键另一个生物）"), true);
            level.playSound(null, target.getX(), target.getY(), target.getZ(),
                    SoundEvents.EVOKER_CAST_SPELL, SoundSource.PLAYERS, 0.8F, 1.4F);
            return InteractionResult.SUCCESS;
        }

        // 已有第一个 → 这次是 B
        FIRST_TARGET.remove(pid);
        Entity first = ((ServerLevel) level).getEntity(firstId);
        if (!(first instanceof Mob mobA)) {
            player.displayClientMessage(Component.literal("§c第一个目标不见了，请重新选"), true);
            return InteractionResult.SUCCESS;
        }
        Mob mobB = (Mob) target;
        if (mobA == mobB) {
            player.displayClientMessage(Component.literal("§c不能选同一只，重新选"), true);
            return InteractionResult.SUCCESS;
        }

        boolean species = SPECIES_MODE.getOrDefault(pid, false);
        if (species) {
            int n = makeSpeciesFight(level, player, mobA.getType(), mobB.getType());
            player.displayClientMessage(Component.literal("§6种族大战开始！参战 " + n + " 只生物"), true);
        } else {
            makeFight(mobA, mobB);
            player.displayClientMessage(Component.literal("§6开打！"), true);
        }

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.EVOKER_CAST_SPELL, SoundSource.PLAYERS, 1.0F, 0.8F);
        if (level instanceof ServerLevel sl) {
            spawnAngry(sl, mobA);
            spawnAngry(sl, mobB);
        }
        return InteractionResult.SUCCESS;
    }

    // 让两只互相仇恨
    private void makeFight(Mob a, Mob b) {
        forceAttack(a, b);
        forceAttack(b, a);
    }

    private void forceAttack(Mob attacker, Mob victim) {
        attacker.setTarget(victim);
        attacker.setLastHurtByMob(victim);
        attacker.setAggressive(true);
    }

    // 种族模式：附近所有 A 类去打最近的 B 类，反之亦然
    private int makeSpeciesFight(Level level, Player player, EntityType<?> typeA, EntityType<?> typeB) {
        AABB area = player.getBoundingBox().inflate(40.0D);
        List<Mob> all = level.getEntitiesOfClass(Mob.class, area);
        List<Mob> as = new ArrayList<>();
        List<Mob> bs = new ArrayList<>();
        for (Mob m : all) {
            if (m.getType() == typeA) as.add(m);
            else if (m.getType() == typeB) bs.add(m);
        }
        for (Mob a : as) {
            Mob t = nearest(a, bs);
            if (t != null) forceAttack(a, t);
        }
        for (Mob b : bs) {
            Mob t = nearest(b, as);
            if (t != null) forceAttack(b, t);
        }
        return as.size() + bs.size();
    }

    private Mob nearest(Mob from, List<Mob> list) {
        Mob best = null;
        double bd = Double.MAX_VALUE;
        for (Mob m : list) {
            double d = from.distanceToSqr(m);
            if (d < bd) { bd = d; best = m; }
        }
        return best;
    }

    private void spawnAngry(ServerLevel level, Mob m) {
        level.sendParticles(ParticleTypes.ANGRY_VILLAGER,
                m.getX(), m.getEyeY() + 0.5D, m.getZ(), 8, 0.3D, 0.3D, 0.3D, 0.0D);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tip, TooltipFlag flag) {
        tip.add(Component.literal("右键生物A，再右键生物B → 它俩开打")
                .withStyle(ChatFormatting.GRAY));
        tip.add(Component.literal("潜行+右键空地 → 切换 单体/种族 模式")
                .withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, level, tip, flag);
    }
}
