package net.tutorial.rubymod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.tutorial.rubymod.RubyMod;
import net.tutorial.rubymod.entity.custom.AdamantineRubyGolemEntity;

// 用人形模型放大显示，带盔甲层和手持武器层。
public class AdamantineRubyGolemRenderer
        extends HumanoidMobRenderer<AdamantineRubyGolemEntity, HumanoidModel<AdamantineRubyGolemEntity>> {

    public AdamantineRubyGolemRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.ZOMBIE)), 0.9F);

        // 穿戴的盔甲
        this.addLayer(new HumanoidArmorLayer<>(this,
                new HumanoidModel<>(context.bakeLayer(ModelLayers.ZOMBIE_INNER_ARMOR)),
                new HumanoidModel<>(context.bakeLayer(ModelLayers.ZOMBIE_OUTER_ARMOR)),
                context.getModelManager()));

        // 手持武器
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    // 放大模型到铁傀儡大小
    @Override
    protected void scale(AdamantineRubyGolemEntity entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(1.8F, 1.8F, 1.8F);
    }

    @Override
    public ResourceLocation getTextureLocation(AdamantineRubyGolemEntity entity) {
        return new ResourceLocation(RubyMod.MOD_ID, "textures/entity/adamantine_ruby_golem.png");
    }
}
