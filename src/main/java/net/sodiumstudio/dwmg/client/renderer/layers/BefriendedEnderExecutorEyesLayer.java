package net.sodiumstudio.dwmg.client.renderer.layers;

import com.github.mechalopa.hmag.HMaG;
import com.github.mechalopa.hmag.client.model.EnderExecutorModel;
import com.github.mechalopa.hmag.client.renderer.layers.EyesLayer2;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.sodiumstudio.dwmg.client.model.BefriendedEnderExecutorModel;
import net.sodiumstudio.dwmg.entities.hmag.EntityBefriendedEnderExecutor;

@OnlyIn(Dist.CLIENT)
public class BefriendedEnderExecutorEyesLayer<T extends EntityBefriendedEnderExecutor, M extends BefriendedEnderExecutorModel<T>> extends EyesLayer2<T, M>
{
	private static final RenderType RENDER_TYPE0 = RenderType.eyes(new ResourceLocation(HMaG.MODID, "textures/entity/ender_executor/ender_executor_eyes.png"));
	private static final RenderType RENDER_TYPE1 = RenderType.eyes(new ResourceLocation(HMaG.MODID, "textures/entity/ender_executor/ender_executor_eyes_is_beam.png"));

	public BefriendedEnderExecutorEyesLayer(RenderLayerParent<T, M> renderLayerParent)
	{
		super(renderLayerParent);
	}

	@Override
	public RenderType renderType(T entity)
	{
		return this.getParentModel().beamAttacking && entity.tickCount % 2 == 0 ? RENDER_TYPE1 : RENDER_TYPE0;
	}
}