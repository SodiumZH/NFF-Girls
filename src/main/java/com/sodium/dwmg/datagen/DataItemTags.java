package com.sodium.dwmg.datagen;

import com.sodium.dwmg.Dwmg;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class DataItemTags extends ItemTagsProvider {

    public DataItemTags(DataGenerator generator, BlockTagsProvider blockTags, ExistingFileHelper helper) {
        super(generator, blockTags, Dwmg.MOD_ID, helper);
    }

    @Override
    protected void addTags() {
        //tag(Tags.Items.ORES)
        //        .add(Registration.MYSTERIOUS_ORE_OVERWORLD_ITEM.get());
              
    }

    @Override
    public String getName() {
        return "DWMG Tags";
    }
    
}
