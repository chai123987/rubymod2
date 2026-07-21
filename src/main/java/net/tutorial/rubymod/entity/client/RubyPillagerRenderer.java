package net.tutorial.rubymod.entity.client;

import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.IllagerRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.tutorial.rubymod.RubyMod;
import net.tutorial.rubymod.entity.custom.RubyPillagerEntity;

// 和原版掠夺者一样的渲染：illager模型 + 手持武器层（弩/斧）
public class RubyPillagerRenderer extends IllagerRenderer<RubyPillagerEntity> {

    public RubyPillagerRenderer(EntityRendererProvider.Context context) {
        super(context, new IllagerModel<>(context.bakeLayer(ModelLayers.PILLAGER)), 0.5F);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(RubyPillagerEntity entity) {
        return new ResourceLocation(RubyMod.MOD_ID, "textures/entity/ruby_pillager.png");
    }
}
