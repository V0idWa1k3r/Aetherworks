package v0id.aw.common.block.prism;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import v0id.aw.AetherWorks;
import v0id.aw.common.creativetabs.AWTabs;
import v0id.aw.lib.AWConsts;

import java.util.Random;

/**
 * Created by V0idWa1k3r on 01-Jun-17.
 */
public class MoonlightAmplifier extends Block
{
    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public MoonlightAmplifier()
    {
        super(Material.ROCK);
        this.setHardness(3);
        this.setResistance(6);
        this.setSoundType(SoundType.GLASS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        this.setRegistryName(AWConsts.blockMoonlightAmplifier);
        this.setUnlocalizedName("aw.moonlight_amplifier");
        this.setCreativeTab(AWTabs.TAB_AW);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.HORIZONTALS[Math.min(meta, EnumFacing.HORIZONTALS.length - 1)]);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(FACING).ordinal() - 2;
    }

    @Override
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.SOLID;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    {
        EnumFacing direction = EnumFacing.fromAngle(placer.rotationYaw);
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(FACING, direction);
    }

    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        if (!worldIn.canBlockSeeSky(pos) || worldIn.getWorldTime() % 24000 < 15000 || worldIn.getWorldTime() % 24000 > 21000)
        {
            return;
        }

        BlockPos sky = pos.up(16);
        float rX = pos.getX() + 0.5F;
        float rZ = pos.getZ() + 0.5F;
        for (float i = 0; i < 8; ++i)
        {
            float sX = (rand.nextFloat() - rand.nextFloat()) * 5;
            float sZ = (rand.nextFloat() - rand.nextFloat()) * 5;
            float sY = rand.nextFloat() * 3;
            AetherWorks.proxy.spawnParticleGlow(worldIn, rX + sX, pos.getY() + 1 + rand.nextFloat() * 3, rZ + sZ, -sX / 60, -sY / 60, -sZ / 60, 0, 0.72F, 0.95F, 1 + rand.nextFloat() * 3, 60);
        }

        EnumFacing dir = stateIn.getValue(FACING);
        float dirFound = 0;
        while (dirFound++ < 16)
        {
            BlockPos offset = pos.offset(dir, (int)dirFound);
            if (!worldIn.isAirBlock(offset))
            {
                break;
            }
        }

        for (float i = 0; i + 1 < dirFound * 8; ++i)
        {
            AetherWorks.proxy.spawnParticleGlow(worldIn, rX + dir.getFrontOffsetX() * (i / 8), pos.getY() + 0.5F, rZ + dir.getFrontOffsetZ() * (i / 8), 0, 0, 0, 0, 0.72F, 0.95F, 2.5f, 60);
        }

        super.randomDisplayTick(stateIn, worldIn, pos, rand);
    }
}
