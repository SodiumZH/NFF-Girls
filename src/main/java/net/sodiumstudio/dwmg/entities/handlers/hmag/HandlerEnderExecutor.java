package net.sodiumstudio.dwmg.entities.handlers.hmag;

import java.util.HashSet;

import com.github.mechalopa.hmag.registry.ModItems;
import com.github.mechalopa.hmag.world.entity.EnderExecutorEntity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.befriendmobs.entity.befriending.BefriendableAddHatredReason;
import net.sodiumstudio.befriendmobs.entity.befriending.BefriendableMobInteractArguments;
import net.sodiumstudio.befriendmobs.entity.befriending.BefriendableMobInteractionResult;
import net.sodiumstudio.befriendmobs.entity.befriending.handlerpreset.HandlerItemGivingProgress;
import net.sodiumstudio.befriendmobs.entity.capability.CBefriendableMob;
import net.sodiumstudio.befriendmobs.registry.BefMobCapabilities;
import net.sodiumstudio.befriendmobs.util.EntityHelper;
import net.sodiumstudio.befriendmobs.util.MiscUtil;
import net.sodiumstudio.befriendmobs.util.NbtHelper;
import net.sodiumstudio.befriendmobs.util.debug.Debug;
import net.sodiumstudio.befriendmobs.util.math.RndUtil;
import net.sodiumstudio.dwmg.registries.DwmgItems;

public class HandlerEnderExecutor extends HandlerItemGivingProgress
{

	@Override
	public void initCap(CBefriendableMob cap)
	{
		cap.getNbt().putBoolean("cannot_teleport", false);
	}

	@Override
	public IBefriendedMob befriend(Player player, Mob target)
	{
		((EnderExecutorEntity)target).setCarriedBlock(null);
		return super.befriend(player, target);
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
				return 0.750d;
			else if (rnd < 0.15d)
				return 0.500d;
			else return 0.250d;
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
								// If this time reaches 0, it will be interrupted
								l.getNbt().putInt("no_attack_expire_time", 200);
								break;
							}
						}
					}
				// In process
				if (l.getNbt().contains("player_uuid_on_befriending", 11))
				{
					Player player = ee.level.getPlayerByUUID(l.getNbt().getUUID("player_uuid_on_befriending"));
					if (player != null)
					{
						// Once entered 7 blocks away, disable teleporting
						// So that the mob will not get over 4 blocks away by itself
						// This tag blocks teleporting in DwmgEntityEvents class
						if (ee.distanceToSqr(player) <= 49.0d && (!l.getNbt().getBoolean("cannot_teleport")))
						{
							l.getNbt().putBoolean("cannot_teleport", true);
						}
						// If player is further than 8 blocks, interrupt
						else if (l.getNbt().getBoolean("cannot_teleport") && ee.distanceToSqr(player) > 64.0d)
						{
							this.interrupt(player, mob, false);
						}
						// Otherwise the process continues, set always hostile
						else
						{
							mob.setTarget(player);
						}
					}
					// Update no attack timer
					// This timer will be reset in EntityEvent LivingHurtEvent listener
					if (l.getNbt().contains("no_attack_expire_time") && l.getNbt().getInt("no_attack_expire_time") > 0 && !player.isCreative())
					{
						l.getNbt().putInt("no_attack_expire_time", l.getNbt().getInt("no_attack_expire_time") - 1);
						if (l.getNbt().getInt("no_attack_expire_time") == 0)
						{
							// When 10s no attack expired, process loses 0.2
							DoubleTag currentValueTag = (DoubleTag) NbtHelper.getPlayerData(l.getPlayerDataNbt(), player, "proc_value");
							double procValue = currentValueTag == null ? 0 : currentValueTag.getAsDouble();
							procValue -= 0.2d;
							// If it drops 0, interrupt
							if (procValue <= 0)
							{
								interrupt(player, mob, false);
							}
							// Otherwise put the value back, send some particles and reset timer
							else 
							{
								NbtHelper.putPlayerData(DoubleTag.valueOf(procValue), l.getPlayerDataNbt(), player, "proc_value");
								EntityHelper.sendParticlesToEntity(mob, ParticleTypes.ANGRY_VILLAGER, mob.getBbHeight() - 0.2, 0.3d, 2, 1d);
								l.getNbt().putInt("no_attack_expire_time", 200);
							}
						}
					}
				}
				
			});
		}
	}

	@Override
	public void interrupt(Player player, Mob mob, boolean isQuiet)
	{
		super.interrupt(player, mob, isQuiet);
		CompoundTag nbt = CBefriendableMob.getCapNbt(mob);
		nbt.remove("player_uuid_on_befriending");
		nbt.remove("no_attack_expire_time");
		nbt.putBoolean("cannot_teleport", false);	
		mob.setTarget(null);
	}
	
	@Override
	public boolean isInProcess(Player player, Mob mob)
	{
		CBefriendableMob l = CBefriendableMob.getCap(mob);
		return l.getNbt().contains("player_uuid_on_befriending", 11) && l.getNbt().getUUID("player_uuid_on_befriending").equals(player.getUUID());
	}

	@Override
	public HashSet<BefriendableAddHatredReason> getAddHatredReasons() {
		HashSet<BefriendableAddHatredReason> set = new HashSet<BefriendableAddHatredReason>();
		set.add(BefriendableAddHatredReason.ATTACKED);
		return set;
	}

	@Override
	public void onAttackProcessingPlayer(Mob mob, Player player, boolean damageGiven)
	{
		if (mob instanceof EnderExecutorEntity ee)
		{
			ee.getCapability(BefMobCapabilities.CAP_BEFRIENDABLE_MOB).ifPresent((cap) -> {
				if (cap.getNbt().contains("no_attack_expire_time"))
				{
					cap.getNbt().putInt("no_attack_expire_time", 200);
				}
						
			});
		}
	}
	
}
