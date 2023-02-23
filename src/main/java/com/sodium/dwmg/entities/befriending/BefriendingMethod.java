package com.sodium.dwmg.entities.befriending;

import java.util.Map;

import com.github.mechalopa.hmag.world.entity.ZombieGirlEntity;
import com.sodium.dwmg.entities.IBefriendedMob;
import com.sodium.dwmg.entities.ZombieGirlFriendly;
import com.sodium.dwmg.registries.ModCapabilities;
import com.sodium.dwmg.registries.ModEntityTypes;
import com.sodium.dwmg.registries.ModItems;
import com.sodium.dwmg.util.NbtHelper;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.LogicalSide;

public class BefriendingMethod 
{
	
	public BefriendingMethod()
	{	
	}

	public EntityType<?> getTypeAfterBefriending(EntityType<?> type)
	{
		if (type == com.github.mechalopa.hmag.registry.ModEntityTypes.ZOMBIE_GIRL.get())
			return ModEntityTypes.ZOMBIE_GIRL_FRIENDLY.get();
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

		if(!target.getCapability(ModCapabilities.CAP_BEFRIENDABLE_MOB).isPresent())
			throw new RuntimeException("Befriending: Target living entity not having CapBefriendableMob capability attached.");
		EntityType<?> newType = getTypeAfterBefriending(target.getType());
		if(newType == null)
			throw new RuntimeException("Befriending: Entity type after befriending is not valid. Check BefriendingMethod.getTypeAfterBefriending function.");
		Entity resultRaw = BefriendingUtil.replaceLivingEntity(newType, target);
		if(!(resultRaw instanceof LivingEntity))
			throw new RuntimeException("Befriending: Entity type after befriending is not a living entity. Check BefriendingMethod.getTypeAfterBefriending function.");
		if(!(resultRaw instanceof IBefriendedMob))
			throw new RuntimeException("Befriending: Entity type after befriending not implementing IBefriendedMob interface.");
		IBefriendedMob result = (IBefriendedMob)resultRaw;
		result.init(player, target);		
		return result;
	}
	
	public IBefriendedMob onBefriendingMobInteract(EntityInteract event)
	{
		LivingEntity entity = (LivingEntity)event.getEntity();
		EntityType<?> type = event.getEntity().getType();
		Player player = event.getPlayer();
		IBefriendedMob result = null;
		
		entity.getCapability(ModCapabilities.CAP_BEFRIENDABLE_MOB).ifPresent((l) -> {
			
			// Server-side actions
			if (event.getSide() != LogicalSide.CLIENT)
			{
				if (!l.isInHatred(player))
				{
					if (type == com.github.mechalopa.hmag.registry.ModEntityTypes.ZOMBIE_GIRL.get())
					{
						if (!player.isShiftKeyDown() && player.getMainHandItem().getItem() == ModItems.SOUL_CAKE_SLICE.get())
						{
							if (l.getNBT().getCompound("player_data").contains(player.getStringUUID())
									&& l.getNBT().getCompound("player_data").getCompound(player.getStringUUID()).contains("cakes_needed"))
								l.getNBT().getCompound("player_data").);
							
					
						}
					}
				}
			}
		});
		return result;
	}
	
}
