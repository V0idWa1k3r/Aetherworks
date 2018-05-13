package v0id.aw.client.render.tile;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.client.model.animation.FastTESR;
import v0id.aw.client.AWClient;
import v0id.aw.common.tile.TilePrism;

import java.util.Random;

/**
 * Created by V0idWa1k3r on 01-Jun-17.
 */
public class RenderPrism extends FastTESR<TilePrism>
{
    public static final Random RANDOM = new Random();
    public static final float[] COLOR = {1, 1, 1, 1};
    public static final int[] LIGHTMAP = {240, 240};

    @Override
    public void renderTileEntityFast(TilePrism te, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer)
    {
        TextureAtlasSprite overlay = AWClient.cachedModSprites.get(AWClient.LOCATION_PRISM_OVERLAY);
        if (te.isStructureValid())
        {
            FastTESRUtils.renderHorizontals(buffer, new Vec3d(x + 3, y - 1, z), overlay, COLOR, LIGHTMAP);
            FastTESRUtils.renderHorizontals(buffer, new Vec3d(x - 3, y - 1, z), overlay, COLOR, LIGHTMAP);
            FastTESRUtils.renderHorizontals(buffer, new Vec3d(x, y - 1, z + 3), overlay, COLOR, LIGHTMAP);
            FastTESRUtils.renderHorizontals(buffer, new Vec3d(x, y - 1, z - 3), overlay, COLOR, LIGHTMAP);
            FastTESRUtils.renderHorizontals(buffer, new Vec3d(x + 2, y - 2, z + 2), overlay, COLOR, LIGHTMAP);
            FastTESRUtils.renderHorizontals(buffer, new Vec3d(x + 2, y - 2, z - 2), overlay, COLOR, LIGHTMAP);
            FastTESRUtils.renderHorizontals(buffer, new Vec3d(x - 2, y - 2, z + 2), overlay, COLOR, LIGHTMAP);
            FastTESRUtils.renderHorizontals(buffer, new Vec3d(x - 2, y - 2, z - 2), overlay, COLOR, LIGHTMAP);
            this.renderRune(te, buffer, new Vec3d(x + 3, y - 2, z), new Vec3i(3, -2, 0));
            this.renderRune(te, buffer, new Vec3d(x - 3, y - 2, z), new Vec3i(-3, -2, 0));
            this.renderRune(te, buffer, new Vec3d(x, y - 2, z + 3), new Vec3i(0, -2, 3));
            this.renderRune(te, buffer, new Vec3d(x, y - 2, z - 3), new Vec3i(0, -2, -3));
            this.renderRune(te, buffer, new Vec3d(x + 2, y - 3, z + 2), new Vec3i(2, -3, 2));
            this.renderRune(te, buffer, new Vec3d(x + 2, y - 3, z - 2), new Vec3i(2, -3, -2));
            this.renderRune(te, buffer, new Vec3d(x - 2, y - 3, z + 2), new Vec3i(-2, -3, 2));
            this.renderRune(te, buffer, new Vec3d(x - 2, y - 3, z - 2), new Vec3i(-2, -3, -2));
        }
    }

    private void renderRune(TilePrism tile, BufferBuilder buffer, Vec3d at, Vec3i offset)
    {
        BlockPos posAt = tile.getPos().add(offset);
        long seed = MathHelper.getPositionRandom(posAt);
        RANDOM.setSeed(seed);
        FastTESRUtils.renderHorizontals(buffer, at, AWClient.cachedModSprites.get(AWClient.LOCATION_RUNES[RANDOM.nextInt(AWClient.LOCATION_RUNES.length)]), COLOR, LIGHTMAP);
    }
}
