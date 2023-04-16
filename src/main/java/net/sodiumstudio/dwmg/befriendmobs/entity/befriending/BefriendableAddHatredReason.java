package net.sodiumstudio.dwmg.befriendmobs.entity.befriending;

public enum BefriendableAddHatredReason
{

	NOT(-1), // Not in hatred
	ATTACKED(0), // The befriendable mob is attack by player
	ATTACKING(1), // The befriendable mob attacked the player
	SET_TARGET(2), // The befriendable mob set target to player
	PROVOKED(3), // Fired when an enderman get angry being looked at
	CUSTOM_1(99), 
	CUSTOM_2(98),
	CUSTOM_3(97),
	CUSTOM_4(96), 
	CUSTOM_5(95), 
	CUSTOM_6(94), 
	CUSTOM_7(93),
	CUSTOM_8(92),
	CUSTOM_9(91);

	private int id;

	private BefriendableAddHatredReason(int inID)
	{
		id = inID;
	}

	public BefriendableAddHatredReason fromID(int inId) {
		for (BefriendableAddHatredReason r : values())
		{
			if (r.id() == inId)
				return r;
		}
		throw new IllegalArgumentException();
	}

	public int id() 
	{
		return this.id;
	}
	
	
}
