package com.sodium.dwmg.entities.befriending;

public enum AIState {
	WAIT,
	FOLLOW,
	WANDER,
	SIT;
	int getID()
	{
		switch(this)
		{
		case WAIT:
		{
			return 0;
		}
		case FOLLOW:
		{
			return 1;
		}
		case WANDER:
		{
			return 2;
		}
		case SIT:
		{
			return 3;
		}
		default:
		{
			throw new IllegalArgumentException("Invalid AI State in IBefriendedMob");
		}
		}
	}
}
