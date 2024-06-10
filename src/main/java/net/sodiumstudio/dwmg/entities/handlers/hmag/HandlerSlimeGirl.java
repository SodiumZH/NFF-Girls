package net.sodiumstudio.dwmg.entities.handlers.hmag;

import java.util.HashSet;
import java.util.Random;

import com.github.mechalopa.hmag.registry.ModEntityTypes;
import com.github.mechalopa.hmag.world.entity.MagicalSlimeEntity;
import com.github.mechalopa.hmag.world.entity.SlimeGirlEntity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.befriendmobs.entity.befriending.BefriendableAddHatredReason;
import net.sodiumstudio.befriendmobs.entity.befriending.BefriendableMobInteractArguments;
import net.sodiumstudio.befriendmobs.entity.befriending.BefriendableMobInteractionResult;
import net.sodiumstudio.befriendmobs.entity.befriending.handlerpreset.HandlerItemGivingProgress;
import net.sodiumstudio.befriendmobs.registry.BMCaps;
import net.sodiumstudio.dwmg.item.MagicalGelColorUtils;
import net.sodiumstudio.dwmg.registries.DwmgItems;
import net.sodiumstudio.nautils.EntityHelper;
import net.sodiumstudio.nautils.NaReflectionUtils;
import net.sodiumstudio.nautils.math.LinearColor;
import net.sodiumstudio.nautils.math.RndUtil;

public class HandlerSlimeGirl extends HandlerItemGivingProgress
{

	protected static Random rnd = new Random();
	
	protected double getDeltaProc(LinearColor color1, LinearColor color2)
	{
		double colorDist = color1.toNormalized().distanceTo(color2.toNormalized()) / Math.sqrt(3d);
		double res = 0;
		if (colorDist < 0.3d)
		{
			res = (0.5d - colorDist) / 0.5d;
			res = res * res;
			res = res * 0.5d;	// 0 ~ 0.5
		}
		else res = (0.5d - colorDist);	// -0.5 ~ 0
		return res;
	}
	
	@Override
	public HashSet<BefriendableAddHatredReason> getAddHatredReasons() {
		HashSet<BefriendableAddHatredReason> set = new HashSet<BefriendableAddHatredReason>();
		set.add(BefriendableAddHatredReason.ATTACKED);
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
			return getDeltaProc(DwmgItems.MAGICAL_GEL_BOTTLE.get().getColor(item), MagicalGelColorUtils.getSlimeColor(sg))
					* Mth.clamp(rnd.nextGaussian() + 1d, 0.5d, 1.5d);
		}
		else if (item.is(DwmgItems.MAGICAL_GEL_BALL.get()))
		{
			return RndUtil.rndRangedDouble(0.02, 0.05);
		}
			
		else return 0;
	}

	@Override
	public boolean isItemAcceptable(ItemStack item) {
		return item.is(DwmgItems.MAGICAL_GEL_BOTTLE.get())
				|| item.is(DwmgItems.MAGICAL_GEL_BALL.get());
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
		if (itemGivenCopy.is(DwmgItems.MAGICAL_GEL_BOTTLE.get()))
		{
			ItemStack cpy = itemGivenCopy.copy();
			// Don't consume if no change
			if (Math.abs(procAfter - procBefore) < 1e-8d)
				return cpy;
			if (DwmgItems.MAGICAL_GEL_BOTTLE.get().getAmount(cpy) == 1)
				return DwmgItems.EMPTY_MAGICAL_GEL_BOTTLE.get().getDefaultInstance();
			else
			{
				DwmgItems.MAGICAL_GEL_BOTTLE.get().setAmount(cpy, DwmgItems.MAGICAL_GEL_BOTTLE.get().getAmount(itemGivenCopy) - 1);
				return cpy;
			}
		}
		else return ItemStack.EMPTY;
	}
	
	@Override
	public void onItemGiven(Player player, Mob mob, ItemStack itemGivenCopy, double procBefore, double procAfter) 
	{
		if (itemGivenCopy.is(DwmgItems.MAGICAL_GEL_BOTTLE.get()))
		{
			double delta = getDeltaProc(DwmgItems.MAGICAL_GEL_BOTTLE.get().getColor(itemGivenCopy), MagicalGelColorUtils.getSlimeColor((SlimeGirlEntity)mob));
			if (delta > 0)
			{
				// 20 glints at most
				int amount = (int) (delta / 0.0125d) + 1;
				amount = Mth.clamp(amount, 1, 40);
				EntityHelper.sendParticlesToEntity(mob, ParticleTypes.HAPPY_VILLAGER, mob.getBbHeight() - 0.2, 0.5d, amount, 1d);
			}
			else
			{
				int amount = (int) ((-delta) / 0.1d) + 1;
				amount = Mth.clamp(amount, 1, 5);
				EntityHelper.sendParticlesToEntity(mob, ParticleTypes.ANGRY_VILLAGER, mob.getBbHeight() - 0.2, 0.3d, amount, 1d);
			}
		}
		else if (itemGivenCopy.is(DwmgItems.MAGICAL_GEL_BALL.get()))
		{
			if (mob.getType() == ModEntityTypes.SLIME_GIRL.get() && mob instanceof SlimeGirlEntity sg && rnd.nextDouble() < 0.25d)
            {
	            MagicalSlimeEntity slime = ModEntityTypes.MAGICAL_SLIME.get().create(mob.level);
	            try
	            {
	            	NaReflectionUtils.forceInvoke(slime, MagicalSlimeEntity.class, "setSize", 1);
	            }
	            catch (Exception e)
	            {
	            	e.printStackTrace();
	            	NaReflectionUtils.forceInvoke(slime, Slime.class, "m_7839_", 1);//setSize
	            }
            	LinearColor sgColorCompl = MagicalGelColorUtils.getSlimeColor(sg).getComplementary();
            	SlimeGirlEntity.ColorVariant v = MagicalGelColorUtils.closestVariant(sgColorCompl);
            	slime.setVariant(v.getId());
            	slime.moveTo(mob.getX() + RndUtil.rndRangedDouble(-0.5, 0.5), mob.getY() + 0.5D, mob.getZ() + RndUtil.rndRangedDouble(-0.5, 0.5), rnd.nextFloat() * 360.0F, 0.0F);
            	mob.level.addFreshEntity(slime);
            }
			EntityHelper.sendGlintParticlesToLivingDefault(mob);
		}
	}
	
	@Override
	public void sendParticlesOnItemReceived(Mob target) {}
	
	@Override
	public BefriendableMobInteractionResult handleInteract(BefriendableMobInteractArguments args)
	{
		if (!args.isClient() && args.isMainHand() && args.getPlayer().getMainHandItem().is(DwmgItems.MAGICAL_GEL_BALL.get()))
		{
			args.getPlayer().getCapability(BMCaps.CAP_BM_PLAYER).ifPresent((c) -> 
			{
				c.getNbt().putBoolean("magical_gel_ball_no_use", true);
			});
		}
		return super.handleInteract(args);
	}
}
