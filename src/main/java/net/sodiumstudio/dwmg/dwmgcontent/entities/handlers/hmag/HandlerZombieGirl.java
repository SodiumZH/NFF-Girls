package net.sodiumstudio.dwmg.dwmgcontent.entities.handlers.hmag;

import java.util.HashSet;
import java.util.Random;

import com.github.mechalopa.hmag.registry.ModItems;

import net.minecraft.nbt.IntTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending.AbstractBefriendingHandler;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending.BefriendableMobInteractArguments;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending.BefriendableMobInteractionResult;
import net.sodiumstudio.dwmg.befriendmobs.util.EntityHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.NbtHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.Util;
import net.sodiumstudio.dwmg.befriendmobs.util.debug.Debug;
import net.sodiumstudio.dwmg.befriendmobs.util.math.RndUtil;
import net.sodiumstudio.dwmg.dwmgcontent.registries.DwmgEffects;
import net.sodiumstudio.dwmg.dwmgcontent.registries.DwmgItems;

public class HandlerZombieGirl extends HandlerItemGivingProcess
{

	@Override
	protected HashSet<Item> givableItems() {
		HashSet<Item> set = new HashSet<Item>();
		set.add(DwmgItems.SOUL_CAKE_SLICE.get());
		set.add(ModItems.SOUL_POWDER.get());
		set.add(ModItems.SOUL_APPLE.get());
		return set;
	}

	@Override
	protected double getProcValue(ItemStack item) {
		float rnd = this.rnd.nextFloat();
		if (item.is(DwmgItems.SOUL_CAKE_SLICE.get()))
			return rnd < 0.05 ? 1.01 : (rnd < 0.2 ? 0.67 : 0.34);
		else if (item.is(ModItems.SOUL_POWDER.get()))
			return RndUtil.rndRangedDouble(0.02, 0.04);
		else if (item.is(ModItems.SOUL_APPLE.get()))
			return RndUtil.rndRangedDouble(0.03, 0.08);
		else return 0;
	}

	@Override
	protected int cooldownTicks() {
		return 200;
	}
	
	@Override
	protected boolean additionalConditions(Player player, Mob mob)
	{
		return player.hasEffect(DwmgEffects.UNDEAD_AFFINITY.get());
	}

}
