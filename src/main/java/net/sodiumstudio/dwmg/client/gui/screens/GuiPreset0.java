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
import net.sodiumstudio.nautils.math.IntVec2;

/** GUI template for all vanilla-undead-mob-like befriended mobs.
*/

@OnlyIn(Dist.CLIENT)
public class GuiPreset0 extends BefriendedGuiScreen {
	
	protected int mobRenderScale = 25;
	protected MobRenderBoxStyle mobRenderBoxStyle = MobRenderBoxStyle.DARK;
	
	@Override
	public ResourceLocation getTextureLocation() {
		return new ResourceLocation(Dwmg.MOD_ID,
			"textures/gui/container/gui_preset_0.png");
	}
	
	@Override
	public IntVec2 getTextureSize()
	{
		return new IntVec2(512, 256);
	}
	
	public GuiPreset0(BefriendedInventoryMenu menu, Inventory playerInventory, IBefriendedMob mob) {
		super(menu, playerInventory, mob, true);
		imageWidth = 224;
		imageHeight = 183;
		inventoryLabelY = imageHeight - 93;
	}

	@Override
	protected void init() {
		super.init();
		//this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
	}

	public IntVec2 getEntityRenderPosition()
	{
		return new IntVec2(52, 80);
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
	public void addBasicAttributeInfo(GuiGraphics graphics, IntVec2 position, int color, int textRowWidth)
	{
		super.addAttributeInfo(graphics, position, color, textRowWidth);
	}
	
	public void addFavorabilityAndLevelInfo(GuiGraphics graphics, IntVec2 position, int color, int textRowWidth)
	{
		graphics.drawString(font, getDefaultLevelInfo(), position.x, position.y, color, false);
		position.addY(textRowWidth);
		graphics.drawString(font, getDefaultExpInfo(), position.x, position.y, color, false);
		position.addY(textRowWidth);
		graphics.drawString(font, getDefaultFavInfo(), position.x, position.y, color, false);
	}
	
	protected MutableComponent getDefaultLevelAndExpInfo()
	{
		IDwmgBefriendedMob bm = (IDwmgBefriendedMob)mob;		
		String lv = Integer.toString(bm.getLevelHandler().getExpectedLevel());
		String exp = Long.toString(bm.getLevelHandler().getExpInThisLevel());
		String expup = Long.toString(bm.getLevelHandler().getRequiredExpInThisLevel());
		return InfoHelper.builder().putTrans("info.dwmg.gui_level_and_exp")
				.putText(": " + lv + " (" + exp + " / " + expup + ")").build();
	}
	
	protected MutableComponent getDefaultLevelInfo()
	{
		IDwmgBefriendedMob bm = (IDwmgBefriendedMob)mob;		
		String lv = Integer.toString(bm.getLevelHandler().getExpectedLevel());
		return InfoHelper.createTrans("info.dwmg.gui_level")
				.append(InfoHelper.createText(": " + lv));	
	}
	
	protected MutableComponent getDefaultExpInfo()
	{
		IDwmgBefriendedMob bm = (IDwmgBefriendedMob)mob;
		String exp = Long.toString(bm.getLevelHandler().getExpInThisLevel());
		String expup = Long.toString(bm.getLevelHandler().getRequiredExpInThisLevel());
		return InfoHelper.createTrans("info.dwmg.gui_exp")
				.append(InfoHelper.createText(": " + exp + " / " + expup));
		
	}
	
	protected MutableComponent getDefaultFavInfo()
	{
		IDwmgBefriendedMob bm = (IDwmgBefriendedMob)mob;	
		String fav = Integer.toString(Mth.floor(bm.getFavorability().getFavorability()));
		String favmax = Integer.toString(Mth.floor(bm.getFavorability().getMaxFavorability()));
		return InfoHelper.createTrans("info.dwmg.gui_favorability")
				.append(InfoHelper.createText(": " + fav + " / " + favmax));
	}
	
	@Override
	public void addAttributeInfo(GuiGraphics graphics, IntVec2 position, int color, int textRowWidth)
	{
		this.addBasicAttributeInfo(graphics, position, color, textRowWidth);
		position.addY(textRowWidth * 3);
		this.addFavorabilityAndLevelInfo(graphics, position, color, textRowWidth);
	}

	@Override
	public void addAttributeInfo(GuiGraphics graphics, IntVec2 position)
	{
		addAttributeInfo(graphics, position, 0x404040, 11);
	}
	
	/** Below are texture-specific, must using gui_preset_0.png */
	
	protected IntVec2 screenSize()
	{
		return IntVec2.valueOf(224, 183);
	}
	
	protected IntVec2 basePos()
	{
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		return IntVec2.valueOf(i, j); 
	}
	
	// Absolute position from relative position of screen
	protected IntVec2 absPos(int x, int y)
	{
		return basePos().add(x, y);
	}
	
	protected IntVec2 absPos(IntVec2 xy)
	{
		return basePos().add(xy);
	}
	
	protected IntVec2 leftRowPos()
	{
		return absPos(7, 17);
	}
	
	protected IntVec2 rightRowPos()
	{
		return absPos(79, 17);
	}
	
	protected void addMainScreen(GuiGraphics graphics)
	{
		this.drawSprite(graphics, basePos(), IntVec2.zero(), screenSize());
	}
	
	protected void addSlotBg(GuiGraphics graphics, int slotIndex, IntVec2 pos, int slotIndexX, int slotIndexY)
	{
		this.addSlotBg(graphics, slotIndex, pos, IntVec2.valueOf(256, 0).coord(slotIndexX, slotIndexY), IntVec2.valueOf(256, 0));
	}

	protected void addBaubleSlotBg(GuiGraphics graphics, int slotIndex, IntVec2 pos)
	{
		this.addSlotBg(graphics, slotIndex, pos, 1, 2);
	}

	@Deprecated
	public void addMobRenderBox(GuiGraphics graphics, int variation)
	{
		this.drawSprite(graphics, absPos(27, 17), IntVec2.valueOf(120 + variation * 50, 183), IntVec2.valueOf(50, 72));
	}
	

	public void addMobRenderBox(GuiGraphics graphics)
	{
		this.drawSprite(graphics, absPos(27, 17), IntVec2.valueOf(120 + this.mobRenderBoxStyle.getIndex() * 50, 183), IntVec2.valueOf(50, 72));
	}
	
	public void addInfoBox(GuiGraphics graphics)

	{
		this.drawSprite(graphics, absPos(99, 17), IntVec2.valueOf(0, 183), IntVec2.valueOf(120, 72));
	}
	
	public IntVec2 infoPos()
	{
		return absPos(103, 21);
	}
	
	public void renderMob(GuiGraphics graphics, IntVec2 offset)
	{
		IntVec2 pos = absPos(getEntityRenderPosition().add(offset));
		InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, pos.x, pos.y, getMobRenderScale(), 
				(float) pos.x - this.xMouse, (float) (pos.y - 50) - this.yMouse, mob.asMob());
	}
	
	public void renderMob(GuiGraphics graphics)
	{
		renderMob(graphics, IntVec2.valueOf(0));
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

