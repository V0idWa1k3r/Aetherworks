package v0id.aw.client.render.tile;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;

/**
 * Created by V0idWa1k3r on 02-Jun-17.
 */
public class FastTESRUtils
{
    public static void renderFaceNorth(BufferBuilder buffer, Vec3d at, TextureAtlasSprite sprite, float[] color, int[] lightmap)
    {
        double x = at.x;
        double y = at.y;
        double z = at.z;
        double minU = sprite.getMinU();
        double maxU = sprite.getMaxU();
        double minV = sprite.getMaxV();
        double maxV = sprite.getMinV();
        buffer.pos(x + 1, y, z - 0.001).color(color[0], color[1], color[2], color[3]).tex(minU, minV).lightmap(lightmap[0], lightmap[1]).endVertex();
        buffer.pos(x, y, z - 0.001).color(color[0], color[1], color[2], color[3]).tex(maxU, minV).lightmap(lightmap[0], lightmap[1]).endVertex();
        buffer.pos(x, y + 1, z - 0.001).color(color[0], color[1], color[2], color[3]).tex(maxU, maxV).lightmap(lightmap[0], lightmap[1]).endVertex();
        buffer.pos(x + 1, y + 1, z - 0.001).color(color[0], color[1], color[2], color[3]).tex(minU, maxV).lightmap(lightmap[0], lightmap[1]).endVertex();
    }

    public static void renderFaceSouth(BufferBuilder buffer, Vec3d at, TextureAtlasSprite sprite, float[] color, int[] lightmap)
    {
        double x = at.x;
        double y = at.y;
        double z = at.z;
        double minU = sprite.getMinU();
        double maxU = sprite.getMaxU();
        double minV = sprite.getMaxV();
        double maxV = sprite.getMinV();
        buffer.pos(x, y, z + 1.001).color(color[0], color[1], color[2], color[3]).tex(minU, minV).lightmap(lightmap[0], lightmap[1]).endVertex();
        buffer.pos(x + 1, y, z + 1.001).color(color[0], color[1], color[2], color[3]).tex(maxU, minV).lightmap(lightmap[0], lightmap[1]).endVertex();
        buffer.pos(x + 1, y + 1, z + 1.001).color(color[0], color[1], color[2], color[3]).tex(maxU, maxV).lightmap(lightmap[0], lightmap[1]).endVertex();
        buffer.pos(x, y + 1, z + 1.001).color(color[0], color[1], color[2], color[3]).tex(minU, maxV).lightmap(lightmap[0], lightmap[1]).endVertex();
    }

    public static void renderFaceWest(BufferBuilder buffer, Vec3d at, TextureAtlasSprite sprite, float[] color, int[] lightmap)
    {
        double x = at.x;
        double y = at.y;
        double z = at.z;
        double minU = sprite.getMinU();
        double maxU = sprite.getMaxU();
        double minV = sprite.getMaxV();
        double maxV = sprite.getMinV();
        buffer.pos(x - 0.001, y, z + 1).color(color[0], color[1], color[2], color[3]).tex(minU, minV).lightmap(lightmap[0], lightmap[1]).endVertex();
        buffer.pos(x - 0.001, y, z).color(color[0], color[1], color[2], color[3]).tex(maxU, minV).lightmap(lightmap[0], lightmap[1]).endVertex();
        buffer.pos(x - 0.001, y + 1, z).color(color[0], color[1], color[2], color[3]).tex(maxU, maxV).lightmap(lightmap[0], lightmap[1]).endVertex();
        buffer.pos(x - 0.001, y + 1, z + 1).color(color[0], color[1], color[2], color[3]).tex(minU, maxV).lightmap(lightmap[0], lightmap[1]).endVertex();
    }

    public static void renderFaceEast(BufferBuilder buffer, Vec3d at, TextureAtlasSprite sprite, float[] color, int[] lightmap)
    {
        double x = at.x;
        double y = at.y;
        double z = at.z;
        double minU = sprite.getMinU();
        double maxU = sprite.getMaxU();
        double minV = sprite.getMaxV();
        double maxV = sprite.getMinV();
        buffer.pos(x + 1.001, y, z).color(color[0], color[1], color[2], color[3]).tex(minU, minV).lightmap(lightmap[0], lightmap[1]).endVertex();
        buffer.pos(x + 1.001, y, z + 1).color(color[0], color[1], color[2], color[3]).tex(maxU, minV).lightmap(lightmap[0], lightmap[1]).endVertex();
        buffer.pos(x + 1.001, y + 1, z + 1).color(color[0], color[1], color[2], color[3]).tex(maxU, maxV).lightmap(lightmap[0], lightmap[1]).endVertex();
        buffer.pos(x + 1.001, y + 1, z).color(color[0], color[1], color[2], color[3]).tex(minU, maxV).lightmap(lightmap[0], lightmap[1]).endVertex();
    }

    public static void renderFace(BufferBuilder buffer, Vec3d at, TextureAtlasSprite sprite, float[] color, int[] lightmap, EnumFacing facing)
    {
        switch (facing)
        {
            case NORTH:
            {
                renderFaceNorth(buffer, at, sprite, color, lightmap);
                break;
            }

            case SOUTH:
            {
                renderFaceSouth(buffer, at, sprite, color, lightmap);
                break;
            }

            case WEST:
            {
                renderFaceWest(buffer, at, sprite, color, lightmap);
                break;
            }

            case EAST:
            {
                renderFaceEast(buffer, at, sprite, color, lightmap);
                break;
            }

            default:
            {
                break;
            }
        }
    }

    public static void renderHorizontals(BufferBuilder buffer, Vec3d at, TextureAtlasSprite sprite, float[] color, int[] lightmap)
    {
        renderFaceNorth(buffer, at, sprite, color, lightmap);
        renderFaceSouth(buffer, at, sprite, color, lightmap);
        renderFaceEast(buffer, at, sprite, color, lightmap);
        renderFaceWest(buffer, at, sprite, color, lightmap);
    }
}
