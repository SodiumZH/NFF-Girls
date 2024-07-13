package net.sodiumstudio.dwmg.subsystem.baublesystem.baubles;

import com.github.mechalopa.hmag.registry.ModItems;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.BaubleAttributeModifier;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.BaubleBehavior;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.BaubleEquippingCondition;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.BaubleProcessingArgs;
import net.sodiumstudio.dwmg.registries.DwmgConfigs;

public class InsomniaFruitBaubleBehavior extends BaubleBehavior
{

	public InsomniaFruitBaubleBehavior(ResourceLocation key, BaubleEquippingCondition equippingCondition)
	{
		super(ModItems.INSOMNIA_FRUIT.get(), key, equippingCondition);
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
	public BaubleAttributeModifier[] getDuplicatableModifiers(BaubleProcessingArgs args) {
		return null;
	}

	@Override
	public BaubleAttributeModifier[] getNonDuplicatableModifiers(Mob mob) {
		return new BaubleAttributeModifier[] {
				new BaubleAttributeModifier(Attributes.ATTACK_DAMAGE, 8d * DwmgConfigs.ValueCache.Baubles.BAUBLE_ATK_BOOSTING_SCALE,
						 AttributeModifier.Operation.ADDITION).setAdditionalCondition(args -> args.user().level().isNight()),
				new BaubleAttributeModifier(Attributes.MAX_HEALTH, 60d * DwmgConfigs.ValueCache.Baubles.BAUBLE_MAX_HP_BOOSTING_SCALE,
						 AttributeModifier.Operation.ADDITION).setAdditionalCondition(args -> args.user().level().isNight())
		};
	}

}
