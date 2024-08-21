package net.sodiumstudio.befriendmobs.bmevents.level;

import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.eventbus.api.Event;
import net.sodiumstudio.befriendmobs.level.CBMLevelModule;
import net.sodiumstudio.befriendmobs.registry.BMCaps;
import net.sodiumstudio.nautils.NaMiscUtils;

public class BMLevelModuleTickStartEvent extends Event
{
	public final ServerLevel level;
	public final CBMLevelModule levelModule;
	
	public BMLevelModuleTickStartEvent(ServerLevel level)
	{
		this.level = level;
		var cap = NaMiscUtils.getValue(level.getCapability(BMCaps.CAP_BM_LEVEL));
		this.levelModule = cap != null ? cap : null;
	}
}
