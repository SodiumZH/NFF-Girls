package net.sodiumstudio.dwmg.befriendmobs.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event.Result;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.BefriendedAIState;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.BefriendedChangeAiStateEvent;
import net.sodiumstudio.dwmg.befriendmobs.entity.capability.CHealingHandlerImpl;
import net.sodiumstudio.dwmg.befriendmobs.entity.capability.CHealingHandlerImplDefault;
import net.sodiumstudio.dwmg.befriendmobs.inventory.AbstractInventoryMenuBefriended;
import net.sodiumstudio.dwmg.befriendmobs.inventory.BefriendedInventory;
import net.sodiumstudio.dwmg.befriendmobs.registry.BefMobCapabilities;
import net.sodiumstudio.dwmg.befriendmobs.util.MiscUtil;
import net.sodiumstudio.dwmg.befriendmobs.util.NbtHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.ReflectHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.Wrapped;

public interface IBefriendedMob extends ContainerListener  {

	/* Initialization */
	
	/** Initialize a mob.
	 * On reading from NBT, the befriendedFrom mob is null, so implementation must handle null cases.
	 * @param player Player who owns this mob.
	 * @param from The source mob from which this mob was befriended or converted. 
	 * E.g. a BefriendedZombie was befriended from a Zombie, or spawned from a Husk by water conversion.
	 * WARNING: Only on creating mob this value is valid. On reading from data it's null !!!
	 */
	public default void init(@Nonnull UUID playerUUID, @Nullable Mob from)
	{
		this.setOwnerUUID(playerUUID);
		if (from != null)
		{
			this.asMob().setHealth(from.getHealth());
		}
		this.setInventoryFromMob();
		this.updateAttributes();
		if (this.getAnchorPos() != null)
		{
			this.setAnchorPos(this.asMob().position());
		}
	}

	public boolean hasInit();
	
	// Call this to label a mob initialized after reading nbt, copying from other, etc.
	public void setInit();

	/* Ownership */
	
	// Get owner as player mob.
	// Warning: be careful calling this on initialization! If the owner hasn't been initialized it will return null.
	public default Player getOwner() 
	{
		if (getOwnerUUID() != null)
			return asMob().level.getPlayerByUUID(getOwnerUUID());
		else return null;
	}
	// Get owner as UUID.
	// Warning: be careful calling this on initialization! If the owner hasn't been initialized it will return null.
	public UUID getOwnerUUID();
	
	// Set owner from player mob.
	public default void setOwner(@Nonnull Player owner)
	{
		setOwnerUUID(owner.getUUID());
	}
	
	// Set owner from player UUID.
	public void setOwnerUUID(@Nonnull UUID ownerUUID);
	
	/* AI configs */
	
	public BefriendedAIState getAIState();
	
	// Action when switching AI e.g. on right click/
	public default BefriendedAIState switchAIState()
	{		
		BefriendedAIState nextState = getAIState().defaultSwitch();
		if (MinecraftForge.EVENT_BUS.post(new BefriendedChangeAiStateEvent(this, getAIState(), nextState)))
			return getAIState();
		setAIState(nextState);
		MiscUtil.printToScreen(this.asMob().getName().getString() + " " + this.getAIState().getDisplayInfo(), getOwner(), this.asMob());
		return nextState;
	}
	
	public void setAIState(BefriendedAIState state);
	
	// Get if a target mob can be attacked by this mob.
	public default boolean wantsToAttack(LivingEntity pTarget)
	{
		return BefriendedHelper.wantsToAttackDefault(this, pTarget);
	}
	
	// Get the previous target before updating target.
	// This function is only called on setting target. DO NOT CALL ANYWHERE ELSE!
	public LivingEntity getPreviousTarget();
	
	// Get the previous target after updating target.
	// This function is only called on setting target. DO NOT CALL ANYWHERE ELSE!
	public void setPreviousTarget(LivingEntity target);
	
	// Get the anchor pos that the mob won't stroll to far from it
	// Keep null == disable anchor
	public default Vec3 getAnchorPos() {return null;}
	
	public default void setAnchorPos(Vec3 pos) {}
	
	public default double getAnchoredStrollRadius()  {return 64.0d;}
	
