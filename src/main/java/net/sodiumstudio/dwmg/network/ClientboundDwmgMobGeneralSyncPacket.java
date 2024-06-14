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
	public final int entityId;
	public final int tradingPlayerId;	// -1 for null
	public final float favorability;
	public final float maxFavorability;
	public final long xp;
	
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
		DwmgClientGamePacketHandler.handleBMGeneralSync(this, handler);
	}

}
