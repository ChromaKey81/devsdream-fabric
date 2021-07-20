package net.devsdream.crafting;

import net.devsdream.Main;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class RecipeTypes {

    static <T extends Recipe<?>> RecipeType<T> register(final String id) {
        return (RecipeType<T>)Registry.register(Registry.RECIPE_TYPE, (Identifier)(new Identifier(Main.MODID, id)), new RecipeType<T>() {
           public String toString() {
              return id;
           }
        });
     }
    

    static RecipeType<StonecuttingNBTRecipe> STONECUTTING_NBT = register("stonecutting_nbt");

}
