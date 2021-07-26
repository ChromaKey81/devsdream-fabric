package net.devsdream.crafting;

import com.google.gson.JsonObject;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapedRecipe;

import net.minecraft.recipe.RecipeSerializer;

import net.devsdream.util.ChromaJsonHelper;

public class ShapedNBTRecipe extends ShapedRecipe {

    public ShapedNBTRecipe(Identifier id, String group, int width, int height, DefaultedList<Ingredient> input,
            ItemStack output) {
        super(id, group, width, height, input, output);
    }

    public RecipeSerializer<?> getSerializer() {
        return Serializers.CRAFTING_SHAPED_NBT;
    }

    public static ItemStack outputFromJson(JsonObject json) {
      ItemStack base = ShapedRecipe.outputFromJson(json);
      base.setNbt(ChromaJsonHelper.getNbt(json, "nbt"));
      return base;
    }

    public static class Serializer implements RecipeSerializer<ShapedNBTRecipe> {
        public ShapedNBTRecipe read(Identifier identifier, JsonObject jsonObject) {
         String string = JsonHelper.getString(jsonObject, "group", "");
         Map<String, Ingredient> map = ShapedRecipe.readSymbols(JsonHelper.getObject(jsonObject, "key"));
         String[] strings = ShapedRecipe.removePadding(ShapedRecipe.getPattern(JsonHelper.getArray(jsonObject, "pattern")));
         int i = strings[0].length();
         int j = strings.length;
         DefaultedList<Ingredient> defaultedList = ShapedRecipe.createPatternMatrix(strings, map, i, j);
         ItemStack itemStack = ShapedNBTRecipe.outputFromJson(JsonHelper.getObject(jsonObject, "result"));
         return new ShapedNBTRecipe(identifier, string, i, j, defaultedList, itemStack);
        }
  
        public ShapedNBTRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
           int i = packetByteBuf.readVarInt();
           int j = packetByteBuf.readVarInt();
           String string = packetByteBuf.readString();
           DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(i * j, Ingredient.EMPTY);

           for (int k = 0; k < defaultedList.size(); ++k) {
              defaultedList.set(k, Ingredient.fromPacket(packetByteBuf));
           }

           ItemStack itemStack = packetByteBuf.readItemStack();
           return new ShapedNBTRecipe(identifier, string, i, j, defaultedList, itemStack);
        }

        public void write(PacketByteBuf packetByteBuf, ShapedNBTRecipe shapedRecipe) {
           packetByteBuf.writeVarInt(shapedRecipe.getWidth());
           packetByteBuf.writeVarInt(shapedRecipe.getHeight());
           packetByteBuf.writeString(shapedRecipe.getGroup());
           Iterator var3 = shapedRecipe.getIngredients().iterator();

           while (var3.hasNext()) {
              Ingredient ingredient = (Ingredient) var3.next();
              ingredient.write(packetByteBuf);
           }

           packetByteBuf.writeItemStack(shapedRecipe.getOutput());
        }
     }
    
}