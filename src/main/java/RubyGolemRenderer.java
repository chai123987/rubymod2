package net.tutorial.rubymod.item;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tutorial.rubymod.RubyMod;
import net.tutorial.rubymod.entity.ModEntities;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, RubyMod.MOD_ID);

    // 红宝石
    public static final RegistryObject<Item> RUBY = ITEMS.register("ruby",
            () -> new Item(new Item.Properties()));

    // 红宝石剑（用铁等级）
    public static final RegistryObject<Item> RUBY_SWORD = ITEMS.register("ruby_sword",
            () -> new SwordItem(Tiers.IRON, 3, -2.4F, new Item.Properties()));

    // 红宝石镐（自定义红宝石等级：速度10、耐久2000）
    public static final RegistryObject<Item> RUBY_PICKAXE = ITEMS.register("ruby_pickaxe",
            () -> new PickaxeItem(ModToolTiers.RUBY, 1, -2.8F, new Item.Properties()));

    // 红宝石斧（自定义红宝石等级，伤害/速度同钻石斧）
    public static final RegistryObject<Item> RUBY_AXE = ITEMS.register("ruby_axe",
            () -> new AxeItem(ModToolTiers.RUBY, 5.0F, -3.0F, new Item.Properties()));

    // 红宝石盔甲：头盔、胸甲、护腿、靴子
    public static final RegistryObject<Item> RUBY_HELMET = ITEMS.register("ruby_helmet",
            () -> new ArmorItem(ModArmorMaterials.RUBY, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> RUBY_CHESTPLATE = ITEMS.register("ruby_chestplate",
            () -> new ArmorItem(ModArmorMaterials.RUBY, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> RUBY_LEGGINGS = ITEMS.register("ruby_leggings",
            () -> new ArmorItem(ModArmorMaterials.RUBY, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> RUBY_BOOTS = ITEMS.register("ruby_boots",
            () -> new ArmorItem(ModArmorMaterials.RUBY, ArmorItem.Type.BOOTS, new Item.Properties()));

    // 红宝石傀儡刷怪蛋
    public static final RegistryObject<Item> RUBY_GOLEM_SPAWN_EGG = ITEMS.register("ruby_golem_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.RUBY_GOLEM, 0xB0171F, 0x7A0C14,
                    new Item.Properties()));

    // 红宝石苦力怕刷怪蛋
    public static final RegistryObject<Item> RUBY_CREEPER_SPAWN_EGG = ITEMS.register("ruby_creeper_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.RUBY_CREEPER, 0xC42A2A, 0x5A0A0A,
                    new Item.Properties()));

    // 红宝石骷髅刷怪蛋
    public static final RegistryObject<Item> RUBY_SKELETON_SPAWN_EGG = ITEMS.register("ruby_skeleton_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.RUBY_SKELETON, 0xD8D8D8, 0x9B1B1B,
                    new Item.Properties()));

    // 艾德曼红宝石傀儡（BOSS）刷怪蛋
    public static final RegistryObject<Item> ADAMANTINE_RUBY_GOLEM_SPAWN_EGG = ITEMS.register("adamantine_ruby_golem_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.ADAMANTINE_RUBY_GOLEM, 0x8B0000, 0xFFD700,
                    new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
