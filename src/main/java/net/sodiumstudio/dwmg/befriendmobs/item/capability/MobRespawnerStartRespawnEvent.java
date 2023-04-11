package net.sodiumstudio.dwmg.befriendmobs.item.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class MobRespawnerStartRespawnEvent extends Event
{
	protected CMobRespawner resp;
	protected Player player;
	protected BlockPos pos;
	protected Direction dir;

	public CMobRespawner getRespawner() {return resp;}
	public Player getPlayer() {return player;}
	public BlockPos getBlockPos() {return pos;}
	public Direction getDirection() {return dir;}

	public MobRespawnerStartRespawnEvent(CMobRespawner resp, Player player, BlockPos pos, Direction dir)
	{
		this.resp = resp;
		this.player = player;
		this.pos = pos;
		this.dir = dir;		
	}
}
