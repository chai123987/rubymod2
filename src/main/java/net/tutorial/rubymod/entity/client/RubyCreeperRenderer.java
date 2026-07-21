package net.tutorial.rubymod.entity.client;

import net.minecraft.client.renderer.entity.CreeperRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Creeper;
import net.tutorial.rubymod.RubyMod;

// 继承原版苦力怕渲染器：自带膨胀变大 + 爆炸前闪白的动画，只是换成红色贴图
public class RubyCreeperRenderer extends CreeperRenderer {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(RubyMod.MOD_ID, "textures/entity/ruby_creeper.png");

    public RubyCreeperRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(Creeper entity) {
        return TEXTURE;
    }
}
