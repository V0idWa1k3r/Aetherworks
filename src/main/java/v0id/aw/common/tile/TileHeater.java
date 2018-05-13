package v0id.aw.common.tile;

import net.minecraft.block.material.Material;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import teamroots.embers.power.EmberCapabilityProvider;
import teamroots.embers.power.IEmberCapability;
import v0id.aw.common.block.forge.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by V0idWa1k3r on 02-Jun-17.
 */
public class TileHeater extends TileSyncableFluidHandler implements IForgePart
{
    private IEmberCapability capability = new SyncableEmberCapacity(this);

    public TileHeater()
    {
        super();
        this.capability.setEmberCapacity(1000);
        this.capability.setEmber(0);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        this.capability.readFromNBT(tag);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        tag = super.writeToNBT(tag);
        this.capability.writeToNBT(tag);
        return tag;
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(this.getPos(), 0, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        this.readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag)
    {
        this.readFromNBT(tag);
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == EmberCapabilityProvider.emberCapability || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
    {
        return capability == EmberCapabilityProvider.emberCapability ? EmberCapabilityProvider.emberCapability.cast(this.capability) : super.getCapability(capability, facing);
    }

    @Override
    public void onForgeTick(IForge forge)
    {
        this.syncTick();

        if (this.getWorld().isBlockIndirectlyGettingPowered(this.getPos()) > 0 || this.getWorld().isBlockPowered(this.getPos()))
        {
            if (this.getWorld().getBlockState(this.getPos().down()).getMaterial() == Material.LAVA)
            {
                FluidStack water = new FluidStack(FluidRegistry.WATER, 1);
                if (this.tank.drain(water, false) != null && this.capability.removeAmount(0.25, false) == 0.25)
                {
                    this.tank.drain(water, true);
                    this.capability.removeAmount(0.25, true);
                    forge.transferHeat(1);
                }
            }
        }
        else
        {
            return;
        }
    }

    private boolean isDirty;

    @Override
    public boolean needsSync()
    {
        return this.isDirty;
    }

    @Override
    public void setNeedsSync(boolean b)
    {
        this.isDirty = b;
    }

    @Override
    public Component.Type getComponentType()
    {
        return Component.Type.HEATER;
    }

    @Override
    public TileEntity getOwner()
    {
        return this;
    }
}
