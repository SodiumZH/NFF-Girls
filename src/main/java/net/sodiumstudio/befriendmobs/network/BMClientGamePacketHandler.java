package net.sodiumstudio.befriendmobs.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.sodiumstudio.befriendmobs.client.gui.screens.BefriendedGuiScreenMaker;
import net.sodiumstudio.befriendmobs.entity.befriended.CBefriendedMobData;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventory;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;
import net.sodiumstudio.befriendmobs.registry.BMCaps;

public class BMClientGamePacketHandler
{
	
	@SuppressWarnings("resource")
	public static void handleBefriendedGuiOpen(ClientboundBefriendedGuiOpenPacket packet, ClientGamePacketListener listener)
	{
		Minecraft mc = Minecraft.getInstance();
		PacketUtils.ensureRunningOnSameThread(packet, listener, mc);
		Entity entity = mc.level.getEntity(packet.getEntityId());
		if (entity instanceof IBefriendedMob bef) {
			LocalPlayer localplayer = mc.player;
			BefriendedInventory inv = new BefriendedInventory(packet.getSize());
			BefriendedInventoryMenu menu =
					bef.makeMenu(packet.getContainerId(), localplayer.getInventory(), inv);
			if (menu == null)
				return;
			localplayer.containerMenu = menu;
			mc.setScreen(BefriendedGuiScreenMaker.make(menu));
		}
	}
	
	public static void handleBefriendingInit(ClientboundBefriendingInitPacket packet, ClientGamePacketListener listener)
	{
		@SuppressWarnings("resource")
		Minecraft mc = Minecraft.getInstance();
		PacketUtils.ensureRunningOnSameThread(packet, listener, mc);
		Entity entity = mc.level.getEntity(packet.entityId);
		entity.setXRot(packet.xRot);
		entity.setYRot(packet.yRot);
		if (entity instanceof Mob mob)
		{
			mob.setYBodyRot(packet.yBodyRot);
			mob.setYHeadRot(packet.yHeadRot);
		}
	}
	
	public static void handleBefriendedDataSync(CBefriendedMobData.ClientboundDataSyncPacket packet, ClientGamePacketListener listener)
	{
		@SuppressWarnings("resource")
		Minecraft mc = Minecraft.getInstance();
		PacketUtils.ensureRunningOnSameThread(packet, listener, mc);
		Entity e = mc.level.getEntity(packet.entityId);
		e.getCapability(BMCaps.CAP_BEFRIENDED_MOB_DATA).ifPresent(c -> {
			for (var entry: packet.objects.entrySet())
				c.setSynchedDataClient(entry.getKey(), entry.getValue().getA(), entry.getValue().getB());
		});
	}
}
