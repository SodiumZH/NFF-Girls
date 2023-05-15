package net.sodiumstudio.dwmg.entities.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.common.util.INBTSerializable;

public interface CLevelHandler extends INBTSerializable<LongTag>
{
	
	public Mob getMob();
	
	public long getExp();
	
	public void setExp(long val);
	
	public void addExp(long deltaVal);
	
	public int getExpectedLevel();
	
	public int getExpInThisLevel();
	
	public int getRequiredExpInThisLevel();
	
	public static class Impl implements CLevelHandler
	{

		protected final Mob mob;
		protected long exp = 0;
		protected int lvl = 0;
		
		public Impl(Mob mob)
		{
			this.mob = mob;
		}
		
		@Override
		public LongTag serializeNBT() {
			return LongTag.valueOf(exp);
		}

		@Override
		public void deserializeNBT(LongTag nbt) {
			exp = nbt.getAsLong();
			lvl = getExpectedLevel();
		}

		@Override
		public Mob getMob() {
			return mob;
		}

		@Override
		public long getExp() {
			return exp;
		}

		@Override
		public void setExp(long val) {
			
		}

		@Override
		public void addExp(long deltaVal) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public long[] getExpRequirementList() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getExpectedLevel() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getExpInThisLevel() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getRequiredExpInThisLevel() {
			// TODO Auto-generated method stub
			return 0;
		}
		
	}
	
	// ==============================
	
	public static class ChangeExpEvent extends Event
	{
		
	}
	
	
	// ==============================
	// Related constants / statics
	
	
	/**
	 * Get ACCUMULATED exp for upgrading to next level from this level.
	 * Identical to player exp table
	 */
	public static long getExpRequirement(int level)
	{
		if (level < 0)
			throw new IllegalArgumentException("Illegal level value");
		else if (level < 16)
			return level * level + level * 6;
		else 
		{
			double leveld = (double)level;
			if (level < 32)
				return Math.round(2.5d * leveld * leveld - 40.5d * leveld + 360d);
			else
				return Math.round(4.5d * leveld * leveld - 162.5 * leveld + 2220d);
		}
	}
}
