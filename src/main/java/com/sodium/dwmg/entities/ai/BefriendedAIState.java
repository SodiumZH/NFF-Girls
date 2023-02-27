package com.sodium.dwmg.entities.ai;

public enum BefriendedAIState {
	WAIT(0),
	FOLLOW(1),
	WANDER(2);
	
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
				id == 1 ? FOLLOW : WANDER);
	}
	
	public BefriendedAIState defaultSwitch()
	{
		byte id = this.id();
		if(id < 2)
			return fromID((byte)(id + 1));
		else return WAIT;
	}
	
}
