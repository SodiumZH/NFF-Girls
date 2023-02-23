package com.sodium.dwmg.datagen;

import com.sodium.dwmg.Dwmg;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class DataBlockStates extends BlockStateProvider {

    public DataBlockStates(DataGenerator gen, ExistingFileHelper helper) {
        super(gen, Dwmg.MOD_ID, helper);
    }

    @Override
    protected void registerStatesAndModels() {
    	//simpleBlock(Registration.MYSTERIOUS_ORE_OVERWORLD.get());
    }
    
}
