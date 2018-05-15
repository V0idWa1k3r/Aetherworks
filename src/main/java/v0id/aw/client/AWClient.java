package v0id.aw.client;

import com.google.common.collect.Maps;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;
import teamroots.embers.particle.ParticleUtil;
import v0id.aw.client.handler.ClientHandler;
import v0id.aw.client.render.tile.RenderAnvil;
import v0id.aw.client.render.tile.RenderForge;
import v0id.aw.client.render.tile.RenderMetalFormer;
import v0id.aw.client.render.tile.RenderPrism;
import v0id.aw.common.block.AWBlocks;
import v0id.aw.common.block.forge.Component;
import v0id.aw.common.item.AWItems;
import v0id.aw.common.item.AWResource;
import v0id.aw.common.item.Geode;
import v0id.aw.common.tile.TileAnvil;
import v0id.aw.common.tile.TileForge;
import v0id.aw.common.tile.TileMetalFormer;
import v0id.aw.common.tile.TilePrism;
import v0id.aw.lib.AWConsts;
import v0id.aw.lib.IAWProxy;

import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by V0idWa1k3r on 31-May-17.
 */
public class AWClient implements IAWProxy
{
    public static final Map<ResourceLocation, TextureAtlasSprite> cachedModSprites = Maps.newHashMap();
    public static final ResourceLocation LOCATION_PRISM_OVERLAY = new ResourceLocation(AWConsts.modid, "blocks/prism_active_overlay_0");
    public static final ResourceLocation[] LOCATION_RUNES = {
            new ResourceLocation(AWConsts.modid, "blocks/runes/a"),
            new ResourceLocation(AWConsts.modid, "blocks/runes/e"),
            new ResourceLocation(AWConsts.modid, "blocks/runes/h"),
            new ResourceLocation(AWConsts.modid, "blocks/runes/r"),
            new ResourceLocation(AWConsts.modid, "blocks/runes/t")
    };

    public static final ResourceLocation LOCATION_FORGE_OVERLAY = new ResourceLocation(AWConsts.modid, "blocks/forge_overlay");

    @SubscribeEvent
    public void onModelRegistry(ModelRegistryEvent event)
    {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(AWBlocks.AETHER_ORE), 0, mkModelPtr(AWBlocks.AETHER_ORE, "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(AWBlocks.AETHER_PRISM_CONTROLLER_MATRIX), 0, mkModelPtr(AWBlocks.AETHER_PRISM_CONTROLLER_MATRIX, "inventory"));
        Stream.of(AWResource.ResourceType.values()).forEach(resource -> ModelLoader.setCustomModelResourceLocation(AWItems.RESOURCE, resource.getMeta(), mkModelPtr(AWItems.RESOURCE, "type=" + resource.getName())));
        Stream.of(EnumFacing.values()).forEach(face -> ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(AWBlocks.MOONLIGHT_AMPLIFIER), face.ordinal(), mkModelPtr(AWBlocks.MOONLIGHT_AMPLIFIER, "facing=north")));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(AWBlocks.PRISM_SUPPORT), 0, mkModelPtr(AWBlocks.PRISM_SUPPORT, "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(AWBlocks.PRISM), 0, mkModelPtr(AWBlocks.PRISM, "inventory"));
        ModelLoader.setCustomStateMapper(AWBlocks.BLOCK_FLUID_AETHERIUM, new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                return new ModelResourceLocation(AWConsts.blockAetheriumGas, "fluid");
            }
        });

