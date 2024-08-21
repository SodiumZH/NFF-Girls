package net.sodiumstudio.befriendmobs.entity.befriending.handlerpreset;

import java.util.Random;
import java.util.UUID;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.entity.befriending.BefriendableMobInteractArguments;
import net.sodiumstudio.befriendmobs.entity.befriending.BefriendableMobInteractionResult;
import net.sodiumstudio.befriendmobs.entity.befriending.BefriendingHandler;
import net.sodiumstudio.nautils.EntityHelper;
import net.sodiumstudio.nautils.NbtHelper;

public abstract class HandlerItemGiving extends BefriendingHandler
{
	
	protected Random rnd = new Random();
	
	/** Check if the mob accepts the item.
	 * @deprecated Use ItemStack sensitive version instead
	 */
	@Deprecated
	public final boolean isItemAcceptable(Item item) {
		return isItemAcceptable(item.getDefaultInstance());
	}
	
	/** Check if the mob accepts the item.
	 */
	public abstract boolean isItemAcceptable(ItemStack itemstack);
	
	/**
	 * If true, the item should consume after using
	 * @deprecated Use ItemStack sensitive version instead
	 */ 
	@Deprecated
	public final boolean shouldItemConsume(Item item) 
	{
		return shouldItemConsume(item.getDefaultInstance());
	}
	
	/**
	 * If true, the item should consume after using
	 */
	public boolean shouldItemConsume(ItemStack itemstack)
	{
		return true;
	}
	
	// Additional conditions when giving items
	public abstract boolean additionalConditions(Player player, Mob mob);
	
	// If true, the action can proceed even if the player is in the hatred list
	public boolean shouldIgnoreHatred() {return false;}
	
	// Actions when the item condition is satisfied
	// If the mob is befriended immediately, return it. Otherwise return null.
	public IBefriendedMob finalActions(Player player, Mob mob)
	{
		sendParticlesOnBefriended(mob);
		return befriend(player, mob);
	}
	
	public abstract int getItemGivingCooldownTicks();

	public void sendParticlesOnBefriended(Mob target)
	{
		EntityHelper.sendHeartParticlesToLivingDefault(target);
	}
	
	// If true, disable actions if the mob is a passenger
	public boolean shouldBlockOnRiding()
	{
		return true;
	}
	
	/**
	 * Executed after item is given.
	 * Not executed when the condition is satisfied after giving. Handle this case in finalActions().
	 */
	public void afterItemGiven(Player player, Mob mob, ItemStack item) {}
	
}
