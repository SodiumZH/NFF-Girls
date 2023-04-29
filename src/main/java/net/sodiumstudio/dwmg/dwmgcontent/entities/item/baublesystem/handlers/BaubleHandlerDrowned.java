package net.sodiumstudio.dwmg.dwmgcontent.entities.item.baublesystem.handlers;

import java.util.HashSet;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.sodiumstudio.dwmg.befriendmobs.item.baublesystem.BaubleHandler;
import net.sodiumstudio.dwmg.befriendmobs.item.baublesystem.IBaubleHolder;
import net.sodiumstudio.dwmg.dwmgcontent.registries.DwmgItems;

public class BaubleHandlerDrowned extends BaubleHandlerGeneral
{

	@Override
	public HashSet<Item> getItemsAccepted() {
		HashSet<Item> set = super.getItemsAccepted();
		set.add(Items.CONDUIT);
		return set;
	}

	@Override
	public void postTick(IBaubleHolder owner)
	{
		super.postTick(owner);
		owner.removeBaubleModifiers("conduit");
		if (owner.hasBaubleItem(Items.CONDUIT) && owner.getLiving().isInWater())
		{
			owner.addBaubleModifier("conduit", "conduit_speed", Attributes.MOVEMENT_SPEED, 3.0d, Operation.MULTIPLY_BASE);
		}
	}
	
	
}
