package net.sodiumstudio.dwmg.entities.item.baublesystem;

import java.util.HashMap;

import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.nautils.ContainerHelper;

@Deprecated
public class DwmgBaubleSlotGetter
{
	protected HashMap<String, Integer> map = new HashMap<String, Integer>();
	
	public DwmgBaubleSlotGetter put(String key, int inventoryIndex)
	{
		map.put(key, inventoryIndex);
		return this;
	}
	
	public DwmgBaubleSlotGetter put(int keyID, int inventoryIndex)
	{
		return put(Integer.toString(keyID), inventoryIndex);
	}
	
	public HashMap<String, ItemStack> get(IBefriendedMob mob)
	{
		return ContainerHelper.castMap(map, (String s) -> s, (Integer v) -> mob.getAdditionalInventory().getItem(v), true, false);
	}
	
	public static final DwmgBaubleSlotGetter NO_EQUIPMENT_FOUR_BAUBLES = 
			new DwmgBaubleSlotGetter().put("0", 0).put("1", 1).put("2", 2).put("3", 3);
}
