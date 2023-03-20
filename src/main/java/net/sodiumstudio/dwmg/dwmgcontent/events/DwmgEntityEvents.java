package net.sodiumstudio.dwmg.dwmgcontent.events;

import com.github.mechalopa.hmag.registry.ModItems;
import com.github.mechalopa.hmag.world.entity.CreeperGirlEntity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumstudio.dwmg.befriendmobs.BefriendMobs;
import net.sodiumstudio.dwmg.befriendmobs.events.BefriendedDeathEvent;
import net.sodiumstudio.dwmg.befriendmobs.registry.BefMobCapabilities;
import net.sodiumstudio.dwmg.befriendmobs.util.EntityHelper;
import net.sodiumstudio.dwmg.dwmgcontent.entities.hmag.EntityBefriendedCreeperGirl;

@Mod.EventBusSubscriber(modid = BefriendMobs.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DwmgEntityEvents
{

	@SubscribeEvent
	public static void onBefDie(BefriendedDeathEvent event)
	{
		if (event.getDamageSource().getEntity() != null)
		{
			event.getDamageSource().getEntity().getCapability(BefMobCapabilities.CAP_BEFRIENDABLE_MOB).ifPresent((l) -> 
			{
				if (event.getDamageSource().getEntity() instanceof CreeperGirlEntity cg)
				{
					// Befriended mobs won't be killed by CreeperGirl's "final explosion". They leave 1 health and get invulnerable for 3s, 
					// preventing them to be killed by falling down after blowed up by the explosion.
					if (l.getNBT().contains("final_explosion_player", 11)
					&& event.getMob().getOwner() != null
					&& l.getNBT().getUUID("final_explosion_player").equals(event.getMob().getOwnerUUID()))
					{
						event.getMob().asMob().setHealth(1.0f);
						event.getMob().asMob().invulnerableTime += 600;
						EntityHelper.sendGreenStarParticlesToLivingDefault(event.getMob().asMob());
						event.setCanceled(true);
						return;
					}
				}
			});
			if (event.isCanceled())
				return;
		}
		
		if (event.getMob() instanceof EntityBefriendedCreeperGirl cg)
		{
			if (cg.isPowered())
				cg.spawnAtLocation(new ItemStack(ModItems.LIGHTNING_PARTICLE.get(), 1));
		}
	}
	
}
