package net.sodiumstudio.befriendmobs.item.event;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import net.sodiumstudio.befriendmobs.item.MobRespawnerInstance;

@Cancelable
public class MobRespawnerStartRespawnEvent extends Event
{
	protected MobRespawnerInstance resp;
	protected Player player;
	protected BlockPos pos;
	protected Direction dir;

	public MobRespawnerInstance getRespawner() {return resp;}
	public Player getPlayer() {return player;}
	public BlockPos getBlockPos() {return pos;}
	public Direction getDirection() {return dir;}

	public MobRespawnerStartRespawnEvent(MobRespawnerInstance resp, Player player, BlockPos pos, Direction dir)
	{
		this.resp = resp;
		this.player = player;
		this.pos = pos;
		this.dir = dir;		
	}
}
