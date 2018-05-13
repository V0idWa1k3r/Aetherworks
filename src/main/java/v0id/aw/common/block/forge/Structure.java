package v0id.aw.common.block.forge;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import teamroots.embers.RegistryManager;
import v0id.aw.common.creativetabs.AWTabs;
import v0id.aw.common.handler.CommonHandler;
import v0id.aw.lib.AWConsts;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by V0idWa1k3r on 02-Jun-17.
 */
public class Structure extends Block
{
    public enum Offset implements IStringSerializable
    {
        NONE(new EnumFacing[0]),
        LEFT(EnumFacing.EAST),
        RIGHT(EnumFacing.WEST),
        FRONT(EnumFacing.SOUTH),
        BACK(EnumFacing.NORTH),
        LEFT_FRONT(LEFT, FRONT),
        LEFT_BACK(LEFT, BACK),
        RIGHT_FRONT(RIGHT, FRONT),
        RIGHT_BACK(RIGHT, BACK);

        Offset(Offset... parents)
        {
            List<EnumFacing> facings = Lists.newArrayList();
            for (Offset parent : parents)
            {
                facings.addAll(Arrays.asList(parent.directionOffsets));
            }

            EnumFacing[] array = new EnumFacing[facings.size()];
            array = facings.toArray(array);
            this.directionOffsets = array;
        }

        Offset(EnumFacing... directionOffsets)
        {
            this.directionOffsets = directionOffsets;
        }

        private final EnumFacing[] directionOffsets;

        @Override
        public String getName()
        {
            return this.name().toLowerCase();
        }
    }

    public static final PropertyEnum<Offset> OFFSET_PROPERTY = PropertyEnum.create("offset", Offset.class);

    public Structure()
    {
        super(Material.IRON);
        this.setRegistryName(AWConsts.blockForge);
        this.setHardness(5);
        this.setResistance(10);
        this.setSoundType(SoundType.METAL);
        this.setDefaultState(this.blockState.getBaseState().withProperty(OFFSET_PROPERTY, Offset.NONE));
        this.setUnlocalizedName("aw.forge_structure");
        this.setCreativeTab(AWTabs.TAB_AW);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return 0;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(RegistryManager.block_dawnstone);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, OFFSET_PROPERTY);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        return new ItemStack(RegistryManager.block_dawnstone, 1, 0);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return this.getExtendedState(state, worldIn, pos);
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        Offset offset = this.getOffset(world, pos);
        return state.withProperty(OFFSET_PROPERTY, this.getOffset(world, pos));
    }

    private Offset getOffset(IBlockAccess world, BlockPos pos)
    {
        Block front = world.getBlockState(pos.north()).getBlock();
        Block back = world.getBlockState(pos.south()).getBlock();
        Block left = world.getBlockState(pos.west()).getBlock();
        Block right = world.getBlockState(pos.east()).getBlock();
        if (front == this && back == this && left == this && right == this)
        {
            return Offset.NONE;
        }

        if (back == this && left == this && right == this)
        {
            return Offset.FRONT;
        }

        if (front == this && left == this && right == this)
        {
            return Offset.BACK;
        }

        if (front == this && back == this && left == this)
        {
            return Offset.RIGHT;
        }

        if (front == this && back == this && right == this)
        {
            return Offset.LEFT;
        }

        if (front == this && left == this)
        {
            return Offset.RIGHT_BACK;
        }

        if (front == this && right == this)
        {
            return Offset.LEFT_BACK;
        }

        if (back == this && left == this)
        {
            return Offset.RIGHT_FRONT;
        }

        return Offset.LEFT_FRONT;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        IBlockState sanityCheck = worldIn.getBlockState(pos);
        if (sanityCheck == state)
        {
            Offset offsetEnum = this.getOffset(worldIn, pos);
            BlockPos offset = pos;
            for (EnumFacing facing : offsetEnum.directionOffsets)
            {
                offset = offset.offset(facing);
            }

            boolean structValid = true;
            for (int[] ints : CommonHandler.KERNEL)
            {
                BlockPos at = offset.add(ints[0], 0, ints[1]);
                if (worldIn.getBlockState(at).getBlock() != this)
                {
                    structValid = false;
                    break;
                }
            }

            if (!structValid)
            {
                for (int[] ints : CommonHandler.KERNEL)
                {
                    BlockPos at = offset.add(ints[0], 0, ints[1]);
                    if (worldIn.getBlockState(at).getBlock() == this)
                    {
                        worldIn.setBlockState(at, RegistryManager.block_dawnstone.getDefaultState(), 0b10);
                    }
                }
            }
        }

        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
    }
}
