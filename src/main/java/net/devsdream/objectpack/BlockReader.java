package net.devsdream.objectpack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.JsonOps;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.devsdream.util.ChromaJsonHelper;
import net.devsdream.util.PublicConstructors;
import net.minecraft.block.AmethystBlock;
import net.minecraft.block.AmethystClusterBlock;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BambooBlock;
import net.minecraft.block.BambooSaplingBlock;
import net.minecraft.block.BannerBlock;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.BeaconBlock;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.BeetrootsBlock;
import net.minecraft.block.BellBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BrewingStandBlock;
import net.minecraft.block.BubbleColumnBlock;
import net.minecraft.block.BuddingAmethystBlock;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.CandleBlock;
import net.minecraft.block.CarpetBlock;
import net.minecraft.block.CarrotsBlock;
import net.minecraft.block.CauldronBlock;
import net.minecraft.block.CaveVinesBodyBlock;
import net.minecraft.block.CaveVinesHeadBlock;
import net.minecraft.block.ChainBlock;
import net.minecraft.block.ChorusPlantBlock;
import net.minecraft.block.CobwebBlock;
import net.minecraft.block.CocoaBlock;
import net.minecraft.block.CommandBlock;
import net.minecraft.block.ComparatorBlock;
import net.minecraft.block.ComposterBlock;
import net.minecraft.block.ConcretePowderBlock;
import net.minecraft.block.ConduitBlock;
import net.minecraft.block.CoralBlockBlock;
import net.minecraft.block.CryingObsidianBlock;
import net.minecraft.block.DaylightDetectorBlock;
import net.minecraft.block.DetectorRailBlock;
import net.minecraft.block.DragonEggBlock;
import net.minecraft.block.DropperBlock;
import net.minecraft.block.EndPortalFrameBlock;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.FireBlock;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.FrostedIceBlock;
import net.minecraft.block.GlassBlock;
import net.minecraft.block.GlazedTerracottaBlock;
import net.minecraft.block.GlowLichenBlock;
import net.minecraft.block.GourdBlock;
import net.minecraft.block.GrassBlock;
import net.minecraft.block.GravelBlock;
import net.minecraft.block.HayBlock;
import net.minecraft.block.HoneyBlock;
import net.minecraft.block.HopperBlock;
import net.minecraft.block.IceBlock;
import net.minecraft.block.InfestedBlock;
import net.minecraft.block.LanternBlock;
import net.minecraft.block.LavaCauldronBlock;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.LightBlock;
import net.minecraft.block.LightningRodBlock;
import net.minecraft.block.MagmaBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.MossBlock;
import net.minecraft.block.MushroomBlock;
import net.minecraft.block.MushroomPlantBlock;
import net.minecraft.block.MyceliumBlock;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.block.NetherrackBlock;
import net.minecraft.block.NoteBlock;
import net.minecraft.block.ObserverBlock;
import net.minecraft.block.OreBlock;
import net.minecraft.block.OxidizableBlock;
import net.minecraft.block.OxidizableSlabBlock;
import net.minecraft.block.OxidizableStairsBlock;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.PistonExtensionBlock;
import net.minecraft.block.PistonHeadBlock;
import net.minecraft.block.PointedDripstoneBlock;
import net.minecraft.block.PotatoesBlock;
import net.minecraft.block.PowderSnowBlock;
import net.minecraft.block.PowderSnowCauldronBlock;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.block.RedstoneBlock;
import net.minecraft.block.RedstoneLampBlock;
import net.minecraft.block.RedstoneOreBlock;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.block.RootedDirtBlock;
import net.minecraft.block.RotatedInfestedBlock;
import net.minecraft.block.SandBlock;
import net.minecraft.block.Oxidizable.OxidizationLevel;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTables;
import net.minecraft.server.function.CommandFunctionManager;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.HugeFungusFeatureConfig;

public class BlockReader {

    public static Map<Identifier, BlockSoundGroup> putVanillaSoundGroup(Map<Identifier, BlockSoundGroup> map,
            String name, BlockSoundGroup soundGroup) {
        map.put(new Identifier(Identifier.DEFAULT_NAMESPACE, name), soundGroup);
        return map;
    }

