package net.sodiumstudio.dwmg.subsystem.baublesystem;

import java.util.function.Function;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.BaubleEquippingCondition;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.RegisterBaubleEquippableMobsEvent;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.RegisterBaublesEvent;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.entities.hmag.HmagZombieGirlEntity;
import net.sodiumstudio.dwmg.registries.DwmgItems;
import net.sodiumstudio.dwmg.subsystem.baublesystem.baubles.InsomniaFruitBaubleBehavior;

@EventBusSubscriber(modid = Dwmg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DwmgBaubleRegistrations
{
	
	@SubscribeEvent
	public static void dwmgBaubleAdditionalRegistration(DwmgBaubleAdditionalRegistry.RegisterEvent event)
	{
		event.register(DwmgItems.SOUL_AMULET.get());
		event.register(DwmgItems.SOUL_AMULET_II.get());
	}
	
	private static Function<Mob, ItemStack> accessMobAdditionalInventory(int position)
	{
		return mob -> {
			if (mob instanceof IBefriendedMob bm)
				return bm.getAdditionalInventory().getItem(position);
			else throw new IllegalArgumentException("Input mob isn't IBefriendedMob.");
		};
	}
	
	@SubscribeEvent
	public static void baubleEquippableRegistration(RegisterBaubleEquippableMobsEvent event)
	{
		event.register(HmagZombieGirlEntity.class)
			.addSlot("0", accessMobAdditionalInventory(6))
			.addSlot("1", accessMobAdditionalInventory(7));
	}
	
	@SubscribeEvent
	public static void baubleRegistration(RegisterBaublesEvent event)
	{
		event.register(DwmgItems.SOUL_AMULET.get());
		event.register(DwmgItems.SOUL_AMULET_II.get());
		event.register(new InsomniaFruitBaubleBehavior(new ResourceLocation("dwmg:insomnia_fruit"), BaubleEquippingCondition.always()));
	}
	
	
}
