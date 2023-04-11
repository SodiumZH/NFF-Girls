package net.sodiumstudio.dwmg.dwmgcontent.events;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumstudio.dwmg.befriendmobs.BefriendMobs;
import net.sodiumstudio.dwmg.befriendmobs.item.capability.CMobRespawnerImpl;
import net.sodiumstudio.dwmg.befriendmobs.item.capability.RespawnerAddedEvent;
import net.sodiumstudio.dwmg.befriendmobs.util.NbtHelper;

@Mod.EventBusSubscriber(modid = BefriendMobs.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ItemEvents
{
	@SubscribeEvent
	public static void onRespawnerAdded(RespawnerAddedEvent event)
	{
		CompoundTag mobNbt = ((CMobRespawnerImpl)(event.getRespawner())).tag.getCompound("mob_nbt");
		if (mobNbt.contains("CustomName", NbtHelper.TagType.TAG_STRING.getID()))
		{
			String name = mobNbt.getString("CustomName");
			MutableComponent comp = 
					new TranslatableComponent("item.befriendmobs.mob_respawner")
					.append(" - ")
					.append(name);
			comp.setStyle(comp.getStyle().withItalic(false));
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
}