    private static Map<String, MapColor> mapMapColors() {
        Map<String, MapColor> map = new HashMap<String, MapColor>();
        map.put("clear", MapColor.CLEAR);
        map.put("pale_green", MapColor.PALE_GREEN);
        map.put("pale_yellow", MapColor.PALE_YELLOW);
        map.put("white_gray", MapColor.WHITE_GRAY);
        map.put("bright_red", MapColor.BRIGHT_RED);
        map.put("pale_purple", MapColor.PALE_PURPLE);
        map.put("iron_gray", MapColor.IRON_GRAY);
        map.put("dark_green", MapColor.DARK_GREEN);
        map.put("white", MapColor.WHITE);
        map.put("light_blue_gray", MapColor.LIGHT_BLUE_GRAY);
        map.put("dirt_brown", MapColor.DIRT_BROWN);
        map.put("stone_gray", MapColor.STONE_GRAY);
        map.put("water_blue", MapColor.WATER_BLUE);
        map.put("oak_tan", MapColor.OAK_TAN);
        map.put("off_white", MapColor.OFF_WHITE);
        map.put("orange", MapColor.ORANGE);
        map.put("magenta", MapColor.MAGENTA);
        map.put("light_blue", MapColor.LIGHT_BLUE);
        map.put("yellow", MapColor.YELLOW);
        map.put("lime", MapColor.LIME);
        map.put("pink", MapColor.PINK);
        map.put("gray", MapColor.GRAY);
        map.put("light_gray", MapColor.LIGHT_GRAY);
        map.put("cyan", MapColor.CYAN);
        map.put("purple", MapColor.PURPLE);
        map.put("blue", MapColor.BLUE);
        map.put("brown", MapColor.BROWN);
        map.put("green", MapColor.GREEN);
        map.put("red", MapColor.RED);
        map.put("black", MapColor.BLACK);
        map.put("gold", MapColor.GOLD);
        map.put("diamond_blue", MapColor.DIAMOND_BLUE);
        map.put("lapis_blue", MapColor.LAPIS_BLUE);
        map.put("emerald_green", MapColor.EMERALD_GREEN);
        map.put("spruce_brown", MapColor.SPRUCE_BROWN);
        map.put("dark_red", MapColor.DARK_RED);
        map.put("terracotta_white", MapColor.TERRACOTTA_WHITE);
        map.put("terracotta_orange", MapColor.TERRACOTTA_ORANGE);
        map.put("terracotta_magenta", MapColor.TERRACOTTA_MAGENTA);
        map.put("terracotta_light_blue", MapColor.TERRACOTTA_LIGHT_BLUE);
        map.put("terracotta_yellow", MapColor.TERRACOTTA_YELLOW);
        map.put("terracotta_lime", MapColor.TERRACOTTA_LIME);
        map.put("terracotta_pink", MapColor.TERRACOTTA_PINK);
        map.put("terracotta_gray", MapColor.TERRACOTTA_GRAY);
        map.put("terracotta_light_gray", MapColor.TERRACOTTA_LIGHT_GRAY);
        map.put("terracotta_cyan", MapColor.TERRACOTTA_CYAN);
        map.put("terracotta_purple", MapColor.TERRACOTTA_PURPLE);
        map.put("terracotta_blue", MapColor.TERRACOTTA_BLUE);
        map.put("terracotta_brown", MapColor.TERRACOTTA_BROWN);
        map.put("terracotta_green", MapColor.TERRACOTTA_BLUE);
        map.put("terracotta_red", MapColor.TERRACOTTA_RED);
        map.put("terracotta_black", MapColor.TERRACOTTA_BLACK);
        map.put("dull_red", MapColor.DULL_RED);
        map.put("dull_pink", MapColor.DULL_PINK);
        map.put("dark_crimson", MapColor.DARK_CRIMSON);
        map.put("teal", MapColor.TEAL);
        map.put("dark_aqua", MapColor.DARK_AQUA);
        map.put("dark_dull_pink", MapColor.DARK_DULL_PINK);
        map.put("bright_teal", MapColor.BRIGHT_TEAL);
        map.put("deepslate_gray", MapColor.DEEPSLATE_GRAY);
        map.put("raw_iron_pink", MapColor.RAW_IRON_PINK);
        map.put("lichen_green", MapColor.LICHEN_GREEN);
        return map;
    }

    private static Map<String, PistonBehavior> mapPistonBehaviors() {
        Map<String, PistonBehavior> map = new HashMap<String, PistonBehavior>();
        map.put("block", PistonBehavior.BLOCK);
        map.put("destroy", PistonBehavior.DESTROY);
        map.put("ignore", PistonBehavior.IGNORE);
        map.put("normal", PistonBehavior.NORMAL);
        map.put("push_only", PistonBehavior.PUSH_ONLY);
        return map;
    }

