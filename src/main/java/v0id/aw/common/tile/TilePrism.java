package v0id.aw.common.tile;

import net.minecraft.block.Block;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.embers.RegistryManager;
import v0id.aw.AetherWorks;
import v0id.aw.common.block.AWBlocks;
import v0id.aw.common.fluid.AWFluids;

import static v0id.aw.common.block.prism.MoonlightAmplifier.FACING;

/**
 * Created by V0idWa1k3r on 01-Jun-17.
 */
public class TilePrism extends TileEntity implements ITickable
{
    public boolean isStructureValid()
    {
        return this.isStructureValid;
    }

    private boolean canWork()
    {
        return this.canWork;
    }

    private boolean isStructureValid;
    private boolean canWork;
    private int ticksExisted;

    private BlockPos currentCrystal;
    private int currentCrystalFXTime;
    private boolean currentCrystalHeat;

    public TilePrism()
    {
        super();
    }

    @Override
    public void update()
    {
        if (++ticksExisted % 60 == 0)
        {
            this.updateTick();
        }

        if (this.world.isRemote && this.canWork() && this.isStructureValid())
        {
            this.mkCrystalFX();
        }
    }

    private void mkCrystalFX()
    {
        if (--currentCrystalFXTime <= 0)
        {
            int crystalPtr = this.world.rand.nextInt(4);
            this.currentCrystal = this.pos.offset(crystalPtr / 2 == 0 ? EnumFacing.WEST : EnumFacing.EAST, 2).offset(crystalPtr % 2 == 0 ? EnumFacing.NORTH : EnumFacing.SOUTH, 2);
            this.currentCrystalFXTime = 40;
            this.currentCrystalHeat = this.world.rand.nextBoolean();
            this.world.playSound(AetherWorks.proxy.getClientPlayer(), this.currentCrystal, this.currentCrystalHeat ? SoundEvents.ITEM_FIRECHARGE_USE : SoundEvents.BLOCK_SNOW_BREAK, SoundCategory.BLOCKS, 1, 0.1F);
        }
        else
        {
            float progress = (float)this.currentCrystalFXTime / 40;
            float sX = this.getPos().getX() - ((float)(this.getPos().getX() - this.currentCrystal.getX()) * progress) + 0.5F;
            float sZ = this.getPos().getZ() - ((float)(this.getPos().getZ() - this.currentCrystal.getZ()) * progress) + 0.5F;
            float sY = this.getPos().getY() + 0.5F + (float)Math.sin(Math.toRadians(progress * 360));
            if (this.currentCrystalHeat)
            {
                AetherWorks.proxy.spawnParticleGlow(this.getWorld(), sX, sY, sZ, 0, 0, 0, 255, 64, 16, 2, 80);
            }
            else
            {
                AetherWorks.proxy.spawnParticleGlow(this.getWorld(), sX, sY, sZ, 0, 0, 0, 176, 217, 229, 2, 80);
            }
        }
    }

    @Override
    public boolean hasFastRenderer()
    {
        return true;
    }

    private void updateTick()
    {
        this.isStructureValid = this.checkStructure();
        this.canWork = this.isStructureValid && this.checkWorkConditions();
        if (this.canWork)
        {
            this.work();
        }
    }

