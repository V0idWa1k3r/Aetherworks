package v0id.aw.common.handler;

import net.minecraft.block.Block;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import teamroots.embers.RegistryManager;
import v0id.aw.common.block.AWBlocks;

/**
 * Created by V0idWa1k3r on 02-Jun-17.
 */
public class CommonHandler
{
    public static final int[][] KERNEL = {
            {-1, -1},
            {-1,  1},
            {1,  -1},
            {1,   1},
            {1,   0},
            {-1,  0},
            {0,  -1},
            {0,   1},
            {0,   0}
    };

    @SubscribeEvent
    public void onPlayerClickedBlock(PlayerInteractEvent.RightClickBlock event)
    {
        if (event.getEntityPlayer() != null)
        {
            ItemStack stack = event.getItemStack();
            if (RegistryManager.tinker_hammer.equals(stack.getItem()))
            {
                World w = event.getWorld();
                BlockPos pos = event.getPos();
                Block dawnstoneBlkRef = RegistryManager.block_dawnstone;
                for (int[] ints : KERNEL)
                {
                    BlockPos offset = pos.add(ints[0], 0, ints[1]);
                    if (w.getBlockState(offset).getBlock() != dawnstoneBlkRef)
                    {
                        return;
                    }
                }

                for (int[] ints : KERNEL)
                {
                    BlockPos offset = pos.add(ints[0], 0, ints[1]);
                    w.setBlockState(offset, AWBlocks.FORGE_STRUCTURE.getDefaultState(), 0b10);
                    w.playSound(event.getEntityPlayer(), pos, SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.BLOCKS, 0.1F, 0.1F);
                    event.getEntityPlayer().swingArm(event.getHand());
                    stack.damageItem(1, event.getEntityPlayer());
                    event.setCanceled(true);
                }
            }
        }
    }
}
