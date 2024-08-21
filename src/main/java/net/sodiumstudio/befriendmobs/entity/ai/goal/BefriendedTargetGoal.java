package net.sodiumstudio.befriendmobs.entity.ai.goal;

import java.util.HashSet;
import java.util.Random;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraftforge.common.MinecraftForge;
import net.sodiumstudio.befriendmobs.bmevents.entity.ai.BefriendedGoalCheckCanUseEvent;
import net.sodiumstudio.befriendmobs.entity.ai.BefriendedAIState;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedOwnerHurtByTargetGoal;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;

public abstract class BefriendedTargetGoal extends TargetGoal implements IBefriendedGoal
{

	// for simplification
	protected static final BefriendedAIState WAIT = BefriendedAIState.WAIT;
	protected static final BefriendedAIState FOLLOW = BefriendedAIState.FOLLOW;
	protected static final BefriendedAIState WANDER = BefriendedAIState.WANDER;

	protected IBefriendedMob mob = null;
	protected HashSet<BefriendedAIState> allowedStates = new HashSet<BefriendedAIState>();
	protected boolean isBlocked = false;
	protected Random rnd = new Random();
	/**
	 *  If it's more than 0, when checking if this goal should be executed (only on start, not on check continue using),
	 *  it will has chance to be directly skipped, allowing goals below to be executed.
	 */
	protected double skipChance = 0;
	
	/**
	 * Chance to be interrupted each second running.
	 * This value is intended to prevent infinite running of a single goal.
	 */
	protected double interruptChance = 0;
	
	/**
	 * If true, this goal will require the mob's owner is in the same level to run.
	 */
	protected boolean requireOwnerPresent = true;
	/**
	 * Additional condition to start this goal. (Not checked on checking continue to use)
	 */
	protected Predicate<BefriendedTargetGoal> startCondition = null;
	
	/**
	 * If the {@code noExpireCondition} keeps false over this time (in ticks), the target goal will be interrupted.
	 * Set it negative to disable.
	 */
	protected int expireTicks = -1;
	private int expireTimer = 0;
	
	/**
	 * Condition to prevent the target goal from giving up.
	 */
	protected Supplier<Boolean> noExpireCondition = () -> true;
	
	
	public BefriendedTargetGoal(IBefriendedMob mob, boolean mustSee)
	{
		this(mob, mustSee, false);
	}

	public BefriendedTargetGoal(IBefriendedMob mob, boolean mustSee, boolean mustReach)
	{
		super(mob.asMob(), mustSee, mustReach);
		this.mob = mob;
	}

	public HashSet<BefriendedAIState> getAllowedStates()
	{
		return allowedStates;
	}
	
	@Override
	public boolean isStateAllowed() {
		return allowedStates.contains(mob.getAIState());
	}

	@Override
	public IBefriendedGoal allowState(BefriendedAIState state) {
		if (!allowedStates.contains(state))
			allowedStates.add(state);
		return this;
	}

	@Override
	public IBefriendedGoal excludeState(BefriendedAIState state) {
		if (allowedStates.contains(state))
			allowedStates.remove(state);
		return this;
	}

	@Override
	public IBefriendedGoal allowAllStates() {
		for (BefriendedAIState state : BefriendedAIState.getAllStates())
			allowedStates.add(state);
		return this;
	}

	@Override
	public IBefriendedGoal allowAllStatesExceptWait() {
		allowAllStates();
		excludeState(WAIT);
		return this;
	}

	@Override
	public void disallowAllStates() {
		allowedStates.clear();
	}

	@Override
	public IBefriendedGoal block() {
		isBlocked = true;
		return this;
	}

	@Override
	public IBefriendedGoal unblock() {
		isBlocked = false;
		return this;
	}

	@Override
	public boolean isDisabled() {
		return isBlocked || !isStateAllowed();
	}

	public LivingEntity getLiving() {
		return (LivingEntity) mob;
	}

