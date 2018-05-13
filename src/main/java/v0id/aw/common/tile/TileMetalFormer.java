package v0id.aw.common.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import v0id.aw.AetherWorks;
import v0id.aw.common.block.forge.Component;
import v0id.aw.common.recipe.MetalFormerRecipes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

/**
 * Created by V0idWa1k3r on 02-Jun-17.
 */
public class TileMetalFormer extends TileSyncableFluidHandler implements IForgePart
{
    private ItemStackHandler inventory = new ItemStackHandler(1){
        protected void onContentsChanged(int slot)
        {
            if (!TileMetalFormer.this.world.isRemote)
            {
                TileMetalFormer.this.sync();
            }
        }

        @Override
        public int getSlotLimit(int slot)
        {
            return 1;
        }
    };

    public int progress;

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        this.inventory.deserializeNBT(tag.getCompoundTag("capInventory"));
        this.progress = tag.getInteger("progress");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        tag = super.writeToNBT(tag);
        tag.setTag("capInventory", this.inventory.serializeNBT());
        tag.setInteger("progress", this.progress);
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
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.inventory) : super.getCapability(capability, facing);
    }

    @Override
    public void onForgeTick(IForge forge)
    {
        if (!this.world.isRemote)
        {
            FluidStack fs = this.tank.getFluid();
            if (fs == null || fs.amount < this.tank.getCapacity())
            {
                for (IFluidHandler iFluidHandler : forge.getAttachedFluidHandlers())
                {
                    FluidStack tryDrain = fs == null ? null : new FluidStack(fs.getFluid(), this.tank.getCapacity() - this.tank.getFluidAmount()) ;
                    FluidStack drainAttempt = tryDrain == null ? iFluidHandler.drain(this.tank.getCapacity(), false) : iFluidHandler.drain(tryDrain, false);
                    if (drainAttempt != null)
                    {
                        this.tank.fill(iFluidHandler.drain(drainAttempt, true), true);
                        if (this.tank.getFluidAmount() >= this.tank.getCapacity())
                        {
                            break;
                        }
                    }
                }
            }
        }

        if (this.tank.getFluid() != null && !this.inventory.getStackInSlot(0).isEmpty())
        {
            Optional<MetalFormerRecipes.MetalFormerRecipe> recipe = MetalFormerRecipes.findMatchingRecipe(this.inventory.getStackInSlot(0), this.tank.getFluid(), (int) forge.getHeatCapability().getHeatStored());
            if (recipe.isPresent())
            {
                if (!this.world.isRemote)
                {
                    if (forge.getEmberCapability().removeAmount(1,true) > 0)
                    {
                        forge.getHeatCapability().setHeat(forge.getHeatCapability().getHeatStored() - 0.5F );
                        ++this.progress;
                    }

                    if (this.progress >= 400)
                    {
                        this.progress = 0;
                        this.inventory.setStackInSlot(0, recipe.get().getRecipeOutput());
                        this.tank.drain(recipe.get().getFluidRequirements(), true);
                    }
                }
                else
                {
                    int color = this.tank.getFluid().getFluid().getColor(this.tank.getFluid());
                    int r = (color & 0xFF0000) >> 16;
                    int g = (color & 0xFF00) >> 8;
                    int b = color & 0xFF;
                    Random rand = this.getWorld().rand;
                    AetherWorks.proxy.spawnParticleGlow(this.getWorld(), this.getPos().getX() + rand.nextFloat(), this.getPos().getY() + 0.1F, this.getPos().getZ() + rand.nextFloat(), 0, 0.01F, 0, r, g, b, 1,30);
                }
            }
            else
            {
                this.progress = 0;
            }
        }
        else
        {
            this.progress = 0;
        }

        this.syncTick();
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
        return Component.Type.METAL_FORMER;
    }

    @Override
    public TileEntity getOwner()
    {
        return this;
    }
}
