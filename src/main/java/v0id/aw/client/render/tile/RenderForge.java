package v0id.aw.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.animation.FastTESR;
import v0id.aw.client.AWClient;
import v0id.aw.common.tile.TileForge;

/**
 * Created by V0idWa1k3r on 02-Jun-17.
 */
public class RenderForge extends FastTESR<TileForge>
{
    @Override
    public void renderTileEntityFast(TileForge te, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer)
    {
        if (!te.canFunction())
        {
            return;
        }

        TextureAtlasSprite overlay = AWClient.cachedModSprites.get(AWClient.LOCATION_FORGE_OVERLAY);
        float sin = 0.5F + (float) Math.abs(Math.sin(Math.toRadians((float) Minecraft.getMinecraft().player.ticksExisted * 5 + partialTicks))) * 0.5F;
        float[] color = {1, sin, sin, 1};
        FastTESRUtils.renderFaceWest(buffer, new Vec3d(x + 2.01, y - 1, z), overlay, color, RenderPrism.LIGHTMAP);
        FastTESRUtils.renderFaceWest(buffer, new Vec3d(x + 2.01, y - 1, z - 1), overlay, color, RenderPrism.LIGHTMAP);
        FastTESRUtils.renderFaceWest(buffer, new Vec3d(x + 2.01, y - 1, z + 1), overlay, color, RenderPrism.LIGHTMAP);
        FastTESRUtils.renderFaceEast(buffer, new Vec3d(x - 2.01, y - 1, z), overlay, color, RenderPrism.LIGHTMAP);
        FastTESRUtils.renderFaceEast(buffer, new Vec3d(x - 2.01, y - 1, z - 1), overlay, color, RenderPrism.LIGHTMAP);
        FastTESRUtils.renderFaceEast(buffer, new Vec3d(x - 2.01, y - 1, z + 1), overlay, color, RenderPrism.LIGHTMAP);
        FastTESRUtils.renderFaceNorth(buffer, new Vec3d(x, y - 1, z + 2.01), overlay, color, RenderPrism.LIGHTMAP);
        FastTESRUtils.renderFaceNorth(buffer, new Vec3d(x - 1, y - 1, z + 2.01), overlay, color, RenderPrism.LIGHTMAP);
        FastTESRUtils.renderFaceNorth(buffer, new Vec3d(x + 1, y - 1, z + 2.01), overlay, color, RenderPrism.LIGHTMAP);
        FastTESRUtils.renderFaceSouth(buffer, new Vec3d(x, y - 1, z - 2.01), overlay, color, RenderPrism.LIGHTMAP);
        FastTESRUtils.renderFaceSouth(buffer, new Vec3d(x - 1, y - 1, z - 2.01), overlay, color, RenderPrism.LIGHTMAP);
        FastTESRUtils.renderFaceSouth(buffer, new Vec3d(x + 1, y - 1, z - 2.01), overlay, color, RenderPrism.LIGHTMAP);
    }
}
