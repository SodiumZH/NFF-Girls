package net.sodiumstudio.dwmg.entities.capabilities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.common.util.INBTSerializable;
import net.sodiumstudio.nautils.EntityHelper;
import net.sodiumstudio.nautils.annotation.DontCallManually;
import net.sodiumstudio.nautils.containers.SerializableMap;
import net.sodiumstudio.nautils.exceptions.UnimplementedException;

/**
 * Mobs that are default hostile but being neutral under some conditions.
 */
public interface CConditionedNeutralMob extends INBTSerializable<CompoundTag>
{
	
	/** Get the attached nbt */
	public CompoundTag getNbt();
	
	/** Get this mob */
	public Mob getMob();

	/** Returns whether this mob is neutral to the living entity */
	public boolean isNeutralTo(LivingEntity target);
	
	/** Returns whether neutral list is enabled */
	public boolean isNeutralListEnabled();
	
	/** Sets whether neutral list should be enabled */
	public void setNeutralListEnabled(boolean enabled);
	
	/** Add a living entity to neutral list.
	 * @param duration Ticks before this mob returns hostile, or -1 for permanent neutral.
	 */
	public void addNeutral(LivingEntity target, int duration);
	
	/** Add a living entity to neutral list with 5 min duration */
	public default void addNeutral(LivingEntity target) {addNeutral(target, 300 * 20);}
	
	/** Add a living entity to permanent neutral */
	public default void addPermanentNeutral(LivingEntity target) {addNeutral(target, -1);}
	
	/** Remove a living entity from neutral list */
	public void removeNeutral(LivingEntity target);
	
	/** 
	 * Test with checking function for whether a living should be neutral.
	 * If the living is in neutral list and the neutral list is enabled, it will be always neutral ignoring this check.
	 * The checking function is initialized in implementation constructor.
	 */
	public boolean testNeutralPredicate(LivingEntity target);

	/** Run every tick */
	@DontCallManually
	public void tick();

	public static class Impl implements CConditionedNeutralMob
	{

		protected Mob mob;
		protected CompoundTag tag;
		protected Timers neutral;
		Predicate<LivingEntity> predicate;
		
		public Impl(Mob mob, Predicate<LivingEntity> neutralPredicate)
		{
			throw UnimplementedException.forClass();
			/*
			this.mob = mob;
			tag = new CompoundTag();
			neutral = new Timers(mob);
			predicate = neutralPredicate;
			tag.put("neutral", neutral.serializeNBT());
			tag.put("enable_neutral_list", ByteTag.valueOf(true));*/
		}
		
		public Impl(Mob mob)
		{
			this(mob, l -> false);
		}
		
		@Override
		public CompoundTag serializeNBT() {
			tag.put("neutral", neutral.serializeNBT());
			return tag;
		}

		@Override
		public void deserializeNBT(CompoundTag nbt) {
			tag = nbt;
			neutral.deserializeNBT(nbt.getCompound("neutral"));
		}

		@Override
		public CompoundTag getNbt() {
			return tag;
		}
		
		@Override
		public Mob getMob() {
			return mob;
		}

		@Override
		public boolean isNeutralTo(LivingEntity target) {
			return (isNeutralListEnabled() && neutral.keySet().contains(target)) || testNeutralPredicate(target);
		}

		@Override
		public boolean isNeutralListEnabled()
		{
			return tag.getBoolean("enable_neutral_list");
		}
		
		@Override
		public void setNeutralListEnabled(boolean enabled)
		{
			tag.putBoolean("enable_neutral_list", enabled);
		}
		
		@Override
		public void addNeutral(LivingEntity target, int duration) {
			neutral.put(target, duration);
		}

		@Override
		public void removeNeutral(LivingEntity target) {
			neutral.remove(target);
		}

		@Override
		public void tick() {
			List<LivingEntity> toRemove = new ArrayList<LivingEntity>();
			for (LivingEntity le: neutral.keySet())
			{
				if (neutral.get(le) > 0)
				{
					neutral.put(le, neutral.get(le) - 1);
				}
				if (neutral.get(le) == 0)
				{
					toRemove.add(le);
				}
			}
			for (LivingEntity le: toRemove)
			{
				neutral.remove(le);
			}
		}

		@Override
		public boolean testNeutralPredicate(LivingEntity target) {
			return this.predicate.test(target);
		}
	}
	
	public static class Timers extends SerializableMap<LivingEntity, Integer>
	{		
		
		private static final long serialVersionUID = 3751544923780435573L;
		
		Entity levelContext;
		public Timers(Entity levelContext)
		{
			super(LivingEntity::getStringUUID, IntTag::valueOf,
					str -> {
						Entity e = EntityHelper.getEntityByUUID(levelContext.level(), UUID.fromString(str));
						return (e != null && e instanceof LivingEntity) ? (LivingEntity)e : null;
					},
					(Tag tag) -> ((IntTag)tag).getAsInt());
			this.levelContext = levelContext;		
		}
	}
	
	
}
