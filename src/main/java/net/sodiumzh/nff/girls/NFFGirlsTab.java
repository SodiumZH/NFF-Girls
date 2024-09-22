package net.sodiumzh.nff.girls;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;

public class NFFGirlsTab extends CreativeModeTab{
    
	private NFFGirlsTab(int index, String label) {
        super(index, label);
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(NFFGirlsItems.TAB_ICON.get());
    }
    
	public static final CreativeModeTab TAB = new NFFGirlsTab(CreativeModeTab.TABS.length, "nffgirls_tab");
	
}
