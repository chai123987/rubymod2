package net.tutorial.rubymod.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tutorial.rubymod.RubyMod;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, RubyMod.MOD_ID);


    // 红宝石块
    public static final RegistryObject<Block> RUBY_BLOCK =
            BLOCKS.register("ruby_block",
                    () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIAMOND_BLOCK)
                            .sound(SoundType.AMETHYST)));


    // 红宝石矿
    public static final RegistryObject<Block> RUBY_ORE =
            BLOCKS.register("ruby_ore",
                    () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIAMOND_ORE)));


    // 红宝石树树干
    public static final RegistryObject<Block> RUBY_LOG =
            BLOCKS.register("ruby_log",
                    () -> new RotatedPillarBlock(
                            BlockBehaviour.Properties.copy(Blocks.OAK_LOG)
                    ));


    // 红宝石树树叶
    public static final RegistryObject<Block> RUBY_LEAVES =
            BLOCKS.register("ruby_leaves",
                    () -> new LeavesBlock(
                            BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)
                    ));

}
