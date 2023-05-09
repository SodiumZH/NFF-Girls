package net.sodiumstudio.dwmg.entities.projectile;

import com.github.mechalopa.hmag.registry.ModEntityTypes;
import com.github.mechalopa.hmag.world.entity.NecroticReaperEntity;
import com.github.mechalopa.hmag.world.entity.projectile.MagicBulletEntity;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PlayMessages;
import net.sodiumstudio.befriendmobs.entity.befriending.registry.BefriendingTypeRegistry;
import net.sodiumstudio.befriendmobs.util.EntityHelper;
import net.sodiumstudio.befriendmobs.util.ReflectHelper;
import net.sodiumstudio.dwmg.entities.handlers.hmag.HandlerNecroticReaper;
import net.sodiumstudio.dwmg.entities.hmag.EntityBefriendedNecroticReaper;
import net.sodiumstudio.dwmg.registries.DwmgEntityTypes;
import net.sodiumstudio.dwmg.registries.DwmgItems;

public class NecromancerMagicBulletEntity extends MagicBulletEntity
{

	public boolean hasNecromancerHat = false;
	
	public NecromancerMagicBulletEntity(EntityType<? extends NecromancerMagicBulletEntity> type, Level worldIn)
	{
		super(type, worldIn);
		this.setVariant(1);
	}

	public NecromancerMagicBulletEntity(Level worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ)
	{
		super(worldIn, shooter, accelX, accelY, accelZ);
		ReflectHelper.forceSet(this, Entity.class, "type", DwmgEntityTypes.NECROMANCER_MAGIC_BULLET.get());
	}

	@OnlyIn(Dist.CLIENT)
	public NecromancerMagicBulletEntity(Level worldIn, double x, double y, double z, double accelX, double accelY, double accelZ)
	{
		super(worldIn, x, y, z, accelX, accelY, accelZ);
		ReflectHelper.forceSet(this, Entity.class, "type", DwmgEntityTypes.NECROMANCER_MAGIC_BULLET.get());
	}

	public NecromancerMagicBulletEntity(PlayMessages.SpawnEntity spawnEntity, Level level)
	{
		this(DwmgEntityTypes.NECROMANCER_MAGIC_BULLET.get(), level);
	}
	
	@Override
	protected void onHitEntity(EntityHitResult result)
	{
		Entity entity = result.getEntity();

		if (!this.level.isClientSide)
		{

			if (entity != null && entity instanceof LivingEntity living)
			{
				if (!(living instanceof ArmorStand))
				{
					boolean shouldAddWither = true;
					if (result.getEntity().getClass().equals(NecroticReaperEntity.class) && this.getOwner() instanceof Player player)
					{
						NecroticReaperEntity nr = (NecroticReaperEntity)(result.getEntity());
						HandlerNecroticReaper handler = 
								(HandlerNecroticReaper) BefriendingTypeRegistry.getHandler(ModEntityTypes.NECROTIC_REAPER.get());
						shouldAddWither = !handler.onHit(player, nr);
					}
					
					// Befriended Necrotic Reapers are immune to this magic bullet;
					// If it's shot by the owner, it will heal the mob and optionally add strength effect if having a Necromancer's Hat.
					else if (result.getEntity() instanceof EntityBefriendedNecroticReaper nr)
					{
						if (this.getOwner() instanceof Player player && nr.getOwnerUUID().equals(player.getUUID()))
						{
							if (hasNecromancerHat)
							{
								nr.heal(3);
								EntityHelper.addEffectSafe(nr, MobEffects.DAMAGE_BOOST, 100, 1);
							}
							else
							{
								nr.heal(2);
							}
							EntityHelper.sendGlintParticlesToLivingDefault(nr);
						}
						shouldAddWither = false;
					}
					if (shouldAddWither)
					{
						EntityHelper.addEffectSafe(living, MobEffects.WITHER, 100, hasNecromancerHat ? 3 : 2);
						if (this.getOwner() instanceof LivingEntity ownerLiving)
						{
							living.setLastHurtByMob(ownerLiving);
						}
					}
				}
			}
			this.level.broadcastEntityEvent(this, (byte)3);
		}
	}
	
	@Override
	public int getVariant()
	{
		return 1;
	}
	
}
