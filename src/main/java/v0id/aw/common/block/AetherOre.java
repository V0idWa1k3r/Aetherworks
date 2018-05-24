package v0id.aw.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import v0id.aw.AetherWorks;
import v0id.aw.common.creativetabs.AWTabs;
import v0id.aw.common.item.AWItems;
import v0id.aw.lib.AWConsts;

import java.util.Random;

/**
 * Created by V0idWa1k3r on 31-May-17.
 */
public class AetherOre extends Block
{
    AetherOre()
    {
        super(Material.ROCK);
        this.setRegistryName(AWConsts.blockAetherOre);
        this.setHardness(5);
        this.setResistance(12);
        this.setCreativeTab(AWTabs.TAB_AW);
        this.setLightLevel(1F);
        this.setUnlocalizedName("aw.aether_ore");
        this.setHarvestLevel("pickaxe", 3);
    }

    @Override
    public int getExpDrop(IBlockState state, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune)
    {
        Random rand = world instanceof World ? ((World)world).rand : new Random();
        return MathHelper.getInt(rand, 4, 8);
    }

    @Override
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager manager)
    {
        Random rand = world.rand;
        for (int i = 0; i < 300; i++)
        {
            AetherWorks.proxy.spawnParticleSpark(world, pos.getX() + rand.nextFloat(), pos.getY() + rand.nextFloat(), pos.getZ() + rand.nextFloat(), (rand.nextFloat() - rand.nextFloat()) / 20, rand.nextFloat() / 20, (rand.nextFloat() - rand.nextFloat()) / 20, 0, 0.72F, 0.95F, 1 + rand.nextFloat(), 60);
        }

        return super.addDestroyEffects(world, pos, manager);
    }

    @Override
    public boolean addHitEffects(IBlockState state, World worldObj, RayTraceResult target, ParticleManager manager)
    {
        BlockPos pos = target.getBlockPos();
        Random rand = worldObj.rand;
        AetherWorks.proxy.spawnParticleSpark(worldObj, pos.getX() + rand.nextFloat(), pos.getY() + rand.nextFloat(), pos.getZ() + rand.nextFloat(), (rand.nextFloat() - rand.nextFloat()) / 20, rand.nextFloat() / 20, (rand.nextFloat() - rand.nextFloat()) / 20, 0, 0.72F, 0.95F, 1 + rand.nextFloat(), 60);
        return super.addHitEffects(state, worldObj, target, manager);
    }

    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        AetherWorks.proxy.spawnParticleSpark(worldIn, pos.getX() + rand.nextFloat(), pos.getY() + rand.nextFloat(), pos.getZ() + rand.nextFloat(), (rand.nextFloat() - rand.nextFloat()) / 20, rand.nextFloat() / 20, (rand.nextFloat() - rand.nextFloat()) / 20, 0, 0.72F, 0.95F, 1 + rand.nextFloat(), 60);
        super.randomDisplayTick(stateIn, worldIn, pos, rand);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return AWItems.RESOURCE;
    }

    @Override
    public int quantityDroppedWithBonus(int fortune, Random random)
    {
        return 1 + random.nextInt(1 + fortune);
    }
}
