package net.sodiumstudio.dwmg.client.renderer.layers;

import com.github.mechalopa.hmag.client.ModModelLayers;
import com.github.mechalopa.hmag.client.model.AbstractGirlModel;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.sodiumstudio.dwmg.client.model.BefriendedCreeperGirlArmorModel;
import net.sodiumstudio.dwmg.entities.hmag.EntityBefriendedCreeperGirl;

// Ported from HMaG CreeperGirlPowerLayer
@OnlyIn(Dist.CLIENT)
public class BefriendedCreeperGirlPowerLayer extends EnergySwirlLayer<EntityBefriendedCreeperGirl, AbstractGirlModel<EntityBefriendedCreeperGirl>>
{
	private static final ResourceLocation TEX = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
	private final BefriendedCreeperGirlArmorModel<EntityBefriendedCreeperGirl> model;

	public BefriendedCreeperGirlPowerLayer(RenderLayerParent<EntityBefriendedCreeperGirl, AbstractGirlModel<EntityBefriendedCreeperGirl>> renderLayerParent, EntityModelSet modelSet)
	{
		super(renderLayerParent);
		this.model = new BefriendedCreeperGirlArmorModel<>(modelSet.bakeLayer(ModModelLayers.CREEPER_GIRL_POWER_ARMOR));
	}

	@Override
	protected float xOffset(float f)
	{
		return f * 0.01F;
	}

	@Override
	protected ResourceLocation getTextureLocation()
	{
		return TEX;
	}

	@Override
	protected EntityModel<EntityBefriendedCreeperGirl> model()
	{
		return this.model;
	}
}