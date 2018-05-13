package v0id.aw.compat.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import v0id.aw.lib.AWConsts;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by V0idWa1k3r on 03-Jun-17.
 */
public class Categories
{
    public static class AnvilCategory implements IRecipeCategory<Wrappers.AnvilWrapper>
    {
        private final IDrawable drawable;

        public AnvilCategory(IGuiHelper helper)
        {
            this.drawable = helper.createDrawable(AWConsts.anvilUI, 0, 0, 87, 81);
        }

        @Override
        public String getUid()
        {
            return AWConsts.catIDAnvil;
        }

        @Override
        public String getTitle()
        {
            return I18n.format(AWConsts.catIDAnvil);
        }

        @Override
        public String getModName()
        {
            return AWConsts.modid;
        }

        @Override
        public IDrawable getBackground()
        {
            return this.drawable;
        }

        @Nullable
        @Override
        public IDrawable getIcon()
        {
            return null;
        }

        @Override
        public void drawExtras(Minecraft minecraft)
        {

        }

        @Override
        public void setRecipe(IRecipeLayout iRecipeLayout, Wrappers.AnvilWrapper anvilWrapper, IIngredients iIngredients)
        {
            iRecipeLayout.getItemStacks().init(0, true, 4, 22);
            iRecipeLayout.getItemStacks().init(1, false, 58, 22);
            iRecipeLayout.getItemStacks().set(0, iIngredients.getInputs(ItemStack.class).get(0));
            iRecipeLayout.getItemStacks().set(1, iIngredients.getOutputs(ItemStack.class).stream().map(l -> l.get(0)).collect(Collectors.toList()));
        }

        @Override
        public List<String> getTooltipStrings(int i, int i1)
        {
            return Collections.emptyList();
        }
    }

    public static class MetalFormerCategory implements IRecipeCategory<Wrappers.MetalFormerWrapper>
    {
        public final IDrawable drawable;

        public MetalFormerCategory(IGuiHelper guiHelper)
        {
            this.drawable = guiHelper.createDrawable(AWConsts.metalFormerUI, 0, 0, 87, 81);
        }

        @Override
        public String getModName()
        {
            return AWConsts.modid;
        }

        @Override
        public String getUid()
        {
            return AWConsts.catIDMetalFormer;
        }

        @Override
        public String getTitle()
        {
            return I18n.format(AWConsts.catIDMetalFormer);
        }

        @Override
        public IDrawable getBackground()
        {
            return this.drawable;
        }

        @Nullable
        @Override
        public IDrawable getIcon()
        {
            return null;
        }

        @Override
        public void drawExtras(Minecraft minecraft)
        {

        }

        @Override
        public void setRecipe(IRecipeLayout iRecipeLayout, Wrappers.MetalFormerWrapper metalFormerWrapper, IIngredients iIngredients)
        {
            iRecipeLayout.getItemStacks().init(0, true, 4, 4);
            iRecipeLayout.getItemStacks().init(1, false, 58, 22);
            iRecipeLayout.getFluidStacks().init(0, true, 5, 42, 16, 34, 1000, true, null);
            iRecipeLayout.getItemStacks().set(0, iIngredients.getInputs(ItemStack.class).get(0));
            iRecipeLayout.getItemStacks().set(1, iIngredients.getOutputs(ItemStack.class).get(0));
            iRecipeLayout.getFluidStacks().set(0, iIngredients.getInputs(FluidStack.class).get(0));
        }

        @Override
        public List<String> getTooltipStrings(int i, int i1)
        {
            return Collections.emptyList();
        }
    }
}
