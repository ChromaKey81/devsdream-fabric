package net.devsdream;

// import java.io.File;
// import java.io.FileNotFoundException;
// import java.io.FileReader;
// import java.io.IOException;
// import java.util.HashMap;
// import java.util.Map;
// import java.util.stream.Stream;

// import com.google.gson.JsonObject;
// import com.google.gson.JsonSyntaxException;

// import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.devsdream.commands.AirCommand;
import net.devsdream.commands.CalculateCommand;
import net.devsdream.commands.DamageCommand;
import net.devsdream.commands.DamageItemCommand;
import net.devsdream.commands.EffectCommand;
import net.devsdream.commands.ExecuteCommand;
import net.devsdream.commands.ExhaustCommand;
import net.devsdream.commands.FeedCommand;
import net.devsdream.commands.FreezeCommand;
import net.devsdream.commands.HealthCommand;
import net.devsdream.commands.IgniteCommand;
import net.devsdream.commands.MotionCommand;
import net.devsdream.commands.RandomNumberCommand;
import net.devsdream.crafting.Serializers;
// import net.devsdream.objectpack.BlockReader;
// import net.devsdream.objectpack.ItemReader;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
// import net.minecraft.block.Block;
// import net.minecraft.block.Material;
// import net.minecraft.item.Item;
// import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
// import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

public class Main implements ModInitializer {

  public static final String MODID = "devsdream";

  public static final Logger logger = LogManager.getLogger(MODID);

  // public static JsonObject getObjectFromFile(File file) throws JsonSyntaxException {
  //   try (FileReader reader = new FileReader(file)) {
  //     return JsonHelper.deserialize(reader, false);
  //   } catch (FileNotFoundException e) {
  //     throw new JsonSyntaxException(e.getMessage());
  //   } catch (IOException e) {
  //     throw new JsonSyntaxException(e.getMessage());
  //   }
  // }

  // private static void put(Map<Identifier, Material> map, String name, Material material) {
  //   map.put(new Identifier(Identifier.DEFAULT_NAMESPACE, name), material);
  // }

  // private static Map<Identifier, Material> mapVanillaMaterials() {
  //   Map<Identifier, Material> map = new HashMap<Identifier, Material>();
  //   put(map, "aggregate", Material.AGGREGATE);
  //   put(map, "air", Material.AIR);
  //   put(map, "amethyst", Material.AMETHYST);
  //   put(map, "bamboo", Material.BAMBOO);
  //   put(map, "bamboo_sapling", Material.BAMBOO_SAPLING);
  //   put(map, "barrier", Material.BARRIER);
  //   put(map, "bubble_column", Material.BUBBLE_COLUMN);
  //   put(map, "cactus", Material.CACTUS);
  //   put(map, "cake", Material.CAKE);
  //   put(map, "cobweb", Material.COBWEB);
  //   put(map, "decoration", Material.DECORATION);
  //   put(map, "dense_ice", Material.DENSE_ICE);
  //   put(map, "egg", Material.EGG);
  //   put(map, "fire", Material.FIRE);
  //   put(map, "glass", Material.GLASS);
  //   put(map, "gourd", Material.GOURD);
  //   put(map, "ice", Material.ICE);
  //   put(map, "lava", Material.LAVA);
  //   put(map, "leaves", Material.LEAVES);
  //   put(map, "metal", Material.METAL);
  //   put(map, "moss_block", Material.MOSS_BLOCK);
  //   put(map, "nether_shoots", Material.NETHER_SHOOTS);
  //   put(map, "nether_wood", Material.NETHER_WOOD);
  //   put(map, "organic_product", Material.ORGANIC_PRODUCT);
  //   put(map, "piston", Material.PISTON);
  //   put(map, "plant", Material.PLANT);
  //   put(map, "portal", Material.PORTAL);
  //   put(map, "powder_snow", Material.POWDER_SNOW);
  //   put(map, "redstone_lamp", Material.REDSTONE_LAMP);
  //   put(map, "repair_station", Material.REPAIR_STATION);
  //   put(map, "replaceable_plant", Material.REPLACEABLE_PLANT);
  //   put(map, "replaceable_underwater_plant", Material.REPLACEABLE_UNDERWATER_PLANT);
  //   put(map, "sculk", Material.SCULK);
  //   put(map, "shulker_box", Material.SHULKER_BOX);
  //   put(map, "snow_block", Material.SNOW_BLOCK);
  //   put(map, "snow_layer", Material.SNOW_LAYER);
  //   put(map, "soil", Material.SOIL);
  //   put(map, "solid_organic", Material.SOLID_ORGANIC);
  //   put(map, "sponge", Material.SPONGE);
  //   put(map, "stone", Material.STONE);
  //   put(map, "structure_void", Material.STRUCTURE_VOID);
  //   put(map, "tnt", Material.TNT);
  //   put(map, "underwater_plant", Material.UNDERWATER_PLANT);
  //   put(map, "water", Material.WATER);
  //   put(map, "wood", Material.WOOD);
  //   put(map, "wool", Material.WOOL);
  //   return map;
  // }

  // Map<Identifier, BlockSoundGroup> soundGroupMap = BlockReader.soundGroups;

