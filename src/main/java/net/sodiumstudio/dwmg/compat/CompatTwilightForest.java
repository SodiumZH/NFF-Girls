package net.sodiumstudio.dwmg.compat;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.sodiumstudio.dwmg.entities.IDwmgBefriendedMob;

/**
 * Handling compatibility issues with Twilight Forest
 */
public class CompatTwilightForest
{
	protected static final String TF_MOD_ID = "twilightforest";

	
//	@SubscribeEvent
	public static void onLivingHurt(LivingHurtEvent event)
	{
		
		if (event.getEntity() instanceof IDwmgBefriendedMob bm)
		{
			if (event.getSource().getDirectEntity() != null
					&& EntityType.getKey(event.getSource().getDirectEntity().getType()).equals(new ResourceLocation(TF_MOD_ID, "seeker_arrow")))
			{
				Projectile proj = (Projectile) event.getSource().getDirectEntity();
				if (proj.getOwner() == bm.getOwner())
					event.setCanceled(true);
			}
		}
	}
	
}
