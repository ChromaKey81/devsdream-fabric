package net.devsdream.commands;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.text.TranslatableText;

public class FreezeCommand {
   private static SimpleCommandExceptionType FREEZE_FAILED_EXCEPTION = new SimpleCommandExceptionType(
         new TranslatableText("commands.devsdream.freeze.failed"));

   public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
      dispatcher.register(CommandManager.literal("freeze").requires((user) -> {
         return user.hasPermissionLevel(2);
      }).then(CommandManager.argument("targets", EntityArgumentType.entities())
            .then(CommandManager.argument("ticks", IntegerArgumentType.integer(0)).executes((context) -> {
               return freezeEntity(context.getSource(), EntityArgumentType.getEntities(context, "targets"),
                     IntegerArgumentType.getInteger(context, "ticks"));
            }))));
   }

   private static int freezeEntity(ServerCommandSource source, Collection<? extends Entity> targets, int freezeTime)
         throws CommandSyntaxException {
      List<Entity> list = Lists.newArrayListWithCapacity(targets.size());

      for (Entity entity : targets) {
         ((Entity) entity).setFrozenTicks(freezeTime);
         list.add(entity);
      }

      if (list.isEmpty()) {
         throw FREEZE_FAILED_EXCEPTION.create();
      } else {
         if (list.size() == 1) {
            source.sendFeedback(new TranslatableText("commands.devsdream.freeze.success.single",
                  list.iterator().next().getDisplayName(), freezeTime), true);
         } else {
            source.sendFeedback(
                  new TranslatableText("commands.devsdream.freeze.success.multiple", list.size(), freezeTime),
                  true);
         }

         return (int) freezeTime;
      }
   }
}