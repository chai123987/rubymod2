package net.tutorial.rubymod;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.tutorial.rubymod.block.ModBlocks;
import net.tutorial.rubymod.entity.ModEntities;
import net.tutorial.rubymod.item.ModItems;

@Mod(RubyMod.MOD_ID)
public class RubyMod {

    public static final String MOD_ID = "rubymod";

    public RubyMod() {

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        // 注册方块
        ModBlocks.BLOCKS.register(bus);

        // 注册物品
        ModItems.register(bus);

        // 注册实体
        ModEntities.register(bus);

    }
}
