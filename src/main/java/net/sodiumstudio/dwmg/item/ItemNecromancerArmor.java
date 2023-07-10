package net.sodiumstudio.dwmg.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.sodiumstudio.nautils.EntityHelper;
import net.sodiumstudio.dwmg.blocks.BlockSoulCarpet;
import net.sodiumstudio.dwmg.entities.hmag.EntityBefriendedNecroticReaper;
import net.sodiumstudio.dwmg.registries.DwmgEffects;
import net.sodiumstudio.dwmg.registries.DwmgItems;

public class ItemNecromancerArmor extends ArmorItem
{

	public ItemNecromancerArmor(ArmorMaterial pMaterial, EquipmentSlot pSlot, Properties pProperties)
	{
		super(pMaterial, pSlot, pProperties);
	}
	
	// Called only in LivingUpdateEvent listener
	public static void necromancerArmorUpdate(LivingEntity living)
	{				
		if (living instanceof ArmorStand)
			return;

		if (living.getItemBySlot(EquipmentSlot.HEAD).is(DwmgItems.NECROMANCER_HAT.get()))
		{
			EntityHelper.addEffectIfNotHaving(living, new MobEffectInstance(MobEffects.DAMAGE_BOOST, 40, 1, true, false));
			EntityHelper.addEffectIfNotHaving(living, new MobEffectInstance(DwmgEffects.UNDEAD_AFFINITY.get(), 40, 0, true, false));
			EntityHelper.addEffectIfNotHaving(living, new MobEffectInstance(MobEffects.DIG_SPEED, 40, 1, true, false));
			if (!BlockSoulCarpet.isEntityInside(living))
				EntityHelper.addEffectIfNotHaving(living, new MobEffectInstance(DwmgEffects.NECROMANCER_WITHER.get(), 40, 0, true, false));	
			if (living instanceof Player p)
			{
				// Nearby Necrotic Reapers add regeneration
				int regLvl = EntityBefriendedNecroticReaper.countNearby(p) - 1;
				// Max level 3
				if (regLvl > 2)
					regLvl = 2;
				if (regLvl >= 0)
				{
					EntityHelper.addEffectIfNotHaving(living, new MobEffectInstance(MobEffects.REGENERATION, 80, regLvl, true, false)); 
				}
			}
		}	
	}

}
