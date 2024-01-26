package net.sodiumstudio.dwmg.subsystem.baublesystem.baubles;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.BaubleAttributeModifier;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.BaubleBehavior;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.BaubleEquippingCondition;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.BaubleProcessingArgs;
import net.sodiumstudio.dwmg.entities.hmag.HmagNecroticReaperEntity;

public class NecroticReaperMainHandHoeBaubleBehavior extends BaubleBehavior
{

	public NecroticReaperMainHandHoeBaubleBehavior(ResourceLocation key)
	{
		super((item, itemstack) -> (item instanceof HoeItem), key, BaubleEquippingCondition.of(
				args -> args.user() instanceof HmagNecroticReaperEntity && args.slotKey().equals("main_hand")));
	}

	@Override
	public void onEquipped(BaubleProcessingArgs args) {
	}

	@Override
	public void preSlotTick(BaubleProcessingArgs args) {
	}

	@Override
	public void postSlotTick(BaubleProcessingArgs args) {
		// TODO Auto-generated method stub

	}

	@Override
	public void slotTick(BaubleProcessingArgs args) {
	}

	@Override
	public BaubleAttributeModifier[] getNonDuplicatableModifiers(Mob mob) {
		return null;
	}

	/*@Override
	public BaubleAttributeModifier[] getDuplicatableModifiers(BaubleProcessingArgs args) {
		double atk = getHoeAtk(args.baubleItemStack().getItem())
		return BaubleAttributeModifier.makeModifiers(Attributes.ATTACK_DAMAGE, //getHoeAtk)
	}*/

	private static int getHoeAtk(int lv)
	{
		if (lv < 0)
			return 0;
		int[] seq = {1, 2, 3, 5, 8, 13, 21, 34};
		if (lv < 8)
			return seq[lv];
		else return seq[7];
	}

	@SuppressWarnings("deprecation")
	@Override
	public BaubleAttributeModifier[] getDuplicatableModifiers(BaubleProcessingArgs args) {
		int lv = Math.round(((HoeItem)(args.baubleItemStack().getItem())).getTier().getLevel());
		double atk = (double)getHoeAtk(lv) * (1d + (double)EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY, args.baubleItemStack()) * 0.1d);
		return new BaubleAttributeModifier[] {
				new BaubleAttributeModifier(Attributes.ATTACK_DAMAGE, atk, AttributeModifier.Operation.ADDITION)
		};
	}
	
}
