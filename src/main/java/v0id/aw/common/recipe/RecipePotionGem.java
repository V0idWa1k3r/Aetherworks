package v0id.aw.common.recipe;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import v0id.aw.common.item.AWItems;
import v0id.aw.common.item.PotionGem;
import v0id.aw.lib.AWConsts;

public class RecipePotionGem extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe
{
    public RecipePotionGem()
    {
        this.setRegistryName(AWConsts.recipePotionGem);
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn)
    {
        ItemStack potion = ItemStack.EMPTY;
        ItemStack gem = ItemStack.EMPTY;
        for (int i = 0; i < inv.getWidth() * inv.getHeight(); ++i)
        {
            ItemStack is = inv.getStackInSlot(i);
            if (is.getItem() instanceof ItemPotion)
            {
                if (!potion.isEmpty())
                {
                    return false;
                }

                potion = is;
            }
            else
            {
                if (is.getItem() instanceof PotionGem)
                {
                    if (!gem.isEmpty())
                    {
                        return false;
                    }

                    gem = is;
                }
                else
                {
                    if (!is.isEmpty())
                    {
                        return false;
                    }
                }
            }
        }

        return !potion.isEmpty() && !gem.isEmpty();
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv)
    {
        ItemStack potion = ItemStack.EMPTY;
        ItemStack gem = ItemStack.EMPTY;
        for (int i = 0; i < inv.getWidth() * inv.getHeight(); ++i)
        {
            ItemStack is = inv.getStackInSlot(i);
            if (is.getItem() instanceof ItemPotion)
            {
                potion = is;
            }
            else
            {
                if (is.getItem() instanceof PotionGem)
                {
                    gem = is;
                }
            }
        }

        for (PotionType type : PotionType.REGISTRY)
        {
            ItemStack ppotion = new ItemStack(Items.POTIONITEM, 1, 0);
            ppotion = PotionUtils.addPotionToItemStack(ppotion, type);
            if (ItemStack.areItemStackTagsEqual(ppotion, potion))
            {
                return new ItemStack(AWItems.POTION_GEM, 1, PotionType.REGISTRY.getIDForObject(type) + 1);
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
    {
        NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>withSize(inv.getSizeInventory(), ItemStack.EMPTY);

        for (int i = 0; i < nonnulllist.size(); ++i)
        {
            ItemStack itemstack = inv.getStackInSlot(i);
            if (itemstack.getItem() instanceof ItemPotion)
            {
                nonnulllist.set(i, new ItemStack(Items.GLASS_BOTTLE, 1, 0));
            }
            else
            {
                nonnulllist.set(i, net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack));
            }
        }

        return nonnulllist;
    }

    @Override
    public boolean canFit(int width, int height)
    {
        return width * height >= 2;
    }

    @Override
    public ItemStack getRecipeOutput()
    {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isDynamic()
    {
        return true;
    }
}
