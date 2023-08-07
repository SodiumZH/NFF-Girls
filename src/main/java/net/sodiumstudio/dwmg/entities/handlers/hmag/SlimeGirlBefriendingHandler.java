package net.sodiumstudio.dwmg.entities.handlers.hmag;

import java.util.HashSet;

import com.github.mechalopa.hmag.world.entity.SlimeGirlEntity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.befriendmobs.entity.befriending.BefriendableAddHatredReason;
import net.sodiumstudio.befriendmobs.entity.befriending.BefriendableMobInteractArguments;
import net.sodiumstudio.befriendmobs.entity.befriending.BefriendableMobInteractionResult;
import net.sodiumstudio.befriendmobs.entity.befriending.BefriendingHandler;
import net.sodiumstudio.befriendmobs.entity.befriending.handlerpreset.HandlerItemGiving;
import net.sodiumstudio.befriendmobs.entity.befriending.handlerpreset.HandlerItemGivingProgress;
import net.sodiumstudio.befriendmobs.entity.capability.CBefriendableMob;
import net.sodiumstudio.befriendmobs.registry.BMCaps;
import net.sodiumstudio.dwmg.item.MagicalGelBottleItem;
import net.sodiumstudio.dwmg.item.MagicalGelColorUtils;
import net.sodiumstudio.dwmg.registries.DwmgItems;
import net.sodiumstudio.nautils.ContainerHelper;
import net.sodiumstudio.nautils.EntityHelper;
import net.sodiumstudio.nautils.NbtHelper;
import net.sodiumstudio.nautils.math.LinearColor;
import net.sodiumstudio.nautils.math.RndUtil;

public class SlimeGirlBefriendingHandler extends HandlerItemGivingProgress
{

	protected double getDeltaProc(LinearColor color1, LinearColor color2)
	{
		double colorDist = color1.toNormalized().distanceTo(color2.toNormalized()) / Math.sqrt(3d);
		double res = 0;
		if (colorDist < 0.2d)
		{
			res = (0.2d - colorDist) / 0.2d;
			res = res * res * res;
			res = res * 0.25d;	// 0 ~ 0.25
		}
		else res = (0.2d - colorDist) * 0.625d;	// -0.5 ~ 0
		res *= RndUtil.rndRangedDouble(0.5d, 1.5d);
		return res;
	}
	
	@Override
	public HashSet<BefriendableAddHatredReason> getAddHatredReasons() {
		HashSet<BefriendableAddHatredReason> set = new HashSet<BefriendableAddHatredReason>();
		set.add(BefriendableAddHatredReason.ATTACKED);
		set.add(BefriendableAddHatredReason.ATTACKING);
		set.add(BefriendableAddHatredReason.HIT);
		return set;
	}
		
	@Override
	public int getHatredDurationTicks(BefriendableAddHatredReason reason)
	{
		switch (reason)
		{
		case ATTACKED:
			return 60 * 20;
		case ATTACKING:
			return 15 * 20; 
		case HIT:
			return 15 * 20;
		default:
			return 0;				
		}
	}
	@Override
	protected double getProcValueToAdd(ItemStack item, Player player, Mob mob, double lastProc) {
		if (item.is(DwmgItems.MAGICAL_GEL_BOTTLE.get()) && mob instanceof SlimeGirlEntity sg)
		{
			return getDeltaProc(DwmgItems.MAGICAL_GEL_BOTTLE.get().getColor(item), MagicalGelColorUtils.getSlimeColor(sg));
		}
		else return 0;
	}

	@Override
	public boolean isItemAcceptable(ItemStack item) {
		return item.is(DwmgItems.MAGICAL_GEL_BOTTLE.get());
	}

	@Override
	public boolean additionalConditions(Player player, Mob mob) {
		return true;
	}

	@Override
	public int getItemGivingCooldownTicks() {
		return 10 * 20;
	}
	
	@Override
	public void onAddingHatred(Mob mob, Player player, BefriendableAddHatredReason reason)
	{}
	
	@Override
	public ItemStack getReturnedItem(Player player, Mob mob, ItemStack itemGivenCopy, double procBefore, double procAfter)
	{
		ItemStack cpy = itemGivenCopy.copy();
		if (DwmgItems.MAGICAL_GEL_BOTTLE.get().getAmount(cpy) == 1)
			return DwmgItems.EMPTY_MAGICAL_GEL_BOTTLE.get().getDefaultInstance();
		else
		{
			DwmgItems.MAGICAL_GEL_BOTTLE.get().setAmount(cpy, DwmgItems.MAGICAL_GEL_BOTTLE.get().getAmount(itemGivenCopy) - 1);
			return cpy;
		}
	}
	
	@Override
	public void onItemGiven(Player player, Mob mob, ItemStack itemGivenCopy, double procBefore, double procAfter) 
	{
		if (procBefore - procAfter > 0)
		{
			int amount = (int) ((procBefore - procAfter) / 0.2d) + 1;
			EntityHelper.sendParticlesToEntity(mob, ParticleTypes.ANGRY_VILLAGER, mob.getBbHeight() - 0.2, 0.3d, amount, 1d);
		}
	}
}
