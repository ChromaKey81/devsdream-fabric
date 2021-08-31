package net.devsdream.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

import java.util.Collection;
import java.util.List;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.text.TranslatableText;

public class DamageCommand {
        private static final SimpleCommandExceptionType DAMAGE_FAILED_EXCEPTION = new SimpleCommandExceptionType(
                        new TranslatableText("commands.devsdream.damage.failed"));

        public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
                dispatcher.register(CommandManager.literal("damage").requires((user) -> {
                        return user.hasPermissionLevel(2);
                }).then(CommandManager.argument("targets", EntityArgumentType.entities()).then(CommandManager
                                .argument("amount", FloatArgumentType.floatArg(0))
                                .then(CommandManager.argument("sourceString", StringArgumentType.string())
                                                                .then(CommandManager.argument("isFire",
                                                                                BoolArgumentType.bool())
                                                                                .executes((damage) -> {
                                                                                        return damageEntity(damage
                                                                                                        .getSource(),
                                                                                                        EntityArgumentType.getEntities(
                                                                                                                        damage,
                                                                                                                        "targets"),
                                                                                                        setDamageProperties(
                                                                                                                        StringArgumentType
                                                                                                                                        .getString(damage,
                                                                                                                                                        "sourceString"),
                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                        damage,
                                                                                                                                        "isFire"),
                                                                                                                        false, false, false, false, false, false, false, false, false, false, null, false),
                                                                                                        FloatArgumentType
                                                                                                                        .getFloat(damage,
                                                                                                                                        "amount"));
                                                                                })
                                                                                .then(CommandManager.argument("pierceArmor",
                                                                                                BoolArgumentType.bool())
                                                                                                .executes((damage) -> {
                                                                                                        return damageEntity(
                                                                                                                        damage.getSource(),
                                                                                                                        EntityArgumentType.getEntities(
                                                                                                                                        damage,
                                                                                                                                        "targets"),
                                                                                                                        setDamageProperties(
                                                                                                                                StringArgumentType
                                                                                                                                                .getString(damage,
                                                                                                                                                                "sourceString"),
                                                                                                                                BoolArgumentType.getBool(
                                                                                                                                                damage,
                                                                                                                                                "isFire"),
                                                                                                                                BoolArgumentType.getBool(damage, "pierceArmor"), false, false, false, false, false, false, false, false, false, null, false),
                                                                                                                        FloatArgumentType
                                                                                                                                        .getFloat(damage,
                                                                                                                                                        "amount"));
                                                                                                }).then(CommandManager.argument(
                                                                                                                                "isMagic",
                                                                                                                                BoolArgumentType.bool())
                                                                                                                                .executes((damage) -> {
                                                                                                                                        return damageEntity(
                                                                                                                                                        damage.getSource(),
                                                                                                                                                        EntityArgumentType.getEntities(
                                                                                                                                                                        damage,
                                                                                                                                                                        "targets"),
                                                                                                                                                        setDamageProperties(
                                                                                                                                                                StringArgumentType
                                                                                                                                                                                .getString(damage,
                                                                                                                                                                                                "sourceString"),
                                                                                                                                                                BoolArgumentType.getBool(
                                                                                                                                                                                damage,
                                                                                                                                                                                "isFire"),
                                                                                                                                                                BoolArgumentType.getBool(damage, "pierceArmor"), BoolArgumentType.getBool(damage, "isMagic"), false, false, false, false, false, false, false, false, null, false),
                                                                                                                                                        FloatArgumentType
                                                                                                                                                                        .getFloat(damage,
                                                                                                                                                                                        "amount"));
                                                                                                                                })
                                                                                                                                .then(CommandManager.argument(
                                                                                                                                                "bypassInvulnerability",
                                                                                                                                                BoolArgumentType.bool())
                                                                                                                                                .executes((damage) -> {
                                                                                                                                                        return damageEntity(
                                                                                                                                                                        damage.getSource(),
                                                                                                                                                                        EntityArgumentType.getEntities(
                                                                                                                                                                                        damage,
                                                                                                                                                                                        "targets"),
                                                                                                                                                                        setDamageProperties(
                                                                                                                                                                                StringArgumentType
                                                                                                                                                                                                .getString(damage,
                                                                                                                                                                                                                "sourceString"),
                                                                                                                                                                                BoolArgumentType.getBool(
                                                                                                                                                                                                damage,
                                                                                                                                                                                                "isFire"),
                                                                                                                                                                                BoolArgumentType.getBool(damage, "pierceArmor"), BoolArgumentType.getBool(damage, "isMagic"), BoolArgumentType.getBool(damage, "bypassInvulnerability"), false, false, false, false, false, false, false, null, false),
                                                                                                                                                                        FloatArgumentType
                                                                                                                                                                                        .getFloat(damage,
                                                                                                                                                                                                        "amount"));
                                                                                                                                                })
                                                                                                                                                .then(CommandManager.argument(
                                                                                                                                                                "isExplosion",
                                                                                                                                                                BoolArgumentType.bool())
                                                                                                                                                                .executes((damage) -> {
                                                                                                                                                                        return damageEntity(
                                                                                                                                                                                        damage.getSource(),
                                                                                                                                                                                        EntityArgumentType.getEntities(
                                                                                                                                                                                                        damage,
                                                                                                                                                                                                        "targets"),
                                                                                                                                                                                        setDamageProperties(
                                                                                                                                                                                                StringArgumentType
                                                                                                                                                                                                                .getString(damage,
                                                                                                                                                                                                                                "sourceString"),
                                                                                                                                                                                                BoolArgumentType.getBool(
                                                                                                                                                                                                                damage,
                                                                                                                                                                                                                "isFire"),
                                                                                                                                                                                                BoolArgumentType.getBool(damage, "pierceArmor"), BoolArgumentType.getBool(damage, "isMagic"), BoolArgumentType.getBool(damage, "bypassInvulnerability"), BoolArgumentType.getBool(damage, "isExplosion"), false, false, false, false, false, false, null, false),
                                                                                                                                                                                        FloatArgumentType
                                                                                                                                                                                                        .getFloat(damage,
                                                                                                                                                                                                                        "amount"));
                                                                                                                                                                })
                                                                                                                                                                .then(CommandManager.argument(
                                                                                                                                                                                "isProjectile",
                                                                                                                                                                                BoolArgumentType.bool())
                                                                                                                                                                                .executes((damage) -> {
                                                                                                                                                                                        return damageEntity(
                                                                                                                                                                                                        damage.getSource(),
                                                                                                                                                                                                        EntityArgumentType.getEntities(
                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                        "targets"),
                                                                                                                                                                                                        setDamageProperties(
                                                                                                                                                                                                                StringArgumentType
                                                                                                                                                                                                                                .getString(damage,
                                                                                                                                                                                                                                                "sourceString"),
                                                                                                                                                                                                                BoolArgumentType.getBool(
                                                                                                                                                                                                                                damage,
                                                                                                                                                                                                                                "isFire"),
                                                                                                                                                                                                                BoolArgumentType.getBool(damage, "pierceArmor"), BoolArgumentType.getBool(damage, "isMagic"), BoolArgumentType.getBool(damage, "bypassInvulnerability"), BoolArgumentType.getBool(damage, "isExplosion"), BoolArgumentType.getBool(damage, "isProjectile"), false, false, false, false, false, null, false),
                                                                                                                                                                                                        FloatArgumentType
                                                                                                                                                                                                                        .getFloat(damage,
                                                                                                                                                                                                                                        "amount"));
                                                                                                                                                                                })
                                                                                                                                                                                .then(CommandManager.argument(
                                                                                                                                                                                                "damageHelmet",
                                                                                                                                                                                                BoolArgumentType.bool())
                                                                                                                                                                                                .executes((damage) -> {
                                                                                                                                                                                                        return damageEntity(
                                                                                                                                                                                                                        damage.getSource(),
                                                                                                                                                                                                                        EntityArgumentType.getEntities(
                                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                                        "targets"),
                                                                                                                                                                                                                        setDamageProperties(
                                                                                                                                                                                                                                StringArgumentType
                                                                                                                                                                                                                                                .getString(damage,
                                                                                                                                                                                                                                                                "sourceString"),
                                                                                                                                                                                                                                BoolArgumentType.getBool(
                                                                                                                                                                                                                                                damage,
                                                                                                                                                                                                                                                "isFire"),
                                                                                                                                                                                                                                BoolArgumentType.getBool(damage, "pierceArmor"), BoolArgumentType.getBool(damage, "isMagic"), BoolArgumentType.getBool(damage, "bypassInvulnerability"), BoolArgumentType.getBool(damage, "isExplosion"), BoolArgumentType.getBool(damage, "isProjectile"), BoolArgumentType.getBool(damage, "damageHelmet"), false, false, false, false, null, false),
                                                                                                                                                                                                                        FloatArgumentType
                                                                                                                                                                                                                                        .getFloat(damage,
                                                                                                                                                                                                                                                        "amount"));
                                                                                                                                                                                                })
                                                                                                                                                                                                .then(CommandManager.argument(
                                                                                                                                                                                                                "aggro",
                                                                                                                                                                                                                BoolArgumentType.bool())
                                                                                                                                                                                                                .executes((damage) -> {
                                                                                                                                                                                                                        return damageEntity(
                                                                                                                                                                                                                                        damage.getSource(),
                                                                                                                                                                                                                                        EntityArgumentType.getEntities(
                                                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                                                        "targets"),
                                                                                                                                                                                                                                        setDamageProperties(
                                                                                                                                                                                                                                                StringArgumentType
                                                                                                                                                                                                                                                                .getString(damage,
                                                                                                                                                                                                                                                                                "sourceString"),
                                                                                                                                                                                                                                                BoolArgumentType.getBool(
                                                                                                                                                                                                                                                                damage,
                                                                                                                                                                                                                                                                "isFire"),
                                                                                                                                                                                                                                                BoolArgumentType.getBool(damage, "pierceArmor"), BoolArgumentType.getBool(damage, "isMagic"), BoolArgumentType.getBool(damage, "bypassInvulnerability"), BoolArgumentType.getBool(damage, "isExplosion"), BoolArgumentType.getBool(damage, "isProjectile"), BoolArgumentType.getBool(damage, "damageHelmet"), BoolArgumentType.getBool(damage, "aggro"), false, false, false, null, false),
                                                                                                                                                                                                                                        FloatArgumentType
                                                                                                                                                                                                                                                        .getFloat(damage,
                                                                                                                                                                                                                                                                        "amount"));
                                                                                                                                                                                                                }).then(CommandManager.argument(
                                                                                                                                                                                                                        "bypassMagic",
                                                                                                                                                                                                                        BoolArgumentType.bool())
                                                                                                                                                                                                                        .executes((damage) -> {
                                                                                                                                                                                                                                return damageEntity(
                                                                                                                                                                                                                                                damage.getSource(),
                                                                                                                                                                                                                                                EntityArgumentType.getEntities(
                                                                                                                                                                                                                                                                damage,
                                                                                                                                                                                                                                                                "targets"),
                                                                                                                                                                                                                                                setDamageProperties(
                                                                                                                                                                                                                                                        StringArgumentType
                                                                                                                                                                                                                                                                        .getString(damage,
                                                                                                                                                                                                                                                                                        "sourceString"),
                                                                                                                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                                                                        "isFire"),
                                                                                                                                                                                                                                                        BoolArgumentType.getBool(damage, "pierceArmor"), BoolArgumentType.getBool(damage, "isMagic"), BoolArgumentType.getBool(damage, "bypassInvulnerability"), BoolArgumentType.getBool(damage, "isExplosion"), BoolArgumentType.getBool(damage, "isProjectile"), BoolArgumentType.getBool(damage, "damageHelmet"), BoolArgumentType.getBool(damage, "aggro"), BoolArgumentType.getBool(damage, "bypassMagic"), false, false, null, false),
                                                                                                                                                                                                                                                FloatArgumentType
                                                                                                                                                                                                                                                                .getFloat(damage,
                                                                                                                                                                                                                                                                                "amount"));
                                                                                                                                                                                                                        }).then(CommandManager.argument(
                                                                                                                                                                                                                                "fromFalling",
                                                                                                                                                                                                                                BoolArgumentType.bool())
                                                                                                                                                                                                                                .executes((damage) -> {
                                                                                                                                                                                                                                        return damageEntity(
                                                                                                                                                                                                                                                        damage.getSource(),
                                                                                                                                                                                                                                                        EntityArgumentType.getEntities(
                                                                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                                                                        "targets"),
                                                                                                                                                                                                                                                        setDamageProperties(
                                                                                                                                                                                                                                                                StringArgumentType
                                                                                                                                                                                                                                                                                .getString(damage,
                                                                                                                                                                                                                                                                                                "sourceString"),
                                                                                                                                                                                                                                                                BoolArgumentType.getBool(
                                                                                                                                                                                                                                                                                damage,
                                                                                                                                                                                                                                                                                "isFire"),
                                                                                                                                                                                                                                                                BoolArgumentType.getBool(damage, "pierceArmor"), BoolArgumentType.getBool(damage, "isMagic"), BoolArgumentType.getBool(damage, "bypassInvulnerability"), BoolArgumentType.getBool(damage, "isExplosion"), BoolArgumentType.getBool(damage, "isProjectile"), BoolArgumentType.getBool(damage, "damageHelmet"), BoolArgumentType.getBool(damage, "aggro"), BoolArgumentType.getBool(damage, "bypassMagic"), BoolArgumentType.getBool(damage, "fromFalling"), false, null, false),
                                                                                                                                                                                                                                                        FloatArgumentType
                                                                                                                                                                                                                                                                        .getFloat(damage,
                                                                                                                                                                                                                                                                                        "amount"));
                                                                                                                                                                                                                                }).then(CommandManager.argument(
                                                                                                                                                                                                                                        "difficultyScaled",
                                                                                                                                                                                                                                        BoolArgumentType.bool())
                                                                                                                                                                                                                                        .executes((damage) -> {
                                                                                                                                                                                                                                                return damageEntity(
                                                                                                                                                                                                                                                                damage.getSource(),
                                                                                                                                                                                                                                                                EntityArgumentType.getEntities(
                                                                                                                                                                                                                                                                                damage,
                                                                                                                                                                                                                                                                                "targets"),
                                                                                                                                                                                                                                                                setDamageProperties(
                                                                                                                                                                                                                                                                        StringArgumentType
                                                                                                                                                                                                                                                                                        .getString(damage,
                                                                                                                                                                                                                                                                                                        "sourceString"),
                                                                                                                                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                                                                                        "isFire"),
                                                                                                                                                                                                                                                                        BoolArgumentType.getBool(damage, "pierceArmor"), BoolArgumentType.getBool(damage, "isMagic"), BoolArgumentType.getBool(damage, "bypassInvulnerability"), BoolArgumentType.getBool(damage, "isExplosion"), BoolArgumentType.getBool(damage, "isProjectile"), BoolArgumentType.getBool(damage, "damageHelmet"), BoolArgumentType.getBool(damage, "aggro"), BoolArgumentType.getBool(damage, "bypassMagic"), BoolArgumentType.getBool(damage, "fromFalling"), BoolArgumentType.getBool(damage, "difficultyScaled"), null, false),
                                                                                                                                                                                                                                                                FloatArgumentType
                                                                                                                                                                                                                                                                                .getFloat(damage,
                                                                                                                                                                                                                                                                                                "amount"));
                                                                                                                                                                                                                                        })).then(CommandManager.argument(
                                                                                                                                                                                                                                                "sourceEntity",
                                                                                                                                                                                                                                                EntityArgumentType.entity())
                                                                                                                                                                                                                                                .executes((damage) -> {
                                                                                                                                                                                                                                                        return damageEntity(
                                                                                                                                                                                                                                                                        damage.getSource(),
                                                                                                                                                                                                                                                                        EntityArgumentType.getEntities(
                                                                                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                                                                                        "targets"),
                                                                                                                                                                                                                                                                        setDamageProperties(
                                                                                                                                                                                                                                                                                StringArgumentType
                                                                                                                                                                                                                                                                                                .getString(damage,
                                                                                                                                                                                                                                                                                                                "sourceString"),
                                                                                                                                                                                                                                                                                BoolArgumentType.getBool(
                                                                                                                                                                                                                                                                                                damage,
                                                                                                                                                                                                                                                                                                "isFire"),
                                                                                                                                                                                                                                                                                BoolArgumentType.getBool(damage, "pierceArmor"), BoolArgumentType.getBool(damage, "isMagic"), BoolArgumentType.getBool(damage, "bypassInvulnerability"), BoolArgumentType.getBool(damage, "isExplosion"), BoolArgumentType.getBool(damage, "isProjectile"), BoolArgumentType.getBool(damage, "damageHelmet"), BoolArgumentType.getBool(damage, "aggro"), BoolArgumentType.getBool(damage, "bypassMagic"), BoolArgumentType.getBool(damage, "fromFalling"), false, EntityArgumentType.getEntity(damage, "sourceEntity"), false),
                                                                                                                                                                                                                                                                        FloatArgumentType
                                                                                                                                                                                                                                                                                        .getFloat(damage,
                                                                                                                                                                                                                                                                                                        "amount"));
                                                                                                                                                                                                                                                }).then(CommandManager.argument(
                                                                                                                                                                                                                                                        "thorns",
                                                                                                                                                                                                                                                        BoolArgumentType.bool())
                                                                                                                                                                                                                                                        .executes((damage) -> {
                                                                                                                                                                                                                                                                return damageEntity(
                                                                                                                                                                                                                                                                                damage.getSource(),
                                                                                                                                                                                                                                                                                EntityArgumentType.getEntities(
                                                                                                                                                                                                                                                                                                damage,
                                                                                                                                                                                                                                                                                                "targets"),
                                                                                                                                                                                                                                                                                setDamageProperties(
                                                                                                                                                                                                                                                                                        StringArgumentType
                                                                                                                                                                                                                                                                                                        .getString(damage,
                                                                                                                                                                                                                                                                                                                        "sourceString"),
                                                                                                                                                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                                                                                                        "isFire"),
                                                                                                                                                                                                                                                                                        BoolArgumentType.getBool(damage, "pierceArmor"), BoolArgumentType.getBool(damage, "isMagic"), BoolArgumentType.getBool(damage, "bypassInvulnerability"), BoolArgumentType.getBool(damage, "isExplosion"), BoolArgumentType.getBool(damage, "isProjectile"), BoolArgumentType.getBool(damage, "damageHelmet"), BoolArgumentType.getBool(damage, "aggro"), BoolArgumentType.getBool(damage, "bypassMagic"), BoolArgumentType.getBool(damage, "fromFalling"), false, EntityArgumentType.getEntity(damage, "sourceEntity"), BoolArgumentType.getBool(damage, "thorns")),
                                                                                                                                                                                                                                                                                FloatArgumentType
                                                                                                                                                                                                                                                                                                .getFloat(damage,
                                                                                                                                                                                                                                                                                                                "amount"));
                                                                                                                                                                                                                                                        })))))))))))))))));
        }

        private static DamageSource setDamageProperties(String sourceString, boolean isFire, boolean pierceArmor, boolean isMagic, boolean bypassInvulnerability, boolean isExplosion,
                        boolean isProjectile, boolean damageHelmet, boolean aggro, boolean pierceMagic, boolean fromFalling, 
                        boolean difficultyScaled, @Nullable Entity sourceEntity, boolean thorns) {

                DamageSource damage;

                if (sourceEntity == null) {
                        damage = new EntityDamageSource(sourceString, sourceEntity);
                } else {
                        damage = new DamageSource(sourceString);
                }
               

                if (isFire == true) {
                        damage.setFire();
                }

                if (pierceArmor == true) {
                        damage.setBypassesArmor();
                }

                if (damageHelmet == true) {
                        damage.setFallingBlock();
                }

                if (difficultyScaled == true) {
                        damage.setScaledWithDifficulty();
                }

                if (isMagic == true) {
                        damage.setUsesMagic();
                }

                if (bypassInvulnerability == true) {
                        damage.setOutOfWorld();
                }

                if (isExplosion == true) {
                        damage.setExplosive();
                }

                if (isProjectile == true) {
                        damage.setProjectile();
                }

                if (pierceMagic == true) {
                        damage.setUnblockable();
                }

                if (thorns == true) {
                        ((EntityDamageSource)damage).setThorns();
                }

                if (aggro == false) {
                        damage.setNeutral();
                }

                if (fromFalling == true) {
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