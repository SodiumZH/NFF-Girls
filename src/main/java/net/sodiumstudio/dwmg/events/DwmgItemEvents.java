package net.sodiumstudio.dwmg.events;

import java.util.UUID;

import com.github.mechalopa.hmag.registry.ModItems;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumstudio.befriendmobs.events.ServerEntityTickEvent;
import net.sodiumstudio.befriendmobs.item.MobRespawnerInstance;
import net.sodiumstudio.befriendmobs.item.MobRespawnerItem;
import net.sodiumstudio.befriendmobs.item.event.MobRespawnerStartRespawnEvent;
import net.sodiumstudio.befriendmobs.item.event.RespawnerConstructEvent;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.entities.IDwmgBefriendedMob;
import net.sodiumstudio.dwmg.item.IWithDuration;
import net.sodiumstudio.dwmg.registries.DwmgItems;
import net.sodiumstudio.nautils.NbtHelper;
import net.sodiumstudio.nautils.events.entity.ItemEntityHurtEvent;


@Mod.EventBusSubscriber(modid = Dwmg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DwmgItemEvents
{
	@SubscribeEvent
	public static void beforeRespawnerConstruct(RespawnerConstructEvent.Before event)
	{
	}
	
	@SubscribeEvent
	public static void afterRespawnerConstruct(RespawnerConstructEvent.After event)
	{
		CompoundTag mobNbt = event.getRespawner().getMobNbt();
		
		// Only modify dwmg respawner types
		String itemKey = null;
		if (event.getRespawner().get().is(DwmgItems.MOB_RESPAWNER.get()))
			itemKey = "item.dwmg.mob_respawner";
		else if (event.getRespawner().get().is(DwmgItems.MOB_STORAGE_POD.get()))
			itemKey = "item.dwmg.mob_storage_pod";
		if (itemKey != null)
		{
			mobNbt.putShort("Fire", (short) 0);
		}
	}
	
	@SubscribeEvent
	public static void onItemEntityPickUp(EntityItemPickupEvent event)
	{
		if (event.getItem().getItem().getItem() instanceof MobRespawnerItem)
		{
			MobRespawnerInstance ins = MobRespawnerInstance.create(event.getItem().getItem());
				// If the mob isn't a dwmg befriended mob, it should not have this uuid
				if (ins.getMobNbt().hasUUID("dwmg:befriended_owner")
					 && !ins.getMobNbt().getUUID("dwmg:befriended_owner").equals(event.getEntity().getUUID()))
				{
					event.setCanceled(true);
				}
		}
		// Clear already picked mobs when player picking up
		if (event.getItem().getItem().getTag() != null && event.getItem().getItem().getTag().contains("already_picked_befriendable_mobs"))
		{
			event.getItem().getItem().removeTagKey("already_picked_befriendable_mobs");
		}
	}
	
	@SubscribeEvent
	public static void onMobRespawnerStartRespawn(MobRespawnerStartRespawnEvent event)
	{
		if (event.getRespawner().getMobNbt().hasUUID("dwmg:befriended_owner") 
				&& !event.getRespawner().getMobNbt().getUUID("dwmg:befriended_owner").equals(event.getPlayer().getUUID()))
		{
			event.setCanceled(true);
		}
	}

	/** @deprecated Use {@link IDwmgBefriendedMob#getSharpnessModifierUUID} instead */
	@Deprecated

	protected static final UUID SHARPNESS_MODIFIER_UUID = UUID.fromString("9c12b503-63c0-43e6-bd30-d7aae9818c99");
	
	/**
	 * Add sharpness atk modifier to befriended mobs
	 */
	/*@SubscribeEvent
	public static void onBefriendedMainHandItemChange(CItemStackMonitor.ChangeEvent event)
	{
		event.living.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(SHARPNESS_MODIFIER_UUID);
		@SuppressWarnings("deprecation")
		int lv = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SHARPNESS, event.to);
		if (lv > 0)
		{
			event.living.getAttribute(Attributes.ATTACK_DAMAGE).addTransientModifier(new AttributeModifier(
					SHARPNESS_MODIFIER_UUID, "sharpness_modifier", 0.5d + 0.5d * (double) lv, AttributeModifier.Operation.ADDITION));
		}
	}*/
	
	@SubscribeEvent
	public static void onAnvilChange(AnvilUpdateEvent event)
	{
		if (event.getLeft().getItem() instanceof IWithDuration wd)
		{
			// Necromancer's Wand
			if (event.getLeft().is(DwmgItems.NECROMANCER_WAND.get()) 
					&& event.getRight().is(DwmgItems.DEATH_CRYSTAL_POWDER.get())
					&& wd.canRepair(event.getLeft()))
			{
				ItemStack out = event.getLeft().copy();
				event.setCost(1);
				event.setMaterialCost(1);
				wd.repair(out, 32);
				event.setOutput(out);
			}
			// Evil Magnet fixing
			if (event.getLeft().is(DwmgItems.EVIL_MAGNET.get())
					&& wd.canRepair(event.getLeft())
					&& event.getRight().is(ModItems.EVIL_CRYSTAL.get()))
			{
				ItemStack out = event.getLeft().copy();
				event.setCost(1);
				event.setMaterialCost(1);
				wd.repair(out, 8);
				event.setOutput(out);
			}
		}
	}

	@SubscribeEvent
	public static void onServerItemEntityTick(ServerEntityTickEvent.PreWorldTick event)
	{
		if (event.getEntity() instanceof ItemEntity ie)
		{
			if (ie.getItem().getTag() != null 
					&& ie.getItem().getTag().contains("already_picked_befriendable_mobs", NbtHelper.TAG_COMPOUND_ID))
			{
				for (String key: ie.getItem().getTag().getCompound("already_picked_befriendable_mobs").getAllKeys())
				{
					if (ie.getItem().getTag().getCompound("already_picked_befriendable_mobs").getInt(key) > 0)
					{
						ie.getItem().getTag().getCompound("already_picked_befriendable_mobs").putInt(key, 
								ie.getItem().getTag().getCompound("already_picked_befriendable_mobs").getInt(key) - 1);
					}
					if (ie.getItem().getTag().getCompound("already_picked_befriendable_mobs").getInt(key) == 0)
					{
						ie.getItem().getTag().getCompound("already_picked_befriendable_mobs").remove(key);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onItemEntityHurt(ItemEntityHurtEvent event)
	{
		if (event.damageSource.getEntity() != null && event.damageSource.getEntity() instanceof IDwmgBefriendedMob)
			event.setCanceled(true);
	}
}
