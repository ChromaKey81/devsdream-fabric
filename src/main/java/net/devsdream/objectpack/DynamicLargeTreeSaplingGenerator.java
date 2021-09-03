// package net.devsdream.objectpack;

// import java.util.List;
// import java.util.Random;

// import net.devsdream.util.ChromaMathUtils;
// import net.minecraft.block.sapling.LargeTreeSaplingGenerator;
// import net.minecraft.world.gen.feature.ConfiguredFeature;
// import net.minecraft.world.gen.feature.TreeFeatureConfig;

// public class DynamicLargeTreeSaplingGenerator extends LargeTreeSaplingGenerator {

//     private final List<ConfiguredFeature<TreeFeatureConfig, ?>> trees;
//     private final List<Integer> weights;
//     private final List<ConfiguredFeature<TreeFeatureConfig, ?>> beeTrees;
//     private final List<Integer> beeWeights;
//     private final List<ConfiguredFeature<TreeFeatureConfig, ?>> largeTrees;
//     private final List<Integer> largeTreeWeights;

//     public DynamicLargeTreeSaplingGenerator(List<ConfiguredFeature<TreeFeatureConfig, ?>> configs, List<ConfiguredFeature<TreeFeatureConfig, ?>> beeConfigs, List<ConfiguredFeature<TreeFeatureConfig, ?>> largeConfigs, List<Integer> weights, List<Integer> beeWeights, List<Integer> largeWeights) {
//         this.trees = configs;
//         this.weights = weights;
//         this.beeTrees = beeConfigs;
//         this.beeWeights = beeWeights;
//         this.largeTrees = largeConfigs;
//         this.largeTreeWeights = largeWeights;
//     }

//     @Override
//     protected ConfiguredFeature<TreeFeatureConfig, ?> getLargeTreeFeature(Random random) {
//         return ChromaMathUtils.weightsRandom(this.largeTrees, this.largeTreeWeights, random);
//     }

//     @Override
//     protected ConfiguredFeature<TreeFeatureConfig, ?> getTreeFeature(Random random, boolean bees) {
//         if (bees) {
//             return ChromaMathUtils.weightsRandom(this.beeTrees, this.beeWeights, random);
//         } else {
//             return ChromaMathUtils.weightsRandom(this.trees, this.weights, random);
//         }
//     }
    
// }
