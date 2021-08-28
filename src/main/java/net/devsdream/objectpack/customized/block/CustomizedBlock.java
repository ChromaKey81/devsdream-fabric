package net.devsdream.objectpack.customized.block;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

import net.devsdream.objectpack.BlockType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.function.CommandFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.explosion.Explosion;

public class CustomizedBlock extends Block {

    private final BlockType type;

    public CustomizedBlock(Settings settings, BlockType type) {
        super(settings);
        this.type = type;
    }

    static void executeFunction(Function<World, CommandFunction> function, World world, Function<World, ServerCommandSource> source, Consumer<?> elseDo) {
        if (!world.isClient && function != null) {
            world.getServer().getCommandFunctionManager().execute(function.apply(world), source.apply(world));
        } else {
            elseDo.accept(null);
        }
    }

    static void executeFunctionWorldAccess(Function<WorldAccess, CommandFunction> function, WorldAccess world, Function<WorldAccess, ServerCommandSource> source, Consumer<?> elseDo) {
        if (function != null) {
            world.getServer().getCommandFunctionManager().execute(function.apply(world), source.apply(world));
        } else {
            elseDo.accept(null);
        }
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        if (this.type.getRandomTickPredicate() != null) {
            return this.type.getRandomTickPredicate().test(state);
        } else {
            return super.hasRandomTicks(state);
        }
    }
    
    @Override
    public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
        if (this.type.getPropogatesSkylightDown(state) != null) {
            return (boolean)this.type.getPropogatesSkylightDown(state);
        } else {
            return super.isTranslucent(state, world, pos);
        }
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        executeFunction((w) -> this.type.getAnimateTickFunction(w), world, (w) -> w.getServer().getCommandSource().withPosition(Vec3d.of(pos)), (w) -> {
            super.randomDisplayTick(state, world, pos, random);
        });
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        executeFunctionWorldAccess((w) -> this.type.getOnDestroyFunction(w), world, (w) -> w.getServer().getCommandSource().withPosition(Vec3d.of(pos)), (w) -> {
            super.onBroken(world, pos, state);
        });
    }

    @Override
    public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
        executeFunction((w) -> this.type.getOnDestroyedByExplosionFunction(w), world, (w) -> {
            ServerCommandSource source = w.getServer().getCommandSource().withPosition(Vec3d.of(pos));
            if (explosion.getCausingEntity() != null) {
                source.withEntity(explosion.getCausingEntity());
            }
            return source;
        }, (w) -> {
            super.onDestroyedByExplosion(world, pos, explosion);
        });
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        executeFunction((w) -> this.type.getStepOnFunction(w), world, (w) -> w.getServer().getCommandSource().withPosition(Vec3d.of(pos)).withEntity(entity), (w) -> {
            super.onSteppedOn(world, pos, state, entity);
        });
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
        executeFunction((w) -> this.type.getOnPlayerDestroyFunction(w), world, (w) -> w.getServer().getCommandSource().withPosition(Vec3d.of(pos)).withEntity(player), (w) -> {
            super.afterBreak(world, player, pos, state, blockEntity, stack);
        });
    }
}
