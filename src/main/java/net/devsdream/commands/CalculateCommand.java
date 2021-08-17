package net.devsdream.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;

public class CalculateCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("calculate").requires((user) -> {
            return user.hasPermissionLevel(2);
        }).then(CommandManager.literal("value")
                        .then(CommandManager.argument("value", IntegerArgumentType.integer()).then(add("value"))
                                .then(subtract("value")).then(multiply("value")).then(divide("value"))
                                .then(modulo("value")).then(equals("value")))).then(CommandManager.literal("command").then(CommandManager.argument("command", StringArgumentType.string()).then(add("command")).then(subtract("command")).then(multiply("command")).then(divide("command")).then(modulo("command")))));
    }

    private static LiteralArgumentBuilder<ServerCommandSource> add(String firstArgumentType) {
        return CommandManager
                .literal(
                        "+")
                .then(CommandManager.literal("value")
                        .then(CommandManager.argument("value2", IntegerArgumentType.integer()).executes((context) -> {
                            return performOperation(context.getSource(), findFirstArgument(context, firstArgumentType),
                                    IntegerArgumentType.getInteger(context, "value2"), "add");
                        }))).then(CommandManager.literal("command").then(CommandManager.argument("command2", StringArgumentType.string()).executes((context) -> {
                            return performOperation(context.getSource(), findFirstArgument(context, firstArgumentType), getValueFromCommand(context, "command2"), "add");
                        })));
    }

    private static LiteralArgumentBuilder<ServerCommandSource> subtract(String firstArgumentType) {
        return CommandManager
                .literal(
                        "-")
                .then(CommandManager.literal("value")
                        .then(CommandManager.argument("value2", IntegerArgumentType.integer()).executes((context) -> {
                            return performOperation(context.getSource(), findFirstArgument(context, firstArgumentType),
                                    IntegerArgumentType.getInteger(context, "value2"), "subtract");
                        }))).then(CommandManager.literal("command").then(CommandManager.argument("command2", StringArgumentType.string()).executes((context) -> {
                            return performOperation(context.getSource(), findFirstArgument(context, firstArgumentType), getValueFromCommand(context, "command2"), "subtract");
                        })));
    }

    private static LiteralArgumentBuilder<ServerCommandSource> multiply(String firstArgumentType) {
        return CommandManager
                .literal(
                        "*")
                .then(CommandManager.literal("value")
                        .then(CommandManager.argument("value2", IntegerArgumentType.integer()).executes((context) -> {
                            return performOperation(context.getSource(), findFirstArgument(context, firstArgumentType),
                                    IntegerArgumentType.getInteger(context, "value2"), "multiply");
                        }))).then(CommandManager.literal("command").then(CommandManager.argument("command2", StringArgumentType.string()).executes((context) -> {
                            return performOperation(context.getSource(), findFirstArgument(context, firstArgumentType), getValueFromCommand(context, "command2"), "multiply");
                        })));
    }

    private static LiteralArgumentBuilder<ServerCommandSource> divide(String firstArgumentType) {
        return CommandManager
                .literal(
                        "/")
                .then(CommandManager.literal("value")
                        .then(CommandManager.argument("value2", IntegerArgumentType.integer()).executes((context) -> {
                            return performOperation(context.getSource(), findFirstArgument(context, firstArgumentType),
                                    IntegerArgumentType.getInteger(context, "value2"), "divide");
                        }))).then(CommandManager.literal("command").then(CommandManager.argument("command2", StringArgumentType.string()).executes((context) -> {
                            return performOperation(context.getSource(), findFirstArgument(context, firstArgumentType), getValueFromCommand(context, "command2"), "divide");
                        })));
    }

    private static LiteralArgumentBuilder<ServerCommandSource> modulo(String firstArgumentType) {
        return CommandManager
                .literal(
                        "%")
                .then(CommandManager.literal("value")
                        .then(CommandManager.argument("value2", IntegerArgumentType.integer()).executes((context) -> {
                            return performOperation(context.getSource(), findFirstArgument(context, firstArgumentType),
                                    IntegerArgumentType.getInteger(context, "value2"), "modulo");
                        }))).then(CommandManager.literal("command").then(CommandManager.argument("command2", StringArgumentType.string()).executes((context) -> {
                            return performOperation(context.getSource(), findFirstArgument(context, firstArgumentType), getValueFromCommand(context, "command2"), "modulo");
                        })));
    }
    private static LiteralArgumentBuilder<ServerCommandSource> equals(String firstArgumentType) {
        return CommandManager
                .literal(
                        "=")
                .then(CommandManager.literal("value")
                        .then(CommandManager.argument("value2", IntegerArgumentType.integer()).executes((context) -> {
                            return performOperation(context.getSource(), findFirstArgument(context, firstArgumentType),
                                    IntegerArgumentType.getInteger(context, "value2"), "equals");
                        }))).then(CommandManager.literal("command").then(CommandManager.argument("command2", StringArgumentType.string()).executes((context) -> {
                            return performOperation(context.getSource(), findFirstArgument(context, firstArgumentType), getValueFromCommand(context, "command2"), "equals");
                        })));
    }

    private static int findFirstArgument(CommandContext<ServerCommandSource> context, String firstArgumentType)
            throws CommandSyntaxException {
        switch (firstArgumentType) {
            case "value": {
                return IntegerArgumentType.getInteger(context, "value");
            }
            case "command": {
                return getValueFromCommand(context, "command");
            }
            default: {
                return 69;
            }
        }
    }

    private static int getValueFromCommand(CommandContext<ServerCommandSource> context, String argument) throws CommandSyntaxException {
        return context.getSource().getServer().getCommandManager().getDispatcher().execute(StringArgumentType.getString(context, argument), context.getSource());
    }

    private static int performOperation(ServerCommandSource source, int valueSoFar, int input, String type) {
        int newVal = 0;
        switch (type) {
            case "add": {
                newVal = valueSoFar + input;
                break;
            }
            case "subtract": {
                newVal = valueSoFar - input;
                break;
            }
            case "multiply": {
                newVal = valueSoFar * input;
                break;
            }
            case "divide": {
                newVal = valueSoFar / input;
                break;
            }
            case "modulo": {
                newVal = valueSoFar % input;
                break;
            }
            case "equals": {
                newVal = valueSoFar == input ? 1 : 0;
                break;
            }
        }
        source.sendFeedback(new TranslatableText("commands.devsdream.calculate.success", newVal), true);
        return newVal;
    }
}