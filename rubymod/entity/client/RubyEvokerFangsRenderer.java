package net.tutorial.rubymod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.EvokerFangsModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.tutorial.rubymod.RubyMod;
import net.tutorial.rubymod.entity.custom.RubyEvokerFangsEntity;

public class RubyEvokerFangsRenderer extends EntityRenderer<RubyEvokerFangsEntity> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(RubyMod.MOD_ID, "textures/entity/ruby_evoker_fangs.png");

    private final EvokerFangsModel<RubyEvokerFangsEntity> model;

    public RubyEvokerFangsRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new EvokerFangsModel<>(context.bakeLayer(ModelLayers.EVOKER_FANGS));
    }

    @Override
    public void render(RubyEvokerFangsEntity entity, float yaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource buffer, int light) {
        float f = entity.getAnimationProgress(partialTick);
        if (f != 0.0F) {
            float scale = 2.0F;
            if (f > 0.9F) {
                scale = (float) ((double) scale * ((1.0D - (double) f) / 0.1D));
            }
            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees(90.0F - entity.getYRot()));
            poseStack.scale(-scale, -scale, scale);
            poseStack.translate(0.0F, -0.626F, 0.0F);
            this.model.setupAnim(entity, f, 0.0F, 0.0F, entity.getYRot(), entity.getXRot());
            VertexConsumer vc = buffer.getBuffer(this.model.renderType(TEXTURE));
            this.model.renderToBuffer(poseStack, vc, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            poseStack.popPose();
        }
        super.render(entity, yaw, partialTick, poseStack, buffer, light);
    }

    @Override
    public ResourceLocation getTextureLocation(RubyEvokerFangsEntity entity) {
        return TEXTURE;
    }
}
