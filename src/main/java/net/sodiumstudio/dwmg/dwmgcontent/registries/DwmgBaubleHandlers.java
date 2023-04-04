package net.sodiumstudio.dwmg.dwmgcontent.registries;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.sodiumstudio.dwmg.dwmgcontent.Dwmg;

@Mod.EventBusSubscriber(modid = Dwmg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DwmgBaubleHandlers {

	@SubscribeEvent
	public static void registerBaubleHandlers(FMLCommonSetupEvent event)
	{
		
	}
	
}
