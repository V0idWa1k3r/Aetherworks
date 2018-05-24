package v0id.aw.lib;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import v0id.aw.net.AWNetworkHarvestNode;

import java.util.*;
import java.util.function.Predicate;

/**
 * Created by V0idWa1k3r on 04-Jun-17.
 */
@Mod.EventBusSubscriber(modid = AWConsts.modid)
public class AWHarvestHelper
{
    private static final Map<UUID, AWHarvestNode> nodes = Maps.newHashMap();
    public static final Random RAND = new Random();

    public static ItemStack getValidItemStackFor(IBlockState state)
    {
        Block b = state.getBlock();
        Item i = Item.getItemFromBlock(b);
        if (i instanceof ItemBlock)
        {
            return new ItemStack(i, 1, b.damageDropped(state));
        }
        else
        {
            return ItemStack.EMPTY;
        }
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event)
    {
        if (event.phase != TickEvent.Phase.END)
        {
            return;
        }

        Stack<UUID> toRemove = new Stack<>();
        for (Map.Entry<UUID, AWHarvestNode> uuidawHarvestNodeEntry : nodes.entrySet())
        {
            uuidawHarvestNodeEntry.getValue().tick();
            if (uuidawHarvestNodeEntry.getValue().isInvalid())
            {
                toRemove.add(uuidawHarvestNodeEntry.getKey());
            }
        }

        while (!toRemove.isEmpty())
        {
            nodes.remove(toRemove.pop());
        }
    }

    @SubscribeEvent
    public static void onWorldUnload(WorldEvent.Unload event)
    {
        Stack<UUID> toRemove = new Stack<>();
        for (Map.Entry<UUID, AWHarvestNode> uuidawHarvestNodeEntry : nodes.entrySet())
        {
            if (uuidawHarvestNodeEntry.getValue().harvestedIn == event.getWorld())
            {
                toRemove.add(uuidawHarvestNodeEntry.getKey());
            }
        }

        while (!toRemove.isEmpty())
        {
            nodes.remove(toRemove.pop());
        }
    }

    @SubscribeEvent
    public static void onPlayerDisconnect(PlayerEvent.PlayerLoggedOutEvent event)
    {
        Stack<UUID> toRemove = new Stack<>();
        for (Map.Entry<UUID, AWHarvestNode> uuidawHarvestNodeEntry : nodes.entrySet())
        {
            if (uuidawHarvestNodeEntry.getValue().harvester == event.player)
            {
                toRemove.add(uuidawHarvestNodeEntry.getKey());
            }
        }

        while (!toRemove.isEmpty())
        {
            nodes.remove(toRemove.pop());
        }
    }

    public static boolean addNode(EntityPlayer invoker, AWExchangeNode node)
    {
        if (!(invoker instanceof EntityPlayerMP))
        {
            return false;
        }

        UUID playerID = invoker.getPersistentID();
        if (nodes.containsKey(playerID))
        {
            return false;
        }

        node.initNode();
        if (node.isInvalid())
        {
            return false;
        }

        nodes.put(playerID, node);
        return true;
    }

    public static boolean addNode(EntityPlayer invoker, BlockPos at, int range, Predicate<EntityPlayer> predicate)
    {
        if (!(invoker instanceof EntityPlayerMP))
        {
            return false;
        }

        UUID playerID = invoker.getPersistentID();
        if (nodes.containsKey(playerID))
        {
            return false;
        }

        AWHarvestNode node = new AWHarvestNode(invoker, invoker.world, at, range, predicate);
        node.initNode();
        if (node.isInvalid())
        {
            return false;
        }

        nodes.put(playerID, node);
        return true;
    }

    public static void harvestBlock(World w, EntityPlayerMP player, BlockPos pos, IBlockState state, ItemStack stack)
    {
        int exp = net.minecraftforge.common.ForgeHooks.onBlockBreakEvent(w, GameType.SURVIVAL, player, pos);
        if (exp == -1)
        {
            return;
        }

        w.playEvent(player, 2001, pos, Block.getStateId(state));
        boolean flag = state.getBlock().canHarvestBlock(w, pos, player);
        boolean flag1 = removeBlock(pos, flag, player, w);
        if (flag1 && flag)
        {
            state.getBlock().harvestBlock(w, player, pos, state, w.getTileEntity(pos), stack);
        }
    }

