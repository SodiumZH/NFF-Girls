package net.sodiumstudio.befriendmobs.subsystems.baublesystem;

import java.util.function.Predicate;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.sodiumstudio.nautils.function.NaFunctionUtils;

/**
 * A checker for if a bauble item can be equipped in a given bauble slot of a given living entity.
 */
public class BaubleEquippingCondition
{
	
	/**
	 * Predicate for directly checking the condition.
	 */
	private Predicate<BaubleProcessingArgs> conditionPredicate;
	
	private BaubleEquippingCondition(Predicate<BaubleProcessingArgs> condition)
	{
		this.conditionPredicate = condition;
	}
	
	/**
	 * Create a condition with an existing predicate. 
	 */
	public static BaubleEquippingCondition of(Predicate<BaubleProcessingArgs> predicate)
	{
		return new BaubleEquippingCondition(predicate);
	}
	
	/**
	 * Create a new condition that is always true.
	 */
	public static BaubleEquippingCondition always()
	{
		return of(args -> true);
	}
	
	/**
	 * Create a condition only for a given mob type.
	 */
	public static BaubleEquippingCondition forMob(EntityType<? extends Mob> type)
	{
		return always().onlyForType(type);
	}
	
	/**
	 * Create a condition only for a given mob type.
	 */
	@SafeVarargs
	public static BaubleEquippingCondition forMobs(EntityType<? extends Mob>... types)
	{
		return always().onlyForTypes(types);
	}
	
	/**
	 * Create a condition only for subclass of a given class/interface.
	 */
	public static BaubleEquippingCondition forSubclassOf(Class<?> clazz)
	{
		return always().onlyForSubclassOf(clazz);
	}
	
	/**
	 * Create a depending copy of given condition without direct access of the internal predicate.
	 * <p>Note: If this condition is modified, the created dependent will also change;
	 * but modifying the dependent will not affect this condition.
	 */
	public BaubleEquippingCondition createDependent()
	{
		return new BaubleEquippingCondition(args -> this.test(args));
	}
	
	/**
	 * Create a depending copy of given condition without direct access of the internal predicate.
	 * <p>Note: If the source condition is modified, the created dependent will also change;
	 * but modifying the dependent will not affect the source condition.
	 */
	public static BaubleEquippingCondition createDependent(BaubleEquippingCondition from)
	{
		return new BaubleEquippingCondition(args -> from.test(args));
	}
	
	/**
	 * Add an AND condition.
	 * <p> Note: For OR condition, please create a new {@code IBaubleRegistryEntry} instead. 
	 * To create an entry with same effect, use {@link ClonedBaubleBehavior}.
	 */
	public BaubleEquippingCondition and(Predicate<BaubleProcessingArgs> other)
	{
		var old = conditionPredicate;
		conditionPredicate = (args) -> (old.test(args) && other.test(args));
		return this;
	}
	
	/**
	 * Exclude a specific entity type.
	 */
	public BaubleEquippingCondition excludeLivingType(EntityType<? extends Mob> toExclude)
	{
		return this.and((args) -> (args.user().getType() != toExclude));
	}

	/**
	 * Exclude multiple entity types.
	 */
	public BaubleEquippingCondition excludeLivingTypes(EntityType<? extends Mob>... toExclude)
	{
		return this.and(args -> NaFunctionUtils.and((EntityType<? extends Mob> type) -> args.user().getType() != type, toExclude));
	}
	
	/**
	 * Exclude specific class, not including subclasses.
	 */
	public BaubleEquippingCondition excludeClass(Class<?> toExclude)
	{
		return this.and((args) -> args.user().getClass() != toExclude);
	}
	
	/**
	 * Exclude a class and its subclasses. Accepting interfaces.
	 */
	public BaubleEquippingCondition excludeSubclassesOf(Class<?> toExclude)
	{
		return this.and((args) -> !toExclude.getClass().isAssignableFrom(args.user().getClass()));
	}
	
	/**
	 * Add a condition only checked for a specific type but skipped for other types.
	 */
	public BaubleEquippingCondition addConditonForType(EntityType<? extends Mob> type, Predicate<BaubleProcessingArgs> condition)
	{
		return this.and((args) -> (args.user().getType() != type || condition.test(args)));
	}
	
	/**
	 * Exclude a bauble slot key for a specific type.
	 */
	public BaubleEquippingCondition excludeSlotForType(EntityType<? extends Mob> type, String toExclude)
	{
		return this.and((args) -> (args.user().getType() != type || args.slotKey() != toExclude));
	}
	
	/**
	 * Make this condition only allow given type.
	 */
	public BaubleEquippingCondition onlyForType(EntityType<? extends Mob> type)
	{
		return this.and((args) -> args.user().getType() == type);
	}
	
	/**
	 * Make this condition only allow given types.
	 */
	public BaubleEquippingCondition onlyForTypes(EntityType<? extends Mob>... types)
	{
		return this.and((args) -> NaFunctionUtils.or((EntityType<? extends LivingEntity> entitytype) -> args.user().getType() == entitytype, types));
	}
	
	/**
	 * Make this condition only allow subclasses of given class.
	 */
	public BaubleEquippingCondition onlyForSubclassOf(Class<?> clazz)
	{
		return this.and(args -> clazz.isAssignableFrom(args.user().getClass()));
	}
	
	/**
	 * Make this condition only allow a given slot of a given type.
	 */
	public BaubleEquippingCondition onlyForSlotOfType(EntityType<? extends Mob> type, String key)
	{
		return this.and((args) -> args.user().getType() == type && args.slotKey() == key);
	}
	
	/**
	 * Finally check if an equipping attempt is allowed.
	 */
	public boolean test(BaubleProcessingArgs args)
	{
		return this.conditionPredicate.test(args);
	}

}
