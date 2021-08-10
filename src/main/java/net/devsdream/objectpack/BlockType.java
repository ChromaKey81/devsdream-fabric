package net.devsdream.objectpack;

import com.google.gson.JsonObject;

import net.minecraft.util.Identifier;

public class BlockType {

    private final Identifier identifier;

    public BlockType(Identifier identifier, JsonObject object) {
        this.identifier = identifier;
    }

    public Identifier getIdentifier() {
        return this.identifier;
    }
    
    
}
