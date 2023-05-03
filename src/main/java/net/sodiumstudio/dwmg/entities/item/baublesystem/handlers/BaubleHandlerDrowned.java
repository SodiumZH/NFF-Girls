package net.sodiumstudio.dwmg.entities.item.baublesystem.handlers;

import java.util.HashSet;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.sodiumstudio.befriendmobs.item.baublesystem.BaubleHandler;
import net.sodiumstudio.befriendmobs.item.baublesystem.IBaubleHolder;
import net.sodiumstudio.dwmg.registries.DwmgItems;

public class BaubleHandlerDrowned extends BaubleHandlerVanillaUndead
{

	@Override
	public HashSet<Item> getItemsAccepted() {
		HashSet<Item> set = super.getItemsAccepted();
		set.add(DwmgItems.AQUA_JADE.get());
		return set;
	}

	@Override
	public void postTick(IBaubleHolder owner)
	{
		super.postTick(owner);
		owner.removeBaubleModifiers("aj");
		if (owner.hasBaubleItem(DwmgItems.AQUA_JADE.get()) && owner.getLiving().isInWater())
		{
			owner.addBaubleModifier("aj", "aj_speed", Attributes.MOVEMENT_SPEED, 3.0d, Operation.MULTIPLY_BASE);
		}
	}
	
	
}
