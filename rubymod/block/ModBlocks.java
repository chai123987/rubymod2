package net.tutorial.rubymod.block;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.tutorial.rubymod.worldgen.tree.RubyTreeGrower;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tutorial.rubymod.RubyMod;
import net.tutorial.rubymod.item.ModItems;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, RubyMod.MOD_ID);

    // 1) 红宝石块（储存方块）
    public static final RegistryObject<Block> RUBY_BLOCK = registerBlock("ruby_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .requiresCorrectToolForDrops()));

    // 2) 红宝石矿石（破坏掉落红宝石 + 经验）
    public static final RegistryObject<Block> RUBY_ORE = registerBlock("ruby_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)
                    .requiresCorrectToolForDrops(), UniformInt.of(3, 7)));

    // 3) 深层红宝石矿石
    public static final RegistryObject<Block> DEEPSLATE_RUBY_ORE = registerBlock("deepslate_ruby_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_IRON_ORE)
                    .requiresCorrectToolForDrops(), UniformInt.of(3, 7)));

    // 4) 红宝石草方块（只在红宝石群系当地表，做好后由群系生成）
    public static final RegistryObject<Block> RUBY_GRASS_BLOCK = registerBlock("ruby_grass_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.GRASS_BLOCK)));

    // 5) 红宝石原木（树干）——RotatedPillarBlock 才有 axis 朝向，配方里用得到
    public static final RegistryObject<Block> RUBY_LOG = registerBlock("ruby_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));

    // 6) 红宝石树叶（树冠）——LeavesBlock 自带掉落树苗/会随距离消失等行为
    public static final RegistryObject<Block> RUBY_LEAVES = registerBlock("ruby_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));

    // 7) 红宝石树苗——SaplingBlock + 我们的 RubyTreeGrower，骨粉催熟就长成红宝石树
    public static final RegistryObject<Block> RUBY_SAPLING = registerBlock("ruby_sapling",
            () -> new SaplingBlock(new RubyTreeGrower(),
                    BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));

    // 8) 红宝石木板——由红宝石原木分解而成（普通方块，复制橡木板属性）
    public static final RegistryObject<Block> RUBY_PLANKS = registerBlock("ruby_planks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));

    // 注册方块的同时，自动注册一个对应的 BlockItem（这样背包里才有这个方块）
    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
