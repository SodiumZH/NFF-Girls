package net.sodiumstudio.befriendmobs.level;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.sodiumstudio.befriendmobs.BefriendMobs;
import net.sodiumstudio.befriendmobs.bmevents.BMHooks;
import net.sodiumstudio.befriendmobs.entity.befriending.registry.BefriendingTypeRegistry;
import net.sodiumstudio.befriendmobs.entity.capability.CBMPlayerModule;
import net.sodiumstudio.befriendmobs.entity.capability.CBMPlayerModule.Impl;
import net.sodiumstudio.befriendmobs.item.MobRespawnerInstance;
import net.sodiumstudio.befriendmobs.item.MobRespawnerItem;
import net.sodiumstudio.befriendmobs.registry.BMCaps;
import net.sodiumstudio.nautils.NaContainerUtils;
import net.sodiumstudio.nautils.NbtHelper;
import net.sodiumstudio.nautils.Wrapped;
import net.sodiumstudio.nautils.debug.Debug;

/**
 * Comprehensive serializable module for levels in BM.
 * <p> Note: it's only for server. To sync the data you must send packets.
 */
public interface CBMLevelModule extends INBTSerializable<CompoundTag>
{

	/** Get the attached level. */
	public ServerLevel getLevel();

	/** Get the whole NBT tag. */
	public CompoundTag getNbt();
	
	/** Get the overall tick count from this level created. */
	public long getTickCount();
	
	/** Add a suspended respawner.
	 * <p> A suspended respawner is a respawner item stack generated on befriended mob dying that cannot be instantly given back to the owner.
	 * So it will be temporarily saved in the level, and once the owner enters the level, the respawner will be given and removed from the suspended list.
	 * <p> Suspended respawners are added only when {@link IBefriendedMob#getDeathRespawnerGenerationType} returns {@code DeathRespawnerGenerationType.GIVE}, but can be given back any time.
	 * <p> The adding action will be handled in {@link BMEntityEvents#onLivingDeath}.
	 * @param owner UUID of the owner.
	 */
	public void addSuspendedRespawner(MobRespawnerInstance respawner);
	
	/** Try returning a suspended respawner to the owner, and remove it from suspended list if succeeded.
	 * @param key UUID of the mob as the key.
	 * @return Whether succeeded. If succeeded, the UUID entry will NOT be removed, since in updating it's an iteration and the invalidated entries will be removed together after that.
	 */
	public boolean tryReturnSuspendedRespawner(String key);
	
	/**
	 * Remove a suspended respawner.
	 */
	public void removeSuspendedRespawner(String key);
	
	/** Scan the saved respawner list, check if some of them can be given to the owner, and give them */
	public void updateSuspendedRespawners();

	/** Executed every tick.
	 * Called in {@link BMServerEvents#onLevelTick}.
	 * <p> It's ticked after the vanilla level tick.
	 */
	public void tick();

	
	// ============================ Implementation ============================ //
	
	public static class Impl implements CBMLevelModule
	{

		protected ServerLevel level;
		protected CompoundTag nbt;
		protected long tickCount = 0;
		
		
		protected Impl(ServerLevel level)
		{
			this.level = level;
			this.nbt = new CompoundTag();
			nbt.put("suspended_respawners", new CompoundTag());
		}
		
		
		@Override
		public CompoundTag serializeNBT() {
			return nbt.copy();
		}

		@Override
		public void deserializeNBT(CompoundTag nbt) {
			this.nbt = nbt.copy();
		}

		@Override
		public ServerLevel getLevel() {
			return level;
		}

		@Override
		public CompoundTag getNbt() {
			return nbt;
		}

		@Override
		public long getTickCount()
		{
			return tickCount;
		}
		
		@Override
		public void addSuspendedRespawner(MobRespawnerInstance respawner) {
			CompoundTag tag = new CompoundTag();
			respawner.get().save(tag);
			this.getNbt().getCompound("suspended_respawners").put(respawner.getUUID().toString(), tag);
		}
		
		@Override
		public boolean tryReturnSuspendedRespawner(String key) {
			CompoundTag container = this.getNbt().getCompound("suspended_respawners");
			if (!container.contains(key, NbtHelper.TAG_COMPOUND_ID))
				return false;	// No such entry
			ItemStack stack = ItemStack.of(container.getCompound(key));
			if (stack == null)
				return false;	// Not a valid item stack
			MobRespawnerInstance resp = MobRespawnerInstance.create(stack);
			if (resp == null)
				return false;	// Not a valid respawner
			if (this.getLevel().getPlayerByUUID(resp.getOwnerUUID()) == null)
				return false;	// Player isn't present
			if (this.getLevel().getPlayerByUUID(resp.getOwnerUUID()).getInventory().getFreeSlot() == -1)
				return false;	// Player's inventory is full
			if (!this.getLevel().getPlayerByUUID(resp.getOwnerUUID()).addItem(resp.get()))
				return false;	// Adding failed for possible other reason
			else return true;	// Successfully added
		}

		@Override
		public void removeSuspendedRespawner(String key) 
		{
			this.getNbt().getCompound("suspended_respawners").remove(key);
		}
		
		@Override
		public void updateSuspendedRespawners() {
			CompoundTag container = this.getNbt().getCompound("suspended_respawners");
			HashSet<String> toRemove = new HashSet<>();
			for (String key: container.getAllKeys())
			{
				if (this.tryReturnSuspendedRespawner(key))
					toRemove.add(key);
			}
			for (String key: toRemove)
			{
				this.removeSuspendedRespawner(key);
			}
		}
		
		@Override
		public void tick()
		{
			tickCount += 1;
			BMHooks.Level.onModuleTickStart(level);
			
			if (getTickCount() % 20 == 0)
				updateSuspendedRespawners();
			
			// Debug output time
			if (BefriendMobs.IS_DEBUG_MODE && getTickCount() % 200 == 0 && getLevel().players().size() > 0)
				Debug.printToScreen("Time (s) : " + Long.toString(getTickCount()), getLevel().players().get(0));
			
			BMHooks.Level.onModuleTickEnd(level);
		}
	}
	
	
	// ============================ Provider ============================ //	
	
	public static class Prvd implements ICapabilitySerializable<CompoundTag>
	{

		protected final Impl impl;
		
		public Prvd(ServerLevel level)
		{
			impl = new Impl(level);
		}
		
		@Override
		public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
			if (cap == BMCaps.CAP_BM_LEVEL)
				return LazyOptional.of(() -> {return this.impl;}).cast();
			else return LazyOptional.empty();
		}

		@Override
		public CompoundTag serializeNBT() {
			return impl.serializeNBT();
		}

		@Override
		public void deserializeNBT(CompoundTag nbt) {
			impl.deserializeNBT(nbt);
		}
		
	}
	
	public static CBMPlayerModule get(Player player)
	{
		Wrapped<CBMPlayerModule> wrp = new Wrapped<>(null);
		player.getCapability(BMCaps.CAP_BM_PLAYER).ifPresent(c -> 
		{
			wrp.set(c);
		});
		return wrp.get();
	}
	
}
