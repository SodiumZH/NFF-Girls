package net.sodiumstudio.befriendmobs.entity.befriended;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mojang.logging.LogUtils;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import net.sodiumstudio.befriendmobs.bmevents.entity.BefriendedMobDataConstructEvent;
import net.sodiumstudio.befriendmobs.entity.ai.BefriendedAIState;
import net.sodiumstudio.befriendmobs.entity.befriending.BefriendingHandler;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventory;
import net.sodiumstudio.befriendmobs.network.BMChannels;
import net.sodiumstudio.befriendmobs.network.BMClientGamePacketHandler;
import net.sodiumstudio.befriendmobs.registry.BMCaps;
import net.sodiumstudio.nautils.NaReflectionUtils;
import net.sodiumstudio.nautils.NaUtils;
import net.sodiumstudio.nautils.NbtHelper;
import net.sodiumstudio.nautils.annotation.DontCallManually;
import net.sodiumstudio.nautils.capability.CEntityTickingCapability;
import net.sodiumstudio.nautils.containers.Tuple3;
import net.sodiumstudio.nautils.function.MutablePredicate;
import net.sodiumstudio.nautils.network.NaUtilsDataSerializer;


/**
 * A temporal module for storage of data in IBefriendedMob interface.
 */
public interface CBefriendedMobData extends INBTSerializable<CompoundTag>, CEntityTickingCapability<Mob> {
	
	// General //
	
	/** Get the befriended mob owning this data. */
	public IBefriendedMob getBM();

	/** Get the whole additional NBT as {@link CompoundTag}.
	 * <p>Use this if the additional data needs to be saved but not synched. If needs to be synched, use the synched data system.
	 */
	public CompoundTag getAdditionalNBT();
	
	/** Get sun immunity. It only works when the mob is an {@link IBefriendedSunSensitiveMob}, otherwise throws exception. */
	public MutablePredicate<IBefriendedSunSensitiveMob> getSunImmunity();
	
	/** Get temporary object from a key from table. Temporary object table is a non-serialized object table to store any objects, 
	 * not directly accessible but only with {@code getTempObject}, {@code addTempObject} and {@code removeTempObject}.
	 * @return Object if present, or null if not.
	 */
	@Nullable
	public Object getTempObject(String key);
	
	/** Add a temporary object to table. Temporary object table is a non-serialized object table to store any objects.*/
	public void addTempObject(String key, Object obj);
	
	/** Remove a temporary object from table. Temporary object table is a non-serialized object table to store any objects.
	 * If the key is absent, it will not do anything.
	 */
	public void removeTempObject(String key);
	
