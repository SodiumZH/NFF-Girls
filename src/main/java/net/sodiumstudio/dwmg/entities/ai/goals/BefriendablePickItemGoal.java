package net.sodiumstudio.dwmg.entities.ai.goals;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraftforge.registries.ForgeRegistries;
import net.sodiumstudio.befriendmobs.entity.befriending.registry.BefriendingTypeRegistry;
import net.sodiumstudio.befriendmobs.entity.capability.CBefriendableMob;
import net.sodiumstudio.befriendmobs.registry.BefMobCapabilities;
import net.sodiumstudio.befriendmobs.util.EntityHelper;
import net.sodiumstudio.dwmg.entities.handlers.hmag.HandlerItemDropping;

public class BefriendablePickItemGoal extends Goal
{

	protected Mob mob;
	protected HandlerItemDropping handler;
	
	@SuppressWarnings("unchecked")
	public BefriendablePickItemGoal(Mob mob)
	{
		this.mob = mob;
		this.handler = (HandlerItemDropping) BefriendingTypeRegistry.getHandler((EntityType<? extends Mob>) mob.getType());
	}
	
	protected CBefriendableMob cap()
	{
		return CBefriendableMob.getCap(mob);
	}
	
	protected List<ItemEntity> getAcceptableItems()
	{
		List<ItemEntity> list = new ArrayList<ItemEntity>();
		mob.level.getEntities(mob, EntityHelper.getNeighboringArea(mob, 8))
		.stream().filter((Entity e) -> {
			if (e instanceof ItemEntity ie)
			{
				return handler.getDeltaProc().containsKey(ForgeRegistries.ITEMS.getKey(ie.getItem().getItem()).toString())
						&& !cap().getHatred().contains(mob.getUUID());
			}
			return false;
		}).forEach((Entity e) -> {list.add((ItemEntity)e);});
		return list;
	}
	
	
	@Override
	public boolean canUse() 
	{
		return false;
		/*targetingItems = 
		return targetingItems.size() > 0;*/
	}

	
	
}
