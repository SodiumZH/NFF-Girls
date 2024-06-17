package net.sodiumstudio.dwmg.subsystem.baublesystem.baubles;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.BaubleAttributeModifier;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.BaubleEquippingCondition;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.BaubleProcessingArgs;
import net.sodiumstudio.dwmg.registries.DwmgConfigs;
import net.sodiumstudio.dwmg.subsystem.baublesystem.DwmgDedicatedBaubleItem;

public class ResistanceAmuletBaubleItem extends DwmgDedicatedBaubleItem
{
	
	public ResistanceAmuletBaubleItem(String additionalKey, int tier, Properties pProperties)
	{
		super(additionalKey, tier, pProperties);
	}

	@Override
	public BaubleEquippingCondition getEquippingCondition() {
		return BaubleEquippingCondition.always();
	}

	@Override
	public void onEquipped(BaubleProcessingArgs args) {

	}

	@Override
	public void preSlotTick(BaubleProcessingArgs args) {
	}

	@Override
	public void postSlotTick(BaubleProcessingArgs args) {
	}

	@Override
	public void slotTick(BaubleProcessingArgs args) {
	}

	@Override
	public BaubleAttributeModifier[] getNonDuplicatableModifiers(Mob mob) {
		return null;
	}

	@Override
	public BaubleAttributeModifier[] getDuplicatableModifiers(BaubleProcessingArgs args) {
		switch (this.tier)
		{
		case 1:
		{
			return BaubleAttributeModifier.makeModifiers(				 
					Attributes.MAX_HEALTH, 15d * DwmgConfigs.ValueCache.Baubles.BAUBLE_MAX_HP_BOOSTING_SCALE, 
					Attributes.ARMOR, 4d * DwmgConfigs.ValueCache.Baubles.BAUBLE_ARMOR_BOOSTING_SCALE,
					Attributes.MOVEMENT_SPEED, -0.1d, "mb");
			/*return new BaubleAttributeModifier[] {
				new BaubleAttributeModifier(Attributes.ARMOR, 4d, AttributeModifier.Operation.ADDITION),
				new BaubleAttributeModifier(Attributes.MAX_HEALTH, 15d, AttributeModifier.Operation.ADDITION),
				new BaubleAttributeModifier(Attributes.MOVEMENT_SPEED, -0.1d, AttributeModifier.Operation.MULTIPLY_BASE)};*/
		}
		case 2:
		{
			return BaubleAttributeModifier.makeModifiers(
					Attributes.ARMOR, 6d * DwmgConfigs.ValueCache.Baubles.BAUBLE_ARMOR_BOOSTING_SCALE,
					Attributes.MAX_HEALTH, 25d * DwmgConfigs.ValueCache.Baubles.BAUBLE_MAX_HP_BOOSTING_SCALE, 
					Attributes.MOVEMENT_SPEED, -0.15d, "mb");
			/*return new BaubleAttributeModifier[] {
					new BaubleAttributeModifier(Attributes.ARMOR, 6d, AttributeModifier.Operation.ADDITION),
					new BaubleAttributeModifier(Attributes.MAX_HEALTH, 25d, AttributeModifier.Operation.ADDITION),
					new BaubleAttributeModifier(Attributes.MOVEMENT_SPEED, -0.15d, AttributeModifier.Operation.MULTIPLY_BASE)};*/
		}
		default:
		{
			throw this.unsupportedTier();
		}
		}
	}

	@Override
	public ResourceLocation getBaubleRegistryKeyUnsuffixed() {
		return new ResourceLocation("dwmg:resistance_amulet");
	}

}
