package net.sodiumstudio.befriendmobs.item;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.ForgeRegistries;
import net.sodiumstudio.befriendmobs.entity.befriended.BefriendedHelper;
import net.sodiumstudio.befriendmobs.entity.befriended.CBefriendedMobData;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.item.event.MobRespawnerFinishRespawnEvent;
import net.sodiumstudio.befriendmobs.item.event.MobRespawnerStartRespawnEvent;
import net.sodiumstudio.befriendmobs.item.event.RespawnerConstructEvent;
import net.sodiumstudio.nautils.EntityHelper;
import net.sodiumstudio.nautils.InfoHelper;
import net.sodiumstudio.nautils.NbtHelper;

/**
 * Wrapper of item stack of mob respawner
 */
public class MobRespawnerInstance
{
	protected ItemStack stack;
	
	protected MobRespawnerInstance(ItemStack stack)
	{
		if (!(stack.getItem() instanceof MobRespawnerItem))
		{
			throw new IllegalArgumentException("MobRespawnerInstance: illegal itemstack type.");
		}
		this.stack = stack;
		if (!stack.getOrCreateTag().contains("mob_respawner"))
			stack.getOrCreateTag().put("mob_respawner", new CompoundTag());
	}
	
	public void set(ItemStack stack)
	{
		if (!(stack.getItem() instanceof MobRespawnerItem))
		{
			throw new IllegalArgumentException("MobRespawnerInstance: illegal itemstack type.");
		}
		this.stack = stack;
		if (!stack.getOrCreateTag().contains("mob_respawner"))
			stack.getOrCreateTag().put("mob_respawner", new CompoundTag());
	}
	
	public ItemStack get()
	{
		return stack;
	}
	
	/**
	 * Transform ItemStack to MobRespawnerInstance.
	 * If empty or type mismatching, return null.
	 */
	@Nullable
	public static MobRespawnerInstance create(ItemStack stack)
	{
		if (!stack.isEmpty() && stack.getItem() instanceof MobRespawnerItem)
		{
			return new MobRespawnerInstance(stack);
		}
		return null;
	}
	
	
	// ====================
	// Previous CMobRespawner

	public CompoundTag getCapTag() {
		return this.get().getOrCreateTag().getCompound("mob_respawner");
	}

	public boolean isNoExpire() {
		if (getCapTag().contains("no_expire", NbtHelper.TAG_BYTE_ID))
		{
			return getCapTag().getBoolean("no_expire");
		} else
		{
			return false;
		}
	}

	public void setNoExpire(boolean val) {
		getCapTag().putBoolean("no_expire", val);
	}

	public boolean recoverInVoid() {
		if (getCapTag().contains("recover_in_void", NbtHelper.TAG_BYTE_ID))
		{
			return getCapTag().getBoolean("recover_in_void");
		} else
		{
			return false;
		}
	}

	public void setRecoverInVoid(boolean val) {
		getCapTag().putBoolean("recover_in_void", val);
	}

	public boolean isInvulnerable() {
		if (getCapTag().contains("invulnerable", NbtHelper.TAG_BYTE_ID))
		{
			return getCapTag().getBoolean("invulnerable");
		} else
		{
			return false;
		}
	}

	public void setInvulnerable(boolean val) {
		getCapTag().putBoolean("invulnerable", val);
	}

	public CompoundTag getMobNbt() {
		return getCapTag().getCompound("mob_nbt");
	}

	@SuppressWarnings("unchecked")
	public EntityType<? extends Mob> getType() {
		return (EntityType<? extends Mob>) ForgeRegistries.ENTITY_TYPES
				.getValue(new ResourceLocation(getCapTag().getString("mob_type")));
	}

	public CompoundTag makeMobData(Player player, BlockPos pos, Direction direction) {
		CompoundTag nbt = getCapTag().getCompound("mob_nbt");
		// Update position first, otherwise the generated mob will perform teleporting
		// away and back
		Vec3 posV = new Vec3((double) pos.getX() + 0.5D, (double) (pos.getY() + 1), (double) pos.getZ() + 0.5D);
		NbtHelper.putVec3(nbt, "Pos", posV);
		return nbt;
	}

	public void initFromMob(Mob mob) {
		MinecraftForge.EVENT_BUS.post(new RespawnerConstructEvent.Before(mob, this));
		getCapTag().putString("mob_type", ForgeRegistries.ENTITY_TYPES.getKey(mob.getType()).toString());
		CompoundTag nbt = new CompoundTag();
		mob.save(nbt);
		getCapTag().put("mob_nbt", nbt);
		MinecraftForge.EVENT_BUS.post(new RespawnerConstructEvent.After(mob, this));
	}

	public Mob respawn(Player player, BlockPos pos, Direction direction) {
		if (player.level.isClientSide)
			return null;
		if (MinecraftForge.EVENT_BUS.post(new MobRespawnerStartRespawnEvent(this, player, pos, direction)))
			return null;
		BlockState blockstate = player.level.getBlockState(pos);
		BlockPos pos1;
		if (blockstate.getCollisionShape(player.level, pos).isEmpty())
		{
			pos1 = pos;
		} else
		{
			pos1 = pos.relative(direction);
		}
		Mob mob = EntityHelper.spawnDefaultMob(getType(), (ServerLevel) (player.level), null,
				getCapTag().contains("mob_custom_name", NbtHelper.TAG_STRING_ID)
						? InfoHelper.createText(getCapTag().getString("mob_custom_name"))
						: null,
				player, pos1, true, !pos.equals(pos1) && direction == Direction.UP);
		if (mob != null)
		{
			CompoundTag nbt = makeMobData(player, pos1, direction);
			mob.setYRot(direction.toYRot());
			mob.load(nbt);
			mob.setHealth(mob.getMaxHealth());
			if (mob instanceof IBefriendedMob b)
			{
				//b.setInventoryFromMob();
				b.updateAnchor();
				b.setInit();
			}
			// stack.shrink(1);
			MinecraftForge.EVENT_BUS.post(new MobRespawnerFinishRespawnEvent(mob, player, this));
		}
		return mob;
	}

	public UUID getOwnerUUID()
	{
		return CBefriendedMobData.getOwnerUUIDFromMobTag(getMobNbt());
	}
	
	public String getModId()
	{
		return CBefriendedMobData.getModIdFromMobTag(getMobNbt());
	}
	
	public Component getName()
	{
		return EntityHelper.getNameFromNbt(getMobNbt(), getType());
	}
	
	public UUID getUUID()
	{
		return getMobNbt().getUUID("UUID");
	}
}
