package v0id.aw.common.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import v0id.aw.common.block.forge.Component;

/**
 * Created by V0idWa1k3r on 02-Jun-17.
 */
public class TileHeatVent extends TileEntity implements IForgePart
{
    @Override
    public void onForgeTick(IForge forge)
    {
        if (this.world.getBlockState(this.getPos()).getBlock() instanceof Component && forge.getHeatCapability().getHeatStored() > 100 && !world.isBlockPowered(this.getPos()) && world.isBlockIndirectlyGettingPowered(this.getPos()) <= 0)
        {
            if (this.world.isRemote)
            {
                IBlockState state = this.getWorld().getBlockState(this.getPos());
                EnumFacing facing = state.getBlock().getExtendedState(state, this.world, this.getPos()).getValue(Component.FACING_PROPERTY);
                switch (facing)
                {
                    case NORTH:
                    {
                        this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.getPos().getX() + this.world.rand.nextFloat(), this.getPos().getY() + this.world.rand.nextFloat(), this.getPos().getZ() + 0.8F, 0, 0, -0.1F);
                        break;
                    }

                    case SOUTH:
                    {
                        this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.getPos().getX() + this.world.rand.nextFloat(), this.getPos().getY() + this.world.rand.nextFloat(), this.getPos().getZ() + 0.1F, 0, 0, 0.1F);
                        break;
                    }

                    case WEST:
                    {
                        this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.getPos().getX() + 0.8F, this.getPos().getY() + this.world.rand.nextFloat(), this.getPos().getZ() + this.world.rand.nextFloat(), -0.1F, 0, 0);
                        break;
                    }

                    case EAST:
                    {
                        this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.getPos().getX(), this.getPos().getY() + this.world.rand.nextFloat(), this.getPos().getZ() + this.world.rand.nextFloat(), 0.1F, 0, 0);
                        break;
                    }

                    default:
                    {
                        break;
                    }
                }
            }
            else
            {
                forge.getHeatCapability().setHeat(forge.getHeatCapability().getHeatStored() - 0.5F);
            }
        }
    }

    @Override
    public Component.Type getComponentType()
    {
        return Component.Type.HEAT_VENT;
    }
}
