package net.sodiumstudio.dwmg.entities.handlers.hmag;

import java.util.Map;
import java.util.function.Supplier;

import net.sodiumstudio.nautils.ContainerHelper;
import net.sodiumstudio.nautils.containers.MapPair;
import net.sodiumstudio.nautils.math.RndUtil;

public class HandlerJackFrost extends HandlerItemDropping
{

	@Override
	public Map<String, Supplier<Double>> getDeltaProc() {
		return ContainerHelper.<String, Supplier<Double>>mapOf(
				MapPair.of("minecraft:blue_ice", () -> RndUtil.rndRangedDouble(0.03, 0.06)),
				MapPair.of("minecraft:lapis_lazuli", () -> RndUtil.rndRangedDouble(0.03, 0.06)),
				MapPair.of("minecraft:emerald", () -> RndUtil.rndRangedDouble(0.04, 0.08)),
				MapPair.of("minecraft:diamond", () -> RndUtil.rndRangedDouble(0.06, 0.10)),
				MapPair.of("hmag:cureberry", () -> RndUtil.rndRangedDouble(0.08, 0.12)),
				MapPair.of("hmag:randomberry", () -> RndUtil.rndRangedDouble(0.08, 0.12)),
				MapPair.of("hmag:exp_berry", () -> RndUtil.rndRangedDouble(0.08, 0.12)),
				MapPair.of("minecraft:golden_apple", () -> RndUtil.rndRangedDouble(0.10, 0.15)),
				MapPair.of("hmag:golden_tropical_fish", () -> RndUtil.rndRangedDouble(0.10, 0.15)),
				MapPair.of("twilightforest:ice_bomb", () -> RndUtil.rndRangedDouble(0.10, 0.15))
				);
	}

	@Override
	public int getHoldingItemTime() {
		return 5 * 20;
	}

}
