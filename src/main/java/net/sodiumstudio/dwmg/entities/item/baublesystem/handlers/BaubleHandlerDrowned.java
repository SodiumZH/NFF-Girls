package net.sodiumstudio.dwmg.entities.item.baublesystem.handlers;

import java.util.Map;
import java.util.function.Predicate;

import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.sodiumstudio.befriendmobs.item.baublesystem.IBaubleHolder;
import net.sodiumstudio.dwmg.registries.DwmgItems;

public class BaubleHandlerDrowned extends BaubleHandlerUndead
{

	@Override
	public Map<String, Predicate<IBaubleHolder>> getItemKeysAccepted(String key, IBaubleHolder mob) {
		Map<String, Predicate<IBaubleHolder>> map = super.getItemKeysAccepted(key, mob);
		map.put("dwmg:aqua_jade", null);
		return map;
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
