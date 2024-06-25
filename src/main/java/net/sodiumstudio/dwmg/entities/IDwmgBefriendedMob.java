package net.sodiumstudio.dwmg.entities;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import org.apache.commons.lang3.mutable.MutableObject;

import com.mojang.logging.LogUtils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.network.PacketDistributor;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.entity.capability.HealingItemTable;
import net.sodiumstudio.befriendmobs.entity.capability.wrapper.IAttributeMonitor;
import net.sodiumstudio.befriendmobs.item.MobRespawnerItem;
import net.sodiumstudio.befriendmobs.item.capability.wrapper.IItemStackMonitor;
import net.sodiumstudio.dwmg.entities.capabilities.CFavorabilityHandler;
import net.sodiumstudio.dwmg.entities.capabilities.CLevelHandler;
import net.sodiumstudio.dwmg.entities.vanillatrade.CDwmgTradeHandler;
import net.sodiumstudio.dwmg.network.ClientboundDwmgMobGeneralSyncPacket;
import net.sodiumstudio.dwmg.network.DwmgChannels;
import net.sodiumstudio.dwmg.registries.DwmgCapabilities;
import net.sodiumstudio.dwmg.registries.DwmgItems;
import net.sodiumstudio.dwmg.subsystem.baublesystem.DwmgBaubleStatics;
import net.sodiumstudio.nautils.annotation.DontCallManually;
import net.sodiumstudio.nautils.annotation.DontOverride;

public interface IDwmgBefriendedMob extends IBefriendedMob, /*IBaubleEquipable, */IAttributeMonitor, IItemStackMonitor
{
	
	/**
	 * Check if a mob has a Dwmg BM interface.
	 * <p>
	 * As IBefriendedMob could also be implemented in capabilities instead of the mob class in the future,
	 * always use this instead of {@code instanceof} check.
	 */
	public static boolean isBM(Object o)
	{
		if (o == null) return false;
		if (o instanceof IDwmgBefriendedMob bm)
			return true;
		else return false;
	}
	
	/**
	 * Cast a mob to the Dwmg BM interface. Null if failed.
	 * <p>
	 * As IBefriendedMob could also be implemented in capabilities instead of the mob class in the future,
	 * always use this to cast a mob to BM.
	 */
	@Nullable
	public static IDwmgBefriendedMob getBM(Object o)
	{
		if (o == null) return null;
		if (o instanceof IDwmgBefriendedMob bm)
			return bm;
		else return null;
	}
	
	/**
	 * Do an action if a mob has a Dwmg BM interface.
	 * <p>
	 * As IBefriendedMob could also be implemented in capabilities instead of the mob class in the future,
	 * you can use this to safely cast and do things to BM.
	 * @return Whether the action is invoked.
	 */
	public static boolean ifBM(Object o, Consumer<IDwmgBefriendedMob> action)
	{
		if (o == null) return false;
		if (IDwmgBefriendedMob.isBM(o))
		{
			action.accept(IDwmgBefriendedMob.getBM(o));
			return true;
		}
		else return false;
	}
	
	/**
	 * Check if a mob has a Dwmg BM interface and satisfied the given condition.
	 * <p>
	 * As IBefriendedMob could also be implemented in capabilities instead of the mob class in the future,
	 * always use this instead of {@code instanceof} check and followed checks of the cast BM.
	 */
	public static boolean isBMAnd(Object o, Predicate<IDwmgBefriendedMob> cond)
	{
		if (!isBM(o)) return false;
		return cond.test(getBM(o));
	}
	
	@DontOverride
	public default CFavorabilityHandler getFavorabilityHandler()
	{
		MutableObject<CFavorabilityHandler> cap = new MutableObject<CFavorabilityHandler>(null);
		asMob().getCapability(DwmgCapabilities.CAP_FAVORABILITY_HANDLER).ifPresent((c) -> 
		{
			cap.setValue(c);
		});
		if (cap.getValue() == null)
		{
			LogUtils.getLogger().error("Missing CFavorabilityHandler capability");
			return new CFavorabilityHandler.Impl(this.asMob());
		}
		return cap.getValue();
	}
	
