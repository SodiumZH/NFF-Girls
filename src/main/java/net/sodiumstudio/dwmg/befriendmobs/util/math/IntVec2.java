package net.sodiumstudio.dwmg.befriendmobs.util.math;

public class IntVec2 
{
	public int x = 0;
	public int y = 0;
	
	public IntVec2(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public IntVec2(int val)
	{
		this(val, val);
	}
	
	public IntVec2()
	{
		this(0, 0);
	}
	
	public static IntVec2 of(int x, int y)
	{
		return new IntVec2(x, y);
	}
	
	public static IntVec2 of(int val)
	{
		return new IntVec2(val);
	}
	
	public static IntVec2 zero()
	{
		return new IntVec2();
	}
	
	public boolean equals(IntVec2 other)
	{
		return this.x == other.x && this.y == other.y;
	}
	
	public IntVec2 copy()
	{
		return new IntVec2(this.x, this.y);
	}
	
	public IntVec2 set(int x, int y)
	{
		this.x = x;
		this.y = y;
		return this;		
	}
	
	public IntVec2 add(IntVec2 other)
	{
		x += other.x;
		y += other.y;
		return this;
	}
	
	public IntVec2 minus(IntVec2 other)
	{
		x -= other.x;
		y -= other.y;
		return this;
	}
	
	public IntVec2 addX(int other)
	{
		return add(other, 0);
	}
	
	public IntVec2 addY(int other)
	{
		return add(0, other);
	}
	
	public IntVec2 add(int other)
	{
		return add(other, other);
	}
	
	public IntVec2 add(int x, int y)
	{
		this.x += x;
		this.y += y;
		return this;
	}
	
	/* Utility for inventoryTag menu XY handling */
	
	// Move an IntVec2 to the nth slot below
	// Set value!!
	public IntVec2 slotBelow(int n)
	{
		y = y + 18 * n;
		return this;
	}
	
	// Move an IntVec2 to the slot below
	// Set value!!
	public IntVec2 slotBelow()
	{
		y = y + 18;
		return this;
	}
	
	// Move an IntVec2 to the nth slot on the right
	// Set value!!
	public IntVec2 slotRight(int n)
	{
		x = x + 18 * n;
		return this;
	}
	
	// Move an IntVec2 to the nth slot on the right
	// Set value!!
	public IntVec2 slotRight()
	{
		x = x + 18;
		return this;
	}
		
	public IntVec2 slotAbove(int n) {
		y = y - 18 * n;
		return this;
	}
	
	public IntVec2 slotAbove() {
		y = y - 18;
		return this;
	}
	
	public IntVec2 slotLeft(int n) {
		x = x - 18 * n;
		return this;
	}
	
	public IntVec2 slotLeft() {
		x = x - 18;
		return this;
	}

	// Get slot coordinate from this as the base point.
	// e.g. for base point v, v.coord(1,2) means v.slotRight(1).slotBelow(2)
	// This method returns a new IntVec2 object and doesn't change the input vector.
	// Designed for locating slot in 2D asset array of item slots.
	public IntVec2 coord(int x, int y)
	{
		return new IntVec2(this.x + 18 * x, this.y + 18 * y);
	}
	
	
}
