package net.devsdream.commands;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

import com.google.common.collect.Maps;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.ItemSlotArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;

public class DamageItemCommand {
    private static final SimpleCommandExceptionType DAMAGEITEM_FAILED_EXCEPTION = new SimpleCommandExceptionType(
            new TranslatableText("commands.devsdream.damageitem.failed"));
            private static final SimpleCommandExceptionType NO_ITEM_EXCEPTION = new SimpleCommandExceptionType(
                new TranslatableText("commands.devsdream.damageitem.failed.no_item"));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("damageitem").requires((user) -> {
            return user.hasPermissionLevel(2);
        }).then(CommandManager.argument("targets", EntityArgumentType.entities())
                .then(CommandManager.argument("amount", IntegerArgumentType.integer()).then(CommandManager.argument("slot", ItemSlotArgumentType.itemSlot()).executes((context) -> {
                    return damageItem(context.getSource(), EntityArgumentType.getEntities(context, "targets"), ItemSlotArgumentType.getItemSlot(context, "slot"), IntegerArgumentType.getInteger(context, "amount"));
                })))));
    }

    private static int damageItem(ServerCommandSource source, Collection<? extends Entity> targets,
            int slot, int amount) throws CommandSyntaxException {
        Map<LivingEntity, ItemStack> map = Maps.newHashMapWithExpectedSize(targets.size());

        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                LivingEntity target = (LivingEntity)entity;
                    StackReference stackReference = target.getStackReference(slot);
                    if (stackReference == StackReference.EMPTY) {
                        throw NO_ITEM_EXCEPTION.create();
                    } else {
                        stackReference.get().damage(amount, target, (p) -> {
                                if (slot == 98) {
                                    p.sendToolBreakStatus(Hand.MAIN_HAND);
                                } else if (slot == 99) {
                                    p.sendToolBreakStatus(Hand.OFF_HAND);
                                } else if (slot >= 100 && slot <= 103) {
                                    p.sendEquipmentBreakStatus(Stream.of(EquipmentSlot.values()).filter(s -> s.getEntitySlotId() == (slot - 100)).findFirst().get());
                                }
                        });
                        if (target instanceof ServerPlayerEntity) {
                            ((ServerPlayerEntity)target).currentScreenHandler.sendContentUpdates();
                        }
                        map.put(target, stackReference.get());
                    }
            }
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