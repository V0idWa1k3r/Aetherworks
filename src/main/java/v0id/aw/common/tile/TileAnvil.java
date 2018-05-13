package v0id.aw.common.tile;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import v0id.aw.AetherWorks;
import v0id.aw.common.block.forge.Component;
import v0id.aw.common.recipe.AARecipes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

/**
 * Created by V0idWa1k3r on 02-Jun-17.
 */
public class TileAnvil extends TileEntity implements IForgePart, ISyncable
{
    private ItemStackHandler inventory = new ItemStackHandler(1){
        protected void onContentsChanged(int slot)
        {
            if (!TileAnvil.this.world.isRemote)
            {
                TileAnvil.this.sync();
            }
        }
    };

    public int progressPrev = -1;
    public int hitTimeout;
    public int progress;
    public float heatFluctuationsMemory;
    public int mistakes;
    public Optional<AARecipes.AARecipe> hasRecipe = Optional.empty();

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        this.inventory.deserializeNBT(tag.getCompoundTag("capInventory"));
        this.hitTimeout = tag.getInteger("hitTimeout");
        this.progress = tag.getInteger("progress");
        this.heatFluctuationsMemory = tag.getFloat("heatFluctuationsMemory");
        this.mistakes = tag.getInteger("mistakes");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        tag = super.writeToNBT(tag);
        tag.setTag("capInventory", this.inventory.serializeNBT());
        tag.setInteger("hitTimeout", this.hitTimeout);
        tag.setInteger("progress", this.progress);
        tag.setFloat("heatFluctuationsMemory", this.heatFluctuationsMemory);
        tag.setInteger("mistakes", this.mistakes);
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
        if (pkt.getNbtCompound().getInteger("hitTimeout") != this.hitTimeout && this.hitTimeout == 0 && this.hasWorld())
        {
            this.getWorld().playSound(AetherWorks.proxy.getClientPlayer(), this.getPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1, 1);
        }

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
        global: if (!this.inventory.getStackInSlot(0).isEmpty())
        {
            Optional<AARecipes.AARecipe> recipe = AARecipes.findMatchingRecipe(this.inventory.getStackInSlot(0), (int) forge.getHeatCapability().getHeatStored());
            this.hasRecipe = recipe;
            if (recipe.isPresent())
            {
                if (this.getWorld().isRemote)
                {
                    Random rand = this.getWorld().rand;
                    AetherWorks.proxy.spawnParticleGlow(this.getWorld(), this.getPos().getX() + rand.nextFloat(), this.getPos().getY() + 0.1F, this.getPos().getZ() + rand.nextFloat(), 0, 0.01F, 0, 0, 0.72F, 0.95F, 1, 30);
                }

                if (this.progress == recipe.get().getHitsRequired() && !this.world.isRemote)
                {
                    this.inventory.setStackInSlot(0, recipe.get().getOutput(this.world));
                    this.hitTimeout = 0;
                    this.mistakes = 0;
                    this.heatFluctuationsMemory = 0;
                    this.progress = 0;
                    this.world.playSound(null, this.getPos(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1, 0.1F);
                    break global;
                }

                if (this.hitTimeout > 0)
                {
                    if (--this.hitTimeout == 0)
                    {
                        if (!this.world.isRemote)
                        {
                            this.mkMistake();
                        }

                        break global;
                    }
                }

                if (!this.world.isRemote)
                {
                    if (this.progressPrev != this.progress && this.progressPrev != -1)
                    {
                        if (forge.getEmberCapability().removeAmount(recipe.get().getEmbersPerHit(), true) != recipe.get().getEmbersPerHit())
                        {
                            this.mkMistake();
                        }
                    }

                    if (this.heatFluctuationsMemory != 0)
                    {
                        forge.getHeatCapability().setHeat(forge.getHeatCapability().getHeatStored() + this.heatFluctuationsMemory);
                        this.heatFluctuationsMemory = 0;
                    }

                    if (this.hitTimeout == 0)
                    {
                        if (this.world.rand.nextFloat() < 0.01F + (float)recipe.get().getDifficulty() / 100)
                        {
                            this.hitTimeout = Math.max(10, 100 - recipe.get().getDifficulty() * 10);
                            this.sync();
                        }
                    }
                }
            }
            else
            {
                this.hitTimeout = 0;
                this.mistakes = 0;
                this.heatFluctuationsMemory = 0;
                this.progress = 0;
            }
        }

        this.syncTick();
        this.progressPrev = progress;
    }

    private void mkMistake()
    {
        this.world.playSound(null, this.getPos(), SoundEvents.BLOCK_ANVIL_DESTROY, SoundCategory.PLAYERS, 1, 1);
        if (++this.mistakes == 3)
        {
            this.inventory.setStackInSlot(0, ItemStack.EMPTY);
            this.hitTimeout = 0;
            this.mistakes = 0;
            this.heatFluctuationsMemory = 0;
            this.progress = 0;
        }

        this.sync();
    }

    public void activate()
    {
        if (this.hasRecipe.isPresent())
        {
            if (this.hitTimeout > 0)
            {
                if (!this.world.isRemote)
                {
                    if (this.progress < this.hasRecipe.get().getHitsRequired() - 1)
                    {
                        this.world.playSound(null, this.getPos(), SoundEvents.BLOCK_ANVIL_USE, SoundCategory.PLAYERS, 1, 1);
                    }

                    ++this.progress;
                    this.hitTimeout = 0;
                    this.heatFluctuationsMemory += this.hasRecipe.get().getTemperatureFluctuation() * (this.world.rand.nextBoolean() ? -1 : 1);
                    this.sync();
                }
            }
            else
            {
                this.mkMistake();
            }
        }
    }

    @Override
    public Component.Type getComponentType()
    {
        return Component.Type.ANVIL;
    }

    @Override
    public TileEntity getOwner()
    {
        return this;
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
}
