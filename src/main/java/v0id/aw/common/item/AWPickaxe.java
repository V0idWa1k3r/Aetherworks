package v0id.aw.common.item;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import teamroots.embers.util.EmberInventoryUtil;
import v0id.aw.AetherWorks;
import v0id.aw.common.creativetabs.AWTabs;
import v0id.aw.lib.AWConsts;
import v0id.aw.lib.AWHarvestHelper;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by V0idWa1k3r on 04-Jun-17.
 */
public class AWPickaxe extends ItemPickaxe
{
    public enum Type implements IStringSerializable
    {
        AETHER,
        EMBER;

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

    public AWPickaxe(Type type)
    {
        super(AWConsts.AETHERIUM);
        this.type = type;
        if (type == Type.AETHER)
        {
            this.setRegistryName(AWConsts.itemPickaxeAether);
            this.setUnlocalizedName("aw.aether_pickaxe");
        }
        else
        {
            this.setRegistryName(AWConsts.itemPickaxeEmber);
            this.setUnlocalizedName("aw.ember_pickaxe");
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
        if (this.getType() == Type.EMBER && !worldIn.isRemote && worldIn.rand.nextInt(64) == 0)
        {
            Geode.Type rndGeodeType = Geode.Type.getByBiome(worldIn.getBiome(pos));
            ItemStack geode = new ItemStack(AWItems.GEODE, 1, rndGeodeType.ordinal());
            EntityItem item = new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, geode);
            item.setDefaultPickupDelay();
            worldIn.spawnEntity(item);
        }

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
                            if (this.getType() == Type.EMBER)
                            {
                                AetherWorks.proxy.spawnParticleGlow(worldIn, at.getX() + worldIn.rand.nextFloat(), at.getY() + worldIn.rand.nextFloat(), at.getZ() + worldIn.rand.nextFloat(), (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) / 100, (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) / 100, (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) / 100, 255, 64, 16, 1, 100);
                            }
                            else
                            {
                                AetherWorks.proxy.spawnParticleGlow(worldIn, at.getX() + worldIn.rand.nextFloat(), at.getY() + worldIn.rand.nextFloat(), at.getZ() + worldIn.rand.nextFloat(), (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) / 100, (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) / 100, (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) / 100, 0, 0.72F, 0.95F, 1, 100);
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

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        EnumActionResult result = super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
        if (result == EnumActionResult.PASS && hand == EnumHand.MAIN_HAND && this.getType() == Type.AETHER && this.canHarvestBlock(worldIn.getBlockState(pos), player.getHeldItem(hand)) && this.getDestroySpeed(player.getHeldItem(hand), worldIn.getBlockState(pos)) >= this.efficiency)
        {
            if (!worldIn.isRemote)
            {
                if (AWHarvestHelper.addNode(player, pos, 6, p -> p.getHeldItemMainhand().getItem() == this))
                {
                    player.swingArm(hand);
                }
            }
        }

        return result;
    }
}
