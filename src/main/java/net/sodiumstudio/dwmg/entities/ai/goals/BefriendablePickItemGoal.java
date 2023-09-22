package net.sodiumstudio.dwmg.entities.ai.goals;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraftforge.registries.ForgeRegistries;
import net.sodiumstudio.befriendmobs.entity.befriending.registry.BefriendingTypeRegistry;
import net.sodiumstudio.befriendmobs.entity.capability.CBefriendableMob;
import net.sodiumstudio.befriendmobs.registry.BMCaps;
import net.sodiumstudio.nautils.EntityHelper;
import net.sodiumstudio.dwmg.entities.handlers.hmag.HandlerItemDropping;

public class BefriendablePickItemGoal extends Goal
{

	protected final Random rnd = new Random();
	protected final Mob mob;
	protected final CBefriendableMob cap;
	protected final HandlerItemDropping handler;
	/** Determines the frequency this goal is attempted to run. Larger value means lower frequency. */
	protected int chance = 5;
	protected ItemEntity targetItem = null;
	protected double speedModifier = 1.0d;
	
	/** The timer for picking a specific item. If it failed to pick up an item (and end running this goal), the goal will be 
	 * force ended, preventing endless attempt to access an unreachable position. */
	protected int targetExpireTimer = 10 * 20; 
	
	@SuppressWarnings("unchecked")
	public BefriendablePickItemGoal(Mob mob)
	{
		this.mob = mob;
		if (mob.getCapability(BMCaps.CAP_BEFRIENDABLE_MOB).isPresent())
		{
			cap = CBefriendableMob.getCap(mob);
		}
		else throw new UnsupportedOperationException("This goal supports only mobs with CBefriendableMob capability.");
		if (!(BefriendingTypeRegistry.getHandler(mob) instanceof HandlerItemDropping))
			throw new UnsupportedOperationException("This goal supports befriendable mobs only with HandlerItemDropping as befriending handler.");

		this.handler = (HandlerItemDropping) BefriendingTypeRegistry.getHandler((EntityType<? extends Mob>) mob.getType());
	}
	
	public BefriendablePickItemGoal(Mob mob, double speedModifier)
	{
		this(mob);
		this.speedModifier = speedModifier;
	}
	
	public BefriendablePickItemGoal(Mob mob, double speedModifier, int chance)
	{
		this(mob, speedModifier);
		this.chance = chance;
	}
	
	protected CBefriendableMob cap()
	{
		return CBefriendableMob.getCap(mob);
	}
	
	protected List<ItemEntity> getAcceptableItems()
	{
		List<ItemEntity> list = new ArrayList<ItemEntity>();
		mob.level().getEntities(mob, EntityHelper.getNeighboringArea(mob, 8, 2, 8))
		.stream().filter((Entity e) -> {
			if (e instanceof ItemEntity ie)
			{
				return handler.canPickUpItem(mob, ie) && mob.hasLineOfSight(ie);
			}
			return false;
		}).forEach((Entity e) -> {list.add((ItemEntity)e);});
		return list;
	}
	
	
	@Override
	public boolean canUse() 
	{
		return /*rnd.nextInt(chance) == 0 &&*/ getAcceptableItems().size() > 0;
	}
	
	@Override
	public boolean canContinueToUse() 
	{
		return targetItem != null 	
				&& targetItem.isAlive() // The selected item still exists
				&& getAcceptableItems().contains(targetItem)	// And in range
				&& targetExpireTimer > 0;	// And it didn't take too much time reaching it
	}
	
	@Override
	public void start()
	{
		/** If it doesn't have a target item or existing item is out of range,
		 * reset the item to the closest one. 
		 * If the old target is still valid, don't reset.
		 */
		List<ItemEntity> items = getAcceptableItems().stream()
				.sorted(Comparator.comparingDouble((Entity e) -> e.distanceToSqr(mob)))
				.toList();	
		if (items.size() == 0)
		{
			targetItem = null;
			return;
		}
		if (targetItem == null || !items.contains(targetItem))
			targetItem = items.get(0);
		
	}
	
	@Override
	public void tick()
	{
		/**
		 * Set movement depending on the move control type.
		 * Supporting navigation for PathfinderMob and move control for other mobs.
		 */
		if (mob instanceof PathfinderMob)
		{
			mob.getNavigation().moveTo(targetItem, 1.0d);
		}
		else if (mob.getMoveControl() != null)
			mob.getMoveControl().setWantedPosition(targetItem.getX(), targetItem.getY(), targetItem.getZ(), 1);
		else throw new UnsupportedOperationException("Mob missing valid move control.");
		if (targetExpireTimer > 0)
			targetExpireTimer --;
	}

	@Override
	public void stop()
	{
		this.targetItem = null;
		this.targetExpireTimer = 10 * 20;
	}
	
}
