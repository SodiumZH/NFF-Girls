package net.sodiumstudio.befriendmobs.entity.befriended;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.PacketDistributor;
import net.sodiumstudio.befriendmobs.entity.ai.BefriendedAIState;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventory;
import net.sodiumstudio.befriendmobs.network.BMChannels;
import net.sodiumstudio.befriendmobs.network.ClientboundBefriendedGuiOpenPacket;
import net.sodiumstudio.nautils.NaContainerUtils;
import net.sodiumstudio.nautils.NaNetworkUtils;
import net.sodiumstudio.nautils.EntityHelper;
import net.sodiumstudio.nautils.NbtHelper;

/**
 * A function library for befriended mobs
 */

public class BefriendedHelper
{

	/* AI */

	/**
	 *  Default settings of the rule about what the mob can attack.
	 *  <p>判断生物是否可以攻击对象的规则的默认预设。
	 */
	public static boolean wantsToAttackDefault(IBefriendedMob mob, LivingEntity target) {
		if (target instanceof Creeper && !mob.canAttackCreeper())
			return false;
		else if (target instanceof Ghast && !mob.canAttackGhast())
			return false;
		else if (isLivingAlliedToBM(mob, target))
			return false;
		else
			return true;
	}

	/* Save & Load */

	@Deprecated
	public static void addBefriendedCommonSaveData(IBefriendedMob mob, CompoundTag nbt, String modId) {		
		addBefriendedCommonSaveData(mob, nbt);
	}

	/**
	 * @deprecated No longer used, moved to data
	 */
	@Deprecated
	public static void addBefriendedCommonSaveData(IBefriendedMob mob, CompoundTag nbt)
	{
		/*nbt.put("bm_common", new CompoundTag());
		nbt.getCompound("bm_common").putString("mod_id", mob.getModId());
		if (mob.getOwnerUUID() != null)
			nbt.getCompound("bm_common").putUUID("owner", mob.getOwnerUUID());
		nbt.getCompound("bm_common").putString("ai_state", mob.getAIState().getId().toString());
		mob.getAdditionalInventory().saveToTag(nbt.getCompound("bm_common"), "inventory");*/
		
		/*String modId = mob.getModId();
		String ownerKey = modId + ":befriended_owner";
		String aiStateKey = modId + ":befriended_ai_state";
		String inventoryKey = modId + ":befriended_additional_inventory";
		// Mod ID
		nbt.putString("befriended_mod_id", modId);
		// Owner UUID
		if (mob.getOwnerUUID() != null)
			nbt.putUUID(ownerKey, mob.getOwnerUUID());
		else
			nbt.putUUID(ownerKey, new UUID(0, 0));
		nbt.putInt(aiStateKey, mob.getAIState().id);
		mob.getAdditionalInventory().saveToTag(nbt, inventoryKey);*/
	}
	
	@Deprecated	// Use version without modid input
	public static void readBefriendedCommonSaveData(IBefriendedMob mob, CompoundTag nbt, String inModId)
	{
		readBefriendedCommonSaveData(mob, nbt);
	}
	
	/**
	 * Read mob's Mod Id, owner, AI state and additional inventory information.
	 * <p>读取生物所属的Mod ID、拥有者信息、AI状态及附加道具栏
	 * @since 0.x.25
	 * TODO Remove in 0.x.30. Before 30 it's left to port legacy.
	 */
	public static void readBefriendedCommonSaveData(IBefriendedMob mob, CompoundTag nbt) {
		
		if (nbt.contains("bm_common", NbtHelper.TAG_COMPOUND_ID))
		{
			if (nbt.getCompound("bm_common").getUUID("owner") == null)
			{
				new IllegalStateException("Reading befriended mob data error: invalid owner. Was IBefriendedMob.init() not called?").printStackTrace();
				return;
			}
			mob.setOwnerUUID(nbt.getCompound("bm_common").getUUID("owner"));
			mob.init(mob.getOwnerUUID(), null);
			if (nbt.getCompound("bm_common").contains("ai_state", Tag.TAG_STRING))
				mob.setAIState(BefriendedAIState.fromID(new ResourceLocation(nbt.getCompound("bm_common").getString("ai_state"))), false);
			else mob.setAIState(BefriendedAIState.WAIT, false);
			mob.getAdditionalInventory().readFromTag(nbt.getCompound("bm_common").getCompound("inventory"));
		}
	}