	/**
	 * Force access a value with casted class.
	 * If it's absent, return null. If class mismatches, log error and return null.
	 * @return Value with casted class, or null if absent or class mismatching.
	 */
	@SuppressWarnings("unchecked")
	@Nullable
	public default <T> T getTempObjectCast(String key)
	{
		Object obj = getTempObject(key);
		if (obj == null) return null;
		try {
			return (T) obj;
		}
		catch (ClassCastException e) {
			LogUtils.getLogger().error("CBefriendedMobData#getTempObjectCasted: class cast mismatch found.");
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Get the UUID identifier of this mob. (Independent to the entity UUID. 
	 * This is generated on befriended for identifying a mob even if it has respawned with a new UUID.)
	 */
	public UUID getIdentifier();
	
	/**
	 * Generate UUID identifier of this mob. (Independent to the entity UUID. 
	 * This is generated on befriended for identifying a mob even if it has respawned with a new UUID.)
	 */
	@DontCallManually
	public void generateIdentifier();

	/**
	 * Get the registry key of the entity type of this mob on befriended.
	 * <p>
	 * This allows to read the mob's "initial" type after it converts to other
	 * types somehow.
	 */
	public EntityType<? extends Mob> getInitialEntityType();
	
	/**
	 * Record the registry key of the entity type of this mob on befriended.
	 * <p>
	 * This allows to read the mob's "initial" type after it converts to other
	 * types somehow.
	 */
	public void recordEntityType();
	
	// Owner info related //
	
	/**
	 * Get the owner's display name, or "(Unknown)" if not found.
	 */
	public String getOwnerName();
	
	/**
	 * Set the owner's display name stored.
	 */
	public void setOwnerName(String val);
	
	/**
	 * Get the date player encountered it, including befriended, or took ownership from another player.
	 * @since 0.x.20
	 * @return An int[3] indicating year, month and day, or (0,0,0) if not recorded (legacy).
	 */
	@Nullable
	public int[] getEncounteredDate();
	
	/**
	 * Record info about befriending time and location. Invoked in {@link BefriendingHandler#befriend}.
	 */
	@DontCallManually
	public void setEncounteredDate(int[] val);
	
	public default void recordEncounteredDate()
	{
		LocalDate now = LocalDate.now();
		int[] date = new int[] {now.get(ChronoField.YEAR), now.get(ChronoField.MONTH_OF_YEAR), now.get(ChronoField.DAY_OF_MONTH)};
		this.setEncounteredDate(date);
	}
	
	public UUID getOwnerUUID();
	
	public void setOwnerUUID(UUID value);
	
	// Behavior related //
	
	/**
	 * Get random stroll anchor point as vector.
	 */
	public Vec3 getAnchor();
	
	/**
	* Set random stroll anchor point.
	*/ 
	public void setAnchor(Vec3 anchor);

	public BefriendedAIState getAIState();
	
	public void setAIState(BefriendedAIState state); 
	
	/**
	 * <b> Don't call manually! </b> This method is only called in {@link IBefriendedMob#getPreviousTarget}.
	 */
	@DontCallManually
	@Nullable
	public LivingEntity getPreviousTarget();
	
	/**
	 * <b> Don't call manually! </b> This method is only called in {@link IBefriendedMob#setPreviousTarget}.
	 */
	@DontCallManually
	@Nullable
	public void setPreviousTarget(LivingEntity target);
	
	// Inventory related //
	
	public BefriendedInventory getAdditionalInventory();
	
	// Synched Data related //
	
	/**
	 * Create a synched data. Synched data must be created before other any operations.
	 * @param <T>Data class. Exactly same to the data class in {@code dataSerialzier}.
	 * @param key Data key as string.
	 * @param dataSerialzier Data serializer applied.
	 * @param initValue Default value if not set.
	 */
	public <T> void createSynchedData(String key, NaUtilsDataSerializer<T> dataSerialzier, T initValue);	

	public <T> boolean hasSynchedData(String key, Class<T> dataType);

	public <T> T getSynchedData(String key, Class<T> dataClass);
	
	public <T> T getSynchedDataUnchecked(String key);
	
	/**
	 * Set synched data value. Only on server.
	 * @param key Synched data key
	 * @param dataClass Expected data class. <i>This is a salt to ensure you know what class this field expects to receive.</i>
	 * @param value New value.
	 */
	public <T> void setSynchedData(String key, Class<T> dataClass, T value);
	
	public void setSynchedDataClient(String key, NaUtilsDataSerializer<?> serializer, Object value);
	
	public void setDataSyncInterval(int ticks);

	// Misc //
	
	/**
	 * <b> Don't call manually! </b> Use {@link IBefriendedMob#hasInit()} instead.
	 * Get whether this mob has finished initialization.
	 * <p>After finishing initialization the mob will start updating from its inventory.
	 */
	@DontCallManually
	public boolean hasInit();
	
	/**
	 * <b> Don't call manually! </b> Use {@link IBefriendedMob#setInit()} or {@link IBefriendedMob#setNotInit()} instead.
	 * Set the label for if this mob has finished initialization.
	 */
	@DontCallManually
	public void setInitState(boolean value);

	// ********************************************************************************* //
	// ********************************************************************************* //
	
	// Values of mob data, also as implementation of interface methods.
	public static class Values implements CBefriendedMobData
	{
		private static final UUID EMPTY_UUID = new UUID(0l, 0l);
		private IBefriendedMob mob;

		// General
		private static final String IDENTIFIER_SYNCHED_KEY = "identifier";
		private EntityType<? extends Mob> initialType = null;
		private CompoundTag nbt = new CompoundTag();
		// Owner
		private static final String OWNER_UUID_SYNCHED_KEY = "ownerUUID";
		private static final String OWNER_NAME_SYNCHED_KEY = "ownerName";
		private static final String ENCOUNTERED_DATE_SYNCHED_KEY = "encounteredDate";
		// Behavior
		private LivingEntity previousTarget = null;
		private Vec3 anchor;
		private static final String AI_STATE_SYNCHED_KEY = "aiState";
		// Inventory
		private BefriendedInventory inventory;
		// Misc
		private boolean hasInit = false;
		// Syncher
		private Map<String, Tuple<NaUtilsDataSerializer<?>, Object>> synchedData = new HashMap<>();
		private int syncInterval = 1;
		// BefriendedUndeadMob data
		private MutablePredicate<IBefriendedSunSensitiveMob> sunImmunity = new MutablePredicate<>();
		// Temp
		private Map<String, Object> tempObjects = new HashMap<>();
		
		public Values(IBefriendedMob mob)
		{
			this.mob = mob;
			this.anchor = mob.asMob().position();
			this.nbt = new CompoundTag();
			this.inventory = this.getBM().createAdditionalInventory();
			this.createSynchedData(IDENTIFIER_SYNCHED_KEY, NaUtilsDataSerializer.UUID, EMPTY_UUID);
			this.createSynchedData(OWNER_UUID_SYNCHED_KEY, NaUtilsDataSerializer.UUID, EMPTY_UUID);
			this.createSynchedData(OWNER_NAME_SYNCHED_KEY, NaUtilsDataSerializer.STRING, "");
			this.createSynchedData(ENCOUNTERED_DATE_SYNCHED_KEY, NaUtilsDataSerializer.INT_ARRAY, new int[] {0, 0, 0});
			this.createSynchedData(AI_STATE_SYNCHED_KEY, NaUtilsDataSerializer.STRING, BefriendedAIState.WAIT.getId().toString());
			
			this.getBM().onDataInit(this);
			MinecraftForge.EVENT_BUS.post(new BefriendedMobDataConstructEvent(this));
			this.sync();
		}
	
		private Level getLevel()
		{
			return this.getBM().asMob().getLevel();
		}

		@SuppressWarnings({ "resource", "unused" })
		private boolean isClientSide()
		{
			return this.getLevel().isClientSide;
		}
		
		private boolean isEntityFirstTick()
		{
			return NaReflectionUtils.forceGet(this.getBM().asMob(), Entity.class, "f_19803_").cast();
		}
		
		@Override
		public CompoundTag serializeNBT() {
			CompoundTag save = new CompoundTag();
			// General
			save.putBoolean("25+", true);	// This is a label that the data format is 0.x.25+. Remove this after 0.x.30.
			save.put("additionalNBT", this.getAdditionalNBT());
			save.put("synchedData", this.saveSynchedData());
			//save.putUUID("identifier", this.getIdentifier());
			save.putString("initialEntityType", ForgeRegistries.ENTITY_TYPES.getKey(this.getInitialEntityType()).toString());
			save.putString("modId", ForgeRegistries.ENTITY_TYPES.getKey(this.getEntity().getType()).getNamespace());	// This field is not directly accessible, but only for reading mod id from entity saved data NBT.
			// Owner
			//save.putString("ownerName", this.getOwnerName());	// TODO: Remove after 0.x.30
			//save.putUUID("ownerUUID", this.getOwnerUUID());
			//if (this.getEncounteredDate() != null) save.putIntArray("encounteredDate", getEncounteredDate()); 
			//else save.putIntArray("encounteredDate", new int[] {0, 0, 0});	// TODO: remove after 0.x.30
			// Behavior
			NbtHelper.putVec3(save, "randomStrollAnchor", this.getAnchor());
			//save.putString("aiState", this.getAIState().getId().toString());
			// Inventory
			this.getAdditionalInventory().saveToTag(save, "additionalInventory");
			return save;
		}

		@Override
		public void deserializeNBT(CompoundTag nbt) {
			// 0.x.25+: normal reading
			if (nbt.getBoolean("25+"))
			{
				this.nbt = nbt.getCompound("additionalNBT").copy();
				this.readSynchedData(nbt.getCompound("synchedData"));
				//this.setIdentifier(nbt.getUUID("identifier"));
				this.setInitialEntityType(ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(nbt.getString("initialEntityType"))));
				//this.setOwnerName(nbt.getString("ownerName"));
				//this.setOwnerUUID(nbt.getUUID("ownerUUID"));
				//this.setEncounteredDate(nbt.getIntArray("encounteredDate"));
				this.setAnchor(NbtHelper.getVec3(nbt, "randomStrollAnchor"));
				//this.setAIState(BefriendedAIState.fromID(new ResourceLocation(nbt.getString("aiState"))));
				this.inventory.readFromTag(nbt.getCompound("additionalInventory"));
			}
			// Port legacy
			else 
			{
				CompoundTag nbtCpy = nbt.copy();
				nbtCpy.remove("mod_id");
				
				UUID identifier = nbtCpy.getUUID("identifier");
				if (identifier == null || identifier.equals(EMPTY_UUID)) this.generateIdentifier();
				else this.setIdentifier(identifier);
				nbtCpy.remove("identifier");
				
				String entityTypeKey = nbtCpy.getString("initial_entity_type");
				if (entityTypeKey != null && ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(entityTypeKey)) != null)
					this.setInitialEntityType(ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(entityTypeKey)));
				else this.recordEntityType();
				nbtCpy.remove("initial_entity_type");
				
				String ownerName = nbtCpy.getString("owner_name");
				if (ownerName != null) this.setOwnerName(ownerName);
				else this.setOwnerName("");
				nbtCpy.remove("owner_name");
				
				// owner uuid is set in BefriendedHelper
				int[] encounteredDate = nbtCpy.getIntArray("encountered_date");
				if (encounteredDate != null && encounteredDate.length >= 3) 
					this.setEncounteredDate(new int[] {encounteredDate[0], encounteredDate[1], encounteredDate[2]});
				else this.setEncounteredDate(new int[] {0, 0, 0});
				nbtCpy.remove("encountered_date");
				
				this.anchor = this.getEntity().position();
				this.nbt = nbtCpy;
				// AI state, owner uuid and inventory are loaded from BefriendedHelper
			}
			if (this.getEncounteredDate()[0] == 0)
				this.recordEncounteredDate();
			// TODO Remove from TempPortingEvent and restore this
			/*
			this.getBM().updateFromInventory();
			this.getBM().init(this.getOwnerUUID(), null);
			this.getBM().setInit();*/
		}

