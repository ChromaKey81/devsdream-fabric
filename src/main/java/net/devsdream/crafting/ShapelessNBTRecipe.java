package net.devsdream.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;

public class ShapelessNBTRecipe extends ShapelessRecipe {

    public ShapelessNBTRecipe(Identifier id, String group, ItemStack output, DefaultedList<Ingredient> input) {
        super(id, group, output, input);
    }

    public RecipeSerializer<?> getSerializer() {
        return Serializers.CRAFTING_SHAPELESS_NBT;
    }

    public static class Serializer implements RecipeSerializer<ShapelessNBTRecipe> {

        public ShapelessNBTRecipe read(Identifier identifier, JsonObject jsonObject) {
            String string = JsonHelper.getString(jsonObject, "group", "");
            DefaultedList<Ingredient> defaultedList = getIngredients(JsonHelper.getArray(jsonObject, "ingredients"));
            if (defaultedList.isEmpty()) {
               throw new JsonParseException("No ingredients for shapeless recipe");
            } else if (defaultedList.size() > 9) {
               throw new JsonParseException("Too many ingredients for shapeless recipe");
            } else {
               ItemStack itemStack = ShapedNBTRecipe.outputFromJson(JsonHelper.getObject(jsonObject, "result"));
               return new ShapelessNBTRecipe(identifier, string, itemStack, defaultedList);
            }
         }
   
         private static DefaultedList<Ingredient> getIngredients(JsonArray json) {
            return ShapelessRecipe.Serializer.getIngredients(json);
         }
   
         public ShapelessNBTRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
            String string = packetByteBuf.readString();
            int i = packetByteBuf.readVarInt();
            DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(i, Ingredient.EMPTY);
   
            for(int j = 0; j < defaultedList.size(); ++j) {
               defaultedList.set(j, Ingredient.fromPacket(packetByteBuf));
            }
   
            ItemStack itemStack = packetByteBuf.readItemStack();
            return new ShapelessNBTRecipe(identifier, string, itemStack, defaultedList);
         }
   
         public void write(PacketByteBuf packetByteBuf, ShapelessNBTRecipe shapelessRecipe) {
            new ShapelessRecipe.Serializer().write(packetByteBuf, shapelessRecipe);
         }

    }
    
}
