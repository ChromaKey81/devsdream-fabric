package net.devsdream.objectpack;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.devsdream.Main;
import net.devsdream.util.ChromaJsonHelper;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.Rarity;

public class ItemReader {

    static Map<String, Rarity> mapRarity() {
        Map<String, Rarity> map = new HashMap<String, Rarity>();
        map.put("common", Rarity.COMMON);
        map.put("uncommon", Rarity.UNCOMMON);
        map.put("rare", Rarity.RARE);
        map.put("epic", Rarity.EPIC);
        return map;
    }

    public static final Map<String, Rarity> rarityMap = mapRarity();

    public static Item.Settings readSettings(JsonObject object) throws JsonSyntaxException {
        Item.Settings settings = new Item.Settings();

        if (object.has("food")) {
            JsonObject food = JsonHelper.getObject(object, "food");
            FoodComponent.Builder builder = new FoodComponent.Builder();
            builder.hunger(JsonHelper.getInt(food, "nutrition"))
                    .saturationModifier(JsonHelper.getFloat(food, "saturation_modifier"));
            if (ChromaJsonHelper.getBooleanOrDefault(food, "meat", false)) {
                builder.meat();
            }
            if (ChromaJsonHelper.getBooleanOrDefault(food, "fast", false)) {
                builder.snack();
            }
            if (ChromaJsonHelper.getBooleanOrDefault(food, "always_edible", false)) {
                builder.alwaysEdible();
            }
            JsonArray effectsArray = ChromaJsonHelper.getArrayOrDefault(food, "effects", new JsonArray());
            effectsArray.forEach((next) -> {
                JsonHelper.asObject(next, "JSON object");
                builder.statusEffect(
                        ChromaJsonHelper.effectInstance(JsonHelper.getObject((JsonObject) next, "status_effect")),
                        JsonHelper.getFloat((JsonObject) next, "chance"));
            });
            settings.food(builder.build());
        }

        if (ChromaJsonHelper.getBooleanOrDefault(object, "fire_resistant", false)) {
            settings.fireproof();
        }

        if (object.has("durability")) {
            settings.maxDamage(JsonHelper.getInt(object, "durability"));
        }

        if (object.has("default_durability")) {
            settings.maxDamage(JsonHelper.getInt(object, "default_durability"));
        }
        
        if (object.has("craft_remainder")) {
            settings.recipeRemainder(JsonHelper.getItem(object, "craft_remainder"));
        }

        // if (object.has("tab")) {
        //     settings.group(ChromaJsonHelper.getFromMapSafe(ChromaJsonHelper.getString(object, "tab"),
        //     "creative mode tab", itemGroupMap));
        // }

        settings.group(ItemGroup.MISC);
        
        settings.rarity(ChromaJsonHelper.getFromMapSafe(ChromaJsonHelper.getStringOrDefault(object, "rarity", "common"),
                "rarity", rarityMap));

        try {
            settings.maxCount(ChromaJsonHelper.getIntOrDefault(object, "stacks_to", 64));
        } catch (RuntimeException e) {
            throw new JsonSyntaxException(e.getMessage());
        }

        return settings;
    }

