package net.devsdream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.devsdream.objectpack.BlockReader;
import net.devsdream.util.PublicConstructors;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

public class Main implements ModInitializer {

    private static final Logger logger = LogManager.getLogger("devsdream");

    public static JsonObject getObjectFromFile(File file) throws JsonSyntaxException {
        try (FileReader reader = new FileReader(file)) {
            return JsonHelper.deserialize(reader, false);
        } catch (FileNotFoundException e) {
            throw new JsonSyntaxException(e.getMessage());
        } catch (IOException e) {
            throw new JsonSyntaxException(e.getMessage());
        }
    }

    private static void put(Map<Identifier, Material> map, String name, Material material) {
        map.put(new Identifier(Identifier.DEFAULT_NAMESPACE, name), material);
    }

    private static Map<Identifier, Material> mapVanillaMaterials() {
        Map<Identifier, Material> map = new HashMap<Identifier, Material>();
        put(map, "aggregate", Material.AGGREGATE);
        put(map, "air", Material.AIR);
        put(map, "amethyst", Material.AMETHYST);
        put(map, "bamboo", Material.BAMBOO);
        put(map, "bamboo_sapling", Material.BAMBOO_SAPLING);
        put(map, "barrier", Material.BARRIER);
        put(map, "bubble_column", Material.BUBBLE_COLUMN);
        put(map, "cactus", Material.CACTUS);
        put(map, "cake", Material.CAKE);
        put(map, "cobweb", Material.COBWEB);
        put(map, "decoration", Material.DECORATION);
        put(map, "dense_ice", Material.DENSE_ICE);
        put(map, "egg", Material.EGG);
        put(map, "fire", Material.FIRE);
        put(map, "glass", Material.GLASS);
        put(map, "gourd", Material.GOURD);
        put(map, "ice", Material.ICE);
        put(map, "lava", Material.LAVA);
        put(map, "leaves", Material.LEAVES);
        put(map, "metal", Material.METAL);
        put(map, "moss_block", Material.MOSS_BLOCK);
        put(map, "nether_shoots", Material.NETHER_SHOOTS);
        put(map, "nether_wood", Material.NETHER_WOOD);
        put(map, "organic_product", Material.ORGANIC_PRODUCT);
        put(map, "piston", Material.PISTON);
        put(map, "plant", Material.PLANT);
        put(map, "portal", Material.PORTAL);
        put(map, "powder_snow", Material.POWDER_SNOW);
        put(map, "redstone_lamp", Material.REDSTONE_LAMP);
        put(map, "repair_station", Material.REPAIR_STATION);
        put(map, "replaceable_plant", Material.REPLACEABLE_PLANT);
        put(map, "replaceable_underwater_plant", Material.REPLACEABLE_UNDERWATER_PLANT);
        put(map, "sculk", Material.SCULK);
        put(map, "shulker_box", Material.SHULKER_BOX);
        put(map, "snow_block", Material.SNOW_BLOCK);
        put(map, "snow_layer", Material.SNOW_LAYER);
        put(map, "soil", Material.SOIL);
        put(map, "solid_organic", Material.SOLID_ORGANIC);
        put(map, "sponge", Material.SPONGE);
        put(map, "stone", Material.STONE);
        put(map, "structure_void", Material.STRUCTURE_VOID);
        put(map, "tnt", Material.TNT);
        put(map, "underwater_plant", Material.UNDERWATER_PLANT);
        put(map, "water", Material.WATER);
        put(map, "wood", Material.WOOD);
        put(map, "wool", Material.WOOL);
        return map;
    }

    Map<Identifier, BlockSoundGroup> soundGroupMap = BlockReader.soundGroups;

