package net.sodiumstudio.dwmg.befriendmobs.util;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class InfoHelper
{

	// Create a component with plain text content, equals to MutableComponent.create(TranslatableContents) in 1.19.2
	// For keeping the API consistent between 1.18.2 and 1.19.2 and simplifying porting
	public static MutableComponent createText(String str) 
	{
		return new TextComponent(str);
	}

	// Create a component with translatable content, equals to MutableComponent.create(TranslatableContents) in 1.19.2
	// For keeping the API consistent between 1.18.2 and 1.19.2 and simplifying porting
	public static MutableComponent createTrans(String key)
	{
		return new TranslatableComponent(key);
	}
}