	@Override
	public IBefriendedMob getMob() {
		return mob;
	}

	
	/**
	 * Get mob as PathfinderMob
	 * @return mob cast to PathfinderMob, or null if the mob isn't a PathfinderMob
	 */
	@Nullable
	public PathfinderMob getPathfinder()
	{
		return mob instanceof PathfinderMob ? (PathfinderMob)mob : null;
	}
	
	/**
	 * Fixed here because some common checks are needed here.
	 * In subclasses, override {@link IBefriendedGoal#checkCanUse} instead.
	 */
	@Override
	public final boolean canUse() 
	{
		// Detect if checkCanUse() calling canUse() which leads to infinite loop
		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
		if (stacktrace.length > 2 && stacktrace[2].getMethodName().equals("checkCanUse"))
			throw new RuntimeException("Illegal method call: checkCanUse() method cannot call canUse() method inside, otherwise an infinite loop will occur. To get super class' check, call checkCanUse().");
		if (mob == null || requireOwnerPresent && !mob.isOwnerPresent())
			return false;
		if (startCondition != null && !startCondition.test(this))
			return false;
		if (skipChance > 0 && rnd.nextDouble() < skipChance)
			return false;
		BefriendedGoalCheckCanUseEvent event = new BefriendedGoalCheckCanUseEvent(this, BefriendedGoalCheckCanUseEvent.Phase.CAN_USE);
		MinecraftForge.EVENT_BUS.post(event);
		if (event.getManualSetValue().isPresent())
			return event.getManualSetValue().get();
		if (isDisabled())
			return false;
		return checkCanUse();
	}
	
	/**
	 * Fixed here because some common checks are needed here.
	 * In subclasses, override {@link IBefriendedGoal#checkCanContinueToUse} instead.
	 */
	@Override
	public final boolean canContinueToUse()
	{
		// Detect if checkCanContinueToUse() calling canContinueToUse() which leads to infinite loop
		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
		if (stacktrace.length > 2 && stacktrace[2].getMethodName().equals("checkCanContinueToUse"))
			throw new RuntimeException("Illegal method call: checkCanContinueToUse() method cannot call canContinueToUse() method inside, otherwise an infinite loop will occur. To get super class' check, call checkCanContinueToUse().");
		if (mob == null || requireOwnerPresent && !mob.isOwnerPresent())
			return false;
		// Interruption
		// Most goals tick each 2 level-ticks so tickCount is always odd or always even
		int i = (this.requiresUpdateEveryTick() || this.mob.asMob().tickCount % 2 == 0) ? 0 : 1;
		if (this.mob.asMob().tickCount % 20 == i && this.rnd.nextDouble() < interruptChance)
			return false;
		BefriendedGoalCheckCanUseEvent event = new BefriendedGoalCheckCanUseEvent(this, BefriendedGoalCheckCanUseEvent.Phase.CAN_CONTINUE_TO_USE);
		MinecraftForge.EVENT_BUS.post(event);
		if (event.getManualSetValue().isPresent())
			return event.getManualSetValue().get();
		if (isDisabled())
			return false;
		if (this.expireTimer > this.expireTicks)
			return false;
		return checkCanContinueToUse() && super.canContinueToUse();
	}
	
	/**
	 * Fixed here because some common actions are needed here.
	 * In subclasses, override {@code onStart()}.
	 */
	@Override
	public final void start()
	{
		// Detect if onStart() calling super.start() which leads to infinite loop
		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
		if (stacktrace.length > 2 && stacktrace[2].getMethodName().equals("onStart"))
			throw new RuntimeException("Illegal method call: onStart() method cannot call start() method inside, otherwise an infinite loop will occur. To get super class' tick, call onStart().");
		this.expireTimer = 0;
		this.onStart();
	}
	
