package com.sodium.dwmg.befriendmobsapi.entitiy.befriending;

import java.util.Random;

import com.sodium.dwmg.befriendmobsapi.entitiy.IBefriendedMob;
import com.sodium.dwmg.befriendmobsapi.registry.RegCapabilities;
import com.sodium.dwmg.befriendmobsapi.util.Debug;
import com.sodium.dwmg.befriendmobsapi.util.EntityHelper;
import com.sodium.dwmg.befriendmobsapi.util.NbtHelper;
import com.sodium.dwmg.registries.ModCapabilities;
import com.sodium.dwmg.registries.ModEffects;
import com.sodium.dwmg.registries.ModEntityTypes;
import com.sodium.dwmg.registries.ModItems;

import net.minecraft.nbt.IntTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public abstract class AbstractBefriendingHandler 
{
	public AbstractBefriendingHandler()
	{	
	}

	public EntityType<?> getTypeAfterBefriending(EntityType<?> type)
	{
		if (type == com.github.mechalopa.hmag.registry.ModEntityTypes.ZOMBIE_GIRL.get())
			return ModEntityTypes.BEF_ZOMBIE_GIRL.get();
		// Add more types here using else if
		
		else return null;
	}
	
	public IBefriendedMob befriend(Player player, LivingEntity target)
	{
		// Don't execute on client
		if (target.level.isClientSide())
			return null;
		// Don't execute on mobs already befriended
		if (target instanceof IBefriendedMob)
			return null;		

		if(!target.getCapability(RegCapabilities.CAP_BEFRIENDABLE_MOB).isPresent())
			throw new RuntimeException("Befriending: Target living entity not having CapBefriendableMob capability attached.");
		EntityType<?> newType = getTypeAfterBefriending(target.getType());
		if(newType == null)
			throw new RuntimeException("Befriending: Entity type after befriending is not valid. Check BefriendingMethod.getTypeAfterBefriending function.");
		Entity resultRaw = EntityHelper.replaceLivingEntity(newType, target);
		if(!(resultRaw instanceof LivingEntity))
			throw new RuntimeException("Befriending: Entity type after befriending is not a living entity. Check BefriendingMethod.getTypeAfterBefriending function.");
		if(!(resultRaw instanceof IBefriendedMob))
			throw new RuntimeException("Befriending: Entity type after befriending not implementing IBefriendedMob interface.");
		IBefriendedMob result = (IBefriendedMob)resultRaw;
		result.init(player.getUUID(), target);		
		Debug.printToScreen("Mob "+target.toString()+" befriended", player, target);
		return result;
	}

	
	public abstract BefriendableMobInteractionResult onBefriendableMobInteract(BefriendableMobInteractArguments args);
	
}
