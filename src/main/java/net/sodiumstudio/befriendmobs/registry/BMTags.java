package net.sodiumstudio.befriendmobs.registry;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.sodiumstudio.befriendmobs.BefriendMobs;
import net.sodiumstudio.nautils.TagHelper;

public class BMTags
{

	public static final TagKey<EntityType<?>> NEUTRAL_TO_BM_MOBS = TagHelper.createEntityTypeTag(BefriendMobs.MOD_ID, "neutral_to_bm_mobs");
}
