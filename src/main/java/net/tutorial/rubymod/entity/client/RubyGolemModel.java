package net.tutorial.rubymod.entity.client;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.Entity;

public class RubyGolemModel<T extends Entity> extends HierarchicalModel<T> {
    private final ModelPart root;

    public RubyGolemModel(ModelPart root) {
        this.root = root;
    }

    // 定义模型的所有“盒子”。纹理大小为 64 x 64。
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // 身体
        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-5.0F, -8.0F, -6.0F, 10.0F, 8.0F, 12.0F),
                PartPose.offset(0.0F, 16.0F, 0.0F));

        // 头
        root.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(0, 20)
                        .addBox(-4.0F, -6.0F, -6.0F, 8.0F, 6.0F, 6.0F),
                PartPose.offset(0.0F, 10.0F, -6.0F));

        // 四条腿（共用同一块纹理区域，外观一致）
        CubeListBuilder leg = CubeListBuilder.create().texOffs(0, 32)
                .addBox(-1.5F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F);

        root.addOrReplaceChild("leg_front_right", leg, PartPose.offset(-3.0F, 18.0F, -4.0F));
        root.addOrReplaceChild("leg_front_left", leg, PartPose.offset(3.0F, 18.0F, -4.0F));
        root.addOrReplaceChild("leg_back_right", leg, PartPose.offset(-3.0F, 18.0F, 4.0F));
        root.addOrReplaceChild("leg_back_left", leg, PartPose.offset(3.0F, 18.0F, 4.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks,
                          float netHeadYaw, float headPitch) {
        // 站立静止。想加走路摆腿动画可在这里旋转 leg 部件。
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}