    private static Map<Identifier, BlockSoundGroup> mapSoundGroups() {
        Map<Identifier, BlockSoundGroup> map = new HashMap<Identifier, BlockSoundGroup>();
        putVanillaSoundGroup(map, "wood", BlockSoundGroup.WOOD);
        putVanillaSoundGroup(map, "gravel", BlockSoundGroup.GRAVEL);
        putVanillaSoundGroup(map, "grass", BlockSoundGroup.GRASS);
        putVanillaSoundGroup(map, "lily_pad", BlockSoundGroup.LILY_PAD);
        putVanillaSoundGroup(map, "stone", BlockSoundGroup.STONE);
        putVanillaSoundGroup(map, "metal", BlockSoundGroup.METAL);
        putVanillaSoundGroup(map, "glass", BlockSoundGroup.GLASS);
        putVanillaSoundGroup(map, "wool", BlockSoundGroup.WOOL);
        putVanillaSoundGroup(map, "sand", BlockSoundGroup.SAND);
        putVanillaSoundGroup(map, "snow", BlockSoundGroup.SNOW);
        putVanillaSoundGroup(map, "powder_snow", BlockSoundGroup.POWDER_SNOW);
        putVanillaSoundGroup(map, "ladder", BlockSoundGroup.LADDER);
        putVanillaSoundGroup(map, "anvil", BlockSoundGroup.ANVIL);
        putVanillaSoundGroup(map, "slime", BlockSoundGroup.SLIME);
        putVanillaSoundGroup(map, "honey", BlockSoundGroup.HONEY);
        putVanillaSoundGroup(map, "wet_grass", BlockSoundGroup.WET_GRASS);
        putVanillaSoundGroup(map, "coral", BlockSoundGroup.CORAL);
        putVanillaSoundGroup(map, "bamboo", BlockSoundGroup.BAMBOO);
        putVanillaSoundGroup(map, "bamboo_sapling", BlockSoundGroup.BAMBOO_SAPLING);
        putVanillaSoundGroup(map, "scaffolding", BlockSoundGroup.SCAFFOLDING);
        putVanillaSoundGroup(map, "sweet_berry_bush", BlockSoundGroup.SWEET_BERRY_BUSH);
        putVanillaSoundGroup(map, "crop", BlockSoundGroup.CROP);
        putVanillaSoundGroup(map, "stem", BlockSoundGroup.STEM);
        putVanillaSoundGroup(map, "vine", BlockSoundGroup.VINE);
        putVanillaSoundGroup(map, "nether_wart", BlockSoundGroup.NETHER_WART);
        putVanillaSoundGroup(map, "lantern", BlockSoundGroup.LANTERN);
        putVanillaSoundGroup(map, "nether_stem", BlockSoundGroup.NETHER_STEM);
        putVanillaSoundGroup(map, "nylium", BlockSoundGroup.NYLIUM);
        putVanillaSoundGroup(map, "fungus", BlockSoundGroup.FUNGUS);
        putVanillaSoundGroup(map, "roots", BlockSoundGroup.ROOTS);
        putVanillaSoundGroup(map, "shroomlight", BlockSoundGroup.SHROOMLIGHT);
        putVanillaSoundGroup(map, "weeping_vines", BlockSoundGroup.WEEPING_VINES);
        putVanillaSoundGroup(map, "weeping_vines_low_pitch", BlockSoundGroup.WEEPING_VINES_LOW_PITCH);
        putVanillaSoundGroup(map, "soul_sand", BlockSoundGroup.SOUL_SAND);
        putVanillaSoundGroup(map, "soul_soil", BlockSoundGroup.SOUL_SOIL);
        putVanillaSoundGroup(map, "basalt", BlockSoundGroup.BASALT);
        putVanillaSoundGroup(map, "wart_block", BlockSoundGroup.WART_BLOCK);
        putVanillaSoundGroup(map, "netherrack", BlockSoundGroup.NETHERRACK);
        putVanillaSoundGroup(map, "nether_bricks", BlockSoundGroup.NETHER_BRICKS);
        putVanillaSoundGroup(map, "nether_sprouts", BlockSoundGroup.NETHER_SPROUTS);
        putVanillaSoundGroup(map, "nether_ore", BlockSoundGroup.NETHER_ORE);
        putVanillaSoundGroup(map, "bone", BlockSoundGroup.BONE);
        putVanillaSoundGroup(map, "netherite", BlockSoundGroup.NETHERITE);
        putVanillaSoundGroup(map, "ancient_debris", BlockSoundGroup.ANCIENT_DEBRIS);
        putVanillaSoundGroup(map, "lodestone", BlockSoundGroup.LODESTONE);
        putVanillaSoundGroup(map, "chain", BlockSoundGroup.CHAIN);
        putVanillaSoundGroup(map, "nether_gold_ore", BlockSoundGroup.NETHER_GOLD_ORE);
        putVanillaSoundGroup(map, "gilded_blackstone", BlockSoundGroup.GILDED_BLACKSTONE);
        putVanillaSoundGroup(map, "candle", BlockSoundGroup.CANDLE);
        putVanillaSoundGroup(map, "amethyst_block", BlockSoundGroup.AMETHYST_BLOCK);
        putVanillaSoundGroup(map, "amethyst_cluster", BlockSoundGroup.AMETHYST_CLUSTER);
        putVanillaSoundGroup(map, "small_amethyst_bud", BlockSoundGroup.SMALL_AMETHYST_BUD);
        putVanillaSoundGroup(map, "medium_amethyst_bud", BlockSoundGroup.MEDIUM_AMETHYST_BUD);
        putVanillaSoundGroup(map, "large_amethyst_bud", BlockSoundGroup.LARGE_AMETHYST_BUD);
        putVanillaSoundGroup(map, "tuff", BlockSoundGroup.TUFF);
        putVanillaSoundGroup(map, "calcite", BlockSoundGroup.CALCITE);
        putVanillaSoundGroup(map, "dripstone_block", BlockSoundGroup.DRIPSTONE_BLOCK);
        putVanillaSoundGroup(map, "pointed_dripstone", BlockSoundGroup.POINTED_DRIPSTONE);
        putVanillaSoundGroup(map, "copper", BlockSoundGroup.COPPER);
        putVanillaSoundGroup(map, "cave_vines", BlockSoundGroup.CAVE_VINES);
        putVanillaSoundGroup(map, "spore_blossom", BlockSoundGroup.SPORE_BLOSSOM);
        putVanillaSoundGroup(map, "azalea", BlockSoundGroup.AZALEA);
        putVanillaSoundGroup(map, "flowering_azalea", BlockSoundGroup.FLOWERING_AZALEA);
        putVanillaSoundGroup(map, "moss_carpet", BlockSoundGroup.MOSS_CARPET);
        putVanillaSoundGroup(map, "moss_block", BlockSoundGroup.MOSS_BLOCK);
        putVanillaSoundGroup(map, "big_dripleaf", BlockSoundGroup.BIG_DRIPLEAF);
        putVanillaSoundGroup(map, "small_dripleaf", BlockSoundGroup.SMALL_DRIPLEAF);
        putVanillaSoundGroup(map, "rooted_dirt", BlockSoundGroup.ROOTED_DIRT);
        putVanillaSoundGroup(map, "hanging_roots", BlockSoundGroup.HANGING_ROOTS);
        putVanillaSoundGroup(map, "azalea_leaves", BlockSoundGroup.AZALEA_LEAVES);
        putVanillaSoundGroup(map, "sculk_sensor", BlockSoundGroup.SCULK_SENSOR);
        putVanillaSoundGroup(map, "glow_lichen", BlockSoundGroup.GLOW_LICHEN);
        putVanillaSoundGroup(map, "deepslate", BlockSoundGroup.DEEPSLATE);
        putVanillaSoundGroup(map, "deepslate_bricks", BlockSoundGroup.DEEPSLATE_BRICKS);
        putVanillaSoundGroup(map, "deepslate_tiles", BlockSoundGroup.DEEPSLATE_TILES);
        putVanillaSoundGroup(map, "polished_deepslate", BlockSoundGroup.POLISHED_DEEPSLATE);
        return map;
    }

    private static Map<String, OxidizationLevel> mapOxidizationLevels() {
        Map<String, OxidizationLevel> map = new HashMap<String, OxidizationLevel>();
        map.put("unaffected", OxidizationLevel.UNAFFECTED);
        map.put("exposed", OxidizationLevel.EXPOSED);
        map.put("weathered", OxidizationLevel.WEATHERED);
        map.put("oxidized", OxidizationLevel.OXIDIZED);
        return map;
    }

    public static BlockSoundGroup readSoundGroup(JsonObject object) {
        return new BlockSoundGroup(JsonHelper.getFloat(object, "volume"), JsonHelper.getFloat(object, "pitch"),
                ChromaJsonHelper.getSound(object, "break"), ChromaJsonHelper.getSound(object, "step"),
                ChromaJsonHelper.getSound(object, "place"), ChromaJsonHelper.getSound(object, "hit"),
                ChromaJsonHelper.getSound(object, "fall"));
    }

    public static final Map<Identifier, BlockSoundGroup> soundGroups = mapSoundGroups();
    public static final Map<String, MapColor> mapColors = mapMapColors();
    public static final Map<String, PistonBehavior> pistonBehaviors = mapPistonBehaviors();

