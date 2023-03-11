package net.sodiumstudio.dwmg.befriendmobs.entitiy.ai;

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
				id == 1 ? FOLLOW : 
				id == 2 ? WANDER :
					null);
	}
	
	public static BefriendedAIState fromID(int id)
	{
		return fromID((byte)id);
	}
	
	public BefriendedAIState defaultSwitch()
	{
		return fromID(id() + 1 ) != null ? fromID(id() + 1) : fromID(0);
	}
	
}
