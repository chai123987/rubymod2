package net.tutorial.rubymod.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tutorial.rubymod.RubyMod;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, RubyMod.MOD_ID);


    public static final RegistryObject<Block> RUBY_BLOCK =
            BLOCKS.register("ruby_block",
                    () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIAMOND_BLOCK)
                            .sound(SoundType.AMETHYST)));

    public static final RegistryObject<Block> RUBY_ORE =
            BLOCKS.register("ruby_ore",
                    () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIAMOND_ORE)));

}
