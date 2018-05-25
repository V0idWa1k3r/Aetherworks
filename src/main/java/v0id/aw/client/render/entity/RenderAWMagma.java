package v0id.aw.client.render.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import v0id.aw.common.entity.AWMagma;

import javax.annotation.Nullable;

public class RenderAWMagma extends Render<AWMagma>
{
    public RenderAWMagma(RenderManager renderManager)
    {
        super(renderManager);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(AWMagma entity)
    {
        return null;
    }
}
