package net.sodiumstudio.dwmg.events.hooks;

import com.github.mechalopa.hmag.world.entity.JackFrostEntity;
import com.github.mechalopa.hmag.world.entity.MeltyMonsterEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.Event.HasResult;
import net.sodiumstudio.nautils.events.NaUtilsLivingEvent;
import net.sodiumstudio.dwmg.mixins.*;

public class DwmgHooks
{

	/** Fired on Peach Wood Sword damaging by percentage */
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
	
	/** Fired on Jack o'Frost check melting biome. Implemented via {@link DwmgJackFrostEntityMixin}. 
	 * If cancelled it will not be regarded as a melting biome.
	 */
	@Cancelable
	public static class JackFrostCheckMeltingBiomeEvent extends NaUtilsLivingEvent<JackFrostEntity>
	{
		public JackFrostCheckMeltingBiomeEvent(JackFrostEntity entity) {
			super(entity);
		}
	}
	
	/** Fired on Melty Monster setting fire. Implemented via {@link DwmgMeltyMonsterEntityMixin}.
	 */
	@Cancelable
	public static class MeltyMonsterSetFireEvent extends NaUtilsLivingEvent<MeltyMonsterEntity>
	{
		public final BlockPos blockpos; 
		public final BlockState blockstate;
		
		public MeltyMonsterSetFireEvent(MeltyMonsterEntity entity, BlockPos pos, BlockState blockstate)
		{
			super(entity);
			this.blockpos = pos;
			this.blockstate = blockstate;
		}
	}
}
