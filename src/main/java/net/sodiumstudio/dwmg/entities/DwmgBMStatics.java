package net.sodiumstudio.dwmg.entities;

import java.util.HashMap;

import com.github.mechalopa.hmag.registry.ModItems;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

public class DwmgBMStatics
{
	/** 
	 * Healing items for default undead or undead-like mobs
	 */
	public static final HashMap<Item, Float> UNDEAD_DEFAULT_HEALING_ITEMS = new HashMap<Item, Float>();
	static
	{
		UNDEAD_DEFAULT_HEALING_ITEMS.put(ModItems.SOUL_POWDER.get(), 5.0f);
		UNDEAD_DEFAULT_HEALING_ITEMS.put(ModItems.SOUL_APPLE.get(), 15.0f);
	}
	
	/*public static InteractionResult commonInteract(IDwmgBefriendedMob mob, Player player, InteractionHand hand)
	{
		if (player.level.isClientSide)
			return InteractionResult.PASS;
		if (!mob.isOwnerPresent() || mob.getOwner() != player)
			return InteractionResult.PASS;
		
	}*/
}
