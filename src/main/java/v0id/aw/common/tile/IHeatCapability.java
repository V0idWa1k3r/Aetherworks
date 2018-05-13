package v0id.aw.common.tile;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.INBTSerializable;
import v0id.aw.lib.AWConsts;

import javax.annotation.Nullable;

/**
 * Created by V0idWa1k3r on 02-Jun-17.
 */
public interface IHeatCapability extends INBTSerializable<NBTTagCompound>
{
    default void readFromNBT(NBTTagCompound tag)
    {
        this.deserializeNBT(tag.getCompoundTag("aw.heatCap"));
    }

    default void writeToNBT(NBTTagCompound tag)
    {
        tag.setTag("aw.heatCap", this.serializeNBT());
    }

    float getHeatStored();

    float getHeatCapacity();

    void setHeat(float f);

    void setHeatCapacity(float f);

    class DefaultHeatCapability implements IHeatCapability
    {
        private float heat;
        private float heatCapacity;

        public DefaultHeatCapability()
        {

        }

        public DefaultHeatCapability(float capacity)
        {
            this();
            this.setHeatCapacity(capacity);
        }

        @Override
        public float getHeatStored()
        {
            return this.heat;
        }

        @Override
        public float getHeatCapacity()
        {
            return this.heatCapacity;
        }

        @Override
        public void setHeat(float f)
        {
            this.heat = f;
        }

        @Override
        public void setHeatCapacity(float f)
        {
            this.heatCapacity = f;
        }

        @Override
        public NBTTagCompound serializeNBT()
        {
            NBTTagCompound ret = new NBTTagCompound();
            ret.setFloat("heat", this.heat);
            ret.setFloat("heatCapacity", this.heatCapacity);
            return ret;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt)
        {
            this.heat = nbt.getFloat("heat");
            this.heatCapacity = nbt.getFloat("heatCapacity");
        }
    }

    class Holder
    {
        @CapabilityInject(IHeatCapability.class)
        public static Capability<IHeatCapability> cap = null;
        public static final ResourceLocation KEY = new ResourceLocation(AWConsts.modid, "heatCap");

        public static void register()
        {
            CapabilityManager.INSTANCE.register(IHeatCapability.class, new Capability.IStorage<IHeatCapability>()
            {
                @Nullable
                @Override
                public NBTBase writeNBT(Capability<IHeatCapability> capability, IHeatCapability instance, EnumFacing side)
                {
                    return instance.serializeNBT();
                }

                @Override
                public void readNBT(Capability<IHeatCapability> capability, IHeatCapability instance, EnumFacing side, NBTBase nbt)
                {
                    instance.readFromNBT((NBTTagCompound) nbt);
                }
            },
                    DefaultHeatCapability::new);
        }
    }
}
