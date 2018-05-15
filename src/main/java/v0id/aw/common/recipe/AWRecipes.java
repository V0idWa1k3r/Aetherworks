package v0id.aw.common.recipe;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import teamroots.embers.RegistryManager;
import teamroots.embers.item.EnumStampType;
import teamroots.embers.recipe.*;
import v0id.aw.common.block.AWBlocks;
import v0id.aw.common.fluid.AWFluids;
import v0id.aw.common.item.AWItems;
import v0id.aw.common.item.Geode;
import v0id.aw.lib.ILifecycleListener;

import java.util.stream.Stream;

/**
 * Created by V0idWa1k3r on 31-May-17.
 */
@Mod.EventBusSubscriber
public class AWRecipes implements ILifecycleListener
{
    @SubscribeEvent
    public static void onRegisterRecipes(RegistryEvent.Register<IRecipe> event)
    {
        RecipeRegistry.stampingRecipes.add(new ItemStampingRecipe(new ItemStack(RegistryManager.shard_ember), new FluidStack(FluidRegistry.WATER, 1000), EnumStampType.TYPE_FLAT, new ItemStack(AWItems.RESOURCE, 1, 1), false, false));
        RecipeRegistry.mixingRecipes.add(new FluidMixingRecipe(new FluidStack[]{ new FluidStack(AWFluids.FLUID_IMPURE_AETHERIUM_GAS, 8), new FluidStack(RegistryManager.fluid_molten_electrum, 8) }, new FluidStack(AWFluids.FLUID_AETHERIUM_GAS, 16)));
        RecipeRegistry.stampingRecipes.add(new ItemStampingRecipe(ItemStack.EMPTY, new FluidStack(AWFluids.FLUID_AETHERIUM_GAS, 16), EnumStampType.TYPE_FLAT, new ItemStack(AWItems.RESOURCE, 1, 0), false, false));
        RecipeRegistry.meltingOreRecipes.add(new ItemMeltingOreRecipe("shardAether", new FluidStack(AWFluids.FLUID_AETHERIUM_GAS, 16)));
        RecipeRegistry.alchemyRecipes.add(new AlchemyRecipe(12, 16, 12, 16, 12, 16, 12, 16, 12, 16, new ItemStack(AWItems.RESOURCE, 1, 7), ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, new ItemStack(AWItems.RESOURCE, 1, 8)));
        RecipeRegistry.alchemyRecipes.add(new AlchemyRecipe(30, 34, 48, 64, 0, 0, 48, 64, 0, 0, new ItemStack(AWItems.RESOURCE, 1, 10), new ItemStack(AWItems.RESOURCE, 1, 0), new ItemStack(AWItems.RESOURCE, 1, 0), new ItemStack(AWItems.RESOURCE, 1, 0), new ItemStack(AWItems.RESOURCE, 1, 0), new ItemStack(AWItems.RESOURCE, 1, 11)));
        RecipeRegistry.alchemyRecipes.add(new AlchemyRecipe(30, 34, 0, 0, 48, 64, 0, 0, 48, 64, new ItemStack(AWItems.RESOURCE, 1, 10), new ItemStack(RegistryManager.crystal_ember, 1, 0), new ItemStack(RegistryManager.crystal_ember, 1, 0), new ItemStack(RegistryManager.crystal_ember, 1, 0), new ItemStack(RegistryManager.crystal_ember, 1, 0), new ItemStack(AWItems.RESOURCE, 1, 12)));
        RecipeRegistry.alchemyRecipes.add(new AlchemyRecipe(0, 0, 48, 64, 48, 64, 30, 34, 0, 0, new ItemStack(AWItems.RESOURCE, 1, 14), new ItemStack(Items.PRISMARINE_CRYSTALS, 1, 0), new ItemStack(Items.PRISMARINE_SHARD, 1, 0), new ItemStack(Items.PRISMARINE_CRYSTALS, 1, 0), new ItemStack(Items.PRISMARINE_SHARD, 1, 0), new ItemStack(AWItems.RESOURCE, 1, 15)));
        RecipeRegistry.alchemyRecipes.add(new AlchemyRecipe(48, 64, 0, 0, 0, 0, 30, 34, 48, 64, new ItemStack(AWItems.RESOURCE, 1, 14), new ItemStack(Items.ENDER_PEARL, 1, 0), new ItemStack(Items.ENDER_EYE, 1, 0), new ItemStack(Items.ENDER_PEARL, 1, 0), new ItemStack(Items.ENDER_EYE, 1, 0), new ItemStack(AWItems.RESOURCE, 1, 16)));
        RecipeRegistry.alchemyRecipes.add(new AlchemyRecipe(48, 64, 0, 0, 48, 64, 0, 0, 30, 34, new ItemStack(AWItems.RESOURCE, 1, 18), new ItemStack(Items.REDSTONE, 1, 0), new ItemStack(Blocks.PISTON, 1, 0), new ItemStack(Blocks.REDSTONE_TORCH, 1, 0), new ItemStack(Items.REPEATER, 1, 0), new ItemStack(AWItems.RESOURCE, 1, 19)));
        RecipeRegistry.alchemyRecipes.add(new AlchemyRecipe(0, 0, 48, 64, 0, 0, 32, 64, 30, 34, new ItemStack(AWItems.RESOURCE, 1, 18), new ItemStack(Items.SLIME_BALL, 1, 0), new ItemStack(Blocks.STICKY_PISTON, 1, 0), new ItemStack(Items.COMPARATOR, 1, 0), new ItemStack(Blocks.HOPPER, 1, 0), new ItemStack(AWItems.RESOURCE, 1, 20)));
        MetalFormerRecipes.addRecipe(new FluidStack(AWFluids.FLUID_AETHERIUM_GAS, 144), new ItemStack(RegistryManager.ingot_bronze, 1, 0), new ItemStack(AWItems.RESOURCE, 1, 4), 2100);
        MetalFormerRecipes.addRecipe(new FluidStack(AWFluids.FLUID_AETHERIUM_GAS, 144), new ItemStack(RegistryManager.plate_bronze, 1, 0), new ItemStack(AWItems.RESOURCE, 1, 3), 2100);
        MetalFormerRecipes.addRecipe(new FluidStack(AWFluids.FLUID_AETHERIUM_GAS, 576), new ItemStack(Items.DIAMOND, 1, 0), new ItemStack(AWItems.RESOURCE, 1, 5), 2200);
        MetalFormerRecipes.addRecipe(new FluidStack(AWFluids.FLUID_AETHERIUM_GAS, 576), new ItemStack(Items.EMERALD, 1, 0), new ItemStack(AWItems.RESOURCE, 1, 5), 2600);
        AARecipes.addRecipe(new ItemStack(AWItems.RESOURCE, 1, 6), new ItemStack(AWItems.RESOURCE, 1, 7), 1, 50, 15, 2100, 3000, 30);
        AARecipes.addRecipe(new ItemStack(AWItems.RESOURCE, 1, 9), new ItemStack(AWItems.RESOURCE, 1, 10), 4, 60, 30, 2400, 2900, 50);
        AARecipes.addRecipe(new ItemStack(AWItems.RESOURCE, 1, 13), new ItemStack(AWItems.RESOURCE, 1, 14), 4, 55, 35, 2350, 2800, 40);
        AARecipes.addRecipe(new ItemStack(AWItems.RESOURCE, 1, 17), new ItemStack(AWItems.RESOURCE, 1, 18), 4, 70, 25, 2500, 2900, 55);
        Stream.of(Geode.Type.values()).forEach(t -> AARecipes.addRecipe(new AARecipes.GeodeRecipe(new ItemStack(AWItems.GEODE, 1, t.ordinal()), ItemStack.EMPTY, 1, 10, 5, 800, 3000, 30)));
        registerGeodes();
    }

