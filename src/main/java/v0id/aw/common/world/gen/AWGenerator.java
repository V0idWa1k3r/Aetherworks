package v0id.aw.common.world.gen;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;
import v0id.aw.common.block.AWBlocks;
import v0id.aw.common.config.AWCfg;
import v0id.aw.common.handler.CommonHandler;
import v0id.aw.lib.ILifecycleListener;

import java.util.Iterator;
import java.util.Random;

/**
 * Created by V0idWa1k3r on 31-May-17.
 */
public class AWGenerator implements IWorldGenerator, ILifecycleListener
{
    public static AWGenerator INSTANCE = null;

    public AWGenerator()
    {
        INSTANCE = this;
    }

    @Override
    public void preInit(FMLPreInitializationEvent evt)
    {
        GameRegistry.registerWorldGenerator(this, 20);
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {
        this.doAetherOrePass(world, chunkX, chunkZ, random);
    }

    private void doAetherOrePass(World w, int x, int z, Random rand)
    {
        AWCfg.GenSettings settings = AWCfg.worldGen.oreAether;
        if (ArrayUtils.contains(settings.blacklistDimensions, w.provider.getDimension()))
        {
            return;
        }

        Iterator<Pair<Integer, ChunkPos>> retrogenListIterator = CommonHandler.getRetrogenList().listIterator();
        while (retrogenListIterator.hasNext())
        {
            Pair<Integer, ChunkPos> next = retrogenListIterator.next();
            if (next.getLeft() == w.provider.getDimension() && next.getRight().x == x && next.getRight().z == z)
            {
                retrogenListIterator.remove();
                break;
            }
        }

        float chance = settings.triesPerChunk;
        if (chance >= 1 || w.rand.nextFloat() <= chance)
        {
            for (int i = 0; i < chance; i++)
            {
                BlockPos at = new BlockPos(x << 4, settings.minHeight + rand.nextInt(settings.maxHeight - settings.minHeight), z << 4);
                WorldGenMinable minableGen = new WorldGenMinable(AWBlocks.AETHER_ORE.getDefaultState(), settings.veinSize);
                minableGen.generate(w, rand, at);
            }
        }
    }
}
