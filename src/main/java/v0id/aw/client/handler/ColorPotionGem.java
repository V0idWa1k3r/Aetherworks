package v0id.aw.client.handler;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import v0id.aw.common.item.PotionGem;

public class ColorPotionGem implements IItemColor
{
    @Override
    public int colorMultiplier(ItemStack stack, int tintIndex)
    {
        return ((PotionGem)stack.getItem()).getColor(stack, tintIndex);
    }
}
