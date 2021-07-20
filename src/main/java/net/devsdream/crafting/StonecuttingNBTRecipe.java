package net.devsdream.crafting;

import net.minecraft.block.Blocks;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class StonecuttingNBTRecipe extends CuttingNBTRecipe {
    
    public StonecuttingNBTRecipe(Identifier id, String group, Ingredient input, ItemStack output) {
       super(RecipeTypes.STONECUTTING_NBT, RecipeSerializer.STONECUTTING, id, group, input, output);
    }
 
    public boolean matches(Inventory inventory, World world) {
       return this.input.test(inventory.getStack(0));
    }
 
    public ItemStack createIcon() {
       return new ItemStack(Blocks.STONECUTTER);
    }
 }