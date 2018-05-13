package v0id.aw.common.tile;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import v0id.aw.lib.ILifecycleListener;

/**
 * Created by V0idWa1k3r on 01-Jun-17.
 */
public class AWTiles implements ILifecycleListener
{
    @Override
    public void preInit(FMLPreInitializationEvent evt)
    {
        IHeatCapability.Holder.register();
        GameRegistry.registerTileEntity(TilePrism.class, "aetherworks:tile_prism");
        GameRegistry.registerTileEntity(TileHeater.class, "aetherworks:tile_heater");
        GameRegistry.registerTileEntity(TileCooler.class, "aetherworks:tile_cooler");
        GameRegistry.registerTileEntity(TileHeatVent.class, "aetherworks:tile_heat_vent");
        GameRegistry.registerTileEntity(TileMetalFormer.class, "aetherworks:tile_metal_former");
        GameRegistry.registerTileEntity(TileAnvil.class, "aetherworks:tile_anvil");
        GameRegistry.registerTileEntity(TileForge.class, "aetherworks:tile_forge");
    }
}
