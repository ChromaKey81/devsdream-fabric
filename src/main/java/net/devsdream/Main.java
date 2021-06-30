package net.devsdream;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Main implements ModInitializer {

    @Override
    public void onInitialize() {
        final Item FABRIC_ITEM = new Item(new Item.Settings().maxDamageIfAbsent(3));
        Registry.register(Registry.ITEM, new Identifier("wee", "woo"), FABRIC_ITEM);
    }
    
}
