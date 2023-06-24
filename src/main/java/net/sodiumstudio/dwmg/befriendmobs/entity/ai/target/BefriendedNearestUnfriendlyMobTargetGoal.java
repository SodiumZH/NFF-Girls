package net.sodiumstudio.dwmg.befriendmobs.entity.ai.target;

import java.util.EnumSet;
import java.util.function.Predicate;

import javax.annotation.Nullable;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.befriendmobs.entity.ai.goal.BefriendedTargetGoal;

/**
 * Makes the mob to attack the nearest mob targeting it.
 */
public class BefriendedNearestUnfriendlyMobTargetGoal extends BefriendedTargetGoal
{
	// private static final int DEFAULT_RANDOM_INTERVAL = 10;
	protected final int randomInterval;
	@Nullable
	protected LivingEntity target;
	/**
	 * This filter is applied to the Entity search. Only matching entities will be
	 * targeted.
	 */
	protected TargetingConditions targetConditions;
	/** Attacker state filter. It will try targeting only when the attack fulfills this check. */
	protected Predicate<IBefriendedMob> stateConditions = m -> true;

	public BefriendedNearestUnfriendlyMobTargetGoal(IBefriendedMob mob, boolean mustSee)
	{
		this(mob, 10, mustSee, false, (Predicate<LivingEntity>) null);
	}

	public BefriendedNearestUnfriendlyMobTargetGoal(IBefriendedMob mob, boolean mustSee,
			Predicate<LivingEntity> targetPredicate)
	{
		this(mob, 10, mustSee, false, targetPredicate);
	}

	public BefriendedNearestUnfriendlyMobTargetGoal(IBefriendedMob mob, boolean mustSee, boolean mustReach)
	{
		this(mob, 10, mustSee, mustReach, (Predicate<LivingEntity>) null);
	}

	public BefriendedNearestUnfriendlyMobTargetGoal(IBefriendedMob mob, int pRandomInterval, boolean mustSee,
			boolean mustReach, @Nullable Predicate<LivingEntity> targetPredicate)
	{
		super(mob, mustSee, mustReach);
		this.randomInterval = reducedTickDelay(pRandomInterval);
		this.setFlags(EnumSet.of(Goal.Flag.TARGET));
		this.targetConditions = TargetingConditions.forCombat().range(this.getFollowDistance())
				.selector(targetPredicate);
		this.allowAllStatesExceptWait();
	}

	public BefriendedNearestUnfriendlyMobTargetGoal stateConditions(Predicate<IBefriendedMob> condition)
	{
		stateConditions = condition;
		return this;
	}
	
	@Override
	public boolean checkCanUse() {
		if (!stateConditions.test(mob))
			return false;
		if (this.randomInterval > 0 && this.mob.asMob().getRandom().nextInt(this.randomInterval) != 0)
		{
			return false;
		} else
		{
			this.findTarget();
			return this.target != null;
		}
	}

	protected AABB getTargetSearchArea(double targetDistance) {
		return this.mob.asMob().getBoundingBox().inflate(targetDistance, 4.0D, targetDistance);
	}

	protected void findTarget() {
	      double followDist = mob.asMob().getAttributeValue(Attributes.FOLLOW_RANGE);
	      AABB searchArea = new AABB(mob.asMob().position().subtract(new Vec3(followDist, followDist, followDist)), mob.asMob().position().add(new Vec3(followDist, followDist, followDist)));
	      List<Entity> unfriendlys = mob.asMob().level.getEntities(mob.asMob(), searchArea, (Entity e) -> 
	      {
	    	  if (e instanceof Mob m)
	    	  {
	    		  return m.getTarget() == mob.asMob() 
	    			&& mob.asMob().hasLineOfSight(m)
	    			&& mob.asMob().distanceToSqr(m) <= followDist * followDist
	    			&& targetConditions.test(mob.asMob(), m);
	    	  }
	    	  else return false;
	      });
	      if (unfriendlys.size() <= 0)
	      {
	    	  this.target = null;
	    	  return;
	      }
	      Mob setTarget = null;
	      for (Entity e: unfriendlys)
	      {
	    	  if (e instanceof Mob m)
	    	  {
	    		  if (setTarget == null || mob.asMob().distanceToSqr(m) > mob.asMob().distanceToSqr(setTarget))
	    		  {
	    			  setTarget = m;
	    		  }
	    	  }
	      }
	      this.target = setTarget;
	      return;
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void start() {
		this.mob.asMob().setTarget(this.target);
		super.start();
	}

	public void setTarget(@Nullable LivingEntity target) {
		this.target = target;
	}

}
