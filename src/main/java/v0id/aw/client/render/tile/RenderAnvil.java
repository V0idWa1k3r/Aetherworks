package v0id.aw.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import v0id.aw.common.tile.TileAnvil;

/**
 * Created by V0idWa1k3r on 03-Jun-17.
 */
public class RenderAnvil extends TileEntitySpecialRenderer<TileAnvil>
{
    @Override
    public void render(TileAnvil te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        IItemHandler inv = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (inv != null && !inv.getStackInSlot(0).isEmpty())
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x + 0.5, y + 0.15, z + 0.5);
            GlStateManager.rotate(90, 1, 0 ,0);
            Minecraft.getMinecraft().getRenderItem().renderItem(inv.getStackInSlot(0), ItemCameraTransforms.TransformType.FIXED);
            GlStateManager.popMatrix();
        }

        if (te.hasRecipe.isPresent())
        {
            TextFormatting formatting = te.hitTimeout > 0 ? TextFormatting.DARK_GREEN : TextFormatting.RED;
            this.drawNameplate(te, formatting + "!", x, y - 0.5, z, 8);
        }
    }
}
