package net.sodiumstudio.dwmg.entities.handlers.hmag;

import com.github.mechalopa.hmag.world.entity.HarpyEntity;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.dwmg.entities.hmag.HmagHarpyEntity;

public class HandlerHarpy extends HandlerSnowCanine
{

	@Override
	public IBefriendedMob befriend(Player player, Mob target)
	{
		HarpyEntity.Variant variant = HarpyEntity.Variant.byId(0);
		if (target instanceof HarpyEntity h)
		{
			variant = h.getVariant();
		}
		IBefriendedMob mob = super.befriend(player, target);
		if (mob instanceof HmagHarpyEntity h && variant.getId() >= 0)
		{
			h.setVariant(variant);
		}
		return mob;
	}
	
}
