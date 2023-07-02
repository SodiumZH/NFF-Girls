package net.sodiumstudio.dwmg.entities.item.baublesystem.handlers;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.befriendmobs.item.baublesystem.BaubleHandler;
import net.sodiumstudio.befriendmobs.item.baublesystem.IBaubleHolder;

/**
 * A bauble handler that doesn't do anything
*/
public class BaubleHandlerEmpty extends BaubleHandler
{

	@Override
	public void refreshBaubleEffect(String slotKey, ItemStack bauble, IBaubleHolder owner) 
	{}
	
	/**
	 * Don't tick to save resource
	 */
	@Override
	public void tick(IBaubleHolder holder)
	{}

	@Override
	public Map<String, Predicate<IBaubleHolder>> getItemKeysAccepted(String slotKey, IBaubleHolder mob) {
		// TODO Auto-generated method stub
		return Map.of();
	}
	
}
