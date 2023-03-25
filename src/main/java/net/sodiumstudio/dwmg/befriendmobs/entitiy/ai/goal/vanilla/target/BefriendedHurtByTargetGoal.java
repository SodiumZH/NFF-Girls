package net.sodiumstudio.dwmg.befriendmobs.entitiy.ai.goal.vanilla.target;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.phys.AABB;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.ai.goal.BefriendedTargetGoal;

@SuppressWarnings("unused")
public class BefriendedHurtByTargetGoal extends BefriendedTargetGoal {

	private static final TargetingConditions HURT_BY_TARGETING = TargetingConditions.forCombat().ignoreLineOfSight()
			.ignoreInvisibilityTesting();
	private static final int ALERT_RANGE_Y = 10;
	private boolean alertSameType;
	/** Store the previous revengeTimer value */
	private int timestamp;
	private final Class<?>[] toIgnoreDamage;
	@Nullable
	private Class<?>[] toIgnoreAlert;

	public BefriendedHurtByTargetGoal(IBefriendedMob mob, Class<?>... pToIgnoreDamage) {
		super(mob, true);
		this.toIgnoreDamage = pToIgnoreDamage;
		this.setFlags(EnumSet.of(Goal.Flag.TARGET));
		allowAllStatesExceptWait();
	}

	/**
	 * Returns whether execution should begin. You can also read and cache any state
	 * necessary for execution in this handler as well.
	 */
	public boolean canUse() {
		if (isDisabled())
			return false;
		LivingEntity lastHurt = getMob().getLastHurtByMob();
		if (getMob().getLastHurtByMobTimestamp() != this.timestamp && lastHurt != null) 
		{
			if (lastHurt instanceof Player && mob.getOwner() != (Player)lastHurt) 
				return false;
			else 
			{
				for (Class<?> oclass : this.toIgnoreDamage) 
				{
					if (oclass.isAssignableFrom(lastHurt.getClass())) {
						return false;
					}
				}
				if (this.canAttack(lastHurt, HURT_BY_TARGETING) && mob.wantsToAttack(lastHurt))
					return true;
				else return false;
			}
		} 
		else return false;
	}

	public BefriendedHurtByTargetGoal setAlertOthers(Class<?>... pReinforcementTypes) {
		this.alertSameType = true;
		this.toIgnoreAlert = pReinforcementTypes;
		return this;
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void start() {
		getMob().setTarget(getMob().getLastHurtByMob());
		this.targetMob = getMob().getTarget();
		this.timestamp = getMob().getLastHurtByMobTimestamp();
		this.unseenMemoryTicks = 300;
		if (this.alertSameType) {
			this.alertOthers();
		}

		super.start();
	}

	protected void alertOthers() {
		double d0 = this.getFollowDistance();
		AABB aabb = AABB.unitCubeFromLowerCorner(getMob().position()).inflate(d0, 10.0D, d0);
		List<? extends Mob> list = getMob().level.getEntitiesOfClass(getMob().getClass(), aabb,
				EntitySelector.NO_SPECTATORS);

		for (Mob other : list) {
			boolean dontAlert = false;
			// Other is not this, and doesn't have target
			if (other == getMob() || other.getTarget() != null)
				dontAlert = true;
			// Other isn't a tamable owned by other player
			else if (other instanceof TamableAnimal tamable) {
				if (mob.getOwner() != tamable.getOwner())
					dontAlert = true;
			}
			// For befriended, the same
			else if (other instanceof IBefriendedMob bef) {
				if (mob.getOwner() != bef.getOwner())
					dontAlert = true;
			}
			// Other isn't allied to the target
			else if (other.isAlliedTo(getMob().getLastHurtByMob()))
				dontAlert = true;
			// Other's class isn't in ignore alert class list
			else if (this.toIgnoreAlert != null) {
				for (Class<?> oclass : this.toIgnoreAlert) {
					if (mob.getClass() == oclass) {
						dontAlert = true;
						break;
					}
				}
			}
			if (!dontAlert)
				this.alertOther(other, getMob().getLastHurtByMob());
		}
	}

	protected void alertOther(Mob pMob, LivingEntity pTarget) {
		pMob.setTarget(pTarget);
	}
}
