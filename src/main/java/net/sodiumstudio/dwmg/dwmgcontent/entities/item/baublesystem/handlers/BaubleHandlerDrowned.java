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

public class BaubleHandlerDrowned extends BaubleHandler
{

	@Override
	public HashSet<Item> getItemsAccepted() {
		HashSet<Item> set = new HashSet<Item>();
		set.add(DwmgItems.DEATH_CRYSTAL.get());
		set.add(Items.NETHERITE_INGOT);
		set.add(Items.CONDUIT);
		return set;
	}

	@Override
	public void applyBaubleEffect(ItemStack bauble, IBaubleHolder owner) {
		if (bauble.is(DwmgItems.DEATH_CRYSTAL.get()))
		{
			owner.addBaubleModifier(Attributes.ATTACK_DAMAGE, 2.0d, Operation.ADDITION);
		}
		if (bauble.is(Items.NETHERITE_INGOT))
		{
			owner.addBaubleModifier(Attributes.ARMOR, 2.0d, Operation.ADDITION);
		}
	}
	
	@Override
	public void postUpdate(IBaubleHolder owner)
	{
		if (owner.hasBaubleItem(Items.CONDUIT) && owner.getLiving().isInWater())
		{
			owner.addBaubleModifier(Attributes.MOVEMENT_SPEED, 3.0d, Operation.MULTIPLY_BASE);
		}
	}
	
	
}
