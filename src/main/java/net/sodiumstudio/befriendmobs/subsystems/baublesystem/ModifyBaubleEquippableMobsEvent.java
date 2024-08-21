package net.sodiumstudio.befriendmobs.subsystems.baublesystem;

import com.mojang.logging.LogUtils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;

public class ModifyBaubleEquippableMobsEvent extends Event implements IModBusEvent
{

	/**
	 * Add a bauble entry for a special slot to accept.
	 */
	public void addSpecialSlotAccepted(Class<? extends Mob> clazz, String slot, ResourceLocation entryKey)
	{
		if (BaubleEquippableMobRegistries.isSlotSpecial(clazz, slot))
		{
			BaubleEquippableMobRegistries.getMobSlotPropertyMap(clazz).get(slot).d.add(BaubleRegistries.getEntryByKey(entryKey));
		}
		else LogUtils.getLogger().error("ModifyBaubleEquippableMobsEvent#addSpecialSlotAccepted: slot \"" + slot + "\" for mob class \""
				+ clazz.getSimpleName() + "\" isn't a special slot or doesn't exist. Skipped.");
	}
	
	/**
	 * Add a bauble entry for a special slot to accept. Entry key uses ResourceLocation format.
	 */
	public void addSpecialSlotAccepted(Class<? extends Mob> clazz, String slot, String entryKey)
	{
		this.addSpecialSlotAccepted(clazz, slot, new ResourceLocation(entryKey));
	}
	
}
