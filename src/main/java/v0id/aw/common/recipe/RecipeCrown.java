package v0id.aw.common.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.IForgeRegistryEntry;
import v0id.aw.common.item.AWCrown;
import v0id.aw.common.item.AWItems;
import v0id.aw.common.item.PotionGem;
import v0id.aw.lib.AWConsts;

public class RecipeCrown extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe
{
    public RecipeCrown()
    {
        this.setRegistryName(AWConsts.recipePotionGem);
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn)
    {
        ItemStack crown = ItemStack.EMPTY;
        ItemStack gem = ItemStack.EMPTY;
        for (int i = 0; i < inv.getHeight() * inv.getWidth(); ++i)
        {
            ItemStack is = inv.getStackInSlot(i);
            if (is.getItem() instanceof AWCrown)
            {
                if (!crown.isEmpty())
                {
                    return false;
                }

                crown = is;
            }
            else
            {
                if (is.getItem() instanceof PotionGem)
                {
                    if (!gem.isEmpty() || is.getMetadata() == 0)
                    {
                        return false;
                    }

                    gem = is;
                }
            }
        }

        return !crown.isEmpty();
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv)
    {
        ItemStack crown = ItemStack.EMPTY;
        ItemStack gem = ItemStack.EMPTY;
        for (int i = 0; i < inv.getHeight() * inv.getWidth(); ++i)
        {
            ItemStack is = inv.getStackInSlot(i);
            if (is.getItem() instanceof AWCrown)
            {
                crown = is;
            }
            else
            {
                if (is.getItem() instanceof PotionGem)
                {
                    gem = is;
                }
            }
        }

        if (!crown.isEmpty() && !gem.isEmpty())
        {
            ItemStack newCrown = crown.copy();
            if (!newCrown.hasTagCompound())
            {
                newCrown.setTagCompound(new NBTTagCompound());
            }

            newCrown.getTagCompound().setString("potionType", PotionType.REGISTRY.getObjectById(gem.getMetadata() - 1).getRegistryName().toString());
            return newCrown;
        }
        else
        {
            if (!crown.isEmpty())
            {
                if (crown.hasTagCompound() && crown.getTagCompound().hasKey("potionType", Constants.NBT.TAG_STRING))
                {
                    return new ItemStack(AWItems.POTION_GEM, 1, PotionType.REGISTRY.getIDForObject(PotionType.REGISTRY.getObject(new ResourceLocation(crown.getTagCompound().getString("potionType")))) + 1);
                }
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public boolean canFit(int width, int height)
    {
        return width * height >= 1;
    }

    @Override
    public ItemStack getRecipeOutput()
    {
        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
    {
        NonNullList<ItemStack> list = NonNullList.<ItemStack>withSize(inv.getSizeInventory(), ItemStack.EMPTY);
        int posCrown = -1;
        ItemStack crown = ItemStack.EMPTY;
        int posGem = -1;
        ItemStack gem = ItemStack.EMPTY;
        for (int i = 0; i < list.size(); ++i)
        {
            ItemStack is = inv.getStackInSlot(i);
            if (is.getItem() instanceof AWCrown)
            {
                posCrown = i;
                crown = is;
            }
            else
            {
                if (is.getItem() instanceof PotionGem)
                {
                    posGem = i;
                    gem = is;
                }
            }
        }

        if (posCrown != -1 && posGem != -1)
        {
            ItemStack newGem = ItemStack.EMPTY;
            if (crown.hasTagCompound() && crown.getTagCompound().hasKey("potionType", Constants.NBT.TAG_STRING))
            {
                newGem = new ItemStack(AWItems.POTION_GEM, 1, PotionType.REGISTRY.getIDForObject(PotionType.REGISTRY.getObject(new ResourceLocation(crown.getTagCompound().getString("potionType")))) + 1);
            }

            list.set(posGem, newGem);
        }
        else
        {
            if (posCrown != -1)
            {
                ItemStack newGem = ItemStack.EMPTY;
                if (crown.hasTagCompound() && crown.getTagCompound().hasKey("potionType", Constants.NBT.TAG_STRING))
                {
                    ItemStack newCrown = crown.copy();
                    newCrown.getTagCompound().removeTag("potionType");
                    list.set(posCrown, newCrown);
                }
            }
        }

        return list;
    }

    @Override
    public boolean isDynamic()
    {
        return true;
    }
}
