package v0id.aw.common.block.prism;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import v0id.aw.common.creativetabs.AWTabs;
import v0id.aw.lib.AWConsts;

/**
 * Created by V0idWa1k3r on 31-May-17.
 */
public class ControllerMatrix extends Block
{
    public static final AxisAlignedBB AXIS_ALIGNED_BB = new AxisAlignedBB(0, 0, 0, 1, 1.5, 1);

    public ControllerMatrix()
    {
        super(Material.ROCK);
        this.setRegistryName(AWConsts.blockAetherPrismControllerMatrix);
        this.setHardness(3);
        this.setResistance(6);
        this.setCreativeTab(AWTabs.TAB_AW);
        this.setUnlocalizedName("aw.aether_prism_controller_matrix");
        this.setHarvestLevel("pickaxe", 1);
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return AXIS_ALIGNED_BB;
    }

    @Override
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.SOLID;
    }

    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
}
