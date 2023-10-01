package net.sodiumstudio.dwmg.entities.handlers.hmag;

import java.util.HashSet;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.befriendmobs.entity.befriending.BefriendableAddHatredReason;
import net.sodiumstudio.befriendmobs.entity.befriending.BefriendableMobInteractArguments;
import net.sodiumstudio.befriendmobs.entity.befriending.BefriendableMobInteractionResult;
import net.sodiumstudio.befriendmobs.entity.befriending.handlerpreset.HandlerItemGiving;

public class HandlerCursedDoll extends HandlerItemGiving
{

	@Override
	public boolean isItemAcceptable(ItemStack itemstack) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean additionalConditions(Player player, Mob mob) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getItemGivingCooldownTicks() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BefriendableMobInteractionResult handleInteract(BefriendableMobInteractArguments args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void interrupt(Player player, Mob mob, boolean isQuiet) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isInProcess(Player player, Mob mob) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public HashSet<BefriendableAddHatredReason> getAddHatredReasons() {
		// TODO Auto-generated method stub
		return null;
	}

}
