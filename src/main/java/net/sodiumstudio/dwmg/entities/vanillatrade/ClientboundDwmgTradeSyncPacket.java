package net.sodiumstudio.dwmg.entities.vanillatrade;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.sodiumstudio.dwmg.registries.DwmgCapabilities;

public class ClientboundDwmgTradeSyncPacket implements Packet<ClientGamePacketListener>
{
	public final int id;
	public final MerchantOffers offers;
	public final List<DwmgTradeOfferMetaData> meta;
	
	public ClientboundDwmgTradeSyncPacket(CDwmgTradeHandler handler)
	{
		this.id = handler.getMob().getId();
		this.offers = handler.getOffers();
		this.meta = handler.getMeta();
	}
	
	public ClientboundDwmgTradeSyncPacket(FriendlyByteBuf buf)
	{
		this.id = buf.readInt();
		this.offers = MerchantOffers.createFromStream(buf);
		this.meta = new ArrayList<>();
		int size = buf.readInt();
		for (int i = 0; i < size; ++i)
		{
			this.meta.add(DwmgTradeOfferMetaData.readBuf(buf));
		}
	}
	
	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeInt(id);
		this.offers.writeToStream(buf);
		buf.writeInt(meta.size());
		for (int i = 0; i < meta.size(); ++i)
		{
			meta.get(i).writeToBuf(buf);
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void handle(ClientGamePacketListener pHandler) {
		Minecraft mc = Minecraft.getInstance();
		PacketUtils.ensureRunningOnSameThread(this, pHandler, mc);
		Entity entity = mc.level.getEntity(id);
		if (entity != null)
		{
			entity.getCapability(DwmgCapabilities.CAP_TRADE_HANDLER).ifPresent(cap -> {
				cap.getOffers().clear();
				cap.getMeta().clear();
				for (int i = 0; i < this.offers.size(); ++i)
				{
					cap.getOffers().add(this.offers.get(i));
					cap.getMeta().add(this.meta.get(i));
				}
			});
		}
	}

}
