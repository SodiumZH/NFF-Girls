package com.sodium.dwmg.datagen;

import com.sodium.dwmg.Dwmg;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class DataItemModels extends ItemModelProvider {
	
	  public DataItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
	        super(generator, Dwmg.MODID, existingFileHelper);
	    }

	    @Override
	    protected void registerModels() {
	        //withExistingParent(Registration.MYSTERIOUS_ORE_OVERWORLD_ITEM.getId().getPath(), modLoc("block/mysterious_ore_overworld"));
	    }
}