    static Item fromItemType(JsonObject object, Item.Settings settings, ItemType type) throws JsonSyntaxException {
        if (type.getIdentifier().getNamespace().equals(Identifier.DEFAULT_NAMESPACE)) {
            switch (type.getIdentifier().getPath()) {
                case "simple": {
                    return new Item(settings);
                }
                // case "air": {
                //     return new AirBlockItem(ChromaJsonHelper.getBlock(object, "block"), settings);
                // }
                // case "axe": {
                //     return new AxeItem(ChromaJsonHelper.getFromMapSafe(JsonHelper.getString(object, "tier"), "tier", toolMaterialMap), JsonHelper.getFloat(object, "attack_damage"), JsonHelper.getFloat(object, "attack_speed"), settings);
                // }
                // case "armor": {
                //     return new ArmorItem(ChromaJsonHelper.getFromMapSafe(JsonHelper.getString(object, "armor_material"), "armor material", armorMaterialMap), ChromaJsonHelper.getEquipmentSlot(object, "equipment_slot"), settings);
                // }
                // case "armor_stand": {
                //     return new ArmorStandItem(settings);
                // }
                // case "arrow": {
                //     return new ArrowItem(settings);
                // }
                // case "banner": {
                //     return new BannerItem(ChromaJsonHelper.getBlock(object, "standing_block"), ChromaJsonHelper.getBlock(object, "wall_block"), settings);
                // }
                // case "banner_pattern": {
                //     String pattern = JsonHelper.getString(object, "banner_pattern");
                //     BannerPattern bannerPattern = BannerPattern.byName(pattern);
                //     if (bannerPattern == null) {
                //         throw new JsonSyntaxException("Expected a banner pattern, got unknown string '" + pattern + "'");
                //     }
                //     return new BannerPatternItem(bannerPattern, settings);
                // }
                // case "bed": {
                //     return new BedItem(ChromaJsonHelper.getBlock(object, "block"), settings);
                // }
                case "block": {
                    return new BlockItem(ChromaJsonHelper.getBlock(object, "block"), settings);
                }
                // case "boat": {
                //     String boatTypeString = JsonHelper.getString(object, "boat_type");
                //     BoatEntity.Type boatType = null;
                //     BoatEntity.Type[] types = BoatEntity.Type.values();
                //     for(int i = 0; i < types.length; ++i) {
                //        if (types[i].getName().equals(boatTypeString)) {
                //           boatType = types[i];
                //        }
                //     }
                //     if (boatType == null) {
                //         throw new JsonSyntaxException("Expected a boat type, got unknown string '" + boatTypeString + "'");
                //     }
                //     return new BoatItem(boatType, settings);
                // }
                // case "bone_meal": {
                //     return new BoneMealItem(settings);
                // }
                // case "book": {
                //     return new BookItem(settings);
                // }
                // case "bottle": {
                //     return new GlassBottleItem(settings);
                // }
                // case "bow": {
                //     return new BowItem(settings);
                // }
                // case "bowl_food": {
                //     return new MushroomStewItem(settings);
                // }
                // case "bucket": {
                //     return new BucketItem(ChromaJsonHelper.getFluid(object, "fluid"), settings);
                // }
                // case "bundle": {
                //     return new BundleItem(settings);
                // }
                // case "chorus_fruit": {
                //     return new ChorusFruitItem(settings);
                // }
                // case "compass": {
                //     return new CompassItem(settings);
                // }
                // case "complex": {
                //     return new NetworkSyncedItem(settings);
                // }
                // case "crossbow": {
                //     return new CrossbowItem(settings);
                // }
                // case "debug_stick": {
                //     return new DebugStickItem(settings);
                // }
                // case "digger": {
                //     return new MiningToolItem(JsonHelper.getFloat(object, "attack_damage"), JsonHelper.getFloat(object, "attack_speed"), ChromaJsonHelper.getFromMapSafe(JsonHelper.getString(object, "tier"), "tier", toolMaterialMap), BlockTags.REQUIRED_TAGS.add(JsonHelper.getString(object, "effective_on")), settings);
                // }
                // case "double_high_block": {
                //     return new TallBlockItem(ChromaJsonHelper.getBlock(object, "block"), settings);
                // }
                // case "dyeable_armor": {
                //     return new DyeableArmorItem()
                // }
                default: {
                    throw new JsonSyntaxException("Unknown item type");
                }
            }
        } else if (type.getIdentifier().toString().equals("devsdream:useable")) {
            return new RightClickItem(readSettings(JsonHelper.getObject(object, "settings")), new Identifier(JsonHelper.getString(object, "function")));
        } else {
            throw new JsonSyntaxException("Unknown item type");
        }
    }

    public static Item readItem(JsonObject object) throws JsonSyntaxException {
        ItemType type = new ItemType(new Identifier(ChromaJsonHelper.getStringOrDefault(object, "type", "minecraft:simple")), new JsonObject());
        return fromItemType(object, readSettings(JsonHelper.getObject(object, "settings")), type);
    }

}
