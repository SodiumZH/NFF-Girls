package net.sodiumstudio.dwmg.registries;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.entity.capability.CAttributeMonitor;
import net.sodiumstudio.befriendmobs.item.capability.CItemStackMonitor;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.entities.IDwmgBefriendedMob;
import net.sodiumstudio.dwmg.entities.capabilities.CFavorabilityHandler;
import net.sodiumstudio.dwmg.entities.capabilities.CLevelHandler;
import net.sodiumstudio.dwmg.entities.capabilities.CUndeadMobProvider;
import net.sodiumstudio.dwmg.entities.vanillatrade.CDwmgTradeHandler;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DwmgCapabilityAttachment {

	// Attach capabilities
	@SubscribeEvent
	public static void attachLivingEntityCapabilities(AttachCapabilitiesEvent<Entity> event)
	{
		if(event.getObject() instanceof LivingEntity living)
		{
			MobType mobtype = MobType.UNDEFINED;
			// Some mods may not be able to load mob type at this phase since many parameters are not initialized
			// e.g. Target Dummy. At this time ignore it.
			try {mobtype = living.getMobType();} catch (NullPointerException e) {}
			
			if ((mobtype == MobType.UNDEAD || living.getType().is(DwmgTags.AFFECTED_BY_UNDEAD_AFFINITY))
					&& !(living instanceof IBefriendedMob)  // Befriended mobs aren't affected by Death Affinity
					&& !living.getType().is(DwmgTags.IGNORES_UNDEAD_AFFINITY))	
			{
				event.addCapability(new ResourceLocation(Dwmg.MOD_ID, "cap_undead"), new CUndeadMobProvider());
			}
			IDwmgBefriendedMob bm;
			if ((bm = IDwmgBefriendedMob.getBM(living)) != null)
			{
				event.addCapability(new ResourceLocation(Dwmg.MOD_ID, "cap_favorability"), new CFavorabilityHandler.Prvd(bm.asMob()));
				event.addCapability(new ResourceLocation(Dwmg.MOD_ID, "cap_level"), new CLevelHandler.Prvd(bm.asMob()));
				if (DwmgTrades.TRADES.hasListings(bm.asMob().getType(), VillagerProfession.NONE))
				{
					event.addCapability(new ResourceLocation(Dwmg.MOD_ID, "cap_trade"), 
							new CDwmgTradeHandler.Prvd(bm, DwmgCapabilities.CAP_TRADE_HANDLER));
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void setupAttributeMonitor(CAttributeMonitor.SetupEvent event)
	{
		if (IDwmgBefriendedMob.isBM(event.living))
		{
			event.monitor.listen(Attributes.MAX_HEALTH);
		}
	}
	
	@SubscribeEvent
	public static void setupItemStackMonitor(CItemStackMonitor.SetupEvent event)
	{
		if (event.living instanceof IBefriendedMob b)
		{
			event.monitor.listen("main_hand", () -> event.living.getMainHandItem());
		}
	}
}

