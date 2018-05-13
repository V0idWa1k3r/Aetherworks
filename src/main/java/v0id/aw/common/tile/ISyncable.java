package v0id.aw.common.tile;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

/**
 * Created by V0idWa1k3r on 02-Jun-17.
 */
public interface ISyncable
{
    default void forceSync()
    {
        if (this.isRemote())
        {
            return;
        }

        BlockPos at = this.getOwner().getPos();
        WorldServer ws = (WorldServer) this.getOwner().getWorld();
        SPacketUpdateTileEntity packet = this.getOwner().getUpdatePacket();
        ws.playerEntities.stream().filter(p -> p.getDistance(at.getX(), at.getY(), at.getZ()) <= this.getSyncRange()).map(p -> ((EntityPlayerMP)p)).forEach(p -> p.connection.sendPacket(packet));
    }

    default void sync()
    {
        this.setNeedsSync(true);
    }

    default float getSyncRange()
    {
        return 64;
    }

    TileEntity getOwner();

    default boolean isRemote()
    {
        return this.getOwner() == null || !this.getOwner().hasWorld() || this.getOwner().getWorld().isRemote;
    }

    boolean needsSync();

    void setNeedsSync(boolean b);

    default void syncTick()
    {
        if (this.isRemote() || !this.needsSync())
        {
            return;
        }

        BlockPos at = this.getOwner().getPos();
        WorldServer ws = (WorldServer) this.getOwner().getWorld();
        SPacketUpdateTileEntity packet = this.getOwner().getUpdatePacket();
        ws.playerEntities.stream().filter(p -> p.getDistance(at.getX(), at.getY(), at.getZ()) <= this.getSyncRange()).map(p -> ((EntityPlayerMP)p)).forEach(p -> p.connection.sendPacket(packet));
        this.setNeedsSync(false);
    }
}
