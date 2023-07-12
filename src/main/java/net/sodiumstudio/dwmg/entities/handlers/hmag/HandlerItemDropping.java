package net.sodiumstudio.dwmg.entities.handlers.hmag;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.function.Supplier;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.befriendmobs.entity.befriending.BefriendableAddHatredReason;
import net.sodiumstudio.befriendmobs.entity.befriending.BefriendableMobInteractArguments;
import net.sodiumstudio.befriendmobs.entity.befriending.BefriendableMobInteractionResult;
import net.sodiumstudio.befriendmobs.entity.befriending.BefriendingHandler;
import net.sodiumstudio.befriendmobs.entity.capability.CBefriendableMob;
import net.sodiumstudio.nautils.EntityHelper;
import net.sodiumstudio.nautils.ItemHelper;
import net.sodiumstudio.nautils.NbtHelper;
import net.sodiumstudio.nautils.debug.Debug;
import net.sodiumstudio.dwmg.events.DwmgItemEvents;

public abstract class HandlerItemDropping extends BefriendingHandler
{
	
	@Override
	public void initCap(CBefriendableMob cap)
	{
		cap.getNbt().put("ongoing_players", new CompoundTag());
	}
	
	@Override
	public BefriendableMobInteractionResult handleInteract(BefriendableMobInteractArguments args) {
		return new BefriendableMobInteractionResult();
	}

	@Override
	public void interrupt(Player player, Mob mob, boolean isQuiet) 
	{
		CBefriendableMob.getCapNbt(mob).getCompound("ongoing_players").remove(player.getStringUUID());
		mob.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
		Debug.printToScreen("Interrupted", player);
		if (!isQuiet)
			EntityHelper.sendAngryParticlesToLivingDefault(mob);
	}

	/**
	 * Player progress is stored at: nbt/ongoing_players/(uuid)
	 */
	@Override
	public boolean isInProcess(Player player, Mob mob) {
		return CBefriendableMob.getCapNbt(mob).getCompound("ongoing_players").contains(player.getStringUUID(), NbtHelper.TAG_DOUBLE_ID)
				&& CBefriendableMob.getCapNbt(mob).getCompound("ongoing_players").getDouble(player.getStringUUID()) > 0;
	}

	@Override
	public HashSet<BefriendableAddHatredReason> getAddHatredReasons() {
		HashSet<BefriendableAddHatredReason> reasons = new HashSet<BefriendableAddHatredReason>();
		reasons.add(BefriendableAddHatredReason.ATTACKED);
		return reasons;
	}
	
	/**
	 * Map of accepted item registry ID to the progress function
	 */
	@SuppressWarnings("unchecked")
	public abstract Map<String, Supplier<Double>> getDeltaProc();

	public final Map<Item, Supplier<Double>> getItemDeltaProc()
	{
		Map<String, Supplier<Double>> procMap = getDeltaProc();
		Map<Item, Supplier<Double>> out = new HashMap<Item, Supplier<Double>>();
		for (String str: procMap.keySet())
		{
			if (ItemHelper.getItem(str) != null)
				out.put(ItemHelper.getItem(str), procMap.get(str));
		}
		return out;
	}
	
	
	/**
	 * Check if a mob can pick up the item
	 */
	public boolean canPickUpItem(Mob mob, ItemEntity itemEntity)
	{
		// If item type not matching, pass
		if (!getDeltaProc().keySet().contains(ItemHelper.getRegistryKeyStr(itemEntity.getItem())))
			return false;
		// If item not thrown by player, pass
		if (itemEntity.getThrower() == null || mob.level.getPlayerByUUID(itemEntity.getThrower()) == null)
			return false;
		// If in hatred, pass
		if (CBefriendableMob.getCap(mob).getHatred().contains(itemEntity.getThrower()))
			return false;
		// If the item is still in picking cooldown for the mob, pass
		if (itemEntity.getItem().getOrCreateTagElement("already_picked_befriendable_mobs").contains(mob.getStringUUID(), NbtHelper.TAG_INT_ID)
			&& itemEntity.getItem().getOrCreateTagElement("already_picked_befriendable_mobs").getInt(mob.getStringUUID()) > 0)
			return false;
		// Overlap check is in serverTick()
		/*if (!mob.getBoundingBox().intersects(itemEntity.getBoundingBox()))
			return false;*/
		// Holding another item, pass
		if (!mob.getItemInHand(InteractionHand.OFF_HAND).isEmpty() || CBefriendableMob.getCap(mob).hasTimer("hold_item_time"))
			return false;
		// In picking up cooldown, pass
		if (CBefriendableMob.getCap(mob).hasTimer("picking_cooldown"))
			return false;
		return true;
	}
		
