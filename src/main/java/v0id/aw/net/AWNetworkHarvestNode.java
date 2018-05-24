package v0id.aw.net;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import v0id.aw.AetherWorks;

public class AWNetworkHarvestNode implements IMessageHandler<AWPacketHarvestNode, IMessage>
{
    public static void sendNodeToClient(NetworkRegistry.TargetPoint point, IBlockState state, BlockPos pos)
    {
        AWNetworkSwitchMode.wrapper.sendToAllAround(new AWPacketHarvestNode(state, pos), point);
    }

    @Override
    public IMessage onMessage(AWPacketHarvestNode message, MessageContext ctx)
    {
        if (ctx.side == Side.CLIENT)
        {
            AetherWorks.proxy.getContextListener(0).addScheduledTask(() ->
            {
                for (int i = 0; i < 10; ++i)
                {
                    World world = AetherWorks.proxy.getContextWorld(0);
                    AetherWorks.proxy.spawnParticleGlow(world, message.getPos().getX() + world.rand.nextFloat(), message.getPos().getY() + world.rand.nextFloat(), message.getPos().getZ() + world.rand.nextFloat(), (world.rand.nextFloat() - world.rand.nextFloat()) / 100, (world.rand.nextFloat() - world.rand.nextFloat()) / 100, (world.rand.nextFloat() - world.rand.nextFloat()) / 100, 0, 0.72F, 0.95F, 1, 100);
                    world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, message.getPos().getX() + world.rand.nextFloat(), message.getPos().getY() + world.rand.nextFloat(), message.getPos().getZ() + world.rand.nextFloat(), 0, 0, 0, message.getStateID());
                }
            });
        }

        return null;
    }
}
