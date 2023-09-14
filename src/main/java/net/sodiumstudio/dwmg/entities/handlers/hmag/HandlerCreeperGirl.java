package net.sodiumstudio.dwmg.entities.handlers.hmag;

import java.util.Collection;
import java.util.HashSet;

import com.github.mechalopa.hmag.registry.ModItems;
import com.github.mechalopa.hmag.world.entity.CreeperGirlEntity;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.befriendmobs.entity.befriending.BefriendableAddHatredReason;
import net.sodiumstudio.befriendmobs.entity.befriending.handlerpreset.HandlerItemGivingProgress;
import net.sodiumstudio.befriendmobs.events.BefriendableTimerUpEvent;
import net.sodiumstudio.befriendmobs.registry.BMCaps;
import net.sodiumstudio.nautils.EntityHelper;
import net.sodiumstudio.nautils.Wrapped;
import net.sodiumstudio.nautils.math.RndUtil;
import net.sodiumstudio.dwmg.Dwmg;

@Mod.EventBusSubscriber(modid = Dwmg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HandlerCreeperGirl extends HandlerItemGivingProgress
{
	
	@Override
	public IBefriendedMob befriend(Player player, Mob target)
	{
		target.setNoAi(false);
		return super.befriend(player, target);
	}

	@Override
	public boolean additionalConditions(Player player, Mob mob)
	{
		Wrapped<Boolean> res = new Wrapped<Boolean>(false);
		mob.getCapability(BMCaps.CAP_BEFRIENDABLE_MOB).ifPresent((cap) -> 
		{
			res.set(!cap.hasTimer("final_explosion_fail_cooldown"));
		});
		return res.get();
	}
	
	@Override
	public void serverTick(Mob mob)
	{
		CreeperGirlEntity cg = (CreeperGirlEntity)mob;
		mob.getCapability(BMCaps.CAP_BEFRIENDABLE_MOB).ifPresent((l) ->
		{
			if (l.getNbt().contains("final_explosion_player", 11)) 
			{
				
				if (cg.getSwelling(1.0f) * 28.0f <= 26.0f)
				{
					cg.setSwellDir(1);
				}
				else if (cg.getSwelling(1.0f) * 28.0f >= 28.0f)
				{
					cg.setSwellDir(-1);
				}
				
				Player player = mob.level.getPlayerByUUID(l.getNbt().getUUID("final_explosion_player"));
				// Fix reloading crash after quit after player die 
				if (player == null)
					return;
				int tb = l.getNbt().getInt("final_explosion_ticks_before");
				int ta = l.getNbt().getInt("final_explosion_ticks_after");
				if (mob.distanceToSqr(player) >= 64.0f)
				{
					this.interrupt(player, cg, false);
				}
				else if (tb > 0)
				{
					if (tb % 3 == 1 || tb <= 13)
						EntityHelper.sendGlintParticlesToLivingDefault(cg);
					EntityHelper.sendSmokeParticlesToLivingDefault(cg);
					l.getNbt().putInt("final_explosion_ticks_before", tb - 1);
				}
				else if (ta > 0)
				{
					if (ta == 5)
					{
						doFinalExplosion((CreeperGirlEntity)mob, player);
					}
					l.getNbt().putInt("final_explosion_ticks_after", ta - 1);
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
		mob.getCapability(BMCaps.CAP_BEFRIENDABLE_MOB).ifPresent((l) ->
		{
			l.getNbt().putUUID("final_explosion_player", player.getUUID());
			l.getNbt().putInt("final_explosion_ticks_before", 80);
			l.getNbt().putInt("final_explosion_ticks_after", 5);
			mob.setNoAi(true);
			if (mob.getSwelling(1.0f) * 28.0f < 24.0f)	// getSwelling(1) is ((float)swell/28.0f)
				mob.setSwellDir(1);
			else mob.setSwellDir(-1);
		});
	}
	
	protected void finalExplosionFailed(CreeperGirlEntity mob, Player player, boolean isQuiet)
	{
		mob.getCapability(BMCaps.CAP_BEFRIENDABLE_MOB).ifPresent((l) ->
		{
			if (l.getNbt().contains("final_explosion_player", 11) && l.getNbt().getUUID("final_explosion_player").equals(player.getUUID()))
			{
					l.getNbt().remove("final_explosion_player");
					l.getNbt().remove("final_explosion_ticks_before");
					l.getNbt().remove("final_explosion_ticks_after");
					//l.setTimer("final_explosion_fail_cooldown", 60);	/* NOT WORKING NOW */
					mob.setNoAi(false);
					mob.setSwellDir(-1);
					/*if (!isQuiet)
						for (int i = 0; i < 5; ++i)
							EntityHelper.sendAngryParticlesToLivingDefault(mob);*/
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
	/*
	@SubscribeEvent
	public static void onPlayerDie(LivingDeathEvent event) {

		if (event.getSource().getEntity() instanceof CreeperGirlEntity cg)
		{
			if (event.getEntity() instanceof Player player)
			{
				cg.getCapability(BMCaps.CAP_BEFRIENDABLE_MOB).ifPresent((l) ->
				{
					if (l.getNbt().contains("final_explosion_player", 11)
							&& cg.level.getPlayerByUUID(l.getNbt().getUUID("final_explosion_player")) == player)
					{
						((HandlerCreeperGirl) (BefriendingTypeRegistry.getHandler(ModEntityTypes.CREEPER_GIRL.get())))
								.finalExplosionFailed(cg, player, true);
					}
				});
			}
			else if (event.getEntity() instanceof IBefriendedMob bef)
			{
				cg.getCapability(BMCaps.CAP_BEFRIENDABLE_MOB).ifPresent((l) ->
				{
					if (l.getNbt().contains("final_explosion_player", 11) && bef.getOwner() != null
							&& cg.level.getPlayerByUUID(l.getNbt().getUUID("final_explosion_player")) == bef.getOwner())
					{
						
					}
				});
			}
		}
	}*/
	
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
	protected double getProcValueToAdd(ItemStack item, Player player, Mob mob, double lastProc) {
		double rnd = this.rnd.nextDouble();
		if (item.is(ModItems.LIGHTNING_PARTICLE.get()))
			return rnd < 0.1 ? 0.50 : (rnd < 0.4 ? 0.25 : 0.125);
		if (item.is(Items.GUNPOWDER))
			return RndUtil.rndRangedDouble(0.015, 0.03);
		else if (item.is(Items.TNT))
			return RndUtil.rndRangedDouble(0.03, 0.06);
		else return 0;
	}

	@Override
	public int getItemGivingCooldownTicks() {
		return 100;
	}

	@Override
	public boolean isItemAcceptable(ItemStack item) {
		return item.is(Items.GUNPOWDER)
				|| item.is(Items.TNT)
				|| item.is(ModItems.LIGHTNING_PARTICLE.get());
	}

	@Override
	public IBefriendedMob finalActions(Player player, Mob mob)
	{
		finalExplosionStart((CreeperGirlEntity)mob, player);
		return null;
	}
	
	@Override
	public void interrupt(Player player, Mob mob, boolean isQuiet)
	{
		super.interrupt(player, mob, isQuiet);
		this.finalExplosionFailed((CreeperGirlEntity) mob, player, isQuiet);
	}

	// Duration of neutral added
	// -1 means permanent
	@Override
	public int getHatredDurationTicks(BefriendableAddHatredReason reason)
	{
		return 200;
	}
	
	@Override
	public HashSet<BefriendableAddHatredReason> getAddHatredReasons() {
		HashSet<BefriendableAddHatredReason> set = new HashSet<BefriendableAddHatredReason>();
		set.add(BefriendableAddHatredReason.ATTACKED);
		return set;
	}
	
	@SubscribeEvent
	public static void onTimerUp(BefriendableTimerUpEvent event)
	{
		if (event.getMob() != null && event.getMob().isAlive() && event.getMob() instanceof CreeperGirlEntity)
		{
			if (event.getKey().equals("final_explosion_fail_cooldown"))
				event.getMob().setNoAi(false);			
		}
	}
}
