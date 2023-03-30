package net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal;

import java.util.EnumSet;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.sodiumstudio.dwmg.befriendmobs.entity.vanillapreset.enderman.AbstractBefriendedEnderMan;

public class BefriendedNearestAttackableTargetGoal<T extends LivingEntity> extends BefriendedTargetGoal {
  // private static final int DEFAULT_RANDOM_INTERVAL = 10;
   protected final Class<T> targetType;
   protected final int randomInterval;
   @Nullable
   protected LivingEntity target;
   /** This filter is applied to the Entity search. Only matching entities will be targeted. */
   protected TargetingConditions targetConditions;

   public BefriendedNearestAttackableTargetGoal(AbstractBefriendedEnderMan mob, Class<T> targetType, boolean mustSee) {
      this(mob, targetType, 10, mustSee, false, (Predicate<LivingEntity>)null);
   }

   public BefriendedNearestAttackableTargetGoal(AbstractBefriendedEnderMan mob, Class<T> targetType, boolean mustSee, Predicate<LivingEntity> targetPredicate) {
      this(mob, targetType, 10, mustSee, false, targetPredicate);
   }

   public BefriendedNearestAttackableTargetGoal(AbstractBefriendedEnderMan mob, Class<T> targetType, boolean mustSee, boolean mustReach) {
      this(mob, targetType, 10, mustSee, mustReach, (Predicate<LivingEntity>)null);
   }

   public BefriendedNearestAttackableTargetGoal(AbstractBefriendedEnderMan mob, Class<T> targetType, int pRandomInterval, boolean mustSee, boolean mustReach, @Nullable Predicate<LivingEntity> targetPredicate) {
      super(mob, mustSee, mustReach);
      this.targetType = targetType;
      this.randomInterval = reducedTickDelay(pRandomInterval);
      this.setFlags(EnumSet.of(Goal.Flag.TARGET));
      this.targetConditions = TargetingConditions.forCombat().range(this.getFollowDistance()).selector(targetPredicate);
      this.allowAllStatesExceptWait();
   }

   public boolean canUse() {
	  if (isDisabled())
		  return false;
      if (this.randomInterval > 0 && this.mob.asMob().getRandom().nextInt(this.randomInterval) != 0) {
         return false;
      } else {
         this.findTarget();
         return this.target != null;
      }
   }

   protected AABB getTargetSearchArea(double targetDistance) {
      return this.mob.asMob().getBoundingBox().inflate(targetDistance, 4.0D, targetDistance);
   }

   protected void findTarget() {
      if (this.targetType != Player.class && this.targetType != ServerPlayer.class) {
         this.target = this.mob.asMob().level.getNearestEntity(this.mob.asMob().level.getEntitiesOfClass(this.targetType, this.getTargetSearchArea(this.getFollowDistance()), (t) -> {
            return true;
         }), this.targetConditions, this.mob.asMob(), this.mob.asMob().getX(), this.mob.asMob().getEyeY(), this.mob.asMob().getZ());
      } else {
         this.target = this.mob.asMob().level.getNearestPlayer(this.targetConditions, this.mob.asMob(), this.mob.asMob().getX(), this.mob.asMob().getEyeY(), this.mob.asMob().getZ());
      }

   }

   /**
    * Execute a one shot task or start executing a continuous task
    */
   public void start() {
      this.mob.asMob().setTarget(this.target);
      super.start();
   }

   public void setTarget(@Nullable LivingEntity target) {
      this.target = target;
   }
}