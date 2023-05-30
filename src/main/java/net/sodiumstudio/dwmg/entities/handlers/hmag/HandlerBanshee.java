package net.sodiumstudio.dwmg.entities.handlers.hmag;

import java.util.HashSet;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.sodiumstudio.befriendmobs.entity.befriending.BefriendableAddHatredReason;

public class HandlerBanshee extends HandlerSkeletonGirl
{
	@Override
	public boolean additionalConditions(Player player, Mob mob)
	{
		return mob.level.dimension().equals(Level.NETHER) && player.getOffhandItem().is(Items.WITHER_ROSE);
	}
	
	@Override
	public HashSet<BefriendableAddHatredReason> getAddHatredReasons() {
		HashSet<BefriendableAddHatredReason> set = new HashSet<BefriendableAddHatredReason>();
		set.add(BefriendableAddHatredReason.ATTACKED);
		set.add(BefriendableAddHatredReason.ATTACKING);
		return set;
	}
		
	@Override
	public int getHatredDurationTicks(BefriendableAddHatredReason reason)
	{
		switch (reason)
		{
		case ATTACKED:
			return 18000;
		case ATTACKING:
			return 1200; 
		default:
			return 0;				
		}
	}
	
	public void serverTick(Player player, Mob mob)
	{
		if (mob != null && player != null && mob.distanceToSqr(player) > 32 * 32)
		{
			interrupt(player, mob, true);
		}
	}
}

