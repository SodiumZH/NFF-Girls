package net.sodiumstudio.dwmg.client.renderer;

import com.github.mechalopa.hmag.HMaG;
import com.github.mechalopa.hmag.client.ModModelLayers;
import com.github.mechalopa.hmag.client.model.JackFrostModel;
import com.github.mechalopa.hmag.client.renderer.AbstractGirlRenderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.sodiumstudio.dwmg.entities.hmag.HmagJackFrostEntity;

@OnlyIn(Dist.CLIENT)
public class HmagJackFrostRenderer extends AbstractGirlRenderer<HmagJackFrostEntity, JackFrostModel<HmagJackFrostEntity>>
{
	private static final ResourceLocation TEX = new ResourceLocation(HMaG.MODID, "textures/entity/jack_frost.png");

	public  HmagJackFrostRenderer(EntityRendererProvider.Context context)
	{
		super(context, new JackFrostModel<>(context.bakeLayer(ModModelLayers.JACK_FROST)), 0.5F, -1);
	}

	@Override
	public ResourceLocation getTextureLocation(HmagJackFrostEntity entity)
	{
		return TEX;
	}
}