		private CompoundTag saveSynchedData()
		{
			CompoundTag nbt = new CompoundTag();
			for (var entry: this.synchedData.entrySet())
			{
				CompoundTag entryNBT = new CompoundTag();
				entryNBT.putString("serializer", entry.getValue().getA().getKey().toString());
				entryNBT.put("value", NaUtilsDataSerializer.toTagUnchecked(entry.getValue().getA(), entry.getValue().getB()));
				nbt.put(entry.getKey(), entryNBT);
			}
			return nbt;
		}
		
		private void readSynchedData(CompoundTag nbt)
		{
			for (String key: nbt.getAllKeys())
			{
				CompoundTag entryNBT = nbt.getCompound(key);
				NaUtilsDataSerializer<?> s = NaUtilsDataSerializer.fromId(new ResourceLocation(entryNBT.getString("serializer")));
				this.synchedData.put(key, new Tuple<>(s, s.fromTag(entryNBT.get("value"))));
			}
		}
		
		@Override
		public CompoundTag getAdditionalNBT() {
			return nbt;
		}

		@Override
		public IBefriendedMob getBM() {
			return mob;
		}

		@Override
		public Mob getEntity()
		{
			return getBM().asMob();
		}
		
		@Override
		public MutablePredicate<IBefriendedSunSensitiveMob> getSunImmunity() {
			if (mob instanceof IBefriendedSunSensitiveMob)
				return sunImmunity;
			else throw new UnsupportedOperationException("CBefriendedMobData only supports IBefriendedSunSensitiveMob. "
					+ "Attempted class: " + mob.getClass().toString());
		}

