package net.sodiumstudio.befriendmobs.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventory;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;

public class ClientboundBefriendedGuiOpenPacket implements Packet<ClientGamePacketListener> {

	protected final int containerId;
	protected final int size;
	protected final int entityId;

	public ClientboundBefriendedGuiOpenPacket(int containerId, int size, int entityId) {
		this.containerId = containerId;
		this.size = size;
		this.entityId = entityId;
	}

	public ClientboundBefriendedGuiOpenPacket(FriendlyByteBuf pBuffer) {
		this.containerId = pBuffer.readUnsignedByte();
		this.size = pBuffer.readVarInt();
		this.entityId = pBuffer.readInt();
	}


	@Override
	public void write(FriendlyByteBuf pBuffer) {
		pBuffer.writeByte(this.containerId);
		pBuffer.writeVarInt(this.size);
		pBuffer.writeInt(this.entityId);
	}

	@Override
	public void handle(ClientGamePacketListener handler) {
		BMClientGamePacketHandler.handleBefriendedGuiOpen(this, handler);
	}

	public int getContainerId() {
		return this.containerId;
	}

	public int getSize() {
		return this.size;
	}

	public int getEntityId() {
		return this.entityId;
	}
}
