package net.devsdream.util;

import java.util.NoSuchElementException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.function.CommandFunction;
import net.minecraft.server.function.CommandFunctionManager;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;

public class ChromaJsonHelper extends JsonHelper {

    /**
     * 
     * Reimplementing these methods with better names for more readability and easier access.
     * 
     */
    public static float getFloatOrDefault(JsonObject object, String element, float defaultValue) throws JsonSyntaxException {
        return JsonHelper.getFloat(object, element, defaultValue);
    }
    public static int getIntOrDefault(JsonObject object, String element, int defaultValue) throws JsonSyntaxException {
        return JsonHelper.getInt(object, element, defaultValue);
    }
    public static long getLongOrDefault(JsonObject object, String element, long defaultValue) throws JsonSyntaxException {
        return JsonHelper.getLong(object, element, defaultValue);
    }
    public static String getStringOrDefault(JsonObject object, String element, String defaultValue) throws JsonSyntaxException {
        return JsonHelper.getString(object, element, defaultValue);
    }
    public static boolean getBooleanOrDefault(JsonObject object, String element, boolean defaultValue) throws JsonSyntaxException {
        return JsonHelper.getBoolean(object, element, defaultValue);
    }
    public static Item getItemOrDefault(JsonObject object, String element, Item defaultValue) throws JsonSyntaxException {
        return JsonHelper.getItem(object, element, defaultValue);
    }

    /**
     * 
     * New methods for similar functionality to {@link JsonHelper#asItem(JsonElement, String)} and {@link JsonHelper#getItem(JsonObject, String)}
     * 
     */
    public static Block asBlock(JsonElement element, String name) throws JsonSyntaxException {
        if (element.isJsonPrimitive()) {
            String string = element.getAsString();
            return (Block)Registry.BLOCK.getOrEmpty(new Identifier(string)).orElseThrow(() -> {
               return new JsonSyntaxException("Expected " + name + " to be a block, was unknown string '" + string + "'");
            });
         } else {
            throw new JsonSyntaxException("Expected " + name + " to be a block, was " + getType(element));
         }
    }
    public static Block getBlock(JsonObject object, String key) throws JsonSyntaxException {
        if (object.has(key)) {
            return asBlock(object.get(key), key);
         } else {
            throw new JsonSyntaxException("Missing " + key + ", expected to find a block");
         }
    }
    public static Block getBlockOrDefault(JsonObject object, String key, Block defaultBlock) throws JsonSyntaxException {
        return object.has(key) ? asBlock(object.get(key), key) : defaultBlock;
    }

    public static SoundEvent asSound(JsonElement element, String name) throws JsonSyntaxException {
        if (element.isJsonPrimitive()) {
            String string = element.getAsString();
            return (SoundEvent)Registry.SOUND_EVENT.getOrEmpty(new Identifier(string)).orElseThrow(() -> {
               return new JsonSyntaxException("Expected " + name + " to be a sound event, was unknown string '" + string + "'");
            });
         } else {
            throw new JsonSyntaxException("Expected " + name + " to be a sound event, was " + getType(element));
         }
    }
    public static SoundEvent getSound(JsonObject object, String key) throws JsonSyntaxException {
        if (object.has(key)) {
            return asSound(object.get(key), key);
         } else {
            throw new JsonSyntaxException("Missing " + key + ", expected to find a sound event");
         }
    }
    public static SoundEvent getSoundOrDefault(JsonObject object, String key, SoundEvent defaultSound) throws JsonSyntaxException {
        return object.has(key) ? asSound(object.get(key), key) : defaultSound;
    }

    public static EntityType<?> asEntity(JsonElement element, String name) throws JsonSyntaxException {
        if (element.isJsonPrimitive()) {
            String string = element.getAsString();
            return (EntityType<?>)Registry.ENTITY_TYPE.getOrEmpty(new Identifier(string)).orElseThrow(() -> {
               return new JsonSyntaxException("Expected " + name + " to be an entity type, was unknown string '" + string + "'");
            });
         } else {
            throw new JsonSyntaxException("Expected " + name + " to be an entity type, was " + getType(element));
         }
    }
    public static EntityType<?> getEntity(JsonObject object, String key) throws JsonSyntaxException {
        if (object.has(key)) {
            return asEntity(object.get(key), key);
         } else {
            throw new JsonSyntaxException("Missing " + key + ", expected to find an entity type");
         }
    }
    public static EntityType<?> getEntityOrDefault(JsonObject object, String key, EntityType<?> defaultEntity) throws JsonSyntaxException {
        return object.has(key) ? asEntity(object.get(key), key) : defaultEntity;
    }

