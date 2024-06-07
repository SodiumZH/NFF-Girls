package net.sodiumstudio.dwmg.entities.vanillatrade;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.events.hooks.DwmgHooks;
import net.sodiumstudio.dwmg.registries.DwmgItems;
import net.sodiumstudio.nautils.InfoHelper;

@Mod.EventBusSubscriber(modid = Dwmg.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DwmgTradeClientEventHandlers
{
	@SubscribeEvent
	public static void onMerchantOfferUnavailableInfo(DwmgHooks.MerchantOfferUnavaliableInfoEvent event)
	{
		var tradingMob = event.searchOngoingDwmgTrader(16d);
		if (tradingMob != null)
		{
			if (tradingMob.isValidOffers() && tradingMob.getMeta().size() > event.activeOfferIndex)
			{
				if (event.activeOffer.getResult().is(DwmgItems.TRADE_INTRODUCTION_LETTER.get()))
				{
					event.setInfo(InfoHelper.createTranslatable("info.dwmg.introduction_unavailable", tradingMob.getPoints(), tradingMob.getPointsPerIntroduction()));
				}
				else if (tradingMob.getMeta().get(event.activeOfferIndex).requiredMerchantLevel > tradingMob.getMerchantLevel())
				{
					int lvlRequired = CDwmgTradeHandler.LEVEL_REQUIREMENTS[tradingMob.getMeta().get(event.activeOfferIndex).requiredMerchantLevel - 1];
					event.setInfo(InfoHelper.createTranslatable("info.dwmg.trade_level_not_satisfied", lvlRequired));
				}
				else event.setInfo(InfoHelper.createTranslatable("info.dwmg.trade_out_of_stock"));
			}
			else event.noInfo();
		}
	}
}
