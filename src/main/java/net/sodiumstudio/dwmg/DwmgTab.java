package net.sodiumstudio.dwmg;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.dwmg.registries.DwmgItems;

public class DwmgTab extends CreativeModeTab{
    
	private DwmgTab(int index, String label) {
        super(index, label);
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(DwmgItems.TAB_ICON.get());
    }
    
	public static final CreativeModeTab TAB = new DwmgTab(CreativeModeTab.TABS.length, "dwmg_tab");
	
}
