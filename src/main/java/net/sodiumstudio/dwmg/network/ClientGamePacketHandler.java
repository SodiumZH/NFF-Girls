package net.sodiumstudio.dwmg.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.sodiumstudio.dwmg.entities.capabilities.CFavorabilityHandler;
import net.sodiumstudio.dwmg.entities.capabilities.CLevelHandler;
import net.sodiumstudio.dwmg.registries.DwmgCapabilities;

public class ClientGamePacketHandler
{

	public static void handleFavorabilityHandlerSync(CFavorabilityHandler.SyncPacket packet, ClientGamePacketListener listener)
	{
		@SuppressWarnings("resource")
		Minecraft mc = Minecraft.getInstance();
		PacketUtils.ensureRunningOnSameThread(packet, listener, mc);
		Entity entity = mc.level.getEntity(packet.entityId);
		// Needs a null check here as sometimes it may invoke on null??
		if (entity != null)
		{
			entity.getCapability(DwmgCapabilities.CAP_FAVORABILITY_HANDLER).ifPresent((cap) ->
			{
				cap.setFavorability(packet.favorability);
				cap.setMaxFavorability(packet.maxFavorability);
			});	
		}
	}
	
	public static void handleLevelHandlerSync(CLevelHandler.SyncPacket packet, ClientGamePacketListener listener) 
	{
		@SuppressWarnings("resource")
		Minecraft mc = Minecraft.getInstance();
		PacketUtils.ensureRunningOnSameThread(packet, listener, mc);
		Entity entity = mc.level.getEntity(packet.entityId);
		// Needs a null check here as sometimes it may invoke on null??
		if (entity != null)
		{
			entity.getCapability(DwmgCapabilities.CAP_LEVEL_HANDLER).ifPresent((cap) ->
			{
				cap.setExp(packet.exp);
			});	
		}
	}
}
