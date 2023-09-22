package net.sodiumstudio.dwmg.entities.item.baublesystem.handlers;

import java.util.Map;
import java.util.function.Predicate;

import com.github.mechalopa.hmag.registry.ModItems;

import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.befriendmobs.item.baublesystem.BaubleHandler;
import net.sodiumstudio.befriendmobs.item.baublesystem.IBaubleHolder;
import net.sodiumstudio.nautils.ContainerHelper;
import net.sodiumstudio.nautils.containers.MapPair;
import net.sodiumstudio.dwmg.registries.DwmgItems;

public class BaubleHandlerGeneral extends BaubleHandler
{
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Predicate<IBaubleHolder>> getItemKeysAccepted(String key, IBaubleHolder mob) {
		return ContainerHelper.mapOf(
				MapPair.of("dwmg:resistance_amulet", null),
				MapPair.of("dwmg:resistance_amulet_ii", null),
				MapPair.of("hmag:insomnia_fruit", null),
				MapPair.of("dwmg:healing_jade", null),
				MapPair.of("dwmg:life_jade", null),
				MapPair.of("dwmg:life_jade_ii", null),
				MapPair.of("dwmg:courage_amulet", null),
				MapPair.of("dwmg:courage_amulet_ii", null));
	}
	
	@Override
	public boolean shouldAlwaysRefresh(String slotKey, IBaubleHolder holder)
	{
		return holder.getBaubleSlots().get(slotKey).is(DwmgItems.HEALING_JADE.get())
				|| holder.getBaubleSlots().get(slotKey).is(DwmgItems.LIFE_JADE.get())
				|| holder.getBaubleSlots().get(slotKey).is(DwmgItems.LIFE_JADE_II.get());
	} 
	
	@Override
	public void refreshBaubleEffect(String slotKey, ItemStack bauble, IBaubleHolder owner) {
		if (bauble.is(DwmgItems.RESISTANCE_AMULET.get()))
		{
			owner.addBaubleModifier(slotKey, "ra_armor", Attributes.ARMOR, 4.0d, Operation.ADDITION);
			owner.addBaubleModifier(slotKey, "ra_speed_slow",Attributes.MOVEMENT_SPEED, -0.1d, Operation.MULTIPLY_BASE);
		}
		else if (bauble.is(DwmgItems.RESISTANCE_AMULET_II.get()))
		{
			owner.addBaubleModifier(slotKey, "ra2_armor", Attributes.ARMOR, 6.0d, Operation.ADDITION);
			owner.addBaubleModifier(slotKey, "ra2_speed_slow",Attributes.MOVEMENT_SPEED, -0.1d, Operation.MULTIPLY_BASE);
		}
		else if (bauble.is(DwmgItems.HEALING_JADE.get()))
		{
			owner.getLiving().heal(0.005f);// 0.1 per second
		}
		else if (bauble.is(DwmgItems.LIFE_JADE.get()))
		{
			owner.addBaubleModifier(slotKey, "lj_hpmax", Attributes.MAX_HEALTH, 5.0d, Operation.ADDITION);	
			owner.getLiving().heal(0.0075f);// 0.15 per second
		}
		else if (bauble.is(DwmgItems.LIFE_JADE_II.get()))
		{
			owner.addBaubleModifier(slotKey, "lj2_hpmax", Attributes.MAX_HEALTH, 10.0d, Operation.ADDITION);
			owner.getLiving().heal(0.01f);// 0.2 per second
		}
		else if (bauble.is(DwmgItems.COURAGE_AMULET.get()))
		{
			owner.addBaubleModifier(slotKey, "ca_atk", Attributes.ATTACK_DAMAGE, 4.0d, Operation.ADDITION);
			owner.addBaubleModifier(slotKey, "ca_speed_up", Attributes.MOVEMENT_SPEED, 0.2d, Operation.MULTIPLY_BASE);
		}
		else if (bauble.is(DwmgItems.COURAGE_AMULET_II.get()))
		{
			owner.addBaubleModifier(slotKey, "ca2_atk", Attributes.ATTACK_DAMAGE, 6.0d, Operation.ADDITION);
			owner.addBaubleModifier(slotKey, "ca2_speed_up", Attributes.MOVEMENT_SPEED, 0.3d, Operation.MULTIPLY_BASE);
		}
	}
	
	
	@Override
	public void postTick(IBaubleHolder owner)
	{
		super.postTick(owner);
		
		owner.removeBaubleModifiers("if");
		if (owner.hasBaubleItem(ModItems.INSOMNIA_FRUIT.get()) && owner.getLiving().level().isNight())
		{
			owner.addBaubleModifier("if", "if_health", Attributes.MAX_HEALTH, 60d, Operation.ADDITION);
			owner.addBaubleModifier("if", "if_atk", Attributes.ATTACK_DAMAGE, 8d, Operation.ADDITION);		
		}
	}
	
}
