package net.sodiumstudio.befriendmobs.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.entity.befriending.registry.BefriendingTypeRegistry;
import net.sodiumstudio.befriendmobs.registry.BMCaps;
import net.sodiumstudio.befriendmobs.registry.BMItems;
import net.sodiumstudio.nautils.EntityHelper;
import net.sodiumstudio.nautils.NaMiscUtils;
import net.sodiumstudio.nautils.NaParticleUtils;
import net.sodiumstudio.nautils.debug.Debug;
import net.sodiumstudio.nautils.exceptions.UnimplementedException;

public class DebugBefrienderItem extends Item
{

	public DebugBefrienderItem(Properties pProperties)
	{
		super(pProperties);
	}

	@Override
	@SuppressWarnings("unchecked")
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) 
	{
		if (player.isCreative() && !player.level.isClientSide)
		{
			if (target instanceof IBefriendedMob bef)
			{
				bef.init(player.getUUID(), null);
				NaMiscUtils.printToScreen("Mob " + target.getName().getString() + " initialized", player);
			}
			else 
			{
				target.getCapability(BMCaps.CAP_BEFRIENDABLE_MOB).ifPresent((l) ->
				{
					IBefriendedMob bef = BefriendingTypeRegistry.getHandler((EntityType<Mob>)target.getType()).befriend(player, l.getOwner());
					if (bef != null)
					{
						NaParticleUtils.sendHeartParticlesToEntityDefault(bef.asMob());
						NaMiscUtils.printToScreen("Mob " + target.getName().getString() + " befriended", player);
					} else
						throw new UnimplementedException(
								"Entity type befriend method unimplemented: " + target.getType().toShortString()
								+ ", handler class: " + BefriendingTypeRegistry.getHandler((EntityType<Mob>)target.getType()).toString());
	
				});
			}
			return InteractionResult.sidedSuccess(player.level.isClientSide);
		}
		else return InteractionResult.PASS;
	}
}
