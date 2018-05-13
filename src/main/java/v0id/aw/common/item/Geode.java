package v0id.aw.common.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import v0id.aw.common.creativetabs.AWTabs;
import v0id.aw.lib.AWConsts;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by V0idWa1k3r on 04-Jun-17.
 */
public class Geode extends Item
{
    public enum Type implements IStringSerializable
    {
        BASIC(null),
        COLD(BiomeDictionary.Type.COLD),
        HOT(BiomeDictionary.Type.HOT),
        OCEAN(BiomeDictionary.Type.OCEAN),
        END(BiomeDictionary.Type.END),
        NETHER(BiomeDictionary.Type.NETHER);

        Type(BiomeDictionary.Type associated)
        {
            this.associated = associated;
        }

        @Nullable
        private final BiomeDictionary.Type associated;

        public static Type getByBiome(Biome biome)
        {
            for (BiomeDictionary.Type type : BiomeDictionary.getTypes(biome))
            {
                Type t = getByBiome(type);
                if (t != BASIC)
                {
                    return t;
                }
            }

            return BASIC;
        }

        public static Type getByBiome(BiomeDictionary.Type biome)
        {
            for (Type type : values())
            {
                if (type.associated != null && type.associated.equals(biome))
                {
                    return type;
                }
            }

            return BASIC;
        }


        @Override
        public String getName()
        {
            return this.name().toLowerCase();
        }
    }

    public Geode()
    {
        super();
        this.setRegistryName(AWConsts.itemGeode);
        this.setHasSubtypes(true);
        this.setUnlocalizedName("aw.geode");
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + '.' + this.getType(stack).getName();
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(I18n.format("aw.tooltip.item.geode." + Type.values()[Math.min(stack.getMetadata(), Type.values().length - 1)].getName()));
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems)
    {
        if (tab == AWTabs.TAB_AW)
        {
            Stream.of(Type.values()).forEach(t -> subItems.add(new ItemStack(this, 1, t.ordinal())));
        }
    }

    public static Type getType(ItemStack stack)
    {
        return Type.values()[Math.min(stack.getMetadata(), Type.values().length - 1)];
    }
}
