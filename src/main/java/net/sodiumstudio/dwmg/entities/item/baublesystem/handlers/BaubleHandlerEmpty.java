package net.sodiumstudio.dwmg.entities.item.baublesystem.handlers;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.befriendmobs.item.baublesystem.BaubleHandler;
import net.sodiumstudio.befriendmobs.item.baublesystem.IBaubleEquipable;

/**
 * A bauble handler that doesn't do anything
*/

@Deprecated
public class BaubleHandlerEmpty extends BaubleHandler
{

	@Override
	public void refreshBaubleEffect(String slotKey, ItemStack bauble, IBaubleEquipable owner) 
	{}

	@Override
	public Map<String, Predicate<IBaubleEquipable>> getItemKeysAccepted(String slotKey, IBaubleEquipable mob) {
		// TODO Auto-generated method stub
		return Map.of();
	}
	
}
