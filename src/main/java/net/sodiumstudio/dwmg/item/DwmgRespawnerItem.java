package net.sodiumstudio.dwmg.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.sodiumstudio.befriendmobs.item.MobRespawnerInstance;
import net.sodiumstudio.befriendmobs.item.MobRespawnerItem;
import net.sodiumstudio.nautils.InfoHelper;

public class DwmgRespawnerItem extends MobRespawnerItem
{

	public DwmgRespawnerItem(Properties pProperties)
	{
		super(pProperties);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag)
	{
		super.appendHoverText(stack, level, list, tooltipFlag);
		if (stack.getTag() != null)
		{
			MobRespawnerInstance inst = MobRespawnerInstance.create(stack);
			MutableComponent name = (MutableComponent) inst.getName();
			MutableComponent type = (MutableComponent) inst.getType().getDescription();
			
			if (name != null && type != null)
			{
				list.add(InfoHelper.createTranslatable("item.dwmg.respawner.name").append(name));
				list.add(InfoHelper.createTranslatable("item.dwmg.respawner.type").append(type));
			}
		}
	}
}
