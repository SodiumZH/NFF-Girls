package net.sodiumstudio.dwmg.entities.vanillatrade;

import com.github.mechalopa.hmag.registry.ModItems;

import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumstudio.nautils.NaUtils;
import net.sodiumstudio.nautils.entity.vanillatrade.VanillaMerchantRegisterListingEvent;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.registries.*;

@Mod.EventBusSubscriber(modid = Dwmg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DwmgTradeSetupEventHandlers
{
	
	@SubscribeEvent
	public static void registerTradeListing(VanillaMerchantRegisterListingEvent event)
	{
		event.push(DwmgEntityTypes.HMAG_ZOMBIE_GIRL.get()).addListings(
				DwmgTradeListings.buys(ModItems.SOUL_POWDER.get(), 10, 14, 3),
				DwmgTradeListings.sells(DwmgItems.DEATH_CRYSTAL_POWDER.get().getDefaultInstance(), 3, 5, 3))
			.pop()
			.link(DwmgEntityTypes.HMAG_SKELETON_GIRL.get(), DwmgEntityTypes.HMAG_ZOMBIE_GIRL.get());
	}
	
	
}
