package net.sodiumstudio.dwmg.network;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.sodiumstudio.befriendmobs.BefriendMobs;
import net.sodiumstudio.befriendmobs.network.ClientboundBefriendedGuiOpenPacket;
import net.sodiumstudio.dwmg.Dwmg;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DwmgChannels {
	public static SimpleChannel DWMG_CHANNEL;
    public static final String VERSION = "1.0";
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    @SuppressWarnings("resource")
	public static void registerMessage() {
    	DWMG_CHANNEL = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(Dwmg.MOD_ID, "dwmg_network_channel"),
                () -> {
                    return VERSION;
                },
                (version) -> {
                    return version.equals(VERSION);
                },
                (version) -> {
                    return version.equals(VERSION);
                });
    	DWMG_CHANNEL.registerMessage(
                nextID(),
                ClientboundDwmgMobSyncPacket.class,
                (pack, buffer) -> {
                    pack.write(buffer);
                },
                (buffer) -> {
                    return new ClientboundDwmgMobSyncPacket(buffer);
                },
                (pack, ctx) -> {
                	ctx.get().enqueueWork(() ->
                	{ 
                		pack.handle(Minecraft.getInstance().getConnection());
                	});
                	ctx.get().setPacketHandled(true);
                }
        );
    }
    
    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        registerMessage();
    }
    
}
