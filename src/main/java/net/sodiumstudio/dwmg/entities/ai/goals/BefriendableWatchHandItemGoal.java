package net.sodiumstudio.dwmg.entities.ai.goals;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import net.sodiumstudio.befriendmobs.entity.befriending.registry.BefriendingTypeRegistry;
import net.sodiumstudio.befriendmobs.entity.capability.CBefriendableMob;
import net.sodiumstudio.befriendmobs.registry.BefMobCapabilities;
import net.sodiumstudio.dwmg.entities.handlers.hmag.HandlerItemDropping;

public class BefriendableWatchHandItemGoal extends Goal
{
	protected final Mob mob;
	protected final CBefriendableMob cap;
	
	public BefriendableWatchHandItemGoal(Mob mobBefriendable)
	{
		this.mob = mobBefriendable;
		if (mob.getCapability(BefMobCapabilities.CAP_BEFRIENDABLE_MOB).isPresent())
		{
			cap = CBefriendableMob.getCap(mob);
		}
		else throw new UnsupportedOperationException("This goal supports only mobs with CBefriendableMob capability.");
		if (!(BefriendingTypeRegistry.getHandler(mob) instanceof HandlerItemDropping))
			throw new UnsupportedOperationException("This goal supports befriendable mobs only with HandlerItemDropping as befriending handler.");
	}
	
	@Override
	public boolean canUse() {
		return cap.hasTimer("hold_item_time");
	}
}
