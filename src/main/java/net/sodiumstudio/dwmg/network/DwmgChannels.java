package net.sodiumstudio.dwmg.network;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import net.sodiumstudio.befriendmobs.util.NetworkHelper;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.entities.capabilities.CFavorabilityHandler;
import net.sodiumstudio.dwmg.entities.capabilities.CLevelHandler;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DwmgChannels {
	public static SimpleChannel SYNC_CHANNEL;
    public static final String VERSION = "1.0";
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    @SuppressWarnings("resource")
	public static void registerMessage() {
    	SYNC_CHANNEL = NetworkHelper.newChannel(Dwmg.MOD_ID, "dwmg_sync_channel");
    	NetworkHelper.registerDefaultClientGamePacket(nextID(), SYNC_CHANNEL, CFavorabilityHandler.SyncPacket.class);
    	NetworkHelper.registerDefaultClientGamePacket(nextID(), SYNC_CHANNEL, CLevelHandler.SyncPacket.class);
    }
    
    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        registerMessage();
    }
    
}