    public static Material readMaterial(JsonObject object) throws JsonSyntaxException {
        MapColor mapColor = mapColors.get(JsonHelper.getString(object, "map_color"));
        if (mapColor == null)
            throw new JsonSyntaxException("Unknown map color '" + object.get("map_color").getAsString() + "'");

        PistonBehavior pistonBehavior = pistonBehaviors
                .get(ChromaJsonHelper.getStringOrDefault(object, "piston_behavior", "normal"));
        if (pistonBehavior == null)
            throw new JsonSyntaxException(
                    "Unknown piston behavior '" + object.get("piston_behavior").getAsString() + "'");

        return new Material(mapColor, ChromaJsonHelper.getBooleanOrDefault(object, "liquid", false),
                ChromaJsonHelper.getBooleanOrDefault(object, "solid", true),
                ChromaJsonHelper.getBooleanOrDefault(object, "blocks_movement", true),
                ChromaJsonHelper.getBooleanOrDefault(object, "blocks_light", true),
                ChromaJsonHelper.getBooleanOrDefault(object, "burnable", false),
                ChromaJsonHelper.getBooleanOrDefault(object, "replaceable", false), pistonBehavior);
    }

    private static List<EntityType<?>> entityList;

    private static Boolean allowSpawningTrue(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
        return true;
    }

    private static Boolean allowSpawningFalse(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
        return false;
    }

    private static Boolean allowSpawningList(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
        return entityList.contains(type);
    }

    private static boolean falseContextPredicate(BlockState state, BlockView world, BlockPos pos) {
        return false;
    }

    private static boolean trueContextPredicate(BlockState state, BlockView world, BlockPos pos) {
        return false;
    }

    public static Block.Settings readSettings(JsonObject object, Map<Identifier, Material> materialMap,
            Map<Identifier, BlockSoundGroup> soundGroupMap) throws JsonSyntaxException {
        Material material;
        Identifier materialIdentifier = new Identifier(JsonHelper.getString(object, "material"));
        if (materialMap.get(materialIdentifier) != null) {
            material = materialMap.get(materialIdentifier);
        } else {
            throw new JsonSyntaxException("Unknown material '" + materialIdentifier.toString() + "'");
        }
        Block.Settings settings = Block.Settings.of(material);

        MapColor color = material.getColor();
        if (JsonHelper.hasElement(object, "map_color")) {
            color = mapColors.get(JsonHelper.getString(object, "map_color"));
            if (color == null)
                throw new JsonSyntaxException("Unknown map color '" + object.get("map_color").getAsString() + "'");
        }

        settings.mapColor(color);

        boolean flag = false;

        if (!ChromaJsonHelper.getBooleanOrDefault(object, "collidable", true)) {
            settings.noCollision();
            flag = true;
        }

        if (!ChromaJsonHelper.getBooleanOrDefault(object, "opaque", true)) {
            settings.nonOpaque();
        } else if (flag) {
            throw new JsonSyntaxException("Block cannot be opaque without collision");
        }

        if (ChromaJsonHelper.getBooleanOrDefault(object, "air", false))
            settings.air();

        if (object.has("allows_spawning")) {
            JsonElement allowsSpawning = object.get("allows_spawning");
            if (allowsSpawning.isJsonArray()) {
                entityList = new ArrayList<EntityType<?>>();
                JsonHelper.getArray(object, "allows_spawning").iterator().forEachRemaining(element -> {
                    entityList.add(ChromaJsonHelper.asEntity(element, "entity type"));
                });
                settings.allowsSpawning(BlockReader::allowSpawningList);
            } else if (JsonHelper.isString(allowsSpawning)) {
                entityList = new ArrayList<EntityType<?>>();
                entityList.add(ChromaJsonHelper.getEntity(object, "allows_spawning"));
                settings.allowsSpawning(BlockReader::allowSpawningList);
            } else if (JsonHelper.isBoolean(allowsSpawning)) {
                if (JsonHelper.getBoolean(object, "allows_spawning")) {
                    settings.allowsSpawning(BlockReader::allowSpawningTrue);
                } else {
                    settings.allowsSpawning(BlockReader::allowSpawningFalse);
                }
            } else {
                throw new JsonSyntaxException(
                        "Expected 'allows_spawning' to be a Boolean or an array of entity types, was "
                                + JsonHelper.getType(allowsSpawning));
            }
        }

        if (ChromaJsonHelper.getBooleanOrDefault(object, "air", false))
            settings.air();

        if (JsonHelper.hasElement(object, "blocks_vision")) {
            if (JsonHelper.asBoolean(object.get("blocks_vision"), "'blocks_vision'")) {
                settings.blockVision(BlockReader::trueContextPredicate);
            } else {
                settings.blockVision(BlockReader::falseContextPredicate);
            }
        }

        if (JsonHelper.hasElement(object, "drops")) {
            JsonElement dropsLike = object.get("drops");
            if (Identifier.tryParse(dropsLike.getAsString()) == LootTables.EMPTY) {
                settings.dropsNothing();
            } else {
                settings.dropsLike(ChromaJsonHelper.asBlock(dropsLike, "drops"));
            }
        }

        if (ChromaJsonHelper.getBooleanOrDefault(object, "dynamic_bounds", false))
            settings.dynamicBounds();

        if (ChromaJsonHelper.getBooleanOrDefault(object, "emissive_lighting", false))
            settings.emissiveLighting(BlockReader::falseContextPredicate);

        if (object.has("hardness"))
            settings.hardness(JsonHelper.asFloat(object.get("hardness"), "hardness"));

        if (object.has("resistance"))
            settings.resistance(JsonHelper.asFloat(object.get("resistance"), "resistance"));

        settings.jumpVelocityMultiplier(ChromaJsonHelper.getFloatOrDefault(object, "jump_velocity_multiplier", 1.0F));

        settings.luminance((state) -> {
            return ChromaJsonHelper.getIntOrDefault(object, "luminance", 0);
        });

        if (object.has("post_process")) {
            if (JsonHelper.asBoolean(object.get("post_process"), "post process")) {
                settings.postProcess(BlockReader::trueContextPredicate);
            } else {
                settings.postProcess(BlockReader::falseContextPredicate);
            }
        }

        if (ChromaJsonHelper.getBooleanOrDefault(object, "requires_tool", false))
            settings.requiresTool();

        settings.slipperiness(ChromaJsonHelper.getFloatOrDefault(object, "slipperiness", 0.6F));

        if (object.has("solid_block")) {
            if (JsonHelper.asBoolean(object.get("solid_block"), "solid block")) {
                settings.solidBlock(BlockReader::trueContextPredicate);
            } else {
                settings.solidBlock(BlockReader::falseContextPredicate);
            }
        }

        BlockSoundGroup soundGroup = BlockSoundGroup.STONE;
        Identifier soundGroupIdentifier = new Identifier(JsonHelper.getString(object, "sounds"));
        if (soundGroupMap.get(soundGroupIdentifier) != null) {
            soundGroup = soundGroupMap.get(soundGroupIdentifier);
        } else {
            throw new JsonSyntaxException("Unknown sound group '" + soundGroupIdentifier.toString() + "'");
        }
        settings.sounds(soundGroup);

        if (object.has("suffocates")) {
            if (JsonHelper.asBoolean(object.get("suffocates"), "suffocates")) {
                settings.suffocates(BlockReader::trueContextPredicate);
            } else {
                settings.suffocates(BlockReader::falseContextPredicate);
            }
        }

        if (ChromaJsonHelper.getBooleanOrDefault(object, "tick_randomly", false))
            settings.ticksRandomly();

        settings.velocityMultiplier(ChromaJsonHelper.getFloatOrDefault(object, "velocity_multiplier", 1.0F));

        return settings;
    }

