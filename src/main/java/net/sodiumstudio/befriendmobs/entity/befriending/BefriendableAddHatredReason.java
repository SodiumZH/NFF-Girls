package net.sodiumstudio.befriendmobs.entity.befriending;

public enum BefriendableAddHatredReason
{
	/** Not in hatred */
	NOT(-1), 
	/** The befriendable mob is attack by player */
	ATTACKED(0),
	/** The befriendable mob attacked the player */
	ATTACKING(1), 
	/** The hostile befriendable mob set target to player */
	SET_TARGET(2),
	/** Unused */
	PROVOKED(3), 
	/** The mob is hit by the player, but not taking damage */
	HIT(4), 
	/** The mob is damaged by the thorns enchantment of player */
	THORNS(5),
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
