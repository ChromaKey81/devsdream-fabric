package net.devsdream.commands;

import net.minecraft.server.command.*;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import java.util.Iterator;
import java.util.List;
import java.util.function.IntFunction;

import net.minecraft.command.argument.DimensionArgumentType;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.ItemSlotArgumentType;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.command.argument.RotationArgumentType;
import net.minecraft.command.argument.StatusEffectArgumentType;
import net.minecraft.command.argument.SwizzleArgumentType;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.command.argument.NbtPathArgumentType.NbtPath;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtFloat;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtLong;
import net.minecraft.nbt.NbtShort;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;

public class ExecuteCommand extends net.minecraft.server.command.ExecuteCommand {

   public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
      LiteralCommandNode<ServerCommandSource> literalCommandNode = dispatcher
            .register((LiteralArgumentBuilder) CommandManager.literal("dreamexecute").requires((source) -> {
               return source.hasPermissionLevel(2);
            }));
      dispatcher.register(
            (LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) CommandManager
                  .literal("execute").requires((source) -> {
                     return source.hasPermissionLevel(2);
                  })).then(CommandManager.literal("run").redirect(dispatcher.getRoot())))
                        .then(addConditionArguments(literalCommandNode, CommandManager.literal("if"), true)))
                              .then(addConditionArguments(literalCommandNode, CommandManager.literal("unless"), false)))
                                    .then(CommandManager.literal("as")
                                          .then(CommandManager.argument("targets", EntityArgumentType.entities())
                                                .fork(literalCommandNode, (context) -> {
                                                   List<ServerCommandSource> list = Lists.newArrayList();
                                                   Iterator var2 = EntityArgumentType
                                                         .getOptionalEntities(context, "targets").iterator();

                                                   while (var2.hasNext()) {
                                                      Entity entity = (Entity) var2.next();
                                                      list.add(((ServerCommandSource) context.getSource())
                                                            .withEntity(entity));
                                                   }

                                                   return list;
                                                })))).then(CommandManager.literal("at")
                                                      .then(CommandManager
                                                            .argument("targets", EntityArgumentType.entities())
                                                            .fork(literalCommandNode, (context) -> {
                                                               List<ServerCommandSource> list = Lists.newArrayList();
                                                               Iterator var2 = EntityArgumentType
                                                                     .getOptionalEntities(context, "targets")
                                                                     .iterator();

                                                               while (var2.hasNext()) {
                                                                  Entity entity = (Entity) var2.next();
                                                                  list.add(((ServerCommandSource) context.getSource())
                                                                        .withWorld((ServerWorld) entity.world)
                                                                        .withPosition(entity.getPos())
                                                                        .withRotation(entity.getRotationClient()));
                                                               }

                                                               return list;
                                                            })))).then(((LiteralArgumentBuilder) CommandManager
                                                                  .literal("store")
                                                                  .then(addStoreArguments(
                                                                        literalCommandNode, CommandManager
                                                                              .literal("result"),
                                                                        true))).then(addStoreArguments(
                                                                              literalCommandNode,
                                                                              CommandManager.literal("success"),
                                                                              false)))).then(
                                                                                    ((LiteralArgumentBuilder) CommandManager
                                                                                          .literal("positioned")
                                                                                          .then(CommandManager
                                                                                                .argument("pos",
                                                                                                      Vec3ArgumentType
                                                                                                            .vec3())
                                                                                                .redirect(
                                                                                                      literalCommandNode,
                                                                                                      (context) -> {
                                                                                                         return ((ServerCommandSource) context
                                                                                                               .getSource())
                                                                                                                     .withPosition(
                                                                                                                           Vec3ArgumentType
                                                                                                                                 .getVec3(
                                                                                                                                       context,
                                                                                                                                       "pos"))
                                                                                                                     .withEntityAnchor(
                                                                                                                           EntityAnchorArgumentType.EntityAnchor.FEET);
                                                                                                      }))).then(
                                                                                                            CommandManager
                                                                                                                  .literal(
                                                                                                                        "as")
                                                                                                                  .then(CommandManager
                                                                                                                        .argument(
                                                                                                                              "targets",
                                                                                                                              EntityArgumentType
                                                                                                                                    .entities())
                                                                                                                        .fork(literalCommandNode,
                                                                                                                              (context) -> {
                                                                                                                                 List<ServerCommandSource> list = Lists
                                                                                                                                       .newArrayList();
                                                                                                                                 Iterator var2 = EntityArgumentType
                                                                                                                                       .getOptionalEntities(
                                                                                                                                             context,
                                                                                                                                             "targets")
                                                                                                                                       .iterator();

                                                                                                                                 while (var2
                                                                                                                                       .hasNext()) {
                                                                                                                                    Entity entity = (Entity) var2
                                                                                                                                          .next();
                                                                                                                                    list.add(
                                                                                                                                          ((ServerCommandSource) context
                                                                                                                                                .getSource())
                                                                                                                                                      .withPosition(
                                                                                                                                                            entity.getPos()));
                                                                                                                                 }

                                                                                                                                 return list;
                                                                                                                              }))))).then(
                                                                                                                                    ((LiteralArgumentBuilder) CommandManager
                                                                                                                                          .literal(
                                                                                                                                                "rotated")
                                                                                                                                          .then(CommandManager
                                                                                                                                                .argument(
                                                                                                                                                      "rot",
                                                                                                                                                      RotationArgumentType
                                                                                                                                                            .rotation())
                                                                                                                                                .redirect(
                                                                                                                                                      literalCommandNode,
                                                                                                                                                      (context) -> {
                                                                                                                                                         return ((ServerCommandSource) context
                                                                                                                                                               .getSource())
                                                                                                                                                                     .withRotation(
                                                                                                                                                                           RotationArgumentType
                                                                                                                                                                                 .getRotation(
                                                                                                                                                                                       context,
                                                                                                                                                                                       "rot")
                                                                                                                                                                                 .toAbsoluteRotation(
                                                                                                                                                                                       (ServerCommandSource) context
                                                                                                                                                                                             .getSource()));
                                                                                                                                                      }))).then(
                                                                                                                                                            CommandManager
                                                                                                                                                                  .literal(
                                                                                                                                                                        "as")
                                                                                                                                                                  .then(CommandManager
                                                                                                                                                                        .argument(
                                                                                                                                                                              "targets",
                                                                                                                                                                              EntityArgumentType
                                                                                                                                                                                    .entities())
                                                                                                                                                                        .fork(literalCommandNode,
                                                                                                                                                                              (context) -> {
                                                                                                                                                                                 List<ServerCommandSource> list = Lists
                                                                                                                                                                                       .newArrayList();
                                                                                                                                                                                 Iterator var2 = EntityArgumentType
                                                                                                                                                                                       .getOptionalEntities(
                                                                                                                                                                                             context,
                                                                                                                                                                                             "targets")
                                                                                                                                                                                       .iterator();

                                                                                                                                                                                 while (var2
                                                                                                                                                                                       .hasNext()) {
                                                                                                                                                                                    Entity entity = (Entity) var2
                                                                                                                                                                                          .next();
                                                                                                                                                                                    list.add(
                                                                                                                                                                                          ((ServerCommandSource) context
                                                                                                                                                                                                .getSource())
                                                                                                                                                                                                      .withRotation(
                                                                                                                                                                                                            entity.getRotationClient()));
                                                                                                                                                                                 }

                                                                                                                                                                                 return list;
                                                                                                                                                                              }))))).then(
                                                                                                                                                                                    ((LiteralArgumentBuilder) CommandManager
                                                                                                                                                                                          .literal(
                                                                                                                                                                                                "facing")
                                                                                                                                                                                          .then(CommandManager
                                                                                                                                                                                                .literal(
                                                                                                                                                                                                      "entity")
                                                                                                                                                                                                .then(CommandManager
                                                                                                                                                                                                      .argument(
                                                                                                                                                                                                            "targets",
                                                                                                                                                                                                            EntityArgumentType
                                                                                                                                                                                                                  .entities())
                                                                                                                                                                                                      .then(CommandManager
                                                                                                                                                                                                            .argument(
                                                                                                                                                                                                                  "anchor",
                                                                                                                                                                                                                  EntityAnchorArgumentType
                                                                                                                                                                                                                        .entityAnchor())
                                                                                                                                                                                                            .fork(literalCommandNode,
                                                                                                                                                                                                                  (context) -> {
                                                                                                                                                                                                                     List<ServerCommandSource> list = Lists
                                                                                                                                                                                                                           .newArrayList();
                                                                                                                                                                                                                     EntityAnchorArgumentType.EntityAnchor entityAnchor = EntityAnchorArgumentType
                                                                                                                                                                                                                           .getEntityAnchor(
                                                                                                                                                                                                                                 context,
                                                                                                                                                                                                                                 "anchor");
                                                                                                                                                                                                                     Iterator var3 = EntityArgumentType
                                                                                                                                                                                                                           .getOptionalEntities(
                                                                                                                                                                                                                                 context,
                                                                                                                                                                                                                                 "targets")
                                                                                                                                                                                                                           .iterator();

                                                                                                                                                                                                                     while (var3
                                                                                                                                                                                                                           .hasNext()) {
                                                                                                                                                                                                                        Entity entity = (Entity) var3
                                                                                                                                                                                                                              .next();
                                                                                                                                                                                                                        list.add(
                                                                                                                                                                                                                              ((ServerCommandSource) context
                                                                                                                                                                                                                                    .getSource())
                                                                                                                                                                                                                                          .withLookingAt(
                                                                                                                                                                                                                                                entity,
                                                                                                                                                                                                                                                entityAnchor));
                                                                                                                                                                                                                     }

                                                                                                                                                                                                                     return list;
                                                                                                                                                                                                                  }))))).then(
                                                                                                                                                                                                                        CommandManager
                                                                                                                                                                                                                              .argument(
                                                                                                                                                                                                                                    "pos",
                                                                                                                                                                                                                                    Vec3ArgumentType
                                                                                                                                                                                                                                          .vec3())
                                                                                                                                                                                                                              .redirect(
                                                                                                                                                                                                                                    literalCommandNode,
                                                                                                                                                                                                                                    (context) -> {
                                                                                                                                                                                                                                       return ((ServerCommandSource) context
                                                                                                                                                                                                                                             .getSource())
                                                                                                                                                                                                                                                   .withLookingAt(
                                                                                                                                                                                                                                                         Vec3ArgumentType
                                                                                                                                                                                                                                                               .getVec3(
                                                                                                                                                                                                                                                                     context,
                                                                                                                                                                                                                                                                     "pos"));
                                                                                                                                                                                                                                    })))).then(
                                                                                                                                                                                                                                          CommandManager
                                                                                                                                                                                                                                                .literal(
                                                                                                                                                                                                                                                      "align")
                                                                                                                                                                                                                                                .then(CommandManager
                                                                                                                                                                                                                                                      .argument(
                                                                                                                                                                                                                                                            "axes",
                                                                                                                                                                                                                                                            SwizzleArgumentType
                                                                                                                                                                                                                                                                  .swizzle())
                                                                                                                                                                                                                                                      .redirect(
                                                                                                                                                                                                                                                            literalCommandNode,
                                                                                                                                                                                                                                                            (context) -> {
                                                                                                                                                                                                                                                               return ((ServerCommandSource) context
                                                                                                                                                                                                                                                                     .getSource())
                                                                                                                                                                                                                                                                           .withPosition(
                                                                                                                                                                                                                                                                                 ((ServerCommandSource) context
                                                                                                                                                                                                                                                                                       .getSource())
                                                                                                                                                                                                                                                                                             .getPosition()
                                                                                                                                                                                                                                                                                             .floorAlongAxes(
                                                                                                                                                                                                                                                                                                   SwizzleArgumentType
                                                                                                                                                                                                                                                                                                         .getSwizzle(
                                                                                                                                                                                                                                                                                                               context,
                                                                                                                                                                                                                                                                                                               "axes")));
                                                                                                                                                                                                                                                            })))).then(
                                                                                                                                                                                                                                                                  CommandManager
                                                                                                                                                                                                                                                                        .literal(
                                                                                                                                                                                                                                                                              "anchored")
                                                                                                                                                                                                                                                                        .then(CommandManager
                                                                                                                                                                                                                                                                              .argument(
                                                                                                                                                                                                                                                                                    "anchor",
                                                                                                                                                                                                                                                                                    EntityAnchorArgumentType
                                                                                                                                                                                                                                                                                          .entityAnchor())
                                                                                                                                                                                                                                                                              .redirect(
                                                                                                                                                                                                                                                                                    literalCommandNode,
                                                                                                                                                                                                                                                                                    (context) -> {
                                                                                                                                                                                                                                                                                       return ((ServerCommandSource) context
                                                                                                                                                                                                                                                                                             .getSource())
                                                                                                                                                                                                                                                                                                   .withEntityAnchor(
                                                                                                                                                                                                                                                                                                         EntityAnchorArgumentType
                                                                                                                                                                                                                                                                                                               .getEntityAnchor(
                                                                                                                                                                                                                                                                                                                     context,
                                                                                                                                                                                                                                                                                                                     "anchor"));
                                                                                                                                                                                                                                                                                    })))).then(
                                                                                                                                                                                                                                                                                          CommandManager
                                                                                                                                                                                                                                                                                                .literal(
                                                                                                                                                                                                                                                                                                      "in")
                                                                                                                                                                                                                                                                                                .then(CommandManager
                                                                                                                                                                                                                                                                                                      .argument(
                                                                                                                                                                                                                                                                                                            "dimension",
                                                                                                                                                                                                                                                                                                            DimensionArgumentType
                                                                                                                                                                                                                                                                                                                  .dimension())
                                                                                                                                                                                                                                                                                                      .redirect(
                                                                                                                                                                                                                                                                                                            literalCommandNode,
                                                                                                                                                                                                                                                                                                            (context) -> {
                                                                                                                                                                                                                                                                                                               return ((ServerCommandSource) context
                                                                                                                                                                                                                                                                                                                     .getSource())
                                                                                                                                                                                                                                                                                                                           .withWorld(
                                                                                                                                                                                                                                                                                                                                 DimensionArgumentType
                                                                                                                                                                                                                                                                                                                                       .getDimensionArgument(
                                                                                                                                                                                                                                                                                                                                             context,
                                                                                                                                                                                                                                                                                                                                             "dimension"));
                                                                                                                                                                                                                                                                                                            }))));
   }

   protected static ArgumentBuilder<ServerCommandSource, ?> addStoreArguments(
         LiteralCommandNode<ServerCommandSource> node, LiteralArgumentBuilder<ServerCommandSource> builder,
         boolean requestResult) {
      ArgumentBuilder<ServerCommandSource, ?> newBuilder = net.minecraft.server.command.ExecuteCommand
            .addStoreArguments(node, builder, requestResult);
      newBuilder
            .then(CommandManager
                  .literal(
                        "player")
                  .then(CommandManager
                        .argument("player",
                              EntityArgumentType.player())
                        .then(CommandManager
                              .literal(
                                    "item")
                              .then(CommandManager
                                    .argument("slot",
                                          ItemSlotArgumentType.itemSlot())
                                    .then(CommandManager.argument("path", NbtPathArgumentType.nbtPath())
                                          .then(CommandManager.literal("int")
                                                .then(CommandManager.argument("scale", DoubleArgumentType.doubleArg())
                                                      .redirect(node, (context) -> {
                                                         return storeIntoItem(context.getSource(),
                                                               EntityArgumentType.getPlayer(context, "player"),
                                                               ItemSlotArgumentType.getItemSlot(context, "slot"),
                                                               (value) -> {
                                                                  return NbtInt
                                                                        .of((int) ((double) value * DoubleArgumentType
                                                                              .getDouble(context, "scale")));
                                                               }, NbtPathArgumentType.getNbtPath(context, "path"),
                                                               requestResult);
                                                      })))
                                          .then(CommandManager.literal("float")
                                                .then(CommandManager.argument("scale", DoubleArgumentType.doubleArg())
                                                      .redirect(node, (context) -> {
                                                         return storeIntoItem(context.getSource(),
                                                               EntityArgumentType.getPlayer(context, "player"),
                                                               ItemSlotArgumentType.getItemSlot(context, "slot"),
                                                               (value) -> {
                                                                  return NbtFloat
                                                                        .of((float) ((double) value * DoubleArgumentType
                                                                              .getDouble(context, "scale")));
                                                               }, NbtPathArgumentType.getNbtPath(context, "path"),
                                                               requestResult);
                                                      })))
                                          .then(CommandManager.literal("short")
                                                .then(CommandManager.argument("scale", DoubleArgumentType.doubleArg())
                                                      .redirect(node, (context) -> {
                                                         return storeIntoItem(context.getSource(),
                                                               EntityArgumentType.getPlayer(context, "player"),
                                                               ItemSlotArgumentType.getItemSlot(context, "slot"),
                                                               (value) -> {
                                                                  return NbtShort.of((short) ((int) ((double) value
                                                                        * DoubleArgumentType.getDouble(context,
                                                                              "scale"))));
                                                               }, NbtPathArgumentType.getNbtPath(context, "path"),
                                                               requestResult);
                                                      })))
                                          .then(CommandManager.literal("long")
                                                .then(CommandManager.argument("scale", DoubleArgumentType.doubleArg())
                                                      .redirect(node, (context) -> {
                                                         return storeIntoItem(context.getSource(),
                                                               EntityArgumentType.getPlayer(context, "player"),
                                                               ItemSlotArgumentType.getItemSlot(context, "slot"),
                                                               (value) -> {
                                                                  return NbtLong
                                                                        .of((long) ((double) value * DoubleArgumentType
                                                                              .getDouble(context, "scale")));
                                                               }, NbtPathArgumentType.getNbtPath(context, "path"),
                                                               requestResult);
                                                      })))
                                          .then(CommandManager.literal("double")
                                                .then(CommandManager.argument("scale", DoubleArgumentType.doubleArg())
                                                      .redirect(node, (context) -> {
                                                         return storeIntoItem(context.getSource(),
                                                               EntityArgumentType.getPlayer(context, "player"),
                                                               ItemSlotArgumentType.getItemSlot(context, "slot"),
                                                               (value) -> {
                                                                  return NbtDouble
                                                                        .of((double) value * DoubleArgumentType
                                                                              .getDouble(context, "scale"));
                                                               }, NbtPathArgumentType.getNbtPath(context, "path"),
                                                               requestResult);
                                                      })))
                                          .then(CommandManager.literal("byte")
                                                .then(CommandManager.argument("scale", DoubleArgumentType.doubleArg())
                                                      .redirect(node, (context) -> {
                                                         return storeIntoItem(context.getSource(),
                                                               EntityArgumentType.getPlayer(context, "player"),
                                                               ItemSlotArgumentType.getItemSlot(context, "slot"),
                                                               (value) -> {
                                                                  return NbtByte.of((byte) ((int) ((double) value
                                                                        * DoubleArgumentType.getDouble(context,
                                                                              "scale"))));
                                                               }, NbtPathArgumentType.getNbtPath(context, "path"),
                                                               requestResult);
                                                      }))))))
                        .then(CommandManager.literal("motion")
                              .then(CommandManager.literal("x").redirect(node, (context) -> {
                                 return storeIntoXMotion(context.getSource(),
                                       EntityArgumentType.getPlayer(context, "player"), requestResult);
                              })).then(CommandManager.literal("y").redirect(node, (context) -> {
                                 return storeIntoYMotion(context.getSource(),
                                       EntityArgumentType.getPlayer(context, "player"), requestResult);
                              })).then(CommandManager.literal("z").redirect(node, (context) -> {
                                 return storeIntoZMotion(context.getSource(),
                                       EntityArgumentType.getPlayer(context, "player"), requestResult);
                              })))
                        .then(CommandManager.literal("health").redirect(node, (context) -> {
                           return storeIntoHealth(context.getSource(), EntityArgumentType.getPlayer(context, "player"),
                                 requestResult);
                        })).then(CommandManager.literal("food").redirect(node, (context) -> {
                           return storeIntoFood(context.getSource(), EntityArgumentType.getPlayer(context, "player"),
                                 requestResult);
                        })).then(CommandManager.literal("saturation").redirect(node, (context) -> {
                           return storeIntoSaturation(context.getSource(),
                                 EntityArgumentType.getPlayer(context, "player"), requestResult);
                        })).then(CommandManager.literal("exhaustion").redirect(node, (context) -> {
                           return storeIntoExhaustion(context.getSource(),
                                 EntityArgumentType.getPlayer(context, "player"), requestResult);
                        })).then(CommandManager.literal("air").redirect(node, (context) -> {
                           return storeIntoAir(context.getSource(), EntityArgumentType.getPlayer(context, "player"),
                                 requestResult);
                        })).then(CommandManager.literal("fire").redirect(node, (context) -> {
                           return storeIntoBurnTime(context.getSource(),
                                 EntityArgumentType.getPlayer(context, "player"), requestResult);
                        }))
                        .then(CommandManager.literal("effect")
                              .then(CommandManager.argument("effect", StatusEffectArgumentType.statusEffect())
                                    .then(CommandManager.literal("amplifier").redirect(node, (context) -> {
                                       return storeIntoEffectAmplifier(context.getSource(),
                                             StatusEffectArgumentType.getStatusEffect(context, "effect"),
                                             EntityArgumentType.getPlayer(context, "player"), requestResult);
                                    })).then(CommandManager.literal("duration").redirect(node, (context) -> {
                                       return storeIntoEffectDuration(context.getSource(),
                                             StatusEffectArgumentType.getStatusEffect(context, "effect"),
                                             EntityArgumentType.getPlayer(context, "player"), requestResult);
                                    }))))));

      return builder;
   }

   private static ServerCommandSource storeIntoItem(ServerCommandSource source, ServerPlayerEntity target, int slot,
         IntFunction<NbtElement> tagConverter, NbtPath path, boolean storingResult) throws CommandSyntaxException {
      return source.withConsumer((context, successful, result) -> {
         try {
            StackReference stackReference = target.getStackReference(slot);
            if (stackReference != StackReference.EMPTY) {
               throw new SimpleCommandExceptionType(
                     new TranslatableText("commands.devsdream.execute.store.player.item.failed.no_item")).create();
            } else {
               ItemStack stack = stackReference.get();
               NbtCompound tag = stack.getNbt();
               int i = storingResult ? result : (successful ? 1 : 0);
               path.put(tag, () -> {
                  return tagConverter.apply(i);
               });
               stack.setNbt(stack.getNbt().copyFrom(tag));
               target.currentScreenHandler.sendContentUpdates();
               ;
            }
         } catch (CommandSyntaxException e) {
         }
      });
   }

   private static ServerCommandSource storeIntoHealth(ServerCommandSource source, ServerPlayerEntity target,
         boolean storingResult) {
      return source.withConsumer((context, successful, result) -> {
         int i = storingResult ? result : (successful ? 1 : 0);
         target.setHealth((float) i);
      });
   }

   private static ServerCommandSource storeIntoFood(ServerCommandSource source, ServerPlayerEntity target,
         boolean storingResult) {
      return source.withConsumer((context, successful, result) -> {
         int i = storingResult ? result : (successful ? 1 : 0);
         target.getHungerManager().setFoodLevel(i);
      });
   }

   private static ServerCommandSource storeIntoSaturation(ServerCommandSource source, ServerPlayerEntity target, boolean storingResult) {
      return source.withConsumer((context, successful, result) -> {
          int i = storingResult ? result : (successful ? 1 : 0);
          target.getHungerManager().setSaturationLevel((float) i);
      });
  }

  private static ServerCommandSource storeIntoExhaustion(ServerCommandSource source, ServerPlayerEntity target, boolean storingResult) {
   return source.withConsumer((context, successful, result) -> {
       int i = storingResult ? result : (successful ? 1 : 0);
       target.getHungerManager().addExhaustion((float) i);
   });
}

