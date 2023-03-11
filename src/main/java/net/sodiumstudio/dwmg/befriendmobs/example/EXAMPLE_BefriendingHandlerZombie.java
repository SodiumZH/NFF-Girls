package net.sodiumstudio.dwmg.befriendmobs.example;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending.AbstractBefriendingHandler;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending.BefriendableMobInteractArguments;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending.BefriendableMobInteractionResult;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending.registry.BefriendingTypeRegistry;
import net.sodiumstudio.dwmg.befriendmobs.registry.RegItems;
import net.sodiumstudio.dwmg.befriendmobs.util.EntityHelper;

// We set that using an item "EXAMPLE Zombie Befriending Item" befriends the zombie
public class EXAMPLE_BefriendingHandlerZombie extends AbstractBefriendingHandler
{

	// This is a very simple example.
	// For more complicated interaction logics, you could see my source code of projects depending on the Befriend Mobs system.
	@Override
	public BefriendableMobInteractionResult handleInteract(BefriendableMobInteractArguments args) {
			
		BefriendableMobInteractionResult result = new BefriendableMobInteractionResult();
		
		/** You can use BefriendableMobInteractArguments methods "execServer", "execClient" and "execBoth" to make actions on specific side(s)
		* instead of multiple if-else checks.
		* The consumer input is the "CBefriendableMob" capability on the target mob.
		* "CBefriendableMob" capability is automatically applied to all befriendable mobs, so you don't need to configure it manually on initializing. 
		* See CBefriendableMob.java and CBefriendableMobImpl.java for details.
		* This interaction method will be called for both client/server, main/off hand, 4 times overall on each right click.
		*/
		args.execServer((cap) -> 
		{
			// Check player has an EXAMPLE Zombie Befriending Item on main hand
			if (args.isMainHand() && args.getPlayer().getMainHandItem().getItem() == RegItems.EXAMPLE_ZOMBIE_BEFRIENDING_ITEM.get())
			{
				IBefriendedMob bef = BefriendingTypeRegistry.getHandler((EntityType<? extends Mob>) args.getTarget().getType())
					.befriend(args.getPlayer(), args.getTarget());
				// WARNING: now args.getTarget() has been invalidated, so we must send particles to the new mob
				EntityHelper.sendHeartParticlesToMob(bef.asMob());
				// Don't forget to set the result
				result.befriendedMob = bef;
				result.setHandled();
			}
		});
		return result;
	}


}
