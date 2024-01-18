package net.sodiumstudio.dwmg.subsystem.baublesystem;

import java.util.Map;

import net.minecraft.resources.ResourceLocation;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.BaubleAttributeModifier;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.BaubleEquippingCondition;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.BaubleProcessingArgs;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.DedicatedBaubleItem;
import net.sodiumstudio.nautils.ContainerHelper;
import net.sodiumstudio.nautils.containers.MapPair;

public abstract class DwmgDedicatedBaubleItem extends DedicatedBaubleItem
{

	public final ResourceLocation additionalKey;
	public final int tier;
	
	/** additionalKey uses ResourceLocation format. */
	public DwmgDedicatedBaubleItem(String additionalKey, int tier, Properties pProperties)
	{
		super(pProperties);
		if (tier <= 0)
			throw new IllegalArgumentException("DwmgDedicatedBaubleItem tier must be positive (not supporting 0).");
		this.additionalKey = new ResourceLocation(additionalKey);
		this.tier = tier;
	}

	public String getTierSuffix()
	{
		if (tier == 1)
			return "";
		return "_" + (tier > 10 ? Integer.toString(tier) : DwmgBaubleStatics.getRomanNumeral(tier, false));
	}
	
	@Override
	public final ResourceLocation getBaubleRegistryKey()
	{
		ResourceLocation unsuffixed = getBaubleRegistryKeyUnsuffixed();
		return new ResourceLocation(unsuffixed.getNamespace(), unsuffixed.getPath() + getTierSuffix());
	}
	
	/**
	 * Bauble registry key, no suffix. (Will be auto-added)
	 */
	public abstract ResourceLocation getBaubleRegistryKeyUnsuffixed();
}
