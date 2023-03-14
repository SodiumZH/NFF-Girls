package net.sodiumstudio.dwmg.befriendmobs.util.debug;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.sodiumstudio.dwmg.befriendmobs.BefriendMobs;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending.registry.BefriendingTypeRegistry;
import net.sodiumstudio.dwmg.befriendmobs.registry.RegCapabilities;
import net.sodiumstudio.dwmg.befriendmobs.registry.RegItems;
import net.sodiumstudio.dwmg.befriendmobs.util.EntityHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.exceptions.UnimplementedException;

public class BMDebugItemHandler
{

	@SuppressWarnings("unchecked")
	public static void onDebugItemUsed(Player player, Mob target, Item item) {
		
		if (!BefriendMobs.IS_DEBUG_MODE)
			throw new RuntimeException("Debug item cannot use in publish versions!!!");
		
		if (item.equals(RegItems.DEBUG_TARGET_SETTER.get()))
		{
			MobEffect effect = target.getMobType().equals(MobType.UNDEAD) ? MobEffects.HARM : MobEffects.HEAL;
			if (target instanceof IBefriendedMob)
			{
				if (target.hasEffect(effect))
				{
					target.removeEffect(effect);
				} 
				else
				{
					target.addEffect(new MobEffectInstance(effect, 9999999));
				}
			}
			else
			{
				if (target.hasEffect(target.getMobType().equals(MobType.UNDEAD) ? MobEffects.HARM : MobEffects.HEAL))
				{
					target.kill();
				} 
				else
				{
					target.addEffect(new MobEffectInstance(effect, 9999999));
					target.setCustomName(new TextComponent("Debug Target"));
				}
			}
		}

		else if (item.equals(RegItems.DEBUG_BEFRIENDER.get()))
		{
			if (target instanceof IBefriendedMob bef)
				bef.init(player.getUUID(), null);
			else 
			{
				target.getCapability(RegCapabilities.CAP_BEFRIENDABLE_MOB).ifPresent((l) ->
				{
					IBefriendedMob bef = BefriendingTypeRegistry.getHandler((EntityType<Mob>)target.getType()).befriend(player, target);
					if (bef != null)
					{
						EntityHelper.sendHeartParticlesToMob(bef.asMob()); // TODO: move this to a MobBefriendEvent listener
					} else
						throw new UnimplementedException(
								"Entity type befriend method unimplemented: " + target.getType().toShortString()
								+ ", handler class: " + BefriendingTypeRegistry.getHandler((EntityType<Mob>)target.getType()).toString());
	
				});
			}
		}

		else if (item.equals(RegItems.DEBUG_ARMOR_GIVER.get()) && target.getCapability(RegCapabilities.CAP_BEFRIENDABLE_MOB).isPresent())
		{
			if (target.getItemBySlot(EquipmentSlot.HEAD).isEmpty())
				target.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET.asItem()));
			else if (target.getItemBySlot(EquipmentSlot.CHEST).isEmpty())
				target.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE.asItem()));
			else if (target.getItemBySlot(EquipmentSlot.LEGS).isEmpty())
				target.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS.asItem()));
			else if (target.getItemBySlot(EquipmentSlot.FEET).isEmpty())
				target.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS.asItem()));
		}
	}
}
