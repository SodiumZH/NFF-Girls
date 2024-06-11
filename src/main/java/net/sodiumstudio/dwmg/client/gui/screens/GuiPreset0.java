package net.sodiumstudio.dwmg.client.gui.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.sodiumstudio.befriendmobs.client.gui.screens.BefriendedGuiScreen;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.entities.IDwmgBefriendedMob;
import net.sodiumstudio.nautils.InfoHelper;
import net.sodiumstudio.nautils.info.ComponentBuilder;
import net.sodiumstudio.nautils.math.GuiPos;

/** GUI template for all vanilla-undead-mob-like befriended mobs.
*/

@OnlyIn(Dist.CLIENT)
public class GuiPreset0 extends BefriendedGuiScreen {
	
	protected static final ResourceLocation DEFAULT_TEXTURE_LOC = new ResourceLocation(Dwmg.MOD_ID, "textures/gui/container/gui_preset_0.png");
	protected static final GuiPos DEFAULT_TEXTURE_SIZE = new GuiPos(512, 256);
	protected int mobRenderScale = 25;
	@Deprecated
	protected MobRenderBoxStyle mobRenderBoxStyle = MobRenderBoxStyle.DARK;
	protected int page = 0;
	protected int maxPage = 1;
	
	public GuiPreset0(BefriendedInventoryMenu menu, Inventory playerInventory, IBefriendedMob mob) {
		super(menu, playerInventory, mob, true);
		imageWidth = 224;
		imageHeight = 183;
		inventoryLabelY = imageHeight - 93;
		this.setTexture(DEFAULT_TEXTURE_LOC, DEFAULT_TEXTURE_SIZE);
	}

	@Override
	protected void init() {
		super.init();
		//this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
	}

	public GuiPos getEntityRenderPosition()
	{
		return new GuiPos(52, 80);
	}
	
	public int getMobRenderScale()
	{
		return mobRenderScale;
	}
	
	public GuiPreset0 setMobRenderScale(int value)
	{
		mobRenderScale = value;
		return this;
	}
	
