package net.sodiumstudio.befriendmobs.subsystems.baublesystem;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

/**
 * Used when {@code IBaubleEquippableMob} trying to access the capability when it's not present,
 * e.g. the mob is pending removal and the capabilities have been detached.
 * <p>It doesn't do anything but only prevents errors caused by accident capability access after removal.
 */
class CBaubleEquippableMobEmptyImpl implements CBaubleEquippableMob
{
	private Mob mob;
	public CBaubleEquippableMobEmptyImpl(Mob mob)
	{
		this.mob = mob;
	}
	
	@Override
	public Mob getMob() {
		return mob;
	}

	@Override
	public BaubleSlotAccessor getBaubleSlotAccessor() {
		return BaubleSlotAccessor.empty(this);
	}

	@Override
	public Set<BaubleAttributeModifier> getModifiers() {
		return new HashSet<>();
	}

	@Override
	public void modifierTick() {
	}

	@Override
	public void onSlotChange() {
	}

	@Override
	public void preTick() {
	}

	@Override
	public void slotTick(BaubleProcessingArgs args) {
	}

	@Override
	public void postTick() {
	}

	@Override
	public void tick() {
	}

}
