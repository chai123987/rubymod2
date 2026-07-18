package net.tutorial.rubymod.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tutorial.rubymod.RubyMod;
import net.tutorial.rubymod.entity.ModEntities;
import net.tutorial.rubymod.entity.client.ModModelLayers;
import net.tutorial.rubymod.entity.client.RubyCreeperRenderer;
import net.tutorial.rubymod.entity.client.RubySkeletonRenderer;
import net.tutorial.rubymod.entity.client.RubyGolemModel;
import net.tutorial.rubymod.entity.client.RubyGolemRenderer;
import net.tutorial.rubymod.entity.client.AdamantineRubyGolemRenderer;
import net.tutorial.rubymod.entity.client.RubySpiderRenderer;
import net.tutorial.rubymod.entity.client.RubyEvokerRenderer;
import net.tutorial.rubymod.entity.client.RubyEvokerFangsRenderer;
import net.tutorial.rubymod.entity.client.RubyPillagerRenderer;
import net.tutorial.rubymod.entity.client.RubyPlayerRenderer;

// value = Dist.CLIENT：整个类只在客户端加载，专门处理渲染注册
@Mod.EventBusSubscriber(modid = RubyMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientEvents {

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.RUBY_GOLEM, RubyGolemModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.RUBY_GOLEM.get(), RubyGolemRenderer::new);
        event.registerEntityRenderer(ModEntities.RUBY_CREEPER.get(), RubyCreeperRenderer::new);
        event.registerEntityRenderer(ModEntities.RUBY_SKELETON.get(), RubySkeletonRenderer::new);
        event.registerEntityRenderer(ModEntities.ADAMANTINE_RUBY_GOLEM.get(), AdamantineRubyGolemRenderer::new);
        event.registerEntityRenderer(ModEntities.RUBY_SPIDER.get(), RubySpiderRenderer::new);
        event.registerEntityRenderer(ModEntities.RUBY_EVOKER.get(), RubyEvokerRenderer::new);
        event.registerEntityRenderer(ModEntities.RUBY_EVOKER_FANGS.get(), RubyEvokerFangsRenderer::new);
        event.registerEntityRenderer(ModEntities.RUBY_PILLAGER.get(), RubyPillagerRenderer::new);
        event.registerEntityRenderer(ModEntities.RUBY_PLAYER.get(), RubyPlayerRenderer::new);
    }
}
