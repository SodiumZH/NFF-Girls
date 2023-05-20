package net.sodiumstudio.dwmg.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.sodiumstudio.dwmg.entities.projectile.NecromancerMagicBulletEntity;
import net.sodiumstudio.dwmg.registries.DwmgItems;

public class NecromancerWandItem extends Item
{

	public NecromancerWandItem(Properties pProperties)
	{
		super(pProperties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
	{
		ItemStack stack = player.getItemInHand(usedHand);
		// Don't use if out of duration, leaving 1 (like TF wands)
		if (stack.getDamageValue() >= stack.getMaxDamage() - 1)
		{
			return InteractionResultHolder.fail(stack);
		}
		else
		{
			// Movement velocity vector
			Vec3 velocity = player.getLookAngle().scale(4d);
			Vec3 offset = player.getLookAngle().scale(0.2d);
			NecromancerMagicBulletEntity bullet = new NecromancerMagicBulletEntity(level, player, velocity.x, velocity.y, velocity.z);
			bullet.setPos(player.getX() + offset.x , player.getY() + 0.8d + offset.y, player.getZ() + offset.z);
			bullet.setDamage(0);
			if (player.getItemBySlot(EquipmentSlot.HEAD).is(DwmgItems.NECROMANCER_HAT.get()))
				bullet.hasNecromancerHat = true;
			level.addFreshEntity(bullet);
			stack.hurtAndBreak(1, player, (m) -> {throw new RuntimeException("Necromancer's Wand should not be broken and should leave 1 durability.");});
			player.hurt(DamageSource.MAGIC, 2);
			player.getCooldowns().addCooldown(DwmgItems.NECROMANCER_WAND.get(), 100);
			return InteractionResultHolder.success(stack);
		}
	}

}