		@Override
		public Object getTempObject(String key) {
			return tempObjects.get(key);
		}

		@Override
		public void addTempObject(String key, Object obj) {
			tempObjects.put(key, obj);
		}

		@Override
		public void removeTempObject(String key) {
			tempObjects.remove(key);
		}
		
		@Override
		public UUID getIdentifier()
		{
			UUID id = this.getSynchedData(IDENTIFIER_SYNCHED_KEY, UUID.class);
			if (id != null && !id.equals(EMPTY_UUID))
				return id;
			else {
				LogUtils.getLogger().error(String.format("CBefriendedMobData: mob %s missing identifier. Regenerated.", this.getEntity().getName().getString()));
				this.generateIdentifier();
				return this.getSynchedData(IDENTIFIER_SYNCHED_KEY, UUID.class);
			}
		}

		@Override
		public void generateIdentifier()
		{
			UUID id = this.getSynchedData(IDENTIFIER_SYNCHED_KEY, UUID.class);
			if (id == null || id.equals(EMPTY_UUID))
			{
				if (!this.getEntity().level.isClientSide)
				{
					this.setSynchedData(IDENTIFIER_SYNCHED_KEY, UUID.class, UUID.randomUUID());
					this.sync();
				}
			}
			else throw new UnsupportedOperationException("CBefriendedMobData#generateIdentifier: Identifier is valid, not supported to regenerate.");
		}
		
