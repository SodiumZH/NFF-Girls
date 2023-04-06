package net.sodiumstudio.dwmg.befriendmobs.events;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import net.sodiumstudio.dwmg.befriendmobs.util.MiscUtil;

@Cancelable
public class AddHatredEvent extends Event
{
	Mob mob;
	LivingEntity toAdd;
	Reason reason;

	public static enum Reason
	{
		ATTACKED,	// The befriendable mob is attack by player
		SET_TARGET,	// The befriendable mob set target to player
		PROVOKED,	// Fired when an enderman get angry being looked at
		CUSTOM_1,
		CUSTOM_2;
		
		public static boolean filter(Reason test, Reason[] allow)
		{
			return MiscUtil.isIn(test, allow);
		}
		
	}
}
