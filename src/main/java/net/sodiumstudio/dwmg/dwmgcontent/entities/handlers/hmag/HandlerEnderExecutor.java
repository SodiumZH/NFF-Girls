package net.sodiumstudio.dwmg.dwmgcontent.entities.handlers.hmag;

import com.github.mechalopa.hmag.registry.ModItems;
import com.github.mechalopa.hmag.world.entity.EnderExecutorEntity;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending.BefriendableMobInteractArguments;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending.BefriendableMobInteractionResult;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending.handlerpreset.HandlerItemGivingProgress;
import net.sodiumstudio.dwmg.befriendmobs.registry.BefMobCapabilities;
import net.sodiumstudio.dwmg.befriendmobs.util.MiscUtil;
import net.sodiumstudio.dwmg.befriendmobs.util.math.RndUtil;
import net.sodiumstudio.dwmg.dwmgcontent.registries.DwmgItems;

public class HandlerEnderExecutor extends HandlerItemGivingProgress
{

	@Override
	protected double getProcValueToAdd(ItemStack item) {
		if (item.is(Items.ENDER_EYE))
			return RndUtil.rndRangedDouble(0.005d, 0.01d);
		else if (item.is(ModItems.ENDER_PLASM.get()))
			return RndUtil.rndRangedDouble(0.01d, 0.015d);
		else if (item.is(DwmgItems.ENDER_PIE.get()))
		{
			double rnd = this.rnd.nextDouble();
			if (rnd < 0.05d)
				return 0.7501d;
			else if (rnd < 0.15d)
				return 0.5001d;
			else return 0.2501d;
		}
		else return 0.0d;
	}

	@Override
	public boolean isItemAcceptable(Item item) {
		Item[] items = {
			Items.ENDER_EYE,
			ModItems.ENDER_PLASM.get(),
			DwmgItems.ENDER_PIE.get()
		};
		return MiscUtil.isIn(item, items, Items.AIR);
	}

	@Override
	public boolean additionalConditions(Player player, Mob mob) {
		// NOT IMPLEMENTED
		return false;
	}

	@Override
	public int getItemGivingCooldownTicks() {
		return 300;
	}

	@Override
	public BefriendableMobInteractionResult handleInteract(BefriendableMobInteractArguments args) {
		// NOT IMPLEMENTED
		return null;
	}
	
	@Override
	public void serverTick(Mob mob)
	{
		EnderExecutorEntity ee = (EnderExecutorEntity)mob;
		ee.getCapability(BefMobCapabilities.CAP_BEFRIENDABLE_MOB).ifPresent((l) -> {
			// NOT IMPLEMENTED
			//for(Player player: mob.level.getNearbyPlayers(null, ee, null)) {}
		});
	}

}
