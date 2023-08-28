package net.sodiumstudio.dwmg.item;

import com.github.mechalopa.hmag.registry.ModEntityTypes;
import com.github.mechalopa.hmag.world.entity.JiangshiEntity;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.befriendmobs.entity.befriending.registry.BefriendingTypeRegistry;
import net.sodiumstudio.befriendmobs.registry.BMCaps;
import net.sodiumstudio.dwmg.entities.handlers.hmag.HandlerJiangshi;
import net.sodiumstudio.nautils.Wrapped;

public class TaoistTalismanItem extends Item
{

	public TaoistTalismanItem(Properties pProperties)
	{
		super(pProperties);
	}
	
	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity living, InteractionHand usedHand)
	{
		if (!player.level.isClientSide && living.getType() == ModEntityTypes.JIANGSHI.get() && living instanceof JiangshiEntity js)
		{
			Wrapped<Boolean> interacted = new Wrapped<>(false);
			if (BefriendingTypeRegistry.getHandler(js) != null && BefriendingTypeRegistry.getHandler(js) instanceof HandlerJiangshi handler)
			{
				js.getCapability(BMCaps.CAP_BEFRIENDABLE_MOB).ifPresent(cap ->
				{
					if (handler.applyTalisman(js));
					{
						stack.shrink(1);
						interacted.set(true);
						living.level.playSound(null, living, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, SoundSource.BLOCKS, 2.0f, 1.0f);
					}
				});
				
			}
			if (interacted.get())
				return InteractionResult.sidedSuccess(player.level.isClientSide);
		}
		return InteractionResult.PASS;
	}
	
}