    private static Block fromBlockType(JsonObject object, Block.Settings settings, BlockType type)
            throws JsonSyntaxException {
        if (type.getIdentifier().getNamespace() == Identifier.DEFAULT_NAMESPACE) {
            switch (type.getIdentifier().getPath()) {
                case "simple": {
                    return new Block(settings);
                }
                case "air": {
                    return new PublicConstructors.PublicAirBlock(settings);
                }
                case "amethyst": {
                    return new AmethystBlock(settings);
                }
                case "amethyst_cluster": {
                    return new AmethystClusterBlock(ChromaJsonHelper.getIntOrDefault(object, "height", 0),
                            ChromaJsonHelper.getIntOrDefault(object, "x_z_offset", 1), settings);
                }
                case "anvil": {
                    return new AnvilBlock(settings);
                }
                case "attached_stem": {
                    Block gourd = ChromaJsonHelper.getBlock(object, "gourd_block");
                    if (!(gourd instanceof GourdBlock)) {
                        throw new JsonSyntaxException("Expected a block of type minecraft:gourd");
                    } else {
                        return new PublicConstructors.PublicAttachedStemBlock((GourdBlock) gourd, () -> {
                            return ChromaJsonHelper.getItemOrDefault(object, "pick_block_item", Items.AIR);
                        }, settings);
                    }
                }
                case "azalea": {
                    return new PublicConstructors.PublicAzaleaBlock(settings);
                }
                case "bamboo": {
                    return new BambooBlock(settings);
                }
                case "bamboo_sapling": {
                    return new BambooSaplingBlock(settings);
                }
                case "banner": {
                    return new BannerBlock(ChromaJsonHelper.getDyeColor(object, "dye_color"), settings);
                }
                case "barrel": {
                    return new BarrelBlock(settings);
                }
                case "barrier": {
                    return new PublicConstructors.PublicBarrierBlock(settings);
                }
                case "beacon": {
                    return new BeaconBlock(settings);
                }
                case "bed": {
                    return new BedBlock(ChromaJsonHelper.getDyeColor(object, "color"), settings);
                }
                case "beehive": {
                    return new BeehiveBlock(settings);
                }
                case "beetroots": {
                    return new BeetrootsBlock(settings);
                }
                case "bell": {
                    return new BellBlock(settings);
                }
                case "big_dripleaf": {
                    return new PublicConstructors.PublicBigDripleafBlock(settings);
                }
                case "big_dripleaf_stem": {
                    return new PublicConstructors.PublicBigDripleafStemBlock(settings);
                }
                case "blast_furnace": {
                    return new PublicConstructors.PublicBlastFurnaceBlock(settings);
                }
                case "brewing_stand": {
                    return new BrewingStandBlock(settings);
                }
                case "bubble_column": {
                    return new BubbleColumnBlock(settings);
                }
                case "budding_amethyst": {
                    return new BuddingAmethystBlock(settings);
                }
                case "cactus": {
                    return new PublicConstructors.PublicCactusBlock(settings);
                }
                case "cake": {
                    return new PublicConstructors.PublicCakeBlock(settings);
                }
                case "campfire": {
                    return new CampfireBlock(ChromaJsonHelper.getBooleanOrDefault(object, "emits_particles", true),
                            JsonHelper.getInt(object, "fire_damage"), settings);
                }
                case "candle": {
                    return new CandleBlock(settings);
                }
                case "candle_cake": {
                    return new PublicConstructors.PublicCandleCakeBlock(ChromaJsonHelper.getBlock(object, "candle"),
                            settings);
                }
                case "carpet": {
                    return new CarpetBlock(settings);
                }
                case "carrots": {
                    return new CarrotsBlock(settings);
                }
                case "cartography_table": {
                    return new PublicConstructors.PublicCartographyTableBlock(settings);
                }
                case "carved_pumpkin": {
                    return new PublicConstructors.PublicCarvedPumpkinBlock(settings);
                }
                case "cauldron": {
                    return new CauldronBlock(settings);
                }
                case "cave_vines_body": {
                    return new CaveVinesBodyBlock(settings);
                }
                case "cave_vines_head": {
                    return new CaveVinesHeadBlock(settings);
                }
                case "chain": {
                    return new ChainBlock(settings);
                }
                case "chest": {
                    BlockEntityType<?> blockEntityType = ChromaJsonHelper.getBlockEntity(object, "chest_block_entity");
                    if (blockEntityType.instantiate(null, null) instanceof ChestBlockEntity) {
                        return new PublicConstructors.PublicChestBlock(settings, () -> {
                            return (BlockEntityType<ChestBlockEntity>) blockEntityType;
                        });
                    } else {
                        throw new JsonSyntaxException("Block entity must be of a chest type");
                    }
                }
                case "chorus_flower": {
                    Block plant = ChromaJsonHelper.getBlock(object, "chorus_plant");
                    if (!(plant instanceof ChorusPlantBlock)) {
                        throw new JsonSyntaxException("Expected a block of type minecraft:chorus_plant");
                    } else {
                        return new PublicConstructors.PublicChorusFlowerBlock((ChorusPlantBlock) plant, settings);
                    }
                }
                case "chorus_plant": {
                    return new PublicConstructors.PublicChorusPlantBlock(settings);
                }
                case "cobweb": {
                    return new CobwebBlock(settings);
                }
                case "cocoa": {
                    return new CocoaBlock(settings);
                }
                case "command": {
                    return new CommandBlock(settings, ChromaJsonHelper.getBooleanOrDefault(object, "auto", false));
                }
                case "comparator": {
                    return new ComparatorBlock(settings);
                }
                case "composter": {
                    return new ComposterBlock(settings);
                }
                case "concrete_powder": {
                    return new ConcretePowderBlock(ChromaJsonHelper.getBlock(object, "hardened"), settings);
                }
                case "conduit": {
                    return new ConduitBlock(settings);
                }
                case "connecting": {
                    return new PublicConstructors.PublicConnectingBlock(
                            ChromaJsonHelper.getFloatOrDefault(object, "radius", 0), settings);
                }
                case "coral": {
                    return new PublicConstructors.PublicCoralBlock(
                            ChromaJsonHelper.getBlock(object, "dead_coral_block"), settings);
                }
                case "coral_block": {
                    return new CoralBlockBlock(ChromaJsonHelper.getBlock(object, "dead_coral_block"), settings);
                }
                case "coral_fan": {
                    return new PublicConstructors.PublicCoralBlock(
                            ChromaJsonHelper.getBlock(object, "dead_coral_block"), settings);
                }
                case "coral_parent": {
                    return new PublicConstructors.PublicCoralParentBlock(settings);
                }
                case "coral_wall_fan": {
                    return new PublicConstructors.PublicCoralBlock(
                            ChromaJsonHelper.getBlock(object, "dead_coral_block"), settings);
                }
                case "crafting_table": {
                    return new PublicConstructors.PublicCraftingTableBlock(settings);
                }
                case "crop": {
                    return new PublicConstructors.PublicCropBlock(settings);
                }
                case "crying_obsidian": {
                    return new CryingObsidianBlock(settings);
                }
                case "daylight_detector": {
                    return new DaylightDetectorBlock(settings);
                }
                case "dead_bush": {
                    return new PublicConstructors.PublicDeadBushBlock(settings);
                }
                case "dead_coral": {
                    return new PublicConstructors.PublicDeadCoralBlock(settings);
                }
                case "dead_coral_fan": {
                    return new PublicConstructors.PublicDeadCoralBlock(settings);
                }
                case "dead_coral_wall_fan": {
                    return new PublicConstructors.PublicDeadCoralWallFanBlock(settings);
                }
                case "detector_rail": {
                    return new DetectorRailBlock(settings);
                }
                case "dirt_path": {
                    return new PublicConstructors.PublicDirtPathBlock(settings);
                }
                case "dispenser": {
                    return new PublicConstructors.PublicDispenserBlock(settings);
                }
                case "door": {
                    return new PublicConstructors.PublicDoorBlock(settings);
                }
                case "dragon_egg": {
                    return new DragonEggBlock(settings);
                }
                case "dropper": {
                    return new DropperBlock(settings);
                }
                case "dyed_carpet": {
                    return new PublicConstructors.PublicDyedCarpetBlock(
                            ChromaJsonHelper.getDyeColor(object, "dye_color"), settings);
                }
                case "enchanting_table": {
                    return new PublicConstructors.PublicEnchantingTableBlock(settings);
                }
                case "end_gateway": {
                    return new PublicConstructors.PublicEndGatewayBlock(settings);
                }
                case "end_portal": {
                    return new PublicConstructors.PublicEndPortalBlock(settings);
                }
                case "end_portal_frame": {
                    return new EndPortalFrameBlock(settings);
                }
                case "end_rod": {
                    return new PublicConstructors.PublicEndRodBlock(settings);
                }
                case "ender_chest": {
                    return new PublicConstructors.PublicEnderChestBlock(settings);
                }
                case "facing": {
                    return new PublicConstructors.PublicFacingBlock(settings);
                }
                case "falling": {
                    return new FallingBlock(settings);
                }
                case "farmland": {
                    return new PublicConstructors.PublicFarmlandBlock(settings);
                }
                case "fence": {
                    return new FenceBlock(settings);
                }
                case "fence_gate": {
                    return new FenceGateBlock(settings);
                }
                case "fern": {
                    return new PublicConstructors.PublicFernBlock(settings);
                }
                case "fire": {
                    return new FireBlock(settings);
                }
                case "fletching_table": {
                    return new PublicConstructors.PublicFletchingTableBlock(settings);
                }
                case "flower": {
                    return new FlowerBlock(ChromaJsonHelper.getEffect(object, "suspicious_stew_effect"),
                            ChromaJsonHelper.getInt(object, "effect_duration"), settings);
                }
                case "flower_pot": {
                    return new FlowerPotBlock(ChromaJsonHelper.getBlockOrDefault(object, "content", Blocks.AIR),
                            settings);
                }
                case "fluid": {
                    Fluid fluid = ChromaJsonHelper.getFluid(object, "fluid");
                    if (fluid instanceof FlowableFluid) {
                        return new PublicConstructors.PublicFluidBlock(
                                (FlowableFluid) ChromaJsonHelper.getFluid(object, "fluid"), settings);
                    } else {
                        throw new JsonSyntaxException("Fluid must be flowable");
                    }
                }
                case "frosted_ice": {
                    return new FrostedIceBlock(settings);
                }
                case "fungus": {
                    return new PublicConstructors.PublicFungusBlock(settings, () -> {
                        return Feature.HUGE_FUNGUS.configure(HugeFungusFeatureConfig.CODEC
                                .parse(JsonOps.INSTANCE, JsonHelper.getObject(object, "huge_fungus"))
                                .getOrThrow(false, (error) -> {
                                    error = new String("Could not parse huge fungus");
                                }));
                    });
                }
                case "furnace": {
                    return new PublicConstructors.PublicFurnaceBlock(settings);
                }
                case "glass": {
                    return new GlassBlock(settings);
                }
                case "glazed_terracotta": {
                    return new GlazedTerracottaBlock(settings);
                }
                case "glow_lichen": {
                    return new GlowLichenBlock(settings);
                }
                case "grass": {
                    return new GrassBlock(settings);
                }
                case "gravel": {
                    return new GravelBlock(settings);
                }
                case "grindstone": {
                    return new PublicConstructors.PublicGrindstoneBlock(settings);
                }
                case "hanging_roots": {
                    return new PublicConstructors.PublicHangingRootsBlock(settings);
                }
                case "hay": {
                    return new HayBlock(settings);
                }
                case "honey": {
                    return new HoneyBlock(settings);
                }
                case "hopper": {
                    return new HopperBlock(settings);
                }
                case "horizontal_connecting": {
                    return new PublicConstructors.PublicHorizontalConnectingBlock(
                            JsonHelper.getFloat(object, "radius_1"), JsonHelper.getFloat(object, "radius_2"),
                            JsonHelper.getFloat(object, "bounding_height_1"),
                            JsonHelper.getFloat(object, "bounding_height_2"),
                            JsonHelper.getFloat(object, "collision_height"), settings);
                }
                case "horizontal_facing": {
                    return new PublicConstructors.PublicHorizontalFacingBlock(settings);
                }
                case "ice": {
                    return new IceBlock(settings);
                }
                case "infested": {
                    return new InfestedBlock(ChromaJsonHelper.getBlock(object, "regular_block"), settings);
                }
                case "jigsaw": {
                    return new PublicConstructors.PublicJigsawBlock(settings);
                }
                case "jukebox": {
                    return new PublicConstructors.PublicJukeboxBlock(settings);
                }
                case "kelp": {
                    return new PublicConstructors.PublicKelpBlock(settings);
                }
                case "kelp_plant": {
                    return new PublicConstructors.PublicKelpPlantBlock(settings);
                }
                case "ladder": {
                    return new PublicConstructors.PublicLadderBlock(settings);
                }
                case "lantern": {
                    return new LanternBlock(settings);
                }
                case "lava_cauldron": {
                    return new LavaCauldronBlock(settings);
                }
                case "leaves": {
                    return new LeavesBlock(settings);
                }
                case "lectern": {
                    return new PublicConstructors.PublicLecternBlock(settings);
                }
                case "leveled_cauldron": {
                    Object2ObjectOpenHashMap<Item, CauldronBehavior> behaviorMap = (Object2ObjectOpenHashMap<Item, CauldronBehavior>) Util
                            .make(new Object2ObjectOpenHashMap<Item, CauldronBehavior>(), (map) -> {
                                map.defaultReturnValue((state, world, pos, player, hand, stack) -> {
                                    return ActionResult.PASS;
                                });
                            });
                    JsonHelper.getArray(object, "cauldron_behaviors").forEach((element) -> {
                        JsonObject behaviorObj = JsonHelper.asObject(element,
                                "cauldron behavior in cauldron_behaviors array");
                        behaviorMap.put(ChromaJsonHelper.getItem(behaviorObj, "item"),
                                (state, world, pos, player, hand, stack) -> {
                                    if (!world.isClient()) {
                                        try {
                                            ChromaJsonHelper.ServerOnly.getAndExecuteFunction(behaviorObj, world,
                                                    "function", player.getCommandSource().withPosition(Vec3d.of(pos)));
                                        } catch (NoSuchElementException e) {
                                        }
                                    }
                                    return ActionResult.success(world.isClient);
                                });
                    });
                    Biome.Precipitation target = Biome.Precipitation.byName(ChromaJsonHelper.getStringOrDefault(object, "precipitation", null));
                    return new LeveledCauldronBlock(settings, (precipitation) -> {
                        return precipitation == target;
                    }, behaviorMap);
                }
                case "lever": {
                    return new PublicConstructors.PublicLeverBlock(settings);
                }
                case "light": {
                    return new LightBlock(settings);
                }
                case "lightning_rod": {
                    return new LightningRodBlock(settings);
                }
                case "lily_pad": {
                    return new PublicConstructors.PublicLilyPadBlock(settings);
                }
                case "loom": {
                    return new PublicConstructors.PublicLoomBlock(settings);
                }
                case "magma": {
                    return new MagmaBlock(settings);
                }
                case "melon": {
                    return new PublicConstructors.PublicMelonBlock(settings);
                }
                case "moss": {
                    return new MossBlock(settings);
                }
                case "mushroom": {
                    return new MushroomBlock(settings);
                }
                case "mushroom_plant": {
                    JsonObject featureObj = JsonHelper.getObject(object, "feature");
                    return new MushroomPlantBlock(settings, () -> {
                        return ChromaJsonHelper.getFeature(featureObj, "id").getCodec()
                                .parse(JsonOps.INSTANCE, JsonHelper.getObject(featureObj, "configuration"))
                                .getOrThrow(false, (error) -> {
                                    error = new String("Could not parse huge fungus");
                                });
                    });
                }
                case "mycelium": {
                    return new MyceliumBlock(settings);
                }
                case "nether_portal": {
                    return new NetherPortalBlock(settings);
                }
                case "nether_wart": {
                    return new PublicConstructors.PublicNetherWartBlock(settings);
                }
                case "netherrack": {
                    return new NetherrackBlock(settings);
                }
                case "note": {
                    return new NoteBlock(settings);
                }
                case "nylium": {
                    return new PublicConstructors.PublicNyliumBlock(settings);
                }
                case "observer": {
                    return new ObserverBlock(settings);
                }
                case "ore": {
                    return new OreBlock(settings);
                }
                case "oxidizable": {
                    OxidizationLevel oxidization = mapOxidizationLevels()
                            .get(JsonHelper.getString(object, "oxidization"));
                    if (oxidization != null) {
                        return new OxidizableBlock(oxidization, settings);
                    } else {
                        throw new JsonSyntaxException(
                                "Unknown oxidization level '" + JsonHelper.getString(object, "oxidization") + "'");
                    }
                }
                case "oxidizable_slab": {
                    OxidizationLevel oxidization = mapOxidizationLevels()
                            .get(JsonHelper.getString(object, "oxidization"));
                    if (oxidization != null) {
                        return new OxidizableSlabBlock(oxidization, settings);
                    } else {
                        throw new JsonSyntaxException(
                                "Unknown oxidization level '" + JsonHelper.getString(object, "oxidization") + "'");
                    }
                }
                case "oxidizable_stairs": {
                    OxidizationLevel oxidization = mapOxidizationLevels()
                            .get(JsonHelper.getString(object, "oxidization"));
                    if (oxidization != null) {
                        return new OxidizableStairsBlock(oxidization,
                                ChromaJsonHelper.getBlock(object, "full_block").getDefaultState(), settings);
                    } else {
                        throw new JsonSyntaxException(
                                "Unknown oxidization level '" + JsonHelper.getString(object, "oxidization") + "'");
                    }
                }
                case "pane": {
                    return new PublicConstructors.PublicPaneBlock(settings);
                }
                case "pillar": {
                    return new PillarBlock(settings);
                }
                case "piston": {
                    return new PistonBlock(ChromaJsonHelper.getBooleanOrDefault(object, "sticky", false), settings);
                }
                case "piston_extension": {
                    return new PistonExtensionBlock(settings);
                }
                case "piston_head": {
                    return new PistonHeadBlock(settings);
                }
                case "plant": {
                    return new PublicConstructors.PublicPlantBlock(settings);
                }
                case "player_skull": {
                    return new PublicConstructors.PublicPlayerSkullBlock(settings);
                }
                case "pointed_dripstone": {
                    return new PointedDripstoneBlock(settings);
                }
                case "potatoes": {
                    return new PotatoesBlock(settings);
                }
                case "powder_snow": {
                    return new PowderSnowBlock(settings);
                }
                case "powder_snow_cauldron": {
                    Object2ObjectOpenHashMap<Item, CauldronBehavior> behaviorMap = (Object2ObjectOpenHashMap<Item, CauldronBehavior>) Util
                            .make(new Object2ObjectOpenHashMap<Item, CauldronBehavior>(), (map) -> {
                                map.defaultReturnValue((state, world, pos, player, hand, stack) -> {
                                    return ActionResult.PASS;
                                });
                            });
                    JsonHelper.getArray(object, "cauldron_behaviors").forEach((element) -> {
                        JsonObject behaviorObj = JsonHelper.asObject(element,
                                "cauldron behavior in cauldron_behaviors array");
                        behaviorMap.put(ChromaJsonHelper.getItem(behaviorObj, "item"),
                                (state, world, pos, player, hand, stack) -> {
                                    if (!world.isClient()) {
                                        try {
                                            ChromaJsonHelper.ServerOnly.getAndExecuteFunction(behaviorObj, world,
                                                    "function", player.getCommandSource().withPosition(Vec3d.of(pos)));
                                        } catch (NoSuchElementException e) {
                                        }
                                    }
                                    return ActionResult.success(world.isClient);
                                });
                    });
                    Biome.Precipitation target = Biome.Precipitation.byName(ChromaJsonHelper.getStringOrDefault(object, "precipitation", null));
                    return new PowderSnowCauldronBlock(settings, (precipitation) -> {
                        return precipitation == target;
                    }, behaviorMap);
                }
                case "powered_rail": {
                    return new PublicConstructors.PublicPoweredRailBlock(settings);
                }
                case "pressure_plate": {
                    PressurePlateBlock.ActivationRule activationRule;
                    switch (ChromaJsonHelper.getStringOrDefault(object, "type", "everything")) {
                        case "everything": {
                            activationRule = PressurePlateBlock.ActivationRule.EVERYTHING;
                            break;
                        }
                        case "mobs": {
                            activationRule = PressurePlateBlock.ActivationRule.MOBS;
                            break;
                        }
                        default: {
                            throw new JsonSyntaxException("Expected either 'everything' or 'mobs' for type");
                        }
                    }
                    return new PublicConstructors.PublicPressurePlateBlock(activationRule, settings);
                }
                case "pumpkin": {
                    return new PublicConstructors.PublicPumpkinBlock(settings);
                }
                case "rail": {
                    return new PublicConstructors.PublicRailBlock(settings);
                }
                case "redstone": {
                    return new RedstoneBlock(settings);
                }
                case "redstone_lamp": {
                    return new RedstoneLampBlock(settings);
                }
                case "redstone_ore": {
                    return new RedstoneOreBlock(settings);
                }
                case "redstone_torch": {
                    return new PublicConstructors.PublicRedstoneTorchBlock(settings);
                }
                case "redstone_wire": {
                    return new RedstoneWireBlock(settings);
                }
                case "repeater": {
                    return new PublicConstructors.PublicRepeaterBlock(settings);
                }
                case "respawn_anchor": {
                    return new RespawnAnchorBlock(settings);
                }
                case "rod": {
                    return new PublicConstructors.PublicRodBlock(settings);
                }
                case "rooted_dirt": {
                    return new RootedDirtBlock(settings);
                }
                case "roots": {
                    return new PublicConstructors.PublicRootsBlock(settings);
                }
                case "rotated_infested": {
                    return new RotatedInfestedBlock(ChromaJsonHelper.getBlock(object, "block"), settings);
                }
                case "sand": {
                    return new SandBlock(JsonHelper.getInt(object, "color"), settings);
                }
                default: {
                    break;
                }
            }
        }
        return new CustomizedBlock(settings, type);
    }

    public static Block readBlock(JsonObject object, Map<Identifier, Material> materialMap,
            Map<Identifier, BlockSoundGroup> soundGroupMap) {
        Block block = fromBlockType(object, readSettings(object, materialMap, soundGroupMap),
                new BlockType(new Identifier(Identifier.DEFAULT_NAMESPACE, "cobweb"), object));
        return block;
    }
}
