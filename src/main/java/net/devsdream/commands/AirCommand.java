package net.devsdream.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;

import java.util.Collection;
import java.util.List;

import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;

public class AirCommand {
    private static final SimpleCommandExceptionType AIR_SET_FAILED_EXCEPTION = new SimpleCommandExceptionType(
            new TranslatableText("commands.devsdream.air.set.failed"));
    private static final SimpleCommandExceptionType AIR_ADD_FAILED_EXCEPTION = new SimpleCommandExceptionType(
            new TranslatableText("commands.devsdream.air.add.failed"));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("air").requires((user) -> {
            return user.hasPermissionLevel(2);
        }).then(CommandManager.argument("targets", EntityArgumentType.entities()).then(CommandManager.literal("set")
                .then(CommandManager.argument("amount", IntegerArgumentType.integer(0)).executes((run) -> {
                    return setAir(run.getSource(), EntityArgumentType.getEntities(run, "targets"),
                            IntegerArgumentType.getInteger(run, "amount"));
                }))).then(CommandManager.literal("add")
                        .then(CommandManager.argument("amount", IntegerArgumentType.integer()).executes((run) -> {
                            return increaseAir(run.getSource(), EntityArgumentType.getEntities(run, "targets"),
                                    IntegerArgumentType.getInteger(run, "amount"));
                        })))));
    }

    private static int setAir(ServerCommandSource source, Collection<? extends Entity> targets, int amount)
            throws CommandSyntaxException {
        int i = 0;

        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                if (amount <= ((LivingEntity) entity).getMaxAir()) {
                    ((LivingEntity) entity).setAir(amount);
                    i++;
                } else {
                    ((LivingEntity) entity).setAir(((LivingEntity) entity).getMaxAir());
                }
            }
        }

        if (i == 0) {
            throw AIR_SET_FAILED_EXCEPTION.create();
        } else {
            if (targets.size() == 1) {
                source.sendFeedback(new TranslatableText("commands.devsdream.air.set.success.single",
                        targets.iterator().next().getDisplayName(), amount), true);
            } else {
                source.sendFeedback(new TranslatableText("commands.devsdream.air.set.success.multiple",
                        targets.size(), amount), true);
            }

            return i;
        }
    }

    private static int increaseAir(ServerCommandSource source, Collection<? extends Entity> targets, int amount)
            throws CommandSyntaxException {
        List<Entity> list = Lists.newArrayListWithCapacity(targets.size());

        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                if (((LivingEntity) entity).getAir() + amount <= ((LivingEntity) entity).getMaxAir()) {
                    ((LivingEntity) entity).setAir(((LivingEntity) entity).getAir() + amount);
                    list.add(entity);
                } else {
                    ((LivingEntity) entity).setAir(((LivingEntity) entity).getMaxAir());
                    list.add(entity);
                }
            }
        }

        if (list.isEmpty()) {
            throw AIR_ADD_FAILED_EXCEPTION.create();
        } else {
            if (list.size() == 1) {
                source.sendFeedback(new TranslatableText("commands.devsdream.air.add.success.single",
                        list.iterator().next().getDisplayName(), amount), true);
            } else {
                source.sendFeedback(new TranslatableText("commands.devsdream.air.add.success.multiple",
                        list.size(), amount), true);
            }

            return amount;
        }
    }
}