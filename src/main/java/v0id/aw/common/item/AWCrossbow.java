package v0id.aw.common.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import v0id.aw.AetherWorks;
import v0id.aw.common.creativetabs.AWTabs;
import v0id.aw.common.entity.AWArrow;
import v0id.aw.common.entity.AWMagma;
import v0id.aw.lib.AWConsts;

import javax.annotation.Nullable;
import java.util.List;

public class AWCrossbow extends ItemBow
{
    public enum Type
    {
        QUARTZ,
        MAGMA
    }

    private Type type;

    public AWCrossbow(Type type)
    {
        this.setMaxStackSize(1);
        this.setMaxDamage(ToolMaterial.DIAMOND.getMaxUses());
        this.setCreativeTab(AWTabs.TAB_AW);
        this.setRegistryName(type == Type.QUARTZ ? AWConsts.itemCrossbowQuartz : AWConsts.itemCrossbowMagma);
        this.setUnlocalizedName("aw.crossbow." + type.name().toLowerCase());
        this.type = type;
        this.addPropertyOverride(new ResourceLocation("draw"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            {
                if (entityIn == null)
                {
                    return 0.0F;
                }
                else
                {
                    return stack.hasTagCompound() && stack.getTagCompound().hasKey("arrow", Constants.NBT.TAG_COMPOUND) ? Math.max((float)stack.getTagCompound().getInteger("draw") / 20F, 0.1F) : 0.0F;
                }
            }
        });
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 0;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.NONE;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
    {
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if (stack.hasTagCompound() && stack.getTagCompound().getInteger("draw") >= 20)
        {
            ItemStack arrow = this.getArrow(stack);
            stack.getTagCompound().setInteger("draw", 0);
            stack.getTagCompound().removeTag("arrow");
            boolean infinity = playerIn.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
            int velocity = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, playerIn, 1000, true);
            if (velocity < 0)
            {
                return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
            }

            if (!worldIn.isRemote)
            {
                if (this.type == Type.MAGMA)
                {
                    for (int i = 0; i < 8 + worldIn.rand.nextInt(16); ++i)
                    {
                        AWMagma magma = new AWMagma(worldIn, playerIn);
                        magma.shoot(playerIn, playerIn.rotationPitch + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 16, playerIn.rotationYaw + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 45, 0.0F, 1.0F, 0);
                        worldIn.spawnEntity(magma);
                    }
                }

                ItemArrow itemarrow = (ItemArrow)(arrow.getItem() instanceof ItemArrow ? arrow.getItem() : Items.ARROW);
                int times = this.type == Type.MAGMA ? 3 + worldIn.rand.nextInt(6) : 1;
                for (int i = 0; i < times; ++i)
                {
                    EntityArrow entityarrow = itemarrow.createArrow(worldIn, arrow, playerIn);
                    if (entityarrow instanceof EntityTippedArrow)
                    {
                        entityarrow = new AWArrow(worldIn, playerIn);
                        ((AWArrow) entityarrow).setPotionEffect(arrow);
                        if (this.type == Type.QUARTZ)
                        {
                            ((AWArrow) entityarrow).setBypassArmor(true);
                            entityarrow.setDamage(16);
                        }
                        else
                        {
                            ((AWArrow) entityarrow).setAllowDefaultDamage(true);
                        }
                    }

                    entityarrow.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 4.5F, i);
                    entityarrow.setIsCritical(true);
                    int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
                    if (j > 0)
                    {
                        if (type == Type.QUARTZ)
                        {
                            j *= 2;
                        }

                        entityarrow.setDamage(entityarrow.getDamage() + (double) j * 0.5D + 0.5D);
                    }

                    int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
                    if (k > 0)
                    {
                        entityarrow.setKnockbackStrength(k);
                    }

                    if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0)
                    {
                        entityarrow.setFire(100);
                    }

                    stack.damageItem(1, playerIn);
                    if (i > 0 || infinity || playerIn.capabilities.isCreativeMode && (arrow.getItem() == Items.SPECTRAL_ARROW || arrow.getItem() == Items.TIPPED_ARROW))
                    {
                        entityarrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
                    }

                    worldIn.spawnEntity(entityarrow);
                }
            }

            worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + 1.3F * 0.5F);
            playerIn.addStat(StatList.getObjectUseStats(this));
        }

        return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }

    @Override
    public boolean hasEffect(ItemStack stack)
    {
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(AetherWorks.proxy.translate("aw.enchantment.aetherid", "II"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
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

        if (!(entityIn instanceof EntityPlayer))
        {
            return;
        }

        if (!this.hasArrow(stack))
        {
            ItemStack arrow = this.findAmmo((EntityPlayer) entityIn);
            if (((EntityPlayer)entityIn).capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0)
            {
                NBTTagCompound tag = arrow.getTagCompound();
                arrow = new ItemStack(arrow.isEmpty() ? Items.ARROW : arrow.getItem(), 1, arrow.isEmpty() ? 0 : arrow.getMetadata());
                arrow.setTagCompound(tag);
            }

            boolean empty = arrow.isEmpty();
            ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(stack, worldIn, (EntityPlayer)entityIn, EnumHand.MAIN_HAND, empty);
            if (!empty && (ret == null || ret.getType() == EnumActionResult.SUCCESS))
            {
                ItemStack arrowCPY = arrow.copy();
                arrowCPY.setCount(1);
                this.setArrow(stack, arrowCPY);
                arrow.shrink(1);
            }
        }
        else
        {
            if (stack.getTagCompound().getInteger("draw") < 20)
            {
                stack.getTagCompound().setInteger("draw", stack.getTagCompound().getInteger("draw") + 1);
                if (stack.getTagCompound().getInteger("draw") == 20)
                {
                    worldIn.playSound(entityIn.posX, entityIn.posY, entityIn.posZ, SoundEvents.BLOCK_IRON_DOOR_OPEN, SoundCategory.PLAYERS, 1, 2, false);
                }
            }
        }
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
    {
        return !ItemStack.areItemsEqual(oldStack, newStack) || oldStack.getMetadata() != newStack.getMetadata() || slotChanged;
    }

    private boolean hasArrow(ItemStack stack)
    {
        return stack.hasTagCompound() && stack.getTagCompound().hasKey("arrow", Constants.NBT.TAG_COMPOUND);
    }

    private ItemStack getArrow(ItemStack stack)
    {
        if (this.hasArrow(stack))
        {
            return new ItemStack(stack.getTagCompound().getCompoundTag("arrow"));
        }
        else
        {
            return ItemStack.EMPTY;
        }
    }

    private void setArrow(ItemStack stack, ItemStack arrow)
    {
        if (!stack.hasTagCompound())
        {
            stack.setTagCompound(new NBTTagCompound());
        }

        stack.getTagCompound().setTag("arrow", arrow.serializeNBT());
    }

    private ItemStack findAmmo(EntityPlayer player)
    {
        if (this.isArrow(player.getHeldItem(EnumHand.OFF_HAND)))
        {
            return player.getHeldItem(EnumHand.OFF_HAND);
        }
        else if (this.isArrow(player.getHeldItem(EnumHand.MAIN_HAND)))
        {
            return player.getHeldItem(EnumHand.MAIN_HAND);
        }
        else
        {
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i)
            {
                ItemStack itemstack = player.inventory.getStackInSlot(i);

                if (this.isArrow(itemstack) && !itemstack.isEmpty())
                {
                    return itemstack;
                }
            }

            return ItemStack.EMPTY;
        }
    }
}
