package net.sodiumstudio.dwmg.dwmgcontent.registries;


import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumstudio.dwmg.dwmgcontent.entities.capabilities.CUndeadMob;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DwmgCapabilities
{

	public static Capability<CUndeadMob> CAP_UNDEAD_MOB = CapabilityManager.get(new CapabilityToken<>(){});
	
	// Register capabilities
	@SubscribeEvent
	public static void register(RegisterCapabilitiesEvent event)
	{
		// Entities
		event.register(CUndeadMob.class);
	}
	
	
}
