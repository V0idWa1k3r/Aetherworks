package v0id.aw.net;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class AWPacketHarvestNode implements IMessage
{
    private int stateID;
    private BlockPos pos;

    public AWPacketHarvestNode(IBlockState state, BlockPos pos)
    {
        this.stateID = Block.getStateId(state);
        this.pos = pos;
    }

    public AWPacketHarvestNode(int stateID, BlockPos pos)
    {
        this.stateID = stateID;
        this.pos = pos;
    }

    public AWPacketHarvestNode()
    {
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.stateID = buf.readInt();
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.stateID);
        buf.writeInt(this.pos.getX());
        buf.writeInt(this.pos.getY());
        buf.writeInt(this.pos.getZ());
    }

    public int getStateID()
    {
        return stateID;
    }

    public BlockPos getPos()
    {
        return pos;
    }
}
