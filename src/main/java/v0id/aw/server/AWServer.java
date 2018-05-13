package v0id.aw.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import v0id.aw.lib.IAWProxy;

/**
 * Created by V0idWa1k3r on 31-May-17.
 */
public class AWServer implements IAWProxy
{
    @Override
    public EntityPlayer getClientPlayer()
    {
        return null;
    }

    @Override
    public String translate(String key, Object... values)
    {
        return "";
    }

    @Override
    public void spawnParticleGlow(World world, float x, float y, float z, float mx, float my, float mz, float r, float g, float b, float scale, int lifetime)
    {

    }

    @Override
    public void spawnParticleSpark(World world, float x, float y, float z, float mx, float my, float mz, float r, float g, float b, float scale, int lifetime)
    {

    }
}
