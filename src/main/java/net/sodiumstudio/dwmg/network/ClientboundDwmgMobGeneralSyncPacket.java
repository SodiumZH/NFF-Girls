package net.sodiumstudio.dwmg.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;
import net.sodiumstudio.dwmg.entities.IDwmgBefriendedMob;
import net.sodiumstudio.dwmg.entities.capabilities.CFavorabilityHandler;
import net.sodiumstudio.dwmg.entities.capabilities.CLevelHandler;
import net.sodiumstudio.dwmg.registries.DwmgCapabilities;
import net.sodiumstudio.nautils.NaMiscUtils;

public class ClientboundDwmgMobGeneralSyncPacket implements Packet<ClientGamePacketListener>
{
	private final int entityId;
	
	private final int tradingPlayerId;	// -1 for null
	private float favorability;
	private float maxFavorability;
	private long xp;
	
	public ClientboundDwmgMobGeneralSyncPacket(IDwmgBefriendedMob mob)
	{
		this.entityId = mob.asMob().getId();
		this.tradingPlayerId = NaMiscUtils.getValueFromCapability(mob.asMob(), DwmgCapabilities.CAP_TRADE_HANDLER, 
				cap -> (cap.getTradingPlayer() != null ? cap.getTradingPlayer().getId() : -1), -1);
		this.favorability = NaMiscUtils.getValueFromCapability(mob.asMob(), DwmgCapabilities.CAP_FAVORABILITY_HANDLER, 
				CFavorabilityHandler::getFavorability, 50f);
		this.maxFavorability = NaMiscUtils.getValueFromCapability(mob.asMob(), DwmgCapabilities.CAP_FAVORABILITY_HANDLER, 
				CFavorabilityHandler::getMaxFavorability, 100f);
		this.xp = NaMiscUtils.getValueFromCapability(mob.asMob(), DwmgCapabilities.CAP_LEVEL_HANDLER, 
				CLevelHandler::getExp, 0l);
	}
	
	public ClientboundDwmgMobGeneralSyncPacket(FriendlyByteBuf buf)
	{
		this.entityId = buf.readInt();
		this.tradingPlayerId = buf.readInt();
		this.favorability = buf.readFloat();
		this.maxFavorability = buf.readFloat();
		this.xp = buf.readLong();
	}
	
	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeInt(entityId);
		buf.writeInt(tradingPlayerId);
		buf.writeFloat(favorability);
		buf.writeFloat(maxFavorability);
		buf.writeLong(xp);
	}

	@Override
	public void handle(ClientGamePacketListener handler) {
		Minecraft mc = Minecraft.getInstance();
		PacketUtils.ensureRunningOnSameThread(this, handler, mc);
		IDwmgBefriendedMob bm = (IDwmgBefriendedMob) mc.level.getEntity(entityId);
		
		bm.asMob().getCapability(DwmgCapabilities.CAP_TRADE_HANDLER).ifPresent(cap -> 
		{
			cap.setTradingPlayer(tradingPlayerId == -1 ? null : (Player) mc.level.getEntity(tradingPlayerId));
		});
		bm.asMob().getCapability(DwmgCapabilities.CAP_FAVORABILITY_HANDLER).ifPresent(cap ->
		{
			cap.setFavorability(favorability);
		});
		bm.asMob().getCapability(DwmgCapabilities.CAP_FAVORABILITY_HANDLER).ifPresent(cap ->
		{
			cap.setMaxFavorability(maxFavorability);
		});
		bm.asMob().getCapability(DwmgCapabilities.CAP_LEVEL_HANDLER).ifPresent(cap ->
		{
			cap.setExp(xp);
		});
	}
	
	public static void doSync(IDwmgBefriendedMob mob)
	{
		ClientboundDwmgMobGeneralSyncPacket packet = new ClientboundDwmgMobGeneralSyncPacket(mob);
		if (mob.isOwnerInDimension() && mob.getOwner() instanceof ServerPlayer toPlayer)
			DwmgChannels.SYNC_CHANNEL.send(PacketDistributor.PLAYER.with(() -> toPlayer), packet);
	}
}
