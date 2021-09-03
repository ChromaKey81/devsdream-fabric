package net.devsdream.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;

import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;

public class DamageCommand {

        private static Map<String, Boolean> damageOptions = mapDamageOptions();
                private static Map<String, Boolean> pierceOptions = mapPierceOptions();
                private static List<Object> sourceEntityThings = makeSourceEntityThings();

        static Map<String, Boolean> mapDamageOptions() {
                Map<String, Boolean> map = new HashMap<String, Boolean>();
                map.put("fire", false);
                map.put("magic", false);
                map.put("explosion", false);
                map.put("projectile", false);
                map.put("aggroless", false);
                map.put("headhurt", false);
                map.put("scaled", false);
                map.put("fall", false);
                return map;
        }
        static Map<String, Boolean> mapPierceOptions() {
                Map<String, Boolean> map = new HashMap<String, Boolean>();
                map.put("armor", false);
                map.put("invulnerability", false);
                map.put("magic", false);
                return map;
        }

        static List<Object> makeSourceEntityThings() {
                List<Object> list = new ArrayList<Object>();
                list.add(null);
                list.add(false);
                return list;
        }

        private static final SimpleCommandExceptionType DAMAGE_FAILED_EXCEPTION = new SimpleCommandExceptionType(
                        new TranslatableText("commands.devsdream.damage.failed"));

        public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
                LiteralArgumentBuilder<ServerCommandSource> builder = CommandManager.literal("damage").requires((source) -> {
                        return source.hasPermissionLevel(2);
                });
                builder.then(CommandManager.literal("run").then(CommandManager.argument("targets", EntityArgumentType.entities()).then(CommandManager
                                .argument("amount", FloatArgumentType.floatArg(0))
                                .then(CommandManager.argument("sourceString", StringArgumentType.string()).executes(context -> {
                                        DamageSource damage = setDamageProperties(StringArgumentType.getString(context, "sourceString"), damageOptions.get("fire"), pierceOptions.get("armor"), damageOptions.get("magic"), pierceOptions.get("invulnerability"), damageOptions.get("explosion"), damageOptions.get("projectile"), damageOptions.get("headhurt"), !(damageOptions.get("aggroless")), pierceOptions.get("magic"), damageOptions.get("fall"), damageOptions.get("scaled"));
                                        damageOptions = mapDamageOptions();
                                        pierceOptions = mapPierceOptions();
                                        if (sourceEntityThings.get(0) == null) {
                                                sourceEntityThings = makeSourceEntityThings();
                                                return damageEntity(context.getSource(), EntityArgumentType.getEntities(context, "targets"), damage, FloatArgumentType.getFloat(context, "amount"));
                                        } else {
                                                EntityDamageSource entityDamage = upgradeToEntitySource((Entity) (sourceEntityThings.get(0)), (boolean) (sourceEntityThings.get(1)), damage);
                                                sourceEntityThings = makeSourceEntityThings();
                                                return damageEntity(context.getSource(), EntityArgumentType.getEntities(context, "targets"), entityDamage, FloatArgumentType.getFloat(context, "amount"));
                                        }
                                })))));
                LiteralCommandNode<ServerCommandSource> node = dispatcher
                .register(builder);
                                
                damageOptions.forEach((string, bool) -> {
                        builder.then(CommandManager.literal(string).redirect(node, (context) -> {
                                damageOptions.put(string, true);
                                return context.getSource();
                        }));
                });
                LiteralArgumentBuilder<ServerCommandSource> pierce = CommandManager.literal("pierce");
                pierceOptions.forEach((string, bool) -> {
                        pierce.then(CommandManager.literal(string).redirect(node, (context) -> {
                                pierceOptions.put(string, true);
                                return context.getSource();
                        }));
                });

                builder.then(pierce);

                builder.then(CommandManager.literal("from").then(CommandManager.argument("source", EntityArgumentType.entity()).then(CommandManager.argument("thorns", BoolArgumentType.bool()).redirect(node, (context) -> {
                        sourceEntityThings.set(0, EntityArgumentType.getEntity(context, "source"));
                        sourceEntityThings.set(1, BoolArgumentType.getBool(context, "thorns"));
                        return context.getSource();
                }))));
                
                dispatcher.register(builder);
        }

        private static DamageSource setDamageProperties(String sourceString, boolean isFire, boolean pierceArmor, boolean isMagic, boolean bypassInvulnerability, boolean isExplosion,
                        boolean isProjectile, boolean damageHelmet, boolean aggro, boolean pierceMagic, boolean fromFalling, 
                        boolean difficultyScaled) {

                DamageSource damage = new DamageSource(sourceString);
               

                if (isFire) {
                        damage.setFire();
                }

                if (pierceArmor) {
                        damage.setBypassesArmor();
                }

                if (damageHelmet) {
                        damage.setFallingBlock();
                }

                if (difficultyScaled) {
                        damage.setScaledWithDifficulty();
                }

                if (isMagic) {
                        damage.setUsesMagic();
                }

                if (bypassInvulnerability) {
                        damage.setOutOfWorld();
                }

                if (isExplosion) {
                        damage.setExplosive();
                }

                if (isProjectile) {
                        damage.setProjectile();
                }

                if (pierceMagic) {
                        damage.setUnblockable();
                }

                if (!aggro) {
                        damage.setNeutral();
                }

                if (fromFalling) {
                        damage.setFromFalling();
                }

                return damage;
        }

        private static EntityDamageSource upgradeToEntitySource(Entity sourceEntity, boolean thorns, DamageSource base) {

                EntityDamageSource damage = new EntityDamageSource(base.getName(), sourceEntity);

                if (thorns) {
                        damage.setThorns();
                }
               

                if (base.isFire()) {
                        damage.setFire();
                }

                if (base.bypassesArmor()) {
                        damage.setBypassesArmor();
                }

                if (base.isFallingBlock()) {
                        damage.setFallingBlock();
                }

                if (base.isMagic()) {
                        damage.setUsesMagic();
                }

                if (base.isOutOfWorld()) {
                        damage.setOutOfWorld();
                }

                if (base.isExplosive()) {
                        damage.setExplosive();
                }

                if (base.isProjectile()) {
                        damage.setProjectile();
                }

                if (base.isUnblockable()) {
                        damage.setUnblockable();
                }

                if (!base.isNeutral()) {
                        damage.setNeutral();
                }

                if (base.isFromFalling()) {
                        damage.setFromFalling();
                }

                return damage;
        }

        private static int damageEntity(ServerCommandSource source, Collection<? extends Entity> targets,
                        DamageSource damageSource, float amount) throws CommandSyntaxException {
                List<Entity> list = Lists.newArrayListWithCapacity(targets.size());

                for (Entity entity : targets) {
                        if (entity instanceof LivingEntity) {
                                ((LivingEntity) entity).damage(damageSource, amount);
                                list.add(entity);
                        }
                }

                if (list.isEmpty()) {
                        throw DAMAGE_FAILED_EXCEPTION.create();
                } else {
                        if (list.size() == 1) {
                                source.sendFeedback(
                                                new TranslatableText("commands.devsdream.damage.success.single",
                                                                list.iterator().next().getDisplayName(), amount),
                                                true);
                        } else {
                                source.sendFeedback(new TranslatableText(
                                                "commands.devsdream.damage.success.multiple", list.size(), amount),
                                                true);
                        }

                        return (int) amount;
                }
        }
}