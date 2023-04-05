package net.sodiumstudio.dwmg.dwmgcontent.entities.item.baublesystem;

import java.util.UUID;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class DwmgBaubleHandlerAttributeModifiers
{
	public static final UUID DEATH_CRYSTAL_BAUBLE_EFFECT_MODIFIER_UUID = UUID.fromString("174079eb-dda2-4d0b-bf7a-ece38340b871");
	public static final AttributeModifier DEATH_CRYSTAL_BAUBLE_EFFECT_MODIFIER = 
			new AttributeModifier(DEATH_CRYSTAL_BAUBLE_EFFECT_MODIFIER_UUID, "death_crystal_bauble_effect_modifier", 
					2.0d, AttributeModifier.Operation.ADDITION);
	
	public static final UUID NETHERITE_INGOT_BAUBLE_EFFECT_MODIFIER_UUID = UUID.fromString("61b77e0a-ab35-4851-9d4d-9d2d520d8e6c");
	public static final AttributeModifier NETHERITE_INGOT_BAUBLE_EFFECT_MODIFIER = 
			new AttributeModifier(NETHERITE_INGOT_BAUBLE_EFFECT_MODIFIER_UUID, "netherite_ingot_bauble_effect_modifier", 
					2.0d, AttributeModifier.Operation.ADDITION);
}
