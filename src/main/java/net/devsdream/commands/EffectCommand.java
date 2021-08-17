package net.devsdream.commands;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import org.jetbrains.annotations.Nullable;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.StatusEffectArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.text.TranslatableText;

public class EffectCommand {
   private static final SimpleCommandExceptionType GIVE_FAILED_EXCEPTION = new SimpleCommandExceptionType(
         new TranslatableText("commands.effect.give.failed"));
   private static final SimpleCommandExceptionType CLEAR_EVERYTHING_FAILED_EXCEPTION = new SimpleCommandExceptionType(
         new TranslatableText("commands.effect.clear.everything.failed"));
   private static final SimpleCommandExceptionType CLEAR_SPECIFIC_FAILED_EXCEPTION = new SimpleCommandExceptionType(
         new TranslatableText("commands.effect.clear.specific.failed"));

   public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
      dispatcher
            .register(CommandManager.literal("dreameffect").requires((user) -> {
               return user.hasPermissionLevel(2);
            }).then(CommandManager.literal("clear").executes((clearAllUser) -> {
               return clearAllEffects(clearAllUser.getSource(),
                     ImmutableList.of(clearAllUser.getSource().getEntityOrThrow()));
            }).then(CommandManager.argument("targets", EntityArgumentType.entities()).executes((clearAllTargets) -> {
               return clearAllEffects(clearAllTargets.getSource(),
                     EntityArgumentType.getEntities(clearAllTargets, "targets"));
            }).then(CommandManager.argument("effect", StatusEffectArgumentType.statusEffect()).executes((clearEffect) -> {
               return clearEffect(clearEffect.getSource(), EntityArgumentType.getEntities(clearEffect, "targets"),
                     StatusEffectArgumentType.getStatusEffect(clearEffect, "effect"));
            })))).then(CommandManager.literal("give").then(CommandManager.argument("targets", EntityArgumentType.entities())
                  .then(CommandManager.argument("effect", StatusEffectArgumentType.statusEffect()).executes((context) -> {
                     return addEffect(context.getSource(), EntityArgumentType.getEntities(context, "targets"),
                           StatusEffectArgumentType.getStatusEffect(context, "effect"), (Integer) null, 0, true, true, false);
                  }).then(CommandManager.argument("seconds", IntegerArgumentType.integer(1, 1000000))
                        .executes((context) -> {
                           return addEffect(context.getSource(), EntityArgumentType.getEntities(context, "targets"),
                                 StatusEffectArgumentType.getStatusEffect(context, "effect"),
                                 IntegerArgumentType.getInteger(context, "seconds"), 0, true, true, false);
                        }).then(CommandManager.argument("amplifier", IntegerArgumentType.integer(0, 255))
                              .executes((context) -> {
                                 return addEffect(context.getSource(),
                                       EntityArgumentType.getEntities(context, "targets"),
                                       StatusEffectArgumentType.getStatusEffect(context, "effect"),
                                       IntegerArgumentType.getInteger(context, "seconds"),
                                       IntegerArgumentType.getInteger(context, "amplifier"), true, true, false);
                              }).then(CommandManager.argument("hideParticles", BoolArgumentType.bool())
                                    .executes((context) -> {
                                       return addEffect(context.getSource(),
                                             EntityArgumentType.getEntities(context, "targets"),
                                             StatusEffectArgumentType.getStatusEffect(context, "effect"),
                                             IntegerArgumentType.getInteger(context, "seconds"),
                                             IntegerArgumentType.getInteger(context, "amplifier"),
                                             !BoolArgumentType.getBool(context, "hideParticles"), false, false);
                                    }).then(CommandManager.argument("hideIcon", BoolArgumentType.bool())
                                          .executes((context) -> {
                                             return addEffect(context.getSource(),
                                                   EntityArgumentType.getEntities(context, "targets"),
                                                   StatusEffectArgumentType.getStatusEffect(context, "effect"),
                                                   IntegerArgumentType.getInteger(context, "seconds"),
                                                   IntegerArgumentType.getInteger(context, "amplifier"),
                                                   !BoolArgumentType.getBool(context, "hideParticles"),
                                                   !BoolArgumentType.getBool(context, "hideIcon"), false);
                                          }).then(CommandManager.argument("isAmbient", BoolArgumentType.bool())
                                                .executes((context) -> {
                                                   return addEffect(context.getSource(),
                                                         EntityArgumentType.getEntities(context, "targets"),
                                                         StatusEffectArgumentType.getStatusEffect(context, "effect"),
                                                         IntegerArgumentType.getInteger(context, "seconds"),
                                                         IntegerArgumentType.getInteger(context, "amplifier"),
                                                         !BoolArgumentType.getBool(context, "hideParticles"),
                                                         !BoolArgumentType.getBool(context, "hideIcon"),
                                                         BoolArgumentType.getBool(context, "isAmbient"));
                                                }))))))))));
   }

   private static int addEffect(ServerCommandSource source, Collection<? extends Entity> targets, StatusEffect effect,
         @Nullable Integer seconds, int amplifier, boolean showParticles, boolean showIcon, boolean ambient)
         throws CommandSyntaxException {
      List<Entity> list = Lists.newArrayListWithCapacity(targets.size());
      int j;
      if (seconds != null) {
         if (effect.isInstant()) {
            j = seconds;
         } else {
            j = seconds * 20;
         }
      } else if (effect.isInstant()) {
         j = 1;
      } else {
         j = 600;
      }

      for (Entity entity : targets) {
         if (entity instanceof LivingEntity) {
            StatusEffectInstance effectinstance = new StatusEffectInstance(effect, j, amplifier, ambient, showParticles, showIcon);
            if (((LivingEntity) entity).addStatusEffect(effectinstance)) {
               list.add(entity);
            }
         }
      }

      if (list.isEmpty()) {
         throw GIVE_FAILED_EXCEPTION.create();
      } else {
         if (list.size() == 1) {
            source.sendFeedback(new TranslatableText("commands.effect.give.success.single",
                  effect.getName(), targets.iterator().next().getDisplayName(), j / 20), true);
         } else {
            source.sendFeedback(new TranslatableText("commands.effect.give.success.multiple",
                  effect.getName(), targets.size(), j / 20), true);
         }

         return list.size();
      }
   }

   private static int clearAllEffects(ServerCommandSource source, Collection<? extends Entity> targets)
         throws CommandSyntaxException {
      List<Entity> list = Lists.newArrayListWithCapacity(targets.size());

      for (Entity entity : targets) {
         if (entity instanceof LivingEntity && ((LivingEntity) entity).clearStatusEffects()) {
            list.add(entity);
         }
      }

      if (list.isEmpty()) {
         throw CLEAR_EVERYTHING_FAILED_EXCEPTION.create();
      } else {
         if (list.size() == 1) {
            source.sendFeedback(new TranslatableText("commands.effect.clear.everything.success.single",
                  targets.iterator().next().getDisplayName()), true);
         } else {
            source.sendFeedback(
                  new TranslatableText("commands.effect.clear.everything.success.multiple", targets.size()),
                  true);
         }

         return list.size();
      }
   }

   private static int clearEffect(ServerCommandSource source, Collection<? extends Entity> targets, StatusEffect effect)
         throws CommandSyntaxException {
      List<Entity> list = Lists.newArrayListWithCapacity(targets.size());

      for (Entity entity : targets) {
         if (entity instanceof LivingEntity && ((LivingEntity) entity).removeStatusEffect(effect)) {
            list.add(entity);
         }
      }

      if (list.isEmpty()) {
         throw CLEAR_SPECIFIC_FAILED_EXCEPTION.create();
      } else {
         if (list.size() == 1) {
            source.sendFeedback(new TranslatableText("commands.effect.clear.specific.success.single",
                  effect.getName(), list.iterator().next().getDisplayName()), true);
         } else {
            source.sendFeedback(new TranslatableText("commands.effect.clear.specific.success.multiple",
                  effect.getName(), list.size()), true);
         }

         return list.size();
      }
   }
}