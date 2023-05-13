package net.sodiumstudio.dwmg.entities.handlers.hmag;

import java.util.HashSet;
import java.util.UUID;

import com.github.mechalopa.hmag.world.entity.EnderExecutorEntity;
import com.github.mechalopa.hmag.world.entity.NecroticReaperEntity;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.sodiumstudio.befriendmobs.entity.befriending.BefriendableAddHatredReason;
import net.sodiumstudio.befriendmobs.entity.befriending.BefriendableMobInteractArguments;
import net.sodiumstudio.befriendmobs.entity.befriending.BefriendableMobInteractionResult;
import net.sodiumstudio.befriendmobs.entity.befriending.BefriendingHandler;
import net.sodiumstudio.befriendmobs.entity.capability.CBefriendableMob;
import net.sodiumstudio.befriendmobs.registry.BefMobCapabilities;
import net.sodiumstudio.befriendmobs.util.EntityHelper;
import net.sodiumstudio.befriendmobs.util.NbtHelper;
import net.sodiumstudio.dwmg.blocks.BlockSoulCarpet;
import net.sodiumstudio.dwmg.registries.DwmgCapabilities;
import net.sodiumstudio.dwmg.registries.DwmgItems;

/**
 * Process mechanism:
 * Player must have Necromancer's Hat and Necromancer's Wand.
 * When the mob is standing on soul carpet, cast with wand to hit it.
 * Overall 6 hits required. After 1~5 hits, its atk will increase by: 5, 10, 15, 20, 30.
 * 
 */
public class HandlerNecroticReaper extends BefriendingHandler
{

	protected UUID[] modifierUUIDs = {
			UUID.fromString("86750cf1-7597-4a24-bcae-b3f95886428a"),
			UUID.fromString("d0c6d0bc-3480-40c1-9d45-7fe0414d9eb0"),
			UUID.fromString("dbc4f9a3-e79f-4634-ba1e-4173c394a86a"),
			UUID.fromString("c0c5c069-860b-432a-81b0-42961fb47ab2"),
			UUID.fromString("47d74e17-c1b8-4f59-bd03-8d7a539f20b6")};
	protected AttributeModifier[] modifiers = {
			new AttributeModifier(modifierUUIDs[0], "nr_atk_boost_1", 5d, AttributeModifier.Operation.ADDITION),
			new AttributeModifier(modifierUUIDs[1], "nr_atk_boost_2", 10d, AttributeModifier.Operation.ADDITION),
			new AttributeModifier(modifierUUIDs[2], "nr_atk_boost_3", 15d, AttributeModifier.Operation.ADDITION),
			new AttributeModifier(modifierUUIDs[3], "nr_atk_boost_4", 20d, AttributeModifier.Operation.ADDITION),
			new AttributeModifier(modifierUUIDs[4], "nr_atk_boost_5", 25d, AttributeModifier.Operation.ADDITION)};
		

	/**
	 * Init on adding capability to mob
	 */
	@Override
	public void initCap(CBefriendableMob cap)
	{
		if (!cap.getNbt().contains("already_hits", 3))
		{
			cap.getNbt().putInt("already_hits", 0);
		}
		updateModifier(cap);
	}	
	
	/**
	 * Update modifier from current hit count
	 */
	protected void updateModifier(CBefriendableMob cap)
	{
		int hits = cap.getNbt().getInt("already_hits");
		if (hits < 0 || hits > 5)
			throw new RuntimeException("Wrong hit counts. Accepts: 0 ~ 5");
		int expectedModifierIndex = hits - 1;
		// Remove wrong modifiers
		for (int i = 0 ; i < 5; ++i)
		{
			if (i != expectedModifierIndex)
			{
				cap.getOwner().getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(modifiers[i]);
			}
		}
		// Add right modifier
		if (expectedModifierIndex >= 0 && !cap.getOwner().getAttribute(Attributes.ATTACK_DAMAGE).hasModifier(modifiers[expectedModifierIndex]))
		{
			cap.getOwner().getAttribute(Attributes.ATTACK_DAMAGE).addTransientModifier(modifiers[expectedModifierIndex]);
		}
	}
	
	protected void clearModifier(CBefriendableMob cap)
	{
		for (int i = 0; i < 5; ++i)
		{
			cap.getOwner().getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(modifiers[i]);
		}
	}
	
	
	/**
	 * This doesn't depend on interaction, so no action here
	 */
	@Override
	public BefriendableMobInteractionResult handleInteract(BefriendableMobInteractArguments args) {
		return BefriendableMobInteractionResult.of(false, false, null);
	}

	@Override
	public void interrupt(Player player, Mob mob, boolean isQuiet) {
		CBefriendableMob cap = CBefriendableMob.getCap(mob);
		cap.getNbt().remove("ongoing_player_uuid");
		cap.getNbt().putInt("already_hits", 0);
		mob.setLastHurtByPlayer(null);
		mob.setTarget(null);
		if (!isQuiet)
			EntityHelper.sendAngryParticlesToLivingDefault(mob);
	}

	@Override
	public boolean isInProcess(Player player, Mob mob) {
		return CBefriendableMob.getCapNbt(mob).contains("ongoing_player_uuid", NbtHelper.TAG_INT_ARRAY_ID)
				&& CBefriendableMob.getCapNbt(mob).getUUID("ongoing_player_uuid") != null
				&& CBefriendableMob.getCapNbt(mob).getUUID("ongoing_player_uuid").equals(player.getUUID());
	}

	@Override
	public HashSet<BefriendableAddHatredReason> getAddHatredReasons() {
		HashSet<BefriendableAddHatredReason> set = new HashSet<BefriendableAddHatredReason>();
		set.add(BefriendableAddHatredReason.ATTACKED);
		return set;
	}

