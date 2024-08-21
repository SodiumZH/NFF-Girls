package net.sodiumstudio.befriendmobs.item.event;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.entity.befriending.registry.BefriendingTypeRegistry;
import net.sodiumstudio.befriendmobs.registry.BMCaps;
import net.sodiumstudio.befriendmobs.registry.BMItems;
import net.sodiumstudio.nautils.EntityHelper;
import net.sodiumstudio.nautils.InfoHelper;
import net.sodiumstudio.nautils.NaMiscUtils;
import net.sodiumstudio.nautils.debug.Debug;
import net.sodiumstudio.nautils.exceptions.UnimplementedException;

@Deprecated
public class BMDebugItemHandler
{
/*
	@SuppressWarnings("unchecked")
	public static void onDebugItemUsed(Player player, Mob target, Item item) {

		if (item.equals(BMItems.DEBUG_TARGET_SETTER.get()))
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
					target.setCustomName(InfoHelper.createText("Debug Target"));
				}
			}
		}

		else if (item.equals(BMItems.DEBUG_BEFRIENDER.get()) && player.isCreative())
		{
			if (target instanceof IBefriendedMob bef)
			{
				bef.init(player.getUUID(), null);
				Debug.printToScreen("Mob " + target.getName().getString() + " initialized", player);
			}
			else 
			{
				target.getCapability(BMCaps.CAP_BEFRIENDABLE_MOB).ifPresent((l) ->
				{
					IBefriendedMob bef = BefriendingTypeRegistry.getHandler((EntityType<Mob>)target.getType()).befriend(player, target);
					if (bef != null)
					{
						EntityHelper.sendHeartParticlesToLivingDefault(bef.asMob());
					} else
						throw new UnimplementedException(
								"Entity type befriend method unimplemented: " + target.getType().toShortString()
								+ ", handler class: " + BefriendingTypeRegistry.getHandler((EntityType<Mob>)target.getType()).toString());
	
				});
			}
		}
		
		else if (item == BMItems.DEBUG_AI_SWITCH.get())
		{
			target.setNoAi(!target.isNoAi());
			String key = target.isNoAi() ? "info.befriendmobs.debug_ai_switch_off" : "info.befriendmobs.debug_ai_switch_on";		
			MutableComponent info = Component.translatable(key, target.getName().getString());
			NaMiscUtils.printToScreen(info, player);
		}

		else if (item.equals(BMItems.DEBUG_ARMOR_GIVER.get()) && target.getCapability(BMCaps.CAP_BEFRIENDABLE_MOB).isPresent())
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
		
		else if (item.equals(BMItems.DEBUG_MOB_CONVERTER.get()))
		{
			if (target instanceof EntityBefriendedHuskGirl e)
			{
				e.forceUnderWaterConversion();
			}
			else if (target instanceof EntityBefriendedZombieGirl e)
			{
				e.forceUnderWaterConversion();
			}
		}
		
		else if (item.equals(BMItems.DEBUG_ATTRIBUTE_CHECKER.get()))
		{
			Debug.printToScreen("Base: " + Double.toString(target.getAttributeBaseValue(Attributes.ATTACK_DAMAGE)), player);
			Debug.printToScreen("Applied: " + Double.toString(target.getAttributeValue(Attributes.ATTACK_DAMAGE)), player);
		}
		else MinecraftForge.EVENT_BUS.post(new UseEvent(player, target, item));
	}
	
	public static class UseEvent extends Event
	{
		public final Player player;
		public final Mob target;
		public final Item debugItem;
		
		public UseEvent(Player player, Mob mob, Item item)
		{
			this.player = player;
			this.target = mob;
			this.debugItem = item;
		}
	}*/
}
