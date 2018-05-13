package v0id.aw.lib;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Created by V0idWa1k3r on 31-May-17.
 */
public interface IAWProxy extends ILifecycleListener
{
    EntityPlayer getClientPlayer();

    String translate(String key, Object... values);

    void spawnParticleGlow(World world, float x, float y, float z, float mx, float my, float mz, float r, float g, float b, float scale, int lifetime);

    void spawnParticleSpark(World world, float x, float y, float z, float mx, float my, float mz, float r, float g, float b, float scale, int lifetime);
}
