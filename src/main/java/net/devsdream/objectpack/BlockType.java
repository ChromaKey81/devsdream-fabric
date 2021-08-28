package net.devsdream.objectpack;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.devsdream.util.ChromaJsonHelper;
import net.minecraft.block.BlockState;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.server.function.CommandFunction;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class BlockType extends ObjectType {

    protected StatePredicate hasRandomTicks = null;
    protected Object propogatesSkylightDown = null;
    protected Identifier animateTick = null;
    protected Identifier onDestroy = null;
    protected Identifier onDestroyExplosion = null;
    protected Identifier onStep = null;
    protected Identifier onPlayerBreak = null;
    protected final Map<BlockState, LocationPredicate> placementStateMap = new HashMap<BlockState, LocationPredicate>();

    public BlockType(Identifier identifier, JsonObject object) throws JsonSyntaxException {
        super(identifier, object);
        if (object != null) {
            if (object.get("randomly_ticking") != null) this.hasRandomTicks = StatePredicate.fromJson(object.get("randomly_ticking"));
            if (object.get("propogates_skylight_down") != null) {
                JsonElement element = object.get("propogates_skylight_down");
                if (element.isJsonObject()) {
                    this.propogatesSkylightDown = StatePredicate.fromJson(object);
                } else {
                    this.propogatesSkylightDown = JsonHelper.asBoolean(object, "boolean or block state predicate");
                }
            }
            this.animateTick = identifierOrNull(object, "animate_tick");
            this.onDestroy = identifierOrNull(object, "on_destroyed");
            this.onDestroyExplosion = identifierOrNull(object, "on_exploded");
            this.onStep = identifierOrNull(object, "on_step");
            this.onPlayerBreak = identifierOrNull(object, "on_player_break");
        }
    }

    static Identifier identifierOrNull(JsonObject object, String key) {
        String identifier = ChromaJsonHelper.getStringOrDefault(object, key, null);
        if (identifier != null){
            return new Identifier(identifier);
        } else {
            return null;
        }
    }

    static CommandFunction getFunction(World world, Identifier identifier) {
        return world.getServer().getCommandFunctionManager().getFunction(identifier).orElseGet(() -> null);
    }

    static CommandFunction getFunctionWorldAccess(WorldAccess world, Identifier identifier) {
        return world.getServer().getCommandFunctionManager().getFunction(identifier).orElseGet(() -> null);
    }

    public StatePredicate getRandomTickPredicate() {
        return this.hasRandomTicks;
    }

    public Object getPropogatesSkylightDown(BlockState state) {
        if (this.propogatesSkylightDown != null) {
            if (this.propogatesSkylightDown instanceof StatePredicate) {
                return ((StatePredicate)this.propogatesSkylightDown).test(state);
            } else {
                return (boolean)this.propogatesSkylightDown;
            }
        } else {
            return null;
        }
    }

    public CommandFunction getAnimateTickFunction(World world) {
        return getFunction(world, this.animateTick);
    }

    public CommandFunction getOnDestroyFunction(WorldAccess world) {
        return getFunctionWorldAccess(world, this.onDestroy);
    }

    public CommandFunction getOnDestroyedByExplosionFunction(World world) {
        return getFunction(world, this.onDestroyExplosion);
    }

    public CommandFunction getStepOnFunction(World world) {
        return getFunction(world, this.onStep);
    }

    public CommandFunction getOnPlayerDestroyFunction(World world) {
        return getFunction(world, this.onPlayerBreak);
    }
    
}
