package net.sodiumstudio.dwmg.item;

import net.minecraft.nbt.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.sodiumstudio.dwmg.registries.DwmgCapabilities;
import net.sodiumstudio.nautils.NaMiscUtils;
import net.sodiumstudio.nautils.NaParticleUtils;
import net.sodiumstudio.nautils.item.NaUtilsItem;

public class FavorabilityModifierItem extends NaUtilsItem
{

	public FavorabilityModifierItem(Properties pProperties)
	{
		super(pProperties.stacksTo(1));
	}

	@Override
	public ItemStack getDefaultInstance()
	{
		ItemStack res = new ItemStack(this);
		res.getOrCreateTag().putFloat("value", 1f);
		return res;
	}
	
	public float getValue(ItemStack stack)
	{
		if (!stack.getOrCreateTag().contains("value", Tag.TAG_FLOAT))
			stack.getOrCreateTag().putFloat("value", 1);
		return stack.getOrCreateTag().getFloat("value");
	}
	
	public void setValue(ItemStack stack, float value)
	{
		stack.getOrCreateTag().putFloat("value", value);
	}
	
    @Override
	public InteractionResult interactLivingEntity(Player player, LivingEntity target, InteractionHand hand) {
    	if (target.getCapability(DwmgCapabilities.CAP_FAVORABILITY_HANDLER).isPresent())
    	{
    		target.getCapability(DwmgCapabilities.CAP_FAVORABILITY_HANDLER).ifPresent(cap -> {
    			if (!player.level.isClientSide)
	    		{
	    			if (!player.isShiftKeyDown())
	    			{
	    				cap.addFavorability(this.getValue(player.getItemInHand(hand)));
	    				NaMiscUtils.printToScreen(String.format("Mob [%s] favorability + %.0f.", target.getName().getString(), this.getValue(player.getItemInHand(hand))) , player);
	    				NaMiscUtils.printToScreen(String.format("Current: %.2f", cap.getFavorability()) , player);
	    				NaParticleUtils.sendGlintParticlesToEntityDefault(target);
	    			}
	    			else 
	    			{
	    				cap.addFavorability(-this.getValue(player.getItemInHand(hand)));
	    				NaMiscUtils.printToScreen(String.format("Mob [%s] favorability - %.0f.", target.getName().getString(), this.getValue(player.getItemInHand(hand))) , player);
	    				NaMiscUtils.printToScreen(String.format("Current: %.2f", cap.getFavorability()) , player);
	    				NaParticleUtils.sendSmokeParticlesToEntityDefault(target);
	    			}
    			}
    		});
    		return InteractionResult.sidedSuccess(player.level.isClientSide);
    	}
    	return InteractionResult.PASS;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
    	if (!level.isClientSide)
    	{
    		this.setValue(player.getItemInHand(hand), this.getValue(player.getItemInHand(hand)) < 2f ? 5f : 1f);
    		NaMiscUtils.printToScreen(String.format("Favorability per operation: %.0f", this.getValue(player.getItemInHand(hand))), player);
    	}
    	return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide);
    }
}
