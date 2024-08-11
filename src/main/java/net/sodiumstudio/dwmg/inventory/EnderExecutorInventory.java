package net.sodiumstudio.dwmg.inventory;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventory;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryWithHandItems;
import net.sodiumstudio.dwmg.entities.ICarriesBlock;

public class EnderExecutorInventory extends BefriendedInventoryWithHandItems
{
	public EnderExecutorInventory(int size, IBefriendedMob bm)
	{
		super(size, bm);
		if (size < 3) throw new IllegalArgumentException("size must >3, slot 2 = block slot.");
		if (!(bm.asMob() instanceof ICarriesBlock))
			throw new IllegalArgumentException("Mob must implement ICarriesBlock interface.");
		this.setShouldKeepArmorEmpty();
		
	}
	
	@Override
	public void getFromMob(Mob mob) {
		if (mob instanceof ICarriesBlock cb)
		{
			if (cb.getCarryingBlock() != null && !cb.getCarryingBlock().is(Blocks.AIR))
				this.setItem(2, new ItemStack(cb.getCarryingBlock().getBlock().asItem()));
			else this.setItem(2, ItemStack.EMPTY);
		}
		else throw new IllegalArgumentException("Mob must implement ICarriesBlock interface.");
		super.getFromMob(mob);	
	}
	
	@Override
	public void syncToMob(Mob mob)
	{
		if (mob instanceof ICarriesBlock cb)
		{
			if (this.getItem(2).isEmpty())
				cb.setCarryingBlock(null);		
			else if (this.getItem(2).getItem() instanceof BlockItem bi)
				cb.setCarryingBlock(bi.getBlock().defaultBlockState());
			else cb.setCarryingBlock(null);
		}
		else throw new IllegalArgumentException("Mob must implement ICarriesBlock interface.");
		super.syncToMob(mob);
	}
}
