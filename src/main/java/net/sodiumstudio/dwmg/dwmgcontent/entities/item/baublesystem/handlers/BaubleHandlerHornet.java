package net.sodiumstudio.dwmg.dwmgcontent.entities.item.baublesystem.handlers;

import java.util.HashSet;

import com.github.mechalopa.hmag.registry.ModItems;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.dwmg.befriendmobs.item.baublesystem.IBaubleHolder;
import net.sodiumstudio.dwmg.dwmgcontent.entities.hmag.EntityBefriendedHornet;
import net.sodiumstudio.dwmg.dwmgcontent.registries.DwmgItems;

public class BaubleHandlerHornet extends BaubleHandlerGeneral
{
	
	@Override
	public HashSet<Item> getItemsAccepted() {
		HashSet<Item> set = super.getItemsAccepted();
		set.add(DwmgItems.POISONOUS_THORN.get());
		return set;
	}
	
	@Override
	public void postTick(IBaubleHolder owner)
	{
		super.postTick(owner);
		if (owner instanceof EntityBefriendedHornet b)
		{
			if (owner.hasBaubleItem(DwmgItems.POISONOUS_THORN.get()))
			{
				b.addPoisonLevel = 2;
			}
			else
			{
				b.addPoisonLevel = 1;
			}
		}
	}
	
}
