package net.sodiumstudio.befriendmobs.events;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumstudio.befriendmobs.BefriendMobs;
import net.sodiumstudio.befriendmobs.item.MobRespawnerItem;
import net.sodiumstudio.befriendmobs.item.MobRespawnerInstance;
import net.sodiumstudio.befriendmobs.registry.BMCaps;
import net.sodiumstudio.nautils.EntityHelper;
import net.sodiumstudio.nautils.Wrapped;

// Handle Item, Item Stack and Item Entity events
@SuppressWarnings("removal")
@Mod.EventBusSubscriber(modid = BefriendMobs.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BMItemEvents
{
	
	@SubscribeEvent
	public static void onItemEntityJoinWorld(EntityJoinLevelEvent event)
	{
		// Initialize mob respawner invulnerable
		if (event.getEntity() instanceof ItemEntity ie)
		{
			MobRespawnerInstance ins = MobRespawnerInstance.create(ie.getItem());
			if (ins != null && ins.isInvulnerable())
			{
				ie.setInvulnerable(true);
			}	
		}
	}
	
	@SuppressWarnings("unchecked")
	@SubscribeEvent
	public static void onServerItemEntityPreWorldTick(ServerEntityTickEvent.PreWorldTick event)
	{
		if (event.entity instanceof ItemEntity itementity)
		{
			// Handle respawner item entity falling into void
			if (itementity.getY() < (double)(itementity.level.getMinBuildHeight() - 1))
			{
				MobRespawnerInstance ins = MobRespawnerInstance.create(itementity.getItem());
				if (ins != null && ins.recoverInVoid())
				{
					// Lift onto y=64
					itementity.setPos(new Vec3(itementity.getX(), 64d, itementity.getZ()));
				if (!EntityHelper.tryTeleportOntoGround(itementity, new Vec3(16d, 16d, 16d), 32))
					{
						// If cannot teleport onto ground, let it float
						itementity.setNoGravity(true);
					}
					itementity.setDeltaMovement(new Vec3(0d, 0d, 0d));

				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onItemEntityPickUp(EntityItemPickupEvent event)
	{
		if (event.getItem().getItem().getItem() instanceof MobRespawnerItem)
		{
			/**  There was an unknown bug that player will still pick up item even if the inventory
			 *   is full, causing item permanent loss
			 **/
			if (event.getEntity().getInventory().getFreeSlot() == -1)
			{
				event.setCanceled(true);
			}
		}
	}
}
