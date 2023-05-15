package net.sodiumstudio.dwmg.entities.capabilities;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import net.sodiumstudio.dwmg.entities.capabilities.CFavorabilityHandler.SyncPacket;
import net.sodiumstudio.dwmg.registries.DwmgCapabilities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.common.util.INBTSerializable;

public interface CLevelHandler extends INBTSerializable<LongTag>
{
	
	public Mob getMob();

	/**
	 * Get the accumulated exp of the mob.
	 */
	public long getExp();
	
	/**
	 * Force set the exp value to a given value.
	 * This method fires {@code ChangeExpValueEvent}, but not {@code GetExpEvent}.
	 */
	public void setExp(long val);
	
	/**
	 * Add a positive exp value.
	 * This method fires {@code GetExpEvent} but not {@code ChangeExpValueEvent}.
	 */
	public void addExp(long deltaVal);
	
	/**
	 * Get the expected level this mob should have.
	 */
	public int getExpectedLevel();
	
	/**
	 * Get the additional exp after upgrading to this level.
	 * Example: current accumulated exp is 45, upgrading to this level needs overall 40, then return 5.
	 */
	public long getExpInThisLevel();
	
	/**
	 * Get the additional exp required to upgrade from this level to next level.
	 */
	public long getRequiredExpInThisLevel();
	
	/**
	 * Sync the data to client.
	 * executed on server every tick
	 */
	public void sync(ServerPlayer toPlayer);
	
	// ===========
	
	public static class Impl implements CLevelHandler
	{

		protected final Mob mob;
		protected long exp = 0;
		protected int lvl = 0;
		
		public Impl(Mob mob)
		{
			this.mob = mob;
		}
		
		@Override
		public LongTag serializeNBT() {
			return LongTag.valueOf(exp);
		}

		@Override
		public void deserializeNBT(LongTag nbt) {
			exp = nbt.getAsLong();
			lvl = getExpectedLevel();
		}

		@Override
		public Mob getMob() {
			return mob;
		}

		@Override
		public long getExp() {
			return exp;
		}

		@Override
		public void setExp(long val) {
			if (val < 0)
				throw new IllegalArgumentException("Dwmg Level System: Illegal exp value (negative).");
			if (val == exp)
				return;
			ChangeExpEvent event = new ChangeExpEvent(this, getExp(), val);
			boolean canceled = MinecraftForge.EVENT_BUS.post(event);
			if (!canceled)
			{
				exp = event.newExp;
				int lvlOld = lvl;
				lvl = getExpectedLevel();
				if (lvlOld != lvl)
					MinecraftForge.EVENT_BUS.post(new LevelChangeEvent(this, lvlOld, lvl));
			}
		}

		@Override
		public void addExp(long deltaVal) {
			if (deltaVal < 0)
				throw new IllegalArgumentException("Dwmg Level System: Negative exp value to add. If reducing exp is needed, use setExp().");
			if (deltaVal == 0)
				return;
			if (mob.level.isClientSide)
			{
				throw new UnsupportedOperationException("Dwmg Level System: addExp() runs only on server. For client, use setExp().");
			}
			GetExpEvent event = new GetExpEvent(this, getExp(), deltaVal);
			boolean canceled = MinecraftForge.EVENT_BUS.post(event);
			if (!canceled)
			{
				exp += event.expAdded;
				int lvlOld = lvl;
				lvl = getExpectedLevel();
				if (lvlOld != lvl)
					MinecraftForge.EVENT_BUS.post(new LevelChangeEvent(this, lvlOld, lvl));
			}
		}
		}

		@Override
		public int getExpectedLevel() {
			return CLevelHandler.getExpectedLevel(exp);
		}

		@Override
		public long getExpInThisLevel() {
			return CLevelHandler.getCurrentExp(exp);
		}

		@Override
		public long getRequiredExpInThisLevel() {
			return CLevelHandler.getExpRequiredForLevelUp(lvl);
		}