        ModelLoader.setCustomStateMapper(AWBlocks.BLOCK_FLUID_IMPURE_AETHERIUM, new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                return new ModelResourceLocation(AWConsts.blockImpureAetheriumGas, "fluid");
            }
        });

        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(AWBlocks.BLOCK_FLUID_AETHERIUM), 0, mkModelPtr(AWBlocks.BLOCK_FLUID_AETHERIUM, "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(AWBlocks.BLOCK_FLUID_IMPURE_AETHERIUM), 0, mkModelPtr(AWBlocks.BLOCK_FLUID_IMPURE_AETHERIUM, "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(AWBlocks.FORGE_STRUCTURE), 0, mkModelPtr(AWBlocks.FORGE_STRUCTURE, "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(AWBlocks.BLOCK_AETHER), 0, mkModelPtr(AWBlocks.BLOCK_AETHER, "inventory"));
        Stream.of(Component.Type.values()).forEach(type -> ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(AWBlocks.FORGE_COMPONENT), type.ordinal(), mkModelPtr(AWBlocks.FORGE_COMPONENT, "facing=down,type=" + type.getName())));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(AWBlocks.HEAT_DIAL), 0, mkModelPtr(AWBlocks.HEAT_DIAL, "inventory"));
        ModelLoader.setCustomMeshDefinition(AWItems.PICKAXE_AETHERIUM, stack -> mkModelPtr(AWConsts.itemPickaxe, "type=aether"));
        ModelBakery.registerItemVariants(AWItems.PICKAXE_AETHERIUM, mkModelPtr(AWConsts.itemPickaxe, "type=aether"));
        ModelLoader.setCustomMeshDefinition(AWItems.PICKAXE_EMBER, stack -> mkModelPtr(AWConsts.itemPickaxe, "type=ember"));
        ModelBakery.registerItemVariants(AWItems.PICKAXE_EMBER, mkModelPtr(AWConsts.itemPickaxe, "type=ember"));
        Stream.of(Geode.Type.values()).forEach(t -> ModelLoader.setCustomModelResourceLocation(AWItems.GEODE, t.ordinal(), mkModelPtr(AWItems.GEODE, "type=" + t.getName())));
        ModelLoader.setCustomMeshDefinition(AWItems.AXE_PRISMARINE, stack -> mkModelPtr(AWConsts.itemAxe, "type=prismarine"));
        ModelBakery.registerItemVariants(AWItems.AXE_PRISMARINE, mkModelPtr(AWConsts.itemAxe, "type=prismarine"));
        ModelLoader.setCustomMeshDefinition(AWItems.AXE_ENDER, stack -> mkModelPtr(AWConsts.itemAxe, "type=ender"));
        ModelBakery.registerItemVariants(AWItems.AXE_ENDER, mkModelPtr(AWConsts.itemAxe, "type=ender"));
        ModelLoader.setCustomMeshDefinition(AWItems.SHOVEL_REDSTONE, stack -> mkModelPtr(AWConsts.itemShovel, "type=redstone"));
        ModelBakery.registerItemVariants(AWItems.SHOVEL_REDSTONE, mkModelPtr(AWConsts.itemShovel, "type=redstone"));
        ModelLoader.setCustomMeshDefinition(AWItems.SHOVEL_SLIME, stack -> mkModelPtr(AWConsts.itemShovel, "type=slime"));
        ModelBakery.registerItemVariants(AWItems.SHOVEL_SLIME, mkModelPtr(AWConsts.itemShovel, "type=slime"));
    }

    @Override
    public void preInit(FMLPreInitializationEvent evt)
    {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new ClientHandler());
        ClientRegistry.bindTileEntitySpecialRenderer(TilePrism.class, new RenderPrism());
        ClientRegistry.bindTileEntitySpecialRenderer(TileForge.class, new RenderForge());
        ClientRegistry.bindTileEntitySpecialRenderer(TileMetalFormer.class, new RenderMetalFormer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileAnvil.class, new RenderAnvil());
    }

    private static final Map<String, ModelResourceLocation> modelPrtCache = Maps.newHashMap();

    private void clearCache(String... strings)
    {
        if (strings == null)
        {
            modelPrtCache.clear();
            return;
        }

        for (String string : strings)
        {
            modelPrtCache.remove(string);
        }
    }

    private static ModelResourceLocation mkModelPtr(ResourceLocation of, String variant)
    {
        return mkModelPtr(of, variant, false);
    }

    private static ModelResourceLocation mkModelPtr(IForgeRegistryEntry of, String variant)
    {
        return mkModelPtr(of, variant, false);
    }

    private static ModelResourceLocation mkModelPtr(IForgeRegistryEntry of, String variant, boolean cache)
    {
        return mkModelPtr(of.getRegistryName(), variant, cache);
    }

    private static ModelResourceLocation mkModelPtr(ResourceLocation of, String variant, boolean cache)
    {
        ModelResourceLocation ret;
        if (cache)
        {
            if (modelPrtCache.containsKey(of.toString()))
            {
                return modelPrtCache.get(of.toString());
            }
            else
            {
                ret = new ModelResourceLocation(of, variant);
                modelPrtCache.put(of.toString(), ret);
            }
        }
        else
        {
            ret = new ModelResourceLocation(of, variant);
        }

        return ret;
    }

    @Override
    public EntityPlayer getClientPlayer()
    {
        return Minecraft.getMinecraft().player;
    }

    @Override
    public String translate(String key, Object... values)
    {
        return I18n.format(key, values);
    }

    @Override
    public void spawnParticleGlow(World world, float x, float y, float z, float mx, float my, float mz, float r, float g, float b, float scale, int lifetime)
    {
        ParticleUtil.spawnParticleGlow(world, x, y, z, mx, my, mz, r, g, b, scale, lifetime);
    }

    @Override
    public void spawnParticleSpark(World world, float x, float y, float z, float mx, float my, float mz, float r, float g, float b, float scale, int lifetime)
    {
        ParticleUtil.spawnParticleSpark(world, x, y, z, mx, my, mz, r, g, b, scale, lifetime);
    }
}
