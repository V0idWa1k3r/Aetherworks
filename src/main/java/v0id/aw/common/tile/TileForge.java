package v0id.aw.common.tile;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.embers.power.EmberCapabilityProvider;
import teamroots.embers.power.IEmberCapability;
import teamroots.embers.tileentity.TileEntityTank;
import v0id.aw.AetherWorks;
import v0id.aw.common.block.AWBlocks;
import v0id.aw.common.block.forge.Component;
import v0id.aw.common.handler.CommonHandler;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by V0idWa1k3r on 02-Jun-17.
 */
public class TileForge extends TileEntity implements ISyncable, IForge, ITickable
{
    private IEmberCapability capability = new SyncableEmberCapacity(this);
    private IHeatCapability heatCapability = new IHeatCapability.DefaultHeatCapability(3000){
        @Override
        public void setHeat(float f)
        {
            boolean eq = this.getHeatStored() == f;
            super.setHeat(f);
            if (!eq && !TileForge.this.isRemote())
            {
                TileForge.this.sync();
            }
        }
    };

    private Multimap<Component.Type, IForgePart> parts = HashMultimap.create();
    private List<IFluidHandler> fluidHandlers = Lists.newArrayList();
    private boolean isStructureValid;
    private float storedHeat;
    private int ticksInDanger = 0;
    public int lightValueIndex = 0;

    public static final int[][] KERNEL_TOP = {
            {1, 0},
            {1, 1},
            {1, -1},
            {0, 1},
            {0, -1},
            {-1, 1},
            {-1, 0},
            {-1, -1}
    };

    public static final int[][] KERNEL_SIDE = {
            {2, 0},
            {2, 1},
            {2, -1},
            {-2, 1},
            {-2, 0},
            {-2, -1},
            {1, 2},
            {0, 2},
            {-1, 2},
            {1, -2},
            {0, -2},
            {-1, -2}
    };

    private int ticksExisted;

    public TileForge()
    {
        super();
        this.capability.setEmberCapacity(5000);
        this.capability.setEmber(0);
    }

    @Override
    public void update()
    {
        if (this.ticksExisted == 0)
        {
            this.checkStructureAndPopulate();
        }

        if (++ticksExisted >= 60)
        {
            this.checkStructureAndPopulate();
        }

        int prevIndex = lightValueIndex;
        lightValueIndex = (this.ticksExisted / 60) % 16;
        if (prevIndex != lightValueIndex)
        {
            this.world.notifyNeighborsOfStateChange(getPos(), this.getBlockType(), true);
        }

        if (this.isStructureValid)
        {
            for (IForgePart iForgePart : this.parts.values())
            {
                if (((TileEntity)iForgePart).isInvalid())
                {
                    continue;
                }

                iForgePart.onForgeTick(this);
            }
        }

        if (this.storedHeat > 0 && !this.world.isRemote)
        {
            float added = Math.max(0.1F, this.storedHeat * 0.35F);
            added *= Math.max(0.01F, this.getHeatCapability().getHeatStored() / this.heatCapability.getHeatCapacity());
            this.heatCapability.setHeat(this.heatCapability.getHeatStored() + added);
            this.storedHeat -= added;
        }

        if (this.storedHeat <= 0 && !this.world.isRemote)
        {
            float removed = Math.max(0.1F, this.getHeatCapability().getHeatStored() / this.heatCapability.getHeatCapacity());
            if (this.getHeatCapability().getHeatStored() > removed)
            {
                this.heatCapability.setHeat(this.heatCapability.getHeatStored() - removed);
            }
        }

        this.syncTick();
        if (this.isRemote())
        {
            this.spawnParticles();
        }

        if (!this.world.isRemote)
        {
            if (this.getHeatCapability().getHeatStored() >= this.getHeatCapability().getHeatCapacity())
            {
                if (++ticksInDanger >= 60)
                {
                    for (int[] ints : CommonHandler.KERNEL)
                    {
                        this.world.setBlockToAir(this.getPos().add(ints[0], -1, ints[1]));
                    }

                    this.world.newExplosion(null, this.getPos().getX() + 0.5, this.getPos().getY() + 0.5, this.getPos().getZ() + 0.5, 10, true, true);
                }
            }
            else
            {
                this.ticksInDanger = Math.max(0, --ticksInDanger);
            }
        }
    }
    
