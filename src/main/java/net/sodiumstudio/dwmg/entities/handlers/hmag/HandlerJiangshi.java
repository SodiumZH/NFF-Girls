package net.sodiumstudio.dwmg.entities.handlers.hmag;

import java.util.HashSet;
import java.util.UUID;

import javax.annotation.Nullable;

import com.github.mechalopa.hmag.world.entity.JiangshiEntity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.sodiumstudio.befriendmobs.entity.befriending.BefriendableAddHatredReason;
import net.sodiumstudio.befriendmobs.entity.befriending.BefriendableMobInteractArguments;
import net.sodiumstudio.befriendmobs.entity.befriending.BefriendableMobInteractionResult;
import net.sodiumstudio.befriendmobs.entity.befriending.BefriendingHandler;
import net.sodiumstudio.befriendmobs.entity.befriending.registry.BefriendingTypeRegistry;
import net.sodiumstudio.befriendmobs.entity.capability.CBefriendableMob;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.FreezeGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.JiangshiMutableLeapGoal;
import net.sodiumstudio.dwmg.item.TaoistTalismanItem;
import net.sodiumstudio.dwmg.registries.DwmgItems;
import net.sodiumstudio.dwmg.util.DwmgEntityHelper;
import net.sodiumstudio.nautils.ContainerHelper;
import net.sodiumstudio.nautils.EntityHelper;
import net.sodiumstudio.nautils.NbtHelper;
import net.sodiumstudio.nautils.entity.AttributeModifierSwitch;

public class HandlerJiangshi extends BefriendingHandler
{
	
	protected static final AttributeModifierSwitch MODIFIERS = new AttributeModifierSwitch()
			.putGenerated(1, Attributes.ATTACK_DAMAGE, 4.0, Operation.ADDITION)
			.putGenerated(1, Attributes.MOVEMENT_SPEED, 0.1, Operation.MULTIPLY_BASE)
			.putGenerated(2, Attributes.ATTACK_DAMAGE, 8.0, Operation.ADDITION)
			.putGenerated(3, Attributes.MOVEMENT_SPEED, 0.2, Operation.MULTIPLY_BASE)
			.putGenerated(3, Attributes.ATTACK_DAMAGE, 16.0, Operation.ADDITION)
			.putGenerated(3, Attributes.ATTACK_KNOCKBACK, 0.5, Operation.ADDITION)
			.putGenerated(3, Attributes.MOVEMENT_SPEED, 0.3, Operation.MULTIPLY_BASE)
			.putGenerated(4, Attributes.ATTACK_DAMAGE, 28.0, Operation.ADDITION)
			.putGenerated(4, Attributes.ATTACK_KNOCKBACK, 1.5, Operation.ADDITION)
			.putGenerated(4, Attributes.MOVEMENT_SPEED, 0.6, Operation.MULTIPLY_BASE)
			.putGenerated(5, Attributes.ATTACK_DAMAGE, 40.0, Operation.ADDITION)
			.putGenerated(5, Attributes.ATTACK_KNOCKBACK, 2.5, Operation.ADDITION)
			.putGenerated(5, Attributes.MOVEMENT_SPEED, 1.0, Operation.MULTIPLY_BASE);
	
	protected static final AttributeModifier FROZEN_ARMOR = new AttributeModifier(UUID.fromString("3b41599b-b30f-4106-8b69-93cb4b3df66a"), 
			"frozen_armor", 40d, AttributeModifier.Operation.ADDITION);
	@Override
	public void initCap(CBefriendableMob cap)
	{
		cap.getNbt().put("progress", new CompoundTag());
	}	
	
	@Override
	public BefriendableMobInteractionResult handleInteract(BefriendableMobInteractArguments args) 
	{
		return new BefriendableMobInteractionResult();
	}

	@Override
	public void interrupt(Player player, Mob mob, boolean isQuiet) 
	{
		CBefriendableMob.getCapNbt(mob).getCompound("progress").remove(player.getStringUUID());
		MODIFIERS.apply(mob, getAngerPhase(mob));
	}
	
	@Override
	public boolean interruptAll(Mob mob, boolean isQuiet)
	{
		if (!CBefriendableMob.getCapNbt(mob).getCompound("progress").isEmpty())
		{
			CBefriendableMob.getCapNbt(mob).put("progress", new CompoundTag());
			MODIFIERS.apply(mob, 0);
			return true;
		}
		else return false;
	}
	
	@Override
	public boolean isInProcess(Player player, Mob mob) {
		return getProgressLevel(mob, player) > 0;
	}

	@Override
	public HashSet<BefriendableAddHatredReason> getAddHatredReasons() {
		return ContainerHelper.setOf();
	}

	@Override
	public void serverTick(Mob mob)
	{
		super.serverTick(mob);
		CBefriendableMob cap = CBefriendableMob.getCap(mob);
		if (cap == null)
			return;
		//this.forAllPlayersInProcess(mob, player -> this.interrupt(player, mob, false));
		
		if (getFrozenTime(mob) > 0)
		{
			if (!mob.getAttribute(Attributes.ARMOR).hasModifier(FROZEN_ARMOR))
				mob.getAttribute(Attributes.ARMOR).addTransientModifier(FROZEN_ARMOR);
		}
		else
		{
			mob.getAttribute(Attributes.ARMOR).removeModifier(FROZEN_ARMOR);
		}
		
		if (getAngerPhase(mob) >= 3)
		{
			EntityHelper.addEffectSafe(mob,new MobEffectInstance(MobEffects.JUMP, 19, getAngerPhase(mob) == 3 ? 1 : 2));
			JiangshiMutableLeapGoal.setLeapHeightBonus(mob, getAngerPhase(mob) == 3 ? 1 : 2);
		}
		else
		{
			JiangshiMutableLeapGoal.setLeapHeightBonus(mob, 0);
		}
		
		if (getFrozenTime(mob) > 0)
		{
			DwmgEntityHelper.sendCriticalParticlesToLivingDefault(mob, 0, 5);
		}
		if (getAngerPhase(mob) > 0)
		{
			EntityHelper.sendSmokeParticlesToLivingDefault(mob, 0, getAngerPhase(mob) * 2);
		}
		if (cap.hasTimer("clear_fire") && mob.tickCount % 10 == 0)
		{
			mob.clearFire();
		}

	}
	
