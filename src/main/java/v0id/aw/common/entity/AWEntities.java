package v0id.aw.common.entity;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import v0id.aw.AetherWorks;
import v0id.aw.lib.AWConsts;
import v0id.aw.lib.ILifecycleListener;

public class AWEntities implements ILifecycleListener
{
    @Override
    public void preInit(FMLPreInitializationEvent evt)
    {
        EntityRegistry.registerModEntity(AWConsts.entityAWArrow, AWArrow.class, AWConsts.entityAWArrow.toString(), 0, AetherWorks.getInstance(), 64, 1, true);
        EntityRegistry.registerModEntity(AWConsts.entityAWMagma, AWMagma.class, AWConsts.entityAWMagma.toString(), 1, AetherWorks.getInstance(), 16, 1, false);
    }
}
