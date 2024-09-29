package net.sodiumzh.nff.girls.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BreakingItemParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;

public class MagicalGelBallParticle extends BreakingItemParticle// implements ParticleProvider<SimpleParticleType>
{

    protected MagicalGelBallParticle(ClientLevel pLevel, double pX, double pY, double pZ, ItemStack pStack)
	{
		super(pLevel, pX, pY, pZ, pStack);
	}

    public static class Provider implements ParticleProvider<SimpleParticleType>
	{
		@Override
		public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ,
				double pXSpeed, double pYSpeed, double pZSpeed) {
			return new MagicalGelBallParticle(pLevel, pX, pY, pZ, new ItemStack(NFFGirlsItems.MAGICAL_GEL_BALL.get()));
		}

    }
}
