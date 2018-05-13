package v0id.aw.lib;

import net.minecraft.item.ItemStack;

/**
 * Created by V0idWa1k3r on 03-Jun-17.
 */
public class RecipeUtils
{
    public static boolean areItemStacksEqual(ItemStack l, ItemStack r)
    {
        return ItemStack.areItemsEqual(l, r) && ItemStack.areItemStackTagsEqual(l, r) && l.getMetadata() == r.getMetadata();
    }
}
