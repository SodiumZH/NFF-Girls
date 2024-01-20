package net.sodiumstudio.dwmg.subsystem.baublesystem.baubles;

import net.minecraft.resources.ResourceLocation;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.BaubleAttributeModifier;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.BaubleEquippingCondition;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.BaubleProcessingArgs;
import net.sodiumstudio.dwmg.registries.DwmgTags;
import net.sodiumstudio.dwmg.subsystem.baublesystem.DwmgDedicatedBaubleItem;

public class PoisonousThornBaubleItem extends DwmgDedicatedBaubleItem
{

	public PoisonousThornBaubleItem(String additionalKey, int tier, Properties pProperties)
	{
		super(additionalKey, tier, pProperties);
	}

	@Override
	public void slotTick(BaubleProcessingArgs args) {
	}

	@Override
	public BaubleAttributeModifier[] getDuplicatableModifiers(BaubleProcessingArgs args) {
		return null;
	}

	@Override
	public ResourceLocation getBaubleRegistryKeyUnsuffixed() {
		return new ResourceLocation("dwmg:poisonous_thorn");
	}

	@Override
	public BaubleEquippingCondition getEquippingCondition()
	{
		return BaubleEquippingCondition.of(args -> args.user().getMob().getType().is(DwmgTags.CAN_EQUIP_POISONOUS_THORN));
	}

}
