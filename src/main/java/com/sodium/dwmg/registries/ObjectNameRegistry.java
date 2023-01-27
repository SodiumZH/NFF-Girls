package com.sodium.dwmg.registries;

import java.util.HashMap;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ObjectNameRegistry<T> {

	// The hash map. Not directly accessible.
	private HashMap<String, RegistryObject<T>> Map = new HashMap<String, RegistryObject<T>>();
	
	/**
	 * 
	 * @param name Name to register.
	 * @param object Object to register.
	 * @param reg The deferred register reference, usually as a constant of a registry.
	 * @return Registered object as a RegistryObject, so you can still make a constant
	 */
	public RegistryObject<T> register(String name, T object, DeferredRegister<T> reg)
	{
		RegistryObject<T> result = reg.register(name, ()->object);
		Map.put(name, result);
		return result;
	}
	
	// Get object as raw type
	public T get(String name)
	{
		if(!Map.containsKey(name))
			throw new IllegalArgumentException(String.format("Object register name \"%s\" not found.",name));
		return Map.get(name).get();
	}
	
	public RegistryObject<T> getReg(String name)
	{
		if(!Map.containsKey(name))
			throw new IllegalArgumentException(String.format("Object register name \"%s\" not found.",name));
		return Map.get(name);
	}
	
}
