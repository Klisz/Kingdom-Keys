package online.kingdomkeys.kingdomkeys.world.dimension;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.LongFunction;
import java.util.stream.Collectors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeRegistry;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.IExtendedNoiseRandom;
import net.minecraft.world.gen.LazyAreaLayerContext;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.area.LazyArea;
import net.minecraft.world.gen.layer.Layer;
import net.minecraft.world.gen.layer.ZoomLayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class DTTHBiomeProvider extends BiomeProvider {
    public static void registerBiomeProvider() {
        Registry.register(Registry.BIOME_PROVIDER_CODEC, new ResourceLocation(KingdomKeys.MODID, "biome_source"), DTTHBiomeProvider.CODEC);
    }

    public static final Codec<DTTHBiomeProvider> CODEC =
            RecordCodecBuilder.create((instance) -> instance.group(
                    RegistryLookupCodec.getLookUpCodec(Registry.BIOME_KEY).forGetter((vanillaLayeredBiomeSource) -> vanillaLayeredBiomeSource.BIOME_REGISTRY))
            .apply(instance, instance.stable(DTTHBiomeProvider::new)));

    public static ResourceLocation HIVE_WALL = new ResourceLocation(KingdomKeys.MODID, "hive_wall");
    public static ResourceLocation HIVE_PILLAR = new ResourceLocation(KingdomKeys.MODID, "hive_pillar");
    public static ResourceLocation SUGAR_WATER_FLOOR = new ResourceLocation(KingdomKeys.MODID, "sugar_water_floor");

   // private final Layer BIOME_SAMPLER;
    private final Registry<Biome> BIOME_REGISTRY;
    public static Registry<Biome> LAYERS_BIOME_REGISTRY;
    public static List<Biome> NONSTANDARD_BIOME = new ArrayList<>();

    public DTTHBiomeProvider(Registry<Biome> biomeRegistry) {
        this(0, biomeRegistry);
    }
    public DTTHBiomeProvider(long seed, Registry<Biome> biomeRegistry) {
        super(biomeRegistry.getEntries().stream()
                .filter(entry -> entry.getKey().getLocation().getNamespace().equals(KingdomKeys.MODID))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList()));

        NONSTANDARD_BIOME = this.biomes.stream()
                .filter(biome -> biomeRegistry.getKey(biome) != HIVE_WALL &&
                                biomeRegistry.getKey(biome) != HIVE_PILLAR &&
                                biomeRegistry.getKey(biome) != SUGAR_WATER_FLOOR)
                .collect(Collectors.toList());

        //BzBiomeLayer.setSeed(seed);
        this.BIOME_REGISTRY = biomeRegistry;
        DTTHBiomeProvider.LAYERS_BIOME_REGISTRY = biomeRegistry;
     //   this.BIOME_SAMPLER = buildWorldProcedure(seed);
    }

   /* public static Layer buildWorldProcedure(long seed) {
        IAreaFactory<LazyArea> layerFactory = build((salt) ->
                new LazyAreaLayerContext(25, seed, salt));
        return new Layer(layerFactory);
    }*/


    /*public static <T extends IArea, C extends IExtendedNoiseRandom<T>> IAreaFactory<T> build(LongFunction<C> contextFactory) {
        IAreaFactory<T> layer = BzBiomeLayer.INSTANCE.apply(contextFactory.apply(200L));
        layer = BzBiomePillarLayer.INSTANCE.apply(contextFactory.apply(1008L), layer);
        layer = BzBiomeScalePillarLayer.INSTANCE.apply(contextFactory.apply(1055L), layer);
        layer = ZoomLayer.FUZZY.apply(contextFactory.apply(2003L), layer);
        layer = ZoomLayer.FUZZY.apply(contextFactory.apply(2523L), layer);

        if(!NONSTANDARD_BIOME.isEmpty()){
            IAreaFactory<T> layerOverlay = BzBiomeNonstandardLayer.INSTANCE.apply(contextFactory.apply(204L));
            layerOverlay = ZoomLayer.NORMAL.apply(contextFactory.apply(2423L), layerOverlay);
            layerOverlay = ZoomLayer.NORMAL.apply(contextFactory.apply(2503L), layerOverlay);
            layerOverlay = ZoomLayer.NORMAL.apply(contextFactory.apply(2603L), layerOverlay);
            layerOverlay = ZoomLayer.FUZZY.apply(contextFactory.apply(2853L), layerOverlay);
            layerOverlay = ZoomLayer.FUZZY.apply(contextFactory.apply(3583L), layerOverlay);
            layer = BzBiomeMergeLayer.INSTANCE.apply(contextFactory.apply(5583L), layerOverlay, layer);
        }

        return layer;
    }*/

    @Override
    public Biome getNoiseBiome(int x, int y, int z) {
        return biomes.get(0);
    }

   /* public Biome sample(Registry<Biome> registry, int i, int j) {
        int k = ((LayerAccessor)this.BIOME_SAMPLER).bz_getSampler().getValue(i, j);
        Biome biome = registry.getByValue(k);
        if (biome == null) {
            if (SharedConstants.developmentMode) {
                throw Util.pauseDevMode(new IllegalStateException("Unknown biome id: " + k));
            } else {
                return registry.getValueForKey(BiomeRegistry.getKeyFromID(0));
            }
        } else {
            return biome;
        }
    }*/

    @Override
    protected Codec<? extends BiomeProvider> getBiomeProviderCodec() {
        return CODEC;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public BiomeProvider getBiomeProvider(long seed) {
        return new DTTHBiomeProvider(seed, this.BIOME_REGISTRY);
    }
}