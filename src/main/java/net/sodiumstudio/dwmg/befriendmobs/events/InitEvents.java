package net.sodiumstudio.dwmg.befriendmobs.events;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.sodiumstudio.dwmg.befriendmobs.BefriendMobs;

@Mod.EventBusSubscriber(modid = BefriendMobs.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class InitEvents
{

	@SubscribeEvent
	public static void onInit(FMLCommonSetupEvent event)
	{
		// For display atk in gui
		Attributes.ATTACK_DAMAGE.setSyncable(true);
	}
}
