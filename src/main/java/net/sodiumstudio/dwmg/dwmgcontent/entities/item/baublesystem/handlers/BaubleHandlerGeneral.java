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
	public void applyBaubleEffect(ItemStack bauble, IBaubleHolder owner) {
		if (bauble.is(DwmgItems.RESISTANCE_AMULET.get()))
		{
			owner.addBaubleModifier(Attributes.ARMOR, 4.0d, Operation.ADDITION);
			owner.addBaubleModifier(Attributes.MOVEMENT_SPEED, 0.8d, Operation.MULTIPLY_BASE);
		}
		else if (bauble.is(DwmgItems.HEALING_JADE.get()))
		{
			owner.getLiving().heal(0.005f);// 0.1 per second
		}
	}
	
	@Override
	public void postUpdate(IBaubleHolder owner)
	{
		super.postUpdate(owner);
		if (owner.hasBaubleItem(ModItems.INSOMNIA_FRUIT.get()))
		{
			if (owner.getLiving().level.isNight())
			{
				owner.addBaubleModifier(Attributes.MAX_HEALTH, 60d, Operation.ADDITION);
				owner.addBaubleModifier(Attributes.ATTACK_DAMAGE, 8d, Operation.ADDITION);
			}
		}
	}
	

}
