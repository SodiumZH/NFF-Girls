package net.sodiumstudio.dwmg.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.nautils.InfoHelper;


public class DwmgTabs{
    
	public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Dwmg.MOD_ID);
	
	public static final RegistryObject<CreativeModeTab> TAB = CREATIVE_TABS.register("tab", () -> CreativeModeTab.builder()
			.title(InfoHelper.createTrans("item_group.dwmg_tab"))
			.icon(() -> DwmgItems.TAB_ICON.get().getDefaultInstance())
			.displayItems((features, output) ->
			{
				for (var item: DwmgItems.ITEMS.getEntries())
				{
					if (!DwmgItems.NO_TAB.contains(item))
						output.accept(item.get());
				}
			}).build());
}
