package v0id.aw.common.tile;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.TileFluidHandler;

/**
 * Created by V0idWa1k3r on 03-Jun-17.
 */
public abstract class TileSyncableFluidHandler extends TileFluidHandler implements ISyncable
{
    public TileSyncableFluidHandler()
    {
        super();
        this.tank = new FluidTank(Fluid.BUCKET_VOLUME){

            protected void onContentsChanged()
            {
                if (!TileSyncableFluidHandler.this.isRemote())
                {
                    TileSyncableFluidHandler.this.sync();
                }
            }
        };
    }
}