	/**
	 * Mob trying picking up the item stack on befriending
	 * @return Whether successfully picked up
	 */
	public boolean pickUpItem(Mob mob, ItemEntity itemEntity)
	{
		if (canPickUpItem(mob, itemEntity))
		{
			// Drop the holding item first
			if (!mob.getItemInHand(InteractionHand.OFF_HAND).isEmpty())
			{
				mob.spawnAtLocation(mob.getItemInHand(InteractionHand.OFF_HAND));
				mob.setItemInHand(InteractionHand.OFF_HAND, null);
			}
		
			// Pick one and label picked
			ItemStack stack = itemEntity.getItem().copy();
			stack.setCount(1);
			stack.getOrCreateTag().putUUID("befriendable_picked_from_player", itemEntity.getThrower());
			mob.setItemInHand(InteractionHand.OFF_HAND, stack);
			// If only one, remove item entity
			if (itemEntity.getItem().getCount() <= 1)
			{
				itemEntity.discard();
			}
			// Otherwise take one and label taken
			else
			{
				itemEntity.getItem().shrink(1);
				/** Label on the item entity that the mob has picked
				* This is a timer and handled in {@link DwmgItemEvents#onServerItemEntityTick}   
				*/                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           
				itemEntity.getItem().getOrCreateTag().getCompound("already_picked_befriendable_mobs").putInt(mob.getStringUUID(), getItemPickingCooldown());
			}
			// Add timer on mob during which it should hold the item on off-hand
			CBefriendableMob.getCap(mob).setTimer("hold_item_time", getHoldingItemTime());
			return true;
		}
		return false;
	}
	
	/** Get how long the mob should hold the item on its off-hand and be unable to pick another one */
	public abstract int getHoldingItemTime();
	
	/** Get how long before an item stack can be picked up again by the same mob */
	public int getItemPickingCooldown()
	{
		return 300 * 20;	// 5 min by default
	}
	
	/** Get how long before the mob can pick up another item after consuming an item */
	public int getMobPickingCooldown()
	{
		return 15 * 20; 
	}
	
	@Override
	public void serverTick(Mob mob)
	{
		// Remove off-hand item when timer up, and update proc
		if (!mob.getItemInHand(InteractionHand.OFF_HAND).isEmpty()
				&& mob.getItemInHand(InteractionHand.OFF_HAND).getTag() != null
				&& mob.getItemInHand(InteractionHand.OFF_HAND).getTag().contains("befriendable_picked_from_player", NbtHelper.TAG_INT_ARRAY_ID)
				&& !CBefriendableMob.getCap(mob).hasTimer("hold_item_time"))
		{
			Player player = mob.level.getPlayerByUUID(mob.getItemInHand(InteractionHand.OFF_HAND).getTag().getUUID("befriendable_picked_from_player"));
			if (player != null && mob.hasLineOfSight(player))
			{
				String strUUID = player.getStringUUID();
				double oldProc = CBefriendableMob.getCapNbt(mob).getCompound("ongoing_players").contains(strUUID, NbtHelper.TAG_DOUBLE_ID) ?
						CBefriendableMob.getCapNbt(mob).getCompound("ongoing_players").getDouble(strUUID) : 0d;
				double newProc = oldProc + getItemDeltaProc().get(mob.getItemInHand(InteractionHand.OFF_HAND).getItem()).get();
				EntityHelper.sendGlintParticlesToLivingDefault(mob);
				if (newProc >= 1.0d)
				{
					befriend(player, mob);
					return;
				}
				else
				{
					int heartCount = ((int) (newProc / 0.2d) - (int) (oldProc / 0.2d));
					EntityHelper.sendParticlesToEntity(mob, ParticleTypes.HEART, mob.getBbHeight() - 0.5, 0.2d, heartCount, 1d);
					CBefriendableMob.getCapNbt(mob).getCompound("ongoing_players").putDouble(strUUID, newProc);
					CBefriendableMob.getCap(mob).setTimer("picking_cooldown", getMobPickingCooldown());
				}
				Debug.printToScreen("Proc: " + Double.toString(newProc), player);
			}
			else
			{
				mob.getItemInHand(InteractionHand.OFF_HAND).removeTagKey("befriendable_picked_from_player");
				EntityHelper.sendSmokeParticlesToLivingDefault(mob);
				mob.spawnAtLocation(mob.getItemInHand(InteractionHand.OFF_HAND));
			}
			mob.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
		}
		// Check pick-up
		else if (!CBefriendableMob.getCap(mob).hasTimer("hold_item_time"))
		{
			// Overlapping or on neighboring block position
			Predicate<ItemEntity> pickCondition = (ItemEntity ie) -> ie.getBoundingBox().intersects(mob.getBoundingBox()) 
					|| (Math.abs(ie.getBlockX() - mob.getBlockX()) <= 1 && Math.abs(ie.getBlockZ() - mob.getBlockZ()) <= 1 && ie.getBlockY() == mob.getBlockY());
			List<ItemEntity> overlappingItems = 
					mob.level.getEntitiesOfClass(ItemEntity.class, mob.getBoundingBox().minmax(EntityHelper.getNeighboringArea(mob, 2.0d)))
						.stream().filter(pickCondition)
						.toList();
			if (overlappingItems.size() > 0)
			{
				ItemEntity toPick = null;
				for (ItemEntity ie: overlappingItems)
				{
					if (canPickUpItem(mob, ie))
					{
						toPick = ie;
						break;
					}
				}
				if (toPick != null)
				{
					pickUpItem(mob, toPick);
				}
			}
		}
		CBefriendableMob.getCap(mob).setForcePersistent(!CBefriendableMob.getCapNbt(mob).getCompound("ongoing_players").isEmpty());
	}
 }
