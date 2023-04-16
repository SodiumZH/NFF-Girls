package net.sodiumstudio.dwmg.befriendmobs.events;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import net.sodiumstudio.dwmg.befriendmobs.entity.befriending.BefriendableAddHatredReason;
import net.sodiumstudio.dwmg.befriendmobs.util.MiscUtil;

@Cancelable
public class BefriendableAddHatredEvent extends Event
{
	public final Mob mob;
	public final Player toAdd;
	public int ticks;
	public final BefriendableAddHatredReason reason;
	public final boolean isPermanent;

	public BefriendableAddHatredEvent(Mob mob, Player toAdd, int ticks, BefriendableAddHatredReason reason)
	{
		this.mob = mob;
		this.toAdd = toAdd;
		this.ticks = ticks;
		this.reason = reason;
		this.isPermanent = ticks < 0;
	}
}