	/**
	 *  Invoked on server when player hit the mob with Necromancer's Wand. (Only in server)
	 *  Return if the hit effects taken here. If true, it will cancel adding Wither effect.
	 */
	public boolean onHit(Player player, Mob mob)
	{
		CBefriendableMob cap = CBefriendableMob.getCap(mob);
		if (cap == null)
			throw new RuntimeException("Missing capability");
		// If player not wearing necromancer's hat, it cannot be befriended, add wither
		if (!player.getItemBySlot(EquipmentSlot.HEAD).is(DwmgItems.NECROMANCER_HAT.get()))
		{
			return false;
		}
		// If the mob is under befriending by others, add wither
		else if (cap.getNbt().contains("ongoing_player_uuid")
				&& !cap.getNbt().getUUID("ongoing_player_uuid").equals(player.getUUID()))
		{
			return false;
		}
		// If mob is not on soul carpet, nothing happens but don't add wither
		else if (!BlockSoulCarpet.isEntityInside(mob))
		{
			return true;
		}	
		else if (cap.isInHatred(player))
		{
			EntityHelper.sendAngryParticlesToLivingDefault(mob);
			return true;
		}

		int hits = cap.getNbt().getInt("already_hits");

		/* Main process */
		
		// The first hit, add player info
		if (hits == 0)
		{
			// At this time nobody is in process, so there shouldn't be a valid uuid
			if (cap.getNbt().contains("ongoing_player_uuid", NbtHelper.TAG_INT_ARRAY_ID)
					&& cap.getNbt().getUUID("ongoing_player_uuid") != null
					&& mob.level.getPlayerByUUID(cap.getNbt().getUUID("ongoing_player_uuid")) != null)
			{
				throw new IllegalStateException("Player detected but hit count is 0. Player UUID information should be removed when hit count is cleared.");
			}

			cap.getNbt().putUUID("ongoing_player_uuid", player.getUUID());
			// After 10s without attacking player, interrupt
			cap.getNbt().putInt("no_attack_expire_time", 200);
		}
		
		// The last hit, befriend
		if (hits == 5)
		{
			clearModifier(cap);
			EntityHelper.sendHeartParticlesToLivingDefault(mob);
			befriend(player, mob);
			return true;
		}
		// Otherwise just increase the number
		else
		{
			cap.getNbt().putInt("already_hits", hits + 1);
			updateModifier(cap);
			EntityHelper.sendGlintParticlesToLivingDefault(mob);
			return true;
		}	
	}

	@Override
	public void serverTick(Mob mob)
	{
		CBefriendableMob cap = CBefriendableMob.getCap(mob);
		if (	// Is in player process
			cap.getNbt().contains("ongoing_player_uuid", NbtHelper.TAG_INT_ARRAY_ID)
			&& cap.getNbt().getUUID("ongoing_player_uuid") != null
			&& mob.level.getPlayerByUUID(cap.getNbt().getUUID("ongoing_player_uuid")) != null)
		{
			Player player = mob.level.getPlayerByUUID(cap.getNbt().getUUID("ongoing_player_uuid"));
			mob.getCapability(DwmgCapabilities.CAP_UNDEAD_MOB).ifPresent((capUM) ->
			{
				capUM.addHatred(player);	// This blocks the effect of undead affinity
			});
			if (!player.isCreative())
				mob.setTarget(player);
			// Amount of particles emitting each frame
			int amountPerTick = 0;
			
			// Interrupt if player is > 32 blocks away from the mob
			if (mob.distanceToSqr(player) > 1024d)
			{
				interrupt(player, mob, false);
				return;
			}
			
			// If not attacked player for 10s, drop level by 1 (except creative)
			int atkCtd = cap.getNbt().getInt("no_attack_expire_time");
			if (atkCtd <= 0)
			{
				if (!player.isCreative() && cap.getNbt().getInt("already_hits") > 0)
				{
					int hits = cap.getNbt().getInt("already_hits");
						cap.getNbt().putInt("already_hits", hits - 1);
					if (cap.getNbt().getInt("already_hits") <= 0)
						interrupt(player, mob, true);
					updateModifier(cap);
					EntityHelper.sendParticlesToEntity(mob, ParticleTypes.ANGRY_VILLAGER, mob.getBbHeight() - 0.2, 0.3d, 1, 1d);
					cap.getNbt().putInt("no_attack_expire_time", 200);// Reset timer
				}

			}
			else 
			{
				cap.getNbt().putInt("no_attack_expire_time", atkCtd - 1);
			}			

			// Send smoke particles during befriending process
			switch (cap.getNbt().getInt("already_hits"))
			{
			case 0:
			{
				amountPerTick = 0;
				break;
			}
			case 1:
			{
				amountPerTick = 1;
				break;
			}
			case 2:
			{
				amountPerTick = 2;
				break;
			}
			case 3:
			{
				amountPerTick = 3;
				break;
			}
			case 4:
			{
				amountPerTick = 5;
				break;
			}
			case 5:
			{
				amountPerTick = 8;
				break;
			}
			default:
			{
				throw new IllegalStateException("Illegal already-hits count; if 0, there shouldn't be an ongoing-player.");
			}
			}			
			if (amountPerTick > 0)
				EntityHelper.sendParticlesToEntity(
					mob, ParticleTypes.SMOKE, mob.getBbHeight() - 0.2d, 0.5d, amountPerTick, 0d);

			
		}
		
	}
	
	@Override
	public void onAttackProcessingPlayer(Mob mob, Player player, boolean damageGiven)
	{
		if (mob instanceof NecroticReaperEntity ee)
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
