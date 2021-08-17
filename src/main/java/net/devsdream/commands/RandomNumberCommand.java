package net.devsdream.commands;

import java.util.Random;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.TranslatableText;

public class RandomNumberCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralCommandNode<ServerCommandSource> literalCommandNode = dispatcher
                .register(CommandManager.literal("randomnumber").requires((user) -> {
                    return user.hasPermissionLevel(2);
                }).then(CommandManager.argument("maximum", IntegerArgumentType.integer(0, 2147483647)).executes((rng) -> {
                    return rng(rng.getSource(), IntegerArgumentType.getInteger(rng, "maximum"));
                })));

        dispatcher.register(CommandManager.literal("rng").requires((user) -> {
            return user.hasPermissionLevel(2);
        }).redirect(literalCommandNode));
    }

    private static int rng(ServerCommandSource source, int maximum) {
        Random rand = new Random();
        int result = rand.nextInt(maximum);
        source.sendFeedback(new TranslatableText("commands.devsdream.rng.result", result), true);
        return result;
    }
}