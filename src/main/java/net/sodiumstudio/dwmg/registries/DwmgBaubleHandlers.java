
package net.sodiumstudio.dwmg.registries;

import java.util.function.Supplier;

import net.minecraftforge.fml.common.Mod;
import net.sodiumstudio.befriendmobs.item.baublesystem.BaubleHandler;
import net.sodiumstudio.befriendmobs.item.baublesystem.BaubleHandlerRegistry;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.entities.item.baublesystem.handlers.BaubleHandlerCrimsonSlaughterer;
import net.sodiumstudio.dwmg.entities.item.baublesystem.handlers.BaubleHandlerDrowned;
import net.sodiumstudio.dwmg.entities.item.baublesystem.handlers.BaubleHandlerEmpty;
import net.sodiumstudio.dwmg.entities.item.baublesystem.handlers.BaubleHandlerEnderExecutor;
import net.sodiumstudio.dwmg.entities.item.baublesystem.handlers.BaubleHandlerGeneral;
import net.sodiumstudio.dwmg.entities.item.baublesystem.handlers.BaubleHandlerHornet;
import net.sodiumstudio.dwmg.entities.item.baublesystem.handlers.BaubleHandlerNecroticReaper;
import net.sodiumstudio.dwmg.entities.item.baublesystem.handlers.BaubleHandlerUndead;

@Mod.EventBusSubscriber(modid = Dwmg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DwmgBaubleHandlers {
	
	public static final BaubleHandlerEmpty EMPTY = register("empty", BaubleHandlerEmpty::new);
	public static final BaubleHandlerGeneral GENERAL = register("general", BaubleHandlerGeneral::new);
	public static final BaubleHandlerUndead UNDEAD = register("undead", BaubleHandlerUndead::new);
	public static final BaubleHandlerDrowned DROWNED = register("drowned", BaubleHandlerDrowned::new);
	public static final BaubleHandlerHornet HORNET = register("hornet", BaubleHandlerHornet::new);
	public static final BaubleHandlerNecroticReaper NECROTIC_REAPER = register("necrotic_reaper", BaubleHandlerNecroticReaper::new);
	public static final BaubleHandlerCrimsonSlaughterer CRIMSON_SLAUGHTERER = register("crimson_slaughterer", BaubleHandlerCrimsonSlaughterer::new);
	
	protected static <T extends BaubleHandler> T register(String name, Supplier<T> supplier)
	{
		return BaubleHandlerRegistry.create(Dwmg.MOD_ID, "bauble_handler_" + name, supplier);
	}
}
