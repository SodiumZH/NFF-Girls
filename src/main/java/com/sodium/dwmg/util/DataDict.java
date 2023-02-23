package com.sodium.dwmg.util;

import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.StringTag;
import net.minecraftforge.common.util.INBTSerializable;

/** DataDict is a hash map that stores generic data, just like Python dict
* Current supported data type: int(using Integer), boolean(using Boolean), float(using Float), double(using Double), String, UUID
* Not supporting array now
*/
@Deprecated
public class DataDict implements INBTSerializable<CompoundTag>{
	
	private Map<String, Object> dict;
	
	public DataDict()
	{
		dict.clear();
	}
	
	public <T extends Object> void put(String key, T value)
	{
		dict.put(key, value);
	}
	
	/* Get functions*/
	
	public Object getRaw(String key)
	{
		if (!dict.keySet().contains(key))
			throw new IllegalArgumentException("DataDict: invalid key.");
		return dict.get(key);
	}
	
	public int getInt(String key)
	{
		return ((Integer)getRaw(key)).intValue();
	}
	
	public boolean getBoolean(String key)
	{
		return ((Boolean)getRaw(key)).booleanValue();
	}
	
	public float getFloat(String key)
	{
		return ((Float)getRaw(key)).floatValue();
	}
	
	public double getDouble(String key)
	{
		return ((Double)dict.get(key)).doubleValue();
	}
	
	public String getString(String key)
	{
		return (String)getRaw(key);
	}

	public UUID getUUID(String key)
	{
		return ((UUID)dict.get(key));
	}
	
	/* Get functions end */
	
	@Override
	public CompoundTag serializeNBT() {
		CompoundTag nbt = new CompoundTag();
		for (String key: dict.keySet())
		{
			switch(getDataType(key))
			{
			case INT:
			{
				nbt.put(key, IntTag.valueOf(getInt(key)));
				break;
			}
			case BOOLEAN:
			{
				nbt.put(key, ByteTag.valueOf(getBoolean(key)));
				break;
			}
			case FLOAT:
			{
				nbt.put(key, FloatTag.valueOf(getFloat(key)));
				break;
			}
			case DOUBLE:
			{
				nbt.put(key, DoubleTag.valueOf(getDouble(key)));
				break;
			}
			case STRING:
			{
				nbt.put(key, StringTag.valueOf(getString(key)));
				break;
			}
			case UUID:
				nbt.put(key, NbtUtils.createUUID(getUUID(key)));
				break;
			}
		}
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		dict.clear();
		throw new UnsupportedOperationException("DataDict: deserializeNBT not implemented.");
	}
	
	public enum DataType
	{
		INT,
		BOOLEAN,
		FLOAT,
		DOUBLE,
		STRING,
		UUID
	}
	
	public DataType getDataType(String key)
	{
		Object val = getRaw(key);
		if (val instanceof Integer)
			return DataType.INT;
		else if (val instanceof Boolean)
			return DataType.BOOLEAN;
		else if (val instanceof Float)
			return DataType.FLOAT;
		else if (val instanceof Double)
			return DataType.DOUBLE;
		else if (val instanceof String)
			return DataType.STRING;
		else if (val instanceof UUID)
			return DataType.UUID;
		else throw new UnsupportedOperationException("DataDict: unsupported data type.");
	}
	
}
