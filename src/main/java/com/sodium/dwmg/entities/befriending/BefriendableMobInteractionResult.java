package com.sodium.dwmg.entities.befriending;

import com.sodium.dwmg.entities.IBefriendedMob;

public class BefriendableMobInteractionResult {
	
	public IBefriendedMob befriended = null;
	public boolean hasInteracted = false;
	
	public static BefriendableMobInteractionResult of(IBefriendedMob befriended, boolean hasInteracted)
	{
		BefriendableMobInteractionResult res = new BefriendableMobInteractionResult();
		res.befriended = befriended;
		res.hasInteracted = hasInteracted;
		return res;
	}

}
