package net.devsdream.objectpack;

import net.devsdream.Main;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.function.CommandFunction;
import net.minecraft.server.function.CommandFunctionManager;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class RightClickItem extends Item {

    private final Identifier function;

    public RightClickItem(Settings settings, Identifier function) {
        super(settings);
        this.function = function;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        Main.logger.info("useable");

        ItemStack item = user.getStackInHand(hand);

        user.incrementStat(Stats.USED.getOrCreateStat(this));

        if (!world.isClient()) {
            CommandFunctionManager manager = world.getServer().getCommandFunctionManager();
            CommandFunction func = manager.getFunction(this.function).orElseGet(() -> {
                Main.logger.info("function does not exist");
                return null;
            });
            if (func != null) {
                Main.logger.info("executing");
                manager.execute(func, world.getServer().getCommandSource().withEntity(user));
                return TypedActionResult.success(item);
            } else {
                Main.logger.info("fail");
                return TypedActionResult.fail(item);
            }
        } else {
            return TypedActionResult.pass(item);
        }


     }
    
}