		// Directly set, not safe
		private void setIdentifier(UUID val)
		{
			this.setSynchedData(IDENTIFIER_SYNCHED_KEY, UUID.class, val);
		}
		
		@Override
		public EntityType<? extends Mob> getInitialEntityType()
		{
			if (this.initialType != null) return this.initialType;
			else {
				LogUtils.getLogger().error(String.format("CBefriendedMobData: mob %s missing initial type. Reset to current type.", this.getEntity().getName().getString()));
				this.recordEntityType();
				return this.initialType;
			}
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public void recordEntityType()
		{
			this.initialType = (EntityType<? extends Mob>) this.getEntity().getType();
		}
		
		@SuppressWarnings("unchecked")
		private void setInitialEntityType(@Nonnull EntityType<?> entityType)
		{
			this.initialType = (EntityType<? extends Mob>) entityType;
		}
		
		@Override
		public String getOwnerName() {
			String str = this.getSynchedData(OWNER_NAME_SYNCHED_KEY, String.class);
			if (str != null && str != "") return str;
			else {
				LogUtils.getLogger().error(String.format("CBefriendedMobData: mob %s missing owner name. Return \"(Unknown)\". It will be updated once the owner entered the level", this.getEntity().getName().getString()));
				return "(Unknown)";
			}
		}

		@Override
		public void setOwnerName(String val) {
			
			this.setSynchedData(OWNER_NAME_SYNCHED_KEY, String.class, val);
		}		
		
		@Override
		public int[] getEncounteredDate() {
			return this.getSynchedData(ENCOUNTERED_DATE_SYNCHED_KEY, int[].class);
		}

		@Override
		public void setEncounteredDate(int[] val)
		{
			this.setSynchedData(ENCOUNTERED_DATE_SYNCHED_KEY, int[].class, val);
		}

		@Nonnull
		@Override
		public UUID getOwnerUUID()
		{
			UUID uuid = this.getSynchedData(OWNER_UUID_SYNCHED_KEY, UUID.class);
			if (uuid == null || uuid.equals(EMPTY_UUID)) 
				if (this.getLevel().isClientSide)
				{
					LogUtils.getLogger().error("CBefriendedMobData: mob %s missing owner on client. Not initialized?");
					return EMPTY_UUID;
				}
				//else throw new RuntimeException(String.format("CBefriendedMobData: mob %s missing owner.", this.getEntity().getName().getString()));
				else {
					// TODO Change it to exception after 0.x.30
					LogUtils.getLogger().error(String.format("CBefriendedMobData: mob %s missing owner.", this.getEntity().getName().getString()));
					return EMPTY_UUID;
				}
			return uuid;
		}
		
		@Override
		public void setOwnerUUID(@Nonnull UUID value)
		{
			if (value == null || value.equals(EMPTY_UUID))
				throw new IllegalArgumentException(String.format("CBefriendedMobData#setOwnerUUID: requires non-null and non-zero.", this.getEntity().getName().getString()));
			this.setSynchedData(OWNER_UUID_SYNCHED_KEY, UUID.class, value);
		}
		
		@Override
		public BefriendedAIState getAIState()
		{
			return BefriendedAIState.fromID(new ResourceLocation(this.getSynchedData(AI_STATE_SYNCHED_KEY, String.class)));
		}
		
		@Override
		public void setAIState(BefriendedAIState state)
		{
			this.setSynchedData(AI_STATE_SYNCHED_KEY, String.class, state.getId().toString());
		}

		@Override
		public Vec3 getAnchor() {
			return anchor;
		}

		@Override
		public void setAnchor(Vec3 anchor) {
			this.anchor = anchor;
		}

		@Override
		public boolean hasInit() {
			return this.hasInit;
		}

		@Override
		public void setInitState(boolean value) {
			this.hasInit = value;
		}

		@Override
		public LivingEntity getPreviousTarget() {
			return this.previousTarget;
		}

		@Override
		public void setPreviousTarget(LivingEntity target) {
			this.previousTarget = target;
		}
		
		@Override
		public <T> void createSynchedData(String key, NaUtilsDataSerializer<T> dataSerializer, T defaultValue)
		{
			if (this.getBM().asMob().getLevel().isClientSide)
				return;
			if (this.synchedData.containsKey(key))
				throw new IllegalArgumentException("CBefriendedMobData synched data: duplicated data key.");
			this.synchedData.put(key, new Tuple<>(dataSerializer, defaultValue));
		}

		@Override
		public <T> boolean hasSynchedData(String key, Class<T> dataType)
		{
			return this.synchedData.containsKey(key) && dataType.isAssignableFrom(this.synchedData.get(key).getClass());
		}
		
		@SuppressWarnings("resource")
		@Override
		public <T> void setSynchedData(String key, Class<T> dataType, T value)
		{
			if (this.getBM().asMob().getLevel().isClientSide)
				throw new IllegalStateException("CBefriendedMobData synched data: set data only on server. "
						+ "On client it's auto-synched. Use setSynchedDataClient to set only on client.");
			if (!this.synchedData.containsKey(key))
				throw new IllegalArgumentException(String.format("CBefriendedMobData synched data: missing field key \"%s\". "
						+ "Use createSynchedData to define fields first.", key));
			if (!this.synchedData.get(key).getA().getObjectClass().isAssignableFrom(value.getClass()))
				throw new IllegalArgumentException(String.format("CBefriendedMobData#setSynchedData: putting data type %s to key \"%s\"."
						+ "Requires type %s.", value.getClass().getSimpleName(), key, this.synchedData.get(key).getA().getObjectClass()));
			this.synchedData.get(key).setB(value);
		}

		@Override
		@SuppressWarnings("unchecked")
		public <T> T getSynchedData(String key, Class<T> dataClass)
		{
			if (!this.synchedData.containsKey(key))
			{
				LogUtils.getLogger().error(String.format("CBefriendedMobData synched data: Key \"%s\" not found.", key));
				return null;
			}
			if (!dataClass.isAssignableFrom(this.synchedData.get(key).getB().getClass()))
				throw new IllegalArgumentException("CBefriendedMobData synched data: data type and serializer don't match.");
			return (T) this.synchedData.get(key).getB();
		}
		
		@SuppressWarnings("resource")
		@Override
		public void setSynchedDataClient(String key, NaUtilsDataSerializer<?> serializer, Object value)
		{
			if (!this.getBM().asMob().getLevel().isClientSide)
				throw new IllegalStateException("CBefriendedMobData synched data: setSynchedDataClient only on client. On server use setSynchedData() instead.");
			if (!serializer.getObjectClass().isAssignableFrom(value.getClass()))
				throw new IllegalArgumentException("CBefriendedMobData synched data#setSynchedDataClient: data type and serializer don't match.");
			this.synchedData.put(key, new Tuple<>(serializer, value));
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T getSynchedDataUnchecked(String key) {
			if (!this.synchedData.containsKey(key))
				throw new IllegalArgumentException(String.format("CBefriendedMobData#getSynchedDataUnchecked: Field \"%s\" not defined.", key));
			return (T) this.synchedData.get(key).getB();
		}

		@Override
		public void setDataSyncInterval(int ticks)
		{
			if (ticks <= 0)
				throw new IllegalArgumentException();
			this.syncInterval = ticks;
		}
		
		@SuppressWarnings("resource")
		@Override
		public void tick()
		{
			if (!this.getLevel().isClientSide && this.getEntity().tickCount % this.syncInterval == 0 && !this.isEntityFirstTick())	// Don't update on the first tick to prevent some unexpected uninitialized problems
			{
				// Sync data
				if (!this.synchedData.isEmpty())
				{
					this.sync();
				}
				// TODO remove this. It's for porting legacy format.
				if (this.getOwnerName() == "" && this.getBM().isOwnerInDimension())
					this.setOwnerName(this.getBM().getOwnerInDimension().getName().getString());
				// Sync inventory. This is to prevent cases when inventory is not correctly synched to mob.
				this.getAdditionalInventory().syncToMob(this.getEntity());
			}
		}

		@Override
		public BefriendedInventory getAdditionalInventory() {
			return this.inventory;
		}
		
		private void sync()
		{
			ClientboundDataSyncPacket packet = new ClientboundDataSyncPacket(this);
			List<? extends Player> players = this.getLevel().players();
			for (Player player: players)
			{
				if (player instanceof ServerPlayer sp)
				{
					BMChannels.BM_CHANNEL.send(PacketDistributor.PLAYER.with(() -> sp), packet);
				}
			}
		}
	}
	
	public static class Prvd implements ICapabilitySerializable<CompoundTag>
	{
		
		public CBefriendedMobData values;
		
		public Prvd(IBefriendedMob mob)
		{
			values = new Values(mob);
		}
		
		@Override
		public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
			if (cap == BMCaps.CAP_BEFRIENDED_MOB_DATA)
				return LazyOptional.of(() -> {return this.values;}).cast();
			else return LazyOptional.empty();
		}

		@Override
		public CompoundTag serializeNBT() {
			return values.serializeNBT();
		}

		@Override
		public void deserializeNBT(CompoundTag nbt) {
			values.deserializeNBT(nbt);
		}
	}
	
