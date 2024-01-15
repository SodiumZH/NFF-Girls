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

public class TransferringTagItem extends MobOwnershipTransfererItem
{

	public TransferringTagItem(Properties pProperties)
	{
		super(pProperties);
		this.foilCondition(s -> this.isWritten(s));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag)
	{
		super.appendHoverText(stack, level, list, tooltipFlag);
		if (this.isWritten(stack))
		{
			list.add(InfoHelper.createTranslatable("info.dwmg.item.transferring_tag_mob", this.getMobName(stack)).withStyle(ChatFormatting.GRAY));
			list.add(InfoHelper.createTranslatable("info.dwmg.item.transferring_tag_owner", this.getOldOwnerName(stack)).withStyle(ChatFormatting.GRAY));
			if (this.isLocked(stack))
			{
				list.add(InfoHelper.createTranslatable("info.dwmg.item.transferring_tag_locked").withStyle(ChatFormatting.GRAY));
			}
		}
	}
	
}
