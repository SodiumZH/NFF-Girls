package net.sodiumstudio.dwmg.dwmgcontent.entities.handlers.hmag;

import com.github.mechalopa.hmag.registry.ModItems;
import com.github.mechalopa.hmag.world.entity.EnderExecutorEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending.BefriendableMobInteractArguments;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending.BefriendableMobInteractionResult;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending.handlerpreset.HandlerItemGivingProgress;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.capability.CBefriendableMob;
import net.sodiumstudio.dwmg.befriendmobs.registry.BefMobCapabilities;
import net.sodiumstudio.dwmg.befriendmobs.util.EntityHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.MiscUtil;
import net.sodiumstudio.dwmg.befriendmobs.util.NbtHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.math.RndUtil;
import net.sodiumstudio.dwmg.dwmgcontent.registries.DwmgItems;

public class HandlerEnderExecutor extends HandlerItemGivingProgress
{

	@Override
	public void initCap(CBefriendableMob cap)
	{
		cap.getNbt().putBoolean("cannot_teleport", false);
	}

	@Override
	public BefriendableMobInteractionResult handleInteract(BefriendableMobInteractArguments args)
	{
		BefriendableMobInteractionResult result = super.handleInteract(args);
		if (result.handled && args.getPlayer().isCreative())
			CBefriendableMob.getCap(args.getTarget()).getNbt().putUUID("player_uuid_on_befriending", args.getPlayer().getUUID());
		return result;
	}
	
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
	public boolean shouldIgnoreHatred()
	{
		return true;
	}
	
	@Override
	public boolean additionalConditions(Player player, Mob mob) {
		// Only works on player in the mob's tag "player_uuid_on_befriending"
		// This tag is given when player is looked at the enderman
		return CBefriendableMob.getCapNbt(mob).contains("player_uuid_on_befriending", 11)
				&& CBefriendableMob.getCapNbt(mob).getUUID("player_uuid_on_befriending").equals(player.getUUID())
				|| player.isCreative();	// Creative-mode player can always befriend
	}

	@Override
	public int getItemGivingCooldownTicks() {
		return 300;
	}

	@Override
	public void serverTick(Mob mob)
	{
		if (mob instanceof EnderExecutorEntity ee)
		{
			ee.getCapability(BefMobCapabilities.CAP_BEFRIENDABLE_MOB).ifPresent((l) -> {
				// If not in befriending process, find if it's looked at, and enter the process
				// The tag is also an indicator for if this mob is on befriending process
				// Because Ender Executors have an extra state in which the "proc_value" is still 0 but the process has started
				// i.e. the mob is looked at but nothing has been given
				// On interruption it's removed
				if (!l.getNbt().contains("player_uuid_on_befriending", 11))
					for (Player player: mob.level.players()) 
					{
						if (mob.distanceToSqr(player) <= 256.0d)
						{
							if (EntityHelper.isEnderManLookedAt(ee, player) && ee.getTarget() != null && ee.getTarget().equals(player))
							{
								l.getNbt().putUUID("player_uuid_on_befriending", player.getUUID());
								break;
							}
						}
					}
				// In process
				if (l.getNbt().contains("player_uuid_on_befriending", 11))
				{
					Player player = ee.level.getPlayerByUUID(l.getNbt().getUUID("player_uuid_on_befriending"));
					// Once entered 4 blocks away, disable teleporting
					// So that the mob will not get over 4 blocks away by itself
					// This tag blocks teleporting in DwmgEntityEvents class
					if (ee.distanceToSqr(player) <= 16.0d && (!l.getNbt().getBoolean("cannot_teleport")))
					{
						l.getNbt().putBoolean("cannot_teleport", true);
					}
					// If player is further than 4 blocks, interrupt
					else if (ee.distanceToSqr(player) > 16.0d)
					{
						this.interrupt(player, mob);
					}
					// Otherwise the process continues, set always hostile
					else
					{
						mob.setTarget(player);
					}
				}
			});
		}
	}

	@Override
	public void interrupt(Player player, Mob mob)
	{
		super.interrupt(player, mob);
		CompoundTag nbt = CBefriendableMob.getCapNbt(mob);
		nbt.remove("player_uuid_on_befriending");
		nbt.putBoolean("cannot_teleport", false);	
		mob.setTarget(null);
	}
	
}
