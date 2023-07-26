package net.sodiumstudio.dwmg.entities.handlers.hmag;

import java.util.Map;

import java.util.function.Supplier;

import net.minecraft.nbt.DoubleTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.befriendmobs.entity.capability.CBefriendableMob;
import net.sodiumstudio.nautils.ContainerHelper;
import net.sodiumstudio.nautils.EntityHelper;
import net.sodiumstudio.nautils.containers.MapPair;
import net.sodiumstudio.nautils.math.RndUtil;

public class HandlerSnowCanine extends HandlerItemDropping
{

	@Override
	public void initCap(CBefriendableMob cap)
	{
		super.initCap(cap);
		cap.getNbt().put("strength", DoubleTag.valueOf(0));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Supplier<Double>> getDeltaProc() {
		return ContainerHelper.<String, Supplier<Double>>mapOf(
				MapPair.of("minecraft:cooked_chicken", () -> RndUtil.rndRangedDouble(0.04, 0.06)),
				MapPair.of("minecraft:cooked_rabbit", () -> RndUtil.rndRangedDouble(0.04, 0.06)),
				MapPair.of("minecraft:cooked_mutton", () -> RndUtil.rndRangedDouble(0.04, 0.06)),
				MapPair.of("minecraft:cooked_porkchop", () -> RndUtil.rndRangedDouble(0.05, 0.07)),
				MapPair.of("minecraft:cooked_beef", () -> RndUtil.rndRangedDouble(0.05, 0.07)),
				MapPair.of("hmag:golden_tropical_fish", () -> RndUtil.rndRangedDouble(0.07, 0.10)),
				MapPair.of("hmag:cooked_ravager_meat", () -> RndUtil.rndRangedDouble(0.08, 0.12)),
				MapPair.of("minecraft:golden_apple", () -> RndUtil.rndRangedDouble(0.12, 0.16)),
				MapPair.of("minecraft:enchanted_golden_apple", () -> RndUtil.rndRangedDouble(0.40, 0.70)));
	}

	@Override
	public int getHoldingItemTime() {
		return 5 * 20;
	}

	@Override
	public void serverTick(Mob mob)
	{
		if (CBefriendableMob.getCap(mob) == null)
			return;
		super.serverTick(mob);
		if (CBefriendableMob.getCapNbt(mob).getDouble("strength") >= 1e-5d)
			EntityHelper.addEffectSafe(mob, new MobEffectInstance(MobEffects.DAMAGE_BOOST, 10, (int)(CBefriendableMob.getCapNbt(mob).getDouble("strength") / 0.1)));
		CBefriendableMob.getCapNbt(mob).put("strength", DoubleTag.valueOf(Math.max(CBefriendableMob.getCapNbt(mob).getDouble("strength") - 5e-5d, 0d)));	// decrease by 0.001 per second
	}

	@Override
	public void onConsumeItem(Mob mob, ItemStack item, double deltaProc)
	{
		CBefriendableMob.getCapNbt(mob).put("strength", DoubleTag.valueOf(CBefriendableMob.getCapNbt(mob).getDouble("strength") + deltaProc));
	}
	
}
