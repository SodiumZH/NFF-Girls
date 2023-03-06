package com.sodium.dwmg.befriendmobsapi.util.particles;

import net.minecraft.world.phys.Vec3;

public class SendParticleArgs {

	// Average position of the particles
	public Vec3 positionAvr = new Vec3 (0, 0, 0);
	
	public SendParticleArgs positionAvr(double x, double y, double z)
	{
		positionAvr = new Vec3 (x, y, z);
		return this;
	}
	
	public SendParticleArgs positionAvr(Vec3 val)
	{
		positionAvr = val;
		return this;
	}
	
	// The max offset of position randomization
	public Vec3 positionRange = new Vec3 (1, 1, 1);
	
	public SendParticleArgs positionRange(double x, double y, double z)
	{
		positionRange = new Vec3 (x, y, z);
		return this;
	}
	
	public SendParticleArgs positionRange(Vec3 val)
	{
		positionRange = val;
		return this;
	}
	
	// The variance of position randomization
	public Vec3 positionVar = new Vec3 (0.2, 0.2, 0.2);
	
	public SendParticleArgs positionVarScale(double x, double y, double z)
	{
		positionVar = new Vec3 (x * 0.2, y * 0.2, z * 0.2);
		return this;
	}
	
	public SendParticleArgs positionVarScale(Vec3 val)
	{
		positionVar = new Vec3 (val.x * 0.2, val.y * 0.2, val.z * 0.2);
		return this;
	}
	
	// The average speed value
	public double speed = 0; 
	
	// The max
	public double speedRandomizeScale = 0.2;

	
	
}
