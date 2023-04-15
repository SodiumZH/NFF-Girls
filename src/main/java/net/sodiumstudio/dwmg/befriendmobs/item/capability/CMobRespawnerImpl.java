package net.sodiumstudio.dwmg.befriendmobs.item.capability;

import java.awt.TextComponent;
import java.util.Objects;

import javax.annotation.Nonnull;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.LiteralContents;
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
import net.sodiumstudio.dwmg.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.util.EntityHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.NbtHelper;

public class CMobRespawnerImpl implements CMobRespawner
{
	protected ItemStack stack;
	
	public CompoundTag tag = new CompoundTag();
	
	CMobRespawnerImpl(@Nonnull ItemStack stack)
	{
		this.stack = stack;
	}
	
	@Override
	public boolean isNoExpire()
	{
		if (tag.contains("no_expire", NbtHelper.TagType.TAG_BYTE.getID()))
		{
			return tag.getBoolean("no_expire");
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public void setNoExpire(boolean val) 
	{
		tag.putBoolean("no_expire", val);
	}
	
	@Override
	public boolean recoverInVoid()
	{
		if (tag.contains("recover_in_void", NbtHelper.TagType.TAG_BYTE.getID()))
		{
			return tag.getBoolean("recover_in_void");
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public void setRecoverInVoid(boolean val)
	{
		tag.putBoolean("recover_in_void", val);
	}
	
	@Override
	public boolean isInvulnerable()
	{
		if (tag.contains("invulnerable", NbtHelper.TagType.TAG_BYTE.getID()))
		{
			return tag.getBoolean("invulnerable");
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public void setInvulnerable(boolean val)
	{
		tag.putBoolean("invulnerable", val);
	}
	
	@Override
	public ItemStack getItemStack()
	{
		return stack;
	}
	
	@Override
	public CompoundTag getMobNbt()
	{
		return tag.getCompound("mob_nbt");
	}
	
	
	@Override
	public CompoundTag serializeNBT() {
		return tag.copy();
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		tag = nbt.copy();
	}

	@SuppressWarnings("unchecked")
	@Override
	public EntityType<? extends Mob> getType() {
		return (EntityType<? extends Mob>) ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(tag.getString("mob_type")));
	}

	@Override
	public CompoundTag makeMobData(Player player, BlockPos pos, Direction direction) {
		CompoundTag nbt = this.tag.getCompound("mob_nbt");
		// Update position first, otherwise the generated mob will perform teleporting away and back
		Vec3 posV = new Vec3((double)pos.getX() + 0.5D, (double)(pos.getY() + 1), (double)pos.getZ() + 0.5D);
		NbtHelper.putVec3(nbt, "Pos", posV);
		return nbt;
	}
	
	@Override
	public void initFromMob(Mob mob) {
		tag.putString("mob_type", ForgeRegistries.ENTITY_TYPES.getKey(mob.getType()).toString());	
		CompoundTag nbt = new CompoundTag();
		mob.save(nbt);
		tag.put("mob_nbt", nbt);
		MinecraftForge.EVENT_BUS.post(new RespawnerAddedEvent(mob, this, stack));
	}

	
	@Override
	public Mob respawn(Player player, BlockPos pos, Direction direction) {
		if (player.level.isClientSide)
			return null;
		if (MinecraftForge.EVENT_BUS.post(new MobRespawnerStartRespawnEvent(this, player, pos, direction)))
			return null;
		BlockState blockstate = player.level.getBlockState(pos);
        BlockPos pos1;
        if (blockstate.getCollisionShape(player.level, pos).isEmpty()) {
           pos1 = pos;
        }
        else
        {
           pos1 = pos.relative(direction);
        }
		Mob mob = EntityHelper.spawnDefaultMob(
				getType(),
				(ServerLevel)(player.level),
				null,
				tag.contains("mob_custom_name", NbtHelper.TagType.TAG_STRING.getID()) ? MutableComponent.create(new LiteralContents(tag.getString("mob_custom_name"))) : null,
				player,
				pos1,
				true,
				!Objects.equals(pos, pos1) && direction == Direction.UP);
		if (mob != null)
		{
			CompoundTag nbt = makeMobData(player, pos1, direction);
			mob.setYRot(direction.toYRot());
			mob.load(nbt);
			mob.setHealth(mob.getMaxHealth());
			if (mob instanceof IBefriendedMob b)
			{
				b.getAdditionalInventory().clearContent();
			}
			//stack.shrink(1);
			MinecraftForge.EVENT_BUS.post(new MobRespawnerFinishRespawnEvent(mob, player, this));
		}
		return mob;
	}

}