	/**
	 * Fixed here because some common actions are needed here.
	 * In subclasses, override {@code onTick()}.
	 */
	@Override
	public final void tick()
	{
		// Detect if onTick() calling super.tick() which leads to infinite loop
		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
		if (stacktrace.length > 2 && stacktrace[2].getMethodName().equals("onTick"))
			throw new RuntimeException("Illegal method call: onTick() method cannot call tick() method inside, otherwise an infinite loop will occur. To get super class' tick, call onTick().");
		if (this.expireTicks > 0)
		{
			if (this.noExpireCondition.get())
				this.expireTimer = 0;
			else this.expireTimer++;
		}
		this.onTick();
	}

	
	/**
	 * Fixed here because some common actions are needed here.
	 * In subclasses, override {@code onStop()}.
	 */
	@Override
	public final void stop()
	{
		// Detect if onStart() calling super.start() which leads to infinite loop
		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
		if (stacktrace.length > 2 && stacktrace[2].getMethodName().equals("onStop"))
			throw new RuntimeException("Illegal method call: onStop() method cannot call stop() method inside, otherwise an infinite loop will occur. To get super class' tick, call onStop().");
		if (this.expireTicks > 0)
		{
			if (this.noExpireCondition.get())
				this.expireTimer = 0;
			else this.expireTimer++;
		}
		this.expireTimer = 0;
		this.onStop();
	}
	
	@Override
	public boolean checkCanContinueToUse()
	{
		return true;
	}
	
	/**
	 * Get skip chance of this goal.
	 * <p> If it has skip chance > 0, it will have a chance to be directly skipped when checking if to start, allowing goals below to be executed.
	 * When it's skipped, it won't post {@link BefriendedGoalCheckCanUseEvent}.
	 */
	public double getSkipChance()
	{
		return skipChance;
	}
	
	/**
	 * Set skip chance of this goal. This method returns the goal itself, and use template class to specify the subclass to cast.
	 * <p> If it has skip chance > 0, it will have a chance to be directly skipped when checking if to start, allowing goals below to be executed.
	 * When it's skipped, it won't post {@link BefriendedGoalCheckCanUseEvent}.
	 */
	@SuppressWarnings("unchecked")
	public <T extends BefriendedTargetGoal> T setSkipChance(double value)
	{
		this.skipChance = value;
		return (T)this;
	}
	
	/**
	 * Set interruption chance of this goal. 
	 * <p> If it has interruption chance > 0, it will have a chance to be interrupted <b>each second</b> when running.
	 * <p> It's not recommended to set this value too large as it will be checked frequently.
	 * <p> When it's interrupted, it won't post {@link BefriendedGoalCheckCanUseEvent}.
	 */
	@SuppressWarnings("unchecked")
	public <T extends BefriendedTargetGoal> T setInterruptChance(double value)
	{
		this.interruptChance = value;
		return (T)this;
	}
	
	/**
	 * Set additional start condition of this goal. This method returns the goal itself, and use template class to specify the subclass to cast.
	 */
	@SuppressWarnings("unchecked")
	public <T extends BefriendedTargetGoal> T setStartCondition(Predicate<BefriendedTargetGoal> condition)
	{
		this.startCondition = condition;
		return (T)this;
	}
	
	/**
	 * Set if requires the owner to be present to start this goal.
	 */
	@SuppressWarnings("unchecked")
	public <T extends BefriendedTargetGoal> T setRequireOwnerPresent(boolean value)
	{
		this.requireOwnerPresent = value;
		return (T)this;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends BefriendedTargetGoal> T setExpireTicks(int ticks)
	{
		this.expireTicks = ticks;
		return (T)this;
	}
	
	/**
	 * Set this target goal should not expire.
	 */
	@SuppressWarnings("unchecked")
	public <T extends BefriendedTargetGoal> T noExpire()
	{
		this.expireTicks = -1;
		return (T)this;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends BefriendedTargetGoal> T setNoExpireCondition(Supplier<Boolean> condition)
	{
		this.noExpireCondition = condition;
		return (T)this;
	}
	
}
