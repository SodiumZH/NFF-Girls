package net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending.registry.BefriendingTypeRegistry;
import net.sodiumstudio.dwmg.befriendmobs.registry.RegCapabilities;
import net.sodiumstudio.dwmg.befriendmobs.util.EntityHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.debug.Debug;

public abstract class AbstractBefriendingHandler 
{
	public AbstractBefriendingHandler()
	{	
	}

	/** If this method is overridden, it should invalidate the input target living entity.
	 * And don't forget to post MobBefriendEvent in the end.
	 */
	public IBefriendedMob befriend(Player player, Mob target)
	{
		// Don't execute on client
		if (target.level.isClientSide())
			return null;
		// Don't execute on mobs already befriended
		if (target instanceof IBefriendedMob)
			return null;		

		// Check if befriendable capability is attached
		if(!target.getCapability(RegCapabilities.CAP_BEFRIENDABLE_MOB).isPresent())
			throw new RuntimeException("Befriending: Target living entity not having CBefriendableMob capability attached.");
		
		// Get new type, and do check
		@SuppressWarnings("unchecked")
		EntityType<? extends Mob> newType = BefriendingTypeRegistry.getConvertTo((EntityType<? extends Mob>) target.getType());
		if (newType == null)
			throw new RuntimeException("Befriending: Entity type after befriending is not valid. Check if the befriendable mob has been registered to BefriendingTypeRegistry.");

		Mob newMob = EntityHelper.replaceMob(newType, target);
		if(!(newMob instanceof IBefriendedMob))
			throw new RuntimeException("Befriending: Entity type after befriending not implementing IBefriendedMob interface.");
		IBefriendedMob newBefMob = (IBefriendedMob)newMob;
		newBefMob.init(player.getUUID(), target);
		newBefMob.setInventoryFromMob();
		Debug.printToScreen("Mob \""+target.getDisplayName().getString()+"\" befriended", player, target);
		newBefMob.setInit();
		return newBefMob;
	}
	
	public abstract BefriendableMobInteractionResult handleInteract(BefriendableMobInteractArguments args);
}
