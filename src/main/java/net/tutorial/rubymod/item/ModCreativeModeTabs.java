package net.tutorial.rubymod.item;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tutorial.rubymod.RubyMod;
import net.tutorial.rubymod.block.ModBlocks;

@Mod.EventBusSubscriber(modid = RubyMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCreativeModeTabs {

    @SubscribeEvent
    public static void addCreative(BuildCreativeModeTabContentsEvent event) {

        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {

            event.accept(ModBlocks.RUBY_BLOCK.get());
            event.accept(ModBlocks.RUBY_ORE.get());

        }
    }
}
