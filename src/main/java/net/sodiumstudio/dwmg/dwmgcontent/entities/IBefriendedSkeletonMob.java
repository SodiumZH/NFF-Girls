package net.sodiumstudio.dwmg.dwmgcontent.entities;

import net.sodiumstudio.dwmg.befriendmobs.entitiy.IBefriendedMob;

// Extension of IBefriendedMob for skeleton
public interface IBefriendedSkeletonMob extends IBefriendedMob {

	public boolean isBowMode();
	
	public void setBowMode(boolean value);
	
}
