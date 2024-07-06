package net.sodiumstudio.dwmg.subsystem.baublesystem.baubles;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.BaubleAttributeModifier;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.BaubleEquippingCondition;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.BaubleProcessingArgs;
import net.sodiumstudio.dwmg.entities.IDwmgBefriendedSunSensitiveMob;
import net.sodiumstudio.dwmg.registries.DwmgConfigs;
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
					Attributes.MAX_HEALTH, 10d * DwmgConfigs.ValueCache.Baubles.BAUBLE_MAX_HP_BOOSTING_SCALE,
					Attributes.ATTACK_DAMAGE, 3d * DwmgConfigs.ValueCache.Baubles.BAUBLE_ATK_BOOSTING_SCALE);
		}
		case 2:
		{
			return BaubleAttributeModifier.makeModifiers(
					Attributes.MAX_HEALTH, 15d * DwmgConfigs.ValueCache.Baubles.BAUBLE_MAX_HP_BOOSTING_SCALE,
					Attributes.ATTACK_DAMAGE, 5d * DwmgConfigs.ValueCache.Baubles.BAUBLE_ATK_BOOSTING_SCALE);
		}
		case 3:
		{
			return BaubleAttributeModifier.makeModifiers(
					Attributes.MAX_HEALTH, 25d * DwmgConfigs.ValueCache.Baubles.BAUBLE_MAX_HP_BOOSTING_SCALE,
					Attributes.ATTACK_DAMAGE, 8d * DwmgConfigs.ValueCache.Baubles.BAUBLE_ATK_BOOSTING_SCALE);
		}
		case 4:
		{
			return BaubleAttributeModifier.makeModifiers(
					Attributes.MAX_HEALTH, 35d * DwmgConfigs.ValueCache.Baubles.BAUBLE_MAX_HP_BOOSTING_SCALE,
					Attributes.ATTACK_DAMAGE, 12d * DwmgConfigs.ValueCache.Baubles.BAUBLE_ATK_BOOSTING_SCALE);
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
				args.user() instanceof IDwmgBefriendedSunSensitiveMob 
				|| args.user().getType().is(DwmgTags.CAN_EQUIP_SOUL_AMULET)));
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
		switch (this.tier)
		{
		case 1:
		case 2:
		case 3: break;
		case 4:
		{
			arg0.user().heal(DwmgConfigs.ValueCache.Baubles.BAUBLE_HEALTH_RECOVERY_SCALE * 0.1f / 20f);
			break;
		}
		default:
		{
			throw this.unsupportedTier();
		}
		}
	}

	@Override
	public ResourceLocation getBaubleRegistryKeyUnsuffixed() {
		return new ResourceLocation("dwmg:soul_amulet");
	}

	@Override
	public BaubleAttributeModifier[] getNonDuplicatableModifiers(Mob mob) {
		return null;
	}

}