	/**
	 * Convert a befriended mob to other type. This action will keep its data.
	 * @param target The mob to convert.
	 * @param newType The type converting to, must implementing {@code IBefriendedMob} interface.
	 * @return The new mob reference.
	 * <p>========
	 * <p>将一个友好化生物转化为其他类型。这个操作会保持其数据。
	 * @param target 转化前的生物。
	 * @param newType 转化为的类型。其必须实现{@code IBefriendedMob}接口。
	 * @return The new mob reference. 新生物的引用。
	 */
	public static IBefriendedMob convertToOtherBefriendedType(IBefriendedMob target, EntityType<? extends Mob> newType)
	{
		// Additional inventory will be invalidated upon convertion, so backup as a tag
		CompoundTag mobTag = new CompoundTag();
		target.asMob().saveWithoutId(mobTag);
		// Do convertion
		
		Mob newMob = EntityHelper.replaceMob(newType, target.asMob());
		if (!(newMob instanceof IBefriendedMob))
			throw new UnsupportedOperationException("BefriendedHelper::convertToOtherBefriendedType supports mobs implementing IBefriendedMob.");
		newMob.load(mobTag);
		// Write the inventory back
		/*if(inventoryTag.getInt("size") != newMob.getAdditionalInventory().getContainerSize())
			throw new UnsupportedOperationException("BefriendedHelper::convertToOtherBefriendedType additional inventory must have same size before and after conversion.");
		newMob.getAdditionalInventory().readFromTag(inventoryTag);
		// Do other settings
		newMob.setAIState(target.getAIState(), false);
		newMob.init(target.getOwnerUUID(), target.asMob());
		newMob.updateFromInventory();*/
		// setInit() needs to call manually
		
		return (IBefriendedMob)newMob;
	}
	
	/* Inventory */

	/**
	 * Open the inventory GUI of the mob.
	 * <p>Warning: DO NOT call this if {@link IBefriendedMob#makeMenu()} method returns null, otherwise it will crash the game.
	 * <p>打开生物的道具栏GUI。
	 * <p>警告：如果{@link IBefriendedMob#makeMenu()}函数返回null则不要调用这个函数，否则游戏会崩溃。
	 */
	public static void openBefriendedInventory(Player player, IBefriendedMob mob) {
		LivingEntity living = (LivingEntity) mob;
		if (!player.level.isClientSide && player instanceof ServerPlayer sp
				&& (!living.isVehicle() || living.hasPassenger(player)))
		{
			
			if (player.containerMenu != player.inventoryMenu)
			{
				player.closeContainer();
			}

			sp.nextContainerCounter();
			ClientboundBefriendedGuiOpenPacket packet = new ClientboundBefriendedGuiOpenPacket(sp.containerCounter,
					mob.getAdditionalInventory().getContainerSize(), living.getId());
			NaNetworkUtils.sendToPlayer(BMChannels.BM_CHANNEL, packet, sp);
			sp.containerMenu = mob.makeMenu(sp.containerCounter, sp.getInventory(), mob.getAdditionalInventory());
			if (sp.containerMenu == null)
				return;
			sp.initMenu(sp.containerMenu);
			MinecraftForge.EVENT_BUS.post(
					new net.minecraftforge.event.entity.player.PlayerContainerEvent.Open(player, player.containerMenu));
		}
	}

	/**
	 * Get the Mod Id which the mob belongs to, with an nbt for deserialization before the mob spawns
	 * <p>使用一个用于读档的NBT标签，在未实际生成生物前获取生物所属的MOD ID
	 * @deprecated Use {@link CBefriendedMobData#getModIdFromMobTag} instead
	 */
	@Deprecated
	public static String getModIdFromNbt(CompoundTag nbt)
	{
		/*// 0.x.15+ solution
		if (nbt.contains("bm_common", Tag.TAG_COMPOUND))
			return nbt.getCompound("bm_common").getString("mod_id");
		
		// LEGACY
		else return nbt.contains("befriended_mod_id", Tag.TAG_COMPOUND) ?
				nbt.getString("befriended_mod_id") : null;*/
		return CBefriendedMobData.getModIdFromMobTag(nbt);
	}
	
	/**
	 * @deprecated Use {@link CBefriendedMobData#getOwnerUUIDFromMobTag} instead
	 */
	@Deprecated
	public static UUID getOwnerUUIDFromNbt(CompoundTag nbt)
	{
		// 0.x.15+ solution
		/*
		if (nbt.contains("bm_common", NbtHelper.TAG_COMPOUND_ID))
			return nbt.getCompound("bm_common").getUUID("owner");
		
		// LEGACY
		else
		{
			String modid = getModIdFromNbt(nbt);
			if (modid == null)
				return null;
			return nbt.contains(modid + ":befriended_owner", NbtHelper.TAG_INT_ARRAY_ID) ? nbt.getUUID(modid + ":befriended_owner") : null;
		}*/
		return CBefriendedMobData.getOwnerUUIDFromMobTag(nbt);
		
	}
	
