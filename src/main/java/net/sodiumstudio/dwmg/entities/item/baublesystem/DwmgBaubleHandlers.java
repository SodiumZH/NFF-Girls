package net.sodiumstudio.dwmg.entities.item.baublesystem;

import net.minecraftforge.fml.common.Mod;

import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.entities.item.baublesystem.handlers.BaubleHandlerDrowned;
import net.sodiumstudio.dwmg.entities.item.baublesystem.handlers.BaubleHandlerEmpty;
import net.sodiumstudio.dwmg.entities.item.baublesystem.handlers.BaubleHandlerEnderExecutor;
import net.sodiumstudio.dwmg.entities.item.baublesystem.handlers.BaubleHandlerGeneral;
import net.sodiumstudio.dwmg.entities.item.baublesystem.handlers.BaubleHandlerHornet;
import net.sodiumstudio.dwmg.entities.item.baublesystem.handlers.BaubleHandlerNecroticReaper;
import net.sodiumstudio.dwmg.entities.item.baublesystem.handlers.BaubleHandlerUndead;

@Mod.EventBusSubscriber(modid = Dwmg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DwmgBaubleHandlers {
	
	public static final BaubleHandlerEmpty EMPTY = new BaubleHandlerEmpty();
	public static final BaubleHandlerGeneral GENERAL = new BaubleHandlerGeneral();
	public static final BaubleHandlerUndead UNDEAD = new BaubleHandlerUndead();
	public static final BaubleHandlerEnderExecutor ENDER_EXECUTOR = new BaubleHandlerEnderExecutor();
	public static final BaubleHandlerDrowned DROWNED = new BaubleHandlerDrowned();
	public static final BaubleHandlerHornet HORNET = new BaubleHandlerHornet();
	public static final BaubleHandlerNecroticReaper NECROTIC_REAPER = new BaubleHandlerNecroticReaper();

}
