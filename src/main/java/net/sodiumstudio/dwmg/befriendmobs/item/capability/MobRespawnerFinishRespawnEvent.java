package net.sodiumstudio.dwmg.befriendmobs.item.capability;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;

public class MobRespawnerFinishRespawnEvent extends Event
{
	protected Mob mob;
	protected Player player;
	protected CMobRespawner resp;

	public Mob getMob() {return mob;}
	public Player getPlayer() {return player;}
	public CMobRespawner getRespawner() {return resp;}
	
	public MobRespawnerFinishRespawnEvent(Mob mob, Player player, CMobRespawner resp)
	{
		this.mob = mob;
		this.player = player;
		this.resp = resp;
	}
}
