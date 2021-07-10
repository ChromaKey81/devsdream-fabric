package net.devsdream.util;

import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

import com.google.gson.JsonObject;

import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.block.AbstractCauldronBlock;
import net.minecraft.block.AirBlock;
import net.minecraft.block.AttachedStemBlock;
import net.minecraft.block.AzaleaBlock;
import net.minecraft.block.BarrierBlock;
import net.minecraft.block.BigDripleafBlock;
import net.minecraft.block.BigDripleafStemBlock;
import net.minecraft.block.BlastFurnaceBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CactusBlock;
import net.minecraft.block.CakeBlock;
import net.minecraft.block.CandleCakeBlock;
import net.minecraft.block.CartographyTableBlock;
import net.minecraft.block.CarvedPumpkinBlock;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.ChorusFlowerBlock;
import net.minecraft.block.ChorusPlantBlock;
import net.minecraft.block.ConnectingBlock;
import net.minecraft.block.CoralBlock;
import net.minecraft.block.CoralFanBlock;
import net.minecraft.block.CoralParentBlock;
import net.minecraft.block.CoralWallFanBlock;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.block.CropBlock;
import net.minecraft.block.DeadBushBlock;
import net.minecraft.block.DeadCoralBlock;
import net.minecraft.block.DeadCoralFanBlock;
import net.minecraft.block.DeadCoralWallFanBlock;
import net.minecraft.block.DirtPathBlock;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.DyedCarpetBlock;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.block.EndGatewayBlock;
import net.minecraft.block.EndPortalBlock;
import net.minecraft.block.EndRodBlock;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.block.FernBlock;
import net.minecraft.block.FletchingTableBlock;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.FungusBlock;
import net.minecraft.block.FurnaceBlock;
import net.minecraft.block.GourdBlock;
import net.minecraft.block.GrindstoneBlock;
import net.minecraft.block.HangingRootsBlock;
import net.minecraft.block.HorizontalConnectingBlock;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.JigsawBlock;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.block.KelpBlock;
import net.minecraft.block.KelpPlantBlock;
import net.minecraft.block.LadderBlock;
import net.minecraft.block.LecternBlock;
import net.minecraft.block.LeverBlock;
import net.minecraft.block.LilyPadBlock;
import net.minecraft.block.LoomBlock;
import net.minecraft.block.MelonBlock;
import net.minecraft.block.MushroomPlantBlock;
import net.minecraft.block.NetherWartBlock;
import net.minecraft.block.NyliumBlock;
import net.minecraft.block.PaneBlock;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.PlayerSkullBlock;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.block.PumpkinBlock;
import net.minecraft.block.RailBlock;
import net.minecraft.block.RedstoneTorchBlock;
import net.minecraft.block.RepeaterBlock;
import net.minecraft.block.RodBlock;
import net.minecraft.block.RootsBlock;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.Item;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.property.Property;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.HugeFungusFeatureConfig;

public interface PublicConstructors {

    public class PublicAirBlock extends AirBlock {
        public PublicAirBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicStatusEffect extends StatusEffect {
        public PublicStatusEffect(StatusEffectType type, int color) {
            super(type, color);
        }
    }

    public class PublicAbstractBannerBlock extends AbstractBannerBlock {
        public PublicAbstractBannerBlock(DyeColor color, Settings settings) {
            super(color, settings);
        }
    }

    public class PublicAbstractButtonBlock extends AbstractButtonBlock {
        private final SoundEvent on;
        private final SoundEvent off;

        public PublicAbstractButtonBlock(boolean wooden, Settings settings, SoundEvent on, SoundEvent off) {
            super(wooden, settings);
            this.on = on;
            this.off = off;
        }

        @Override
        protected SoundEvent getClickSound(boolean powered) {
            return powered ? on : off;
        }
    }

    public class PublicAbstractCauldronBlock<T extends Comparable<T>> extends AbstractCauldronBlock {

        private final Property<T> fullProperty;
        private final Property.Value<T> fullValue;

