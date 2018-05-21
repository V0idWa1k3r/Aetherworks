package v0id.aw.common.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import v0id.aw.common.creativetabs.AWTabs;
import v0id.aw.lib.AWConsts;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.StreamSupport;

public class PotionGem extends Item
{
    public PotionGem()
    {
        super();
        this.setHasSubtypes(true);
        this.setCreativeTab(AWTabs.TAB_AW);
        this.setRegistryName(AWConsts.itemPotionGemID);
        this.setUnlocalizedName("aw.potiongem");
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(I18n.format("aw.tooltip.item.potionGem"));
        if (stack.getMetadata() == 0)
        {
            tooltip.add(I18n.format("aw.tooltip.item.potionGem.empty"));
        }
        else
        {
            tooltip.add(I18n.format("aw.tooltip.item.potionGem.filled"));
            PotionType type = PotionType.REGISTRY.getObjectById(stack.getMetadata() - 1);
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
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (tab == AWTabs.TAB_AW)
        {
            items.add(new ItemStack(this, 1, 0));
            StreamSupport.stream(PotionType.REGISTRY.spliterator(), false).forEach(pt -> items.add(new ItemStack(this, 1, PotionType.REGISTRY.getIDForObject(pt) + 1)));
        }
    }

    @Override
    public boolean hasEffect(ItemStack stack)
    {
        return stack.getMetadata() > 0;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.RARE;
    }

    public int getColor(ItemStack stack, int index)
    {
        if (index == 0)
        {
            return 0xffffff;
        }
        else
        {
            if (stack.getMetadata() == 0)
            {
                return 0xffffff;
            }
            else
            {
                PotionType type = PotionType.REGISTRY.getObjectById(stack.getMetadata() - 1);
                float r = -1, g = -1, b = -1;
                if (type.getEffects().isEmpty())
                {
                    return 0xffffff;
                }
                else
                {
                    return type.getEffects().get(0).getPotion().getLiquidColor();
                }
            }
        }
    }
}
