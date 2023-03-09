package com.sodium.dwmg.befriendmobs.entitiy.befriending;

import com.sodium.dwmg.befriendmobs.entitiy.IBefriendedMob;
import com.sodium.dwmg.befriendmobs.entitiy.befriending.registry.BefriendingTypeRegistry;
import com.sodium.dwmg.befriendmobs.event.events.MobBefriendEvent;
import com.sodium.dwmg.befriendmobs.registry.RegCapabilities;
import com.sodium.dwmg.befriendmobs.util.Debug;
import com.sodium.dwmg.befriendmobs.util.EntityHelper;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;

public abstract class AbstractBefriendingHandler 
{
	public AbstractBefriendingHandler()
	{	
	}

	/** If this method is overridden, it should invalidate the input target living entity.
	 * And don't forget to post MobBefriendEvent in the end.
	 */
	public IBefriendedMob befriend(Player player, LivingEntity target)
	{
		// Don't execute on client
		if (target.level.isClientSide())
			return null;
		// Don't execute on mobs already befriended
		if (target instanceof IBefriendedMob)
			return null;		

		if(!target.getCapability(RegCapabilities.CAP_BEFRIENDABLE_MOB).isPresent())
			throw new RuntimeException("Befriending: Target living entity not having CBefriendableMob capability attached.");
		EntityType<?> newType = BefriendingTypeRegistry.getConvertTo(target.getType());
		if(newType == null)
			throw new RuntimeException("Befriending: Entity type after befriending is not valid. Check BefriendingMethod.getTypeAfterBefriending function.");
		Entity resultRaw = EntityHelper.replaceLivingEntity(newType, target);
		if(!(resultRaw instanceof LivingEntity))
			throw new RuntimeException("Befriending: Entity type after befriending is not a living entity. Check BefriendingMethod.getTypeAfterBefriending function.");
		if(!(resultRaw instanceof IBefriendedMob))
			throw new RuntimeException("Befriending: Entity type after befriending not implementing IBefriendedMob interface.");
		IBefriendedMob result = (IBefriendedMob)resultRaw;
		result.init(player.getUUID(), target);		
		Debug.printToScreen("Mob \""+target.getDisplayName().getString()+"\" befriended", player, target);
		if (result != null)
			MinecraftForge.EVENT_BUS.post(new MobBefriendEvent(player, target, result));
		return result;
	}
	
	public abstract BefriendableMobInteractionResult handleInteract(BefriendableMobInteractArguments args);
}
