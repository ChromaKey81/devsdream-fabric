package net.devsdream.commands;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.Vec3d;

public class MotionCommand {
    private static SimpleCommandExceptionType MOTION_FAILED_EXCEPTION = new SimpleCommandExceptionType(
         new TranslatableText("commands.devsdream.motion.failed"));

   public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
      dispatcher.register(CommandManager.literal("motion").requires((user) -> {
         return user.hasPermissionLevel(2);
      }).then(CommandManager.argument("targets", EntityArgumentType.entities())
            .then(CommandManager.argument("value", DoubleArgumentType.doubleArg()).then(CommandManager.literal("x").executes((context) -> {
                return setMotion(context.getSource(), EntityArgumentType.getEntities(context, "targets"), "x", DoubleArgumentType.getDouble(context, "value"));
            }))).then(CommandManager.literal("y").executes((context) -> {
                return setMotion(context.getSource(), EntityArgumentType.getEntities(context, "targets"), "y", DoubleArgumentType.getDouble(context, "value"));
            })).then(CommandManager.literal("z").executes((context) -> {
                return setMotion(context.getSource(), EntityArgumentType.getEntities(context, "targets"), "z", DoubleArgumentType.getDouble(context, "value"));
            }))));
   }

   private static int setMotion(ServerCommandSource source, Collection<? extends Entity> targets, String which, double motion)
         throws CommandSyntaxException {
      List<Entity> list = Lists.newArrayListWithCapacity(targets.size());

      for (Entity entity : targets) {
         Vec3d current = entity.getVelocity();
         switch (which) {
             case "x": {
                entity.setVelocity(motion, current.y, current.z);
                break;
             }
             case "y": {
                 entity.setVelocity(current.x, motion, current.z);
                 break;
             }
             case "z": {
                 entity.setVelocity(current.x, current.y, motion);
                 break;
             }
         }
         entity.velocityModified = true;
         list.add(entity);
      }

      if (list.isEmpty()) {
         throw MOTION_FAILED_EXCEPTION.create();
      } else {
         if (list.size() == 1) {
            source.sendFeedback(new TranslatableText("commands.devsdream.motion.success.single",
                  list.iterator().next().getDisplayName(), motion), true);
         } else {
            source.sendFeedback(
                  new TranslatableText("commands.devsdream.motion.success.multiple", list.size(), motion),
                  true);
         }

         return (int) motion;
      }
   }
}
