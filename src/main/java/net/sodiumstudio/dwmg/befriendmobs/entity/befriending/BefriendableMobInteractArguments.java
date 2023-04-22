package net.sodiumstudio.dwmg.befriendmobs.entity.befriending;

import javax.annotation.Nonnull;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.NonNullConsumer;
import net.minecraftforge.fml.LogicalSide;
import net.sodiumstudio.dwmg.befriendmobs.entity.capability.CBefriendableMob;
import net.sodiumstudio.dwmg.befriendmobs.registry.BefMobCapabilities;
import net.sodiumstudio.dwmg.dwmgcontent.registries.DwmgCapabilities;

public class BefriendableMobInteractArguments {

	private LogicalSide side;
	private Player player;
	private Mob target;
	private InteractionHand hand;
	
	private BefriendableMobInteractArguments()
	{
		side = LogicalSide.SERVER;
		player = null;
		target = null;
		hand = null;
	}
	
	public static BefriendableMobInteractArguments of(LogicalSide side, @Nonnull Player player, @Nonnull Mob target, @Nonnull InteractionHand hand)
	{
		BefriendableMobInteractArguments res = new BefriendableMobInteractArguments();
		res.side = side;
		res.player = player;
		res.target = target;
		res.hand = hand;
		if (!target.getCapability(BefMobCapabilities.CAP_BEFRIENDABLE_MOB).isPresent())
			throw new IllegalStateException("BefriendableMobInteraction event: target is not befriendable.");
		return res;
	}
	
	public LogicalSide getSide()
	{
		return side;
	}
	
	public boolean isClient()
	{
		return side == LogicalSide.CLIENT;
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public Mob getTarget()
	{
		return target;
	}
	
	public InteractionHand getHand()
	{
		return hand;
	}
	
	public boolean isMainHand()
	{
		return hand == InteractionHand.MAIN_HAND;
	}
	
	private void execInternal(NonNullConsumer<CBefriendableMob> consumer)
	{
		target.getCapability(BefMobCapabilities.CAP_BEFRIENDABLE_MOB).ifPresent(consumer);
	}
	
	// Do something with the mob's capability, both on server and client
	public void execBoth(NonNullConsumer<CBefriendableMob> consumer)
	{
		execInternal(consumer);
	}
	
	// Do something with the mob's capability, only on client
	public void execClient(NonNullConsumer<CBefriendableMob> consumer)
	{
		if (isClient())
			execInternal(consumer);
	}
	
	// Do something with the mob's capability, only on server	
	public void execServer(NonNullConsumer<CBefriendableMob> consumer)
	{
		if (!isClient())
			execInternal(consumer);
	}
	
	// Get target mob as CBefriendableMob capability
	public CBefriendableMob asCap()
	{
		return CBefriendableMob.getCap(target);
		
	}
	
}
