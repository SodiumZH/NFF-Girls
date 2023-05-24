package net.sodiumstudio.dwmg.befriendmobs.util;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.sodiumstudio.befriendmobs.util.ReflectHelper;

/**
 * Library for functions planned to add into BM.
 */
public class DwmgEntityHelper
{
	public static boolean isMobHostileTo(Mob test, LivingEntity isHostileTo)
	{
		// Check if the mob has a NearestAttackableTargetGoal<isHostileTo.class> goal
		 for(WrappedGoal goal: test.targetSelector.getAvailableGoals())
		 {
			 if (goal.getGoal() instanceof NearestAttackableTargetGoal<?> natg)
			 {
				 Class<?> targetType = (Class<?>) ReflectHelper.forceGet(natg, NearestAttackableTargetGoal.class, "targetConditions");
				 if (targetType == isHostileTo.getClass())
				 {
					 return true;
				 }
			 }
		 }
		 return false;
	}
	
	public static boolean isMobHostileToPlayer(Mob test)
	{
		// Check if the mob has a NearestAttackableTargetGoal<Player> goal
		 for(WrappedGoal goal: test.targetSelector.getAvailableGoals())
		 {
			 if (goal.getGoal() instanceof NearestAttackableTargetGoal<?> natg)
			 {
				 Class<?> targetType = (Class<?>) ReflectHelper.forceGet(natg, NearestAttackableTargetGoal.class, "targetConditions");
				 if (targetType == Player.class)
				 {
					 return true;
				 }
			 }
		 }
		 return false;
	}
}