    @Override
    public void init(FMLInitializationEvent evt)
    {
        OreDictionary.registerOre("ice", Blocks.ICE);
        OreDictionary.registerOre("icePacked", Blocks.PACKED_ICE);
        OreDictionary.registerOre("oreAether", AWBlocks.AETHER_ORE);
        OreDictionary.registerOre("blockAether", AWBlocks.BLOCK_AETHER);

        OreDictionary.registerOre("shardAether", new ItemStack(AWItems.RESOURCE, 1, 0));
        OreDictionary.registerOre("nuggetAether", new ItemStack(AWItems.RESOURCE, 1, 0));
        OreDictionary.registerOre("lensAether", new ItemStack(AWItems.RESOURCE, 1, 2));
        OreDictionary.registerOre("plateAether", new ItemStack(AWItems.RESOURCE, 1, 3));
        OreDictionary.registerOre("ingotAether", new ItemStack(AWItems.RESOURCE, 1, 4));
        OreDictionary.registerOre("gemAether", new ItemStack(AWItems.RESOURCE, 1, 5));
    }

    private static void registerGeodes()
    {
        AARecipes.GeodeRecipe.addEntry(Geode.Type.BASIC, "cobblestone", 10);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.BASIC, "stone", 10);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.BASIC, "stoneGranite", 10);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.BASIC, "stoneDiorite", 10);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.BASIC, "stoneAndesite", 10);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.BASIC, "marble", 10);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.BASIC, "basalt", 10);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.BASIC, "oreCoal", 20);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.BASIC, "oreIron", 10);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.BASIC, "oreGold", 5);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.BASIC, "oreDiamond", 2);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.BASIC, "oreEmerald", 2);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.BASIC, "oreCopper", 15);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.BASIC, "oreTin", 15);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.BASIC, "oreSilver", 10);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.BASIC, "oreNickel", 10);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.BASIC, "oreLead", 10);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.BASIC, "orePlatinum", 3);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.BASIC, "oreIridium", 1);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.BASIC, "oreAluminum", 15);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.BASIC, "oreRedstone", 10);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.BASIC, "oreSalt", 20);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.BASIC, "oreSaltpeter", 20);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.BASIC, "oreNiter", 20);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.BASIC, "oreLapis", 20);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.BASIC, "oreSapphire", 2);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.BASIC, "oreRuby", 2);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.BASIC, "orePeridot", 2);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.BASIC, "oreGarnet", 2);

        AARecipes.GeodeRecipe.addEntry(Geode.Type.HOT, "cobblestone", 2);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.HOT, "stone", 10);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.HOT, "stoneGranite", 20);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.HOT, "stoneDiorite", 10);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.HOT, "stoneAndesite", 10);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.HOT, "marble", 10);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.HOT, "basalt", 30);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.HOT, "sandstone", 30);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.HOT, "obsidian", 20);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.HOT, "oreCoal", 10);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.HOT, "oreIron", 25);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.HOT, "oreGold", 15);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.HOT, "oreDiamond", 3);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.HOT, "oreEmerald", 3);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.HOT, "oreCopper", 12);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.HOT, "oreTin", 12);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.HOT, "oreSilver", 15);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.HOT, "oreNickel", 12);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.HOT, "oreLead", 15);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.HOT, "orePlatinum", 2);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.HOT, "oreIridium", 1);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.HOT, "oreAluminum", 10);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.HOT, "oreRedstone", 20);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.HOT, "oreSalt", 20);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.HOT, "oreSaltpeter", 20);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.HOT, "oreNiter", 50);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.HOT, "oreLapis", 15);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.HOT, "oreSapphire", 3);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.HOT, "oreRuby", 3);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.HOT, "orePeridot", 3);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.HOT, "oreGarnet", 3);

        AARecipes.GeodeRecipe.addEntry(Geode.Type.COLD, "ice", 10);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.COLD, "icePacked", 10);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.COLD, "stone", 5);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.COLD, "stoneGranite", 5);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.COLD, "stoneDiorite", 5);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.COLD, "stoneAndesite", 5);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.COLD, "marble", 15);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.COLD, "basalt", 5);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.COLD, "oreCoal", 25);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.COLD, "oreIron", 10);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.COLD, "oreGold", 5);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.COLD, "oreDiamond", 2);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.COLD, "oreEmerald", 2);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.COLD, "oreCopper", 12);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.COLD, "oreTin", 20);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.COLD, "oreSilver", 20);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.COLD, "oreNickel", 20);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.COLD, "oreLead", 20);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.COLD, "orePlatinum", 6);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.COLD, "oreIridium", 1);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.COLD, "oreAluminum", 20);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.COLD, "oreRedstone", 8);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.COLD, "oreSalt", 2);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.COLD, "oreSaltpeter", 40);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.COLD, "oreNiter", 10);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.COLD, "oreLapis", 10);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.COLD, "oreSapphire", 3);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.COLD, "oreRuby", 2);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.COLD, "orePeridot", 2);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.COLD, "oreGarnet", 2);

        AARecipes.GeodeRecipe.addEntry(Geode.Type.OCEAN, "sand", 60);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.OCEAN, "stone", 2);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.OCEAN, "stoneGranite", 2);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.OCEAN, "stoneDiorite", 2);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.OCEAN, "stoneAndesite", 2);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.OCEAN, "blockPrismarine", 30);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.OCEAN, "gemPrismarine", 20);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.OCEAN, "dustPrismarine", 20);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.OCEAN, "marble", 2);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.OCEAN, "basalt", 2);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.OCEAN, "oreCoal", 10);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.OCEAN, "oreIron", 8);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.OCEAN, "oreGold", 4);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.OCEAN, "oreDiamond", 8);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.OCEAN, "oreEmerald", 8);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.OCEAN, "oreCopper", 8);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.OCEAN, "oreTin", 8);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.OCEAN, "oreSilver", 8);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.OCEAN, "oreNickel", 8);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.OCEAN, "oreLead", 8);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.OCEAN, "orePlatinum", 2);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.OCEAN, "oreIridium", 1);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.OCEAN, "oreAluminum", 8);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.OCEAN, "oreRedstone", 25);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.OCEAN, "oreSalt", 50);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.OCEAN, "oreSaltpeter", 8);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.OCEAN, "oreNiter", 8);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.OCEAN, "oreLapis", 30);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.OCEAN, "oreSapphire", 8);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.OCEAN, "oreRuby", 8);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.OCEAN, "orePeridot", 8);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.OCEAN, "oreGarnet", 8);

        AARecipes.GeodeRecipe.addEntry(Geode.Type.NETHER, "netherrack", 100);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.NETHER, "oreQuartz", 10);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.NETHER, "oreNetherCoal", 20);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.NETHER, "oreNetherIron", 10);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.NETHER, "oreNetherGold", 5);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.NETHER, "oreNetherDiamond", 2);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.NETHER, "oreNetherEmerald", 2);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.NETHER, "oreNetherCopper", 15);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.NETHER, "oreNetherTin", 15);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.NETHER, "oreNetherSilver", 10);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.NETHER, "oreNetherNickel", 10);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.NETHER, "oreNetherLead", 10);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.NETHER, "oreNetherPlatinum", 3);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.NETHER, "oreNetherIridium", 1);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.NETHER, "oreNetherAluminum", 15);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.NETHER, "oreNetherRedstone", 10);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.NETHER, "oreNetherSalt", 20);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.NETHER, "oreNetherSaltpeter", 20);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.NETHER, "oreNetherNiter", 20);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.NETHER, "oreNetherLapis", 20);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.NETHER, "oreNetherSapphire", 2);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.NETHER, "oreNetherRuby", 2);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.NETHER, "oreNetherPeridot", 2);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.NETHER, "oreNetherGarnet", 2);

        AARecipes.GeodeRecipe.addEntry(Geode.Type.END, "endstone", 100);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.END, "obsidian", 30);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.END, "oreEndCoal", 20);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.END, "oreEndIron", 10);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.END, "oreEndGold", 5);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.END, "oreEndDiamond", 2);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.END, "oreEndEmerald", 2);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.END, "oreEndCopper", 15);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.END, "oreEndTin", 15);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.END, "oreEndSilver", 10);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.END, "oreEndNickel", 10);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.END, "oreEndLead", 10);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.END, "oreEndPlatinum", 3);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.END, "oreEndIridium", 1);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.END, "oreEndAluminum", 15);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.END, "oreEndRedstone", 10);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.END, "oreEndSalt", 20);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.END, "oreEndSaltpeter", 20);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.END, "oreEndNiter", 20);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.END, "oreEndLapis", 20);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.END, "oreEndSapphire", 2);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.END, "oreEndRuby", 2);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.END, "oreEndPeridot", 2);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.END, "oreEndGarnet", 2);
        AARecipes.GeodeRecipe.addEntry(Geode.Type.END, "oreDraconium", 10);
    }
}