	@DontOverride
	public default CLevelHandler getLevelHandler()
	{
		MutableObject<CLevelHandler> cap = new MutableObject<CLevelHandler>(null);
		asMob().getCapability(DwmgCapabilities.CAP_LEVEL_HANDLER).ifPresent((c) -> 
		{
			cap.setValue(c);
		});
		if (cap.getValue() == null)
		{
			LogUtils.getLogger().error("Missing CLevelHandler capability");
			return new CLevelHandler.Impl(this.asMob());
		}
		return cap.getValue();
	}


	@DontOverride
	public default float getFavorability()
	{
		return this.getFavorabilityHandler().getFavorability();
	}
	
	/** Get the proportion of fav/maxfav, ranged 0-1 */
	@DontOverride
	public default float getNormalizedFavorability()
	{
		var cap = this.getFavorabilityHandler();
		return cap.getFavorability() / cap.getMaxFavorability();
	}
	
	@DontOverride
	public default float getXpLevel()
	{
		return this.getLevelHandler().getExpectedLevel();
	}
	
	@DontOverride
	public default float getOverallXp()
	{
		return this.getLevelHandler().getExp();
	}
	
	@DontOverride
	public default float getXpInThisLevel()
	{
		return this.getLevelHandler().getExpInThisLevel();
	}
	
	@DontOverride
	@DontCallManually
	public default void touchEntity(Entity other)
	{
		MinecraftForge.EVENT_BUS.post(new OverlapEntityEvent(this, other));
	}
	
	@Override
	public default double getAnchoredStrollRadius()  
	{
		return 16.0d;
	}
	
	/**
	 * Fired EVERY TICK when a befriended mob overlaps an entity.
	 */
	public static class OverlapEntityEvent extends Event
	{
		public final IBefriendedMob thisMob;
		public final Entity touchedEntity;
		public OverlapEntityEvent(IBefriendedMob thisMob, Entity touchedEntity)
		{
			this.thisMob = thisMob;
			this.touchedEntity = touchedEntity;
		}
	}

	/* Bauble related */
	
	/**
	 * @deprecated Use {@code DwmgBaubleStatics#countBaubles} instead
	 */
	@Deprecated
	public default boolean hasDwmgBauble(String typeName)
	{
		/*for (ItemStack stack: this.getBaubleSlots().values())
		{
			if (!stack.isEmpty() && stack.getItem() instanceof DwmgBaubleItem bauble && bauble.is(typeName))
				return true;
		}
		return false;*/
		return DwmgBaubleStatics.countBaubles(this.asMob(), new ResourceLocation(typeName)) > 0;
	}
	
	/**
	 * @deprecated Use {@code DwmgBaubleStatics#countBaublesWithTier} instead
	 */
	@Deprecated
	public default boolean hasDwmgBaubleWithLevel(String typeName, int level)
	{
		/*for (ItemStack stack: this.getBaubleSlots().values())
		{
			if (!stack.isEmpty() && stack.getItem() instanceof DwmgBaubleItem bauble && bauble.is(typeName, level))
				return true;
		}
		return false;*/
		return DwmgBaubleStatics.countBaublesWithTier(this.asMob(), new ResourceLocation(typeName), level) > 0;
	}
	
	/**
	 * @deprecated Use {@code DwmgBaubleStatics#countBaublesWithMinTier} instead
	 */
	@Deprecated
	public default boolean hasDwmgBaubleWithMinLevel(String typeName, int minLevel)
	{
		/*for (ItemStack stack: this.getBaubleSlots().values())
		{
			if (!stack.isEmpty() 
				&& stack.getItem() instanceof DwmgBaubleItem bauble 
				&& bauble.is(typeName)
				&& bauble.getLevel() >= minLevel)
			{
				return true;
			}
		}
		return false;*/
		return DwmgBaubleStatics.countBaublesWithMinTier(this.asMob(), new ResourceLocation(typeName), minLevel) > 0;
	}
	
