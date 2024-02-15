package net.sodiumstudio.dwmg.subsystem.baublesystem.baubles;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.BaubleAttributeModifier;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.BaubleBehavior;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.BaubleEquippingCondition;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.BaubleProcessingArgs;
import net.sodiumstudio.dwmg.entities.hmag.HmagNecroticReaperEntity;

public class EnderManHandBlockBaubleBehavior extends BaubleBehavior
{

	public EnderManHandBlockBaubleBehavior(ResourceLocation key)
	{
		super((item, itemstack) -> (item instanceof BlockItem), key, BaubleEquippingCondition.of(
				args -> args.slotKey().equals("enderman_hand_block")));
	}
	
	@Override
	public BaubleAttributeModifier[] getDuplicatableModifiers(BaubleProcessingArgs arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaubleAttributeModifier[] getNonDuplicatableModifiers(Mob arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onEquipped(BaubleProcessingArgs arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSlotTick(BaubleProcessingArgs arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void preSlotTick(BaubleProcessingArgs arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void slotTick(BaubleProcessingArgs arg0) {
		// TODO Auto-generated method stub

	}

}