	public static class ClientboundDataSyncPacket implements Packet<ClientGamePacketListener> 
	{
		public final CBefriendedMobData.Values dataCap;	// Only on server
		public final int entityId;
		public final Map<String, Tuple<NaUtilsDataSerializer<?>, Object>> objects; // Only on client
		
		public ClientboundDataSyncPacket(CBefriendedMobData.Values v)
		{
			this.dataCap = v;
			this.entityId = v.getBM().asMob().getId();
			this.objects = null;
		}
		
		public ClientboundDataSyncPacket(FriendlyByteBuf buf)
		{
			this.dataCap = null;
			this.objects = new HashMap<>();
			this.entityId = buf.readInt();
			int size = buf.readInt();
			for (int i = 0; i < size; ++i)
			{
				String key = buf.readUtf();
				NaUtilsDataSerializer<?> type = NaUtilsDataSerializer.fromId(new ResourceLocation(buf.readUtf()));
				Object obj = type.read(buf);
				objects.put(key, new Tuple<>(type, obj));
			}
		}
		
		@Override
		public void write(FriendlyByteBuf buf) 
		{
			buf.writeInt(entityId);
			buf.writeInt(dataCap.synchedData.size());
			for (var entry: dataCap.synchedData.entrySet())
			{
				buf.writeUtf(entry.getKey());
				buf.writeUtf(entry.getValue().getA().getKey().toString());
				NaUtilsDataSerializer.writeUnchecked(entry.getValue().getA(), buf, entry.getValue().getB());
			}
		}

