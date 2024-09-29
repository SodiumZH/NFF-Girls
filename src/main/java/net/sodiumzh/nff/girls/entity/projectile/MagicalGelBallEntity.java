package net.sodiumzh.nff.girls.entity.projectile;

import java.util.Random;

import com.github.mechalopa.hmag.registry.ModEntityTypes;
import com.github.mechalopa.hmag.world.entity.MagicalSlimeEntity;
import com.github.mechalopa.hmag.world.entity.SlimeGirlEntity;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.sodiumzh.nautils.statics.NaUtilsEntityStatics;
import net.sodiumzh.nautils.statics.NaUtilsReflectionStatics;
import net.sodiumzh.nautils.statics.NaUtilsMathStatics;
import net.sodiumzh.nautils.math.LinearColor;
import net.sodiumzh.nff.girls.entity.hmag.HmagSlimeGirlEntity;
import net.sodiumzh.nff.girls.item.MagicalGelColorUtils;
import net.sodiumzh.nff.girls.registry.NFFGirlsEntityTypes;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nff.girls.registry.NFFGirlsTags;

public class MagicalGelBallEntity extends ThrowableItemProjectile
{

	protected Random rnd = new Random();
	
	public MagicalGelBallEntity(EntityType<? extends MagicalGelBallEntity> pEntityType, Level pLevel)
	{
		super(pEntityType, pLevel);
	}

	public MagicalGelBallEntity(Level pLevel, LivingEntity pShooter)
	{
		super(NFFGirlsEntityTypes.MAGICAL_GEL_BALL.get(), pShooter, pLevel);
	}

	public MagicalGelBallEntity(Level pLevel, double pX, double pY, double pZ)
	{
		super(NFFGirlsEntityTypes.MAGICAL_GEL_BALL.get(), pX, pY, pZ, pLevel);
	}

	@Override
	protected Item getDefaultItem() {
		return NFFGirlsItems.MAGICAL_GEL_BALL.get();
	}

	private ParticleOptions getParticle() {
		ItemStack itemstack = this.getItemRaw();
		return (ParticleOptions) (itemstack.isEmpty() ? ParticleTypes.ITEM_SLIME
				: new ItemParticleOption(ParticleTypes.ITEM, itemstack));
	}

	/**
	 * Handles an entity event fired from
	 * {@link net.minecraft.world.level.Level#broadcastEntityEvent}.
	 */
	@Override
	public void handleEntityEvent(byte pId) {
		if (pId == 3)
		{
			ParticleOptions particleoptions = this.getParticle();

			for (int i = 0; i < 8; ++i)
			{
				this.level.addParticle(particleoptions, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
			}
		}

	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		super.onHitEntity(result);
		if (result.getEntity() instanceof LivingEntity living && !living.level.isClientSide)
		{
			// Generate a tiny magical slime when hit a large vanilla slime or a slime girl
			if (((living.getType() == EntityType.SLIME && ((Slime)living).getSize() >= 3)
					|| living instanceof SlimeGirlEntity)
					&& living.getRandom().nextDouble() < 0.25d)
			{
	            MagicalSlimeEntity slime = ModEntityTypes.MAGICAL_SLIME.get().create(this.level);
	            try
	            {
	            	NaUtilsReflectionStatics.forceInvoke(slime, MagicalSlimeEntity.class, "setSize", 1);
	            }
	            catch (Exception e)
	            {
	            	e.printStackTrace();
	            	NaUtilsReflectionStatics.forceInvoke(slime, Slime.class, "m_7839_", 1);//setSize
	            }
	            
	            // For vanilla slime, the color is random
	            if (living.getType() == EntityType.SLIME)
	            {
	            	slime.setVariant(-1);
	            }
	            // For slime girl, the color is the complementary
	            else if (living instanceof SlimeGirlEntity sg)
	            {
	            	LinearColor sgColorCompl;
	            	if (sg instanceof HmagSlimeGirlEntity bsg)
	            	{
	            		sgColorCompl = bsg.getColorLinear().getComplementary();
	            	}
	            	else
	            	{
	            		sgColorCompl = MagicalGelColorUtils.getSlimeColor(sg).getComplementary();
	            	}
	            	SlimeGirlEntity.ColorVariant v = MagicalGelColorUtils.closestVariant(sgColorCompl);
	            	slime.setVariant(v.getId());
	            }
	            slime.moveTo(living.getX() + NaUtilsMathStatics.rndRangedDouble(-0.5, 0.5), living.getY() + 0.5D, living.getZ() + NaUtilsMathStatics.rndRangedDouble(-0.5, 0.5), this.random.nextFloat() * 360.0F, 0.0F);
	            this.level.addFreshEntity(slime);
			}
			// For other livings (except slime-derived mobs), make a knockback and give 30s slowness II 
			else if (!(living instanceof Slime) && !(living instanceof SlimeGirlEntity) && !living.getType().is(NFFGirlsTags.IGNORES_MAGICAL_GEL_SLOWNESS))
			{
				result.getEntity().hurt(DamageSource.thrown(this, this.getOwner()), 0);
				NaUtilsEntityStatics.addEffectSafe(living, new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 30 * 20, 1));
			}
		}
		
	}

	@Override
	protected void onHit(HitResult pResult) {
		super.onHit(pResult);
		if (!this.level.isClientSide)
		{
			this.level.broadcastEntityEvent(this, (byte) 3);
			this.discard();
		}
	}

}
