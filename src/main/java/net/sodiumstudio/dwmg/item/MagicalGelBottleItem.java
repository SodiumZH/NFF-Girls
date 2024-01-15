package net.sodiumstudio.dwmg.item;

import java.util.List;

import javax.annotation.Nullable;

import com.mojang.logging.LogUtils;

import com.github.mechalopa.hmag.registry.ModEntityTypes;
import com.github.mechalopa.hmag.world.entity.MagicalSlimeEntity;
import com.github.mechalopa.hmag.world.entity.SlimeGirlEntity;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.sodiumstudio.dwmg.entities.hmag.HmagSlimeGirlEntity;
import net.sodiumstudio.dwmg.registries.DwmgItems;
import net.sodiumstudio.nautils.InfoHelper;
import net.sodiumstudio.nautils.ItemHelper;
import net.sodiumstudio.nautils.NbtHelper;
import net.sodiumstudio.nautils.math.HtmlColors;
import net.sodiumstudio.nautils.math.LinearColor;
import net.sodiumstudio.nautils.math.RndUtil;

public class MagicalGelBottleItem extends Item
{

	public static final LinearColor VANILLA_SLIME_COLOR = LinearColor.fromRGB(100, 172, 81);
	public static final LinearColor MAGMA_CUBE_COLOR = LinearColor.fromRGB(220, 114, 32);
	
	public MagicalGelBottleItem(Properties pProperties)
	{
		super(pProperties.stacksTo(1));
	}

	protected void checkStackType(ItemStack stack)
	{
		if (!(stack.getItem() instanceof MagicalGelBottleItem))
			throw new IllegalArgumentException("Wrong item stack type!");
	}
	
	public static ItemStack create(MagicalGelBottleItem type)
	{
		ItemStack stack = new ItemStack(type);
		stack.getOrCreateTag().put("amount", IntTag.valueOf(1));
		stack.getTag().put("red", DoubleTag.valueOf(0));
		stack.getTag().put("green", DoubleTag.valueOf(0));
		stack.getTag().put("blue", DoubleTag.valueOf(0));
		stack.getTag().put("show_color", ByteTag.valueOf(true));
		return stack;
	}

	public static ItemStack create(MagicalGelBottleItem type, LinearColor color)
	{
		ItemStack stack = create(type);
		type.setColor(stack, color);
		return stack;
	}
	
	protected boolean checkValid(ItemStack stack)
	{
		checkStackType(stack);
		return stack.hasTag() && stack.getTag().contains("amount", NbtHelper.TAG_INT_ID)
				&& stack.getTag().contains("red", NbtHelper.TAG_DOUBLE_ID)
				&& stack.getTag().contains("green", NbtHelper.TAG_DOUBLE_ID) 
				&& stack.getTag().contains("blue", NbtHelper.TAG_DOUBLE_ID)
				&& stack.getTag().contains("show_color", NbtHelper.TAG_BYTE_ID);
	}
		
	public int getAmount(ItemStack stack)
	{
		if (!checkValid(stack))
		{
			LogUtils.getLogger().error("MagicalGelBottleItem stack missing NBT. Always use create() to create MagicalGelBottleItem stack instead of ItemStack#new!");
			return 0;
		}
		return stack.getTag().getInt("amount");
	}
	
	public void setAmount(ItemStack stack, int value)
	{
		if (!checkValid(stack))
		{
			LogUtils.getLogger().error("MagicalGelBottleItem stack missing NBT. Always use create() to create MagicalGelBottleItem stack instead of ItemStack#new!");
			return;
		}
		if (value <= 0)
			throw new IllegalArgumentException("setAmount: must be positive.");
		stack.getOrCreateTag().put("amount", IntTag.valueOf(Math.max(value, 0)));
	}
	
