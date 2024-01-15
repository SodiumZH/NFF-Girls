package net.sodiumstudio.dwmg.subsystem.baublesystem.baubles;

import net.minecraft.resources.ResourceLocation;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.BaubleAttributeModifier;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.BaubleEquippingCondition;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.BaubleProcessingArgs;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.DedicatedBaubleItem;

public abstract class DwmgDedicatedBaubleItem extends DedicatedBaubleItem
{

	public final ResourceLocation additionalKey;
	public final int tier;
	
	/** additionalKey uses ResourceLocation format. */
	public DwmgDedicatedBaubleItem(String additionalKey, int tier, Properties pProperties)
	{
		super(pProperties);
		this.additionalKey = new ResourceLocation(additionalKey);
		this.tier = tier;
	}

}
