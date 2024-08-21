package net.sodiumstudio.befriendmobs.subsystems.baublesystem;

import java.util.function.Function;
import java.util.function.Predicate;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;
import net.sodiumstudio.nautils.containers.ArrayIterationHelper;

public class RegisterBaubleEquippableMobsEvent extends Event implements IModBusEvent
{

	/**
	 * Register a mob class as bauble-equippable. It returns a {@code SlotRegisterer} for further registering slots.
	 */
	public SlotRegisterer register(Class<? extends Mob> clazz)
	{
		if (!BaubleEquippableMobRegistries.containsMobType(clazz))
			BaubleEquippableMobRegistries.registerMob(clazz);
		return new SlotRegisterer(clazz);
	}

	public static record SlotRegisterer(Class<? extends Mob> clazz)
	{

		/**
		 * Add a slot for all mobs of this class.
		 */
		public SlotRegisterer addSlot(String key, Function<Mob, ItemStack> accessor)
		{
			BaubleEquippableMobRegistries.registerGeneralSlot(clazz, key, accessor);
			return this;
		}
		
		/**
		 * Add a slot only for mobs satisfying the condition in the class.
		 * @param condition Condition for the mob to have this slot.
		 */
		public SlotRegisterer addConditionalSlot(String key, Predicate<Mob> condition, Function<Mob, ItemStack> accessor)
		{
			BaubleEquippableMobRegistries.registerConditionalSlot(clazz, key, condition, accessor);
			return this;
		}
		
		/**
		 * Add a special slot for all mobs of this class. A special slot is a slot that doesn't accept any baubles unless manually registered.
		 */
		public SpecialSlotRegisterer addSpecialSlot(String key, Function<Mob, ItemStack> accessor)
		{
			BaubleEquippableMobRegistries.registerGeneralSpecialSlot(clazz, key, accessor);
			return new SpecialSlotRegisterer(clazz, key);
		}
		
		/**
		 * Add a special slot only for mobs satisfying the condition in the class. A special slot is a slot that doesn't accept any baubles unless manually registered.
		 * @param condition Condition for the mob to have this slot.
		 */
		public SpecialSlotRegisterer addConditionalSpecialSlot(String key, Predicate<Mob> condition, Function<Mob, ItemStack> accessor)
		{
			BaubleEquippableMobRegistries.registerConditionalSpecialSlot(clazz, key, condition, accessor);
			return new SpecialSlotRegisterer(clazz, key);
		}		
	}
	
	public static record SpecialSlotRegisterer(Class<? extends Mob> clazz, String slotKey)
	{
		/**
		 * Register accepted Bauble Entries for a special slot. Entry keys uses ResourceLocation format.
		 * @return Go back to slot registerer for further registering slots for the mob class.
		 */
		public SlotRegisterer addSpecialSlotItem(String... acceptedEntryKeys)
		{
			for (String entryKey: ArrayIterationHelper.of(acceptedEntryKeys))
				BaubleEquippableMobRegistries.getMobSlotPropertyMap(clazz).get(slotKey).d.add(BaubleRegistries.getEntryByKey(entryKey));
			return new SlotRegisterer(clazz);
		}
		
		/**
		 * Register accepted Bauble Entries for a special slot.
		 * @return Go back to slot registerer for further registering slots for the mob class.
		 */
		public SlotRegisterer addSpecialSlotItem(ResourceLocation... acceptedEntryKeys)
		{
			for (ResourceLocation entryKey: ArrayIterationHelper.of(acceptedEntryKeys))
				BaubleEquippableMobRegistries.getMobSlotPropertyMap(clazz).get(slotKey).d.add(BaubleRegistries.getEntryByKey(entryKey));
			return new SlotRegisterer(clazz);
		}
	}
}
