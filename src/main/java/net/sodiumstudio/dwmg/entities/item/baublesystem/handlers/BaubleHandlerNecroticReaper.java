package net.sodiumstudio.dwmg.entities.item.baublesystem.handlers;

import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.sodiumstudio.befriendmobs.item.baublesystem.IBaubleEquipable;

public class BaubleHandlerNecroticReaper extends BaubleHandlerUndead
{

	@Override
	public boolean isAccepted(Item item, String key, IBaubleEquipable mob)
	{
		if (key.equals("main_hand"))
		{
			return !(item instanceof TieredItem) || (item instanceof HoeItem);
		}
		else return super.isAccepted(item, key, mob);
	}
	
	// Atk added by main-hand hoe from tier atk level.
	// Using Fibonacci sequence
	protected static int getHoeAtk(int lv)
	{
		if (lv < 0)
			return 0;
		int[] seq = {1, 2, 3, 5, 8, 13, 21, 34};
		if (lv < 8)
			return seq[lv];
		else return seq[7];
	}
	
	@Override
	public void refreshBaubleEffect(String slotKey, ItemStack bauble, IBaubleEquipable owner) {
		if (slotKey.equals("main_hand"))
		{
			if (bauble.getItem() instanceof HoeItem hoe)
			{
				int lv = Math.round(hoe.getTier().getAttackDamageBonus());
				double atk = (double)getHoeAtk(lv) * (1d + (double)EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY, bauble) * 0.1d);
				owner.addBaubleModifier(slotKey, "hoe_atk", Attributes.ATTACK_DAMAGE, atk, Operation.ADDITION);
			}
		}
		else {
			super.refreshBaubleEffect(slotKey, bauble, owner);
		}
	}
}

