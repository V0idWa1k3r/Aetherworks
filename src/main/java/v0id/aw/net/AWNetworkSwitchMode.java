package v0id.aw.net;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import v0id.aw.common.item.AWShovel;
import v0id.aw.lib.ILifecycleListener;

/**
 * Created by V0idWa1k3r on 05-Jun-17.
 */
public class AWNetworkSwitchMode implements IMessageHandler<AWPacketSwitchMode, IMessage>, ILifecycleListener
{
    public static SimpleNetworkWrapper wrapper;
    public static int packetsPerSecond;

    @Override
    public void preInit(FMLPreInitializationEvent evt)
    {
        wrapper = NetworkRegistry.INSTANCE.newSimpleChannel("aetherworks");
        wrapper.registerMessage(this, AWPacketSwitchMode.class, 0, Side.SERVER);
    }

    public static void sendRequestToServer(EntityPlayer requester, AWShovel.Mode requested)
    {
        if (++packetsPerSecond > 10)
        {
            System.err.println("Detected potential packet spam, aborting.");
            return;
        }

        wrapper.sendToServer(new AWPacketSwitchMode(requester, requested));
    }

    @Override
    public IMessage onMessage(final AWPacketSwitchMode message, MessageContext ctx)
    {
        if (ctx.side == Side.SERVER)
        {
            MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
            final EntityPlayerMP playerMP = server.getPlayerList().getPlayerByUUID(message.playerID);
            server.addScheduledTask(() ->
            {
                ItemStack is = playerMP.getHeldItemMainhand();
                if (!is.isEmpty() && is.getItem() instanceof AWShovel && ((AWShovel) is.getItem()).getType() == AWShovel.Type.REDSTONE)
                {
                    AWShovel.setMode(is, message.toSet);
                }
            });
        }

        return null;
    }
}