	@Deprecated
	public GuiPreset0 setMobRenderBoxStyle(MobRenderBoxStyle style)
	{
		this.mobRenderBoxStyle = style;
		return this;
	}
	
	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		super.render(graphics, mouseX, mouseY, partialTick);
	}

	public void renderItemSlots()
	{
		
	}
	
	// Add hp, atk and armor only
	public void addBasicAttributeInfo(GuiGraphics graphics, GuiPos position, int color, int textRowWidth)
	{
		super.addAttributeInfo(graphics, position, color, textRowWidth);
	}

	public void addFavorabilityAndLevelInfo(GuiGraphics graphics, GuiPos position, int color, int textRowWidth)
	{
		graphics.drawString(font, getDefaultLevelInfo(), position.x, position.y, color, false);
		position = position.addY(textRowWidth);
		graphics.drawString(font, getDefaultExpInfo(), position.x, position.y, color, false);
		position = position.addY(textRowWidth);
		graphics.drawString(font, getDefaultFavInfo(), position.x, position.y, color, false);
	}
	
	protected MutableComponent getDefaultLevelAndExpInfo()
	{
		IDwmgBefriendedMob bm = (IDwmgBefriendedMob)mob;		
		String lv = Integer.toString(bm.getLevelHandler().getExpectedLevel());
		String exp = Long.toString(bm.getLevelHandler().getExpInThisLevel());
		String expup = Long.toString(bm.getLevelHandler().getRequiredExpInThisLevel());
		return ComponentBuilder.create().appendTranslatable("info.dwmg.gui_level_and_exp")
				.appendText(": " + lv + " (" + exp + " / " + expup + ")").build();
	}
	
	protected MutableComponent getDefaultLevelInfo()
	{
		IDwmgBefriendedMob bm = (IDwmgBefriendedMob)mob;		
		String lv = Integer.toString(bm.getLevelHandler().getExpectedLevel());
		return InfoHelper.createTranslatable("info.dwmg.gui_level")
				.append(InfoHelper.createText(": " + lv));	
	}
	
	protected MutableComponent getDefaultExpInfo()
	{
		IDwmgBefriendedMob bm = (IDwmgBefriendedMob)mob;
		String exp = Long.toString(bm.getLevelHandler().getExpInThisLevel());
		String expup = Long.toString(bm.getLevelHandler().getRequiredExpInThisLevel());
		return InfoHelper.createTranslatable("info.dwmg.gui_exp")
				.append(InfoHelper.createText(": " + exp + " / " + expup));
		
	}
	
	protected MutableComponent getDefaultFavInfo()
	{
		IDwmgBefriendedMob bm = (IDwmgBefriendedMob)mob;	
		String fav = Integer.toString(Mth.floor(bm.getFavorabilityHandler().getFavorability()));
		String favmax = Integer.toString(Mth.floor(bm.getFavorabilityHandler().getMaxFavorability()));
		return InfoHelper.createTranslatable("info.dwmg.gui_favorability")
				.append(InfoHelper.createText(": " + fav + " / " + favmax));
	}
	
	@Override
	public void addAttributeInfo(GuiGraphics graphics, GuiPos position, int color, int textRowWidth)
	{
		this.addBasicAttributeInfo(graphics, position, color, textRowWidth);
		position = position.addY(textRowWidth * 3);
		this.addFavorabilityAndLevelInfo(graphics, position, color, textRowWidth);
	}

	@Override
	public void addAttributeInfo(GuiGraphics graphics, GuiPos position)
	{
		addAttributeInfo(graphics, position, 0x404040, 11);
	}
	
	/** Below are texture-specific, must using gui_preset_0.png */
	
	protected GuiPos screenSize()
	{
		return GuiPos.valueOf(224, 183);
	}
	
	protected GuiPos basePos()
	{
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		return GuiPos.valueOf(i, j); 
	}
	
	// Absolute position from relative position of screen
	protected GuiPos absPos(int x, int y)
	{
		return basePos().add(x, y);
	}
	
	protected GuiPos absPos(GuiPos xy)
	{
		return basePos().add(xy);
	}
	
	protected GuiPos leftRowPos()
	{
		return absPos(7, 17);
	}
	
	protected GuiPos rightRowPos()
	{
		return absPos(79, 17);
	}
	
	protected void addMainScreen(GuiGraphics graphics)
	{
		this.drawSprite(graphics, basePos(), GuiPos.zero(), screenSize());
	}
	
	protected void addSlotBg(GuiGraphics graphics, int slotIndex, GuiPos pos, int slotIndexX, int slotIndexY)
	{
		this.addSlotBg(graphics, slotIndex, pos, GuiPos.valueOf(256, 0).coord(slotIndexX, slotIndexY), GuiPos.valueOf(256, 0));
	}

	protected void addBaubleSlotBg(GuiGraphics graphics, int slotIndex, GuiPos pos)
	{
		this.addSlotBg(graphics, slotIndex, pos, 1, 2);
	}

	@Deprecated
	public void addMobRenderBox(GuiGraphics graphics, int variation)
	{
		this.drawSprite(graphics, absPos(27, 17), GuiPos.valueOf(120 + variation * 50, 183), GuiPos.valueOf(50, 72));
	}
	

	public void addMobRenderBox(GuiGraphics graphics)
	{
		this.drawSprite(graphics, absPos(27, 17), GuiPos.valueOf(120 + this.mobRenderBoxStyle.getIndex() * 50, 183), GuiPos.valueOf(50, 72));
	}
	
	public void addMobRenderBox(GuiGraphics graphics, MobRenderBoxStyle style)
	{
		this.drawSprite(graphics, absPos(27, 17), GuiPos.valueOf(120 + style.getIndex() * 50, 183), GuiPos.valueOf(50, 72));
	}
	
	public void addInfoBox(GuiGraphics graphics)

	{
		this.drawSprite(graphics, absPos(99, 17), GuiPos.valueOf(0, 183), GuiPos.valueOf(120, 72));
	}

	public GuiPos infoPos()
	{
		return absPos(103, 21);
	}
	
	public void renderMob(GuiGraphics graphics, GuiPos offset)
	{
		GuiPos pos = absPos(getEntityRenderPosition().add(offset));
		InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, pos.x, pos.y, getMobRenderScale(), 
				(float) pos.x - this.xMouse, (float) (pos.y - 50) - this.yMouse, mob.asMob());
	}
	
	public void renderMob(GuiGraphics graphics)
	{
		renderMob(graphics, GuiPos.valueOf(0));
	}
	
	public static enum MobRenderBoxStyle
	{
		LIGHT(0),
		NORMAL(1),
		DARK(2);
		private int index;
		private MobRenderBoxStyle(int index)
		{
			this.index = index;
		}
		public int getIndex()
		{
			return index;
		}
		
	}
	
}

