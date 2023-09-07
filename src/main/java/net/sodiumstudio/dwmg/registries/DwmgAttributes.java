package net.sodiumstudio.dwmg.registries;

import java.util.function.Supplier;

import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;

public class DwmgAttributes
{
	
	public static final Supplier<AttributeSupplier.Builder> HMAG_DULLAHAN_ATTRIBUTES = () ->
		Monster.createMonsterAttributes()
		.add(Attributes.MAX_HEALTH, 60.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.31D)
		.add(Attributes.ATTACK_DAMAGE, 6.0D)
		.add(Attributes.ARMOR, 5.0D)
		.add(Attributes.KNOCKBACK_RESISTANCE, 0.5D)
		.add(Attributes.FOLLOW_RANGE, 20.0D);
	
	public static final Supplier<AttributeSupplier.Builder> HMAG_JIANGSHI_ATTRIBUTES = () ->
		Monster.createMonsterAttributes()
		.add(Attributes.MAX_HEALTH, 40.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.19D)
		.add(Attributes.ATTACK_DAMAGE, 6.0D)
		.add(Attributes.ATTACK_KNOCKBACK, 0.5D)
		.add(Attributes.ARMOR, 4.0D)
		.add(Attributes.KNOCKBACK_RESISTANCE, 0.5D)
		.add(Attributes.FOLLOW_RANGE, 24.0D);

	public static final Supplier<AttributeSupplier.Builder> HMAG_DODOMEKI_ATTRIBUTES = () ->
		Monster.createMonsterAttributes().
		add(Attributes.MAX_HEALTH, 40.0D).
		add(Attributes.MOVEMENT_SPEED, 0.24D).
		add(Attributes.ATTACK_DAMAGE, 7.0D).
		add(Attributes.ARMOR, 5.0D).
		add(Attributes.KNOCKBACK_RESISTANCE, 0.5D).
		add(Attributes.FOLLOW_RANGE, 20.0D);
		
	public static final Supplier<AttributeSupplier.Builder> HMAG_ALRAUNE_ATTRIBUTES = () ->
		Monster.createMonsterAttributes()
		.add(Attributes.MAX_HEALTH, 60.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.12D)
		.add(Attributes.ATTACK_DAMAGE, 6.0D)
		.add(Attributes.ARMOR, 5.0D)
		.add(Attributes.KNOCKBACK_RESISTANCE, 0.98D);
}
