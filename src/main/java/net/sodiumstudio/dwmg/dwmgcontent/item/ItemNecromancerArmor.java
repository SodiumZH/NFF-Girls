package net.sodiumstudio.dwmg.dwmgcontent.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.dwmg.befriendmobs.util.EntityHelper;
import net.sodiumstudio.dwmg.dwmgcontent.registries.DwmgEffects;
import net.sodiumstudio.dwmg.dwmgcontent.registries.DwmgItems;

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
			EntityHelper.addEffectIfNotHaving(living, new MobEffectInstance(MobEffects.DAMAGE_BOOST, 40, 1, false, false));
			EntityHelper.addEffectIfNotHaving(living, new MobEffectInstance(DwmgEffects.UNDEAD_AFFINITY.get(), 40, 0, false, false));
			EntityHelper.addEffectIfNotHaving(living, new MobEffectInstance(MobEffects.DIG_SPEED, 40, 1, false, false));
			EntityHelper.addEffectIfNotHaving(living, new MobEffectInstance(MobEffects.WITHER, 40, 0, false, false));			
		}	
	}

}
