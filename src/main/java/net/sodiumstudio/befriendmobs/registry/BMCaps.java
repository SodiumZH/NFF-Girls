package net.sodiumstudio.befriendmobs.registry;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.sodiumstudio.befriendmobs.entity.befriended.CBefriendedMobData;
import net.sodiumstudio.befriendmobs.entity.capability.CAttributeMonitor;
import net.sodiumstudio.befriendmobs.entity.capability.CBMPlayerModule;
import net.sodiumstudio.befriendmobs.entity.capability.CBefriendableMob;
import net.sodiumstudio.befriendmobs.entity.capability.CHealingHandler;
import net.sodiumstudio.befriendmobs.entity.capability.CLivingEntityDelayedActionHandler;
import net.sodiumstudio.befriendmobs.item.capability.CItemStackMonitor;
import net.sodiumstudio.befriendmobs.level.CBMLevelModule;
import net.sodiumstudio.nautils.capability.CEntityTickingCapability;

public class BMCaps {

	// Functional caps
	public static final Capability<CBefriendableMob> CAP_BEFRIENDABLE_MOB = CapabilityManager.get(new CapabilityToken<>(){});
	public static final Capability<CHealingHandler> CAP_HEALING_HANDLER = CapabilityManager.get(new CapabilityToken<>(){});
	public static final Capability<CAttributeMonitor> CAP_ATTRIBUTE_MONITOR = CapabilityManager.get(new CapabilityToken<>(){});
	public static final Capability<CItemStackMonitor> CAP_ITEM_STACK_MONITOR = CapabilityManager.get(new CapabilityToken<>(){});
	
	// Caps for data storage only
	public static final Capability<CBefriendedMobData> CAP_BEFRIENDED_MOB_DATA = CapabilityManager.get(new CapabilityToken<>(){});
	
	// General functional capabilities
	public static final Capability<CBMPlayerModule> CAP_BM_PLAYER = CapabilityManager.get(new CapabilityToken<>(){});
	public static final Capability<CBMLevelModule> CAP_BM_LEVEL = CapabilityManager.get(new CapabilityToken<>(){});
	public static final Capability<CLivingEntityDelayedActionHandler> CAP_DELAYED_ACTION_HANDLER = CapabilityManager.get(new CapabilityToken<>(){});
	
	static {
		CEntityTickingCapability.registerTicking(CAP_BEFRIENDED_MOB_DATA);
	}
	
	@SubscribeEvent
	public static void register(RegisterCapabilitiesEvent event)
	{
		// Entities
		event.register(CBefriendableMob.class);
		event.register(CHealingHandler.class);
		event.register(CAttributeMonitor.class);
		//event.register(CBaubleDataCache.class);
		event.register(CBefriendedMobData.class);
		event.register(CItemStackMonitor.class);
		event.register(CBMPlayerModule.class);
		event.register(CBMLevelModule.class);
		event.register(CLivingEntityDelayedActionHandler.class);
	}
}
