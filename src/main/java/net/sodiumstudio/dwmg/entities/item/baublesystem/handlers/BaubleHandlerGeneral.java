package net.sodiumstudio.dwmg.entities.item.baublesystem.handlers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Predicate;

import com.github.mechalopa.hmag.registry.ModItems;

import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.befriendmobs.item.baublesystem.BaubleHandler;
import net.sodiumstudio.befriendmobs.item.baublesystem.IBaubleHolder;
import net.sodiumstudio.dwmg.registries.DwmgItems;

public class BaubleHandlerGeneral extends BaubleHandler
{
	
	@Override
	public HashMap<Item, Predicate<IBaubleHolder>> getItemsAccepted(String key, IBaubleHolder mob) {
		HashMap<Item, Predicate<IBaubleHolder>> map = new HashMap<Item, Predicate<IBaubleHolder>>();
		map.put(DwmgItems.RESISTANCE_AMULET.get(), null);
		map.put(ModItems.INSOMNIA_FRUIT.get(), null);
		map.put(DwmgItems.HEALING_JADE.get(), null);
		return map;
	}
	
	@Override
	public boolean shouldAlwaysRefresh(String slotKey, IBaubleHolder holder)
	{
		return holder.getBaubleSlots().get(slotKey).is(DwmgItems.HEALING_JADE.get());
	} 
	
	@Override
	public void refreshBaubleEffect(String slotKey, ItemStack bauble, IBaubleHolder owner) {
		if (bauble.is(DwmgItems.RESISTANCE_AMULET.get()))
		{
			owner.addBaubleModifier(slotKey, "ra_armor", Attributes.ARMOR, 4.0d, Operation.ADDITION);
			owner.addBaubleModifier(slotKey, "ra_speed_slow",Attributes.MOVEMENT_SPEED, -0.2d, Operation.MULTIPLY_BASE);
		}
		else if (bauble.is(DwmgItems.HEALING_JADE.get()))
		{
			owner.getLiving().heal(0.005f);// 0.1 per second
		}
	}
	
	
	@Override
	public void postTick(IBaubleHolder owner)
	{
		super.postTick(owner);
		
		owner.removeBaubleModifiers("if");
		if (owner.hasBaubleItem(ModItems.INSOMNIA_FRUIT.get()) && owner.getLiving().level.isNight())
		{
			owner.addBaubleModifier("if", "if_health", Attributes.MAX_HEALTH, 60d, Operation.ADDITION);
			owner.addBaubleModifier("if", "if_atk", Attributes.ATTACK_DAMAGE, 8d, Operation.ADDITION);		
		}
	}
	
}
