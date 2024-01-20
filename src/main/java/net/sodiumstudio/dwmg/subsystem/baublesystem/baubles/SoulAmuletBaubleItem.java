package net.sodiumstudio.dwmg.subsystem.baublesystem.baubles;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.BaubleAttributeModifier;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.BaubleEquippingCondition;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.BaubleProcessingArgs;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.CBaubleEquippableMob;
import net.sodiumstudio.dwmg.entities.IDwmgBefriendedSunSensitiveMob;
import net.sodiumstudio.dwmg.registries.DwmgTags;
import net.sodiumstudio.dwmg.subsystem.baublesystem.DwmgDedicatedBaubleItem;

public class SoulAmuletBaubleItem extends DwmgDedicatedBaubleItem
{

	public SoulAmuletBaubleItem(String additionalKey, int tier, Properties pProperties)
	{
		super(additionalKey, tier, pProperties);
	}

	@Override
	public BaubleAttributeModifier[] getDuplicatableModifiers(BaubleProcessingArgs arg0) {
		switch (this.tier)
		{
		case 1:
		{
			return BaubleAttributeModifier.makeModifiers(
					Attributes.MAX_HEALTH, 10d,
					Attributes.ATTACK_DAMAGE, 3d);
			/*return new BaubleAttributeModifier[] {
				new BaubleAttributeModifier(Attributes.MAX_HEALTH, 10d, AttributeModifier.Operation.ADDITION),
				new BaubleAttributeModifier(Attributes.ATTACK_DAMAGE, 3d, AttributeModifier.Operation.ADDITION)};*/
		}
		case 2:
		{
			return BaubleAttributeModifier.makeModifiers(
					Attributes.MAX_HEALTH, 15d,
					Attributes.ATTACK_DAMAGE, 5d);
			/*return new BaubleAttributeModifier[] {
				new BaubleAttributeModifier(Attributes.MAX_HEALTH, 15d, AttributeModifier.Operation.ADDITION),
				new BaubleAttributeModifier(Attributes.ATTACK_DAMAGE, 5d, AttributeModifier.Operation.ADDITION)};*/
		}
		default:
		{
			throw this.unsupportedTier();
		}
		}
	}

	@Override
	public BaubleEquippingCondition getEquippingCondition() {
		return BaubleEquippingCondition.of(args -> (
				args.user().getMob() instanceof IDwmgBefriendedSunSensitiveMob 
				|| args.user().getMob().getType().is(DwmgTags.CAN_EQUIP_SOUL_AMULET)));
	}

	@Override
	public void onEquipped(BaubleProcessingArgs arg0) {
	}

	@Override
	public void postSlotTick(BaubleProcessingArgs arg0) {
	}

	@Override
	public void preSlotTick(BaubleProcessingArgs arg0) {
	}

	@Override
	public void slotTick(BaubleProcessingArgs arg0) {
	}

	@Override
	public ResourceLocation getBaubleRegistryKeyUnsuffixed() {
		return new ResourceLocation("dwmg:soul_amulet");
	}

	@Override
	public BaubleAttributeModifier[] getNonDuplicatableModifiers(CBaubleEquippableMob mob) {
		return null;
	}

}
