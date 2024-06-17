package net.sodiumstudio.dwmg.subsystem.baublesystem.baubles;

import net.minecraft.resources.ResourceLocation;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.BaubleAttributeModifier;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.BaubleProcessingArgs;
import net.sodiumstudio.dwmg.registries.DwmgConfigs;
import net.sodiumstudio.dwmg.subsystem.baublesystem.DwmgDedicatedBaubleItem;

public class HealingJadeBaubleItem extends DwmgDedicatedBaubleItem
{

	public HealingJadeBaubleItem(String additionalKey, int tier, Properties pProperties)
	{
		super(additionalKey, tier, pProperties);
	}

	@Override
	public void slotTick(BaubleProcessingArgs args) {
		args.user().heal(DwmgConfigs.ValueCache.Baubles.BAUBLE_HEALTH_RECOVERY_SCALE * 0.1f / 20f);
	}

	@Override
	public BaubleAttributeModifier[] getDuplicatableModifiers(BaubleProcessingArgs args) {
		return null;
	}

	@Override
	public ResourceLocation getBaubleRegistryKeyUnsuffixed() {
		// TODO Auto-generated method stub
		return new ResourceLocation("dwmg:healing_jade");
	}

}
