package net.sodiumstudio.dwmg.entities.handlers.hmag;

import java.util.HashSet;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.sodiumstudio.befriendmobs.entity.befriending.BefriendableAddHatredReason;

public class HandlerBanshee extends HandlerSkeletonGirl
{
	@Override
	public boolean additionalConditions(Player player, Mob mob)
	{
		return mob.level.dimension().equals(Level.NETHER) && mob.level.canSeeSky(mob.blockPosition());
	}
	
	@Override
	public HashSet<BefriendableAddHatredReason> getAddHatredReasons() {
		HashSet<BefriendableAddHatredReason> set = new HashSet<BefriendableAddHatredReason>();
		set.add(BefriendableAddHatredReason.ATTACKED);
		return set;
	}
}

