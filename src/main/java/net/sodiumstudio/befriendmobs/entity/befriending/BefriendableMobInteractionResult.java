package net.sodiumstudio.befriendmobs.entity.befriending;

import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;

public class BefriendableMobInteractionResult {
	
	public boolean handled = false;
	public boolean shouldCancelEvent = false;
	public IBefriendedMob befriendedMob = null;

	public static BefriendableMobInteractionResult of(boolean handled, boolean shouldCancelEvent, IBefriendedMob befriended)
	{
		BefriendableMobInteractionResult res = new BefriendableMobInteractionResult();
		res.befriendedMob = befriended;
		res.handled = handled;
		res.shouldCancelEvent = shouldCancelEvent;
		return res;
	}

	public void setHandled()
	{
		handled = true;
	}
}
