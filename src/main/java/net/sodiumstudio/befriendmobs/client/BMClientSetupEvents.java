package net.sodiumstudio.befriendmobs.client;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.sodiumstudio.befriendmobs.BefriendMobs;
import net.sodiumstudio.befriendmobs.bmevents.client.RegisterGuiScreenEvent;

@Mod.EventBusSubscriber(modid = BefriendMobs.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BMClientSetupEvents 
{

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event)
	{
		event.enqueueWork(() ->
		{
			ModLoader.get().postEvent(new RegisterGuiScreenEvent());
		});
	}
	
}
