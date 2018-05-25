package v0id.aw;

import com.google.common.collect.Lists;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import v0id.aw.common.block.AWBlocks;
import v0id.aw.common.entity.AWEntities;
import v0id.aw.common.fluid.AWFluids;
import v0id.aw.common.handler.CommonHandler;
import v0id.aw.common.item.AWItems;
import v0id.aw.common.recipe.AWRecipes;
import v0id.aw.common.tile.AWTiles;
import v0id.aw.common.world.gen.AWGenerator;
import v0id.aw.lib.AWConsts;
import v0id.aw.lib.IAWProxy;
import v0id.aw.lib.ILifecycleListener;
import v0id.aw.net.AWNetworkSwitchMode;

import java.util.List;

/**
 * Created by V0idWa1k3r on 31-May-17.
 */
@Mod(modid = AWConsts.modid, dependencies = AWConsts.deps, useMetadata = true)
public class AetherWorks
{
    private static List<ILifecycleListener> listenerList = Lists.newArrayList();
    private static AetherWorks instance;

    @SidedProxy(clientSide = AWConsts.clientProxy, serverSide = AWConsts.serverProxy)
    public static IAWProxy proxy;

    static
    {
        listenerList.add(new AWFluids());
        listenerList.add(new AWBlocks());
        listenerList.add(new AWItems());
        listenerList.add(new AWTiles());
        listenerList.add(new AWGenerator());
        listenerList.add(new AWRecipes());
        listenerList.add(new AWNetworkSwitchMode());
        listenerList.add(new AWEntities());
        FluidRegistry.enableUniversalBucket();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent evt)
    {
        instance = this;
        MinecraftForge.EVENT_BUS.register(new CommonHandler());
        listenerList.add(proxy);
        listenerList.forEach(l -> l.preInit(evt));
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent evt)
    {
        listenerList.forEach(l -> l.init(evt));
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent evt)
    {
        listenerList.forEach(l -> l.postInit(evt));
    }

    public IAWProxy getProxy()
    {
        return proxy;
    }

    public static AetherWorks getInstance()
    {
        return instance;
    }
}
