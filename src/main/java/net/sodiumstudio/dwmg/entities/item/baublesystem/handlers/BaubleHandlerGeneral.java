package net.sodiumstudio.dwmg.entities.item.baublesystem.handlers;

import java.util.HashSet;

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
	public HashSet<Item> getItemsAccepted() {
		HashSet<Item> set = new HashSet<Item>();
		set.add(DwmgItems.RESISTANCE_AMULET.get());
		set.add(ModItems.INSOMNIA_FRUIT.get());
		set.add(DwmgItems.HEALING_JADE.get());
		return set;
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
