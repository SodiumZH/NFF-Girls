package net.sodiumstudio.dwmg.subsystem.baublesystem.baubles;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.BaubleSystem;

public class DwmgBaubleStatics
{
	/**
	 * Count how many bauble of given key the mob has within the tier range.
	 */
	public static int countBaublesWithTierRange(Mob mob, ResourceLocation key, int minTier, int maxTierExcluding)
	{
		var equipped = BaubleSystem.getAllSlotItems(mob).values();
		int count = 0;
		for (ItemStack stack: equipped)
		{
			var prop = DwmgBaubleAdditionalRegistry.getRegistry().get(key);
			if (prop != null && prop.getA().equals(key) && prop.getB() >= minTier && prop.getB() < maxTierExcluding)
				count++;
		}
		return count;
	}
	
	/**
	 * Count how many bauble of given key the mob has with the minimum tier.
	 */
	public static int countBaublesWithMinTier(Mob mob, ResourceLocation key, int minTier)
	{
		return countBaublesWithTierRange(mob, key, minTier, Integer.MAX_VALUE);
	}
	
	/**
	 * Count how many bauble of given key the mob has, despite the tier.
	 */
	public static int countBaubles(Mob mob, ResourceLocation key)
	{
		return countBaublesWithMinTier(mob, key, 0); 
	}
}
