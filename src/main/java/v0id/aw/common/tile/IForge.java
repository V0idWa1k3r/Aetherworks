package v0id.aw.common.tile;

import com.google.common.collect.Multimap;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.capability.IFluidHandler;
import teamroots.embers.power.IEmberCapability;
import v0id.aw.common.block.forge.Component;

import java.util.List;

/**
 * Created by V0idWa1k3r on 02-Jun-17.
 */
public interface IForge
{
    TileEntity getOwner();

    IHeatCapability getHeatCapability();

    Multimap<Component.Type, IForgePart> getParts();

    default void transferHeat(float value)
    {
        this.transferHeat(value, false);
    }

    void transferHeat(float value, boolean immediate);

    boolean canFunction();

    IEmberCapability getEmberCapability();

    List<IFluidHandler> getAttachedFluidHandlers();
}
