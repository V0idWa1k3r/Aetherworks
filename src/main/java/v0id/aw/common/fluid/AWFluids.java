package v0id.aw.common.fluid;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import v0id.aw.common.block.AWBlocks;
import v0id.aw.lib.AWConsts;
import v0id.aw.lib.ILifecycleListener;

/**
 * Created by V0idWa1k3r on 01-Jun-17.
 */
public class AWFluids implements ILifecycleListener
{
    public static final Fluid FLUID_IMPURE_AETHERIUM_GAS = new Fluid(AWConsts.fluidImpureAetherium, AWConsts.fluidAetheriumGasLoc, AWConsts.fluidAetheriumGasLoc){
        public int getColor()
        {
            return 0xff6c829f;
        }

    }.setGaseous(true).setDensity(-1000).setLuminosity(15).setTemperature(1020).setUnlocalizedName("aw.impureAetheriumGas");

    public static final Fluid FLUID_AETHERIUM_GAS = new Fluid(AWConsts.fluidAetherium, AWConsts.fluidAetheriumGasLoc, AWConsts.fluidAetheriumGasLoc){
        public int getColor()
        {
            return 0xff00b8ff;
        }

    }.setGaseous(true).setDensity(-1000).setLuminosity(15).setTemperature(1020).setUnlocalizedName("aw.aetheriumGas");

    static
    {
        FluidRegistry.registerFluid(FLUID_IMPURE_AETHERIUM_GAS);
        FluidRegistry.registerFluid(FLUID_AETHERIUM_GAS);
    }

    @Override
    public void preInit(FMLPreInitializationEvent evt)
    {
        FluidRegistry.addBucketForFluid(FLUID_IMPURE_AETHERIUM_GAS.setBlock(AWBlocks.BLOCK_FLUID_IMPURE_AETHERIUM));
        FluidRegistry.addBucketForFluid(FLUID_AETHERIUM_GAS.setBlock(AWBlocks.BLOCK_FLUID_AETHERIUM));
    }
}
