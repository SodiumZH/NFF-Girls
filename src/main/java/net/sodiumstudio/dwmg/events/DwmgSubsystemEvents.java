package net.sodiumstudio.dwmg.events;

import java.util.UUID;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.fml.common.Mod;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.entities.capabilities.CFavorabilityHandler;

@Mod.EventBusSubscriber(modid = Dwmg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DwmgSubsystemEvents
{

	protected static final UUID FAV_ATK_MODIFIER_UUID = UUID.fromString("0e570979-9f96-4559-b31e-93500e69da07");
	
	public static void onFavorabilityChangeValue(CFavorabilityHandler.ChangeValueEvent event)
	{
		// Inspired by Girl's Frontline
		event.mob.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(FAV_ATK_MODIFIER_UUID);
		double atkmod = 1;
		if (event.toValue < 5d)
			atkmod = 0.5;
		else if (event.toValue < 20d)
			atkmod = 0.7;
		else if (event.toValue < 50d)
			atkmod = 0.9;
		else if (event.toValue < 80d)
			atkmod = 1.0;
		else if (event.toValue < 99.9999d)
			atkmod = 1.1;
		else atkmod = 1.2;
		
		event.mob.getAttribute(Attributes.ATTACK_DAMAGE).addTransientModifier(new AttributeModifier(FAV_ATK_MODIFIER_UUID,
				"favorability_atk", atkmod - 1, AttributeModifier.Operation.MULTIPLY_TOTAL));
		
	}
	
}
