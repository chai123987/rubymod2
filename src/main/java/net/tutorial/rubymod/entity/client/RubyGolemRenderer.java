package net.tutorial.rubymod.entity.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.tutorial.rubymod.RubyMod;
import net.tutorial.rubymod.entity.custom.RubyGolemEntity;

public class RubyGolemRenderer extends HumanoidMobRenderer<RubyGolemEntity, HumanoidModel<RubyGolemEntity>> {

    public RubyGolemRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.ZOMBIE)), 0.5F);

        // 显示穿戴的盔甲
        this.addLayer(new HumanoidArmorLayer<>(this,
                new HumanoidModel<>(context.bakeLayer(ModelLayers.ZOMBIE_INNER_ARMOR)),
                new HumanoidModel<>(context.bakeLayer(ModelLayers.ZOMBIE_OUTER_ARMOR)),
                context.getModelManager()));

        // 显示手持的武器
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(RubyGolemEntity entity) {
        return new ResourceLocation(RubyMod.MOD_ID, "textures/entity/ruby_golem.png");
    }
}
