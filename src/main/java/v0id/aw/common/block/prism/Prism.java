package v0id.aw.common.block.prism;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import v0id.aw.common.creativetabs.AWTabs;
import v0id.aw.common.tile.TilePrism;
import v0id.aw.lib.AWConsts;

import javax.annotation.Nullable;

/**
 * Created by V0idWa1k3r on 01-Jun-17.
 */
public class Prism extends Block
{
    public Prism()
    {
        super(Material.IRON);
        this.setHardness(4);
        this.setResistance(8);
        this.setRegistryName(AWConsts.blockPrism);
        this.setUnlocalizedName("aw.prism");
        this.setCreativeTab(AWTabs.TAB_AW);
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
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TilePrism();
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        super.breakBlock(worldIn, pos, state);
        worldIn.removeTileEntity(pos);
    }
}
