package v0id.aw.compat.jei;

import mezz.jei.api.*;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import v0id.aw.common.block.AWBlocks;
import v0id.aw.common.recipe.AARecipes;
import v0id.aw.common.recipe.MetalFormerRecipes;
import v0id.aw.lib.AWConsts;

import java.util.stream.Collectors;

/**
 * Created by V0idWa1k3r on 03-Jun-17.
 */
@JEIPlugin
public class AWPlugin implements IModPlugin
{
    public AWPlugin()
    {

    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistry iSubtypeRegistry)
    {

    }

    @Override
    public void registerIngredients(IModIngredientRegistration iModIngredientRegistration)
    {

    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry)
    {
        registry.addRecipeCategories(
                new Categories.MetalFormerCategory(registry.getJeiHelpers().getGuiHelper()),
                new Categories.AnvilCategory(registry.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void register(IModRegistry iModRegistry)
    {
        iModRegistry.addRecipes(MetalFormerRecipes.recipes.stream().map(Wrappers.MetalFormerWrapper::new).collect(Collectors.toList()), AWConsts.catIDMetalFormer);
        iModRegistry.addRecipeCatalyst(new ItemStack(AWBlocks.FORGE_COMPONENT, 1, 4), AWConsts.catIDMetalFormer);
        iModRegistry.addRecipes(AARecipes.recipes.stream().map(Wrappers.AnvilWrapper::new).collect(Collectors.toList()), AWConsts.catIDAnvil);
        iModRegistry.addRecipeCatalyst(new ItemStack(AWBlocks.FORGE_COMPONENT, 1, 5), AWConsts.catIDAnvil);
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime iJeiRuntime)
    {

    }
}
