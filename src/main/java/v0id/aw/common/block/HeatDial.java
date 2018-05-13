package v0id.aw.common.block;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import teamroots.embers.block.IDial;
import v0id.aw.AetherWorks;
import v0id.aw.common.block.forge.Component;
import v0id.aw.common.creativetabs.AWTabs;
import v0id.aw.common.tile.IHeatCapability;
import v0id.aw.lib.AWConsts;

import java.util.List;
import java.util.Map;

/**
 * Created by V0idWa1k3r on 03-Jun-17.
 */
public class HeatDial extends Block implements IDial
{
    public static final Map<EnumFacing, AxisAlignedBB> FACING_AXIS_ALIGNED_BB_MAP = Maps.newEnumMap(EnumFacing.class);

    static
    {
        FACING_AXIS_ALIGNED_BB_MAP.put(EnumFacing.UP, new AxisAlignedBB(0.3125, 0, 0.3125, 0.6875, 0.125, 0.6875));
        FACING_AXIS_ALIGNED_BB_MAP.put(EnumFacing.DOWN, new AxisAlignedBB(0.3125, 0.875, 0.3125, 0.6875, 1, 0.6875));
        FACING_AXIS_ALIGNED_BB_MAP.put(EnumFacing.EAST, new AxisAlignedBB(0, 0.3125, 0.3125, 0.125, 0.6875, 0.6875));
        FACING_AXIS_ALIGNED_BB_MAP.put(EnumFacing.WEST, new AxisAlignedBB(0.875, 0.3125, 0.3125, 1, 0.6875, 0.6875));
        FACING_AXIS_ALIGNED_BB_MAP.put(EnumFacing.SOUTH, new AxisAlignedBB(0.3125, 0.3125, 0, 0.6875, 0.6875, 0.125));
        FACING_AXIS_ALIGNED_BB_MAP.put(EnumFacing.NORTH, new AxisAlignedBB(0.3125, 0.3125, 0.875, 0.6875, 0.6875, 1));
    }

    public HeatDial()
    {
        super(Material.IRON);
        this.setRegistryName(AWConsts.blockHeatDial);
        this.setUnlocalizedName("aw.heat_dial");
        this.setHardness(1);
        this.setResistance(3);
        this.setSoundType(SoundType.METAL);
        this.setDefaultState(this.getDefaultState().withProperty(Component.FACING_PROPERTY, EnumFacing.NORTH));
        this.setCreativeTab(AWTabs.TAB_AW);
    }
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return FACING_AXIS_ALIGNED_BB_MAP.get(state.getValue(Component.FACING_PROPERTY));
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(Component.FACING_PROPERTY, EnumFacing.values()[Math.min(meta, EnumFacing.values().length - 1)]);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(Component.FACING_PROPERTY).ordinal();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, Component.FACING_PROPERTY);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    {
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(Component.FACING_PROPERTY, facing);
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
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
        BlockPos check = pos.offset(state.getValue(Component.FACING_PROPERTY).getOpposite());
        if (worldIn.isAirBlock(check))
        {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    @Override
    public List<String> getDisplayInfo(World world, BlockPos pos, IBlockState state)
    {
        List<String> ret = Lists.newArrayList();
        BlockPos check = pos.offset(state.getValue(Component.FACING_PROPERTY).getOpposite());
        TileEntity tile = world.getTileEntity(check);
        if (tile != null && tile.hasCapability(IHeatCapability.Holder.cap, state.getValue(Component.FACING_PROPERTY)))
        {
            IHeatCapability cap = tile.getCapability(IHeatCapability.Holder.cap, state.getValue(Component.FACING_PROPERTY));
            if (cap != null)
            {
                ret.add(AetherWorks.proxy.translate("aw.tooltip.heat", (int) cap.getHeatStored(), (int) cap.getHeatCapacity()));
            }
        }

        return ret;
    }

    @Override
    public void updateTEData(World world, IBlockState state, BlockPos pos){}
}
