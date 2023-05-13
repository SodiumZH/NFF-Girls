package net.sodiumstudio.dwmg.events;

import java.util.UUID;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumstudio.befriendmobs.item.ItemMobRespawner;
import net.sodiumstudio.befriendmobs.item.capability.CItemStackMonitor;
import net.sodiumstudio.befriendmobs.item.capability.MobRespawnerStartRespawnEvent;
import net.sodiumstudio.befriendmobs.item.capability.RespawnerAddedEvent;
import net.sodiumstudio.befriendmobs.registry.BefMobCapabilities;
import net.sodiumstudio.befriendmobs.util.NbtHelper;
import net.sodiumstudio.befriendmobs.util.Wrapped;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.registries.DwmgItems;

@Mod.EventBusSubscriber(modid = Dwmg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DwmgItemEvents
{
	@SubscribeEvent
	public static void onRespawnerAdded(RespawnerAddedEvent event)
	{
		CompoundTag mobNbt = event.getRespawner().getMobNbt();
		if (mobNbt.contains("CustomName", NbtHelper.TagType.TAG_STRING.getID())
				&& mobNbt.contains("dwmg:befriended_owner"))
		{
			String name = Component.Serializer.fromJson(mobNbt.getString("CustomName")).getString();
			MutableComponent nameComp = new TextComponent(name);
			nameComp.setStyle(nameComp.getStyle().withItalic(true));
			MutableComponent comp = 
					new TranslatableComponent("item.befriendmobs.mob_respawner")
					.append(" - ");
			comp.setStyle(comp.getStyle().withItalic(false));
			comp.append(nameComp);
			event.getRespawner().getItemStack().setHoverName(comp);
		}
		else 
		{
			MutableComponent comp = 
					new TranslatableComponent("item.befriendmobs.mob_respawner")
					.append(" - ")
					.append(event.getRespawner().getType().getDescription());
			comp.setStyle(comp.getStyle().withItalic(false));
			event.getRespawner().getItemStack().setHoverName(comp);
		}
	}
	
	@SubscribeEvent
	public static void onItemEntityPickUp(EntityItemPickupEvent event)
	{
		if (event.getItem().getItem().getItem() instanceof ItemMobRespawner)
		{
			Wrapped<Boolean> isOwner = new Wrapped<Boolean>(true);
			event.getItem().getItem().getCapability(BefMobCapabilities.CAP_MOB_RESPAWNER).ifPresent((c) -> 
			{
				// If the mob isn't a dwmg befriended mob, it should not have this uuid
				if (c.getMobNbt().hasUUID("dwmg:befriended_owner"))
					isOwner.set(c.getMobNbt().getUUID("dwmg:befriended_owner").equals(event.getPlayer().getUUID()));
			});
			if (!isOwner.get())
				event.setCanceled(true);
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
	
	protected static final UUID SHARPNESS_MODIFIER_UUID = UUID.fromString("9c12b503-63c0-43e6-bd30-d7aae9818c99");
	
	/**
	 * Add sharpness atk modifier to befriended mobs
	 */
	@SubscribeEvent
	public static void onBefriendedMainHandItemChange(CItemStackMonitor.ChangeEvent event)
	{
		event.living.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(SHARPNESS_MODIFIER_UUID);
		int lv = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SHARPNESS, event.to);
		if (lv > 0)
		{
			event.living.getAttribute(Attributes.ATTACK_DAMAGE).addTransientModifier(new AttributeModifier(
					SHARPNESS_MODIFIER_UUID, "sharpness_modifier", 0.5d + 0.5d * (double) lv, AttributeModifier.Operation.ADDITION));
		}		
	}
	
	@SubscribeEvent
	public static void onAnvilChange(AnvilUpdateEvent event)
	{
		// Necromancer's Wand fixing
		if (event.getLeft().is(DwmgItems.NECROMANCER_WAND.get()) 
				&& event.getRight().is(DwmgItems.DEATH_CRYSTAL_POWDER.get())
				&& event.getLeft().getDamageValue() > 0)
		{
			ItemStack out = event.getLeft().copy();
			event.setCost(1);
			event.setMaterialCost(1);
			out.setDamageValue(event.getLeft().getDamageValue() - 16);
			event.setOutput(out);
		}
	}
	
	
}
