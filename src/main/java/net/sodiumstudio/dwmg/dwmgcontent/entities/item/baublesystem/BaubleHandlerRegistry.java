package net.sodiumstudio.dwmg.dwmgcontent.entities.item.baublesystem;

import java.util.HashMap;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;

public class BaubleHandlerRegistry {
	
	protected static BaubleHandlerRegistry REGISTRY;
	
	protected BaubleHandlerRegistry()
	{
	}
	
	protected HashMap<EntityType<? extends Mob>, BaubleHandler> map = new HashMap<EntityType<? extends Mob>, BaubleHandler>();
	
	// Warning: before putting, make sure the mob has implemented IBaubleHolder interface!!!!
	@SuppressWarnings("unchecked")
	public static void put(EntityType<?> type, BaubleHandler handler)
	{
		REGISTRY.map.put((EntityType<? extends Mob>) type, handler);
	}
	
	public static BaubleHandler get(EntityType<?> type)
	{
		return REGISTRY.map.get(type);
	}
	
	@SuppressWarnings("unchecked")
	public static BaubleHandler get(Mob mob)
	{
		if (mob instanceof IBaubleHolder)
			return get(mob.getType());
		else throw new IllegalStateException("Entity type registered to BaubleHandlerRegistry requires the mob to implement IBaubleHolder interface. Current type: " + mob.getType().getRegistryName());
		
	}
	
}
