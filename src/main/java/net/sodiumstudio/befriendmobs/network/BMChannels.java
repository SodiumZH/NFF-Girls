package net.sodiumstudio.befriendmobs.network;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.sodiumstudio.befriendmobs.BefriendMobs;
import net.sodiumstudio.befriendmobs.entity.befriended.CBefriendedMobData;
import net.sodiumstudio.nautils.NaNetworkUtils;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BMChannels {
	public static SimpleChannel BM_CHANNEL;
    public static final String VERSION = "1.0";
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void registerMessage() {
    	
    	BM_CHANNEL = NaNetworkUtils.newChannel(BefriendMobs.MOD_ID, "bm_channel", VERSION);
    	NaNetworkUtils.registerDefaultClientGamePacket(nextID(), BM_CHANNEL, ClientboundBefriendedGuiOpenPacket.class);
    	NaNetworkUtils.registerDefaultClientGamePacket(nextID(), BM_CHANNEL, ClientboundBefriendingInitPacket.class);
    	NaNetworkUtils.registerDefaultClientGamePacket(nextID(), BM_CHANNEL, CBefriendedMobData.ClientboundDataSyncPacket.class);
    	/*BM_CHANNEL = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(BefriendMobs.MOD_ID, "bm_channel"),
                () -> {
                    return VERSION;
                },
                (version) -> {
                    return version.equals(VERSION);
                },
                (version) -> {
                    return version.equals(VERSION);
                });
    	BM_CHANNEL.registerMessage(
                nextID(),
                ClientboundBefriendedGuiOpenPacket.class,
                (pack, buffer) -> {
                    pack.write(buffer);
                },
                (buffer) -> {
                    return new ClientboundBefriendedGuiOpenPacket(buffer);
                },
                (pack, ctx) -> {
                	ctx.get().enqueueWork(() ->
                	{ 
                		pack.handle(Minecraft.getInstance().getConnection());
                	});
                	ctx.get().setPacketHandled(true);
                }
        );*/
    }
    
    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
    	event.enqueueWork(() -> 
    	{
    		registerMessage();
    	});
    }
    
}
