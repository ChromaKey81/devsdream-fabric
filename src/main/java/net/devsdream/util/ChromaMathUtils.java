package net.devsdream.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ChromaMathUtils {

    public static <T> T weightsRandom(List<T> objects, List<Integer> weights, Random random) {
        // Calculate sum of all weights
        int weightTotal = weights.stream().collect(Collectors.summingInt(i -> i));
        // POSSIBILITIES: Generate the sequence of integers between 0 (inclusive) and the sum of all weights (exclusive)
        // Should be x values long, where x is the sum of all weights
        int[] possibilities = IntStream.range(0, weightTotal).toArray();
        List<List<Integer>> lists = new ArrayList<List<Integer>>();
        int i = 0;
        // For the first weight value n, make a new list of integers from POSSIBILITIES consisting of each value in the sequence between
        // and including the first term and term n.
        // Then for the second weight value, do the same thing, but instead of the first term of the sequence, begin with the term at
        // (previous weight + 1). Repeat for all weights and record each lists.
        for (int j = 0; j < weights.size(); j++) {
            List<Integer> ints = new ArrayList<Integer>();
            do {
                ints.add(possibilities[i]);
                i++;
            } while (i < weights.get(j));
            lists.add(ints);
        }
        // Get the random value. This will match one of the values in the sequence.
        int randomVal = random.nextInt(weightTotal);
        i = 0;
        // Check if each integer list n contains the random value. If it does, return the tree at n in the tree list.
        for (int j = 0; j < lists.size(); j++) {
            if (lists.get(j).contains(randomVal)) {
                return objects.get(j);
            }
        }
        return null;
    } 

}