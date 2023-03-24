package net.sodiumstudio.dwmg.dwmgcontent.entities.handlers.hmag;

import java.util.HashSet;
import java.util.Random;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending.AbstractBefriendingHandler;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending.BefriendableMobInteractArguments;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending.BefriendableMobInteractionResult;
import net.sodiumstudio.dwmg.befriendmobs.util.EntityHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.NbtHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.TagHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.debug.Debug;
import net.sodiumstudio.dwmg.dwmgcontent.registries.DwmgEffects;

public abstract class HandlerItemGivingProcess extends AbstractBefriendingHandler {

	Random rnd = new Random();

	@Override
	public BefriendableMobInteractionResult handleInteract(BefriendableMobInteractArguments args) {
		Mob target = args.getTarget();
		Player player = args.getPlayer();
		BefriendableMobInteractionResult result = new BefriendableMobInteractionResult();

		args.execServer((l) -> {

			if (!player.isShiftKeyDown() 
					&& (givableItems().contains(player.getMainHandItem().getItem()) 
							|| hasGivableItemTags(player.getMainHandItem().getItem()))
					&& args.isMainHand() 
					&& additionalConditions(player, target)) {
				// Block if in hatred
				if (l.isInHatred(player) && !ignoreHatred()) {
					EntityHelper.sendAngryParticlesToLivingDefault(target);
					Debug.printToScreen("Unable to befriend: in hatred list.", player, target);
					result.setHandled();

				}
				// Block if in cooldown
				else if (l.getTimerPS(player, "item_cooldown") > 0) {
					// EntityHelper.sendSmokeParticlesToMob(target);
					Debug.printToScreen(
							"Action cooldown " + Integer.toString(l.getTimerPS(player, "item_cooldown") / 20) + " s.",
							player, target);
					// result.setHandled();
				} 
				else
				{
					ItemStack mainhand = player.getMainHandItem();
					// Get amount already given
					DoubleTag currentValueTag = (DoubleTag) NbtHelper.getPlayerData(l.getPlayerData(), player,
							"proc_value");
					double procValue = currentValueTag == null ? 0 : currentValueTag.getAsDouble();
					double lastProcValue = procValue;	
					if (!player.isCreative())
						player.getMainHandItem().shrink(1);
					procValue += getProcValue(mainhand);
					Debug.printToScreen("Process Value: " + Double.toString(procValue), player, target);
					if (procValue >= 1.0d) {
						// Satisfied
						finalAction(player, target);
						result.setHandled();
					} else {
						EntityHelper.sendGreenStarParticlesToLivingDefault(target);
						// Not satisfied, put data
						NbtHelper.putPlayerData(DoubleTag.valueOf(procValue), l.getPlayerData(), player,
								"proc_value");
						sendSingleHeart(target, lastProcValue, procValue, deltaProcPerHeart());
						l.setTimerPS(player, "item_cooldown", cooldownTicks()); // Set cooldown
						result.setHandled();
					}
				}
			}
		});

		// ...................................
		args.execClient((l) -> {
			{
				if (!l.isInHatred(player)) {
					if (!player.isShiftKeyDown() && givableItems().contains(player.getMainHandItem().getItem())
							&& args.isMainHand() && player.hasEffect(DwmgEffects.UNDEAD_AFFINITY.get()))
						result.handled = true;
				}
			}
		});
		// ==============================
		return result;
	}

	protected abstract HashSet<Item> givableItems();
	
	protected HashSet<ResourceLocation> givableItemTags()
	{
		return new HashSet<ResourceLocation>();
	}
	
	protected boolean hasGivableItemTags(Item item)
	{
		for (ResourceLocation tag : givableItemTags())
		{
			if (TagHelper.hasTag(item, tag))
				return true;
		}
		return false;
	}
	
	protected abstract double getProcValue(ItemStack item);
	
	protected void sendSingleHeart(Mob target, double procBefore, double procAfter, double deltaProcPerHeart)
	{
		int times = (int)(procAfter / deltaProcPerHeart) - (int)(procBefore / deltaProcPerHeart);
		for (int i = 0; i < times; ++i)
		{
			EntityHelper.sendParticlesToEntity(target, ParticleTypes.HEART, target.getBbHeight() - 0.5, 0.2d, 1, 1d);
		}
	}
	
	protected IBefriendedMob finalAction(Player player, Mob mob)
	{
		EntityHelper.sendHeartParticlesToLivingDefault(mob);
		return befriend(player, mob);
	}
	
	protected abstract int cooldownTicks();
	protected double deltaProcPerHeart()
	{
		return 0.2d;
	}
	
	protected boolean ignoreHatred()
	{
		return false;
	}
	
	protected boolean additionalConditions(Player player, Mob mob)
	{
		return true;
	}
}
