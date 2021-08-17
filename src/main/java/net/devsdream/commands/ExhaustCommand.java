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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.TranslatableText;

public class ExhaustCommand {
    private static final SimpleCommandExceptionType EXHAUST_FAILED_EXCEPTION = new SimpleCommandExceptionType(
            new TranslatableText("commands.devsdream.exhaust.failed"));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("exhaust").requires((user) -> {
            return user.hasPermissionLevel(2);
        }).then(CommandManager.argument("targets", EntityArgumentType.players())
                .then(CommandManager.argument("amount", FloatArgumentType.floatArg(0)).executes((exhaust) -> {
                    return exhaustPlayer(exhaust.getSource(), EntityArgumentType.getPlayers(exhaust, "targets"),
                            FloatArgumentType.getFloat(exhaust, "amount"));
                }))));
    }

    private static int exhaustPlayer(ServerCommandSource source, Collection<? extends PlayerEntity> targets, float amount)
            throws CommandSyntaxException {

        List<PlayerEntity> list = Lists.newArrayListWithCapacity(targets.size());

        for (PlayerEntity entity : targets) {
            if (entity instanceof PlayerEntity) {
                ((PlayerEntity) entity).getHungerManager().addExhaustion(amount);
                list.add(entity);
            }
        }

        if (list.isEmpty()) {
            throw EXHAUST_FAILED_EXCEPTION.create();
        } else {
            if (list.size() == 1) {
                source.sendFeedback(new TranslatableText("commands.devsdream.exhaust.success.single",
                        list.iterator().next().getDisplayName(), amount), true);
            } else {
                source.sendFeedback(new TranslatableText("commands.devsdream.exhaust.success.multiple",
                        list.size(), amount), true);
            }

            return list.size();
        }
    }
}