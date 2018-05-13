package v0id.aw.common.block.prism;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import v0id.aw.common.creativetabs.AWTabs;
import v0id.aw.lib.AWConsts;

/**
 * Created by V0idWa1k3r on 01-Jun-17.
 */
public class PrismSupport extends Block
{
    public PrismSupport()
    {
        super(Material.ROCK);
        this.setHardness(3);
        this.setResistance(6F);
        this.setCreativeTab(AWTabs.TAB_AW);
        this.setRegistryName(AWConsts.blockPrismSupport);
        this.setUnlocalizedName("aw.prism_support");
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
