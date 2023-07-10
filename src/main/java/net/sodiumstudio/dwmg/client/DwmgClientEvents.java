package net.sodiumstudio.dwmg.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.sodiumstudio.befriendmobs.client.gui.screens.BefriendedGuiScreen;
import net.sodiumstudio.dwmg.Dwmg;

@Mod.EventBusSubscriber(modid = Dwmg.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DwmgClientEvents
{
	
	@SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent event) {
		
		if (event.side == LogicalSide.CLIENT)
		{
			@SuppressWarnings("resource")
			Minecraft mc = Minecraft.getInstance();
			if (mc.screen != null && mc.screen instanceof BefriendedGuiScreen bgs)
			{
				if (bgs.mob.asMob().isAlive() 
						&& bgs.mob.asMob().isAddedToWorld() 
						&& bgs.mob.asMob().distanceToSqr(bgs.mob.getOwner()) > 64.d)
				{
					mc.setScreen(null);
				}
				
			}
		}
	}
}
