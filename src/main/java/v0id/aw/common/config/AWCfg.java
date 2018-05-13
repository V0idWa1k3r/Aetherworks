package v0id.aw.common.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import v0id.aw.lib.AWConsts;

/**
 * Created by V0idWa1k3r on 31-May-17.
 */
@Config(modid = AWConsts.modid)
public class AWCfg
{
    @Config.Comment("Configure AetherWorks's worldgen here")
    public static final Generation worldGen = new Generation();

    public static class Generation
    {
        @Config.Comment("Aether Ore generation settings")
        public final GenSettings oreAether = new GenSettings(4, 80, 128, 4,-1, 1);
    }

    public static class GenSettings
    {
        @Config.Comment("The amount of times the ore will try to generate in each chunk. Set to less than 1 to turn this into a chance to generate type of value")
        public float triesPerChunk;

        @Config.Comment("Minimum Y coordinate for this ore")
        public int minHeight;

        @Config.Comment("Maximum Y coordinate for this ore")
        public int maxHeight;

        @Config.Comment("The maximum size of the vein")
        public int veinSize;

        @Config.Comment("The list of dimension IDs this ore is NOT allowed to generate in")
        public int[] blacklistDimensions;

        GenSettings(float triesPerChunk, int minHeight, int maxHeight, int veinSize, int... blacklistDimensions)
        {
            this.triesPerChunk = triesPerChunk;
            this.minHeight = minHeight;
            this.maxHeight = maxHeight;
            this.veinSize = veinSize;
            this.blacklistDimensions = blacklistDimensions;
        }
    }

    @Mod.EventBusSubscriber(modid = AWConsts.modid)
    private static class Handler
    {
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
        {
            if (event.getModID().equals(AWConsts.modid))
            {
                ConfigManager.sync(AWConsts.modid, Config.Type.INSTANCE);
            }
        }
    }
}
