package com.sodium.dwmg.client.multiplayer;

import java.lang.reflect.InvocationTargetException;

import com.mojang.authlib.GameProfile;
import com.sodium.dwmg.entities.IBefriendedMob;
import com.sodium.dwmg.inventory.AbstractInventoryMenuBefriended;
import com.sodium.dwmg.network.ClientboundBefriendedGuiOpenPacket;

import net.minecraft.client.ClientTelemetryManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;

public class NaClientGamePacketListenerImpl extends ClientPacketListener implements NaClientGamePacketListener {

	protected final Minecraft mc;

	public NaClientGamePacketListenerImpl(Minecraft pMinecraft, Screen pCallbackScreen, Connection pConnection,
			GameProfile pLocalGameProfile, ClientTelemetryManager pTelemetryManager) {
		super(pMinecraft, pCallbackScreen, pConnection, pLocalGameProfile, pTelemetryManager);
		this.mc = pMinecraft;
	}

	@Override
	public void handleBefriendedGuiOpen(ClientboundBefriendedGuiOpenPacket packet) {
		PacketUtils.ensureRunningOnSameThread(packet, this, this.mc);
		@SuppressWarnings("resource")
		Entity entity = this.getLevel().getEntity(packet.getEntityId());
		if (entity instanceof IBefriendedMob bef) {
			LocalPlayer localplayer = this.mc.player;
			SimpleContainer simplecontainer = new SimpleContainer(packet.getSize());
			AbstractInventoryMenuBefriended menu =
					bef.makeMenu(packet.getContainerId(), localplayer.getInventory(), simplecontainer);
			localplayer.containerMenu = menu;
			this.mc.setScreen(bef.makeGui(menu, localplayer.getInventory(), entity.getDisplayName()));

		}

	}

}