	public LinearColor getColor(ItemStack stack)
	{
		if (!checkValid(stack))
		{
			LogUtils.getLogger().error("MagicalGelBottleItem stack missing NBT. Always use create() to create MagicalGelBottleItem stack instead of ItemStack#new!");
			return LinearColor.fromNormalized(0, 0, 0);
		}
		return LinearColor.fromNormalized(stack.getTag().getDouble("red"), stack.getTag().getDouble("green"), stack.getTag().getDouble("blue"));
	}

	public void setColor(ItemStack stack, LinearColor color)
	{
		if (!checkValid(stack))
		{
			LogUtils.getLogger().error("MagicalGelBottleItem stack missing NBT. Always use create() to create MagicalGelBottleItem stack instead of ItemStack#new!");
			return;
		}
		stack.getTag().put("red", DoubleTag.valueOf(color.r));
		stack.getTag().put("green", DoubleTag.valueOf(color.g));
		stack.getTag().put("blue", DoubleTag.valueOf(color.b));
	}

	public boolean getShowColor(ItemStack stack)
	{
		if (!checkValid(stack))
		{
			LogUtils.getLogger().error("MagicalGelBottleItem stack missing NBT. Always use create() to create MagicalGelBottleItem stack instead of ItemStack#new!");
			return true;
		}
		else return stack.getTag().getBoolean("show_color");
	}
	
	public void setShowColor(ItemStack stack, boolean value)
	{
		if (!checkValid(stack))
		{
			LogUtils.getLogger().error("MagicalGelBottleItem stack missing NBT. Always use create() to create MagicalGelBottleItem stack instead of ItemStack#new!");
			return;
		}
		stack.getTag().putBoolean("show_color", value);
	}
	
	public void stain(ItemStack stack, LinearColor newColor, double strength) 
	{
		if (strength <= 0d)
			return;
		double alpha = ((double) strength) / ((double)getAmount(stack) + strength);
		this.setColor(stack, LinearColor.lerp(this.getColor(stack), newColor, alpha));
	}
	
	public void blend(ItemStack stack, LinearColor newColor, int amount)
	{
		this.stain(stack, newColor, amount);
		this.setAmount(stack, this.getAmount(stack) + amount);
	}
	
	public boolean extract(ItemStack stack, LinearColor extractColor, int amount)
	{
		int oldAmount = getAmount(stack);
		LinearColor oldColor = getColor(stack);
		if (amount <= 0)
			return true;
		if (amount >= oldAmount)
		{
			throw new UnsupportedOperationException("Extract operation doesn't support taking all.");
		}
		else
		{
			double newR = (oldColor.r * oldAmount - extractColor.r * amount) / (double) (oldAmount - amount);
			double newG = (oldColor.g * oldAmount - extractColor.g * amount) / (double) (oldAmount - amount);
			double newB = (oldColor.b * oldAmount - extractColor.b * amount) / (double) (oldAmount - amount);
			if (newR < 0 || newG < 0 || newB < 0)
			{
				return false;
			}
			else
			{
				setAmount(stack, oldAmount - amount);
				setColor(stack, LinearColor.fromNormalized(newR, newG, newB));
				return true;
			}
		}
	}
	
	public boolean extract(ItemStack stack, SlimeGirlEntity.ColorVariant extractColor, int amount) 
	{
		return extract(stack, MagicalGelColorUtils.getSlimeColor(extractColor), amount);
	}
	
