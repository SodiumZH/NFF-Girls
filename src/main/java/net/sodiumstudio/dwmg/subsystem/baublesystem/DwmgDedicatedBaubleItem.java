package net.sodiumstudio.dwmg.subsystem.baublesystem;

import net.minecraft.resources.ResourceLocation;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.DedicatedBaubleItem;

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
	
	/**
	 * Create an exception that this item's tier is not supported. Needs to manually throw.
	 */
	protected UnsupportedOperationException unsupportedTier()
	{
		return new UnsupportedOperationException("Unsupported bauble tier ( " +  Integer.toString(this.tier) + ").");
	}
	
}
