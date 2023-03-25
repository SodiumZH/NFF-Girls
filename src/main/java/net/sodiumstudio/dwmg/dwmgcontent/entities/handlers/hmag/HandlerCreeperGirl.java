package net.sodiumstudio.dwmg.dwmgcontent.entities.handlers.hmag;

import java.util.Collection;
import java.util.HashSet;

import com.github.mechalopa.hmag.registry.ModEntityTypes;
import com.github.mechalopa.hmag.registry.ModItems;
import com.github.mechalopa.hmag.world.entity.CreeperGirlEntity;

import net.minecraft.nbt.IntTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending.handlerpreset.HandlerItemGivingProcess;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending.registry.BefriendingTypeRegistry;
import net.sodiumstudio.dwmg.befriendmobs.registry.BefMobCapabilities;
import net.sodiumstudio.dwmg.befriendmobs.util.EntityHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.NbtHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.math.RndUtil;
import net.sodiumstudio.dwmg.dwmgcontent.registries.DwmgEffects;

public class HandlerCreeperGirl extends HandlerItemGivingProcess
{
	
	@Override
	public IBefriendedMob befriend(Player player, Mob target)
	{
		target.setNoAi(false);
		return super.befriend(player, target);
	}

	@Override
	public IBefriendedMob finalAction(Player player, Mob mob)
	{
		finalExplosionStart((CreeperGirlEntity)mob, player);
		return null;
	}
	
	
	@Override
	public void serverTick(Mob mob)
	{
		CreeperGirlEntity cg = (CreeperGirlEntity)mob;
		mob.getCapability(BefMobCapabilities.CAP_BEFRIENDABLE_MOB).ifPresent((l) ->
		{
			if (l.getNBT().contains("final_explosion_player", 11)) 
			{
				
				if (cg.getSwelling(1.0f) * 28.0f <= 26.0f)
				{
					cg.setSwellDir(1);
				}
				else if (cg.getSwelling(1.0f) * 28.0f >= 28.0f)
				{
					cg.setSwellDir(-1);
				}
				
				Player player = mob.level.getPlayerByUUID(l.getNBT().getUUID("final_explosion_player"));
				int tb = l.getNBT().getInt("final_explosion_ticks_before");
				int ta = l.getNBT().getInt("final_explosion_ticks_after");
				if (mob.distanceToSqr(player) >= 64.0f)
				{
					this.finalExplosionFailed(cg, player);
					// TODO: add smoke particle effect here
				}
				else if (tb > 0)
				{
					if (tb % 3 == 1 || tb <= 13)
						EntityHelper.sendGreenStarParticlesToLivingDefault(cg);
					EntityHelper.sendSmokeParticlesToLivingDefault(cg);
					l.getNBT().putInt("final_explosion_ticks_before", tb - 1);
				}
				else if (ta > 0)
				{
					if (ta == 5)
					{
						doFinalExplosion((CreeperGirlEntity)mob, player);
					}
					l.getNBT().putInt("final_explosion_ticks_after", ta - 1);
				}
				else
				{
					this.befriend(player, mob);
					EntityHelper.sendHeartParticlesToLivingDefault(mob);
				}
			}
		});
	}
	
	protected void finalExplosionStart(CreeperGirlEntity mob, Player player)
	{
		mob.getCapability(BefMobCapabilities.CAP_BEFRIENDABLE_MOB).ifPresent((l) ->
		{
			l.getNBT().putUUID("final_explosion_player", player.getUUID());
			l.getNBT().putInt("final_explosion_ticks_before", 80);
			l.getNBT().putInt("final_explosion_ticks_after", 5);
			mob.setNoAi(true);
			if (mob.getSwelling(1.0f) * 28.0f < 24.0f)	// getSwelling(1) is ((float)swell/28.0f)
				mob.setSwellDir(1);
			else mob.setSwellDir(-1);
		});
	}
	
