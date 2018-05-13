package v0id.aw.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import v0id.aw.common.block.forge.Component;
import v0id.aw.common.block.forge.Structure;
import v0id.aw.common.block.prism.ControllerMatrix;
import v0id.aw.common.block.prism.MoonlightAmplifier;
import v0id.aw.common.block.prism.Prism;
import v0id.aw.common.block.prism.PrismSupport;
import v0id.aw.common.fluid.AWFluids;
import v0id.aw.common.item.ItemBlockForgeComponent;
import v0id.aw.lib.AWConsts;
import v0id.aw.lib.ILifecycleListener;

/**
 * Created by V0idWa1k3r on 31-May-17.
 */
@Mod.EventBusSubscriber(modid = AWConsts.modid)
@GameRegistry.ObjectHolder(value = AWConsts.modid)
public class AWBlocks implements ILifecycleListener
{
    @GameRegistry.ObjectHolder(value = AWConsts.blockAetherOreID)
    public static final Block AETHER_ORE = null;

    @GameRegistry.ObjectHolder(value = AWConsts.blockAetherPrismControllerMatrixID)
    public static final Block AETHER_PRISM_CONTROLLER_MATRIX = null;

    @GameRegistry.ObjectHolder(value = AWConsts.blockMoonlightAmplifierID)
    public static final Block MOONLIGHT_AMPLIFIER = null;

    @GameRegistry.ObjectHolder(value = AWConsts.blockPrismSupportID)
    public static final Block PRISM_SUPPORT = null;

    @GameRegistry.ObjectHolder(value = AWConsts.blockPrismID)
    public static final Block PRISM = null;

    @GameRegistry.ObjectHolder(value = AWConsts.blockImpureAetheriumGasID)
    public static final Block BLOCK_FLUID_IMPURE_AETHERIUM = null;

    @GameRegistry.ObjectHolder(value = AWConsts.blockAetheriumGasID)
    public static final Block BLOCK_FLUID_AETHERIUM = null;

    @GameRegistry.ObjectHolder(value = AWConsts.blockForgeID)
    public static final Block FORGE_STRUCTURE = null;

    @GameRegistry.ObjectHolder(value = AWConsts.blockAetherID)
    public static final Block BLOCK_AETHER = null;

    @GameRegistry.ObjectHolder(value = AWConsts.blockComponentID)
    public static final Block FORGE_COMPONENT = null;

    @GameRegistry.ObjectHolder(value = AWConsts.blockHeatDialID)
    public static final Block HEAT_DIAL = null;

    @Override
    public void preInit(FMLPreInitializationEvent evt)
    {
    }

    @SubscribeEvent
    public static void onBlockRegistry(RegistryEvent.Register<Block> event)
    {
        Block[] toRegister = new Block[]{
                new AetherOre(),
                new ControllerMatrix(),
                new MoonlightAmplifier(),
                new PrismSupport(),
                new Prism(),
                new BlockFluidClassic(AWFluids.FLUID_IMPURE_AETHERIUM_GAS, Material.LAVA).setRegistryName(AWConsts.blockImpureAetheriumGas).setUnlocalizedName("aw.impureAetheriumGas"),
                new BlockFluidClassic(AWFluids.FLUID_AETHERIUM_GAS, Material.LAVA).setRegistryName(AWConsts.blockAetheriumGas).setUnlocalizedName("aw.aetheriumGas"),
                new Structure(),
                new AetherBlock(),
                new Component(),
                new HeatDial()
        };

        event.getRegistry().registerAll(toRegister);
    }

    @SubscribeEvent
    public static void onItemRegistry(RegistryEvent.Register<Item> event)
    {
        Item[] toRegister = new Item[]{
                new ItemBlock(AETHER_ORE).setRegistryName(AWConsts.blockAetherOre),
                new ItemBlock(AETHER_PRISM_CONTROLLER_MATRIX).setRegistryName(AWConsts.blockAetherPrismControllerMatrix),
                new ItemBlock(MOONLIGHT_AMPLIFIER).setRegistryName(AWConsts.blockMoonlightAmplifier),
                new ItemBlock(PRISM_SUPPORT).setRegistryName(AWConsts.blockPrismSupport),
                new ItemBlock(PRISM).setRegistryName(AWConsts.blockPrism),
                new ItemBlock(BLOCK_FLUID_IMPURE_AETHERIUM).setRegistryName(AWConsts.blockImpureAetheriumGas),
                new ItemBlock(BLOCK_FLUID_AETHERIUM).setRegistryName(AWConsts.blockAetheriumGas),
                new ItemBlock(FORGE_STRUCTURE).setRegistryName(AWConsts.blockForge),
                new ItemBlock(BLOCK_AETHER).setRegistryName(AWConsts.blockAether),
                new ItemBlockForgeComponent(FORGE_COMPONENT).setRegistryName(AWConsts.blockComponent),
                new ItemBlock(HEAT_DIAL).setRegistryName(AWConsts.blockHeatDial),
        };

        event.getRegistry().registerAll(toRegister);
    }
}
