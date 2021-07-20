package net.devsdream.crafting;

import net.minecraft.recipe.RecipeSerializer;

public class Serializers {
    public static RecipeSerializer<ShapedNBTRecipe> CRAFTING_SHAPED_NBT = new ShapedNBTRecipe.Serializer();
    public static RecipeSerializer<ShapelessNBTRecipe> CRAFTING_SHAPELESS_NBT = new ShapelessNBTRecipe.Serializer();
    public static RecipeSerializer<StonecuttingNBTRecipe> STONECUTTING_NBT = new CuttingNBTRecipe.Serializer<StonecuttingNBTRecipe>(StonecuttingNBTRecipe::new);
}
