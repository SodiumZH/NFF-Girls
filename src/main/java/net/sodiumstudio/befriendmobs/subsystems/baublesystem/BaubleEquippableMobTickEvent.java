package net.sodiumstudio.befriendmobs.subsystems.baublesystem;

import net.minecraft.world.entity.Mob;
import net.minecraftforge.eventbus.api.Cancelable;
import net.sodiumstudio.nautils.events.NaUtilsLivingEvent;


/**
 * Events posted during Bauble-equippable mob tick.
 * <p><b>Note: DO NOT directly listen to this event!</b> It will post 4 times each tick, 
 * including pre-tick, pre-slot-tick, post-slot-tick, post-tick successively. Listen to specific events instead.
 */
public abstract class BaubleEquippableMobTickEvent extends NaUtilsLivingEvent<Mob>
{
	public final CBaubleEquippableMob capability;
	
	public BaubleEquippableMobTickEvent(Mob mob, CBaubleEquippableMob cap)
	{
		super(mob);
		this.capability = cap;
	}
	
	
	/**
	 * Posted before slots pre-tick but after attribute modifiers update for Bauble-equippable mobs.
	 */
	public static class PreTick extends BaubleEquippableMobTickEvent
	{

		public PreTick(Mob mob, CBaubleEquippableMob cap)
		{
			super(mob, cap);
		}
	}
	
	/**
	 * Posted after slots pre-tick before slots tick for Bauble-equippable mobs.
	 */
	public static class PreSlotTick extends BaubleEquippableMobTickEvent
	{

		public PreSlotTick(Mob mob, CBaubleEquippableMob cap)
		{
			super(mob, cap);
		}
	}
	
	/**
	 * Posted after slots tick before slots post-tick for Bauble-equippable mobs.
	 */
	public static class PostSlotTick extends BaubleEquippableMobTickEvent
	{

		public PostSlotTick(Mob mob, CBaubleEquippableMob cap)
		{
			super(mob, cap);
		}

	}
	
	/**
	 * Posted on each slots ticks.
	 * <p>Cancellable. If cancelled, the slot tick will be omitted.
	 */
	@Cancelable
	public static class SlotTick extends BaubleEquippableMobTickEvent
	{

		public SlotTick(Mob mob, CBaubleEquippableMob cap)
		{
			super(mob, cap);
		}

	}
	
	/**
	 * Posted after slots post-tick for Bauble-equippable mobs.
	 */
	public static class PostTick extends BaubleEquippableMobTickEvent
	{

		public PostTick(Mob mob, CBaubleEquippableMob cap)
		{
			super(mob, cap);
		}
	}
}
