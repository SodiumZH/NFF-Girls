package net.sodiumstudio.dwmg.subsystem.baublesystem.baubles;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.BaubleAttributeModifier;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.BaubleProcessingArgs;
import net.sodiumstudio.dwmg.registries.DwmgConfigs;
import net.sodiumstudio.dwmg.subsystem.baublesystem.DwmgDedicatedBaubleItem;

public class CourageAmuletBaubleItem extends DwmgDedicatedBaubleItem
{

	public CourageAmuletBaubleItem(String additionalKey, int tier, Properties pProperties)
	{
		super(additionalKey, tier, pProperties);
	}

	@Override
	public void slotTick(BaubleProcessingArgs args) {
		// TODO Auto-generated method stub

	}
	@Override
	public BaubleAttributeModifier[] getDuplicatableModifiers(BaubleProcessingArgs args) {
		switch (this.tier)
		{
		case 1:
		{
			return BaubleAttributeModifier.makeModifiers(
					Attributes.ATTACK_DAMAGE, 4d * DwmgConfigs.ValueCache.Baubles.BAUBLE_ATK_BOOSTING_SCALE,
					Attributes.MOVEMENT_SPEED, 0.2d, "mb");
		}
		case 2:
		{
			return BaubleAttributeModifier.makeModifiers(
					Attributes.ATTACK_DAMAGE, 6d * DwmgConfigs.ValueCache.Baubles.BAUBLE_ATK_BOOSTING_SCALE,
					Attributes.MOVEMENT_SPEED, 0.3d, "mb");
		}
		default:
		{
			throw this.unsupportedTier();
		}
		}
	}

	@Override
	public ResourceLocation getBaubleRegistryKeyUnsuffixed() {
		return new ResourceLocation("dwmg:courage_amulet");
	}

}
