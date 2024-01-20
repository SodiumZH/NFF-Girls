package net.sodiumstudio.dwmg.subsystem.baublesystem.baubles;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.BaubleAttributeModifier;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.BaubleProcessingArgs;
import net.sodiumstudio.dwmg.subsystem.baublesystem.DwmgDedicatedBaubleItem;

public class LifeJadeBaubleItem extends DwmgDedicatedBaubleItem
{

	public LifeJadeBaubleItem(String additionalKey, int tier, Properties pProperties)
	{
		super(additionalKey, tier, pProperties);
	}

	@Override
	public void slotTick(BaubleProcessingArgs args) {
		switch (this.tier)
		{
		case 1:
		{
			args.user().getMob().heal(0.15f / 20f);
			break;
		}
		case 2:
		{
			args.user().getMob().heal(0.2f / 20f);
			break;
		}
		default:
		{
			throw this.unsupportedTier();
		}
		}
	}

	@Override
	public BaubleAttributeModifier[] getDuplicatableModifiers(BaubleProcessingArgs args) {
		switch (this.tier)
		{
		case 1:
		{
			return BaubleAttributeModifier.makeModifiers(Attributes.MAX_HEALTH, 5d);
		}
		case 2:
		{
			return BaubleAttributeModifier.makeModifiers(Attributes.MAX_HEALTH, 10d);
		}
		default:
		{
			throw this.unsupportedTier();
		}
		}
		
	}

	@Override
	public ResourceLocation getBaubleRegistryKeyUnsuffixed() {
		// TODO Auto-generated method stub
		return new ResourceLocation("dwmg:life_jade");
	}

}
