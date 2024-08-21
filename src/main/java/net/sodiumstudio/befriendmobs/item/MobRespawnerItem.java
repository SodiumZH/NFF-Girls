package net.sodiumstudio.befriendmobs.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.registry.BMItems;
import net.sodiumstudio.nautils.EntityHelper;
import net.sodiumstudio.nautils.item.NaUtilsItem;

public class MobRespawnerItem extends NaUtilsItem
{

	protected boolean retainBefriendedMobInventory = true;
	
	public MobRespawnerItem(Properties pProperties)
	{
		super(pProperties.stacksTo(1));
	}

	public MobRespawnerItem setRetainBefriendedMobInventory(boolean value)
	{
		this.retainBefriendedMobInventory = value;
		return this;
	}
	
	public static ItemStack fromMob(MobRespawnerItem itemType, Mob mob) {
		if (mob.level.isClientSide)
			return ItemStack.EMPTY;
		MobRespawnerInstance ins = MobRespawnerInstance.create(new ItemStack(itemType, 1));
		ins.initFromMob(mob);

		// stack.setHoverName(InfoHelper.createTrans(stack.getHoverName().getString() +
		// " - " + mob.getName().getString()));
		// Check NBT correctly added
		if (ins.getCapTag().isEmpty())
			throw new IllegalStateException("Respawner missing NBT");

		return ins.get();
	}
/*
	public static ItemStack fromMob(Mob mob) {
		return fromMob((MobRespawnerItem) BMItems.MOB_RESPAWNER.get(), mob);
	}
*/
	public static Mob doRespawn(ItemStack stack, Player player, BlockPos pos, Direction direction) {
		MobRespawnerInstance ins = MobRespawnerInstance.create(stack);
		// Check NBT correctly added
		if (ins.getCapTag().isEmpty())
			throw new IllegalStateException("Respawner missing NBT");
		return ins.respawn(player, pos, direction);
	}

	@SuppressWarnings("resource")
	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		if (!(level instanceof ServerLevel))
		{
			return InteractionResult.SUCCESS;
		} else
		{
			Mob mob = doRespawn(context.getItemInHand(), context.getPlayer(), context.getClickedPos(),
					context.getClickedFace());
			if (mob != null)
			{

				context.getItemInHand().shrink(1);
				if (mob instanceof IBefriendedMob bef)
				{
					bef.init(bef.getOwnerUUID(), null);
					if (!retainBefriendedMobInventory)
					{
						if (!bef.getAdditionalInventory().isEmpty())
							bef.getAdditionalInventory().clearContent();
						bef.updateFromInventory();
						EntityHelper.removeAllEquipment(bef.asMob());
					}
					bef.setInit();
				}
				return InteractionResult.CONSUME;
			} else
				return InteractionResult.PASS;
		}
	}

}