private static ServerCommandSource storeIntoAir(ServerCommandSource source, ServerPlayerEntity target, boolean storingResult) {
   return source.withConsumer((context, successful, result) -> {
       int i = storingResult ? result : (successful ? 1 : 0);
       target.setAir(i);
   });
}

private static ServerCommandSource storeIntoBurnTime(ServerCommandSource source, ServerPlayerEntity target, boolean storingResult) {
   return source.withConsumer((context, successful, result) -> {
       int i = storingResult ? result : (successful ? 1 : 0);
       target.setFireTicks(i);
   });
}

private static ServerCommandSource storeIntoEffectAmplifier(ServerCommandSource source, StatusEffect effect, ServerPlayerEntity target, boolean storingResult) {
   return source.withConsumer((context, successful, result) -> {
       try {
           int i = storingResult ? result : (successful ? 1 : 0);
           StatusEffectInstance targetEffect = target.getStatusEffect(effect);
           if (targetEffect == null) {
               throw new SimpleCommandExceptionType(
                   new TranslatableText("commands.devsdream.execute.store.player.item.failed.no_item"))
                           .create();
           } else {
               StatusEffectInstance newEffect = new StatusEffectInstance(targetEffect.getEffectType(), targetEffect.getDuration(), i, targetEffect.isAmbient(), targetEffect.shouldShowParticles(), targetEffect.shouldShowIcon());
               newEffect.upgrade(targetEffect);
               target.removeStatusEffect(effect);
               target.addStatusEffect(newEffect);
           }
       } catch (CommandSyntaxException e) {
       }
   });
}