	public default boolean isTooFarFromAnchor(Vec3 v)
	{
		Vec3 a = getAnchorPos();
		if (a == null)
			return false;
		double dx = v.x - a.x;
		double dz = v.z - a.z;
		return dx * dx + dz * dz > getAnchoredStrollRadius() * getAnchoredStrollRadius();		
	}
	
	public default boolean isTooFarFromAnchor(BlockPos pos)
	{
		return 	isTooFarFromAnchor(new Vec3(pos.getX(), pos.getY(), pos.getZ()));
	}
	
	public default void updateAnchor()
	{
		if (getAnchorPos() != null)
			setAnchorPos(asMob().position());
	}
	
	/* Interaction */
	
	// Actions on player right click the mob
	// Deprecated, use mobInteraction() instead
	@Deprecated
	public default boolean onInteraction(Player player, InteractionHand hand)
	{
		return false;
	}
	
	// Actions on player shift + rightmouse click
	// Deprecated, use mobInteraction() instead
	@Deprecated
	public default boolean onInteractionShift(Player player, InteractionHand hand)
	{
		return false;
	}
	
	/* Inventory */
	
	public BefriendedInventory getAdditionalInventory();

	public int getInventorySize();
	
	// Set mob data from befriendedInventory.
	public void updateFromInventory();
	
	// Set befriendedInventory from mob data
	public void setInventoryFromMob();
	
	// Get item stack from position in inventory tag
	public default ItemStack getInventoryItemStack(int pos)
	{
		if (pos < 0 || pos >= getInventorySize())
			throw new IndexOutOfBoundsException();
		return this.getAdditionalInventory().getItem(pos);
	}
	
	// Get item (type) from position in inventory tag
	public default Item getInventoryItem(int pos)
	{
		return this.getInventoryItemStack(pos).getItem();
	}


	public AbstractInventoryMenuBefriended makeMenu(int containerId, Inventory playerInventory, Container container);

	/* ContainerListener interface */
	// DO NOT override this. Override onInventoryChanged instead.
	@Override
	public default void containerChanged(Container pContainer) 
	{
		if (!(pContainer instanceof BefriendedInventory))
			throw new UnsupportedOperationException("IBefriendedMob container only receives BefriendedInventory.");
		if (hasInit())
			updateFromInventory();
		updateAttributes();
		onInventoryChanged();
	}

	public default void onInventoryChanged() 
	{
	}

	/* Healing related */	

	public default Class<? extends CHealingHandlerImpl> healingHandlerClass()
	{
		return CHealingHandlerImplDefault.class;
	}

	public default boolean applyHealingItem(ItemStack stack, float value, boolean consume)
	{
		Wrapped.Boolean succeeded = new Wrapped.Boolean(false);		
		this.asMob().getCapability(BefMobCapabilities.CAP_HEALING_HANDLER).ifPresent((l) ->
		{
			succeeded.set(l.applyHealingItem(stack, value, consume));
		});		
		return succeeded.get();
	}
	
	public default HashMap<Item, Float> getHealingItems()
	{
		return new HashMap<Item, Float>();
	}
	
	public default HashSet<Item> getNonconsumingHealingItems()
	{	
		return new HashSet<Item>();
	}
	
	public default InteractionResult tryApplyHealingItems(ItemStack stack)
	{
		if (stack.isEmpty())
			return InteractionResult.PASS;
		if (getHealingItems().containsKey(stack.getItem()))
		{
			return applyHealingItem(stack, getHealingItems().get(stack.getItem()), true) ? InteractionResult.SUCCESS : InteractionResult.FAIL;
		}
		else if (getNonconsumingHealingItems().contains(stack.getItem()))
		{
			return applyHealingItem(stack, getHealingItems().get(stack.getItem()), false) ? InteractionResult.SUCCESS : InteractionResult.FAIL;
		}
		return InteractionResult.PASS;
	}
	
	/* Respawn */
	public default boolean shouldDropRespawner()
	{
		return true;
	}
	
	public default boolean isRespawnerInvulnerable()
	{
		return true;
	}
	
	public default boolean shouldRespawnerRecoverOnDropInVoid()
	{
		return true;
	}
	
	public default boolean respawnerNoExpire()
	{
		return true;
	}
	
	/* Misc */
	
	@Deprecated	// Update in tick() or in bauble handler
	public default void updateAttributes() {};

	public default Mob asMob()
	{
		return (Mob)this;
	}
	
	public default IBefriendedMob self()
	{
		return this;
	}

	public String getModId();
	
}
