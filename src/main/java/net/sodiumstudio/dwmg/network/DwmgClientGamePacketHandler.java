package net.sodiumstudio.dwmg.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.sodiumstudio.dwmg.entities.IDwmgBefriendedMob;
import net.sodiumstudio.dwmg.entities.capabilities.CFavorabilityHandler;
import net.sodiumstudio.dwmg.entities.capabilities.CLevelHandler;
import net.sodiumstudio.dwmg.entities.vanillatrade.ClientboundDwmgTradeSyncPacket;
import net.sodiumstudio.dwmg.registries.DwmgCapabilities;

@OnlyIn(Dist.CLIENT)
public class DwmgClientGamePacketHandler
{

	@Deprecated
	public static void handleFavorabilityHandlerSync(CFavorabilityHandler.ClientboundSyncPacket packet, ClientGamePacketListener listener)
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
	
	@Deprecated
	public static void handleLevelHandlerSync(CLevelHandler.ClientboundSyncPacket packet, ClientGamePacketListener listener) 
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
	
	public static void handleBMGeneralSync(ClientboundDwmgMobGeneralSyncPacket packet, ClientGamePacketListener listener)
	{
		Minecraft mc = Minecraft.getInstance();
		PacketUtils.ensureRunningOnSameThread(packet, listener, mc);
		IDwmgBefriendedMob bm = IDwmgBefriendedMob.getBM(mc.level.getEntity(packet.entityId));
		bm.asMob().getCapability(DwmgCapabilities.CAP_TRADE_HANDLER).ifPresent(cap -> 
		{
			cap.setTradingPlayer(packet.tradingPlayerId == -1 ? null : (Player) mc.level.getEntity(packet.tradingPlayerId));
		});
		bm.asMob().getCapability(DwmgCapabilities.CAP_FAVORABILITY_HANDLER).ifPresent(cap ->
		{
			cap.setFavorability(packet.favorability);
			cap.setMaxFavorability(packet.maxFavorability);
		});
		bm.asMob().getCapability(DwmgCapabilities.CAP_LEVEL_HANDLER).ifPresent(cap ->
		{
			cap.setExp(packet.xp);
		});
	}
	
	public static void handleTradeSync(ClientboundDwmgTradeSyncPacket packet, ClientGamePacketListener listener)
	{
		Minecraft mc = Minecraft.getInstance();
		PacketUtils.ensureRunningOnSameThread(packet, listener, mc);
		Entity entity = mc.level.getEntity(packet.id);
		if (entity != null)
		{
			entity.getCapability(DwmgCapabilities.CAP_TRADE_HANDLER).ifPresent(cap -> {
				cap.getOffers().clear();
				cap.getMeta().clear();
				for (int i = 0; i < packet.offers.size(); ++i)
				{
					cap.getOffers().add(packet.offers.get(i));
					cap.getMeta().add(packet.meta.get(i));
					cap.setPoints(packet.points);
				}
			});
		}
	}
}