        public PublicAbstractCauldronBlock(Settings settings, Map<Item, CauldronBehavior> behaviorMap,
                Property<T> fullProperty, Property.Value<T> fullValue) {
            super(settings, behaviorMap);
            this.fullProperty = fullProperty;
            this.fullValue = fullValue;
        }

        @Override
        public boolean isFull(BlockState state) {
            return (T) state.get(fullProperty) == fullValue.getValue();
        }

    }

    public class PublicAttachedStemBlock extends AttachedStemBlock {
        public PublicAttachedStemBlock(GourdBlock gourdBlock, Supplier<Item> pickBlockItem, Settings settings) {
            super(gourdBlock, pickBlockItem, settings);
        }
    }

    public class PublicAzaleaBlock extends AzaleaBlock {
        public PublicAzaleaBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicBarrierBlock extends BarrierBlock {
        public PublicBarrierBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicBigDripleafBlock extends BigDripleafBlock {
        public PublicBigDripleafBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicBigDripleafStemBlock extends BigDripleafStemBlock {
        public PublicBigDripleafStemBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicBlastFurnaceBlock extends BlastFurnaceBlock {
        public PublicBlastFurnaceBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicCakeBlock extends CakeBlock {
        public PublicCakeBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicCactusBlock extends CactusBlock {
        public PublicCactusBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicCandleCakeBlock extends CandleCakeBlock {
        public PublicCandleCakeBlock(Block candle, Settings settings) {
            super(candle, settings);
        }
    }

    public class PublicCartographyTableBlock extends CartographyTableBlock {
        public PublicCartographyTableBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicCarvedPumpkinBlock extends CarvedPumpkinBlock {
        public PublicCarvedPumpkinBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicChestBlock extends ChestBlock {
        public PublicChestBlock(Settings settings, Supplier<BlockEntityType<? extends ChestBlockEntity>> supplier) {
            super(settings, supplier);
        }
    }

    public class PublicChorusFlowerBlock extends ChorusFlowerBlock {
        public PublicChorusFlowerBlock(ChorusPlantBlock plantBlock, Settings settings) {
            super(plantBlock, settings);
        }
    }

    public class PublicChorusPlantBlock extends ChorusPlantBlock {
        public PublicChorusPlantBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicConnectingBlock extends ConnectingBlock {
        public PublicConnectingBlock(float radius, Settings settings) {
            super(radius, settings);
        }
    }

    public class PublicCoralBlock extends CoralBlock {
        public PublicCoralBlock(Block deadCoralBlock, Settings settings) {
            super(deadCoralBlock, settings);
        }
    }

    public class PublicCoralFanBlock extends CoralFanBlock {
        public PublicCoralFanBlock(Block deadCoralBlock, Settings settings) {
            super(deadCoralBlock, settings);
        }
    }

    public class PublicCoralParentBlock extends CoralParentBlock {
        public PublicCoralParentBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicCoralWallFanBlock extends CoralWallFanBlock {
        public PublicCoralWallFanBlock(Block deadCoralBlock, Settings settings) {
            super(deadCoralBlock, settings);
        }
    }

    public class PublicCraftingTableBlock extends CraftingTableBlock {
        public PublicCraftingTableBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicCropBlock extends CropBlock {
        public PublicCropBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicDeadBushBlock extends DeadBushBlock {
        public PublicDeadBushBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicDeadCoralBlock extends DeadCoralBlock {
        public PublicDeadCoralBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicDeadCoralFanBlock extends DeadCoralFanBlock {
        public PublicDeadCoralFanBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicDeadCoralWallFanBlock extends DeadCoralWallFanBlock {
        public PublicDeadCoralWallFanBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicDirtPathBlock extends DirtPathBlock {
        public PublicDirtPathBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicDispenserBlock extends DispenserBlock {
        public PublicDispenserBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicDoorBlock extends DoorBlock {
        public PublicDoorBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicDyedCarpetBlock extends DyedCarpetBlock {
        public PublicDyedCarpetBlock(DyeColor dyeColor, Settings settings) {
            super(dyeColor, settings);
        }
    }

    public class PublicEnchantingTableBlock extends EnchantingTableBlock {
        public PublicEnchantingTableBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicEndGatewayBlock extends EndGatewayBlock {
        public PublicEndGatewayBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicEndPortalBlock extends EndPortalBlock {
        public PublicEndPortalBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicEndRodBlock extends EndRodBlock {
        public PublicEndRodBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicEnderChestBlock extends EnderChestBlock {
        public PublicEnderChestBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicFacingBlock extends FacingBlock {
        public PublicFacingBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicFarmlandBlock extends FarmlandBlock {
        public PublicFarmlandBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicFernBlock extends FernBlock {
        public PublicFernBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicFletchingTableBlock extends FletchingTableBlock {
        public PublicFletchingTableBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicFluidBlock extends FluidBlock {
        public PublicFluidBlock(FlowableFluid fluid, Settings settings) {
            super(fluid, settings);
        }
    }

    public class PublicFungusBlock extends FungusBlock {
        public PublicFungusBlock(Settings settings,
                Supplier<ConfiguredFeature<HugeFungusFeatureConfig, ?>> feature) {
            super(settings, feature);
        }
    }

    public class PublicFurnaceBlock extends FurnaceBlock {
        public PublicFurnaceBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicGrindstoneBlock extends GrindstoneBlock {
        public PublicGrindstoneBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicHangingRootsBlock extends HangingRootsBlock {
        public PublicHangingRootsBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicHorizontalConnectingBlock extends HorizontalConnectingBlock {
        public PublicHorizontalConnectingBlock(float radius1, float radius2, float boundingHeight1,
                float boundingHeight2, float collisionHeight, Settings settings) {
            super(radius1, radius2, boundingHeight1, boundingHeight2, collisionHeight, settings);
        }
    }

    public class PublicHorizontalFacingBlock extends HorizontalFacingBlock {
        public PublicHorizontalFacingBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicJigsawBlock extends JigsawBlock {
        public PublicJigsawBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicJukeboxBlock extends JukeboxBlock {
        public PublicJukeboxBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicKelpBlock extends KelpBlock {
        public PublicKelpBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicKelpPlantBlock extends KelpPlantBlock {
        public PublicKelpPlantBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicLadderBlock extends LadderBlock {
        public PublicLadderBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicLecternBlock extends LecternBlock {
        public PublicLecternBlock(Settings settings) {
            super(settings);
        }
    }
    
    public class PublicLeverBlock extends LeverBlock {
        public PublicLeverBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicLilyPadBlock extends LilyPadBlock {
        public PublicLilyPadBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicLoomBlock extends LoomBlock {
        public PublicLoomBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicMelonBlock extends MelonBlock {
        public PublicMelonBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicNetherWartBlock extends NetherWartBlock {
        public PublicNetherWartBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicNyliumBlock extends NyliumBlock {
        public PublicNyliumBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicPaneBlock extends PaneBlock {
        public PublicPaneBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicPlantBlock extends PlantBlock {
        public PublicPlantBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicPlayerSkullBlock extends PlayerSkullBlock {
        public PublicPlayerSkullBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicPoweredRailBlock extends PoweredRailBlock {
        public PublicPoweredRailBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicPressurePlateBlock extends PressurePlateBlock {
        public PublicPressurePlateBlock(ActivationRule type, Settings settings) {
            super(type, settings);
        }
    }

    public class PublicPumpkinBlock extends PumpkinBlock {
        public PublicPumpkinBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicRailBlock extends RailBlock {
        public PublicRailBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicRedstoneTorchBlock extends RedstoneTorchBlock {
        public PublicRedstoneTorchBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicRepeaterBlock extends RepeaterBlock {
        public PublicRepeaterBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicRodBlock extends RodBlock {
        public PublicRodBlock(Settings settings) {
            super(settings);
        }
    }

    public class PublicRootsBlock extends RootsBlock {
        public PublicRootsBlock(Settings settings) {
            super(settings);
        }
    }

}
