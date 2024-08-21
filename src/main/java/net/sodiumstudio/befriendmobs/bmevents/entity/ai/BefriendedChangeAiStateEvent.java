package net.sodiumstudio.befriendmobs.bmevents.entity.ai;

import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import net.sodiumstudio.befriendmobs.entity.ai.BefriendedAIState;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;

@Cancelable
public class BefriendedChangeAiStateEvent extends Event
{

	protected IBefriendedMob mob;
	protected BefriendedAIState stateBefore;
	protected BefriendedAIState stateAfter;
	public BefriendedChangeAiStateEvent(IBefriendedMob mob, BefriendedAIState stateBefore, BefriendedAIState stateAfter)
	{
		this.mob = mob;
		this.stateBefore = stateBefore;
		this.stateAfter = stateAfter;
	}
	public IBefriendedMob getMob()
	{
		return mob;
	}
	public BefriendedAIState getStateBefore()
	{
		return stateBefore;
	}
	public BefriendedAIState getStateAfter()
	{
		return stateAfter;
	}
	
}
