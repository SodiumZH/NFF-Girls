package net.sodiumstudio.dwmg.events.hooks;

import com.github.mechalopa.hmag.world.entity.JackFrostEntity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.Event.HasResult;

public class DwmgHooks
{

	public static class PeachWoodSwordForceHurtEvent extends Event
	{
		public final Player player;
		public final Mob target;
		public float newDamage;
		public final float oldDamage;
		public PeachWoodSwordForceHurtEvent(Player player, Mob mob, float oldDamage, float expectedNewDamage)
		{
			this.player = player;
			this.target = mob;
			this.oldDamage = oldDamage;
			this.newDamage = expectedNewDamage;
		}
	}
	
	@Cancelable
	public static class JackFrostCheckMeltingBiomeEvent extends LivingEvent
	{

		public JackFrostCheckMeltingBiomeEvent(LivingEntity entity)
		{
			super(entity);
		}
		
		@Override
		public JackFrostEntity getEntity()
		{
			return ((JackFrostEntity)(super.getEntity()));
		}
		
	}
}
