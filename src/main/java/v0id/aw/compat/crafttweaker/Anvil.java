package v0id.aw.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import v0id.aw.common.recipe.AARecipes;
import v0id.aw.lib.RecipeUtils;

import java.util.Iterator;

@ZenRegister
@ZenClass("mods.aetherworks.Anvil")
public class Anvil
{
    @ZenMethod
    public static void addRecipe(IItemStack input, IItemStack output, int temperatureMin, int temperatureMax, int embersPerHit, int difficulty, int hitsRequired, float temperatureFluctuation)
    {
        ItemStack in = (ItemStack)input.getInternal();
        ItemStack out = (ItemStack)output.getInternal();
        CraftTweakerAPI.apply(new Add(new AARecipes.AARecipe(in, out, difficulty, embersPerHit, hitsRequired, temperatureMin, temperatureMax, temperatureFluctuation)));
    }

    @ZenMethod
    public static void addRecipe(IItemStack input, IItemStack output, int temperatureMin, int temperatureMax, int embersPerHit, int difficulty, int hitsRequired)
    {
        ItemStack in = (ItemStack)input.getInternal();
        ItemStack out = (ItemStack)output.getInternal();
        CraftTweakerAPI.apply(new Add(new AARecipes.AARecipe(in, out, difficulty, embersPerHit, hitsRequired, temperatureMin, temperatureMax, 0)));
    }

    @ZenMethod
    public static void removeRecipesByInput(IItemStack input)
    {
        ItemStack is = (ItemStack)input.getInternal();
        CraftTweakerAPI.apply(new Remove(Remove.Context.INPUT, is));
    }

    @ZenMethod
    public static void removeRecipesByOutput(IItemStack output)
    {
        ItemStack is = (ItemStack)output.getInternal();
        CraftTweakerAPI.apply(new Remove(Remove.Context.OUTPUT, is));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack input, IItemStack output)
    {
        ItemStack in = (ItemStack)input.getInternal();
        ItemStack out = (ItemStack)output.getInternal();
        CraftTweakerAPI.apply(new Remove(Remove.Context.INPUTOUTPUT, in, out));
    }

    private static class Add implements IAction
    {
        private AARecipes.AARecipe toAdd;

        public Add(AARecipes.AARecipe toAdd)
        {
            this.toAdd = toAdd;
        }

        @Override
        public void apply()
        {
            AARecipes.addRecipe(this.toAdd);
        }

        @Override
        public String describe()
        {
            return "Adding anvil recipe for " + this.toAdd.getInput().getDisplayName();
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
            Iterator<AARecipes.AARecipe> iterator = AARecipes.recipes.iterator();
            while (iterator.hasNext())
            {
                AARecipes.AARecipe rec = iterator.next();
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
                        if (RecipeUtils.areItemStacksEqual(rec.getOutput(null), this.data[0]))
                        {
                            iterator.remove();
                        }
                    }
                    else
                    {
                        if (RecipeUtils.areItemStacksEqual(rec.getInput(), this.data[0]) && RecipeUtils.areItemStacksEqual(rec.getOutput(null), this.data[1]))
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
            return "Removing anvil recipe for " + this.data[0].getDisplayName();
        }
    }
}
