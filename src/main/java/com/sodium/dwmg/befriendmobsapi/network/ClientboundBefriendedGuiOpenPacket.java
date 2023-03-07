package com.sodium.dwmg.befriendmobsapi.network;

import com.sodium.dwmg.befriendmobsapi.entitiy.IBefriendedMob;
import com.sodium.dwmg.befriendmobsapi.inventory.AbstractInventoryMenuBefriended;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;

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

	public void write(FriendlyByteBuf pBuffer) {
		pBuffer.writeByte(this.containerId);
		pBuffer.writeVarInt(this.size);
		pBuffer.writeInt(this.entityId);
	}

	public void handle(ClientGamePacketListener handler) {
		@SuppressWarnings("resource")
		Minecraft mc = Minecraft.getInstance();
		PacketUtils.ensureRunningOnSameThread(this, handler, mc);
		Entity entity = mc.level.getEntity(getEntityId());
		if (entity instanceof IBefriendedMob bef) {
			LocalPlayer localplayer = mc.player;
			SimpleContainer simplecontainer = new SimpleContainer(getSize());
			AbstractInventoryMenuBefriended menu =
					bef.makeMenu(getContainerId(), localplayer.getInventory(), simplecontainer);
			localplayer.containerMenu = menu;
			mc.setScreen(menu.makeGui());
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
