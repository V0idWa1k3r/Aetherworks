package v0id.aw.common.item;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import teamroots.embers.util.EmberInventoryUtil;
import v0id.aw.AetherWorks;
import v0id.aw.common.creativetabs.AWTabs;
import v0id.aw.common.tile.TileForge;
import v0id.aw.lib.AWConsts;
import v0id.aw.lib.AWHarvestHelper;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by V0idWa1k3r on 04-Jun-17.
 */
public class AWAxe extends ItemAxe
{
    public enum Type implements IStringSerializable
    {
        PRISMARINE,
        ENDER;

        @Override
        public String getName()
        {
            return this.name().toLowerCase();
        }
    }

    public Type getType()
    {
        return type;
    }

    private final Type type;

    public AWAxe(Type type)
    {
        super(AWConsts.AETHERIUM, 10F, -3F);
        this.type = type;
        if (type == Type.PRISMARINE)
        {
            this.setRegistryName(AWConsts.itemAxePrismarine);
            this.setUnlocalizedName("aw.prismarine_axe");
        }
        else
        {
            this.setRegistryName(AWConsts.itemAxeEnder);
            this.setUnlocalizedName("aw.ender_axe");
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
                            if (this.getType() == Type.PRISMARINE)
                            {
                                AetherWorks.proxy.spawnParticleGlow(worldIn, at.getX() + worldIn.rand.nextFloat(), at.getY() + worldIn.rand.nextFloat(), at.getZ() + worldIn.rand.nextFloat(), (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) / 100, (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) / 100, (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) / 100, 139, 195, 184, 1, 100);
                            }
                            else
                            {
                                AetherWorks.proxy.spawnParticleGlow(worldIn, at.getX() + worldIn.rand.nextFloat(), at.getY() + worldIn.rand.nextFloat(), at.getZ() + worldIn.rand.nextFloat(), (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) / 100, (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) / 100, (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) / 100, 3, 38, 32, 1, 100);
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
        else
        {
            if (this.getType() == Type.PRISMARINE)
            {
                playerIn.setActiveHand(handIn);
                return ActionResult.newResult(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
            }
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

    public static final ThreadLocal<List<BlockPos>> rememeredRecusrsionPositions = ThreadLocal.withInitial(() -> Lists.newArrayListWithCapacity(512));

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        EnumActionResult result = super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
        if (result == EnumActionResult.PASS)
        {
            if (this.getType() == Type.ENDER)
            {
                IBlockState usedOn = worldIn.getBlockState(pos);
                Block blockusedOn = usedOn.getBlock();
                if (blockusedOn.isWood(worldIn, pos))
                {
                    if (!worldIn.isRemote)
                    {
                        BlockPos posRecursive = findFurthestLogRecursive(worldIn, pos, blockusedOn, 0, rememeredRecusrsionPositions.get());
                        rememeredRecusrsionPositions.get().clear();
                        if (player instanceof EntityPlayerMP)
                        {
                            SoundType blockSoundType = blockusedOn.getSoundType(usedOn, worldIn, pos, player);
                            worldIn.playSound(null, posRecursive, blockSoundType.getBreakSound(), SoundCategory.BLOCKS, blockSoundType.getVolume(), blockSoundType.getPitch());
                            worldIn.playSound(null, pos, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 0.15F, 2 - worldIn.rand.nextFloat());
                            AWHarvestHelper.harvestBlock(worldIn, (EntityPlayerMP) player, posRecursive, worldIn.getBlockState(posRecursive), player.getHeldItem(hand));
                            player.getHeldItem(hand).damageItem(1, player);
                        }
                    }

                    return EnumActionResult.SUCCESS;
                }
            }
        }

        return result;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return this.getType() == Type.ENDER ? 0 : 72000;
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count)
    {
        World worldIn = player.getEntityWorld();
        BlockPos pos = player.getPosition();
        for (int i = 0; i < 32; i++)
        {
            Vec3d randomVec = new Vec3d(worldIn.rand.nextDouble() - worldIn.rand.nextDouble(), 0, worldIn.rand.nextDouble() - worldIn.rand.nextDouble()).normalize().scale(worldIn.rand.nextDouble() * 16);
            BlockPos randomPos = pos.add(randomVec.x, worldIn.rand.nextBoolean() ? -1 : 0, randomVec.z);
            IBlockState state = worldIn.getBlockState(randomPos);
            if (!worldIn.isRemote)
            {
                state.getBlock().randomTick(worldIn, randomPos, state, worldIn.rand);
                if (worldIn.rand.nextFloat() < 0.01F)
                {
                    if (state.getBlock() instanceof IGrowable)
                    {
                        IGrowable igrowable = (IGrowable)state.getBlock();
                        if (igrowable.canGrow(worldIn, randomPos, state, false))
                        {
                            if (igrowable.canUseBonemeal(worldIn, worldIn.rand, randomPos, state))
                            {
                                igrowable.grow(worldIn, worldIn.rand, randomPos, state);
                            }
                        }
                    }
                }
            }

            AetherWorks.proxy.spawnParticleGlow(worldIn, randomPos.getX() + worldIn.rand.nextFloat(), randomPos.getY() + worldIn.rand.nextFloat(), randomPos.getZ() + worldIn.rand.nextFloat(), 0, 0.05F, 0, 139, 195, 184, 1, 60);
        }

        if (!worldIn.isRemote && player.ticksExisted % 20 == 0)
        {
            player.getHeldItemMainhand().damageItem(1, player);
        }
    }

    private static BlockPos findFurthestLogRecursive(IBlockAccess world, BlockPos at, Block compareTo, int currentIteration, List<BlockPos> knownPositions)
    {
        if (++currentIteration >= 512)
        {
            return at;
        }
        else
        {
            if (world.getBlockState(at.up()).getBlock() == compareTo)
            {
                knownPositions.add(at);
                return findFurthestLogRecursive(world, at.up(), compareTo, currentIteration, knownPositions);
            }
            else
            {
                for (int[] ints : TileForge.KERNEL_TOP)
                {
                    BlockPos offset = at.add(ints[0], 0, ints[1]);
                    if (knownPositions.contains(offset))
                    {
                        continue;
                    }

                    if (world.getBlockState(offset).getBlock() == compareTo)
                    {
                        knownPositions.add(at);
                        return findFurthestLogRecursive(world, offset, compareTo, currentIteration, knownPositions);
                    }

                    if (world.getBlockState(offset.up()).getBlock() == compareTo)
                    {
                        knownPositions.add(at);
                        return findFurthestLogRecursive(world, offset.up(), compareTo, currentIteration, knownPositions);
                    }
                }
            }

            return at;
        }
    }
}
