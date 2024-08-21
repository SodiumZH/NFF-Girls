package net.sodiumstudio.befriendmobs.item;

import java.util.UUID;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.nautils.item.NaUtilsItem;

public class MobOwnershipTransfererItem extends NaUtilsItem
{

	public MobOwnershipTransfererItem(Properties pProperties)
	{
		super(pProperties.stacksTo(1));
	}
	
	public boolean isWritten(ItemStack stack)
	{
		if (stack == null || stack.isEmpty() || !(stack.getItem() instanceof MobOwnershipTransfererItem))
			throw new IllegalArgumentException();
		return stack.getOrCreateTag().contains("mot_written");
	}
	
	public String getMobName(ItemStack stack)
	{
		if (!isWritten(stack))
			return null;
		else return stack.getTag().getString("mot_mob_name");
	}
	
	public UUID getMobUUID(ItemStack stack)
	{
		if (!isWritten(stack))
			return null;
		else return stack.getTag().getUUID("mot_mob_uuid");
	}
	
	public String getOldOwnerName(ItemStack stack)
	{
		if (!isWritten(stack))
			return null;
		else return stack.getTag().getString("mot_owner_name");
	}
	
	public UUID getOldOwnerUUID(ItemStack stack)
	{
		if (!isWritten(stack))
			return null;
		else return stack.getTag().getUUID("mot_owner_uuid");
	}
	
	public boolean isLocked(ItemStack stack)
	{
		if (!isWritten(stack))
			throw new IllegalStateException("MobOwnershipTransfererItem#isLocked: the item isn't written.");
		else return stack.getTag().getBoolean("mot_locked");
	}
	
	public ItemStack fromMob(IBefriendedMob mob, MobOwnershipTransfererItem itemType)
	{
		ItemStack stack = new ItemStack(itemType);
		if (stack == null || stack.isEmpty() || !(stack.getItem() instanceof MobOwnershipTransfererItem))
			throw new IllegalArgumentException();
		if (!mob.isOwnerPresent())
			throw new IllegalStateException("MobOwnershipTransfererItem writing requires the owner to be present in the level.");
		stack.getOrCreateTag().putBoolean("mot_written", true);	// "mot" means "mob ownership transferer"
		stack.getTag().putUUID("mot_mob_uuid", mob.asMob().getUUID());
		stack.getTag().putString("mot_mob_name", mob.asMob().getName().getString());
		stack.getTag().putUUID("mot_owner_uuid", mob.getOwnerUUID());
		stack.getTag().putString("mot_owner_name", mob.getOwner().getName().getString());
		stack.getTag().putBoolean("mot_locked", true);
		return stack;
	}
	
	/**
	 * Apply a written transferer to mob to transfer its owner
	 */
	protected InteractionResult tryTransfer(ItemStack stack, Player player, IBefriendedMob mob)
	{
		// Wrong stack type
		if (stack == null || stack.isEmpty() || !(stack.getItem() instanceof MobOwnershipTransfererItem))
			throw new IllegalArgumentException();
		// Unwritten (shouldn't happen)
		if (!isWritten(stack))
			return InteractionResult.PASS;
		// Applying on another mob
		if (!stack.getOrCreateTag().getUUID("mot_mob_uuid").equals(mob.asMob().getUUID()))
			return InteractionResult.PASS;
		// Player is the old owner
		if (stack.getOrCreateTag().getUUID("mot_owner_uuid").equals(player.getUUID()))
			return InteractionResult.PASS;
		// Locked
		if (stack.getOrCreateTag().getBoolean("mot_locked"))
			return InteractionResult.PASS;
		mob.setOwnerUUID(player.getUUID());
		mob.getData().setOwnerName(player.getName().getString());
		mob.getData().recordEncounteredDate();
		return InteractionResult.sidedSuccess(player.level.isClientSide);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
	{
		// For written transferer
		if (isWritten(player.getItemInHand(usedHand)))
		{
			if (this.getOldOwnerUUID(player.getItemInHand(usedHand)).equals(player.getUUID()))			
			{
				// The old owner can lock/unlock the transferer
				player.getItemInHand(usedHand).getOrCreateTag().putBoolean("mot_locked", !this.isLocked(player.getItemInHand(usedHand)));
				return InteractionResultHolder.success(player.getItemInHand(usedHand));
			}
			// The new owner can only apply the item on the corresponding mob
		}
		return InteractionResultHolder.pass(player.getItemInHand(usedHand));
	}
	
	@Override
	public InteractionResult interactLivingEntity(Player player, LivingEntity living, InteractionHand usedHand)
	{
		ItemStack stack = player.getItemInHand(usedHand);
		if (!player.level.isClientSide && living instanceof IBefriendedMob bm)
		{
			if (isWritten(stack))
			{
				if (tryTransfer(stack, player, bm) == InteractionResult.PASS)
					return InteractionResult.PASS;
				else
				{
					stack.shrink(1);
					return InteractionResult.sidedSuccess(player.level.isClientSide);
				}
			}
			else if (bm.isOwnerPresent() && bm.getOwnerUUID().equals(player.getUUID()))
			{
				stack.shrink(1);
				player.addItem(this.fromMob(bm, this));
			}
		}
		return InteractionResult.PASS;
	}

}