    private void spawnParticles()
    {
        if (AetherWorks.proxy.getClientPlayer().getDistance(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ()) > 32)
        {
            return;
        }

        float current = heatCapability.getHeatStored();
        float max = heatCapability.getHeatCapacity();
        for (int i = 0; i <= current / max * 10; i++)
        {
            AetherWorks.proxy.spawnParticleGlow(this.getWorld(), this.getPos().getX() + 0.4F + this.getWorld().rand.nextFloat() * 0.2F, this.getPos().getY() + 0.2F + this.getWorld().rand.nextFloat() * 0.2F, this.getPos().getZ() + 0.4F + this.getWorld().rand.nextFloat() * 0.2F, 0F, this.getWorld().rand.nextFloat() * 0.02F, 0F, 255, 64, 16, 2F + this.getWorld().rand.nextFloat(), 40);
        }

        if (current >= 2100)
        {
            AetherWorks.proxy.spawnParticleGlow(this.getWorld(), this.getPos().getX() - 1 + this.getWorld().rand.nextFloat() * 3, this.getPos().getY(), this.getPos().getZ() - 1F + this.getWorld().rand.nextFloat() * 3, 0F, this.getWorld().rand.nextFloat() * 0.02F, 0F, 255, 64, 16, 2F + this.getWorld().rand.nextFloat(), 40);
        }

        if (current >= 2900)
        {
            this.getWorld().spawnParticle(EnumParticleTypes.FLAME, this.getPos().getX() - 1 + this.getWorld().rand.nextFloat() * 3, this.getPos().getY(), this.getPos().getZ() - 1F + this.getWorld().rand.nextFloat() * 3, 0F, this.getWorld().rand.nextFloat() * 0.02F, 0F);
        }

        if (current >= 2800)
        {
            this.getWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.getPos().getX() - 1 + this.getWorld().rand.nextFloat() * 3, this.getPos().getY(), this.getPos().getZ() - 1F + this.getWorld().rand.nextFloat() * 3, 0F, this.getWorld().rand.nextFloat() * 0.02F, 0F);
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

    private void checkStructureAndPopulate()
    {
        BlockPos pos = this.getPos();
        for (int[] ints : CommonHandler.KERNEL)
        {
            BlockPos offset = this.getPos().add(ints[0], -1, ints[1]);
            Block b = this.getWorld().getBlockState(offset).getBlock();
            if (!AWBlocks.FORGE_STRUCTURE.isAssociatedBlock(b))
            {
                this.isStructureValid = false;
                return;
            }
        }

        this.isStructureValid = true;
        this.fluidHandlers.clear();
        for (int[] ints : KERNEL_TOP)
        {
            BlockPos offset = this.getPos().add(ints[0], 0, ints[1]);
            TileEntity tile = this.getWorld().getTileEntity(offset);
            if (tile instanceof IForgePart)
            {
                Component.Type t = ((IForgePart)tile).getComponentType();
                if (t.isTopPart())
                {
                    this.parts.put(t, (IForgePart) tile);
                }
            }
        }

        for (int[] ints : KERNEL_SIDE)
        {
            BlockPos offset = this.getPos().add(ints[0], -1, ints[1]);
            TileEntity tile = this.getWorld().getTileEntity(offset);
            if (tile instanceof IForgePart)
            {
                Component.Type t = ((IForgePart)tile).getComponentType();
                if (!t.isTopPart())
                {
                    this.parts.put(t, (IForgePart) tile);
                }
            }
            else
            {
                if (tile instanceof TileEntityTank && tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null))
                {
                    this.fluidHandlers.add(tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null));
                }
            }
        }
    }

    @Override
    public IHeatCapability getHeatCapability()
    {
        return this.heatCapability;
    }

    @Override
    public Multimap<Component.Type, IForgePart> getParts()
    {
        return this.parts;
    }

    @Override
    public void transferHeat(float value, boolean immediate)
    {
        if (!immediate)
        {
            this.storedHeat += value;
        }
        else
        {
            this.heatCapability.setHeat(this.heatCapability.getHeatStored() + value);
        }
    }

    @Override
    public boolean canFunction()
    {
        return this.isStructureValid;
    }

    @Override
    public IEmberCapability getEmberCapability()
    {
        return this.capability;
    }

    @Override
    public List<IFluidHandler> getAttachedFluidHandlers()
    {
        return this.fluidHandlers;
    }

    @Override
    public TileEntity getOwner()
    {
        return this;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == EmberCapabilityProvider.emberCapability || capability == IHeatCapability.Holder.cap || super.hasCapability(capability, facing);

    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == IHeatCapability.Holder.cap)
        {
            return IHeatCapability.Holder.cap.cast(this.heatCapability);
        }

        if (capability == EmberCapabilityProvider.emberCapability)
        {
            return EmberCapabilityProvider.emberCapability.cast(this.capability);
        }

        return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        this.capability.readFromNBT(tag);
        this.heatCapability.readFromNBT(tag);
        this.isStructureValid = tag.getBoolean("isValid");
        this.storedHeat = tag.getFloat("storedHeat");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        tag = super.writeToNBT(tag);
        this.capability.writeToNBT(tag);
        this.heatCapability.writeToNBT(tag);
        tag.setBoolean("isValid", this.isStructureValid);
        tag.setFloat("storedHeat", this.storedHeat);
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
    public boolean hasFastRenderer()
    {
        return true;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public AxisAlignedBB getRenderBoundingBox()
    {
        return super.getRenderBoundingBox().expand(1, 1, 1);
    }


}
