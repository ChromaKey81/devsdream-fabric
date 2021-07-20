package net.devsdream.crafting;

import com.google.gson.JsonObject;

import net.devsdream.util.ChromaJsonHelper;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.CuttingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

public abstract class CuttingNBTRecipe extends CuttingRecipe {

    public CuttingNBTRecipe(RecipeType<?> type, RecipeSerializer<?> serializer, Identifier id, String group,
            Ingredient input, ItemStack output) {
        super(type, serializer, id, group, input, output);
    }

    public static class Serializer<T extends CuttingNBTRecipe> implements RecipeSerializer<T> {
        final CuttingNBTRecipe.Serializer.RecipeFactory<T> recipeFactory;
  
        protected Serializer(CuttingNBTRecipe.Serializer.RecipeFactory<T> recipeFactory) {
           this.recipeFactory = recipeFactory;
        }
  
        public T read(Identifier identifier, JsonObject jsonObject) {
           String string = JsonHelper.getString(jsonObject, "group", "");
           Ingredient ingredient2;
           if (JsonHelper.hasArray(jsonObject, "ingredient")) {
              ingredient2 = Ingredient.fromJson(JsonHelper.getArray(jsonObject, "ingredient"));
           } else {
              ingredient2 = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "ingredient"));
           }
  
           String string2 = JsonHelper.getString(jsonObject, "result");
           int i = JsonHelper.getInt(jsonObject, "count");
           ItemStack itemStack = new ItemStack((ItemConvertible)Registry.ITEM.get(new Identifier(string2)), i);
           itemStack.setNbt(ChromaJsonHelper.getNbt(jsonObject, "nbt"));
           return this.recipeFactory.create(identifier, string, ingredient2, itemStack);
        }
  
        public T read(Identifier identifier, PacketByteBuf packetByteBuf) {
           String string = packetByteBuf.readString();
           Ingredient ingredient = Ingredient.fromPacket(packetByteBuf);
           ItemStack itemStack = packetByteBuf.readItemStack();
           return this.recipeFactory.create(identifier, string, ingredient, itemStack);
        }
  
        public void write(PacketByteBuf packetByteBuf, T cuttingRecipe) {
           packetByteBuf.writeString(cuttingRecipe.group);
           cuttingRecipe.input.write(packetByteBuf);
           packetByteBuf.writeItemStack(cuttingRecipe.output);
        }
  
        interface RecipeFactory<T extends CuttingNBTRecipe> {
           T create(Identifier id, String group, Ingredient input, ItemStack output);
        }
     }
    
}
