package net.tutorial.rubymod;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.tutorial.rubymod.block.ModBlocks;
import net.tutorial.rubymod.entity.ModEntities;
import net.tutorial.rubymod.item.ModCreativeModeTabs;
import net.tutorial.rubymod.item.ModItems;
import net.tutorial.rubymod.item.ModToolTiers;
import org.slf4j.Logger;

import java.util.List;

@Mod(RubyMod.MOD_ID)
public class RubyMod {
    public static final String MOD_ID = "rubymod";
    public static final Logger LOGGER = LogUtils.getLogger();

    public RubyMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModCreativeModeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModEntities.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // 把红宝石等级排在钻石之后、下界合金之前，挖掘判定才正确
            TierSortingRegistry.registerTier(
                    ModToolTiers.RUBY,
                    new ResourceLocation(MOD_ID, "ruby"),
                    List.of(Tiers.DIAMOND),
                    List.of(Tiers.NETHERITE)
            );
        });
        LOGGER.info("RubyMod common setup complete");
    }
}