		@Override
		public void sync(ServerPlayer toPlayer) {
			SyncPacket packet = new SyncPacket(mob.getId(), getExp());
			toPlayer.connection.send(packet);
			
		}		
	}
	// ========================
	
	public class Prvd implements ICapabilitySerializable<LongTag>
	{

		protected final CLevelHandler handler;
		
		public Prvd(Mob mob)
		{
			handler = new Impl(mob);
		}
		
		@Override
		public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
			if (cap == DwmgCapabilities.CAP_LEVEL_HANDLER)
				return LazyOptional.of(() -> {return this.handler;}).cast();
			else return LazyOptional.empty();
		}

		@Override
		public LongTag serializeNBT() {
			return handler.serializeNBT();
		}

		@Override
		public void deserializeNBT(LongTag nbt) {
			handler.deserializeNBT(nbt);
		}
	}
	
	// ==============================

	/**
	 * Fired when the mob's exp changes by {@code setExp}.
	 * The param {@value newExp} can be reset. If reset, the exp will be set to the new value instead.
	 * This event is cancelable. If canceled, the exp will not change.
	 */
	@Cancelable
	public static class ChangeExpEvent extends Event
	{
		public final Mob mob;
		public final CLevelHandler levelHandler;
		public final long oldExp;
		public long newExp;
		
		public ChangeExpEvent(CLevelHandler levelHandler, long oldExp, long newExp)
		{
			this.mob = levelHandler.getMob();
			this.levelHandler = levelHandler;
			this.oldExp = oldExp;
			this.newExp = newExp;
		}
	}
	
	// ==============================
	
	/**
	 * Fired only when the mob's exp changed by {@code addExp}.
	 * The param {@value expAdded} can be reset. If reset, the exp will be added the new value instead.
	 * This event is cancelable. If canceled, the exp will not change.
	 */
	@Cancelable
	public static class GetExpEvent extends Event
	{
		public final Mob mob;
		public final CLevelHandler levelHandler;
		public final long expBefore;
		public long expAdded;
		
		public GetExpEvent(CLevelHandler levelHandler, long expBefore, long expAdded)
		{
			this.mob = levelHandler.getMob();
			this.levelHandler = levelHandler;
			this.expBefore = expBefore;
			this.expAdded = expAdded;
		}
	}
	
	// ==============================
	
	/**
	 * Fired when mob level changes.
	 * This event is not {@code Cancelable}.
	 */
	public static class LevelChangeEvent extends Event
	{
		public final Mob mob;
		public final CLevelHandler levelHandler;
		public final int levelBefore;
		public final int levelAfter;
		
		public LevelChangeEvent(CLevelHandler levelHandler, int levelBefore, int levelAfter)
		{
			this.mob = levelHandler.getMob();
			this.levelHandler = levelHandler;
			this.levelBefore = levelBefore;
			this.levelAfter = levelAfter;
		}
	}
	
	// ====================================
	
	public static class SyncPacket implements Packet<ClientGamePacketListener>
	{
		public final int entityId;
		public final long exp;
		
		public SyncPacket(int entityId, long exp) {
			this.entityId = entityId;
			this.exp = exp;
		}

		public SyncPacket(FriendlyByteBuf buffer) {
			this.entityId = buffer.readInt();
			this.exp = buffer.readLong();
		}


		@Override
		public void write(FriendlyByteBuf buffer) {
			buffer.writeInt(this.entityId);
			buffer.writeLong(this.exp);			
		}

		@SuppressWarnings("resource")
		@Override
		public void handle(ClientGamePacketListener handler) {
			Minecraft mc = Minecraft.getInstance();
			PacketUtils.ensureRunningOnSameThread(this, handler, mc);
			Entity entity = mc.level.getEntity(this.entityId);
			// Needs a null check here as sometimes it may invoke on null??
			if (entity != null)
			{
				entity.getCapability(DwmgCapabilities.CAP_LEVEL_HANDLER).ifPresent((cap) ->
				{
					cap.setExp(this.exp);
				});	
			}
		}	
	}
	
	// ==============================
	// Related constants / statics
	
	
	/**
	 * Get ACCUMULATED exp for reaching this level.
	 * Identical to player exp table
	 */
	public static long getAccumulatedExpRequirement(int level)
	{
		if (level < 0)
			throw new IllegalArgumentException("Illegal level value");
		else if (level < 16)
			return level * level + level * 6;
		else 
		{
			double leveld = (double)level;
			if (level < 32)
				return Math.round(2.5d * leveld * leveld - 40.5d * leveld + 360d);
			else
				return Math.round(4.5d * leveld * leveld - 162.5 * leveld + 2220d);
		}
	}
	
	/**
	 * Get expected level for a given accumulated exp.
	 */
	public static int getExpectedLevel(long exp)
	{
		if (exp < 0)
			throw new IllegalArgumentException("Illegal exp value");
		// TODO Need this awkward algorithm be optimized?
		int i = 0;
		while (getAccumulatedExpRequirement(i) <= exp)
		{
			++i;
		}
		return i - 1;
	}
	
	/**
	 * Get exp under this level, i.e. (accumulated exp) - (exp required to reach this level)
	 */
	public static long getCurrentExp(long accumulatedExp)
	{
		return accumulatedExp - getAccumulatedExpRequirement(getExpectedLevel(accumulatedExp));
	}
	
	public static long getExpRequiredForLevelUp(int levelNow)
	{
		return getAccumulatedExpRequirement(levelNow + 1) - getAccumulatedExpRequirement(levelNow);
	}
}
