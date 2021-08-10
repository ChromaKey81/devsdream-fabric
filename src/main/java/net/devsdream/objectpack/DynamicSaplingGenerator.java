package net.devsdream.objectpack;

import java.util.List;
import java.util.Random;

import net.devsdream.util.ChromaMathUtils;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

public class DynamicSaplingGenerator extends SaplingGenerator {

    private final List<ConfiguredFeature<TreeFeatureConfig, ?>> trees;
    private final List<Integer> weights;
    private final List<ConfiguredFeature<TreeFeatureConfig, ?>> beeTrees;
    private final List<Integer> beeWeights;

    public DynamicSaplingGenerator(List<ConfiguredFeature<TreeFeatureConfig, ?>> configs, List<ConfiguredFeature<TreeFeatureConfig, ?>> beeConfigs, List<Integer> weights, List<Integer> beeWeights) {
        this.trees = configs;
        this.weights = weights;
        this.beeTrees = beeConfigs;
        this.beeWeights = beeWeights;
    }

    @Override
    protected ConfiguredFeature<TreeFeatureConfig, ?> getTreeFeature(Random random, boolean bees) {
        if (bees) {
            return ChromaMathUtils.weightsRandom(this.beeTrees, this.beeWeights, random);
        } else {
            return ChromaMathUtils.weightsRandom(this.trees, this.weights, random);
        }
    }
    
}
