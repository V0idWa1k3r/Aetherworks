package v0id.aw.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import v0id.aw.AetherWorks;
import v0id.aw.common.creativetabs.AWTabs;
import v0id.aw.lib.AWConsts;

import java.util.Random;

/**
 * Created by V0idWa1k3r on 31-May-17.
 */
public class AetherBlock extends Block
{
    AetherBlock()
    {
        super(Material.IRON);
        this.setRegistryName(AWConsts.blockAether);
        this.setHardness(5);
        this.setResistance(12);
        this.setCreativeTab(AWTabs.TAB_AW);
        this.setLightLevel(1F);
        this.setUnlocalizedName("aw.block_aether");
        this.setHarvestLevel("pickaxe", 3);
    }

    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        AetherWorks.proxy.spawnParticleSpark(worldIn, pos.getX() + rand.nextFloat(), pos.getY() + rand.nextFloat(), pos.getZ() + rand.nextFloat(), (rand.nextFloat() - rand.nextFloat()) / 20, rand.nextFloat() / 20, (rand.nextFloat() - rand.nextFloat()) / 20, 0, 0.72F, 0.95F, 1 + rand.nextFloat(), 60);
        super.randomDisplayTick(stateIn, worldIn, pos, rand);
    }
}
