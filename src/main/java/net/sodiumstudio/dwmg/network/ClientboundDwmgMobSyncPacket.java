package net.sodiumstudio.dwmg.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventory;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;
import net.sodiumstudio.befriendmobs.util.Wrapped;
import net.sodiumstudio.dwmg.registries.DwmgCapabilities;

public class ClientboundDwmgMobSyncPacket implements Packet<ClientGamePacketListener>
{
	public final int entityId;
	public final float favorability;
	public final float maxFavorability;
	public final long exp;

	public ClientboundDwmgMobSyncPacket(int entityId, float favorability, float maxFavorability, long exp) {
		this.entityId = entityId;
		this.favorability = favorability;
		this.maxFavorability = maxFavorability;
		this.exp = exp;
	}

	public ClientboundDwmgMobSyncPacket(FriendlyByteBuf buffer) {
		this.entityId = buffer.readInt();
		this.favorability = buffer.readFloat();
		this.maxFavorability = buffer.readFloat();
		this.exp = buffer.readLong();
	}


	@Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(this.entityId);
		buffer.writeFloat(this.favorability);
		buffer.writeFloat(this.maxFavorability);
		buffer.writeLong(this.exp);
		
	}

	@SuppressWarnings("resource")
	@Override
	public void handle(ClientGamePacketListener handler) {
		Minecraft mc = Minecraft.getInstance();
		PacketUtils.ensureRunningOnSameThread(this, handler, mc);
		Entity entity = mc.level.getEntity(this.entityId);
		entity.getCapability(DwmgCapabilities.CAP_FAVORABILITY_HANDLER).ifPresent((cap) ->
		{
			cap.setFavorability(this.favorability);
			cap.setMaxFavorability(this.maxFavorability);
		});
		entity.getCapability(DwmgCapabilities.CAP_LEVEL_HANDLER).ifPresent((cap) -> 
		{
			cap.setExp(this.exp);
		});		
	}

	/* Sync mob data needed to client */
	public static void sync(IBefriendedMob mob)
	{
		if (mob.asMob().level.isClientSide)
			return;
		if (mob.getOwner() instanceof ServerPlayer sp)
		{
			CompoundTag values = new CompoundTag();
			mob.asMob().getCapability(DwmgCapabilities.CAP_FAVORABILITY_HANDLER).ifPresent((cap) ->
			{
				values.putFloat("favorability", cap.getFavorability());
				values.putFloat("max_favorability", cap.getMaxFavorability());
			});
			mob.asMob().getCapability(DwmgCapabilities.CAP_LEVEL_HANDLER).ifPresent((cap) -> 
			{
				values.putLong("exp", cap.getExp());
			});		
			ClientboundDwmgMobSyncPacket packet = new ClientboundDwmgMobSyncPacket(mob.asMob().getId(), 
					values.getFloat("favorability"), values.getFloat("max_favorability"), values.getLong("exp"));
		}
	}
	
	
}
