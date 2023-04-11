package net.sodiumstudio.dwmg.befriendmobs.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.sodiumstudio.dwmg.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.registry.BefMobCapabilities;
import net.sodiumstudio.dwmg.befriendmobs.registry.BefMobItems;
import net.sodiumstudio.dwmg.befriendmobs.util.EntityHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.Wrapped;

public class ItemMobRespawner extends Item
{

	public ItemMobRespawner(Properties pProperties)
	{
		super(pProperties);
	}

	public static ItemStack fromMob(Mob mob)
	{
		if (mob.level.isClientSide)
			return ItemStack.EMPTY;
		ItemStack stack = new ItemStack(BefMobItems.MOB_RESPAWNER.get(), 1);
		stack.getCapability(BefMobCapabilities.CAP_MOB_RESPAWNER).ifPresent((c) -> 
		{
			c.initFromMob(mob);
		});
		
		//stack.setHoverName(new TranslatableComponent(stack.getHoverName().getString() + " - " + mob.getName().getString()));
		return stack;
	}

	public static Mob respawn(ItemStack stack, Player player, BlockPos pos, Direction direction)
	{
		Wrapped<Mob> mob = new Wrapped<Mob>(null);
		stack.getCapability(BefMobCapabilities.CAP_MOB_RESPAWNER).ifPresent((c) -> 
		{
			mob.set(c.respawn(player, pos, direction));
		});
		return mob.get();
	}
	
	 @SuppressWarnings("resource")
	@Override
	public InteractionResult useOn(UseOnContext context) {
	      Level level = context.getLevel();
	      if (!(level instanceof ServerLevel)) {
	         return InteractionResult.SUCCESS;
	      } 
	      else 
	      {
	    	  Mob mob = respawn(
	 		         context.getItemInHand(),
			         context.getPlayer(),
			         context.getClickedPos(),
			         context.getClickedFace());
		      if (mob != null)
		      {
		    	 
		    	 context.getItemInHand().shrink(1);
		         if (mob instanceof IBefriendedMob bef)
		         {
		        	 bef.init(bef.getOwnerUUID(), null);
		        	 if (!bef.getAdditionalInventory().isEmpty())
		        		 bef.getAdditionalInventory().clearContent();
		        	 bef.updateFromInventory();
		        	 EntityHelper.removeAllEquipment(bef.asMob());
		        	 bef.setInit();
		         }
		    	 return InteractionResult.CONSUME;
		      }
		      else return InteractionResult.PASS;
	      }
	   }
	 
	 
}
