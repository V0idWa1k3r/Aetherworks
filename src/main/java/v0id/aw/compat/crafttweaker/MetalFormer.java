package v0id.aw.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import v0id.aw.common.recipe.MetalFormerRecipes;
import v0id.aw.lib.RecipeUtils;

import java.util.Iterator;

@ZenRegister
@ZenClass("mods.aetherworks.MetalFormer")
public class MetalFormer
{
    @ZenMethod
    public static void addRecipe(IItemStack input, ILiquidStack fluid, IItemStack output, int temperature)
    {
        FluidStack fs = (FluidStack)fluid.getInternal();
        ItemStack in = (ItemStack)input.getInternal();
        ItemStack out = (ItemStack)output.getInternal();
        CraftTweakerAPI.apply(new Add(new MetalFormerRecipes.MetalFormerRecipe(fs, in, out, temperature)));
    }

    @ZenMethod
    public static void removeRecipesByOutput(IItemStack output)
    {
        ItemStack is = (ItemStack)output.getInternal();
        CraftTweakerAPI.apply(new Remove(Remove.Context.OUTPUT, is));
    }

    @ZenMethod
    public static void removeRecipesByInput(IItemStack input)
    {
        ItemStack is = (ItemStack)input.getInternal();
        CraftTweakerAPI.apply(new Remove(Remove.Context.INPUT, is));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack input, IItemStack output)
    {
        ItemStack out = (ItemStack)output.getInternal();
        ItemStack in = (ItemStack)input.getInternal();
        CraftTweakerAPI.apply(new Remove(Remove.Context.INPUTOUTPUT, in, out));
    }

    private static class Add implements IAction
    {
        private MetalFormerRecipes.MetalFormerRecipe recipe;

        public Add(MetalFormerRecipes.MetalFormerRecipe recipe)
        {
            this.recipe = recipe;
        }

        @Override
        public void apply()
        {
            MetalFormerRecipes.addRecipe(recipe);
        }

        @Override
        public String describe()
        {
            return "Adding metal former recipe for " + recipe.getInput().getDisplayName();
        }
    }

    private static class Remove implements IAction
    {
        private enum Context
        {
            INPUT,
            OUTPUT,
            INPUTOUTPUT
        }

        private ItemStack[] data;
        private Context context;

        public Remove(Context context, ItemStack... data)
        {
            this.context = context;
            this.data = data;
        }

        @Override
        public void apply()
        {
            Iterator<MetalFormerRecipes.MetalFormerRecipe> iterator = MetalFormerRecipes.recipes.iterator();
            while (iterator.hasNext())
            {
                MetalFormerRecipes.MetalFormerRecipe rec = iterator.next();
                if (this.context == Context.INPUT)
                {
                    if (RecipeUtils.areItemStacksEqual(rec.getInput(), this.data[0]))
                    {
                        iterator.remove();
                    }
                }
                else
                {
                    if (this.context == Context.OUTPUT)
                    {
                        if (RecipeUtils.areItemStacksEqual(rec.getRecipeOutput(), this.data[0]))
                        {
                            iterator.remove();
                        }
                    }
                    else
                    {
                        if (RecipeUtils.areItemStacksEqual(rec.getInput(), this.data[0]) && RecipeUtils.areItemStacksEqual(rec.getRecipeOutput(), this.data[1]))
                        {
                            iterator.remove();
                        }
                    }
                }
            }
        }

        @Override
        public String describe()
        {
            return "Removing metal former recipe for " + this.data[0].getDisplayName();
        }
    }
}
