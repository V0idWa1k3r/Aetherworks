package v0id.aw.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.lwjgl.opengl.GL11;
import v0id.aw.common.tile.TileMetalFormer;

/**
 * Created by V0idWa1k3r on 03-Jun-17.
 */
public class RenderMetalFormer extends TileEntitySpecialRenderer<TileMetalFormer>
{

    @Override
    public void render(TileMetalFormer te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        IFluidHandler handlerCap = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
        if (handlerCap != null)
        {
            if (handlerCap.getTankProperties() != null && handlerCap.getTankProperties().length > 0)
            {
                IFluidTankProperties props = handlerCap.getTankProperties()[0];
                FluidStack stack = props.getContents();
                if (stack != null)
                {
                    ResourceLocation icon = stack.getFluid().getStill(stack);
                    TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(icon.toString());
                    if (sprite != Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite())
                    {
                        GlStateManager.pushAttrib();
                        GlStateManager.disableLighting();
                        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
                        double ly = y + 0.07 + 0.1 * ((double)stack.amount / props.getCapacity());
                        double minU = sprite.getMinU();
                        double minV = sprite.getMinV();
                        double maxU = sprite.getMaxU();
                        double maxV = sprite.getMaxV();
                        int fColor = stack.getFluid().getColor(stack);
                        float a = (float)((fColor & 0xFF000000) >> 24) / 255F;
                        float r = (float)((fColor & 0xFF0000) >> 16) / 255F;
                        float g = (float)((fColor & 0xFF00) >> 8) / 255F;
                        float b = (float)(fColor & 0xFF) / 255F;
                        buffer.pos(x + 0.9, ly, z + 0.1).color(r, g, b, a).tex(minU, minV).lightmap(240, 240).endVertex();
                        buffer.pos(x + 0.1, ly, z + 0.1).color(r, g, b, a).tex(maxU, minV).lightmap(240, 240).endVertex();
                        buffer.pos(x + 0.1, ly, z + 0.9).color(r, g, b, a).tex(maxU, maxV).lightmap(240, 240).endVertex();
                        buffer.pos(x + 0.9, ly, z + 0.9).color(r, g, b, a).tex(minU, maxV).lightmap(240, 240).endVertex();
                        Tessellator.getInstance().draw();
                        GlStateManager.popAttrib();
                    }
                }
            }
        }

        IItemHandler inv = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (inv != null && !inv.getStackInSlot(0).isEmpty())
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x + 0.5, y + 0.15, z + 0.5);
            GlStateManager.rotate(90, 1, 0 ,0);
            Minecraft.getMinecraft().getRenderItem().renderItem(inv.getStackInSlot(0), ItemCameraTransforms.TransformType.FIXED);
            GlStateManager.popMatrix();
        }
    }
}
