package net.sodiumzh.nff.girls.entity.vanillatrade;

import net.minecraft.world.InteractionHand;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nautils.statics.NaUtilsInfoStatics;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.INFFGirlTamed;
import net.sodiumzh.nff.girls.registry.NFFGirlsCapabilities;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;

@Mod.EventBusSubscriber(modid = NFFGirls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class NFFGirlsTradeEventListeners
{
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onMobInteract(EntityInteract event)
	{
		if (INFFGirlTamed.isBM(event.getTarget())
				&& !event.getTarget().getLevel().isClientSide()
				&& INFFGirlTamed.getBM(event.getTarget()).getOwnerUUID().equals(event.getEntity().getUUID())
				&& (event.getEntity().getItemInHand(InteractionHand.MAIN_HAND).isEmpty() || event.getEntity().getItemInHand(InteractionHand.MAIN_HAND).is(NFFGirlsItems.EVIL_GEM.get()))
				&& INFFGirlTamed.getBM(event.getTarget()).asMob().getTarget() == null
				&& !event.getEntity().isShiftKeyDown()
				)
		{
			INFFGirlTamed.getBM(event.getTarget()).asMob().getCapability(NFFGirlsCapabilities.CAP_TRADE_HANDLER).ifPresent(cap -> {
				cap.openTradingScreen(event.getEntity(), NaUtilsInfoStatics.createTranslatable("info.nffgirls.open_trade"), 1);
				event.setCanceled(true);
			});
		}
	}
	
	@SubscribeEvent
	public static void onDie(LivingDeathEvent event)
	{
		event.getEntity().getCapability(NFFGirlsCapabilities.CAP_TRADE_HANDLER).ifPresent(cap -> {
			cap.setTradingPlayer(null);
		});
	}
	
	@SuppressWarnings("resource")
	@SubscribeEvent
	public static void onTick(LivingTickEvent event)
	{
		if (!event.getEntity().getLevel().isClientSide)
			event.getEntity().getCapability(NFFGirlsCapabilities.CAP_TRADE_HANDLER).ifPresent(CNFFGirlsTradeHandler::serverTick);
	}
}
