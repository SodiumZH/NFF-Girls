package net.sodiumstudio.dwmg.registries;

import com.github.mechalopa.hmag.registry.ModEntityTypes;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.sodiumstudio.befriendmobs.entitiy.befriending.registry.BefriendingTypeRegistry;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.entities.befriending.HandlerZombieGirl;

@Mod.EventBusSubscriber(modid = Dwmg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBefriendingTypes {

	@SubscribeEvent
	public static void registerBefriendingType(FMLCommonSetupEvent event)
	{
		BefriendingTypeRegistry.register(ModEntityTypes.ZOMBIE_GIRL.get(), DwmgEntityTypes.BEF_ZOMBIE_GIRL.get(), new HandlerZombieGirl());
		return;
	}
	
	
}