	protected void finalExplosionFailed(CreeperGirlEntity mob, Player player)
	{
		mob.getCapability(BefMobCapabilities.CAP_BEFRIENDABLE_MOB).ifPresent((l) ->{
		if (l.getNBT().contains("final_explosion_player", 11) && l.getNBT().getUUID("final_explosion_player").equals(player.getUUID()))
			{
				l.getNBT().remove("final_explosion_player");
				l.getNBT().remove("final_explosion_ticks_before");
				l.getNBT().remove("final_explosion_ticks_after");
				NbtHelper.putPlayerData(IntTag.valueOf(0), l.getPlayerData(), player,
						"already_given");
				mob.setNoAi(false);
				mob.setSwellDir(-1);
				for (int i = 0; i < 5; ++i)
					EntityHelper.sendAngryParticlesToLivingDefault(mob);
				//Debug.printToScreen("Creeper Girl befriending failed.", player);
			}	
		});
	}
	
	protected void doFinalExplosion(CreeperGirlEntity mob, Player player)
	{
		mob.invulnerableTime += 2;
		Explosion.BlockInteraction explosion$blockinteraction = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(mob.level, mob)
				? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE;
		mob.level.explode(mob, mob.getX(), mob.getY(), mob.getZ(), 12.0f, explosion$blockinteraction);
		spawnLingeringCloud(mob);
	}
	
	@SubscribeEvent
	public static void onPlayerDie(LivingDeathEvent event) {

		if (event.getSource().getEntity() instanceof CreeperGirlEntity cg)
		{
			if (event.getEntity() instanceof Player player)
			{
				cg.getCapability(BefMobCapabilities.CAP_BEFRIENDABLE_MOB).ifPresent((l) ->
				{
					if (l.getNBT().contains("final_explosion_player", 11)
							&& cg.level.getPlayerByUUID(l.getNBT().getUUID("final_explosion_player")) == player)
					{
						((HandlerCreeperGirl) (BefriendingTypeRegistry.getHandler(ModEntityTypes.CREEPER_GIRL.get())))
								.finalExplosionFailed(cg, player);
					}
				});
			}
			else if (event.getEntity() instanceof IBefriendedMob bef)
			{
				cg.getCapability(BefMobCapabilities.CAP_BEFRIENDABLE_MOB).ifPresent((l) ->
				{
					if (l.getNBT().contains("final_explosion_player", 11) && bef.getOwner() != null
							&& cg.level.getPlayerByUUID(l.getNBT().getUUID("final_explosion_player")) == bef.getOwner())
					{
						
					}
				});
			}
		}
	}
	
	protected void spawnLingeringCloud(CreeperGirlEntity mob) {
		Collection<MobEffectInstance> collection = mob.getActiveEffects();
		if (!collection.isEmpty())
		{
			AreaEffectCloud areaeffectcloud = new AreaEffectCloud(mob.level, mob.getX(), mob.getY(), mob.getZ());
			areaeffectcloud.setRadius(10F);	// 4x creeper explosion here
			areaeffectcloud.setRadiusOnUse(-0.5F);
			areaeffectcloud.setWaitTime(10);
			areaeffectcloud.setDuration(areaeffectcloud.getDuration() / 2);
			areaeffectcloud.setRadiusPerTick(-areaeffectcloud.getRadius() / (float) areaeffectcloud.getDuration());

			for (MobEffectInstance mobeffectinstance : collection)
			{
				areaeffectcloud.addEffect(new MobEffectInstance(mobeffectinstance));
			}

			mob.level.addFreshEntity(areaeffectcloud);
		}

	}

	@Override
	protected HashSet<Item> givableItems() {
		HashSet<Item> set = new HashSet<Item>();
		set.add(Items.GUNPOWDER);
		set.add(Items.TNT);
		set.add(ModItems.LIGHTNING_PARTICLE.get());
		return set;
	}

	@Override
	protected double getProcValue(ItemStack item) {
		float rnd = this.rnd.nextFloat();
		if (item.is(ModItems.LIGHTNING_PARTICLE.get()))
			return rnd < 0.1 ? 0.501 : (rnd < 0.4 ? 0.251 : 0.126);
		if (item.is(Items.GUNPOWDER))
			return RndUtil.rndRangedDouble(0.015, 0.03);
		else if (item.is(Items.TNT))
			return RndUtil.rndRangedDouble(0.03, 0.06);
		else return 0;
	}

	@Override
	protected int cooldownTicks() {
		// TODO Auto-generated method stub
		return 100;
	}
	
	@Override
	protected boolean additionalConditions(Player player, Mob mob)
	{
		return player.hasEffect(DwmgEffects.UNDEAD_AFFINITY.get());
	}
	
}
