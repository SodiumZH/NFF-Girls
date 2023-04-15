package net.sodiumstudio.dwmg.dwmgcontent.events;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumstudio.dwmg.befriendmobs.BefriendMobs;
import net.sodiumstudio.dwmg.befriendmobs.item.ItemMobRespawner;
import net.sodiumstudio.dwmg.befriendmobs.item.capability.MobRespawnerStartRespawnEvent;
import net.sodiumstudio.dwmg.befriendmobs.item.capability.RespawnerAddedEvent;
import net.sodiumstudio.dwmg.befriendmobs.registry.BefMobCapabilities;
import net.sodiumstudio.dwmg.befriendmobs.util.InfoHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.NbtHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.Wrapped;

@Mod.EventBusSubscriber(modid = BefriendMobs.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ItemEvents
{
	@SubscribeEvent
	public static void onRespawnerAdded(RespawnerAddedEvent event)
	{
		CompoundTag mobNbt = event.getRespawner().getMobNbt();
		if (mobNbt.contains("CustomName", NbtHelper.TagType.TAG_STRING.getID())
				&& mobNbt.contains("dwmg:befriended_owner"))
		{
			String name = Component.Serializer.fromJson(mobNbt.getString("CustomName")).getString();
			MutableComponent nameComp = InfoHelper.createText(name);
			nameComp.setStyle(nameComp.getStyle().withItalic(true));
			MutableComponent comp = 
					InfoHelper.createTrans("item.befriendmobs.mob_respawner")
					.append(" - ");
			comp.setStyle(comp.getStyle().withItalic(false));
			comp.append(nameComp);
			event.getRespawner().getItemStack().setHoverName(comp);
		}
		else 
		{
			MutableComponent comp = 
					InfoHelper.createTrans("item.befriendmobs.mob_respawner")
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
					isOwner.set(c.getMobNbt().getUUID("dwmg:befriended_owner").equals(event.getEntity().getUUID()));
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
}
