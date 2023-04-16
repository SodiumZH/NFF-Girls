package net.sodiumstudio.dwmg.befriendmobs.events;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumstudio.dwmg.befriendmobs.BefriendMobs;
import net.sodiumstudio.dwmg.befriendmobs.item.ItemMobRespawner;
import net.sodiumstudio.dwmg.befriendmobs.registry.BefMobCapabilities;
import net.sodiumstudio.dwmg.befriendmobs.util.EntityHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.Wrapped;

// Handle Item, Item Stack and Item Entity events
@SuppressWarnings("removal")
@Mod.EventBusSubscriber(modid = BefriendMobs.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ItemEvents
{
	
	@SubscribeEvent
	public static void onItemEntityJoinWorld(EntityJoinLevelEvent event)
	{
		// Initialize mob respawner invulnerable
		if (event.getEntity() instanceof ItemEntity ie)
		{
			ie.getItem().getCapability(BefMobCapabilities.CAP_MOB_RESPAWNER).ifPresent((c) ->
			{
				if (c.isInvulnerable())
					ie.setInvulnerable(true);
			});			
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
				Wrapped<Boolean> recover = new Wrapped<Boolean>(false);
				itementity.getItem().getCapability(BefMobCapabilities.CAP_MOB_RESPAWNER).ifPresent((c) ->
				{
					recover.set(c.recoverInVoid());
				});
				if (recover.get())
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
		if (event.getItem().getItem().getItem() instanceof ItemMobRespawner)
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