    private void work()
    {
        if (this.world.isRemote)
        {
            for (int i = 0; i < 128; i++)
            {
                AetherWorks.proxy.spawnParticleGlow(this.world, this.getPos().getX() + 0.5F, this.getPos().getY() + 0.5F - this.world.rand.nextFloat() * 3, this.getPos().getZ() + 0.5F, 0, 0, 0, 0, 1, 1, 3, 120);
            }
        }
        else
        {
            TileEntity tile = this.world.getTileEntity(this.getPos().down(3));
            if (tile != null && tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP))
            {
                IFluidHandler handler = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP);
                if (handler != null)
                {
                    FluidStack fs = new FluidStack(AWFluids.FLUID_IMPURE_AETHERIUM_GAS, 1);
                    if (handler.fill(fs, false) > 0)
                    {
                        handler.fill(fs, true);
                        tile.markDirty();
                    }
                }
            }
        }
    }

    private boolean checkWorkConditions()
    {
        long time = this.world.getWorldTime() % 24000;
        if (time < 15000 || time > 21000)
        {
            return false;
        }

        World w = this.world;
        BlockPos pos = this.getPos();

        return !(!w.canBlockSeeSky(pos.up()) || !w.canBlockSeeSky(pos.north(3).up()) || !w.canBlockSeeSky(pos.south(3).up()) || !w.canBlockSeeSky(pos.west(3).up()) || !w.canBlockSeeSky(pos.east(3).up()));
    }

    private boolean checkStructure()
    {
        World w = this.getWorld();
        BlockPos pos = this.getPos();
        return
                // Supports
                w.getBlockState(pos.down()).getBlock().isAssociatedBlock(AWBlocks.PRISM_SUPPORT) && w.getBlockState(pos.down(2)).getBlock().isAssociatedBlock(AWBlocks.PRISM_SUPPORT) &&

                // Tank
                w.getBlockState(pos.down(3)).getBlock().isAssociatedBlock(RegistryManager.block_tank) &&

                // Air around
                w.isAirBlock(pos.west()) && w.isAirBlock(pos.west(2)) && w.isAirBlock(pos.east()) && w.isAirBlock(pos.east(2)) && w.isAirBlock(pos.north()) && w.isAirBlock(pos.north(2)) && w.isAirBlock(pos.south()) && w.isAirBlock(pos.south(2)) &&

                // Amplifiers
                w.getBlockState(pos.west(3)) == AWBlocks.MOONLIGHT_AMPLIFIER.getDefaultState().withProperty(FACING, EnumFacing.EAST) && w.getBlockState(pos.east(3)) == AWBlocks.MOONLIGHT_AMPLIFIER.getDefaultState().withProperty(FACING, EnumFacing.WEST) && w.getBlockState(pos.north(3)) == AWBlocks.MOONLIGHT_AMPLIFIER.getDefaultState().withProperty(FACING, EnumFacing.SOUTH) && w.getBlockState(pos.south(3)) == AWBlocks.MOONLIGHT_AMPLIFIER.getDefaultState().withProperty(FACING, EnumFacing.NORTH) &&

                // Matrices
                w.getBlockState(pos.down().west(2).north(2)).getBlock() == AWBlocks.AETHER_PRISM_CONTROLLER_MATRIX && w.getBlockState(pos.down().west(2).south(2)).getBlock() == AWBlocks.AETHER_PRISM_CONTROLLER_MATRIX && w.getBlockState(pos.down().east(2).north(2)).getBlock() == AWBlocks.AETHER_PRISM_CONTROLLER_MATRIX && w.getBlockState(pos.down().east(2).south(2)).getBlock() == AWBlocks.AETHER_PRISM_CONTROLLER_MATRIX &&

                // Base
                this.checkStructureBase(w, pos);
    }

    private boolean checkStructureBase(World w, BlockPos pos)
    {
        for (EnumFacing dir : EnumFacing.HORIZONTALS)
        {
            // Pillar
            for (int dy = 0; dy < 3; ++dy)
            {
                BlockPos offset = pos.offset(dir, 3).down(dy + 1);
                Block test = dy == 0 ? RegistryManager.archaic_edge : RegistryManager.archaic_bricks;
                if (!w.getBlockState(offset).getBlock().isAssociatedBlock(test))
                {
                    return false;
                }
            }
        }

        for (int dy = 0; dy < 2; ++dy)
        {
            Block test = dy == 0 ? RegistryManager.archaic_edge : RegistryManager.archaic_bricks;
            for (int i = 0; i < 4; ++i)
            {
                BlockPos offset = pos.offset(i / 2 == 0 ? EnumFacing.WEST : EnumFacing.EAST, 2).offset(i % 2 == 0 ? EnumFacing.NORTH : EnumFacing.SOUTH, 2);
                offset = offset.down(dy + 2);
                if (!w.getBlockState(offset).getBlock().isAssociatedBlock(test))
                {
                    return false;
                }
            }
        }

        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public AxisAlignedBB getRenderBoundingBox()
    {
        return super.getRenderBoundingBox().offset(-4, -3, -4).expand(9, 3, 9);
    }


}
