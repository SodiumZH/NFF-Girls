package net.sodiumstudio.befriendmobs.entity.befriended;

import net.minecraft.world.entity.Mob;
import net.sodiumstudio.nautils.annotation.DontCallManually;
import net.sodiumstudio.nautils.annotation.DontOverride;
import net.sodiumstudio.nautils.function.MutablePredicate;
import net.sodiumstudio.nautils.mixins.mixins.NaUtilsMixinMob;
import net.sodiumstudio.befriendmobs.events.BMEntityEvents;
/**
 * This is an interface handling sun immunity for sun-sensitive mobs.
 * Put and remove entries in {@code sunImmuneConditions()} and {@sunImmuneNecessaryConditions()} to set rules.
 * Note: applying sun-immunity on mob must be manually implemented on each mob. 
 */
public interface IBefriendedSunSensitiveMob
{
	
	/**
	 * @deprecated Use {@code getBefriended} instead
	 */
	@DontOverride
	@Deprecated
	public default IBefriendedMob asBefriended()
	{
		return getBefriended();
	}
	
	@DontOverride
	public default IBefriendedMob getBefriended()
	{
		if (this instanceof IBefriendedMob bm)
			return bm;
		else throw new UnsupportedOperationException("IBefriendedSunSensitiveMob: mob missing IBefriendedMob interface.");
	}
	
	@DontOverride
	public default Mob getMob()
	{
		if (this instanceof Mob m)
			return m;
		else throw new UnsupportedOperationException("IBefriendedSunSensitiveMob: wrong object type, must be a mob.");
	}

	/**
	 * Check if the mob is immune to sun from rules.
	 * Implemented in {@link BMEntityEvents#onMobSunBurnTick} via {@link NaUtilsMixinMob#isSunBurnTick}
	 */
	@DontOverride
	public default boolean isSunImmune()
	{
		return getSunImmunity().test(this);
	}
	
	/**
	 * Setup rules for sun immunity. Use {@code getSunImmunity()} to access rules.
	 * Called in EntityJoinWorldEvent only
	 */
	@DontCallManually
	public void setupSunImmunityRules();
	
	@DontOverride
	public default MutablePredicate<IBefriendedSunSensitiveMob> getSunImmunity()
	{
		return this.getBefriended().getData().getSunImmunity();
	}
	
}
