package net.sodiumstudio.dwmg.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.sodiumstudio.befriendmobs.item.MobOwnershipTransfererItem;
import net.sodiumstudio.nautils.InfoHelper;

public class TurnoverScutcheonItem extends MobOwnershipTransfererItem
{

	public TurnoverScutcheonItem(Properties pProperties)
	{
		super(pProperties);
	}

	@Override
	public final boolean isFoil(ItemStack stack)
	{
		return this.isWritten(stack);
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag)
	{
		super.appendHoverText(stack, level, list, tooltipFlag);
		if (this.isWritten(stack))
		{
			list.add(InfoHelper.createTrans("info.dwmg.item.turnover_scutcheon_mob", this.getMobName(stack)).withStyle(ChatFormatting.GRAY));
			list.add(InfoHelper.createTrans("info.dwmg.item.turnover_scutcheon_owner", this.getOldOwnerName(stack)).withStyle(ChatFormatting.GRAY));
			if (this.isLocked(stack))
			{
				list.add(InfoHelper.createTrans("info.dwmg.item.turnover_scutcheon_locked").withStyle(ChatFormatting.GRAY));
			}
		}
	}
	
}
