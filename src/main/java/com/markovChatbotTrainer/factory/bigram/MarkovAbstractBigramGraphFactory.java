package com.markovChatbotTrainer.factory.bigram;

import java.util.*;

/**
 * Created by Oliver on 4/27/2017.
 */
public abstract class MarkovAbstractBigramGraphFactory implements MarkovBigramGraphFactory {

    protected void populateBigramFrequenciesMap(String token1, String token2, Map<String, Map<String, Integer>> bigramFrequenciesMap) {
        System.out.println("Processing bigram for frequency map: " + token1 + " " + token2);
        if (bigramFrequenciesMap.containsKey(token1)) {
            if (bigramFrequenciesMap.get(token1).containsKey(token2)) {
                int frequency = bigramFrequenciesMap.get(token1).get(token2);
                frequency++;
                bigramFrequenciesMap.get(token1).put(token2, frequency);
            } else {
                bigramFrequenciesMap.get(token1).put(token2, 1);
            }
        } else {
            Map<String, Integer> map = new HashMap<>();
            map.put(token2, 1);
            bigramFrequenciesMap.put(token1, map);
        }
    }

    protected void populateBigramGraph(Map<String, Map<String, Integer>> bigramFrequenciesMap, Map<String, TreeMap<Integer, List<String>>> bigramGraph) {
        Iterator it = bigramFrequenciesMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String token1 = (String) pair.getKey();
            Map<String, Integer> map = (Map<String, Integer>) pair.getValue();
            Iterator<Map.Entry<String, Integer>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Integer> pair2 = iterator.next();
                String token2 = pair2.getKey();
                Integer frequency = pair2.getValue();
                System.out.println("Processing bigram for graph: " + token1 + " " + token2 + " with frequency: " + frequency);
                if (bigramGraph.containsKey(token1)) {
                    if (bigramGraph.get(token1).containsKey(frequency)) {
                        bigramGraph.get(token1).get(frequency).add(token2);
                    } else {
                        List<String> tokens = new ArrayList<>();
                        tokens.add(token2);
                        bigramGraph.get(token1).put(frequency, tokens);
                    }
                } else {
                    TreeMap<Integer, List<String>> treeMap = new TreeMap<>();
                    List<String> tokens = new ArrayList<>();
                    tokens.add(token2);
                    treeMap.put(frequency, tokens);
                    bigramGraph.put(token1, treeMap);
                }
            }
        }
    }


}
