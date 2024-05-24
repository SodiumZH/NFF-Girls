package net.sodiumstudio.dwmg.entities.vanillatrade;

import net.minecraft.world.InteractionHand;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.entities.IDwmgBefriendedMob;
import net.sodiumstudio.dwmg.registries.DwmgCapabilities;
import net.sodiumstudio.dwmg.registries.DwmgItems;
import net.sodiumstudio.nautils.InfoHelper;

@Mod.EventBusSubscriber(modid = Dwmg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DwmgTradeEventHandlers
{
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onMobInteract(EntityInteract event)
	{
		if (event.getTarget() instanceof IDwmgBefriendedMob mob && event.getEntity().getItemInHand(InteractionHand.MAIN_HAND).is(DwmgItems.EVIL_GEM.get())
				&& !event.getTarget().getLevel().isClientSide())
		{
			mob.asMob().getCapability(DwmgCapabilities.CAP_TRADE_HANDLER).ifPresent(cap -> {
				cap.openTradingScreen(event.getEntity(), InfoHelper.createTranslatable("info.dwmg.open_trade"), 1);
				event.setCanceled(true);
			});
		}
	}
	
	@SubscribeEvent
	public static void onDie(LivingDeathEvent event)
	{
		event.getEntity().getCapability(DwmgCapabilities.CAP_TRADE_HANDLER).ifPresent(cap -> {
			cap.setTradingPlayer(null);
		});
	}
}
