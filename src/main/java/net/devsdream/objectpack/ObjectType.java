package net.devsdream.objectpack;

import com.google.gson.JsonObject;

import net.minecraft.util.Identifier;

public abstract class ObjectType {
    private final Identifier identifier;

    public ObjectType(Identifier identifier, JsonObject object) {
        this.identifier = identifier;
    }

    public Identifier getIdentifier() {
        return this.identifier;
    }
}
