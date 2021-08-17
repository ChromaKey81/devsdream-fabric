package net.devsdream.commands;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;

public class DamageItemCommand {
    private static final SimpleCommandExceptionType DAMAGEITEM_FAILED_EXCEPTION = new SimpleCommandExceptionType(
            new TranslatableText("commands.devsdream.damageitem.failed"));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("damageitem").requires((user) -> {
            return user.hasPermissionLevel(2);
        }).then(CommandManager.argument("targets", EntityArgumentType.players())
                .then(CommandManager.argument("amount", IntegerArgumentType.integer())
                        .then(CommandManager.literal("mainhand").executes((damage) -> {
                            return damageItem(damage.getSource(), EntityArgumentType.getPlayers(damage, "targets"),
                                    EquipmentSlot.MAINHAND, IntegerArgumentType.getInteger(damage, "amount"));
                        })).then(CommandManager.literal("offhand").executes((damage) -> {
                            return damageItem(damage.getSource(), EntityArgumentType.getPlayers(damage, "targets"),
                                    EquipmentSlot.OFFHAND, IntegerArgumentType.getInteger(damage, "amount"));
                        })).then(CommandManager.literal("head").executes((damage) -> {
                            return damageItem(damage.getSource(), EntityArgumentType.getPlayers(damage, "targets"),
                                    EquipmentSlot.HEAD, IntegerArgumentType.getInteger(damage, "amount"));
                        })).then(CommandManager.literal("chest").executes((damage) -> {
                            return damageItem(damage.getSource(), EntityArgumentType.getPlayers(damage, "targets"),
                                    EquipmentSlot.CHEST, IntegerArgumentType.getInteger(damage, "amount"));
                        })).then(CommandManager.literal("legs").executes((damage) -> {
                            return damageItem(damage.getSource(), EntityArgumentType.getPlayers(damage, "targets"),
                                    EquipmentSlot.LEGS, IntegerArgumentType.getInteger(damage, "amount"));
                        })).then(CommandManager.literal("feet").executes((damage) -> {
                            return damageItem(damage.getSource(), EntityArgumentType.getPlayers(damage, "targets"),
                                    EquipmentSlot.FEET, IntegerArgumentType.getInteger(damage, "amount"));
                        })))));
    }

    private static int damageItem(ServerCommandSource source, Collection<? extends ServerPlayerEntity> targets,
            EquipmentSlot slot, int amount) throws CommandSyntaxException {
        Map<ServerPlayerEntity, ItemStack> map = Maps.newHashMapWithExpectedSize(targets.size());

        for (ServerPlayerEntity player : targets) {
            ItemStack targetItem = player.getEquippedStack(slot);
            targetItem.damage(amount, player, (p) -> {
                p.sendEquipmentBreakStatus(slot);
            });
            map.put(player, targetItem);
        }

        if (map.isEmpty()) {
            throw DAMAGEITEM_FAILED_EXCEPTION.create();
        } else {
            if (map.size() == 1) {
                map.forEach((player, itemStack) -> {
                    source.sendFeedback(new TranslatableText("commands.devsdream.damageitem.success.single",
                            player.getDisplayName(), itemStack.getName(), amount), true);
                });
            } else {
                source.sendFeedback(new TranslatableText("commands.devsdream.damageitem.success.multiple",
                        map.size(), amount), true);
            }

            return map.size();
        }
    }
}