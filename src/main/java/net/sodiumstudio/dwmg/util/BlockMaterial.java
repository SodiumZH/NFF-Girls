package net.sodiumstudio.dwmg.util;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

/**
 * Preset class of previous {@code Material} properties for blocks. 
 */
public class BlockMaterial
{
	public static final BlockMaterial AIR = new BlockMaterial.Builder().replaceable().build();
	public static final BlockMaterial STRUCTURAL_AIR = new BlockMaterial.Builder().replaceable().build();
	public static final BlockMaterial PORTAL = new BlockMaterial.Builder().pushReaction(PushReaction.BLOCK).build();
	public static final BlockMaterial CLOTH_DECORATION = new BlockMaterial.Builder().mapColor(MapColor.WOOL).ignitedByLava().build();
	public static final BlockMaterial PLANT = new BlockMaterial.Builder().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).build();
	public static final BlockMaterial WATER_PLANT = new BlockMaterial.Builder().mapColor(MapColor.WATER).noteBlockInstrument(NoteBlockInstrument.BASEDRUM).build();
	public static final BlockMaterial REPLACEABLE_PLANT = new BlockMaterial.Builder().mapColor(MapColor.PLANT).replaceable().ignitedByLava().pushReaction(PushReaction.DESTROY).build();
	public static final BlockMaterial REPLACEABLE_FIREPROOF_PLANT = new BlockMaterial.Builder().mapColor(MapColor.PLANT).replaceable().pushReaction(PushReaction.DESTROY).build();
	public static final BlockMaterial REPLACEABLE_WATER_PLANT = new BlockMaterial.Builder().mapColor(MapColor.WATER).replaceable().pushReaction(PushReaction.DESTROY).build();
	public static final BlockMaterial WATER = new BlockMaterial.Builder().mapColor(MapColor.WATER).replaceable().pushReaction(PushReaction.DESTROY).liquid().build();
	public static final BlockMaterial BUBBLE_COLUMN = new BlockMaterial.Builder().mapColor(MapColor.WATER).replaceable().pushReaction(PushReaction.DESTROY).liquid().build();
	public static final BlockMaterial LAVA = new BlockMaterial.Builder().mapColor(MapColor.FIRE).replaceable().pushReaction(PushReaction.DESTROY).liquid().build();
	public static final BlockMaterial TOP_SNOW = new BlockMaterial.Builder().mapColor(MapColor.SNOW).replaceable().pushReaction(PushReaction.DESTROY).build();
	public static final BlockMaterial FIRE = new BlockMaterial.Builder().mapColor(MapColor.FIRE).replaceable().pushReaction(PushReaction.DESTROY).build();
	public static final BlockMaterial DECORATION = new BlockMaterial.Builder().pushReaction(PushReaction.DESTROY).build();
	public static final BlockMaterial WEB = new BlockMaterial.Builder().mapColor(MapColor.WOOL).pushReaction(PushReaction.DESTROY).build();
	public static final BlockMaterial SCULK = new BlockMaterial.Builder().mapColor(MapColor.COLOR_BLACK).build();
	public static final BlockMaterial BUILDABLE_GRASS = new BlockMaterial.Builder().build();
	public static final BlockMaterial CLAY = new BlockMaterial.Builder().mapColor(MapColor.CLAY).build();
	public static final BlockMaterial DIRT = new BlockMaterial.Builder().mapColor(MapColor.DIRT).build();
	public static final BlockMaterial GRASS = new BlockMaterial.Builder().mapColor(MapColor.GRASS).build();
	public static final BlockMaterial ICE_SOLID = new BlockMaterial.Builder().mapColor(MapColor.ICE).build();
	public static final BlockMaterial SAND = new BlockMaterial.Builder().mapColor(MapColor.SAND).noteBlockInstrument(NoteBlockInstrument.SNARE).build();
	public static final BlockMaterial SPONGE = new BlockMaterial.Builder().mapColor(MapColor.COLOR_YELLOW).build();
	public static final BlockMaterial SHULKER_SHELL = new BlockMaterial.Builder().mapColor(MapColor.COLOR_PURPLE).build();
	public static final BlockMaterial WOOD = new BlockMaterial.Builder().mapColor(MapColor.WOOD).ignitedByLava().noteBlockInstrument(NoteBlockInstrument.BASS).build();
	public static final BlockMaterial NETHER_WOOD = new BlockMaterial.Builder().mapColor(MapColor.WOOD).build();
	public static final BlockMaterial BAMBOO_SAPLING = new BlockMaterial.Builder().mapColor(MapColor.WOOD).ignitedByLava().pushReaction(PushReaction.DESTROY).build();
	public static final BlockMaterial BAMBOO = new BlockMaterial.Builder().mapColor(MapColor.WOOD).ignitedByLava().pushReaction(PushReaction.DESTROY).build();
	public static final BlockMaterial WOOL = new BlockMaterial.Builder().mapColor(MapColor.WOOL).ignitedByLava().build();
	public static final BlockMaterial EXPLOSIVE = new BlockMaterial.Builder().mapColor(MapColor.FIRE).ignitedByLava().build();
	public static final BlockMaterial LEAVES = new BlockMaterial.Builder().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).build();
	public static final BlockMaterial GLASS = new BlockMaterial.Builder().noteBlockInstrument(NoteBlockInstrument.HAT).build();
	public static final BlockMaterial ICE = new BlockMaterial.Builder().mapColor(MapColor.ICE).build();
	public static final BlockMaterial CACTUS = new BlockMaterial.Builder().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).build();
	public static final BlockMaterial STONE = new BlockMaterial.Builder().mapColor(MapColor.STONE).noteBlockInstrument(NoteBlockInstrument.BASEDRUM).build();
	public static final BlockMaterial METAL = new BlockMaterial.Builder().mapColor(MapColor.METAL).build();
	public static final BlockMaterial SNOW = new BlockMaterial.Builder().mapColor(MapColor.SNOW).build();
	public static final BlockMaterial HEAVY_METAL = new BlockMaterial.Builder().mapColor(MapColor.METAL).pushReaction(PushReaction.BLOCK).build();
	public static final BlockMaterial BARRIER = new BlockMaterial.Builder().pushReaction(PushReaction.BLOCK).build();
	public static final BlockMaterial PISTON = new BlockMaterial.Builder().mapColor(MapColor.STONE).pushReaction(PushReaction.BLOCK).build();
	public static final BlockMaterial MOSS = new BlockMaterial.Builder().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).build();
	public static final BlockMaterial VEGETABLE = new BlockMaterial.Builder().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).build();
	public static final BlockMaterial EGG = new BlockMaterial.Builder().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).build();
	public static final BlockMaterial CAKE = new BlockMaterial.Builder().pushReaction(PushReaction.DESTROY).build();
	public static final BlockMaterial AMETHYST = new BlockMaterial.Builder().mapColor(MapColor.COLOR_PURPLE).build();
	public static final BlockMaterial POWDER_SNOW = new BlockMaterial.Builder().mapColor(MapColor.SNOW).build();
	public static final BlockMaterial FROGSPAWN = new BlockMaterial.Builder().mapColor(MapColor.WATER).pushReaction(PushReaction.DESTROY).build();
	public static final BlockMaterial FLOGLIGHT = new BlockMaterial.Builder().build();
	public static final BlockMaterial DECORATED_POT = new BlockMaterial.Builder().mapColor(MapColor.TERRACOTTA_RED).pushReaction(PushReaction.DESTROY).build();

	protected MapColor mapColor = MapColor.NONE;
	protected PushReaction pushReaction = PushReaction.NORMAL;
	protected boolean liquid = false;
	protected boolean replaceable = false;
	protected boolean ignitedByLava = false;
	protected NoteBlockInstrument noteBlockInstrument = NoteBlockInstrument.HARP;

	public BlockBehaviour.Properties properties(MapColor color)
	{
		BlockBehaviour.Properties res = BlockBehaviour.Properties.of()
				.mapColor(color)
				.instrument(this.noteBlockInstrument)
				.pushReaction(this.pushReaction);
		if (this.liquid) res.liquid();
		if (this.replaceable) res.replaceable();
		if (this.ignitedByLava) res.ignitedByLava();
		return res;
	}
	
	public BlockBehaviour.Properties properties()
	{
		return properties(this.mapColor);
	}
	
	public static class Builder
	{
		protected MapColor mapColor = MapColor.NONE;
		protected PushReaction pushReaction = PushReaction.NORMAL;
		protected boolean liquid = false;
		protected boolean replaceable = false;
		protected boolean ignitedByLava = false;
		protected NoteBlockInstrument noteBlockInstrument = NoteBlockInstrument.HARP;
		
		public Builder() {}
		
		public Builder mapColor(MapColor value)
		{
			mapColor = value;
			return this;
		}
		
		public Builder noteBlockInstrument(NoteBlockInstrument value)
		{
			noteBlockInstrument = value;
			return this;
		}
		
		public Builder pushReaction(PushReaction value)
		{
			pushReaction = value;
			return this;
		}
		
		public Builder liquid()
		{
			liquid = true;
			return this;
		}
		
		public Builder replaceable()
		{
			replaceable = true;
			return this;
		}
		
		public Builder ignitedByLava()
		{
			ignitedByLava = true;
			return this;
		}

		public BlockMaterial build()
		{
			BlockMaterial res = new BlockMaterial();
			res.mapColor = mapColor;
			res.noteBlockInstrument = noteBlockInstrument;
			res.pushReaction = pushReaction;
			res.liquid = liquid;
			res.replaceable = replaceable;
			res.ignitedByLava = ignitedByLava;
			return res;
		}
	}

}
