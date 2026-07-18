package net.tutorial.rubymod.entity.client;

import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.SpiderEyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.tutorial.rubymod.RubyMod;
import net.tutorial.rubymod.entity.custom.RubySpiderEntity;

public class RubySpiderRenderer extends MobRenderer<RubySpiderEntity, SpiderModel<RubySpiderEntity>> {

    public RubySpiderRenderer(EntityRendererProvider.Context context) {
        super(context, new SpiderModel<>(context.bakeLayer(ModelLayers.SPIDER)), 0.75F);
        this.addLayer(new SpiderEyesLayer<>(this)); // 发光的红眼睛
    }

    @Override
    public ResourceLocation getTextureLocation(RubySpiderEntity entity) {
        return new ResourceLocation(RubyMod.MOD_ID, "textures/entity/ruby_spider.png");
    }
}