    public static DyeColor asDyeColor(JsonElement element, String name) throws JsonSyntaxException {
        if (element.isJsonPrimitive()) {
            String string = element.getAsString();
            DyeColor color = DyeColor.byName(string, null);
            if (color == null) {
                throw new JsonSyntaxException("Expected " + name + " to be a dye color, was unknown string '" + "'");
            } else {
                return color;
            }
         } else {
            throw new JsonSyntaxException("Expected " + name + " to be a dye color, was " + getType(element));
         }
    }
    public static DyeColor getDyeColor(JsonObject object, String key) throws JsonSyntaxException {
        if (object.has(key)) {
            return asDyeColor(object.get(key), key);
         } else {
            throw new JsonSyntaxException("Missing " + key + ", expected to find an entity type");
         }
    }
    public static DyeColor getDyeColorOrDefault(JsonObject object, String key, DyeColor defaultColor) throws JsonSyntaxException {
        return object.has(key) ? asDyeColor(object.get(key), key) : defaultColor;
    }

    
    public static BlockEntityType<?> asBlockEntity(JsonElement element, String name) throws JsonSyntaxException {
        if (element.isJsonPrimitive()) {
            String string = element.getAsString();
            return (BlockEntityType<?>)Registry.BLOCK_ENTITY_TYPE.getOrEmpty(new Identifier(string)).orElseThrow(() -> {
               return new JsonSyntaxException("Expected " + name + " to be a block entity type, was unknown string '" + string + "'");
            });
         } else {
            throw new JsonSyntaxException("Expected " + name + " to be a block entity type, was " + getType(element));
         }
    }
    public static BlockEntityType<?> getBlockEntity(JsonObject object, String key) throws JsonSyntaxException {
        if (object.has(key)) {
            return asBlockEntity(object.get(key), key);
         } else {
            throw new JsonSyntaxException("Missing " + key + ", expected to find a block entity type");
         }
    }
    public static BlockEntityType<?> getBlockEntityOrDefault(JsonObject object, String key, BlockEntityType<?> defaultBlockEntity) throws JsonSyntaxException {
        return object.has(key) ? asBlockEntity(object.get(key), key) : defaultBlockEntity;
    }

    public static StatusEffect asEffect(JsonElement element, String name) throws JsonSyntaxException {
        if (element.isJsonPrimitive()) {
            String string = element.getAsString();
            return (StatusEffect)Registry.STATUS_EFFECT.getOrEmpty(new Identifier(string)).orElseThrow(() -> {
               return new JsonSyntaxException("Expected " + name + " to be a status effect, was unknown string '" + string + "'");
            });
         } else {
            throw new JsonSyntaxException("Expected " + name + " to be a status effect, was " + getType(element));
         }
    }
    public static StatusEffect getEffect(JsonObject object, String key) throws JsonSyntaxException {
        if (object.has(key)) {
            return asEffect(object.get(key), key);
         } else {
            throw new JsonSyntaxException("Missing " + key + ", expected to find a status effect");
         }
    }
    public static StatusEffect getEffectOrDefault(JsonObject object, String key, StatusEffect defaultEffect) throws JsonSyntaxException {
        return object.has(key) ? asEffect(object.get(key), key) : defaultEffect;
    }

    public static Fluid asFluid(JsonElement element, String name) throws JsonSyntaxException {
        if (element.isJsonPrimitive()) {
            String string = element.getAsString();
            return (Fluid)Registry.FLUID.getOrEmpty(new Identifier(string)).orElseThrow(() -> {
               return new JsonSyntaxException("Expected " + name + " to be a fluid, was unknown string '" + string + "'");
            });
         } else {
            throw new JsonSyntaxException("Expected " + name + " to be a fluid, was " + getType(element));
         }
    }
    public static Fluid getFluid(JsonObject object, String key) throws JsonSyntaxException {
        if (object.has(key)) {
            return asFluid(object.get(key), key);
         } else {
            throw new JsonSyntaxException("Missing " + key + ", expected to find a status effect");
         }
    }
    public static Fluid getFluidOrDefault(JsonObject object, String key, Fluid defaultFluid) throws JsonSyntaxException {
        return object.has(key) ? asFluid(object.get(key), key) : defaultFluid;
    }

    public static Feature<? extends FeatureConfig> asFeature(JsonElement element, String name) throws JsonSyntaxException {
        if (element.isJsonPrimitive()) {
            String string = element.getAsString();
            return (Feature<?>)Registry.FEATURE.getOrEmpty(new Identifier(string)).orElseThrow(() -> {
               return new JsonSyntaxException("Expected " + name + " to be a feature, was unknown string '" + string + "'");
            });
         } else {
            throw new JsonSyntaxException("Expected " + name + " to be a feature, was " + getType(element));
         }
    }
    public static Feature<? extends FeatureConfig> getFeature(JsonObject object, String key) throws JsonSyntaxException {
        if (object.has(key)) {
            return asFeature(object.get(key), key);
         } else {
            throw new JsonSyntaxException("Missing " + key + ", expected to find a status effect");
         }
    }
    public static Feature<? extends FeatureConfig> getFeatureOrDefault(JsonObject object, String key, Feature<?> defaultFeature) throws JsonSyntaxException {
        return object.has(key) ? asFeature(object.get(key), key) : defaultFeature;
    }

    public static NbtCompound getNbt(JsonObject object, String key) throws JsonSyntaxException {
        try {
            NbtCompound nbtCompound = StringNbtReader.parse(JsonHelper.getString(object, key));
            return nbtCompound;
         } catch (CommandSyntaxException e) {
            throw new JsonSyntaxException(e.getMessage());
         }
    }

    /**
     * methods to get datapack-related things from JSON, these should only be used on the logical server
     */
    public class ServerOnly {
        
        public static CommandFunction getFunction(JsonObject object, World world, String key) {
            try {
                CommandFunctionManager manager = world.getServer().getCommandFunctionManager();
                return manager.getFunction(new Identifier(JsonHelper.getString(object, key))).get();
            } catch (NoSuchElementException e) {
                return null;
            }
        }

        public static void getAndExecuteFunction(JsonObject object, World world, String key, ServerCommandSource source) throws NoSuchElementException {
            CommandFunctionManager manager = world.getServer().getCommandFunctionManager();
            source.withSilent();
            manager.execute(manager.getFunction(new Identifier(JsonHelper.getString(object, key))).get(), source);
        }

    }
}
