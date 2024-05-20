package net.sodiumstudio.dwmg.entities.giftingsystem;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import net.sodiumstudio.dwmg.entities.IDwmgBefriendedMob;

@Cancelable
public class DwmgGiftGivingEvent extends Event
{
	public final Player player;
	public final IDwmgBefriendedMob mob;
	public final ItemStack itemStack;
	public final AcceptItemInfo acceptInfo;
	
	public DwmgGiftGivingEvent(Player player, IDwmgBefriendedMob mob, ItemStack itemStack, AcceptItemInfo acceptInfo)
	{
		this.player = player;
		this.mob = mob;
		this.itemStack = itemStack;
		this.acceptInfo = acceptInfo;
	}
}
