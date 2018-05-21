package v0id.aw.common.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import v0id.aw.AetherWorks;
import v0id.aw.common.creativetabs.AWTabs;
import v0id.aw.lib.AWConsts;

import javax.annotation.Nullable;
import java.util.List;

public class AWCrown extends ItemArmor
{
    public AWCrown()
    {
        super(ArmorMaterial.DIAMOND, 3, EntityEquipmentSlot.HEAD);
        this.setRegistryName(AWConsts.itemCrown);
        this.setUnlocalizedName("aw.crown");
        this.setCreativeTab(AWTabs.TAB_AW);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(AetherWorks.proxy.translate("aw.enchantment.aetherid", "II"));
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("potionType", Constants.NBT.TAG_STRING))
        {
            tooltip.add(I18n.format("aw.tooltip.item.crown.filled"));
            PotionType type = PotionType.REGISTRY.getObject(new ResourceLocation(stack.getTagCompound().getString("potionType")));
            if (type.getEffects().isEmpty())
            {
                tooltip.add(I18n.format("effect.none"));
            }
            else
            {
                for (PotionEffect effect : type.getEffects())
                {
                    String name = I18n.format(effect.getEffectName()).trim();
                    if (effect.getAmplifier() > 0)
                    {
                        name = name + " " + I18n.format("potion.potency." + effect.getAmplifier()).trim();
                    }

                    tooltip.add(name);
                }
            }
        }
        else
        {
            tooltip.add(I18n.format("aw.tooltip.item.crown.empty"));
        }

        super.addInformation(stack, worldIn, tooltip, flagIn);
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
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack)
    {
        super.onArmorTick(world, player, itemStack);
        if (player.ticksExisted % 100 == 0 && itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("potionType", Constants.NBT.TAG_STRING))
        {
            PotionType type = PotionType.REGISTRY.getObject(new ResourceLocation(itemStack.getTagCompound().getString("potionType")));
            for (PotionEffect effect1 : type.getEffects())
            {
                PotionEffect effect = new PotionEffect(effect1.getPotion(), 400, effect1.getAmplifier(), effect1.getIsAmbient(), false);
                if (effect.getPotion() == MobEffects.INSTANT_HEALTH)
                {
                    effect = new PotionEffect(MobEffects.REGENERATION, effect.getDuration(), effect.getAmplifier(), effect.getIsAmbient(), effect.doesShowParticles());
                }

                if (effect.getPotion() == MobEffects.INSTANT_DAMAGE)
                {
                    effect = new PotionEffect(MobEffects.POISON, effect.getDuration(), effect.getAmplifier(), effect.getIsAmbient(), effect.doesShowParticles());
                }

                player.addPotionEffect(effect);
            }
        }
    }

    @Override
    public boolean hasEffect(ItemStack stack)
    {
        return true;
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type)
    {
        return "aetherworks:textures/armor/aether_crown.png";
    }
}
