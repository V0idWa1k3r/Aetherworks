package v0id.aw.common.tile;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
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
public class TileCooler extends TileSyncableFluidHandler implements IForgePart
{
    private IEmberCapability capability = new SyncableEmberCapacity(this);
    private int cooldown = 2400;

    public TileCooler()
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
        this.cooldown = tag.getInteger("cooldown");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        tag = super.writeToNBT(tag);
        this.capability.writeToNBT(tag);
        tag.setInteger("cooldown", this.cooldown);
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
        if (!this.world.isRemote)
        {
            --this.cooldown;
            if (this.getWorld().isBlockIndirectlyGettingPowered(this.getPos()) > 0 || this.getWorld().isBlockPowered(this.getPos()))
            {
                if (this.cooldown <= 0)
                {
                    Block b = this.world.getBlockState(pos.down()).getBlock();
                    FluidStack drainedStack = new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME);
                    FluidStack tryDrain = this.tank.drain(drainedStack, false);
                    if (tryDrain != null && tryDrain.amount == drainedStack.amount && this.capability.removeAmount(1000, false) == 1000)
                    {
                        if (b.isAssociatedBlock(Blocks.PACKED_ICE) || b.isAssociatedBlock(Blocks.ICE) || b.isAssociatedBlock(Blocks.SNOW))
                        {
                            this.world.setBlockToAir(pos.down());
                            this.tank.drain(drainedStack, true);
                            this.capability.removeAmount(1000,true);
                            this.cooldown = 2400;
                            this.world.playSound(null, this.getPos(), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 1, 0.1F);
                            forge.transferHeat(-500, true);
                            if (forge.getHeatCapability().getHeatStored() < 0)
                            {
                                forge.getHeatCapability().setHeat(0);
                            }
                        }
                    }
                }
            }
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
        return Component.Type.COOLER;
    }

    @Override
    public TileEntity getOwner()
    {
        return this;
    }
}
