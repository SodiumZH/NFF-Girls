package net.sodiumstudio.dwmg.registries;


import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumstudio.dwmg.entities.capabilities.CFavorabilityHandler;
import net.sodiumstudio.dwmg.entities.capabilities.CLevelHandler;
import net.sodiumstudio.dwmg.entities.capabilities.CUndeadMob;
import net.sodiumstudio.dwmg.entities.vanillatrade.CDwmgTradeHandler;
import net.sodiumstudio.nautils.entity.vanillatrade.CVanillaMerchant;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DwmgCapabilities
{

	public static Capability<CUndeadMob> CAP_UNDEAD_MOB = CapabilityManager.get(new CapabilityToken<>(){});
	public static Capability<CFavorabilityHandler> CAP_FAVORABILITY_HANDLER = CapabilityManager.get(new CapabilityToken<>(){});
	public static Capability<CLevelHandler> CAP_LEVEL_HANDLER = CapabilityManager.get(new CapabilityToken<>(){});
	public static Capability<CDwmgTradeHandler> CAP_TRADE_HANDLER = CapabilityManager.get(new CapabilityToken<>(){});
	
	// Register capabilities
	@SubscribeEvent
	public static void register(RegisterCapabilitiesEvent event)
	{
		// Entities
		event.register(CUndeadMob.class);
		event.register(CFavorabilityHandler.class);
		event.register(CLevelHandler.class);
		event.register(CDwmgTradeHandler.class);
	}
	
	
}