    @Override
    public void onInitialize() {
        try {
          File[] objectpacks = new File(System.getProperty("user.dir") + "/objectpacks").listFiles();
          Map<Identifier, Material> materialsMap = new HashMap<Identifier, Material>();
          materialsMap.putAll(mapVanillaMaterials());
          for (final File namespace : objectpacks) {
            try {
                int materialAmount = 0;
                for (final File material : new File(namespace.getPath() + "/blocks/materials").listFiles()) {
                  String id = namespace.getName() + ":" + FilenameUtils.getBaseName(material.getName());
                  try {
                    Material newMaterial = BlockReader.readMaterial(getObjectFromFile(material));
                    materialsMap.put(new Identifier(id), newMaterial);
                    materialAmount++;
                  } catch (JsonSyntaxException e) {
                    logger.error("Couldn't load material '" + id + "': " + e.getMessage());
                  }
                }
                logger.info("Loaded " + materialAmount + " materials");
              } catch (NullPointerException e) {
              }
              try {
                int soundAmount = 0;
                for (final File soundGroup : new File(namespace.getPath() + "/blocks/sound_groups").listFiles()) {
                  String id = namespace.getName() + ":" + FilenameUtils.getBaseName(soundGroup.getName());
                  try {
                    BlockSoundGroup newSoundGroup = BlockReader.readSoundGroup(getObjectFromFile(soundGroup));
                    soundGroupMap.put(new Identifier(id), newSoundGroup);
                    soundAmount++;
                  } catch (JsonSyntaxException e) {
                    logger.error("Couldn't load block sound group '" + id + "': " + e.getMessage());
                  }
                }
                logger.info("Loaded " + soundAmount + " block sound groups");
              } catch (NullPointerException e) {
              }
            try {
              int blockAmount = 0;
              for (final File block : new File(namespace.getPath() + "/blocks").listFiles()) {
                String id = namespace.getName() + ":" + FilenameUtils.getBaseName(block.getName());
                try {
                  Block newBlock = new Block(BlockReader.readSettings(getObjectFromFile(block), materialsMap, soundGroupMap));
                  try {
                    Registry.register(Registry.BLOCK, new Identifier(id), newBlock);
                  } catch (RuntimeException e) {
                    Registry.register(Registry.BLOCK, Registry.BLOCK.getRawId(Registry.BLOCK.get(new Identifier(id))), id, newBlock);
                  }
                  blockAmount++;
                } catch (JsonSyntaxException e) {
                  logger.error("Couldn't register block '" + id + "': " + e.getMessage());
                }
              }
              logger.info("Registered " + blockAmount + " blocks");
            } catch (NullPointerException e) {
            }
            try {
                int itemAmount = 0;
                for (final File item : new File(namespace.getPath() + "/items").listFiles()) {
                    String id = namespace.getName() + ":" + FilenameUtils.getBaseName(item.getName());
                    try {
                        Item newItem = new Item(new Item.Settings());
                        Registry.register(Registry.ITEM, new Identifier(id), newItem);
                        itemAmount++;
                    } catch (JsonSyntaxException e) {
                        logger.error("Couldn't register item '" + id + "': " + e.getMessage());
                    }
                }
                logger.info("Registered " + itemAmount + " items");
            } catch (NullPointerException e) {
            }
            try {
                int effectAmount = 0;
                for (final File effect : new File(namespace.getPath() + "/effects").listFiles()) {
                    String id = namespace.getName() + ":" + FilenameUtils.getBaseName(effect.getName());
                    try {
                        StatusEffect newEffect = new PublicConstructors.PublicStatusEffect(StatusEffectType.HARMFUL, 100);
                        Registry.register(Registry.STATUS_EFFECT, new Identifier(id), newEffect);
                        effectAmount++;
                    } catch (JsonSyntaxException e) {
                        logger.error("Couldn't register status effect '" + id + "': " + e.getMessage());
                    }
                }
                logger.info("Registered " + effectAmount + " status effects");
            } catch (NullPointerException e) {
            }
          }
        } catch (NullPointerException e) {
        }
    }
    
}
