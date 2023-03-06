package com.sodium.dwmg.befriendmobsapi.util.math;

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
	
	public IntVec2 set(int x, int y)
	{
		this.x += x;
		this.y += y;
		return this;		
	}
	
	public IntVec2 add(IntVec2 other)
	{
		return new IntVec2(x + other.x, y + other.y);
	}
	
	public IntVec2 minus(IntVec2 other)
	{
		return new IntVec2(x - other.x, y - other.y);
	}
	
	public IntVec2 addX(int other)
	{
		return new IntVec2(x + other, y);
	}
	
	public IntVec2 addY(int other)
	{
		return new IntVec2(x, y + other);
	}
	
	public IntVec2 add(int other)
	{
		return new IntVec2(x + other, y + other);
	}
	
	/* Utility for inventory menu XY handling */
	
	public IntVec2 slotBelow(int n)
	{
		y = y + 18 * n;
		return this;
	}
	
	public IntVec2 slotBelow()
	{
		y = y + 18;
		return this;
	}
	
	public IntVec2 slotRight(int n)
	{
		x = x + 18 * n;
		return this;
	}
	
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

}
