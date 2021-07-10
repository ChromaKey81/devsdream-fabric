package net.devsdream.objectpack;

import net.minecraft.block.Block;
import net.minecraft.world.explosion.Explosion;

public class CustomizedBlock extends Block {

    private final boolean dropsItemsOnExplosion;

    public CustomizedBlock(Block.Settings settings, BlockType type) {
        super(settings);
        this.dropsItemsOnExplosion = type.getDropsItemsOnExplosion();
    }

    public boolean shouldDropItemsOnExplosion(Explosion explosion) {
        return this.dropsItemsOnExplosion;
    }
    
}
