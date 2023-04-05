package net.sodiumstudio.dwmg.dwmgcontent.entities.item.baublesystem.handlers;

import java.util.HashSet;

import com.github.mechalopa.hmag.registry.ModItems;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.sodiumstudio.dwmg.befriendmobs.item.baublesystem.BaubleHandler;
import net.sodiumstudio.dwmg.befriendmobs.item.baublesystem.IBaubleHolder;
import net.sodiumstudio.dwmg.befriendmobs.util.EntityHelper;
import net.sodiumstudio.dwmg.dwmgcontent.entities.item.baublesystem.DwmgBaubleHandlerAttributeModifiers;
import net.sodiumstudio.dwmg.dwmgcontent.registries.DwmgItems;

public class BaubleHandlerEnderExecutor extends BaubleHandler
{
	
	@Override
	public HashSet<Item> getItemsAccepted() {
		HashSet<Item> set = new HashSet<Item>();
		set.add(Items.NETHERITE_INGOT);
		return set;
	}

	@Override
	public void applyBaubleEffect(ItemStack bauble, IBaubleHolder owner) {
		if (bauble.is(Items.NETHERITE_INGOT))
		{
			owner.addBaubleModifier(Attributes.ARMOR, 2.0d, Operation.ADDITION);
		}
	}
	

}
