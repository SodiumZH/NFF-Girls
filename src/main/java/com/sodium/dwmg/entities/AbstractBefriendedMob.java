package com.sodium.dwmg.entities;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.Level;

public abstract class AbstractBefriendedMob extends TamableAnimal {

	protected AbstractBefriendedMob(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}
	
}