	public boolean extractClosestSlimeColor(ItemStack stack, int amount)
	{
		return extract(stack, MagicalGelColorUtils.closestVariant(getColor(stack)), amount);
	}
	
	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity living, InteractionHand usedHand)
	{
		if (!player.level.isClientSide)
		{
			if (this.getAmount(stack) <= 0)
			{
				stack.shrink(1);
				player.spawnAtLocation(DwmgItems.EMPTY_MAGICAL_GEL_BOTTLE.get().getDefaultInstance()).setNoPickUpDelay();//ItemHelper.giveOrDropDefault(player, DwmgItems.EMPTY_MAGICAL_GEL_BOTTLE.get());
			}
			// Action type: 0 => no action; 1 => collecting; 2 => staining
			int action = 0;
			// Blend magical slime
			if (living instanceof MagicalSlimeEntity ms && living.getType() == ModEntityTypes.MAGICAL_SLIME.get())
			{
				if (ms.isTiny())
				{
					this.blend(stack, MagicalGelColorUtils.getSlimeColor(ms), 1);
					ms.discard();
					action = 1;
				}
			}
			// Blend vanilla slime
			/*else if (living instanceof Slime sl && living.getType() == EntityType.SLIME)
			{
				if (sl.isTiny())
				{
					this.blend(stack, VANILLA_SLIME_COLOR, 1);
					sl.discard();
					action = 1;
				}
			}
			// Blend vanilla magma cube
			else if (living instanceof MagmaCube mc && living.getType() == EntityType.MAGMA_CUBE)
			{
				if (mc.isTiny())
				{
					this.blend(stack, MAGMA_CUBE_COLOR, 1);
					mc.discard();
					action = 1;
				}
			} */
			// Stain slime girl
			else if (living instanceof HmagSlimeGirlEntity sg && sg.isOwnerPresent() && sg.getOwner() == player)
			{
				sg.stain(this.getColor(stack));
				if (sg.getRandom().nextDouble() < 0.25d)
					sg.spawnAtLocation(DwmgItems.MAGICAL_GEL_BALL.get());
				action = 2;
			}
			
			// Handle item change
			if (action == 1)
			{
				// The max volume is 6; if trying adding more, drop a magical gel ball after blending
				while (getAmount(stack) > 6)
				{
					setAmount(stack, getAmount(stack) - 1);
					ItemStack ball = new ItemStack(DwmgItems.MAGICAL_GEL_BALL.get());
					if (!player.addItem(ball))
						player.spawnAtLocation(ball);
				}
				ItemStack stack1 = stack.copy();
				stack.shrink(1);
				player.spawnAtLocation(stack1, 1f).setNoPickUpDelay();
				return InteractionResult.sidedSuccess(living.level.isClientSide);
			}
			else if (action == 2)
			{
				if (!player.isCreative())
				{
					if (getAmount(stack) == 1)
					{
						stack.shrink(1);
						ItemStack stack1 = DwmgItems.EMPTY_MAGICAL_GEL_BOTTLE.get().getDefaultInstance();
						player.spawnAtLocation(stack1, 1f).setNoPickUpDelay();
					}
					else
					{
						ItemStack stack1 = stack.copy();
						this.setAmount(stack1, this.getAmount(stack1) - 1);
						stack.shrink(1);
						player.spawnAtLocation(stack1, 1f).setNoPickUpDelay();
					}
				}
				return InteractionResult.sidedSuccess(living.level.isClientSide);
			}
			
		}
		return InteractionResult.PASS;
	}
	
	@Override
	public ItemStack getDefaultInstance()
	{
		return create(this);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag)
	{
		super.appendHoverText(stack, level, list, tooltipFlag);
		list.add(InfoHelper.createTranslatable("item.dwmg.magical_gel_bottle.amount", Integer.toString(this.getAmount(stack))));
		if (this.getShowColor(stack))
		{
			Vec3i rgb = getColor(stack).toRGB();
			String rgbInfo = " (R" + Integer.toString(rgb.getX()) + ", G" + Integer.toString(rgb.getY()) + ", B" + Integer.toString(rgb.getZ()) +")";
			list.add(InfoHelper.createTranslatable("item.dwmg.magical_gel_bottle.color")
					.append(InfoHelper.createTranslatable("color.dwmg.html." + HtmlColors.getNearestHtmlColor(this.getColor(stack))))
					.append(InfoHelper.createText(rgbInfo))
					.withStyle(Style.EMPTY.withColor(this.getColor(stack).toCode())));
		}
		else
		{
			list.add(InfoHelper.createTranslatable("item.dwmg.magical_gel_bottle.color_unknown"));
			list.add(InfoHelper.createTranslatable("item.dwmg.magical_gel_bottle.check_color"));
		}
	}
}
