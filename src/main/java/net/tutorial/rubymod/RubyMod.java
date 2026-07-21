package net.tutorial.rubymod;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.tutorial.rubymod.entity.ModEntities;

@Mod(RubyMod.MOD_ID)
public class RubyMod {

    public static final String MOD_ID = "rubymod";

    public RubyMod() {

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ModEntities.register(bus);

    }
}
