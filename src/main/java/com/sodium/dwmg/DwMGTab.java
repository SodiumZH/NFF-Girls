package com.sodium.dwmg;

import com.sodium.dwmg.registries.ModItems;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class DwMGTab extends CreativeModeTab{
    
	private DwMGTab(int index, String label) {
        super(index, label);
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(ModItems.DEATH_CRYSTAL.get());
    }
    
	public static final CreativeModeTab TAB = new DwMGTab(CreativeModeTab.TABS.length, "dwmg");
	
}
