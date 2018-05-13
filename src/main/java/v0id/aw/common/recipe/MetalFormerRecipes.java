package v0id.aw.common.recipe;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import v0id.aw.lib.RecipeUtils;

import java.util.List;
import java.util.Optional;

/**
 * Created by V0idWa1k3r on 03-Jun-17.
 */
public class MetalFormerRecipes
{
    public static final List<MetalFormerRecipe> recipes = Lists.newArrayList();

    public static MetalFormerRecipe addRecipe(FluidStack fsIn, ItemStack in, ItemStack out, int temp)
    {
        return addRecipe(new MetalFormerRecipe(fsIn, in, out, temp));
    }

    public static MetalFormerRecipe addRecipe(MetalFormerRecipe rec)
    {
        recipes.add(rec);
        return rec;
    }

    public static Optional<MetalFormerRecipe> findMatchingRecipe(ItemStack in, FluidStack fs, int i)
    {
        return recipes.stream().filter(r -> r.matches(fs, in, i)).findFirst();
    }

    public static class MetalFormerRecipe
    {
        private FluidStack fluidRequirements;
        private ItemStack in;
        private ItemStack out;
        private int temperature;

        public MetalFormerRecipe(FluidStack fluidRequirements, ItemStack in, ItemStack out, int temperature)
        {
            this.fluidRequirements = fluidRequirements;
            this.in = in;
            this.out = out;
            this.temperature = temperature;
        }

        public boolean matches(FluidStack stack, ItemStack is, int temp)
        {
            return temp >= this.temperature && fluidRequirements.isFluidEqual(stack) && fluidRequirements.amount <= stack.amount && RecipeUtils.areItemStacksEqual(is, in);
        }

        public ItemStack getResult()
        {
            return this.out;
        }

        public ItemStack getInput()
        {
            return this.in;
        }

        public FluidStack getFluidRequirements()
        {
            return this.fluidRequirements;
        }

        public int getTemperature()
        {
            return this.temperature;
        }

        public ItemStack getRecipeOutput()
        {
            return out;
        }
    }
}
