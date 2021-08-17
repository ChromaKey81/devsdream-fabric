package net.devsdream.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;

import java.util.Collection;
import java.util.List;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.TranslatableText;

public class HealthCommand {
    private static final SimpleCommandExceptionType HEALTH_ADJUST_FAILED_EXCEPTION = new SimpleCommandExceptionType(
            new TranslatableText("commands.devsdream.health.add.failed"));
    private static final SimpleCommandExceptionType HEALTH_SET_FAILED_EXCEPTION = new SimpleCommandExceptionType(
            new TranslatableText("commands.devsdream.health.set.failed"));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("health").requires((user) -> {
            return user.hasPermissionLevel(2);
        }).then(CommandManager.literal("add").then(CommandManager.argument("targets", EntityArgumentType.entities())
                .then(CommandManager.argument("amount", FloatArgumentType.floatArg(0)).executes((heal) -> {
                    return healEntity(heal.getSource(), EntityArgumentType.getEntities(heal, "targets"),
                            FloatArgumentType.getFloat(heal, "amount"));
                })))).then(CommandManager.literal("set").then(CommandManager.argument("targets", EntityArgumentType.entities())
                        .then(CommandManager.argument("amount", FloatArgumentType.floatArg(0)).executes((set) -> {
                            return setEntityHealth(set.getSource(), EntityArgumentType.getEntities(set, "targets"),
                                    FloatArgumentType.getFloat(set, "amount"));
                        })))));
    }

    private static int healEntity(ServerCommandSource source, Collection<? extends Entity> targets, float amount)
            throws CommandSyntaxException {
        List<Entity> list = Lists.newArrayListWithCapacity(targets.size());

        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                ((LivingEntity) entity).heal(amount);
                list.add(entity);
            }
        }

        if (list.isEmpty()) {
            throw HEALTH_ADJUST_FAILED_EXCEPTION.create();
        } else {
            if (list.size() == 1) {
                source.sendFeedback(new TranslatableText("commands.devsdream.health.add.success.single",
                        list.iterator().next().getDisplayName(), amount), true);
            } else {
                source.sendFeedback(new TranslatableText("commands.devsdream.health.add.success.multiple",
                        list.size(), amount), true);
            }

            return list.size();
        }
    }

    private static int setEntityHealth(ServerCommandSource source, Collection<? extends Entity> targets, float amount)
            throws CommandSyntaxException {
        List<Entity> list = Lists.newArrayListWithCapacity(targets.size());

        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                ((LivingEntity) entity).setHealth(amount);
                list.add(entity);
            }
        }

        if (list.isEmpty()) {
            throw HEALTH_SET_FAILED_EXCEPTION.create();
        } else {
            if (list.size() == 1) {
                source.sendFeedback(new TranslatableText("commands.devsdream.health.set.success.single",
                        list.iterator().next().getDisplayName(), amount), true);
            } else {
                source.sendFeedback(new TranslatableText("commands.devsdream.health.set.success.multiple",
                        list.size(), amount), true);
            }

            return (int) amount;
        }
    }
}