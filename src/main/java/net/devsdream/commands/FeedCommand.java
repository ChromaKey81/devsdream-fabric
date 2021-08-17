package net.devsdream.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;

import java.util.Collection;
import java.util.List;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.TranslatableText;

public class FeedCommand {
    private static final SimpleCommandExceptionType FEED_FAILED_EXCEPTION = new SimpleCommandExceptionType(
            new TranslatableText("commands.devsdream.feed.failed"));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("feed").requires((user) -> {
            return user.hasPermissionLevel(2);
        }).then(CommandManager.argument("targets", EntityArgumentType.players())
                .then(CommandManager.argument("foodLevel", IntegerArgumentType.integer()).executes((feed) -> {
                    return feedPlayer(feed.getSource(), EntityArgumentType.getPlayers(feed, "targets"),
                            IntegerArgumentType.getInteger(feed, "foodLevel"), 0);
                }).then(CommandManager.argument("saturation", FloatArgumentType.floatArg()).executes((feedWithSaturation) -> {
                    return feedPlayer(feedWithSaturation.getSource(),
                            EntityArgumentType.getPlayers(feedWithSaturation, "targets"),
                            IntegerArgumentType.getInteger(feedWithSaturation, "foodLevel"),
                            FloatArgumentType.getFloat(feedWithSaturation, "saturation"));
                })))));
    }

    private static int feedPlayer(ServerCommandSource source, Collection<? extends PlayerEntity> targets, int foodLevel,
            float saturation) throws CommandSyntaxException {
        List<PlayerEntity> list = Lists.newArrayListWithCapacity(targets.size());

        for (PlayerEntity player : targets) {
            if (player instanceof PlayerEntity) {
                ((PlayerEntity) player).getHungerManager().add(foodLevel, saturation);
                list.add(player);
            }
        }

        if (list.isEmpty()) {
            throw FEED_FAILED_EXCEPTION.create();
        } else {
            if (list.size() == 1) {
                source.sendFeedback(new TranslatableText("commands.devsdream.feed.success.single",
                        list.iterator().next().getDisplayName(), foodLevel, saturation), true);
            } else {
                source.sendFeedback(new TranslatableText("commands.devsdream.feed.success.multiple",
                        list.size(), foodLevel, saturation), true);
            }

            return list.size();
        }
    }
}