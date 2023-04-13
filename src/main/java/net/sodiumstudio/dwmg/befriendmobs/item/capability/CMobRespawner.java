package net.sodiumstudio.dwmg.befriendmobs.item.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import net.sodiumstudio.dwmg.befriendmobs.entity.IBefriendedMob;

public interface CMobRespawner extends INBTSerializable<CompoundTag>
{
	
	public boolean isNoExpire();
	public void setNoExpire(boolean val);
	public boolean recoverInVoid();
	public void setRecoverInVoid(boolean val);
	public boolean isInvulnerable();
	public void setInvulnerable(boolean val);
	
	// This respawner as item stack
	public ItemStack getItemStack();
	
	// The whole mob nbt
	public CompoundTag getMobNbt();
	
	// Entity type to respawn
	public EntityType<? extends Mob> getType();
	
	// Make tag for initialize mob on respawn
	public CompoundTag makeMobData(Player player, BlockPos pos, Direction direction) ;
	
	// Initialize cap from mob 
	public void initFromMob(Mob mob);
	
	// Do respawn action
	public Mob respawn(Player player, BlockPos pos, Direction direction);
}
