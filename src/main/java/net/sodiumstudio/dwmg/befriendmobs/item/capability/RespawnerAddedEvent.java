package net.sodiumstudio.dwmg.befriendmobs.item.capability;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

public class RespawnerAddedEvent extends Event
{
	protected Mob mob;
	protected CMobRespawner respawner;
	protected ItemStack stack;
	
	public Mob getMob() {return mob;};
	public CMobRespawner getRespawner() {return respawner;};
	public ItemStack getStack() {return stack;}
	
	public RespawnerAddedEvent(Mob mob, CMobRespawner respawner, ItemStack stack)
	{
		this.mob = mob;
		this.respawner = respawner;
		this.stack = stack;
	}
	
}
