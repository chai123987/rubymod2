package net.tutorial.rubymod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.tutorial.rubymod.RubyMod;
import net.tutorial.rubymod.entity.custom.RubyEvokerEntity;

public class RubyEvokerRenderer extends MobRenderer<RubyEvokerEntity, IllagerModel<RubyEvokerEntity>> {

    public RubyEvokerRenderer(EntityRendererProvider.Context context) {
        super(context, new IllagerModel<>(context.bakeLayer(ModelLayers.EVOKER)), 0.5F);
    }

    @Override
    protected void scale(RubyEvokerEntity entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(0.9375F, 0.9375F, 0.9375F); // illager 标准缩放
    }

    @Override
    public ResourceLocation getTextureLocation(RubyEvokerEntity entity) {
        return new ResourceLocation(RubyMod.MOD_ID, "textures/entity/ruby_evoker.png");
    }
}
