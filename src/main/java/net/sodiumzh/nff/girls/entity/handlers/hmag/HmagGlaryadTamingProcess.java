package net.sodiumzh.nff.girls.entity.handlers.hmag;

import com.github.mechalopa.hmag.registry.ModItems;
import com.github.mechalopa.hmag.util.ModTags;
import com.github.mechalopa.hmag.world.entity.GlaryadEntity;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.FlowerBlock;
import net.sodiumzh.nautils.math.RndUtil;
import net.sodiumzh.nff.services.entity.taming.TamableHatredReason;
import net.sodiumzh.nff.services.entity.taming.TamingProcessItemGivingProgress;

public class HmagGlaryadTamingProcess extends TamingProcessItemGivingProgress
{

	@Override
	protected double getProcValueToAdd(ItemStack item, Player player, Mob mob, double oldProc) {
		if (item.is(Items.SPORE_BLOSSOM))
			return RndUtil.rndRangedDouble(0.08, 0.16);
		else if (item.is(ModItems.MYSTERIOUS_PETAL.get()))
			return RndUtil.rndRangedDouble(0.15, 0.3);
		else if (item.is(ModItems.CUREBERRY.get()) || item.is(ModItems.EXP_BERRY.get()) || item.is(ModItems.RANDOMBERRY.get()))
			return RndUtil.rndRangedDouble(0.3, 0.5);
		else if (item.getItem() != null && item.getItem() instanceof BlockItem blockitem && blockitem.getBlock() instanceof FlowerBlock)
			return RndUtil.rndRangedDouble(0.06, 0.1);
		else return 0;
	}

	@Override
	public boolean isItemAcceptable(ItemStack itemstack) {
		if (itemstack.is(Items.SPORE_BLOSSOM))
			return true;
		else if (itemstack.is(ModItems.MYSTERIOUS_PETAL.get()))
			return true;
		else if (itemstack.is(ModItems.CUREBERRY.get()) || itemstack.is(ModItems.EXP_BERRY.get()) || itemstack.is(ModItems.RANDOMBERRY.get()))
			return true;
		else if (itemstack.getItem() != null && itemstack.getItem() instanceof BlockItem blockitem && blockitem.getBlock() instanceof FlowerBlock)
			return true;
		else return false;
	}

	@Override
	public boolean additionalConditions(Player player, Mob mob) {
		return mob.hasEffect(MobEffects.REGENERATION) 
				&& mob.getEffect(MobEffects.REGENERATION).getAmplifier() >= 2
				&& mob.getEffect(MobEffects.REGENERATION).getDuration() > 10 * 20
				&& player.getOffhandItem().is(ModTags.ItemTags.GLARYAD_TEMPT_ITEMS)
				&& !player.getUUID().equals(((GlaryadEntity)mob).getPersistentAngerTarget());
	}

	@Override
	public int getItemGivingCooldownTicks() {
		return 5 * 20;
	}

	@Override
	public TamableHatredReason[] getAddHatredReasons() {
		return new TamableHatredReason[] {TamableHatredReason.ATTACKED};
	}

}
