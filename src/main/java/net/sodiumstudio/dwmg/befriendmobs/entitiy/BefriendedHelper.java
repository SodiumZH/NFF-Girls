package net.sodiumstudio.dwmg.befriendmobs.entitiy;

import java.util.UUID;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.ai.BefriendedAIState;
import net.sodiumstudio.dwmg.befriendmobs.inventory.AdditionalInventory;
import net.sodiumstudio.dwmg.befriendmobs.network.ClientboundBefriendedGuiOpenPacket;
import net.sodiumstudio.dwmg.befriendmobs.util.EntityHelper;

/**
 * A function library for setting up befriended mobs
 */

public class BefriendedHelper
{

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

	// This will read owner, AI state and additional inventory
	public static void addBefriendedCommonSaveData(IBefriendedMob mob, CompoundTag nbt) {
		try {
			if (mob.getOwnerUUID() != null)
				nbt.putUUID("owner", mob.getOwnerUUID());
			else
				throw new IllegalStateException(
						"Writing befriended mob data error: invalid owner. Was IBefriendedMob.init() not called?");
		}
		catch(IllegalStateException e)
		{
			e.printStackTrace();
		}
		nbt.putByte("ai_state", mob.getAIState().id());
		mob.getAdditionalInventory().saveToTag(nbt, "inventory_tag");
	}

	public static void readBefriendedCommonSaveData(IBefriendedMob mob, CompoundTag nbt) {
		UUID uuid = nbt.contains("owner") ? nbt.getUUID("owner") : null;
		try {
		if (uuid == null)
			throw new IllegalStateException(
					"Reading befriended mob data error: invalid owner. Was IBefriendedMob.init() not called?");
		}
		catch(IllegalStateException e)
		{
			e.printStackTrace();
			return;
		}
		mob.setOwnerUUID(uuid);
		mob.init(mob.getOwnerUUID(), null);
		mob.setAIState(BefriendedAIState.fromID(nbt.getByte("ai_state")));
		mob.getAdditionalInventory().readFromTag(nbt.getCompound("inventory_tag"));
	}

	public static IBefriendedMob convertToOtherBefriendedType(IBefriendedMob target, EntityType<? extends Mob> newType)
	{
		// Additional inventory will be invalidated upon convertion, so backup as a tag
		CompoundTag inventoryTag = target.getAdditionalInventory().toTag();
		// Do convertion
		Mob newMobRaw = EntityHelper.replaceMob(newType, target.asMob());
		if (!(newMobRaw instanceof IBefriendedMob))
			throw new UnsupportedOperationException("BefriendedHelper::convertToOtherBefriendedType supports mobs implementing IBefriendedMob.");
		IBefriendedMob newMob = (IBefriendedMob) newMobRaw;
		// Write the inventory back
		if(inventoryTag.getInt("size") != newMob.getAdditionalInventory().getContainerSize())
			throw new UnsupportedOperationException("BefriendedHelper::convertToOtherBefriendedType additional inventory must have same size before and after conversion.");
		newMob.getAdditionalInventory().readFromTag(inventoryTag);
		// Do other settings
		newMob.setAIState(target.getAIState());
		newMob.init(target.getOwnerUUID(), target.asMob());
		newMob.updateFromInventory();
		// setInit() needs to call manually
		return newMob;
	}
	
	/* Inventory */

	public static void openBefriendedInventory(Player player, IBefriendedMob target) {
		LivingEntity living = (LivingEntity) target;
		if (!player.level.isClientSide && player instanceof ServerPlayer sp
				&& (!living.isVehicle() || living.hasPassenger(player)))
		{
			if (player.containerMenu != player.inventoryMenu)
			{
				player.closeContainer();
			}

			sp.nextContainerCounter();
			ClientboundBefriendedGuiOpenPacket packet = new ClientboundBefriendedGuiOpenPacket(sp.containerCounter,
					target.getAdditionalInventory().getContainerSize(), living.getId());
			sp.connection.send(packet);
			sp.containerMenu = target.makeMenu(sp.containerCounter, sp.getInventory(), target.getAdditionalInventory());
			sp.initMenu(sp.containerMenu);
			MinecraftForge.EVENT_BUS.post(
					new net.minecraftforge.event.entity.player.PlayerContainerEvent.Open(player, player.containerMenu));
		}
	}

}
