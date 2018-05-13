package v0id.aw.common.block.forge;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import teamroots.embers.item.ItemTinkerHammer;
import v0id.aw.common.block.AWBlocks;
import v0id.aw.common.creativetabs.AWTabs;
import v0id.aw.common.tile.*;
import v0id.aw.lib.AWConsts;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Created by V0idWa1k3r on 02-Jun-17.
 */
public class Component extends Block
{
    public enum Type implements IStringSerializable
    {
        FORGE(true),
        HEATER(false),
        COOLER(false),
        HEAT_VENT(false),
        METAL_FORMER(true),
        ANVIL(true);

        Type(boolean isTopPart)
        {
            this.isTopPart = isTopPart;
        }

        public boolean isTopPart()
        {
            return isTopPart;
        }

        private final boolean isTopPart;

        @Override
        public String getName()
        {
            return this.name().toLowerCase();
        }
    }

    public static final PropertyEnum<Type> TYPE_PROPERTY = PropertyEnum.create("type", Type.class);
    public static final PropertyEnum<EnumFacing> FACING_PROPERTY = PropertyEnum.create("facing", EnumFacing.class);

    public static final Map<EnumFacing, AxisAlignedBB> VENT_AABB = Maps.newEnumMap(EnumFacing.class);
    public static final AxisAlignedBB TOP_AABB = new AxisAlignedBB(0, 0, 0, 1, 0.25, 1);

    static
    {
        VENT_AABB.put(EnumFacing.WEST, new AxisAlignedBB(0, 0, 0, 0.125, 1, 1));
        VENT_AABB.put(EnumFacing.EAST, new AxisAlignedBB(1 - 0.125, 0, 0, 1, 1, 1));
        VENT_AABB.put(EnumFacing.NORTH, new AxisAlignedBB(0, 0, 0, 1, 1, 0.125));
        VENT_AABB.put(EnumFacing.SOUTH, new AxisAlignedBB(0, 0, 1 - 0.125, 1, 1, 1));
    }

    public Component()
    {
        super(Material.IRON);
        this.setRegistryName(AWConsts.blockComponent);
        this.setUnlocalizedName("aw.component");
        this.setHardness(3);
        this.setResistance(3);
        this.setSoundType(SoundType.METAL);
        this.setDefaultState(this.getDefaultState().withProperty(TYPE_PROPERTY, Type.FORGE).withProperty(FACING_PROPERTY, EnumFacing.NORTH));
        this.setCreativeTab(AWTabs.TAB_AW);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        if (state.getValue(TYPE_PROPERTY) == Type.HEAT_VENT)
        {
            return VENT_AABB.get(this.findConnecton(source, pos));
        }

        if (state.getValue(TYPE_PROPERTY) == Type.ANVIL || state.getValue(TYPE_PROPERTY) == Type.METAL_FORMER)
        {
            return TOP_AABB;
        }

        return super.getBoundingBox(state, source, pos);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(TYPE_PROPERTY, Type.values()[Math.min(meta, Type.values().length - 1)]);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(TYPE_PROPERTY).ordinal();
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return this.getExtendedState(state, worldIn, pos);
    }

    @Override
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, TYPE_PROPERTY, FACING_PROPERTY);
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return state.withProperty(FACING_PROPERTY, state.getValue(TYPE_PROPERTY).isTopPart ? EnumFacing.DOWN : state.getValue(TYPE_PROPERTY) == Type.HEAT_VENT ? this.findConnecton(world, pos).getOpposite() : this.findConnecton(world, pos));
    }

    private EnumFacing findConnecton(IBlockAccess world, BlockPos pos)
    {
        for (EnumFacing facing : EnumFacing.HORIZONTALS)
        {
            BlockPos checked = pos.offset(facing);
            if (world.getBlockState(checked).getBlock() == AWBlocks.FORGE_STRUCTURE)
            {
                return facing;
            }
        }

        return EnumFacing.NORTH;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    {
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(TYPE_PROPERTY, Type.values()[Math.min(meta, Type.values().length - 1)]);
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
    {
        for (int i = 0; i < Type.values().length; i++)
        {
            items.add(new ItemStack(this, 1, i));
        }
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
        switch (state.getValue(TYPE_PROPERTY))
        {
            case ANVIL:
            {
                return new TileAnvil();
            }

            case FORGE:
            {
                return new TileForge();
            }

            case COOLER:
            {
                return new TileCooler();
            }

            case HEATER:
            {
                return new TileHeater();
            }

            case HEAT_VENT:
            {
                return new TileHeatVent();
            }

            case METAL_FORMER:
            {
                return new TileMetalFormer();
            }

            default:
            {
                return null;
            }
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }

        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileMetalFormer)
        {
            TileMetalFormer former = (TileMetalFormer) tile;
            former.progress = 0;
            IItemHandler handler = former.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
            if (handler != null)
            {
                ItemStack stack = handler.getStackInSlot(0);
                if (stack.isEmpty())
                {
                    ItemStack current = playerIn.getHeldItem(hand);
                    if (!current.isEmpty())
                    {
                        ItemStack toSet = current.copy();
                        toSet.setCount(1);
                        current.shrink(1);
                        handler.insertItem(0, toSet, false);
                        return true;
                    }
                }
                else
                {
                    EntityItem item = new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack.copy());
                    item.setDefaultPickupDelay();
                    worldIn.spawnEntity(item);
                    handler.extractItem(0, 1, false);
                    return true;
                }
            }
        }

        if (tile instanceof TileAnvil)
        {
            TileAnvil anvil = (TileAnvil) tile;
            ItemStack current = playerIn.getHeldItem(hand);
            if (anvil.hasRecipe.isPresent() && current.getItem() instanceof ItemTinkerHammer)
            {
                anvil.activate();
            }
            else
            {
                anvil.progress = 0;
                IItemHandler handler = anvil.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
                if (handler != null)
                {
                    ItemStack stack = handler.getStackInSlot(0);
                    if (stack.isEmpty())
                    {
                        if (!current.isEmpty())
                        {
                            ItemStack toSet = current.copy();
                            toSet.setCount(1);
                            current.shrink(1);
                            handler.insertItem(0, toSet, false);
                            return true;
                        }
                    }
                    else
                    {
                        EntityItem item = new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack.copy());
                        item.setDefaultPickupDelay();
                        worldIn.spawnEntity(item);
                        handler.extractItem(0, 1, false);
                        return true;
                    }
                }
            }
        }

        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        super.breakBlock(worldIn, pos, state);
        worldIn.removeTileEntity(pos);
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return state.getValue(TYPE_PROPERTY).ordinal();
    }

    @Override
    public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        if (state.getValue(TYPE_PROPERTY) == Type.FORGE)
            return ((TileForge) world.getTileEntity(pos)).lightValueIndex;
        return 0;
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return true;
    }

    @Override
    public boolean shouldCheckWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return 90;
    }
}
