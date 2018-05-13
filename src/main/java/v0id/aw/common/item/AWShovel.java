package v0id.aw.common.item;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import teamroots.embers.util.EmberInventoryUtil;
import v0id.aw.AetherWorks;
import v0id.aw.common.creativetabs.AWTabs;
import v0id.aw.lib.AWConsts;
import v0id.aw.lib.AWHarvestHelper;
import v0id.aw.lib.RecipeUtils;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by V0idWa1k3r on 04-Jun-17.
 */
public class AWShovel extends ItemSpade
{
    public enum Type implements IStringSerializable
    {
        REDSTONE,
        SLIME;

        @Override
        public String getName()
        {
            return this.name().toLowerCase();
        }
    }

    public enum Mode
    {
        EXTRUDE,
        EXTEND;

        Mode()
        {

        }

        public Mode getOpposite()
        {
            return this == EXTRUDE ? EXTEND : EXTRUDE;
        }
    }

    public Type getType()
    {
        return type;
    }

    private final Type type;

    public AWShovel(Type type)
    {
        super(AWConsts.AETHERIUM);
        this.type = type;
        if (type == Type.REDSTONE)
        {
            this.setRegistryName(AWConsts.itemShovelRedstone);
            this.setUnlocalizedName("aw.redstone_shovel");
        }
        else
        {
            this.setRegistryName(AWConsts.itemShovelSlime);
            this.setUnlocalizedName("aw.slime_shovel");
        }

        this.setCreativeTab(AWTabs.TAB_AW);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(AetherWorks.proxy.translate("aw.enchantment.aetherid", "II"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(AetherWorks.proxy.translate("aw.tooltip.item.pickaxe.aoe", this.isAOEEnabled(stack)));
    }

    private boolean isAOEEnabled(ItemStack stack)
    {
        return stack.hasTagCompound() && stack.getTagCompound().hasKey("aw.aoe", Constants.NBT.TAG_BYTE) && stack.getTagCompound().getBoolean("aw.aoe");
    }

    public static ItemStack getFocusedStack(ItemStack of)
    {
        return of.hasTagCompound() && of.getTagCompound().hasKey("aw.focus", Constants.NBT.TAG_COMPOUND) ? new ItemStack(of.getTagCompound().getCompoundTag("aw.focus")) : ItemStack.EMPTY;
    }

    public static void setFocusedStack(ItemStack of, ItemStack toSet)
    {
        of.setTagInfo("aw.focus", toSet.serializeNBT());
    }

    public static Mode getMode(ItemStack of)
    {
        return of.hasTagCompound() && of.getTagCompound().getBoolean("extend") ? Mode.EXTEND : Mode.EXTRUDE;
    }

    public static void setMode(ItemStack of, Mode m)
    {
        of.setTagInfo("extend", new NBTTagByte((byte) (m == Mode.EXTEND ? 1 : 0)));
    }

    @Override
    public boolean hasEffect(ItemStack stack)
    {
        return true;
    }

    private ThreadLocal<Boolean> recursiveBreak = ThreadLocal.withInitial(() -> Boolean.FALSE);

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
    {
        if (!recursiveBreak.get() && this.isAOEEnabled(stack) && this.consumeEmbers(entityLiving, 8))
        {
            recursiveBreak.set(Boolean.TRUE);
            EnumFacing direction = EnumFacing.getDirectionFromEntityLiving(pos, entityLiving);
            for (int dx = -1; dx <= 1; ++dx)
            {
                for (int dy = -1; dy <= 1; ++dy)
                {
                    if (dx == 0 && dy == 0)
                    {
                        continue;
                    }

                    int oX = direction.getFrontOffsetX() == 0 ? dx : 0;
                    int oY = direction.getFrontOffsetY() == 0 ? dy : 0;
                    int oZ = direction.getFrontOffsetZ() == 0 ? direction.getFrontOffsetY() == 0 ? dx : dy : 0;
                    BlockPos at = pos.add(oX, oY, oZ);
                    IBlockState stateAt = worldIn.getBlockState(at);
                    if (!stateAt.equals(state))
                    {
                        continue;
                    }

                    if (worldIn.isRemote)
                    {
                        for (int i = 0; i < 10; i++)
                        {
                            worldIn.spawnParticle(EnumParticleTypes.BLOCK_CRACK, at.getX() + worldIn.rand.nextFloat(), at.getY() + worldIn.rand.nextFloat(), at.getZ() + worldIn.rand.nextFloat(), 0, 0, 0, Block.getStateId(stateAt));
                            if (this.getType() == Type.REDSTONE)
                            {
                                AetherWorks.proxy.spawnParticleGlow(worldIn, at.getX() + worldIn.rand.nextFloat(), at.getY() + worldIn.rand.nextFloat(), at.getZ() + worldIn.rand.nextFloat(), (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) / 100, (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) / 100, (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) / 100, 114, 0, 0, 1, 100);
                            }
                            else
                            {
                                AetherWorks.proxy.spawnParticleGlow(worldIn, at.getX() + worldIn.rand.nextFloat(), at.getY() + worldIn.rand.nextFloat(), at.getZ() + worldIn.rand.nextFloat(), (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) / 100, (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) / 100, (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) / 100, 132, 200, 115, 1, 100);
                            }
                        }
                    }

                    if (entityLiving instanceof EntityPlayerMP)
                    {
                        EntityPlayerMP playerMP = (EntityPlayerMP) entityLiving;
                        PlayerInteractionManager manager = playerMP.interactionManager;
                        manager.tryHarvestBlock(at);
                    }
                }
            }

            recursiveBreak.set(Boolean.FALSE);
        }

        return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
    }

    public boolean consumeEmbers(EntityLivingBase player, double value)
    {
        if (player instanceof EntityPlayer)
        {
            if (EmberInventoryUtil.getEmberTotal((EntityPlayer) player) >= value)
            {
                EmberInventoryUtil.removeEmber((EntityPlayer) player, value);
                return true;
            }
        }

        return false;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        if (playerIn.isSneaking())
        {
            ItemStack current = playerIn.getHeldItem(handIn);
            boolean aoe = this.isAOEEnabled(current);
            current.setTagInfo("aw.aoe", new NBTTagByte((byte) (aoe ? 0 : 1)));
            worldIn.playSound(playerIn, playerIn.getPosition(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1, 1);
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if (!worldIn.isRemote)
        {
            if (stack.getItemDamage() > 0 && worldIn.getWorldTime() % 24000 >= 15000 && worldIn.getWorldTime() % 24000 <= 21000 && worldIn.canBlockSeeSky(entityIn.getPosition().up()))
            {
                if (worldIn.rand.nextFloat() <= 0.05F)
                {
                    stack.setItemDamage(stack.getItemDamage() - 1);
                }
            }
        }

        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    public static ItemStack findInPlayersInventory(ItemStack focus, ItemStack predicted, EntityPlayer player)
    {
        if (predicted.isEmpty())
        {
            return ItemStack.EMPTY;
        }

        int index = focus.isEmpty() ? -1 : player.inventory.getSlotFor(focus);
        if (index != -1)
        {
            return player.inventory.getStackInSlot(index);
        }

        index = player.inventory.getSlotFor(predicted);
        if (index != -1)
        {
            return player.inventory.getStackInSlot(index);
        }

        for (ItemStack stack : player.inventory.mainInventory)
        {
            if (!stack.isEmpty() && stack.getItem() instanceof ItemBlock)
            {
                return stack;
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack is = AWHarvestHelper.getValidItemStackFor(worldIn.getBlockState(pos));
        if (!is.isEmpty())
        {
            if (player.isSneaking())
            {
                if (!worldIn.isRemote)
                {
                    if (RecipeUtils.areItemStacksEqual(getFocusedStack(player.getHeldItem(hand)), is))
                    {
                        setFocusedStack(player.getHeldItem(hand), ItemStack.EMPTY);
                    }
                    else
                    {
                        setFocusedStack(player.getHeldItem(hand), is);
                    }
                }
            }
            else
            {
                if (this.getType() == Type.REDSTONE)
                {
                    if (getMode(player.getHeldItem(hand)) == Mode.EXTEND)
                    {
                        loop: for (int dx = -1; dx <= 1; ++dx)
                        {
                            for (int dy = -1; dy <= 1; ++dy)
                            {
                                if (dx == 0 && dy == 0)
                                {
                                    continue;
                                }

                                int oX = facing.getFrontOffsetX() == 0 ? dx : 0;
                                int oY = facing.getFrontOffsetY() == 0 ? dy : 0;
                                int oZ = facing.getFrontOffsetZ() == 0 ? facing.getFrontOffsetY() == 0 ? dx : dy : 0;
                                BlockPos at = pos.add(oX, oY, oZ);
                                IBlockState stateAt = worldIn.getBlockState(at);
                                ItemStack toUse = findInPlayersInventory(getFocusedStack(player.getHeldItem(hand)), is, player);
                                if (toUse.isEmpty())
                                {
                                    break loop;
                                }

                                if (stateAt.getBlock().isReplaceable(worldIn, at) && player.canPlayerEdit(at, facing, toUse))
                                {
                                    if (!worldIn.isRemote)
                                    {
                                        Block b = ((ItemBlock) toUse.getItem()).getBlock();
                                        IBlockState state = b.getStateFromMeta(toUse.getMetadata());
                                        worldIn.setBlockState(at, state);
                                        toUse.shrink(1);
                                        SoundType type = b.getSoundType(state, worldIn, at, player);
                                        worldIn.playSound(null, at, type.getPlaceSound(), SoundCategory.BLOCKS, type.getPitch(), type.getVolume());
                                        player.getHeldItem(hand).damageItem(1, player);
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        Vec3d look = player.getLookVec();
                        if (facing.getAxis() == EnumFacing.Axis.Y)
                        {
                            EnumFacing horizontal = EnumFacing.getFacingFromVector((float) look.x, 0, (float) look.z);
                            loop: for (int dh = -1; dh <= 1; ++dh)
                            {
                                for (int dy = 1; dy <= 3; ++dy)
                                {
                                    int dx = horizontal.getAxis() == EnumFacing.Axis.Z ? dh : 0;
                                    int dz = horizontal.getAxis() == EnumFacing.Axis.X ? dh : 0;
                                    BlockPos at = pos.add(dx, facing == EnumFacing.UP ? dy : -dy, dz);
                                    IBlockState stateAt = worldIn.getBlockState(at);
                                    ItemStack toUse = findInPlayersInventory(getFocusedStack(player.getHeldItem(hand)), is, player);
                                    if (toUse.isEmpty())
                                    {
                                        break loop;
                                    }

                                    if (stateAt.getBlock().isReplaceable(worldIn, at) && player.canPlayerEdit(at, facing, toUse))
                                    {
                                        if (!worldIn.isRemote)
                                        {
                                            Block b = ((ItemBlock) toUse.getItem()).getBlock();
                                            IBlockState state = b.getStateFromMeta(toUse.getMetadata());
                                            worldIn.setBlockState(at, state);
                                            toUse.shrink(1);
                                            SoundType type = b.getSoundType(state, worldIn, at, player);
                                            worldIn.playSound(null, at, type.getPlaceSound(), SoundCategory.BLOCKS, type.getPitch(), type.getVolume());
                                            player.getHeldItem(hand).damageItem(1, player);
                                        }
                                    }
                                }
                            }
                        }
                        else
                        {
                            loop: for (int dx = -1; dx <= 1; ++dx)
                            {
                                for (int dz = -1; dz <= 1; ++dz)
                                {
                                    BlockPos at = pos.offset(facing, 2).add(dx, 0, dz);
                                    IBlockState stateAt = worldIn.getBlockState(at);
                                    ItemStack toUse = findInPlayersInventory(getFocusedStack(player.getHeldItem(hand)), is, player);
                                    if (toUse.isEmpty())
                                    {
                                        break loop;
                                    }

                                    if (stateAt.getBlock().isReplaceable(worldIn, at) && player.canPlayerEdit(at, facing, toUse))
                                    {
                                        if (!worldIn.isRemote)
                                        {
                                            Block b = ((ItemBlock) toUse.getItem()).getBlock();
                                            IBlockState state = b.getStateFromMeta(toUse.getMetadata());
                                            worldIn.setBlockState(at, state);
                                            toUse.shrink(1);
                                            SoundType type = b.getSoundType(state, worldIn, at, player);
                                            worldIn.playSound(null, at, type.getPlaceSound(), SoundCategory.BLOCKS, type.getPitch(), type.getVolume());
                                            player.getHeldItem(hand).damageItem(1, player);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                else
                {
                    if (!getFocusedStack(player.getHeldItem(hand)).isEmpty())
                    {
                        final ItemStack stack = getFocusedStack(player.getHeldItem(hand));
                        final ItemStack shovel = player.getHeldItem(hand);
                        IBlockState state = worldIn.getBlockState(pos);
                        if (state.getPlayerRelativeBlockHardness(player, worldIn, pos) != -1 && state.getPlayerRelativeBlockHardness(player, worldIn, pos) < 15)
                        {
                            if (player.canPlayerEdit(pos, facing, shovel))
                            {
                                AWHarvestHelper.AWExchangeNode node = new AWHarvestHelper.AWExchangeNode(player, worldIn, pos, 128, e -> e.getHeldItemMainhand().getItem().equals(this), stack);
                                AWHarvestHelper.addNode(player, node);
                            }
                        }
                    }
                }
            }

            return EnumActionResult.SUCCESS;
        }

        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
}
