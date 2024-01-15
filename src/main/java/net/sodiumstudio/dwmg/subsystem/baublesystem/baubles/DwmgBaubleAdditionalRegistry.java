package net.sodiumstudio.dwmg.subsystem.baublesystem.baubles;

import java.util.HashMap;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.sodiumstudio.dwmg.Dwmg;

/**
 * Registry of DWMG additional properties for bauble items.
 */
@EventBusSubscriber(modid = Dwmg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DwmgBaubleAdditionalRegistry
{
	// ResourceLocation key, tier
	private static final HashMap<Item, Tuple<ResourceLocation, Integer>> REGISTRY = new HashMap<>();

	@SubscribeEvent
	public static void fireRegisterEvent(FMLCommonSetupEvent event)
	{
		event.enqueueWork(() -> {
			ModLoader.get().postEvent(new DwmgBaubleAdditionalRegistry.RegisterEvent());
		});
	}
	
	public static HashMap<Item, Tuple<ResourceLocation, Integer>> getRegistry()
	{
		return REGISTRY;
	}
	
	public static class RegisterEvent extends Event implements IModBusEvent
	{
		public void register(Item item, ResourceLocation key, Integer tier)
		{
			DwmgBaubleAdditionalRegistry.REGISTRY.put(item, new Tuple<>(key, tier));
		}
		
		public void register(DwmgDedicatedBaubleItem item)
		{
			DwmgBaubleAdditionalRegistry.REGISTRY.put(item, new Tuple<>(item.additionalKey, item.tier));
		}
	}
}