	/**
	 *  Peach-Wood sword hit
	 *  Invoked in {@link DwmgEntityEvent#onLivingDamage}
	 */
	
	public void onPeachSwordHit(Mob mob, Player player)
	{
		int oldPhase = getAngerPhase(mob);
		if (getFrozenTime(mob) > 0)
		{
			if (getProgressLevel(mob, player) == 5)
			{
				MODIFIERS.apply(mob, 0);
				thunderEffect(mob);
				EntityHelper.sendHeartParticlesToLivingDefault(mob);
				Mob bm = (Mob) befriend(player, mob);
				bm.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 10 * 20));
				return;
			}
			else
			{
				progressIncrease(mob, player);
				EntityHelper.sendParticlesToEntity(mob, ParticleTypes.EXPLOSION, 0.2, 0, 1, 0);
				CBefriendableMob.getCap(mob).stopTimer("frozen", true);
				CBefriendableMob.getCap(mob).setPlayerTimer(player, "peach_sword", 60 * 20);
			}
			if (oldPhase == 2 || getProgressLevel(mob, player) == 3)
			{
				thunderEffect(mob);
			}
			updateModifiers(mob);
			mob.level.playSound(null, mob, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 2f, 0.7f);
		}
		else
		{
			mob.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20 * 20, 1));
		}
	}
	
	
	// Freezing related
	/** Freezing is handled with {@link FreezeGoal and @link DwmgEntityEvents#onEntityJoinLevel} */
	
	public int getFrozenTime(Mob mob)
	{
		CBefriendableMob cap = CBefriendableMob.getCap(mob);
		if (cap == null)
			return 0;
		else if (cap.hasTimer("frozen"))
			return cap.getTimer("frozen");
		else return 0;
	}
	
	public static boolean isFrozen(Mob mob)
	{
		BefriendingHandler handler = BefriendingTypeRegistry.getHandler(mob);
		if (handler instanceof HandlerJiangshi hjs)
		{
			return hjs.getFrozenTime(mob) > 0;
		}
		else return false;
			
	}
	
	/**
	 * Invoked in {@link TaoistTalismanItem#interactLivingEntity}
	 */
	public boolean applyTalisman(Mob mob)
	{
		CBefriendableMob cap = CBefriendableMob.getCap(mob);
		if (getFrozenTime(mob) > 0)
			return false;
		else if (cap.hasTimer("freeze_cooldown"))
			return false;
		else
		{
			cap.setTimer("frozen", 200);
			return true;
		}
	}
	
	// Progress related
	
	public int getProgressLevel(Mob mob, Player player)
	{
		if (CBefriendableMob.getCapNbt(mob).getCompound("progress").contains(player.getStringUUID(), NbtHelper.TAG_INT_ID))
			return CBefriendableMob.getCapNbt(mob).getCompound("progress").getInt(player.getStringUUID());
		else return 0;
	}
	
	public void setProgressLevel(Mob mob, Player player, int value)
	{
		if (value < 0 || value > 5)
			CBefriendableMob.getCapNbt(mob).getCompound("progress").remove(player.getStringUUID());
		else
			CBefriendableMob.getCapNbt(mob).getCompound("progress").putInt(player.getStringUUID(), value);
		updateModifiers(mob);		
	}
	
	public void progressIncrease(Mob mob, Player player)
	{
		setProgressLevel(mob, player, getProgressLevel(mob, player) + 1);
	}
	
	public void progressDecrease(Mob mob, Player player)
	{
		setProgressLevel(mob, player, getProgressLevel(mob, player) - 1);
	}
	
	public int getAngerPhase(Mob mob)
	{
		int res = 0;
		for (Player player: mob.level.players())
		{
			if (isInProcess(player, mob) && getProgressLevel(mob, player) > res)
				res = getProgressLevel(mob, player);
		}
		return res;
	}

	public void updateModifiers(Mob mob)
	{
		MODIFIERS.apply(mob, getAngerPhase(mob));
	}
	
	@Override
	public void onBefriendableMobTimerUp(Mob mob, String timerKey, @Nullable Player player) 
	{
		if (CBefriendableMob.getCap(mob) == null)
			return;
		if (mob instanceof JiangshiEntity && timerKey == "frozen" && player == null)
		{
			if (mob.getRandom().nextDouble() < 0.667)
				mob.spawnAtLocation(DwmgItems.TAOIST_TALISMAN.get().getDefaultInstance());
			CBefriendableMob.getCap(mob).setTimer("freeze_cooldown", 15 * 20);
		}
		else if (mob instanceof JiangshiEntity && timerKey == "peach_sword" && player != null)
		{
			this.progressDecrease(mob, player);
		}
	}
	
	public void thunderEffect(Mob mob)
	{
		LightningBolt lb = EntityType.LIGHTNING_BOLT.create(mob.level);
		lb.moveTo(Vec3.atBottomCenterOf(mob.blockPosition()));
		lb.setDamage(0);
		mob.level.addFreshEntity(lb);
		mob.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 30 * 20));
		CBefriendableMob.getCap(mob).setTimer("clear_fire", 10 * 20);
	}
}
