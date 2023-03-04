package com.sodium.dwmg.client.multiplayer;

import java.lang.reflect.InvocationTargetException;

import com.sodium.dwmg.entities.IBefriendedMob;
import com.sodium.dwmg.network.ClientboundBefriendedGuiOpenPacket;

import net.minecraft.network.protocol.game.ClientGamePacketListener;

public interface NaClientGamePacketListener extends ClientGamePacketListener 
{
	public <T extends IBefriendedMob> void handleBefriendedGuiOpen(ClientboundBefriendedGuiOpenPacket packet) 
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException;
}
