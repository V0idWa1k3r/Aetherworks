package v0id.aw.client.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import v0id.aw.client.AWClient;
import v0id.aw.common.item.AWShovel;
import v0id.aw.lib.AWConsts;
import v0id.aw.net.AWNetworkSwitchMode;

import java.util.Arrays;

/**
 * Created by V0idWa1k3r on 01-Jun-17.
 */
public class ClientHandler
{
    public static long ticks;

    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent.Pre evt)
    {
        final TextureMap map = evt.getMap();
        if (map.getBasePath().equals("textures"))
        {
            AWClient.cachedModSprites.put(AWClient.LOCATION_PRISM_OVERLAY, map.registerSprite(AWClient.LOCATION_PRISM_OVERLAY));
            Arrays.stream(AWClient.LOCATION_RUNES).forEach(loc -> AWClient.cachedModSprites.put(loc, map.registerSprite(loc)));
            AWClient.cachedModSprites.put(AWClient.LOCATION_FORGE_OVERLAY, map.registerSprite(AWClient.LOCATION_FORGE_OVERLAY));
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START)
        {
            ++ticks;

            if (ticks % 20 == 0)
            {
                AWNetworkSwitchMode.packetsPerSecond = 0;
            }
        }
    }

    @SubscribeEvent
    public void onMouseEvent(MouseEvent event)
    {
        if (event.getDwheel() != 0 && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
        {
            if (Minecraft.getMinecraft().player != null)
            {
                ItemStack mainhand = Minecraft.getMinecraft().player.getHeldItemMainhand();
                if (!mainhand.isEmpty() && mainhand.getItem() instanceof AWShovel && ((AWShovel)mainhand.getItem()).getType() == AWShovel.Type.REDSTONE)
                {
                    AWShovel.Mode toSet = AWShovel.getMode(mainhand).getOpposite();
                    AWNetworkSwitchMode.sendRequestToServer(Minecraft.getMinecraft().player, toSet);
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public void onHUDRender(RenderGameOverlayEvent.Post event)
    {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL)
        {
            ItemStack stack = Minecraft.getMinecraft().player.getHeldItemMainhand();
            if (!stack.isEmpty() && stack.getItem() instanceof AWShovel)
            {
                ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
                double x = res.getScaledWidth() / 2 + 100;
                double y = res.getScaledHeight() - 20;
                BufferBuilder buffer = Tessellator.getInstance().getBuffer();
                Minecraft.getMinecraft().renderEngine.bindTexture(AWConsts.shovelUI);
                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
                buffer.pos(x + 20, y, 0).tex(0.5, 0).color(1, 1, 1, 1F).endVertex();
                buffer.pos(x, y, 0).tex(0, 0).color(1, 1, 1, 1F).endVertex();
                buffer.pos(x, y + 20, 0).tex(0, 0.5).color(1, 1, 1, 1F).endVertex();
                buffer.pos(x + 20, y + 20, 0).tex(0.5, 0.5).color(1, 1, 1, 1F).endVertex();
                if (((AWShovel)stack.getItem()).getType() == AWShovel.Type.REDSTONE)
                {
                    float progress = 0;
                    buffer.pos(x + 20, y - 20, 0).tex(1, 0).color(1, 1, 1, 1F).endVertex();
                    buffer.pos(x, y - 20, 0).tex(0.5, 0).color(1, 1, 1, 1F).endVertex();
                    buffer.pos(x, y, 0).tex(0.5, 0.5).color(1, 1, 1, 1F).endVertex();
                    buffer.pos(x + 20, y, 0).tex(1, 0.5).color(1, 1, 1, 1F).endVertex();

                    if (AWShovel.getMode(stack) == AWShovel.Mode.EXTEND)
                    {
                        progress = (float) Math.abs(Math.sin(Math.toRadians(((ticks * 10) % 360) + event.getPartialTicks())));
                        buffer.pos(x + 15, y - 20 - progress, 0).tex(0.5, 0.5).color(1, 1, 1, 1F).endVertex();
                        buffer.pos(x + 5, y - 20 - progress, 0).tex(0, 0.5).color(1, 1, 1, 1F).endVertex();
                        buffer.pos(x + 5, y - 10 - progress, 0).tex(0, 1).color(1, 1, 1, 1F).endVertex();
                        buffer.pos(x + 15, y - 10 - progress, 0).tex(0.5, 1).color(1, 1, 1, 1F).endVertex();
                        buffer.pos(x + 15, y - 10 + progress, 0).tex(0.5, 1).color(1, 1, 1, 1F).endVertex();
                        buffer.pos(x + 5, y - 10 + progress, 0).tex(0, 1).color(1, 1, 1, 1F).endVertex();
                        buffer.pos(x + 5, y + progress, 0).tex(0, 0.5).color(1, 1, 1, 1F).endVertex();
                        buffer.pos(x + 15, y + progress, 0).tex(0.5, 0.5).color(1, 1, 1, 1F).endVertex();
                        buffer.pos(x + 20 + progress, y - 15, 0).tex(0, 0.5).color(1, 1, 1, 1F).endVertex();
                        buffer.pos(x + 10 + progress, y - 15, 0).tex(0, 1).color(1, 1, 1, 1F).endVertex();
                        buffer.pos(x + 10 + progress, y - 5, 0).tex(0.5, 1).color(1, 1, 1, 1F).endVertex();
                        buffer.pos(x + 20 + progress, y - 5, 0).tex(0.5, 0.5).color(1, 1, 1, 1F).endVertex();
                        buffer.pos(x + 10 - progress, y - 15, 0).tex(0, 1).color(1, 1, 1, 1F).endVertex();
                        buffer.pos(x - progress, y - 15, 0).tex(0, 0.5).color(1, 1, 1, 1F).endVertex();
                        buffer.pos(x - progress, y - 5, 0).tex(0.5, 0.5).color(1, 1, 1, 1F).endVertex();
                        buffer.pos(x + 10 - progress, y - 5, 0).tex(0.5, 1).color(1, 1, 1, 1F).endVertex();
                    }
                    else
                    {
                        buffer.pos(x + 20, y - 20, 0).tex(1, 0.5).color(1, 1, 1, 1F).endVertex();
                        buffer.pos(x, y - 20, 0).tex(0.5, 0.5).color(1, 1, 1, 1F).endVertex();
                        buffer.pos(x, y, 0).tex(0.5, 1).color(1, 1, 1, 1F).endVertex();
                        buffer.pos(x + 20, y, 0).tex(1, 1).color(1, 1, 1, 1F).endVertex();
                    }
                }

                Tessellator.getInstance().draw();

                ItemStack is = AWShovel.getFocusedStack(stack);
                if (!is.isEmpty())
                {
                    RenderHelper.disableStandardItemLighting();
                    RenderHelper.enableGUIStandardItemLighting();
                    Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(Minecraft.getMinecraft().player, is, (int)x + 2, (int) y + 2);
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    GlStateManager.disableLighting();
                    GlStateManager.enableAlpha();
                    GlStateManager.enableBlend();
                }
            }
        }
    }
}
