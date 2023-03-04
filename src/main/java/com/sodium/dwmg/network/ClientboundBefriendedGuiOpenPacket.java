package com.sodium.dwmg.network;

import java.lang.reflect.InvocationTargetException;

import com.sodium.dwmg.client.multiplayer.NaClientGamePacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundBefriendedGuiOpenPacket implements Packet<NaClientGamePacketListener> {

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
	public void handle(NaClientGamePacketListener handler) {

			try {
				handler.handleBefriendedGuiOpen(this);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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
