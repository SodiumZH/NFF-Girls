package net.sodiumstudio.dwmg.item;

import com.github.mechalopa.hmag.registry.ModEntityTypes;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.sodiumstudio.befriendmobs.entity.befriending.registry.BefriendingTypeRegistry;
import net.sodiumstudio.befriendmobs.registry.BMCaps;
import net.sodiumstudio.dwmg.entities.projectile.MagicalGelBallEntity;
import net.sodiumstudio.dwmg.registries.DwmgItems;
import net.sodiumstudio.nautils.Wrapped;

public class MagicalGelBallItem extends Item
{

	public MagicalGelBallItem(Properties pProperties)
	{
		super(pProperties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
		ItemStack itemstack = pPlayer.getItemInHand(pHand);
		Wrapped<Boolean> noUse = new Wrapped<>(false);
		pPlayer.getCapability(BMCaps.CAP_BM_PLAYER).ifPresent(c -> {
			if (c.getNbt().contains("magical_gel_ball_no_use"))
				noUse.set(true);
		});
		if (noUse.get())
			return InteractionResultHolder.pass(itemstack);
		pLevel.playSound((Player) null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.SNOWBALL_THROW,
				SoundSource.NEUTRAL, 0.5F, 0.4F / (pLevel.getRandom().nextFloat() * 0.4F + 0.8F));
		if (!pLevel.isClientSide)
		{
			MagicalGelBallEntity snowball = new MagicalGelBallEntity(pLevel, pPlayer);
			snowball.setItem(itemstack);
			snowball.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, 1.5F, 1.0F);
			pLevel.addFreshEntity(snowball);
		}

		pPlayer.awardStat(Stats.ITEM_USED.get(this));
		if (!pPlayer.getAbilities().instabuild && !pLevel.isClientSide)
		{
			itemstack.shrink(1);
		}

		return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
	}
	
	
}
