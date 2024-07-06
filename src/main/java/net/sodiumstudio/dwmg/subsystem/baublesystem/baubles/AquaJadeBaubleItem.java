package net.sodiumstudio.dwmg.subsystem.baublesystem.baubles;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.BaubleAttributeModifier;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.BaubleEquippingCondition;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.BaubleProcessingArgs;
import net.sodiumstudio.dwmg.entities.hmag.HmagDrownedGirlEntity;
import net.sodiumstudio.dwmg.registries.DwmgConfigs;
import net.sodiumstudio.dwmg.subsystem.baublesystem.DwmgDedicatedBaubleItem;

public class AquaJadeBaubleItem extends DwmgDedicatedBaubleItem
{

	public AquaJadeBaubleItem(String additionalKey, int tier, Properties pProperties)
	{
		super(additionalKey, tier, pProperties);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void slotTick(BaubleProcessingArgs args) {
		if (args.user().isInWater())
			args.user().heal(DwmgConfigs.ValueCache.Baubles.BAUBLE_HEALTH_RECOVERY_SCALE * 0.25f / 20f);
	}

	@Override
	public BaubleAttributeModifier[] getDuplicatableModifiers(BaubleProcessingArgs args) {
		return new BaubleAttributeModifier[] {
				new BaubleAttributeModifier(Attributes.MOVEMENT_SPEED, 3.0d, AttributeModifier.Operation.MULTIPLY_BASE)
					.setAdditionalCondition(a -> a.user().isInWater())
				};
	}

	@Override
	public ResourceLocation getBaubleRegistryKeyUnsuffixed() {
		return new ResourceLocation("dwmg:aqua_jade");
	}

	@Override
	public BaubleEquippingCondition getEquippingCondition()
	{
		return BaubleEquippingCondition.of(args -> (args.user() instanceof HmagDrownedGirlEntity));
	}
	
}
