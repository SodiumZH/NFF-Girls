package net.sodiumstudio.dwmg.client.renderer;

import com.github.mechalopa.hmag.HMaG;
import com.github.mechalopa.hmag.client.ModModelLayers;
import com.github.mechalopa.hmag.client.model.AbstractGirlModel;
import com.github.mechalopa.hmag.client.renderer.AbstractGirlRenderer;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.sodiumstudio.dwmg.client.model.BefriendedCreeperGirlArmorModel;
import net.sodiumstudio.dwmg.client.model.BefriendedCreeperGirlModel;
import net.sodiumstudio.dwmg.client.renderer.layers.BefriendedCreeperGirlPowerLayer;
import net.sodiumstudio.dwmg.entities.hmag.EntityBefriendedCreeperGirl;

// Ported from HMaG CreeperGirlRenderer
public class BefriendedCreeperGirlRenderer extends AbstractGirlRenderer<EntityBefriendedCreeperGirl, AbstractGirlModel<EntityBefriendedCreeperGirl>>
{
	private static final ResourceLocation TEX0 = new ResourceLocation(HMaG.MODID, "textures/entity/creeper_girl/creeper_girl_0.png");
	private static final ResourceLocation TEX1 = new ResourceLocation(HMaG.MODID, "textures/entity/creeper_girl/creeper_girl_1.png");
	private static final ResourceLocation TEX2 = new ResourceLocation(HMaG.MODID, "textures/entity/creeper_girl/creeper_girl_2.png");

	public BefriendedCreeperGirlRenderer(EntityRendererProvider.Context context)
	{
		this(context, new BefriendedCreeperGirlModel<>(context.bakeLayer(ModModelLayers.CREEPER_GIRL)), 
				new BefriendedCreeperGirlArmorModel<>(context.bakeLayer(ModModelLayers.CREEPER_GIRL_INNER_ARMOR)), 
				new BefriendedCreeperGirlArmorModel<>(context.bakeLayer(ModModelLayers.CREEPER_GIRL_OUTER_ARMOR)));
	}

	public BefriendedCreeperGirlRenderer(EntityRendererProvider.Context context, BefriendedCreeperGirlModel<EntityBefriendedCreeperGirl> model, AbstractGirlModel<EntityBefriendedCreeperGirl> model1, AbstractGirlModel<EntityBefriendedCreeperGirl> model2)
	{
		super(context, model, 0.5F);
		this.addLayer(new HumanoidArmorLayer<>(this, model1, model2));
		this.addLayer(new BefriendedCreeperGirlPowerLayer(this, context.getModelSet()));
	}

	@Override
	protected void scale(EntityBefriendedCreeperGirl entity, PoseStack poseStack, float partialTickTime)
	{
		float f = entity.getSwelling(partialTickTime);
		float f1 = 1.0F + Mth.sin(f * 100.0F) * f * 0.01F;
		f = Mth.clamp(f, 0.0F, 1.0F);
		f = f * f;
		f = f * f;
		float f2 = (1.0F + f * 0.4F) * f1;
		float f3 = (1.0F + f * 0.1F) / f1;
		poseStack.scale(f2, f3, f2);
	}

	@Override
	protected float getWhiteOverlayProgress(EntityBefriendedCreeperGirl entity, float partialTicks)
	{
		float f = entity.getSwelling(partialTicks);
		return (int)(f * 10.0F) % 2 == 0 ? 0.0F : Mth.clamp(f, 0.5F, 1.0F);
	}

	@Override
	public ResourceLocation getTextureLocation(EntityBefriendedCreeperGirl entity)
	{
		switch (entity.getVariant())
		{
		case 1:
			return TEX1;
		case 2:
			return TEX2;
		default:
			return TEX0;
		}
	}
}
