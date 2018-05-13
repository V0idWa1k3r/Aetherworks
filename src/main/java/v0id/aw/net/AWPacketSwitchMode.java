package v0id.aw.net;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import v0id.aw.common.item.AWShovel;

import java.util.UUID;

/**
 * Created by V0idWa1k3r on 05-Jun-17.
 */
public class AWPacketSwitchMode implements IMessage
{
    public AWShovel.Mode toSet;
    public UUID playerID;

    // FML
    public AWPacketSwitchMode()
    {

    }

    public AWPacketSwitchMode(UUID uuid, AWShovel.Mode mode)
    {
        this.toSet = mode;
        this.playerID = uuid;
    }

    public AWPacketSwitchMode(EntityPlayer player, AWShovel.Mode mode)
    {
        this(player.getPersistentID(), mode);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.toSet = buf.readByte() == 0 ? AWShovel.Mode.EXTRUDE : AWShovel.Mode.EXTEND;
        this.playerID = new UUID(buf.getLong(1), buf.getLong(9));
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeByte(this.toSet == AWShovel.Mode.EXTRUDE ? 0 : 1);
        buf.writeLong(this.playerID.getMostSignificantBits());
        buf.writeLong(this.playerID.getLeastSignificantBits());
    }
}
