package net.tutorial.rubymod.entity.client;

import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.tutorial.rubymod.RubyMod;
import net.tutorial.rubymod.entity.custom.RubySkeletonEntity;

// 跟红宝石傀儡渲染器用同一套成熟写法（已验证能编译），
// 模型换成骷髅模型，这样会正确摆出持弓姿势，贴图换成红色骷髅。
public class RubySkeletonRenderer extends HumanoidMobRenderer<RubySkeletonEntity, SkeletonModel<RubySkeletonEntity>> {

    public RubySkeletonRenderer(EntityRendererProvider.Context context) {
        super(context, new SkeletonModel<>(context.bakeLayer(ModelLayers.SKELETON)), 0.5F);

        // 显示穿戴的盔甲
        this.addLayer(new HumanoidArmorLayer<>(this,
                new SkeletonModel<>(context.bakeLayer(ModelLayers.SKELETON_INNER_ARMOR)),
                new SkeletonModel<>(context.bakeLayer(ModelLayers.SKELETON_OUTER_ARMOR)),
                context.getModelManager()));

        // 显示手持的弓或剑
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(RubySkeletonEntity entity) {
        return new ResourceLocation(RubyMod.MOD_ID, "textures/entity/ruby_skeleton.png");
    }
}
