package com.sodium.dwmg.datagen;

import com.sodium.dwmg.*;
import com.sodium.dwmg.datagen.*;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = Dwmg.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        if (event.includeServer()) {
            generator.addProvider(new DataRecipes(generator));
            generator.addProvider(new DataLootTables(generator));
            DataBlockTags blockTags = new DataBlockTags(generator, event.getExistingFileHelper());
            generator.addProvider(blockTags);
            generator.addProvider(new DataItemTags(generator, blockTags, event.getExistingFileHelper()));
        }
        if (event.includeClient()) {
            generator.addProvider(new DataBlockStates(generator, event.getExistingFileHelper()));
            generator.addProvider(new DataItemModels(generator, event.getExistingFileHelper()));
            generator.addProvider(new DataLanguageProvider(generator, "en_us"));
        }
    }
}
