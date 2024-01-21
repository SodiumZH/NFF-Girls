package net.sodiumstudio.dwmg.entities.item.baublesystem;

import net.minecraft.network.chat.MutableComponent;
import net.sodiumstudio.befriendmobs.item.baublesystem.BaubleItem;

@Deprecated
public class DwmgBaubleItem extends BaubleItem
{

	// TODO: migrate to BM
	protected String typeName = "dwmg_bauble_item";
	protected int level = 1;
	
	public DwmgBaubleItem(Properties pProperties)
	{
		super(pProperties);
	}

	public boolean is(String typeName)
	{
		return this.typeName.equals(typeName);
	}
	
	public boolean isLevel(int level)
	{
		return this.level == level;
	}
	
	public boolean is(String typeName, int level)
	{
		return this.typeName.equals(typeName) && this.level == level;
	}
	
	public DwmgBaubleItem typeName(String name)
	{
		this.typeName = name;
		return this;
	}
	
	public DwmgBaubleItem level(int value)
	{
		if (value <= 0)
			throw new IllegalArgumentException("Level must be positive");
		this.level = value;
		return this;
	}
	
	public String getTypeName()
	{
		return typeName;
	}
	
	public int getLevel()
	{
		return level;
	}
	
	@Override
	public DwmgBaubleItem description(MutableComponent desc)
	{
		return (DwmgBaubleItem) super.description(desc);
	}
	
	@Override
	public DwmgBaubleItem foil()
	{
		return (DwmgBaubleItem) super.foil();
	}
}