		@Override
		public void handle(ClientGamePacketListener pHandler) {
			BMClientGamePacketHandler.handleBefriendedDataSync(this, pHandler);
		}
	}

	// Utils //
	
	/**
	 * Get this capability's save data from the whole entity save data.
	 * @param mobTag Entity nbt saved by {@link Entity#save}
	 * @return This capability's save data, saved by {@code serializeNBT}
	 */
	public static CompoundTag getCapFromMobTag(CompoundTag mobTag)
	{
		CompoundTag forgecaps = mobTag.getCompound("ForgeCaps");
		if (!forgecaps.getCompound("befriendmobs:cap_befriended_mob_data").isEmpty())
			return forgecaps.getCompound("befriendmobs:cap_befriended_mob_data");
		else return forgecaps.getCompound("befriendmobs:cap_befriended_mob_temp_data");	// TODO Legacy format: remove later
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getSynchedDataFromMobTag(CompoundTag mobTag, String key, Class<T> dataClass)
	{
		try {
			CompoundTag synched = getCapFromMobTag(mobTag).getCompound("synchedData");
			NaUtilsDataSerializer<?> serializer = NaUtilsDataSerializer.fromId(new ResourceLocation(synched.getCompound(key).getString("serializer")));
			Object res = serializer.fromTag(synched.getCompound(key).get("value"));
			return (T) res;
		} catch (RuntimeException e)
		{
			LogUtils.getLogger().error("Error in CBefriendedMobData#getSynchedDataFromMobTag. Return null. Cause:");
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Get the owner UUID stored in this cap's save data from the whole entity save data.
	 * @param mobTag Entity nbt saved by {@link Entity#save}
	 * @return Owner UUID saved in this cap
	 */
	public static UUID getOwnerUUIDFromMobTag(CompoundTag mobTag)
	{
		return getSynchedDataFromMobTag(mobTag, "ownerUUID", UUID.class);
	}
	
	/**
	 * Get the owner name stored in this cap's save data from the whole entity save data.
	 * @param mobTag Entity nbt saved by {@link Entity#save}
	 * @return Owner name saved in this cap
	 */
	public static String getOwnerNameFromMobTag(CompoundTag mobTag)
	{
		String res = getSynchedDataFromMobTag(mobTag, "ownerName", String.class);
		return res != null ? res : "(Unknown)";
	}
	
	/**
	 * Get the owner name stored in this cap's save data from the whole entity save data.
	 * @param mobTag Entity nbt saved by {@link Entity#save}
	 * @return Mod id saved in this cap
	 */
	public static String getModIdFromMobTag(CompoundTag mobTag)
	{
		CompoundTag capTag = getCapFromMobTag(mobTag);
		if (capTag == null) return null;
		return capTag.getString("modId");
	}
}
