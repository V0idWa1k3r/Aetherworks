package v0id.aw.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import v0id.aw.lib.AWConsts;
import v0id.aw.lib.ILifecycleListener;

/**
 * Created by V0idWa1k3r on 31-May-17.
 */
@Mod.EventBusSubscriber(modid = AWConsts.modid)
@GameRegistry.ObjectHolder(value = AWConsts.modid)
public class AWItems implements ILifecycleListener
{
    @GameRegistry.ObjectHolder(value = AWConsts.itemResourceID)
    public static final Item RESOURCE = null;

    @GameRegistry.ObjectHolder(value = AWConsts.itemPickaxeAetherID)
    public static final Item PICKAXE_AETHERIUM = null;

    @GameRegistry.ObjectHolder(value = AWConsts.itemPickaxeEmberID)
    public static final Item PICKAXE_EMBER = null;

    @GameRegistry.ObjectHolder(value = AWConsts.itemGeodeID)
    public static final Item GEODE = null;

    @GameRegistry.ObjectHolder(value = AWConsts.itemAxePrismarineID)
    public static final Item AXE_PRISMARINE = null;

    @GameRegistry.ObjectHolder(value = AWConsts.itemAxeEnderID)
    public static final Item AXE_ENDER = null;

    @GameRegistry.ObjectHolder(value = AWConsts.itemShovelRedstoneID)
    public static final Item SHOVEL_REDSTONE = null;

    @GameRegistry.ObjectHolder(value = AWConsts.itemShovelSlimeID)
    public static final Item SHOVEL_SLIME = null;

    @GameRegistry.ObjectHolder(value = AWConsts.itemPotionGemID)
    public static final Item POTION_GEM = null;

    @GameRegistry.ObjectHolder(value = AWConsts.itemCrownID)
    public static final Item CROWN = null;

    @Override
    public void preInit(FMLPreInitializationEvent evt)
    {
        AWConsts.AETHERIUM.setRepairItem(new ItemStack(RESOURCE, 1, 4));
    }

    @SubscribeEvent
    public static void onItemRegistry(RegistryEvent.Register<Item> event)
    {
        Item[] toRegister = new Item[]{
                new AWResource(),
                new AWPickaxe(AWPickaxe.Type.AETHER),
                new AWPickaxe(AWPickaxe.Type.EMBER),
                new Geode(),
                new AWAxe(AWAxe.Type.PRISMARINE),
                new AWAxe(AWAxe.Type.ENDER),
                new AWShovel(AWShovel.Type.REDSTONE),
                new AWShovel(AWShovel.Type.SLIME),
                new PotionGem(),
                new AWCrown()
        };

        event.getRegistry().registerAll(toRegister);
    }
}
