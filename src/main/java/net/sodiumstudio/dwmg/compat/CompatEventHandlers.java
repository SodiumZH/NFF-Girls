package net.sodiumstudio.dwmg.compat;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.entities.IDwmgBefriendedMob;

/**
 * Methods handling compatibility issues
 */
@Mod.EventBusSubscriber(modid = Dwmg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CompatEventHandlers
{
	protected static final String TF_MOD_ID = "twilightforest";
	protected static final String FAA_MOD_ID = "forbidden_arcanus";
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onFinalizeLivingHurt(LivingHurtEvent event)
	{
		
		// Fix TF Seeker Arrow targeting BM
		// Now it's impossible to prevent the arrow from targeting BM, so remove damage only
		// TODO: fully fix this after TF inserts event
		if (event.getEntity() instanceof IDwmgBefriendedMob bm)
		{
			if (event.getSource().getDirectEntity() != null
					&& EntityType.getKey(event.getSource().getDirectEntity().getType())
					.equals(new ResourceLocation(TF_MOD_ID, "seeker_arrow")))
			{
				Projectile proj = (Projectile) event.getSource().getDirectEntity();
				if (proj.getOwner() == bm.getOwner())
					event.setCanceled(true);
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onEntityInteract(EntityInteract event)
	{
		// Fix interaction conflict with FAA Quantum Catcher
		if (ForgeRegistries.ITEMS.getKey(event.getEntity().getItemInHand(event.getHand()).getItem())
				.equals(new ResourceLocation(FAA_MOD_ID, "quantum_catcher")) 
				&& event.getTarget() instanceof IDwmgBefriendedMob bm 
				&& bm.getOwner() == event.getEntity())
		{
			event.setCanceled(true);
		}		
	}
	
	
}
