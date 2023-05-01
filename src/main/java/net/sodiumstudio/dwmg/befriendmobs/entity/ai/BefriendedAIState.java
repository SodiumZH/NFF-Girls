package net.sodiumstudio.dwmg.befriendmobs.entity.ai;

import java.util.function.Function;

import net.minecraft.network.chat.MutableComponent;
import net.sodiumstudio.dwmg.befriendmobs.util.InfoHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.exceptions.UnimplementedException;

public enum BefriendedAIState {
	WAIT(0),
	FOLLOW(1),
	WANDER(2),
	CUSTOM_0(99),
	CUSTOM_1(98),
	CUSTOM_2(97);
	
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
		for (BefriendedAIState state: BefriendedAIState.values())
		{
			if (state.id() == id)
				return state;
		}
		return null;
	}
	
	public static BefriendedAIState fromID(int id)
	{
		return fromID((byte)id);
	}
	
	public BefriendedAIState defaultSwitch()
	{
		return fromID(id() + 1 ) != null ? fromID(id() + 1) : fromID(0);
	}
	
	public MutableComponent getDisplayInfo()
	{
		switch (this)
		{
		case WAIT:
		{
			return InfoHelper.createTrans("info.befriendmobs.mob_wait");
		}
		case FOLLOW:
		{
			return InfoHelper.createTrans("info.befriendmobs.mob_follow");
		}
		case WANDER:
		{
			return InfoHelper.createTrans("info.befriendmobs.mob_wander");
		}
		}
		throw new UnimplementedException("Befriended AI State display info not implemented.");
	}
	
	// Change this function if any additional states are added
	public static Function<BefriendedAIState, MutableComponent> getDisplayInfo = BefriendedAIState::getDisplayInfo;
}
