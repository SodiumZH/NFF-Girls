package com.sodium.dwmg.entities;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import com.sodium.dwmg.entities.ai.BefriendedAIState;
import com.sodium.dwmg.network.ClientboundBefriendedGuiOpenPacket;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;

/**
 * A function library for setting up befriended mobs
 */

public class BefriendedHelper {

	/* AI */

	public static boolean wantsToAttackDefault(IBefriendedMob mob, LivingEntity target) {
		// Don't attack creeper or ghast
		if ((target instanceof Creeper) || (target instanceof Ghast))
			return false;
		// For tamable mobs: attack untamed or (others' mobs if pvp allowed)
		else if (target instanceof TamableAnimal tamable)
			return !tamable.isTame() || (tamable.getOwner() != mob.getOwner()
					&& (mob.getOwner()).canHarmPlayer((Player) (tamable.getOwner())));
		// For players: attack if pvp allowed
		else if (target instanceof Player targetPlayer)
			return mob.getOwner().canHarmPlayer(targetPlayer);
		// For IBefriendedMob: similar to tamable mobs
		else if (target instanceof IBefriendedMob bef)
			return bef.getOwner() != mob.getOwner() && (mob.getOwner()).canHarmPlayer(bef.getOwner());
		// For horses: attack untamed only
		else if (target instanceof AbstractHorse && ((AbstractHorse) target).isTamed())
			return false;
		// Can attack other
		else
			return true;
	}

	/* Save & Load */

	public static void addBefriendedCommonSaveData(IBefriendedMob mob, CompoundTag nbt) {
		if (mob.getOwnerUUID() != null)
			nbt.putUUID("owner", mob.getOwnerUUID());
		else
			throw new IllegalStateException(
					"Writing befriended mob data error: invalid owner. Was IBefriendedMob.init() not called?");
		nbt.putByte("ai_state", mob.getAIState().id());
	}

	public static void readBefriendedCommonSaveData(IBefriendedMob mob, CompoundTag nbt) {
		UUID uuid = nbt.getUUID("owner");
		if (uuid == null)
			throw new IllegalStateException(
					"Reading befriended mob data error: invalid owner. Was IBefriendedMob.init() not called?");
		mob.setOwnerUUID(uuid);
		mob.init(mob.getOwnerUUID(), null);
		mob.setAIState(BefriendedAIState.fromID(nbt.getByte("ai_state")));
	}

	/* Inventory */

	public static void openBefriendedInventory(Player player, IBefriendedMob target) {
		LivingEntity living = (LivingEntity) target;
		if (!player.level.isClientSide && player instanceof ServerPlayer sp
				&& (!living.isVehicle() || living.hasPassenger(player))) {
			if (player.containerMenu != player.inventoryMenu) {
				player.closeContainer();
			}

			sp.nextContainerCounter();
			ClientboundBefriendedGuiOpenPacket packet = new ClientboundBefriendedGuiOpenPacket(sp.containerCounter,
					target.getInventory().getContainerSize(), living.getId());
			sp.connection.send(packet);
			sp.containerMenu = target.makeMenu(sp.containerCounter, sp.getInventory(), target.getInventory());
			sp.initMenu(sp.containerMenu);
			MinecraftForge.EVENT_BUS.post(
					new net.minecraftforge.event.entity.player.PlayerContainerEvent.Open(player, player.containerMenu));
		}
	}

}
