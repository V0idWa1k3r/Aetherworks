package v0id.aw.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import v0id.aw.AetherWorks;

public class AWMagma extends EntityThrowable
{
    protected int lifeTime;

    public AWMagma(World worldIn, EntityLivingBase throwerIn)
    {
        super(worldIn, throwerIn);
    }

    public AWMagma(World worldIn)
    {
        super(worldIn);
    }

    public AWMagma(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    @Override
    public void onUpdate()
    {
        this.setNoGravity(true);
        super.onUpdate();
        if (++this.lifeTime >= 10)
        {
            this.setDead();
        }

        AetherWorks.proxy.spawnParticleGlow(this.world, (float)this.posX, (float)this.posY, (float)this.posZ, (float)this.motionX / 10, (float)this.motionY / 10, (float)this.motionZ / 10, 1.0F, 0.3F, 0.0F, 1.0F, 30);
    }

    @Override
    protected void onImpact(RayTraceResult result)
    {
        Entity e = result.entityHit;
        if (e != null && e != this.getThrower())
        {
            if (e.attackEntityFrom(DamageSource.causeIndirectDamage(this, this.getThrower()).setDamageBypassesArmor(), 4))
            {
                e.setFire(15);
            }
        }

        if (e != this.getThrower())
        {
            this.setDead();
        }

        this.world.spawnParticle(EnumParticleTypes.LAVA, this.posX, this.posY, this.posZ, 0, 0, 0);
    }
}