private static ServerCommandSource storeIntoEffectDuration(ServerCommandSource source, StatusEffect effect, ServerPlayerEntity target, boolean storingResult) {
   return source.withConsumer((context, successful, result) -> {
       try {
           int i = storingResult ? result : (successful ? 1 : 0);
           StatusEffectInstance targetEffect = target.getStatusEffect(effect);
           if (targetEffect == null) {
               throw new SimpleCommandExceptionType(
                   new TranslatableText("commands.devsdream.execute.store.player.item.failed.no_item"))
                           .create();
           } else {
               StatusEffectInstance newEffect = new StatusEffectInstance(targetEffect.getEffectType(), i, targetEffect.getDuration(), targetEffect.isAmbient(), targetEffect.shouldShowParticles(), targetEffect.shouldShowIcon());
               newEffect.upgrade(targetEffect);
               target.removeStatusEffect(effect);
               target.addStatusEffect(newEffect);
           }
       } catch (CommandSyntaxException e) {
       }
   });
}

private static ServerCommandSource storeIntoXMotion(ServerCommandSource source, ServerPlayerEntity target, boolean storingResult) {
   return source.withConsumer((context, successful, result) -> {
       int i = storingResult ? result : (successful ? 1 : 0);
       double motionY = target.getVelocity().y;
       double motionZ = target.getVelocity().z;
       target.setVelocity((double) i, motionY, motionZ);
       target.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(target));
   });
}

private static ServerCommandSource storeIntoYMotion(ServerCommandSource source, ServerPlayerEntity target, boolean storingResult) {
   return source.withConsumer((context, successful, result) -> {
       int i = storingResult ? result : (successful ? 1 : 0);
       double motionX = target.getVelocity().x;
       double motionZ = target.getVelocity().z;
       target.setVelocity(motionX, (double) i, motionZ);
       target.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(target));
   });
}

private static ServerCommandSource storeIntoZMotion(ServerCommandSource source, ServerPlayerEntity target, boolean storingResult) {
   return source.withConsumer((context, successful, result) -> {
       int i = storingResult ? result : (successful ? 1 : 0);
       double motionX = target.getVelocity().x;
       double motionY = target.getVelocity().y;
       target.setVelocity(motionX, motionY, (double) i);
       target.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(target));
   });
}
}
