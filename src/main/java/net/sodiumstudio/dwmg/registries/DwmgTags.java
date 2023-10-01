package net.sodiumstudio.dwmg.registries;

import com.github.mechalopa.hmag.HMaG;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.sodiumstudio.dwmg.Dwmg;

public class DwmgTags
{


	public static final TagKey<Block> AFFECTS_CRIMSON_SLAUGHTERER = blockTag("affects_crimson_slaughterer");
	
	
	protected static TagKey<Block> blockTag(String name)
	{
		return TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Dwmg.MOD_ID, name));
	}
	
}
