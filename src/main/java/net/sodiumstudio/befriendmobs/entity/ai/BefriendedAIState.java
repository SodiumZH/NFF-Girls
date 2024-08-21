package net.sodiumstudio.befriendmobs.entity.ai;

import java.util.Collection;
import java.util.HashMap;

import javax.annotation.Nullable;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.sodiumstudio.befriendmobs.BefriendMobs;
import net.sodiumstudio.nautils.InfoHelper;

public class BefriendedAIState {
	
	private static final HashMap<ResourceLocation, BefriendedAIState> STATES = new HashMap<ResourceLocation, BefriendedAIState>();
	
	public static final BefriendedAIState WAIT = new BefriendedAIState(BefriendMobs.MOD_ID, "wait");
	public static final BefriendedAIState FOLLOW = new BefriendedAIState(BefriendMobs.MOD_ID, "follow");
	public static final BefriendedAIState WANDER = new BefriendedAIState(BefriendMobs.MOD_ID, "wander");

	/**
	 * ID for the state. Must be unique.
	 */
	private final ResourceLocation id;
	
	private BefriendedAIState(ResourceLocation id)
	{
		this.id = id;
		if (STATES.containsValue(this))
			throw new IllegalArgumentException("Befriended AI State: duplicate state.");
		if (STATES.containsKey(id))
		{
			throw new IllegalArgumentException("Befriended AI State: duplicate id. \"" + this.toString() + "\" and \"" + STATES.get(id).toString()
				+ ". Please contact mod authors for compatibility issues.");
		}
			
		STATES.put(id, this);
	}

	private BefriendedAIState(String modId, String key)
	{
		this(new ResourceLocation(modId, key));
	}
	
	/** Unique ID */
	public ResourceLocation getId()
	{
		return this.id;
	}
	
	@Nullable
	public static BefriendedAIState fromID(ResourceLocation id)
	{
		BefriendedAIState res = STATES.get(id);	
		return res != null ? res : BefriendedAIState.WAIT;
	}
	
	@Nullable
	public static BefriendedAIState fromID(String modid, String key)
	{
		return fromID(new ResourceLocation(modid, key));
	}
	
	protected static final HashMap<BefriendedAIState, MutableComponent> DISPLAY_INFO = new HashMap<BefriendedAIState, MutableComponent>();
	static
	{
		DISPLAY_INFO.put(WAIT, InfoHelper.createTranslatable("info.befriendmobs.mob_wait"));
		DISPLAY_INFO.put(FOLLOW, InfoHelper.createTranslatable("info.befriendmobs.mob_follow"));
		DISPLAY_INFO.put(WANDER, InfoHelper.createTranslatable("info.befriendmobs.mob_wander"));
	}
	
	public static void putDisplayInfo(BefriendedAIState state, MutableComponent info)
	{
		DISPLAY_INFO.put(state, info);
	}
	
	public MutableComponent getDisplayInfo()
	{
		if (DISPLAY_INFO.containsKey(this))
			return DISPLAY_INFO.get(this).copy();
		else
		{
			BefriendMobs.LOGGER.error("AI State missing display info: " + this.toString());
			return InfoHelper.createText("");			
		}
	}

	/**
	 * @deprecated Legacy from enum. Use {@code getAllStates()} instead.
	 */
	@Deprecated
	public static Collection<BefriendedAIState> values()
	{
		return getAllStates();
	}
	
	/**
	 * Get all available ai states.
	 */
	public static Collection<BefriendedAIState> getAllStates()
	{
		return STATES.values();
	}
	
	
}