    public static boolean removeBlock(BlockPos pos, boolean canHarvest, EntityPlayer player, World w)
    {
        IBlockState iblockstate = w.getBlockState(pos);
        boolean flag = iblockstate.getBlock().removedByPlayer(iblockstate, w, pos, player, canHarvest);

        if (flag)
        {
            iblockstate.getBlock().onBlockDestroyedByPlayer(w, pos, iblockstate);
        }

        return flag;
    }

    public static class AWExchangeNode extends AWHarvestNode
    {
        public final ItemStack stackConsumed;

        public AWExchangeNode(EntityPlayer harvester, World harvestedIn, BlockPos beginning, int range, Predicate<EntityPlayer> canHarvest, ItemStack is)
        {
            super(harvester, harvestedIn, beginning, range, canHarvest);
            this.stackConsumed = is;
        }

        public void initNode()
        {
            if (!isLoaded(this.beginning))
            {
                this.invalid = true;
                return;
            }

            this.baseState = this.harvestedIn.getBlockState(this.beginning);
            this.traverseRecursive(this.beginning);
            this.toHarvest.sort((BlockPos l, BlockPos r) -> (int) (this.harvester.getDistanceSq(r) - this.harvester.getDistanceSq(l)));
            if (this.toHarvest.isEmpty())
            {
                this.invalid = true;
            }
        }

        private IBlockState baseState;
        private int currentIteration;
        private final EnumFacing[] faces = Arrays.copyOf(EnumFacing.values(), EnumFacing.values().length);

        private void shuffleFacesArray()
        {
            int index;
            EnumFacing temp;
            Random random = new Random();
            for (int i = faces.length - 1; i > 0; i--)
            {
                index = random.nextInt(i + 1);
                temp = faces[index];
                faces[index] = faces[i];
                faces[i] = temp;
            }
        }

        private void traverseRecursive(BlockPos from)
        {
            if (currentIteration >= this.range)
            {
                return;
            }

            this.shuffleFacesArray();
            for (EnumFacing facing : faces)
            {
                BlockPos offset = from.offset(facing);
                if (this.toHarvest.contains(offset))
                {
                    continue;
                }

                if (!this.isLoaded(offset))
                {
                    continue;
                }

                IBlockState state = this.harvestedIn.getBlockState(offset);
                if (state.equals(this.baseState))
                {
                    this.toHarvest.add(0, offset);
                    ++currentIteration;
                    this.traverseRecursive(offset);
                }
            }
        }

        public void tick()
        {
            BlockPos pos = this.toHarvest.pop();
            if (this.isLoaded(pos))
            {
                IBlockState state = this.harvestedIn.getBlockState(pos);
                if (state.equals(this.baseState))
                {
                    if (this.canHarvest.test(this.harvester))
                    {
                        int index = this.harvester.inventory.getSlotFor(this.stackConsumed);
                        if (index == -1)
                        {
                            this.invalid = true;
                        }
                        else
                        {
                            ItemStack is = this.harvester.inventory.getStackInSlot(index);
                            if (is.getItem() instanceof ItemBlock)
                            {
                                IBlockState currentState = this.harvestedIn.getBlockState(pos);
                                List<ItemStack> stacks = currentState.getBlock().getDrops(this.harvestedIn, pos, currentState, 0);
                                IBlockState toSet;
                                try
                                {
                                    toSet = ((ItemBlock) is.getItem()).getBlock().getStateForPlacement(this.harvestedIn, pos, EnumFacing.getDirectionFromEntityLiving(pos, this.harvester), 0, 0, 0, is.getMetadata(), this.harvester, EnumHand.MAIN_HAND);
                                }
                                catch (Exception e)
                                {
                                    toSet = ((ItemBlock) is.getItem()).getBlock().getStateFromMeta(is.getMetadata());
                                }

                                SoundType type = currentState.getBlock().getSoundType(state, this.harvestedIn, pos, this.harvester);
                                this.harvestedIn.playSound(null, pos, type.getBreakSound(), SoundCategory.BLOCKS, type.getVolume(), type.getPitch());
                                this.harvestedIn.setBlockState(pos, toSet);
                                SoundType type1 = toSet.getBlock().getSoundType(state, this.harvestedIn, pos, this.harvester);
                                this.harvestedIn.playSound(null, pos, type1.getBreakSound(), SoundCategory.BLOCKS, type1.getVolume(), type1.getPitch());
                                for (ItemStack stack : stacks)
                                {
                                    if (!this.harvester.inventory.addItemStackToInventory(stack))
                                    {
                                        this.harvester.dropItem(stack, true, false);
                                    }
                                }

                                is.shrink(1);
                                this.harvester.getHeldItemMainhand().damageItem(1, this.harvester);
                            }
                            else
                            {
                                this.invalid = true;
                            }
                        }
                    }
                    else
                    {
                        this.invalid = true;
                    }
                }
            }

            if (this.toHarvest.isEmpty())
            {
                this.invalid = true;
            }
        }
    }

