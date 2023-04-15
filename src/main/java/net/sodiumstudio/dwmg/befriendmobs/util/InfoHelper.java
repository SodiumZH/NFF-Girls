package net.sodiumstudio.dwmg.befriendmobs.util;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.network.chat.contents.TranslatableContents;

public class InfoHelper
{
	// Create a component with plain text content, equals to TextComponent in 1.18.2
	public static MutableComponent createText(String str)
	{
		return MutableComponent.create(new LiteralContents(str));
	}
	
	// Create a component with translatable content, equals to TranslatableComponent in 1.18.2
	public static MutableComponent createTrans(String key)
	{
		return MutableComponent.create(new TranslatableContents(key));
	}
}
