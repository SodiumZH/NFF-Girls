package net.sodiumstudio.nautils.entity.vanillatrade;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.nautils.NaUtils;

@Mod.EventBusSubscriber(modid = Dwmg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class VanillaTradeSetupEventHandlers
{
	@SubscribeEvent
	public static void onCommonSetup(FMLCommonSetupEvent event)
	{
		event.enqueueWork(() -> ModLoader.get().postEvent(new VanillaMerchantRegisterListingEvent()));
	}
}
