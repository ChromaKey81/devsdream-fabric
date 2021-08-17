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

public class IgniteCommand {
   private static SimpleCommandExceptionType IGNITE_FAILED_EXCEPTION = new SimpleCommandExceptionType(
         new TranslatableText("commands.devsdream.ignite.failed"));

   public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
      dispatcher.register(CommandManager.literal("ignite").requires((user) -> {
         return user.hasPermissionLevel(2);
      }).then(CommandManager.argument("targets", EntityArgumentType.entities())
            .then(CommandManager.argument("seconds", IntegerArgumentType.integer(0)).executes((ignite) -> {
               return burnEntity(ignite.getSource(), EntityArgumentType.getEntities(ignite, "targets"),
                     IntegerArgumentType.getInteger(ignite, "seconds"));
            }))));
   }

   private static int burnEntity(ServerCommandSource source, Collection<? extends Entity> targets, int burnTime)
         throws CommandSyntaxException {
      List<Entity> list = Lists.newArrayListWithCapacity(targets.size());

      for (Entity entity : targets) {
         ((Entity) entity).setOnFireFor(burnTime);
         list.add(entity);
      }

      if (list.isEmpty()) {
         throw IGNITE_FAILED_EXCEPTION.create();
      } else {
         if (list.size() == 1) {
            source.sendFeedback(new TranslatableText("commands.devsdream.ignite.success.single",
                  list.iterator().next().getDisplayName(), burnTime), true);
         } else {
            source.sendFeedback(
                  new TranslatableText("commands.devsdream.ignite.success.multiple", list.size(), burnTime),
                  true);
         }

         return (int) burnTime;
      }
   }
}