	/**
	 * @deprecated Use {@link EntityHelper#getNameFromNbt} instead
	 */
	@Deprecated
	public static Component getNameFromNbt(CompoundTag nbt, EntityType<?> type)
	{
		return EntityHelper.getNameFromNbt(nbt, type);
	}
	
	/**
	 * Get owner if the owner is closer than the given distance of the mob. Otherwise return {@link Optional#empty}.
	 * @param mob Mob (implements {@link IBefriendedMob}) to test. No need to do {@IBefriendedMob#isOwnerPresent} check.
	 * @param radius Search area
	 * @param sphericalArea If true, it will search in a sphere with given radius. Otherwise search in a box with given radius.
	 * @return Owner if the owner is present and in the given area. Otherwise {@link Optional#empty}.
	 */
	public static Optional<Player> getOwnerInArea(IBefriendedMob mob, double radius, boolean sphericalArea)
	{
		if (!mob.isOwnerInDimension())
			return Optional.empty();
		List<Entity> list = mob.asMob().level.getEntities(mob.asMob(), EntityHelper.getNeighboringArea(mob.asMob(), radius), e -> e == mob.getOwner());
		if (list.isEmpty())
			return Optional.empty();
		else if (!sphericalArea)
			return Optional.of((Player)(list.get(0)));
		else if (mob.asMob().distanceToSqr(list.get(0)) <= radius * radius)
			return Optional.of((Player)(list.get(0)));
		else return Optional.empty();
	}

	public static ArrayList<Mob> getOwningMobsInArea(Player player, EntityType<? extends Mob> type, double radius, boolean sphericalArea)
	{
		Stream<Entity> stream = player.level.getEntities(player, EntityHelper.getNeighboringArea(player, radius),
				e -> (e.getType() == type && e instanceof IBefriendedMob bm && bm.getOwner() == player)).stream();
		if (sphericalArea)
			stream = stream.filter(e -> e.distanceToSqr(player) <= radius * radius);
		return NaContainerUtils.castListTypeUnchecked(stream.toList());
	}
	
	/**
	 * Check if a living entity ({@code test}) should be considered as ally by an {@code OwnableEntity} ({@code entity}) under BMF rule.
	 * <p>This method is private because it doesn't involve BM, so directly calling this may cause unexpected
	 * behavior changes on vanilla mobs. Call {@code isLivingAlliedToBM} and {@code isBMAlliedToOwnable} instead.
	 * <p>On server only. On client always {@code false}.
	 */
	private static boolean isLivingAlliedToOwnable(OwnableEntity entity, LivingEntity test)
	{
		if (test == null) return false;
		if (entity instanceof LivingEntity living && entity.getOwnerUUID() != null && entity.getOwner() != null && entity.getOwner() instanceof Player)
		{
			if (living.level.isClientSide)
				return false;
			if (living == test)
				return true;
			boolean allowPvp = living.level.getServer().isPvpAllowed();
			// If don't allow pvp, it don't attack any players or owned entities
			// If allow pvp, it just don't attack its owner and owner's other entities
			// Horse is a special case since it's virtually ownable but doesn't have OwnableEntity interface
			if (test instanceof AbstractHorse horse)
			{
				return horse.getOwnerUUID() != null && (allowPvp ? horse.getOwnerUUID().equals(entity.getOwnerUUID()) : true);
			}
			if (!allowPvp)
			{
				return test instanceof Player || test instanceof OwnableEntity ownable && ownable.getOwnerUUID() != null;
			}

			else
			{
				UUID ownerUUID = entity.getOwnerUUID();
				if (test.getUUID().equals(ownerUUID))
					return true;
				else if (test instanceof OwnableEntity ownable && ownable.getOwnerUUID() != null && ownable.getOwnerUUID().equals(ownerUUID))
					return true;
				else return false;
			}
		}
		return false;
	}
	
	/**
	 * Check if a BM is considered as ally by an {@code OwnableEntity}.
	 * <p>On server only. On client always {@code false}.
	 */
	public static boolean isBMAlliedToOwnable(OwnableEntity entity, IBefriendedMob test)
	{
		return isLivingAlliedToOwnable(entity, test.asMob());
	}
	
	/**
	 * Check if a {@code LivingEntity} is considered as ally by a BM.
	 * <p>On server only. On client always {@code false}.
	 */
	public static boolean isLivingAlliedToBM(IBefriendedMob bm, LivingEntity test)
	{
		return isLivingAlliedToOwnable(bm, test);
	}
}
