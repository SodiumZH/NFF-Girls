package com.sodium.dwmg.datagen;

import com.sodium.dwmg.Dwmg;
import com.sodium.dwmg.DwmgTab;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class DataLanguageProvider extends LanguageProvider {

	public DataLanguageProvider(DataGenerator gen, String locale) {
        super(gen, Dwmg.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {
        add("itemGroup." + DwmgTab.TAB, "Days with Monster Girls");
        //add(Registration.MYSTERIOUS_ORE_OVERWORLD.get(), "Mysterious ore");
    }
}
