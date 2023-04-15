package net.sodiumstudio.dwmg.dwmgcontent.entities.item.baublesystem;

import net.minecraftforge.fml.common.Mod;
import net.sodiumstudio.dwmg.dwmgcontent.Dwmg;
import net.sodiumstudio.dwmg.dwmgcontent.entities.item.baublesystem.handlers.BaubleHandlerDrowned;
import net.sodiumstudio.dwmg.dwmgcontent.entities.item.baublesystem.handlers.BaubleHandlerEnderExecutor;
import net.sodiumstudio.dwmg.dwmgcontent.entities.item.baublesystem.handlers.BaubleHandlerVanillaUndead;

@Mod.EventBusSubscriber(modid = Dwmg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DwmgBaubleHandlers {

	public static final BaubleHandlerVanillaUndead VANILLA_UNDEAD = new BaubleHandlerVanillaUndead();
	public static final BaubleHandlerEnderExecutor ENDER_EXECUTOR = new BaubleHandlerEnderExecutor();
	public static final BaubleHandlerDrowned DROWNED = new BaubleHandlerDrowned();
}
