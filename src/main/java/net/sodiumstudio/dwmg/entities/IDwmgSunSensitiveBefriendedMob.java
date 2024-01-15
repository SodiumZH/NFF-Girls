package net.sodiumstudio.dwmg.entities;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedSunSensitiveMob;
import net.sodiumstudio.dwmg.registries.DwmgItems;
import net.sodiumstudio.dwmg.subsystem.baublesystem.baubles.DwmgBaubleStatics;

public interface IDwmgSunSensitiveBefriendedMob extends IDwmgBefriendedMob, IBefriendedSunSensitiveMob
{
	@Override
	public default void setupSunImmunityRules()
	{
		this.getSunImmunity().putOptional("sunhat", mob -> mob.getMob().getItemBySlot(EquipmentSlot.HEAD).is(DwmgItems.SUNHAT.get()));
		this.getSunImmunity().putOptional("soul_amulet", mob -> DwmgBaubleStatics.countBaubles(asMob(), new ResourceLocation("dwmg:soul_amulet")) > 0);
		this.getSunImmunity().putOptional("resis_amulet", mob -> DwmgBaubleStatics.countBaubles(asMob(), new ResourceLocation("dwmg:resistance_amulet")) > 0);
	}
}
