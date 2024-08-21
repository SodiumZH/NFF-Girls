package net.sodiumstudio.befriendmobs.events;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.sodiumstudio.befriendmobs.BefriendMobs;
import net.sodiumstudio.befriendmobs.bmevents.setup.ModifyAttributeEvent;
import net.sodiumstudio.befriendmobs.bmevents.setup.RegisterBefriendingTypeEvent;

@Mod.EventBusSubscriber(modid = BefriendMobs.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BMSetupEvents {

	@SubscribeEvent
	public static void commonSetup(FMLCommonSetupEvent event)
	{
		event.enqueueWork(() ->
		{
			ModLoader.get().postEvent(new RegisterBefriendingTypeEvent());
			// For display atk in gui
			Attributes.ATTACK_DAMAGE.setSyncable(true);
		});
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onAttributeCreate(EntityAttributeCreationEvent event)
	{
		// Attribute modification should be posted right before creating entity attributes
		ModLoader.get().postEvent(new ModifyAttributeEvent());
	}
}