	// === IBefriendedMob interface
	@Override
	public default MobRespawnerItem getRespawnerType()
	{
		return DwmgItems.MOB_RESPAWNER.get();
	}
	
	@Override
	public default DeathRespawnerGenerationType getDeathRespawnerGenerationType()
	{
		return DeathRespawnerGenerationType.GIVE;
	}
	
	@Deprecated
	@Override
	public default boolean dropInventoryOnDeath()
	{
		//return !this.asMob().level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY);
		return true;
	}
	
	// === IAttributeMonitor interface
	
	@Override
	public default void onAttributeChange(Attribute attr, double oldVal, double newVal) 
	{
		if (attr == Attributes.MAX_HEALTH)
		{
			this.asMob().setHealth((float) (this.asMob().getHealth() * newVal / oldVal));
		}
	}

	@Override
	public HealingItemTable getHealingItems();
	
	// === IItemStackMonitor interface
	
	private static UUID getSharpnessModifierUUID()
	{
		return UUID.fromString("9c12b503-63c0-43e6-bd30-d7aae9818c99");
	}
	
	@Override
	public default void onItemStackChange(String key, ItemStack oldStack, ItemStack newStack) {
		if (key.equals("main_hand"))
		{
			this.asMob().getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(getSharpnessModifierUUID());
			@SuppressWarnings("deprecation")
			int lv = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SHARPNESS, newStack);
			if (lv > 0)
			{
				this.asMob().getAttribute(Attributes.ATTACK_DAMAGE).addTransientModifier(new AttributeModifier(
						getSharpnessModifierUUID(), "sharpness_modifier", 0.5d + 0.5d * (double) lv, AttributeModifier.Operation.ADDITION));
			}
		}
	}
	
	// == Trade interface ==
	
	/**
	 * Ticks after sending a trade restock event.
	 * Note: a restock event doesn't necessarily restock all offers. The probability depends on the required level.
	 */
	public default int getRestockTicks()
	{
		return 600 * 20;
	}
	
	/**
	 * Get amount of trade entries for each level. Result[i] = level i+1.
	 */
	public default int[] getTradeEntryCountEachLevel()
	{
		return CDwmgTradeHandler.OFFER_COUNT_FOR_LEVEL;
	}
	
	public default int pointsPerIntroductionLetter()
	{
		return 128;
	}
	
	
	// ===================== Dwmg gamerules related ===================
	
	/**
	 * True if this mob should proactively attack mobs hostile to itself.
	 */
	public default boolean shouldAttackMobsHostileToSelf()
	{
		return DwmgBaubleStatics.countBaubles(this.asMob(), new ResourceLocation("dwmg:courage_amulet")) > 0;
	}
	
	/**
	 * True if this mob should proactively attack mobs hostile to its owner.
	 */
	public default boolean shouldAttackMobsHostileToOwner()
	{
		return DwmgBaubleStatics.countBaublesWithMinTier(this.asMob(), new ResourceLocation("dwmg:courage_amulet"), 2) > 0;
	}
	
	// ===== Network =========== //
	
	public default void doSync()
	{
		ClientboundDwmgMobGeneralSyncPacket packet = new ClientboundDwmgMobGeneralSyncPacket(this);
		if (this.isOwnerInDimension() && this.getOwner() instanceof ServerPlayer toPlayer)
			DwmgChannels.SYNC_CHANNEL.send(PacketDistributor.PLAYER.with(() -> toPlayer), packet);
	}
	
	
	// ===== Util ===
	
	/**
	 * @deprecated Only for old bauble system.
	 */
	@Deprecated
	public default HashMap<String, ItemStack> continuousBaubleSlots(int startIndex, int endIndexExclude)
	{
		HashMap<String, ItemStack> map = new HashMap<>();
		int j = 0;
		for (int i = startIndex; i < endIndexExclude; ++i)
		{
			map.put(Integer.toString(j), this.getAdditionalInventory().getItem(i));
			j++;
		}
		return map;
	}
}