  @Override
  public void onInitialize() {

    CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
      AirCommand.register(dispatcher);
      CalculateCommand.register(dispatcher);
      DamageCommand.register(dispatcher);
      DamageItemCommand.register(dispatcher);
      EffectCommand.register(dispatcher);
      ExecuteCommand.register(dispatcher);
      ExhaustCommand.register(dispatcher);
      FeedCommand.register(dispatcher);
      HealthCommand.register(dispatcher);
      IgniteCommand.register(dispatcher);
      RandomNumberCommand.register(dispatcher);
      FreezeCommand.register(dispatcher);
      MotionCommand.register(dispatcher);
    });

    Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(MODID, "crafting_shaped_nbt"),
        Serializers.CRAFTING_SHAPED_NBT);
    Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(MODID, "crafting_shapeless_nbt"),
        Serializers.CRAFTING_SHAPELESS_NBT);
    Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(MODID, "stonecutting_nbt"),
        Serializers.STONECUTTING_NBT);
    Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(MODID, "smithing_nbt"), Serializers.SMITHING_NBT);

    // try {
    //   File[] objectpacks = new File(System.getProperty("user.dir") + "/objectpacks").listFiles();
    //   Map<Identifier, Material> materialsMap = new HashMap<Identifier, Material>();
    //   materialsMap.putAll(mapVanillaMaterials());
    //   for (final File namespace : objectpacks) {
    //     try {
    //       int materialAmount = 0;
    //       for (final File material : Stream.of(new File(namespace.getPath() + "/blocks/materials").listFiles())
    //           .filter((file) -> FilenameUtils.getExtension(file.getName()).equals("json")).toList()) {
    //         String id = namespace.getName() + ":" + FilenameUtils.getBaseName(material.getName());
    //         try {
    //           Material newMaterial = BlockReader.readMaterial(getObjectFromFile(material));
    //           materialsMap.put(new Identifier(id), newMaterial);
    //           materialAmount++;
    //         } catch (JsonSyntaxException e) {
    //           logger.error("Couldn't load material '" + id + "': " + e.getMessage());
    //         }
    //       }
    //       logger.info("Loaded " + materialAmount + " materials");
    //     } catch (NullPointerException e) {
    //     }
    //     try {
    //       int soundAmount = 0;
    //       for (final File soundGroup : Stream.of(new File(namespace.getPath() + "/blocks/sound_groups").listFiles())
    //           .filter((file) -> FilenameUtils.getExtension(file.getName()).equals("json")).toList()) {
    //         String id = namespace.getName() + ":" + FilenameUtils.getBaseName(soundGroup.getName());
    //         try {
    //           BlockSoundGroup newSoundGroup = BlockReader.readSoundGroup(getObjectFromFile(soundGroup));
    //           soundGroupMap.put(new Identifier(id), newSoundGroup);
    //           soundAmount++;
    //         } catch (JsonSyntaxException e) {
    //           logger.error("Couldn't load block sound group '" + id + "': " + e.getMessage());
    //         }
    //       }
    //       logger.info("Loaded " + soundAmount + " block sound groups");
    //     } catch (NullPointerException e) {
    //     }
    //     try {
    //       int blockAmount = 0;
    //       for (final File block : Stream.of(new File(namespace.getPath() + "/blocks").listFiles())
    //           .filter((file) -> FilenameUtils.getExtension(file.getName()).equals("json")).toList()) {
    //         String id = namespace.getName() + ":" + FilenameUtils.getBaseName(block.getName());
    //         try {
    //           Block newBlock = BlockReader.readBlock(getObjectFromFile(block), materialsMap, soundGroupMap);
    //           try {
    //             Registry.register(Registry.BLOCK, new Identifier(id), newBlock);
    //           } catch (RuntimeException e) {
    //             Registry.register(Registry.BLOCK, Registry.BLOCK.getRawId(Registry.BLOCK.get(new Identifier(id))), id,
    //                 newBlock);
    //           }
    //           blockAmount++;
    //         } catch (JsonSyntaxException e) {
    //           logger.error("Couldn't register block '" + id + "': " + e.toString());
    //         }
    //       }
    //       logger.info("Registered " + blockAmount + " blocks");
    //     } catch (NullPointerException e) {
    //     }
    //     try {
    //       int itemAmount = 0;
    //       for (final File item : new File(namespace.getPath() + "/items").listFiles()) {
    //         String id = namespace.getName() + ":" + FilenameUtils.getBaseName(item.getName());
    //         try {
    //           Item newItem = ItemReader.readItem(getObjectFromFile(item));
    //           try {
    //             Registry.register(Registry.ITEM, new Identifier(id), newItem);
    //           } catch (RuntimeException e) {
    //             Registry.register(Registry.ITEM, Registry.ITEM.getRawId(Registry.ITEM.get(new Identifier(id))), id,
    //                 newItem);
    //           }
    //           itemAmount++;
    //         } catch (JsonSyntaxException e) {
    //           logger.error("Couldn't register item '" + id + "': " + e.getMessage());
    //         }
    //       }
    //       logger.info("Registered " + itemAmount + " items");
    //     } catch (NullPointerException e) {
    //     }
    //     // try {
    //     //   int effectAmount = 0;
    //     //   for (final File effect : new File(namespace.getPath() + "/effects").listFiles()) {
    //     //     String id = namespace.getName() + ":" + FilenameUtils.getBaseName(effect.getName());
    //     //     try {
    //     //       StatusEffect newEffect = new StatusEffect(StatusEffectType.HARMFUL, 100);
    //     //       Registry.register(Registry.STATUS_EFFECT, new Identifier(id), newEffect);
    //     //       effectAmount++;
    //     //     } catch (JsonSyntaxException e) {
    //     //       logger.error("Couldn't register status effect '" + id + "': " + e.getMessage());
    //     //     }
    //     //   }
    //     //   logger.info("Registered " + effectAmount + " status effects");
    //     // } catch (NullPointerException e) {
    //     // }
    //   }
    // } catch (NullPointerException e) {
    // }
  }

}
