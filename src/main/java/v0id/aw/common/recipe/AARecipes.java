package v0id.aw.common.recipe;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import v0id.aw.common.item.Geode;
import v0id.aw.lib.RecipeUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Created by V0idWa1k3r on 03-Jun-17.
 */
public class AARecipes
{
    public static final List<AARecipe> recipes = Lists.newArrayList();

    public static AARecipe addRecipe(ItemStack input, ItemStack output, int difficulty, int embersPerHit, int hitsRequired, int temperatureRequiredMin, int temperatureRequiredMax, float temperatureFluctuation)
    {
        return addRecipe(new AARecipe(input, output, difficulty, embersPerHit, hitsRequired, temperatureRequiredMin, temperatureRequiredMax, temperatureFluctuation));
    }

    public static AARecipe addRecipe(AARecipe rec)
    {
        recipes.add(rec);
        return rec;
    }

    public static Optional<AARecipe> findMatchingRecipe(ItemStack is, int temp)
    {
        return recipes.stream().filter(r -> r.matches(is, temp)).findFirst();
    }

    public static class GeodeRecipe extends AARecipe
    {
        public static final ListMultimap<Geode.Type, Entry> oreDictEntries = ArrayListMultimap.create();

        public static class Entry extends WeightedRandom.Item
        {
            public final String entry;
            private Entry(String s ,int itemWeightIn)
            {
                super(itemWeightIn);
                this.entry = s;
            }
        }

        public GeodeRecipe(ItemStack input, ItemStack output, int difficulty, int embersPerHit, int hitsRequired, int temperatureRequiredMin, int temperatureRequiredMax, float temperatureFluctuation)
        {
            super(input, output, difficulty, embersPerHit, hitsRequired, temperatureRequiredMin, temperatureRequiredMax, temperatureFluctuation);
        }

        public static void addEntry(Geode.Type type, String oreDict, int i)
        {
            if (OreDictionary.doesOreNameExist(oreDict))
            {
                oreDictEntries.put(type, new Entry(oreDict, i));
            }
        }

        public static final Random RANDOM = new Random();

        @Override
        public ItemStack getOutput(@Nullable World w)
        {
            Geode.Type geodeType = Geode.getType(this.getInput());
            if (oreDictEntries.containsKey(geodeType))
            {
                List<Entry> entries = oreDictEntries.get(geodeType);
                RANDOM.setSeed(w != null ? w.getWorldTime() : 0);
                Entry e = WeightedRandom.getRandomItem(entries, RANDOM.nextInt(WeightedRandom.getTotalWeight(entries)));
                String oreDictName = e.entry;
                NonNullList<ItemStack> items = OreDictionary.getOres(oreDictName);
                return items.isEmpty() ? super.getOutput(w) : items.get(0);
            }

            return super.getOutput(w);
        }
    }

    public static class AARecipe
    {
        private final ItemStack input;
        private final ItemStack output;
        private final int difficulty;
        private final int embersPerHit;
        private final int hitsRequired;
        private final int temperatureRequiredMin;
        private final int temperatureRequiredMax;
        private final float temperatureFluctuation;

        public AARecipe(ItemStack input, ItemStack output, int difficulty, int embersPerHit, int hitsRequired, int temperatureRequiredMin, int temperatureRequiredMax, float temperatureFluctuation)
        {
            this.input = input;
            this.output = output;
            this.difficulty = difficulty;
            this.embersPerHit = embersPerHit;
            this.hitsRequired = hitsRequired;
            this.temperatureRequiredMin = temperatureRequiredMin;
            this.temperatureRequiredMax = temperatureRequiredMax;
            this.temperatureFluctuation = temperatureFluctuation;
        }

        public boolean matches(ItemStack in, int temperature)
        {
            return RecipeUtils.areItemStacksEqual(in, this.getInput()) && temperature >= this.getTemperatureRequiredMin() && temperature <= this.getTemperatureRequiredMax();
        }

        public ItemStack getInput()
        {
            return input;
        }

        public ItemStack getOutput(World w)
        {
            return output;
        }

        public int getDifficulty()
        {
            return difficulty;
        }

        public int getEmbersPerHit()
        {
            return embersPerHit;
        }

        public int getHitsRequired()
        {
            return hitsRequired;
        }

        public int getTemperatureRequiredMin()
        {
            return temperatureRequiredMin;
        }

        public int getTemperatureRequiredMax()
        {
            return temperatureRequiredMax;
        }

        public float getTemperatureFluctuation()
        {
            return temperatureFluctuation;
        }
    }
}
