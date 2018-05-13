package v0id.aw.common.creativetabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import v0id.aw.common.item.AWItems;

/**
 * Created by V0idWa1k3r on 31-May-17.
 */
public class AWTabs
{
    public static final CreativeTabs TAB_AW = new CreativeTabs(CreativeTabs.getNextID(), "aetherworks")
    {
        @SideOnly(Side.CLIENT)
        @Override
        public ItemStack getTabIconItem()
        {
            return new ItemStack(AWItems.RESOURCE, 1, 0);
        }
    };
}
