package net.devsdream.objectpack;

import com.google.gson.JsonObject;

import net.devsdream.util.ChromaJsonHelper;
import net.minecraft.util.Identifier;

public class BlockType {

    private final boolean dropsItemsOnExplosion;
    private final Identifier identifier;

    public BlockType(Identifier identifier, JsonObject object) {
        this.identifier = identifier;
        this.dropsItemsOnExplosion = ChromaJsonHelper.getBooleanOrDefault(object, "drops_items_on_explosion", true);
    }

    public Identifier getIdentifier() {
        return this.identifier;
    }

    public boolean getDropsItemsOnExplosion() {
        return this.dropsItemsOnExplosion;
    }
    
    
}
