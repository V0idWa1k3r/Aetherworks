package v0id.aw.common.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import v0id.aw.common.block.forge.Component;

/**
 * Created by V0idWa1k3r on 02-Jun-17.
 */
public class ItemBlockForgeComponent extends ItemBlock
{
    public ItemBlockForgeComponent(Block block)
    {
        super(block);
        this.setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + '.' + Component.Type.values()[Math.min(stack.getMetadata(), Component.Type.values().length - 1)].getName();
    }

    @Override
    public int getMetadata(int damage)
    {
        return damage;
    }
}
