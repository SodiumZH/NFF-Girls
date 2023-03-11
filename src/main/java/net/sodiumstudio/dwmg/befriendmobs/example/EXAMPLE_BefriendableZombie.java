package net.sodiumstudio.dwmg.befriendmobs.example;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;


/** The example befriendable mob.
 * To avoid collision to other zombie befriending actions, we use a new example class.
 * 
 */
public class EXAMPLE_BefriendableZombie extends Zombie
{

	public EXAMPLE_BefriendableZombie(EntityType<? extends Zombie> pEntityType, Level pLevel)
	{
		super(pEntityType, pLevel);
		// TODO Auto-generated constructor stub
	}
}
