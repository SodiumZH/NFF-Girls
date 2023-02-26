package com.sodium.dwmg.entities.ai;

public enum BefriendedAIState {
	WAIT(0),
	SIT(1), // NOT IMPLEMENTED
	FOLLOW(2),
	WANDER(3);
	
	public static final Byte[] IDSET = {0, 1, 2, 3};
	public final byte id;
	
	private BefriendedAIState(int id)
	{
		this.id = (byte)id;
	}
	
	public byte id()
	{
		return this.id;
	}
	
	public static BefriendedAIState fromID(byte id)
	{
		return id == 0 ? WAIT : (
				id == 1 ? SIT : (
				id == 2 ? FOLLOW :
				WANDER));
	}
	
	public BefriendedAIState defaultSwitch()
	{
		byte id = this.id();
		if(id < 3)
			return fromID((byte)(id + 1));
		else return WAIT;
	}
	
}
