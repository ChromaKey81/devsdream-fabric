package net.devsdream.crafting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.devsdream.util.ChromaJsonHelper;
import net.devsdream.Main;

import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.command.argument.NbtPathArgumentType.NbtPath;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.function.CopyNbtLootFunction;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SmithingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class SmithingNBTRecipe extends SmithingRecipe {

    private final Collection<NbtPath> overrides;

    public SmithingNBTRecipe(Identifier id, Ingredient base, Ingredient addition, ItemStack result, Collection<NbtPath> overrides) {
        super(id, base, addition, result);
        this.overrides = overrides;
    }

    public Collection<NbtPath> getOverrides() {
        return this.overrides;
    }

    @Override
    public ItemStack craft(Inventory inventory) {
        ItemStack out = super.craft(inventory).copy();
        out.setNbt(this.getOutput().copy().getNbt());
        this.getOverrides().forEach((path) -> {
            ItemStack base = inventory.getStack(0).copy();
            if (base.getNbt() != null) {
                try {
                    List<NbtElement> source = path.get(base.getNbt().copy());
                    if (!source.isEmpty()) {
                    CopyNbtLootFunction.Operator.REPLACE.merge(out.getOrCreateNbt(), path, source);
                    }
                } catch (CommandSyntaxException e) {
                }
            }
        });
        return out;
    }

    public static class Serializer implements RecipeSerializer<SmithingNBTRecipe> {
        public SmithingNBTRecipe read(Identifier identifier, JsonObject jsonObject) {
           Ingredient ingredient = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "base"));
           Ingredient ingredient2 = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "addition"));
           JsonObject resultObj = JsonHelper.getObject(jsonObject, "result");
           ItemStack itemStack = ShapedNBTRecipe.outputFromJson(resultObj);
           List<NbtPath> overrides = new ArrayList<NbtPath>();
           ChromaJsonHelper.getArrayOrDefault(resultObj, "overrides", new JsonArray()).forEach((element) -> {
               String override = JsonHelper.asString(element, "every override");
               try {
                overrides.add(new NbtPathArgumentType().parse(new StringReader(override)));
               } catch (CommandSyntaxException e) {
                   throw new JsonSyntaxException("Invalid NBT path: " + e.getMessage());
               }
           });
           return new SmithingNBTRecipe(identifier, ingredient, ingredient2, itemStack, overrides);
        }
  
        public SmithingNBTRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
           Ingredient ingredient = Ingredient.fromPacket(packetByteBuf);
           Ingredient ingredient2 = Ingredient.fromPacket(packetByteBuf);
           ItemStack itemStack = packetByteBuf.readItemStack();
           List<NbtPath> overrides = packetByteBuf.readList((buffy) -> {
               try {
                   return new NbtPathArgumentType().parse(new StringReader(buffy.readString()));
               } catch (CommandSyntaxException e) {
                   throw new JsonSyntaxException("if you see this error, you've encountered a bug. please report it ASAP to the github repo at https://github.com/ChromaKey81/devsdream-fabric");
               }
           });
           return new SmithingNBTRecipe(identifier, ingredient, ingredient2, itemStack, overrides);
        }
  
        public void write(PacketByteBuf packetByteBuf, SmithingNBTRecipe smithingRecipe) {
           smithingRecipe.getIngredients().get(0).write(packetByteBuf);
           smithingRecipe.getIngredients().get(1).write(packetByteBuf);
           packetByteBuf.writeItemStack(smithingRecipe.getOutput());
           packetByteBuf.writeCollection(smithingRecipe.getOverrides(), (buffy, element) -> {
               buffy.writeString(element.toString());
           });
        }
     }
    
}
