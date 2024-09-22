package net.sodiumzh.nff.girls.entity.tamingprocess.hmag;

import java.util.Map;
import java.util.function.Supplier;

import net.sodiumzh.nautils.containers.MapPair;
import net.sodiumzh.nautils.statics.NaUtilsMathStatics;
import net.sodiumzh.nautils.statics.NaUtilsContainerStatics;

public class HmagRedcapTamingProcess extends NFFGirlsItemDroppingTamingProcess
{

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Supplier<Double>> getDeltaProc() {
		return NaUtilsContainerStatics.<String, Supplier<Double>>mapOf(
				MapPair.of("minecraft:wheat", () -> NaUtilsMathStatics.rndRangedDouble(0.02, 0.04)),
				MapPair.of("minecraft:bread", () -> NaUtilsMathStatics.rndRangedDouble(0.03, 0.06)),
				MapPair.of("hmag:cureberry", () -> NaUtilsMathStatics.rndRangedDouble(0.08, 0.12)),
				MapPair.of("hmag:randomberry", () -> NaUtilsMathStatics.rndRangedDouble(0.08, 0.12)),
				MapPair.of("hmag:exp_berry", () -> NaUtilsMathStatics.rndRangedDouble(0.08, 0.12)),
				MapPair.of("minecraft:golden_apple", () -> NaUtilsMathStatics.rndRangedDouble(0.10, 0.15)),
				MapPair.of("hmag:golden_tropical_fish", () -> NaUtilsMathStatics.rndRangedDouble(0.10, 0.15)),
				MapPair.of("minecraft:iron_axe", () -> NaUtilsMathStatics.rndRangedDouble(0.05, 0.07)),
				MapPair.of("minecraft:golden_axe", () -> NaUtilsMathStatics.rndRangedDouble(0.07, 0.10)),
				MapPair.of("minecraft:diamond_axe", () -> NaUtilsMathStatics.rndRangedDouble(0.12, 0.18)),
				MapPair.of("minecraft:netherite_axe", () -> NaUtilsMathStatics.rndRangedDouble(0.50, 1.00)),
				MapPair.of("twilightforest:maze_map_focus", () -> NaUtilsMathStatics.rndRangedDouble(0.15, 0.30))
				);
	}

	@Override
	public int getHoldingItemTime() {
		return 5 * 20;
	}

}
