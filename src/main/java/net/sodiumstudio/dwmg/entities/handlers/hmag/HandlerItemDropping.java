package net.sodiumstudio.dwmg.entities.handlers.hmag;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.sodiumstudio.befriendmobs.entity.befriending.BefriendableAddHatredReason;
import net.sodiumstudio.befriendmobs.entity.befriending.BefriendableMobInteractArguments;
import net.sodiumstudio.befriendmobs.entity.befriending.BefriendableMobInteractionResult;
import net.sodiumstudio.befriendmobs.entity.befriending.BefriendingHandler;

public abstract class HandlerItemDropping extends BefriendingHandler
{
	
	@Override
	public BefriendableMobInteractionResult handleInteract(BefriendableMobInteractArguments args) {
		return new BefriendableMobInteractionResult();
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
		HashSet<BefriendableAddHatredReason> reasons = new HashSet<BefriendableAddHatredReason>();
		reasons.add(BefriendableAddHatredReason.ATTACKED);
		return reasons;
	}

	public abstract Map<String, Supplier<Double>> getDeltaProc();

}