    public static class AWHarvestNode
    {
        public final EntityPlayer harvester;
        public final World harvestedIn;
        public final BlockPos beginning;
        public final Predicate<EntityPlayer> canHarvest;
        public final int range;
        public final Stack<BlockPos> toHarvest = new Stack<>();

        public boolean isInvalid()
        {
            return invalid;
        }

        boolean invalid;

        public AWHarvestNode(EntityPlayer harvester, World harvestedIn, BlockPos beginning, int range, Predicate<EntityPlayer> canHarvest)
        {
            this.harvester = harvester;
            this.harvestedIn = harvestedIn;
            this.beginning = beginning;
            this.canHarvest = canHarvest;
            this.range = range;
        }

        public void initNode()
        {
            if (!isLoaded(this.beginning))
            {
                this.invalid = true;
                return;
            }

            this.toHarvest.add(this.beginning);
            IBlockState baseState = this.harvestedIn.getBlockState(this.beginning);
            for (int dx = -range; dx <= range; dx++)
            {
                for (int dy = -range; dy <= range; dy++)
                {
                    for (int dz = -range; dz <= range; dz++)
                    {
                        BlockPos at = this.beginning.add(dx, dy, dz);
                        if (!this.isLoaded(at))
                        {
                            continue;
                        }

                        IBlockState stateAt = this.harvestedIn.getBlockState(at);
                        if (stateAt.equals(baseState))
                        {
                            this.toHarvest.add(at);
                        }
                    }
                }
            }

            this.toHarvest.sort((BlockPos l, BlockPos r) -> (int) (this.harvester.getDistanceSq(r) - this.harvester.getDistanceSq(l)));
            if (this.toHarvest.isEmpty())
            {
                this.invalid = true;
            }
        }

        public void tick()
        {
            BlockPos pos = this.toHarvest.pop();
            if (this.isLoaded(pos))
            {
                IBlockState state = this.harvestedIn.getBlockState(pos);
                if (this.canHarvest.test(this.harvester))
                {
                    if (!this.harvestedIn.isRemote)
                    {
                        int exp = net.minecraftforge.common.ForgeHooks.onBlockBreakEvent(this.harvestedIn, GameType.SURVIVAL, (EntityPlayerMP) this.harvester, pos);
                        if (exp == -1)
                        {
                            return;
                        }

                        SoundType type = state.getBlock().getSoundType(state, this.harvestedIn, pos, this.harvester);
                        this.harvestedIn.playSound(null, pos, type.getBreakSound(), SoundCategory.BLOCKS, type.getVolume(), type.getPitch());
                        boolean flag = state.getBlock().canHarvestBlock(this.harvestedIn, pos, this.harvester);
                        boolean flag1 = this.removeBlock(pos, flag);
                        if (flag1 && flag)
                        {
                            state.getBlock().harvestBlock(this.harvestedIn, this.harvester, pos, state, this.harvestedIn.getTileEntity(pos), this.harvester.getHeldItemMainhand());
                            this.harvester.getHeldItemMainhand().damageItem(1, this.harvester);
                            AWNetworkHarvestNode.sendNodeToClient(new NetworkRegistry.TargetPoint(this.harvestedIn.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 16), state, pos);
                        }
                    }
                }
                else
                {
                    this.invalid = true;
                }
            }

            if (this.toHarvest.isEmpty())
            {
                this.invalid = true;
            }
        }

        public boolean isLoaded(BlockPos pos)
        {
            return this.harvestedIn.isBlockLoaded(pos);
        }

        public boolean removeBlock(BlockPos pos, boolean canHarvest)
        {
            IBlockState iblockstate = this.harvestedIn.getBlockState(pos);
            boolean flag = iblockstate.getBlock().removedByPlayer(iblockstate, this.harvestedIn, pos, this.harvester, canHarvest);

            if (flag)
            {
                iblockstate.getBlock().onBlockDestroyedByPlayer(this.harvestedIn, pos, iblockstate);
            }

            return flag;
        }
    }
}
