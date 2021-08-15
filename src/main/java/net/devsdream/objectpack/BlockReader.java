package net.devsdream.objectpack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.JsonOps;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.devsdream.Main;
import net.devsdream.util.ChromaJsonHelper;
import net.devsdream.util.ChromaGeneral.QuadConsumer;
import net.minecraft.block.*;
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
import net.minecraft.particle.ParticleEffect;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.SignType;
import net.minecraft.util.Util;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.HugeFungusFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

public class BlockReader {

    public static Map<Identifier, BlockSoundGroup> putVanillaSoundGroup(Map<Identifier, BlockSoundGroup> map,
            String name, BlockSoundGroup soundGroup) {
        map.put(new Identifier(Identifier.DEFAULT_NAMESPACE, name), soundGroup);
        return map;
    }

    private static Map<String, MapColor> mapMapColors() {
        Map<String, MapColor> map = new HashMap<String, MapColor>();
        map.put("none", MapColor.CLEAR);
        map.put("grass", MapColor.PALE_GREEN);
        map.put("sand", MapColor.PALE_YELLOW);
        map.put("wool", MapColor.WHITE_GRAY);
        map.put("fire", MapColor.BRIGHT_RED);
        map.put("ice", MapColor.PALE_PURPLE);
        map.put("metal", MapColor.IRON_GRAY);
        map.put("plant", MapColor.DARK_GREEN);
        map.put("snow", MapColor.WHITE);
        map.put("clay", MapColor.LIGHT_BLUE_GRAY);
        map.put("dirt", MapColor.DIRT_BROWN);
        map.put("stone", MapColor.STONE_GRAY);
        map.put("water", MapColor.WATER_BLUE);
        map.put("wood", MapColor.OAK_TAN);
        map.put("quartz", MapColor.OFF_WHITE);
        map.put("color_orange", MapColor.ORANGE);
        map.put("color_magenta", MapColor.MAGENTA);
        map.put("color_light_blue", MapColor.LIGHT_BLUE);
        map.put("color_yellow", MapColor.YELLOW);
        map.put("color_lime", MapColor.LIME);
        map.put("color_pink", MapColor.PINK);
        map.put("color_gray", MapColor.GRAY);
        map.put("color_light_gray", MapColor.LIGHT_GRAY);
        map.put("color_cyan", MapColor.CYAN);
        map.put("color_purple", MapColor.PURPLE);
        map.put("color_blue", MapColor.BLUE);
        map.put("color_brown", MapColor.BROWN);
        map.put("color_green", MapColor.GREEN);
        map.put("color_red", MapColor.RED);
        map.put("color_black", MapColor.BLACK);
        map.put("gold", MapColor.GOLD);
        map.put("diamond_blue", MapColor.DIAMOND_BLUE);
        map.put("lapis", MapColor.LAPIS_BLUE);
        map.put("emerald", MapColor.EMERALD_GREEN);
        map.put("podzol", MapColor.SPRUCE_BROWN);
        map.put("nether", MapColor.DARK_RED);
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
        map.put("crimson_nylium", MapColor.DULL_RED);
        map.put("crimson_stem", MapColor.DULL_PINK);
        map.put("crimson_hyphae", MapColor.DARK_CRIMSON);
        map.put("warped_nylium", MapColor.TEAL);
        map.put("warped__stem", MapColor.DARK_AQUA);
        map.put("warped_hyphae", MapColor.DARK_DULL_PINK);
        map.put("warped_wart_block", MapColor.BRIGHT_TEAL);
        map.put("deepslate", MapColor.DEEPSLATE_GRAY);
        map.put("raw_iron", MapColor.RAW_IRON_PINK);
        map.put("glow_lichen", MapColor.LICHEN_GREEN);
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
        putVanillaSoundGroup(map, "slime_block", BlockSoundGroup.SLIME);
        putVanillaSoundGroup(map, "honey_block", BlockSoundGroup.HONEY);
        putVanillaSoundGroup(map, "wet_grass", BlockSoundGroup.WET_GRASS);
        putVanillaSoundGroup(map, "coral_block", BlockSoundGroup.CORAL);
        putVanillaSoundGroup(map, "bamboo", BlockSoundGroup.BAMBOO);
        putVanillaSoundGroup(map, "bamboo_sapling", BlockSoundGroup.BAMBOO_SAPLING);
        putVanillaSoundGroup(map, "scaffolding", BlockSoundGroup.SCAFFOLDING);
        putVanillaSoundGroup(map, "sweet_berry_bush", BlockSoundGroup.SWEET_BERRY_BUSH);
        putVanillaSoundGroup(map, "crop", BlockSoundGroup.CROP);
        putVanillaSoundGroup(map, "hard_crop", BlockSoundGroup.STEM);
        putVanillaSoundGroup(map, "vine", BlockSoundGroup.VINE);
        putVanillaSoundGroup(map, "nether_wart", BlockSoundGroup.NETHER_WART);
        putVanillaSoundGroup(map, "lantern", BlockSoundGroup.LANTERN);
        putVanillaSoundGroup(map, "stem", BlockSoundGroup.NETHER_STEM);
        putVanillaSoundGroup(map, "nylium", BlockSoundGroup.NYLIUM);
        putVanillaSoundGroup(map, "fungus", BlockSoundGroup.FUNGUS);
        putVanillaSoundGroup(map, "roots", BlockSoundGroup.ROOTS);
        putVanillaSoundGroup(map, "shroomlight", BlockSoundGroup.SHROOMLIGHT);
        putVanillaSoundGroup(map, "weeping_vines", BlockSoundGroup.WEEPING_VINES);
        putVanillaSoundGroup(map, "twisting_vines", BlockSoundGroup.WEEPING_VINES_LOW_PITCH);
        putVanillaSoundGroup(map, "soul_sand", BlockSoundGroup.SOUL_SAND);
        putVanillaSoundGroup(map, "soul_soil", BlockSoundGroup.SOUL_SOIL);
        putVanillaSoundGroup(map, "basalt", BlockSoundGroup.BASALT);
        putVanillaSoundGroup(map, "wart_block", BlockSoundGroup.WART_BLOCK);
        putVanillaSoundGroup(map, "netherrack", BlockSoundGroup.NETHERRACK);
        putVanillaSoundGroup(map, "nether_bricks", BlockSoundGroup.NETHER_BRICKS);
        putVanillaSoundGroup(map, "nether_sprouts", BlockSoundGroup.NETHER_SPROUTS);
        putVanillaSoundGroup(map, "nether_ore", BlockSoundGroup.NETHER_ORE);
        putVanillaSoundGroup(map, "bone_block", BlockSoundGroup.BONE);
        putVanillaSoundGroup(map, "netherite_block", BlockSoundGroup.NETHERITE);
        putVanillaSoundGroup(map, "ancient_debris", BlockSoundGroup.ANCIENT_DEBRIS);
        putVanillaSoundGroup(map, "lodestone", BlockSoundGroup.LODESTONE);
        putVanillaSoundGroup(map, "chain", BlockSoundGroup.CHAIN);
        putVanillaSoundGroup(map, "nether_gold_ore", BlockSoundGroup.NETHER_GOLD_ORE);
        putVanillaSoundGroup(map, "gilded_blackstone", BlockSoundGroup.GILDED_BLACKSTONE);
        putVanillaSoundGroup(map, "candle", BlockSoundGroup.CANDLE);
        putVanillaSoundGroup(map, "amethyst", BlockSoundGroup.AMETHYST_BLOCK);
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
        putVanillaSoundGroup(map, "moss", BlockSoundGroup.MOSS_BLOCK);
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

    private static Map<String, SkullBlock.SkullType> mapSkullTypes() throws JsonSyntaxException {
        Map<String, SkullBlock.SkullType> map = new HashMap<String, SkullBlock.SkullType>();
        map.put("skeleton", SkullBlock.Type.SKELETON);
        map.put("wither_skeleton", SkullBlock.Type.WITHER_SKELETON);
        map.put("dragon", SkullBlock.Type.DRAGON);
        map.put("creeper", SkullBlock.Type.CREEPER);
        map.put("zombie", SkullBlock.Type.ZOMBIE);
        map.put("player", SkullBlock.Type.PLAYER);
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
    public static final Map<String, SkullBlock.SkullType> skullTypes = mapSkullTypes();
    public static final Map<String, Oxidizable.OxidizationLevel> weatherStates = mapOxidizationLevels();

    public static Material readMaterial(JsonObject object) throws JsonSyntaxException {
        MapColor mapColor = ChromaJsonHelper.getFromMapSafe(JsonHelper.getString(object, "map_color"), "map color", mapColors);

        PistonBehavior pistonBehavior = ChromaJsonHelper.getFromMapSafe(ChromaJsonHelper.getStringOrDefault(object, "push_reaction", "normal"), "push reaction", pistonBehaviors);

        return new Material(mapColor, ChromaJsonHelper.getBooleanOrDefault(object, "liquid", false),
                ChromaJsonHelper.getBooleanOrDefault(object, "solid", true),
                ChromaJsonHelper.getBooleanOrDefault(object, "blocks_motion", true),
                ChromaJsonHelper.getBooleanOrDefault(object, "solid_blocking", true),
                ChromaJsonHelper.getBooleanOrDefault(object, "flammable", false),
                ChromaJsonHelper.getBooleanOrDefault(object, "replaceable", false), pistonBehavior);
    }

    public static Block.Settings readSettings(JsonObject object, Map<Identifier, Material> materialMap,
            Map<Identifier, BlockSoundGroup> soundGroupMap, JsonObject overallBlock) throws JsonSyntaxException {

        Material material = ChromaJsonHelper
        .getFromMapSafe(new Identifier(JsonHelper.getString(object, "material")), "material", materialMap);

        Block.Settings settings = Block.Settings.of(material);

        Block temp = BlockReader.fromBlockType(overallBlock, settings, new BlockType(
                new Identifier(ChromaJsonHelper.getStringOrDefault(overallBlock, "type", "minecraft:simple")), null));

        MapColor color = material.getColor();
        if (JsonHelper.hasElement(object, "map_color")) {
            List<Block.Settings> out = new ArrayList<Block.Settings>();
            out.add(settings);
            applySettingWithOptions(object, "map_color", settings, temp.getStateManager(), (obj, key) -> {
                return ChromaJsonHelper.getFromMapSafe(JsonHelper.getString(object, "map_color"), "map color", mapColors);
            }, (map, defaultValue, stateManager, set) -> {
                out.set(0, Block.Settings.of(material, (state) -> {
                    return returnOption(map, defaultValue, stateManager, state);
                }));
            }, (set, obj, key, valueSupplier) -> out.set(0,
                    Block.Settings.of(material, valueSupplier.apply(obj, key))));
            settings = out.get(0);
        } else {
            settings.mapColor(color);
        }

        boolean flag = false;

        if (!ChromaJsonHelper.getBooleanOrDefault(object, "collidable", true)) {
            settings.noCollision();
            flag = true;
        }

        if (!ChromaJsonHelper.getBooleanOrDefault(object, "can_occlude", true)) {
            settings.nonOpaque();
        } else if (flag) {
            throw new JsonSyntaxException("Block cannot occlude without collision");
        }

        if (ChromaJsonHelper.getBooleanOrDefault(object, "air", false))
            settings.air();

        if (object.has("valid_spawn")) {
            applySettingWithOptions(object, "valid_spawn", settings, temp.getStateManager(), (obj, key) -> {
                JsonElement allowsSpawning = obj.get(key);
                if (allowsSpawning.isJsonArray()) {
                    List<EntityType<?>> types = new ArrayList<EntityType<?>>();
                    JsonHelper.asArray(allowsSpawning, "JSON array").forEach((entry) -> {
                        types.add(ChromaJsonHelper.asEntity(entry, "entity type"));
                    });
                    return types;
                } else {
                    return JsonHelper.asBoolean(allowsSpawning, "boolean or JSON array");
                }
            }, (map, defaultValue, stateManager, set) -> set.allowsSpawning((state, world, pos, type) -> {
                List<Boolean> result = new ArrayList<Boolean>();
                if (defaultValue instanceof Boolean) {
                    result.add((boolean) defaultValue);
                } else {
                    result.add(((List<EntityType<?>>) defaultValue).contains(type));
                }
                map.forEach((predicate, returnValue) -> {
                    if (predicate.conditions.stream().allMatch((condition) -> condition.test(stateManager, state))) {
                        if (returnValue instanceof Boolean) {
                            result.set(0, (boolean) returnValue);
                        } else {
                            result.set(0, ((List<EntityType<?>>) returnValue).contains(type));
                        }
                    }
                });
                return result.get(0);
            }), (set, obj, key, valueSupplier) -> set.allowsSpawning((state, world, pos, type) -> {
                Object value = valueSupplier.apply(obj, key);
                if (value instanceof Boolean) {
                    return (boolean) value;
                } else {
                    return ((List<EntityType<?>>) value).contains(type);
                }
            }));
        }

        if (ChromaJsonHelper.getBooleanOrDefault(object, "air", false))
            settings.air();

        if (object.has("blocks_view")) {
            applySettingWithOptions(object, "blocks_view", settings, temp.getStateManager(),
                    (obj, key) -> JsonHelper.getBoolean(obj, key),
                    (map, defaultValue, stateManager, set) -> set
                            .blockVision((state, world, pos) -> returnOption(map, defaultValue, stateManager, state)),
                    (set, obj, key, valueSupplier) -> set
                            .blockVision((state, world, pos) -> valueSupplier.apply(obj, key)));
        }

        if (JsonHelper.hasElement(object, "drops_like")) {
            JsonElement dropsLike = object.get("drops_like");
            if (Identifier.tryParse(dropsLike.getAsString()) == LootTables.EMPTY) {
                settings.dropsNothing();
            } else {
                settings.dropsLike(ChromaJsonHelper.asBlock(dropsLike, "drops_like"));
            }
        }

        if (ChromaJsonHelper.getBooleanOrDefault(object, "dynamic_shape", false))
            settings.dynamicBounds();

        if (object.has("emissive_rendering")) {
            applySettingWithOptions(object, "emissive_rendering", settings, temp.getStateManager(),
                    (obj, key) -> JsonHelper.getBoolean(obj, key),
                    (map, defaultValue, stateManager, set) -> set.emissiveLighting(
                            (state, world, pos) -> returnOption(map, defaultValue, stateManager, state)),
                    (set, obj, key, valueSupplier) -> set
                            .emissiveLighting((state, world, pos) -> valueSupplier.apply(obj, key)));
        }

        if (object.has("destroy_time"))
            settings.hardness(JsonHelper.getFloat(object, "destroy_time"));

        if (object.has("exposion_resistance"))
            settings.resistance(JsonHelper.getFloat(object, "explosion_resistance"));

        settings.jumpVelocityMultiplier(ChromaJsonHelper.getFloatOrDefault(object, "jump_velocity_multiplier", 1.0F));

        if (object.has("light_level")) {
            applySettingWithOptions(object, "light_level", settings, temp.getStateManager(),
                    (obj, key) -> JsonHelper.getInt(obj, key),
                    (map, defaultValue, stateManager, set) -> set
                            .luminance((state) -> returnOption(map, defaultValue, stateManager, state)),
                    (set, obj, key, valueSupplier) -> set.luminance((state) -> valueSupplier.apply(obj, key)));
        }

        if (object.has("post_process")) {
            applySettingWithOptions(object, "post_process", settings, temp.getStateManager(),
                    (obj, key) -> JsonHelper.getBoolean(obj, key),
                    (map, defaultValue, stateManager, set) -> set
                            .postProcess((state, world, pos) -> returnOption(map, defaultValue, stateManager, state)),
                    (set, obj, key, valueSupplier) -> set
                            .postProcess((state, world, pos) -> valueSupplier.apply(obj, key)));
        }

        if (ChromaJsonHelper.getBooleanOrDefault(object, "requires_tool_for_drops", false))
            settings.requiresTool();

        settings.slipperiness(ChromaJsonHelper.getFloatOrDefault(object, "friction", 0.6F));

        if (object.has("solid_block")) {
            applySettingWithOptions(object, "solid_block", settings, temp.getStateManager(),
                    (obj, key) -> JsonHelper.getBoolean(obj, key),
                    (map, defaultValue, stateManager, set) -> set
                            .solidBlock((state, world, pos) -> returnOption(map, defaultValue, stateManager, state)),
                    (set, obj, key, valueSupplier) -> set
                            .solidBlock((state, world, pos) -> valueSupplier.apply(obj, key)));
        }

        if (object.has("suffocates")) {
            applySettingWithOptions(object, "suffocates", settings, temp.getStateManager(),
                    (obj, key) -> JsonHelper.getBoolean(obj, key),
                    (map, defaultValue, stateManager, set) -> set
                            .suffocates((state, world, pos) -> returnOption(map, defaultValue, stateManager, state)),
                    (set, obj, key, valueSupplier) -> set
                            .suffocates((state, world, pos) -> valueSupplier.apply(obj, key)));
        }

        settings.sounds(ChromaJsonHelper.getFromMapSafe(
                new Identifier(ChromaJsonHelper.getStringOrDefault(object, "sounds", "minecraft:stone")),
                "block sound group", soundGroupMap));

        if (ChromaJsonHelper.getBooleanOrDefault(object, "tick_randomly", false))
            settings.ticksRandomly();

        settings.velocityMultiplier(ChromaJsonHelper.getFloatOrDefault(object, "velocity_multiplier", 1.0F));

        return settings;
    }

    static <T> T returnOption(Map<StatePredicate, T> map, T defaultValue, StateManager<Block, BlockState> stateManager,
            BlockState state) {
        List<T> result = new ArrayList<T>();
        result.add(defaultValue);
        map.forEach((predicate, returnValue) -> {
            if (predicate.conditions.stream().allMatch((condition) -> condition.test(stateManager, state))) {
                result.set(0, returnValue);
            }
        });
        return result.get(0);
    }

    static <T> void applySettingWithOptions(JsonObject object, String settingKey, Block.Settings settings,
            StateManager<Block, BlockState> stateManager, BiFunction<JsonObject, String, T> valueSupplier,
            QuadConsumer<Map<StatePredicate, T>, T, StateManager<Block, BlockState>, Block.Settings> apply,
            QuadConsumer<Block.Settings, JsonObject, String, BiFunction<JsonObject, String, T>> alt) {
        JsonElement element = object.get(settingKey);
        if (element.isJsonObject()) {
            Map<StatePredicate, T> map = new HashMap<StatePredicate, T>();
            JsonObject settingObj = JsonHelper.asObject(element, "JSON object");
            T defaultValue = valueSupplier.apply(settingObj, "default");
            ChromaJsonHelper.getArrayOrDefault(settingObj, "options", new JsonArray()).forEach((entry) -> {
                JsonObject entryObj = JsonHelper.asObject(entry, "JSON object");
                T value = valueSupplier.apply(entryObj, settingKey);
                StatePredicate statePredicate = StatePredicate.fromJson(entryObj.get("block_state"));
                statePredicate.check(stateManager, (propertyName) -> {
                    throw new JsonSyntaxException("Block has no property '" + propertyName + "'");
                });
                map.put(statePredicate, value);
            });
            apply.accept(map, defaultValue, stateManager, settings);
        } else {
            alt.accept(settings, object, settingKey, valueSupplier);
        }
    }

    private static Block fromBlockType(JsonObject object, Block.Settings settings, BlockType type)
            throws JsonSyntaxException {
        if (type.getIdentifier().getNamespace().equals(Identifier.DEFAULT_NAMESPACE)) {
            switch (type.getIdentifier().getPath()) {
                case "simple": {
                    return new Block(settings);
                }
                case "air": {
                    return new AirBlock(settings);
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
                    Block gourd = ChromaJsonHelper.getBlock(object, "fruit");
                    if (!(gourd instanceof GourdBlock)) {
                        throw new JsonSyntaxException(
                                "Expected a block of a type which extends the abstract minecraft:stem_grown");
                    } else {
                        return new AttachedStemBlock((GourdBlock) gourd, () -> {
                            return ChromaJsonHelper.getItemOrDefault(object, "seeds", Items.AIR);
                        }, settings);
                    }
                }
                case "azalea": {
                    return new AzaleaBlock(settings);
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
                    return new BarrierBlock(settings);
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
                case "beetroot": {
                    return new BeetrootsBlock(settings);
                }
                case "bell": {
                    return new BellBlock(settings);
                }
                case "big_dripleaf": {
                    return new BigDripleafBlock(settings);
                }
                case "big_dripleaf_stem": {
                    return new BigDripleafStemBlock(settings);
                }
                case "blast_furnace": {
                    return new BlastFurnaceBlock(settings);
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
                    return new CactusBlock(settings);
                }
                case "cake": {
                    return new CakeBlock(settings);
                }
                case "campfire": {
                    return new CampfireBlock(ChromaJsonHelper.getBooleanOrDefault(object, "spawn_particles", true),
                            JsonHelper.getInt(object, "fire_damage"), settings);
                }
                case "candle": {
                    return new CandleBlock(settings);
                }
                case "candle_cake": {
                    return new CandleCakeBlock(ChromaJsonHelper.getBlock(object, "candle"), settings);
                }
                case "carpet": {
                    return new CarpetBlock(settings);
                }
                case "carrot": {
                    return new CarrotsBlock(settings);
                }
                case "cartography_table": {
                    return new CartographyTableBlock(settings);
                }
                case "carved_pumpkin": {
                    return new CarvedPumpkinBlock(settings);
                }
                case "cauldron": {
                    return new CauldronBlock(settings);
                }
                case "cave_vines_plant": {
                    return new CaveVinesBodyBlock(settings);
                }
                case "cave_vines": {
                    return new CaveVinesHeadBlock(settings);
                }
                case "chain": {
                    return new ChainBlock(settings);
                }
                case "chest": {
                    BlockEntityType<?> blockEntityType = ChromaJsonHelper.getBlockEntity(object, "block_entity");
                    if (blockEntityType.instantiate(null, null) instanceof ChestBlockEntity) {
                        return new ChestBlock(settings, () -> {
                            return (BlockEntityType<ChestBlockEntity>) blockEntityType;
                        });
                    } else {
                        throw new JsonSyntaxException("Block entity must be of a chest type");
                    }
                }
                case "chorus_flower": {
                    Block plant = ChromaJsonHelper.getBlock(object, "chorus_plant_block");
                    if (!(plant instanceof ChorusPlantBlock)) {
                        throw new JsonSyntaxException(
                                "Expected a block of type minecraft:chorus_plant or extending type");
                    } else {
                        return new ChorusFlowerBlock((ChorusPlantBlock) plant, settings);
                    }
                }
                case "chorus_plant": {
                    return new ChorusPlantBlock(settings);
                }
                case "web": {
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
                    return new ConcretePowderBlock(ChromaJsonHelper.getBlock(object, "concrete"), settings);
                }
                case "conduit": {
                    return new ConduitBlock(settings);
                }
                case "pipe": {
                    return new ConnectingBlock(ChromaJsonHelper.getFloatOrDefault(object, "radius", 0), settings);
                }
                case "coral": {
                    return new CoralBlock(ChromaJsonHelper.getBlock(object, "dead_block"), settings);
                }
                case "coral_plant": {
                    return new CoralBlockBlock(ChromaJsonHelper.getBlock(object, "dead_block"), settings);
                }
                case "coral_fan": {
                    return new CoralFanBlock(ChromaJsonHelper.getBlock(object, "dead_block"), settings);
                }
                case "base_coral_plant_type": {
                    return new CoralParentBlock(settings);
                }
                case "coral_wall_fan": {
                    return new CoralWallFanBlock(ChromaJsonHelper.getBlock(object, "dead_block"), settings);
                }
                case "crafting_table": {
                    return new CraftingTableBlock(settings);
                }
                case "crop": {
                    return new CropBlock(settings);
                }
                case "crying_obsidian": {
                    return new CryingObsidianBlock(settings);
                }
                case "daylight_detector": {
                    return new DaylightDetectorBlock(settings);
                }
                case "dead_bush": {
                    return new DeadBushBlock(settings);
                }
                case "base_coral": {
                    return new DeadCoralBlock(settings);
                }
                case "base_coral_fan": {
                    return new DeadCoralBlock(settings);
                }
                case "base_coral_wall_fan": {
                    return new DeadCoralWallFanBlock(settings);
                }
                case "detector_rail": {
                    return new DetectorRailBlock(settings);
                }
                case "dirt_path": {
                    return new DirtPathBlock(settings);
                }
                case "dispenser": {
                    return new DispenserBlock(settings);
                }
                case "door": {
                    return new DoorBlock(settings);
                }
                case "dragon_egg": {
                    return new DragonEggBlock(settings);
                }
                case "dropper": {
                    return new DropperBlock(settings);
                }
                case "wool_carpet": {
                    return new DyedCarpetBlock(ChromaJsonHelper.getDyeColor(object, "dye_color"), settings);
                }
                case "enchantment_table": {
                    return new EnchantingTableBlock(settings);
                }
                case "end_gateway": {
                    return new EndGatewayBlock(settings);
                }
                case "end_portal": {
                    return new EndPortalBlock(settings);
                }
                case "end_portal_frame": {
                    return new EndPortalFrameBlock(settings);
                }
                case "end_rod": {
                    return new EndRodBlock(settings);
                }
                case "ender_chest": {
                    return new EnderChestBlock(settings);
                }
                case "falling": {
                    return new FallingBlock(settings);
                }
                case "farm": {
                    return new FarmlandBlock(settings);
                }
                case "fence": {
                    return new FenceBlock(settings);
                }
                case "fence_gate": {
                    return new FenceGateBlock(settings);
                }
                case "tall_grass": {
                    return new FernBlock(settings);
                }
                case "fire": {
                    return new FireBlock(settings);
                }
                case "fletching_table": {
                    return new FletchingTableBlock(settings);
                }
                case "flower": {
                    return new FlowerBlock(ChromaJsonHelper.getEffect(object, "suspicious_stew_effect"),
                            ChromaJsonHelper.getInt(object, "effect_duration"), settings);
                }
                case "flower_pot": {
                    return new FlowerPotBlock(ChromaJsonHelper.getBlockOrDefault(object, "block", Blocks.AIR),
                            settings);
                }
                case "liquid": {
                    Fluid fluid = ChromaJsonHelper.getFluid(object, "fluid");
                    if (fluid instanceof FlowableFluid) {
                        return new FluidBlock((FlowableFluid) ChromaJsonHelper.getFluid(object, "flowing_fluid"), settings);
                    } else {
                        throw new JsonSyntaxException("Fluid must be flowable");
                    }
                }
                case "frosted_ice": {
                    return new FrostedIceBlock(settings);
                }
                case "fungus": {
                    return new FungusBlock(settings, () -> {
                        return Feature.HUGE_FUNGUS.configure(HugeFungusFeatureConfig.CODEC
                                .parse(JsonOps.INSTANCE, JsonHelper.getObject(object, "config"))
                                .getOrThrow(false, (error) -> {
                                    error = new String("Could not parse huge fungus");
                                }));
                    });
                }
                case "furnace": {
                    return new FurnaceBlock(settings);
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
                    return new GrindstoneBlock(settings);
                }
                case "hanging_roots": {
                    return new HangingRootsBlock(settings);
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
                case "cross_collision": {
                    return new HorizontalConnectingBlock(JsonHelper.getFloat(object, "radius_1"),
                            JsonHelper.getFloat(object, "radius_2"), JsonHelper.getFloat(object, "bounding_height_1"),
                            JsonHelper.getFloat(object, "bounding_height_2"),
                            JsonHelper.getFloat(object, "collision_height"), settings);
                }
                case "ice": {
                    return new IceBlock(settings);
                }
                case "infested": {
                    return new InfestedBlock(ChromaJsonHelper.getBlock(object, "regular_block"), settings);
                }
                case "jigsaw": {
                    return new JigsawBlock(settings);
                }
                case "jukebox": {
                    return new JukeboxBlock(settings);
                }
                case "kelp": {
                    return new KelpBlock(settings);
                }
                case "kelp_plant": {
                    return new KelpPlantBlock(settings);
                }
                case "ladder": {
                    return new LadderBlock(settings);
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
                    return new LecternBlock(settings);
                }
                case "layered_cauldron": {
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
                    Biome.Precipitation target = Biome.Precipitation
                            .byName(ChromaJsonHelper.getStringOrDefault(object, "precipitation", null));
                    return new LeveledCauldronBlock(settings, (precipitation) -> {
                        return precipitation == target;
                    }, behaviorMap);
                }
                case "lever": {
                    return new LeverBlock(settings);
                }
                case "light": {
                    return new LightBlock(settings);
                }
                case "lightning_rod": {
                    return new LightningRodBlock(settings);
                }
                case "waterlily": {
                    return new LilyPadBlock(settings);
                }
                case "loom": {
                    return new LoomBlock(settings);
                }
                case "magma": {
                    return new MagmaBlock(settings);
                }
                case "melon": {
                    return new MelonBlock(settings);
                }
                case "moss": {
                    return new MossBlock(settings);
                }
                case "huge_mushroom": {
                    return new MushroomBlock(settings);
                }
                case "mushroom": {
                    JsonObject featureObj = JsonHelper.getObject(object, "feature");
                    return new MushroomPlantBlock(settings, () -> {
                        return ChromaJsonHelper.getFeature(featureObj, "id").getCodec()
                                .parse(JsonOps.INSTANCE, JsonHelper.getObject(featureObj, "configuration"))
                                .getOrThrow(false, (error) -> {
                                    error = new String("Could not parse " + JsonHelper.getString(featureObj, "id" + " with configuration"));
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
                    return new NetherWartBlock(settings);
                }
                case "netherrack": {
                    return new NetherrackBlock(settings);
                }
                case "note": {
                    return new NoteBlock(settings);
                }
                case "nylium": {
                    return new NyliumBlock(settings);
                }
                case "observer": {
                    return new ObserverBlock(settings);
                }
                case "ore": {
                    return new OreBlock(settings);
                }
                case "weathering_copper_full": {
                        return new OxidizableBlock(ChromaJsonHelper.getFromMapSafe(JsonHelper.getString(object, "weather_state"), "weather state", weatherStates), settings);
                }
                case "weathering_copper_slab": {
                        return new OxidizableSlabBlock(ChromaJsonHelper.getFromMapSafe(JsonHelper.getString(object, "weather_state"), "weather state", weatherStates), settings);
                }
                case "weathering_copper_stair": {
                        if (object.get("base").isJsonObject()) {
                            return new OxidizableStairsBlock(ChromaJsonHelper.getFromMapSafe(JsonHelper.getString(object, "weather_state"), "weather state", weatherStates),
                            BlockState.CODEC.parse(JsonOps.INSTANCE, JsonHelper.getObject(object, "base"))
                            .getOrThrow(false, (error) -> {
                                error = new String("Could not parse block with state");
                            }), settings);
                        } else {
                            return new OxidizableStairsBlock(ChromaJsonHelper.getFromMapSafe(JsonHelper.getString(object, "weather_state"), "weather state", weatherStates),
                            ChromaJsonHelper.getBlock(object, "full_block").getDefaultState(), settings);
                        }
                }
                case "iron_bars": {
                    return new PaneBlock(settings);
                }
                case "rotated_pillar": {
                    return new PillarBlock(settings);
                }
                case "piston_base": {
                    return new PistonBlock(ChromaJsonHelper.getBooleanOrDefault(object, "sticky", false), settings);
                }
                case "moving_piston": {
                    return new PistonExtensionBlock(settings);
                }
                case "piston_head": {
                    return new PistonHeadBlock(settings);
                }
                case "bush": {
                    return new PlantBlock(settings);
                }
                case "player_head": {
                    return new PlayerSkullBlock(settings);
                }
                case "pointed_dripstone": {
                    return new PointedDripstoneBlock(settings);
                }
                case "potato": {
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
                    Biome.Precipitation target = Biome.Precipitation
                            .byName(ChromaJsonHelper.getStringOrDefault(object, "precipitation", null));
                    return new PowderSnowCauldronBlock(settings, (precipitation) -> {
                        return precipitation == target;
                    }, behaviorMap);
                }
                case "powered_rail": {
                    return new PoweredRailBlock(settings);
                }
                case "pressure_plate": {
                    PressurePlateBlock.ActivationRule activationRule;
                    switch (ChromaJsonHelper.getStringOrDefault(object, "sensitive_to", "everything")) {
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
                    return new PressurePlateBlock(activationRule, settings);
                }
                case "pumpkin": {
                    return new PumpkinBlock(settings);
                }
                case "rail": {
                    return new RailBlock(settings);
                }
                case "powered": {
                    return new RedstoneBlock(settings);
                }
                case "redstone_lamp": {
                    return new RedstoneLampBlock(settings);
                }
                case "redstone_ore": {
                    return new RedstoneOreBlock(settings);
                }
                case "redstone_torch": {
                    return new RedstoneTorchBlock(settings);
                }
                case "redstone_wire": {
                    return new RedstoneWireBlock(settings);
                }
                case "repeater": {
                    return new RepeaterBlock(settings);
                }
                case "respawn_anchor": {
                    return new RespawnAnchorBlock(settings);
                }
                case "rod": {
                    return new RodBlock(settings);
                }
                case "rooted_dirt": {
                    return new RootedDirtBlock(settings);
                }
                case "roots": {
                    return new RootsBlock(settings);
                }
                case "infested_rotated_pillar": {
                    return new RotatedInfestedBlock(ChromaJsonHelper.getBlock(object, "block"), settings);
                }
                case "sand": {
                    return new SandBlock(JsonHelper.getInt(object, "color"), settings);
                }
                case "sapling": {
                    JsonArray trees = JsonHelper.getArray(object, "trees");
                    JsonArray treesBees = ChromaJsonHelper.getArrayOrDefault(object, "trees_bees", trees);
                    JsonArray treesLarge = ChromaJsonHelper.getArrayOrDefault(object, "large_trees", new JsonArray());
                    if (treesLarge.size() > 0) {
                        return new SaplingBlock(new DynamicLargeTreeSaplingGenerator(getTree(trees), getTree(treesBees),
                                getTree(treesLarge), getWeight(trees), getWeight(treesBees), getWeight(treesLarge)),
                                settings);
                    } else {
                        return new SaplingBlock(new DynamicSaplingGenerator(getTree(trees), getTree(treesBees),
                                getWeight(trees), getWeight(treesBees)), settings);
                    }
                }
                case "scaffolding": {
                    return new ScaffoldingBlock(settings);
                }
                case "sculk_sensor": {
                    return new SculkSensorBlock(settings, JsonHelper.getInt(object, "range"));
                }
                case "sea_pickle": {
                    return new SeaPickleBlock(settings);
                }
                case "seagrass": {
                    return new SeagrassBlock(settings);
                }
                case "shulker_box": {
                    return new ShulkerBoxBlock(ChromaJsonHelper.getDyeColorOrDefault(object, "color", null), settings);
                }
                // TODO: Make custom sign types possible
                case "standing_sign": {
                    String signType = JsonHelper.getString(object, "sign_type");
                    if (!SignType.stream().anyMatch(t -> t.getName() == signType)) {
                        throw new JsonSyntaxException("Unknown type '" + signType + "'");
                    }
                    return new SignBlock(settings,
                            SignType.stream().filter(t -> t.getName() == signType).findFirst().get());
                }
                // TODO: Make custom skull types possible
                case "skull": {
                        return new SkullBlock(ChromaJsonHelper.getFromMapSafe(JsonHelper.getString(object, "skull_type"), "skull type", skullTypes), settings);
                }
                case "slab": {
                    return new SlabBlock(settings);
                }
                case "slime": {
                    return new SlimeBlock(settings);
                }
                case "small_dripleaf": {
                    return new SmallDripleafBlock(settings);
                }
                case "smithing_table": {
                    return new SmithingTableBlock(settings);
                }
                case "smoker": {
                    return new SmokerBlock(settings);
                }
                case "snow_layer": {
                    return new SnowBlock(settings);
                }
                case "snowy_dirt": {
                    return new SnowyBlock(settings);
                }
                case "soul_fire": {
                    return new SoulFireBlock(settings);
                }
                case "soul_sand": {
                    return new SoulSandBlock(settings);
                }
                case "spawner": {
                    return new SpawnerBlock(settings);
                }
                case "sponge": {
                    return new SpongeBlock(settings);
                }
                case "spore_blossom": {
                    return new SporeBlossomBlock(settings);
                }
                case "nether_sprouts": {
                    return new SproutsBlock(settings);
                }
                case "stained_glass": {
                    return new StainedGlassBlock(ChromaJsonHelper.getDyeColor(object, "dye_color"), settings);
                }
                case "stained_glass_pane": {
                    return new StainedGlassPaneBlock(ChromaJsonHelper.getDyeColor(object, "dye_color"), settings);
                }
                case "stair": {
                    if (object.get("base").isJsonObject()) {
                        return new StairsBlock(
                                BlockState.CODEC.parse(JsonOps.INSTANCE, JsonHelper.getObject(object, "base"))
                                        .getOrThrow(false, (error) -> {
                                            error = new String("Could not parse block with state");
                                        }),
                                settings);
                    } else {
                        return new StairsBlock(ChromaJsonHelper.getBlock(object, "base").getDefaultState(), settings);
                    }
                }
                case "stem": {
                    Block gourd = ChromaJsonHelper.getBlock(object, "fruit");
                    if (!(gourd instanceof GourdBlock)) {
                        throw new JsonSyntaxException(
                                "Expected a block of a type which extends the abstract minecraft:stem_grown");
                    } else {
                        return new AttachedStemBlock((GourdBlock) gourd, () -> {
                            return ChromaJsonHelper.getItemOrDefault(object, "seeds", Items.AIR);
                        }, settings);
                    }
                }
                case "stone_button": {
                    return new StoneButtonBlock(settings);
                }
                case "stonecutter": {
                    return new StonecutterBlock(settings);
                }
                case "structure": {
                    return new StructureBlock(settings);
                }
                case "structure_void": {
                    return new StructureVoidBlock(settings);
                }
                case "sugar_cane": {
                    return new SugarCaneBlock(settings);
                }
                case "sweet_berry_bush": {
                    return new SweetBerryBushBlock(settings);
                }
                case "tall_flower": {
                    return new TallFlowerBlock(settings);
                }
                case "double_plant": {
                    return new TallPlantBlock(settings);
                }
                case "tall_seagrass": {
                    return new TallSeagrassBlock(settings);
                }
                case "target": {
                    return new TargetBlock(settings);
                }
                case "tinted_glass": {
                    return new TintedGlassBlock(settings);
                }
                case "tnt": {
                    return new TntBlock(settings);
                }
                case "torch": {
                    return new TorchBlock(settings,
                            (ParticleEffect) ChromaJsonHelper.getParticleType(object, "particle"));
                }
                case "half_transparent": {
                    return new TransparentBlock(settings);
                }
                case "trapdoor": {
                    return new TrapdoorBlock(settings);
                }
                case "trapped_chest": {
                    return new TrappedChestBlock(settings);
                }
                case "trip_wire": {
                    Block tripwireHook = ChromaJsonHelper.getBlock(object, "tripwire_hook_block");
                    if (!(tripwireHook instanceof TripwireHookBlock)) {
                        throw new JsonSyntaxException(
                                "Expected a block of type minecraft:tripwire_hook or extending type");
                    } else {
                        return new TripwireBlock((TripwireHookBlock) tripwireHook, settings);
                    }
                }
                case "trip_wire_hook": {
                    return new TripwireHookBlock(settings);
                }
                case "turtle_egg": {
                    return new TurtleEggBlock(settings);
                }
                case "twisting_vines": {
                    return new TwistingVinesBlock(settings);
                }
                case "twisting_vines_plant": {
                    return new TwistingVinesPlantBlock(settings);
                }
                case "vine": {
                    return new VineBlock(settings);
                }
                case "wall_banner": {
                    return new WallBannerBlock(ChromaJsonHelper.getDyeColor(object, "color"), settings);
                }
                case "wall": {
                    return new WallBlock(settings);
                }
                case "face_attached_horizontal_directional": {
                    return new WallMountedBlock(settings);
                }
                case "player_wall_head": {
                    return new WallPlayerSkullBlock(settings);
                }
                case "redstone_wall_torch": {
                    return new WallRedstoneTorchBlock(settings);
                }
                case "wall_sign": {
                    String signType = JsonHelper.getString(object, "sign_type");
                    if (!SignType.stream().anyMatch(t -> t.getName() == signType)) {
                        throw new JsonSyntaxException("Unknown type '" + signType + "'");
                    }
                    return new WallSignBlock(settings,
                            SignType.stream().filter(t -> t.getName() == signType).findFirst().get());
                }
                case "wall_skull": {
                        return new WallSkullBlock(ChromaJsonHelper.getFromMapSafe(JsonHelper.getString(object, "skull_type"), "skull type", skullTypes), settings);
                }
                case "wall_torch": {
                    return new WallTorchBlock(settings,
                            (ParticleEffect) ChromaJsonHelper.getParticleType(object, "particle"));
                }
                case "wither_wall_skull": {
                    return new WallWitherSkullBlock(settings);
                }
                case "weeping_vines": {
                    return new WeepingVinesBlock(settings);
                }
                case "weeping_vines_plant": {
                    return new WeepingVinesPlantBlock(settings);
                }
                case "weighted_pressure_plate": {
                    return new WeightedPressurePlateBlock(JsonHelper.getInt(object, "weight"), settings);
                }
                case "wet_sponge": {
                    return new WetSpongeBlock(settings);
                }
                case "wither_rose": {
                    return new WitherRoseBlock(ChromaJsonHelper.getEffect(object, "effect"), settings);
                }
                case "wither_skull": {
                    return new WitherSkullBlock(settings);
                }
                case "wood_button": {
                    return new WoodenButtonBlock(settings);
                }
                default: {
                    break;
                }
            }
        }
        return new CustomizedBlock(settings, type);
    }

    static List<Integer> getWeight(JsonArray possibilities) throws JsonSyntaxException {
        List<Integer> list = new ArrayList<Integer>();
        possibilities.forEach((element) -> {
            JsonObject obj = JsonHelper.asObject(element, "object");
            int i = JsonHelper.getInt(obj, "weight");
            if (i < 0) {
                throw new JsonSyntaxException("Weight cannot be less than zero");
            }
            list.add(i);
        });
        return list;
    }

    static List<ConfiguredFeature<TreeFeatureConfig, ?>> getTree(JsonArray possibilities) throws JsonSyntaxException {
        List<ConfiguredFeature<TreeFeatureConfig, ?>> list = new ArrayList<ConfiguredFeature<TreeFeatureConfig, ?>>();
        possibilities.forEach((element) -> {
            JsonObject obj = JsonHelper.asObject(element, "object");
            list.add(Feature.TREE.configure(TreeFeatureConfig.CODEC
                    .parse(JsonOps.INSTANCE, JsonHelper.getObject(obj, "tree")).getOrThrow(false, (error) -> {
                        error = new String("Could not parse tree");
                    })));
        });
        return list;
    }

    public static Block readBlock(JsonObject object, Map<Identifier, Material> materialMap,
            Map<Identifier, BlockSoundGroup> soundGroupMap) throws JsonSyntaxException {
        Block block = fromBlockType(object,
                readSettings(JsonHelper.getObject(object, "settings"), materialMap, soundGroupMap, object),
                new BlockType(new Identifier(ChromaJsonHelper.getStringOrDefault(object, "type", "minecraft:simple")),
                        null));
        return block;
    }
}
