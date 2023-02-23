package com.sodium.dwmg.entities.befriending;

public enum Befriendablity {

	CAN_BEF,	// Can befriend
	CANNOT_BEF,	// Cannot befriend because conditions are not satisfied
	HATRED,		// Cannot befriend because the player is in the hatred list
	WRONG_TYPE	// This mob type is unbefriendable
	
}
