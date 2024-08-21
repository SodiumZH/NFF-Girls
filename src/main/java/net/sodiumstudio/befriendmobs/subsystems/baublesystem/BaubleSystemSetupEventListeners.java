package net.sodiumstudio.befriendmobs.subsystems.baublesystem;

import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.sodiumstudio.befriendmobs.BefriendMobs;

@Mod.EventBusSubscriber(modid = BefriendMobs.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
class BaubleSystemSetupEventListeners
{

	@SubscribeEvent
	public static void baubleSystemSetup(FMLCommonSetupEvent event)
	{
		event.enqueueWork(() -> 
		{
			ModLoader.get().postEvent(new RegisterBaublesEvent());
			BaubleRegistries.clearAndFillRegistries();
			ModLoader.get().postEvent(new ModifyBaubleRegistriesEvent());
			BaubleRegistries.sortSingleRegistry();
			ModLoader.get().postEvent(new RegisterBaubleEquippableMobsEvent());
			ModLoader.get().postEvent(new ModifyBaubleEquippableMobsEvent());
		});
	}

	@SubscribeEvent
	public static void register(RegisterCapabilitiesEvent event)
	{
		event.register(CBaubleEquippableMob.class);
	}
}
