package net.sodiumstudio.dwmg.network;

import com.mojang.logging.LogUtils;

import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.sodiumstudio.dwmg.entities.gifting.GiftingRegistry;
import net.sodiumstudio.dwmg.entities.gifting.ServerboundGiftingInputPacket;

public class DwmgServerGamePacketHandler
{

	public static void handleGiftingInput(ServerboundGiftingInputPacket packet, ServerGamePacketListener listener)
	{
		if (listener instanceof ServerGamePacketListenerImpl listenerImpl)
		{
			PacketUtils.ensureRunningOnSameThread(packet, listener, listenerImpl.player.getLevel());
			GiftingRegistry.getAllAcceptingItems(null)
		}
		else
		{
			LogUtils.getLogger().error("DwmgServerGamePacketHandler: ServerGamePacketListener is not vanilla. Skipped.");
		}
	}
}
