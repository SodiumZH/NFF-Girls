package com.sodium.dwmg;

import com.sodium.dwmg.registries.ModItems;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class DwmgTab extends CreativeModeTab{
    
	private DwmgTab(int index, String label) {
        super(index, label);
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(ModItems.DEATH_CRYSTAL.get());
    }
    
	public static final CreativeModeTab TAB = new DwmgTab(CreativeModeTab.TABS.length, "Days with Monster Girls");
	
}
