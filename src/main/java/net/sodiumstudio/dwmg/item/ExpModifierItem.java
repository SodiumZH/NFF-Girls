package net.sodiumstudio.dwmg.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.sodiumstudio.dwmg.registries.DwmgCapabilities;
import net.sodiumstudio.nautils.EntityHelper;
import net.sodiumstudio.nautils.MiscUtil;
import net.sodiumstudio.nautils.NbtHelper;

public class ExpModifierItem extends Item
{

	public ExpModifierItem(Properties pProperties)
	{
		super(pProperties.stacksTo(1));
	}

	@Override
	public ItemStack getDefaultInstance()
	{
		ItemStack res = new ItemStack(this);
		res.getOrCreateTag().putInt("value", 1);
		return res;
	}
	
	public int getValue(ItemStack stack)
	{
		if (!stack.getOrCreateTag().contains("value", NbtHelper.TAG_INT_ID))
			stack.getOrCreateTag().putInt("value", 1);
		return stack.getOrCreateTag().getInt("value");
	}
	
	public void setValue(ItemStack stack, int value)
	{
		stack.getOrCreateTag().putInt("value", value);
	}
	
    @Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
    	if (target.getCapability(DwmgCapabilities.CAP_LEVEL_HANDLER).isPresent())
    	{
    		target.getCapability(DwmgCapabilities.CAP_LEVEL_HANDLER).ifPresent(cap -> {
    			if (!player.level().isClientSide)
	    		{
	    			if (!player.isShiftKeyDown())
	    			{
	    				cap.setExp(cap.getExp() + this.getValue(stack));
	    				EntityHelper.sendGlintParticlesToLivingDefault(target);
	    			}
	    			else 
	    			{
	    				cap.setExp(Math.max(0, cap.getExp() - this.getValue(stack)));
	    				EntityHelper.sendSmokeParticlesToLivingDefault(target);
	    			}
    			}
    		});
    		return InteractionResult.sidedSuccess(player.level().isClientSide);
    	}
    	return InteractionResult.PASS;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
    	if (!level.isClientSide)
    	{
    		if (!player.isShiftKeyDown())
    		{
	    		switch (this.getValue(player.getItemInHand(hand)))
	    		{
	    		case 1:
	    		{
	    			this.setValue(player.getItemInHand(hand), 4);
	    			break;
	    		}
	    		case 4:
	    		{
	    			this.setValue(player.getItemInHand(hand), 16);
	    			break;
	    		}
	    		case 16:
	    		{
	    			this.setValue(player.getItemInHand(hand), 64);
	    			break;
	    		}
	    		case 64:
	    		{
	    			this.setValue(player.getItemInHand(hand), 256);
	    			break;
	    		}
	    		case 256:
	    		{
	    			this.setValue(player.getItemInHand(hand), 1024);
	    			break;
	    		}
	    		case 1024:
	    		{
	    			this.setValue(player.getItemInHand(hand), 1);
	    			break;
	    		}
	    		default:
	    			throw new RuntimeException();
	    		}
    		}
    		else
    		{
    			switch (this.getValue(player.getItemInHand(hand)))
	    		{
	    		case 1:
	    		{
	    			this.setValue(player.getItemInHand(hand), 1024);
	    			break;
	    		}
	    		case 4:
	    		{
	    			this.setValue(player.getItemInHand(hand), 1);
	    			break;
	    		}
	    		case 16:
	    		{
	    			this.setValue(player.getItemInHand(hand), 4);
	    			break;
	    		}
	    		case 64:
	    		{
	    			this.setValue(player.getItemInHand(hand), 16);
	    			break;
	    		}
	    		case 256:
	    		{
	    			this.setValue(player.getItemInHand(hand), 64);
	    			break;
	    		}
	    		case 1024:
	    		{
	    			this.setValue(player.getItemInHand(hand), 256);
	    			break;
	    		}
	    		default:
	    			throw new RuntimeException();
	    		}
    		}
    		MiscUtil.printToScreen("EXP: " + Integer.toString(this.getValue(player.getItemInHand(hand))), player);
    	}
    	return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide);
    }
	
}
