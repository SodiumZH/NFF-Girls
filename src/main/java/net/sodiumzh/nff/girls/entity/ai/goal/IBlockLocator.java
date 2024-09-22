package net.sodiumzh.nff.girls.entity.ai.goal;

import java.util.Collection;

import net.minecraft.world.level.block.Block;

/**
 * Interface for mobs that can locate a certain type of block nearby
 */
public interface IBlockLocator
{
	public Collection<Block> getLocatingBlocks();
	public int getFrequency();
	public void onStartLocating();
}
