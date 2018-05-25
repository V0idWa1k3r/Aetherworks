package v0id.aw.common.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import v0id.aw.common.creativetabs.AWTabs;
import v0id.aw.lib.AWConsts;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by V0idWa1k3r on 31-May-17.
 */
public class AWResource extends Item
{
    public enum ResourceType implements IStringSerializable
    {
        AETHER_SHARD,
        FOCUS_CRYSTAL,
        AETHERIUM_LENS,
        PLATE_AETHER,
        INGOT_AETHER,
        GEM_AETHER,
        CRUDE_TOOL_ROD,
        TOOL_ROD,
        INFUSED_TOOL_ROD(true),
        CRUDE_PICKAXE_HEAD,
        PICKAXE_HEAD,
        PICKAXE_HEAD_AETHER(true),
        PICKAXE_HEAD_EMBER(true),
        CRUDE_AXE_HEAD,
        AXE_HEAD,
        AXE_HEAD_PRISMARINE(true),
        AXE_HEAD_ENDER(true),
        CRUDE_SHOVEL_HEAD,
        SHOVEL_HEAD,
        SHOVEL_HEAD_REDSTONE(true),
        SHOVEL_HEAD_SLIME(true),
        AETHER_CROWN_CRUDE,
        AETHER_CROWN_MUNDANE,
        CRUDE_CROSSBOW_FRAME,
        CRUDE_CROSSBOW_LIMBS,
        CROSSBOW_FRAME,
        CROSSBOW_LIMBS,
        INFUSED_CROSSBOW_FRAME(true),
        CROSSBOW_LIMBS_QUARTZ(true),
        CROSSBOW_LIMBS_MAGMA(true);

        ResourceType()
        {
            this(false);
        }

        ResourceType(boolean hasEffect)
        {
            this.hasEffect = hasEffect;
        }

        private final boolean hasEffect;
        @Override
        public String getName()
        {
            return this.name().toLowerCase();
        }

        public int getMeta()
        {
            return this.ordinal();
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + '.' + ResourceType.values()[Math.min(stack.getMetadata(), ResourceType.values().length - 1)].getName();
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(I18n.format("aw.tooltip.item.resource." + ResourceType.values()[Math.min(stack.getMetadata(), ResourceType.values().length - 1)].getName()));
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (tab == AWTabs.TAB_AW)
        {
            Stream.of(ResourceType.values()).forEach(r -> items.add(new ItemStack(this, 1, r.getMeta())));
        }
    }

    public AWResource()
    {
        super();
        this.setHasSubtypes(true);
        this.setCreativeTab(AWTabs.TAB_AW);
        this.setRegistryName(AWConsts.itemResource);
        this.setUnlocalizedName("aw.resource");
    }

    @Override
    public boolean hasEffect(ItemStack stack)
    {
        return ResourceType.values()[Math.min(stack.getMetadata(), ResourceType.values().length - 1)].hasEffect;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return ResourceType.values()[Math.min(stack.getMetadata(), ResourceType.values().length - 1)].hasEffect ? EnumRarity.RARE : super.getRarity(stack);
    }
}
