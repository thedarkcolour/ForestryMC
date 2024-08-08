/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package forestry.lepidopterology.render;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import forestry.lepidopterology.entities.EntityButterfly;

public class ButterflyModel extends EntityModel<EntityButterfly> {
	private final ModelPart root;
	private final ModelPart leftWing;
	private final ModelPart rightWing;

	private float scale;

	public ButterflyModel(ModelPart root) {
		this.root = root;
		this.leftWing = root.getChild("left_wing");
		this.rightWing = root.getChild("right_wing");
	}

	public static LayerDefinition createLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
						.texOffs(40, 0)
						.addBox(0f, 0f, -4f, 1, 1, 6),
				PartPose.rotation(0f, 0f, 0.7853982f));
		PartDefinition leftWing = root.addOrReplaceChild("left_wing", CubeListBuilder.create()
						.texOffs(0, 14)
						.addBox(0f, 0f, -6f, 7, 1, 13),
				PartPose.offset(0.5f, 0.5f, 0f));
		PartDefinition rightWing = root.addOrReplaceChild("right_wing", CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-7f, 0f, -6f, 7, 1, 13),
				PartPose.offset(-0.5f, 0.5f, 0f));
		PartDefinition leftEye = root.addOrReplaceChild("left_eye", CubeListBuilder.create()
						.texOffs(40, 7)
						.addBox(0f, 0f, 0f, 1, 1, 1),
				PartPose.offset(0.1f, -0.5f, -4.5f));
		PartDefinition rightEye = root.addOrReplaceChild("right_eye", CubeListBuilder.create()
						.texOffs(40, 9)
						.addBox(0f, 0f, 0f, 1, 1, 1),
				PartPose.offset(-1.1f, -0.5f, -4.5f));

		return LayerDefinition.create(mesh, 64, 32);
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer builder, int light, int overlay, float ageInTicks, float netHeadYaw, float headPitch, float alpha) {
		poseStack.scale(this.scale, this.scale, this.scale);
		poseStack.translate(0.0F, 1.45f / scale, 0.0F);

		root.render(poseStack, builder, light, overlay);
	}

	@Override
	public void setupAnim(EntityButterfly entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		leftWing.zRot = Mth.cos(ageInTicks * 1.3f) * Mth.PI * 0.25f;
		rightWing.zRot = -leftWing.zRot;
	}
}
