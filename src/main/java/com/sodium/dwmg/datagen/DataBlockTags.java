package com.sodium.dwmg.datagen;

import com.sodium.dwmg.Dwmg;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class DataBlockTags extends BlockTagsProvider{
    public DataBlockTags(DataGenerator generator, ExistingFileHelper helper) {
        super(generator, Dwmg.MODID, helper);
    }

    @Override
    protected void addTags() {
       // tag(BlockTags.MINEABLE_WITH_PICKAXE)
       //        .add(Registration.MYSTERIOUS_ORE_OVERWORLD.get())
    }
    
    @Override
    public String getName() {
        return "DWMG Tags";
    }
    
}
