package com.markovChatbotTrainer.factory.trigram;

import java.util.*;

/**
 * Created by Oliver on 4/27/2017.
 */
public abstract class MarkovAbstractTrigramGraphFactory implements MarkovTrigramGraphFactory {

    protected void populateTrigramFrequenciesMap(String token1, String token2, String token3,
                                                 Map<String, Map<String, Map<String, Integer>>> trigramFrequenciesMap) {
        System.out.println("Processing trigram for frequency map: " + token1 + " " + token2 + " " + token3);
        if (trigramFrequenciesMap.containsKey(token1) && trigramFrequenciesMap.get(token1).containsKey(token2)) {
            if (trigramFrequenciesMap.containsKey(token1) && trigramFrequenciesMap.get(token1).get(token2).containsKey(token3)) {
                int frequency = trigramFrequenciesMap.get(token1).get(token2).get(token3);
                frequency++;
                trigramFrequenciesMap.get(token1).get(token2).put(token3, frequency);
            } else {
                trigramFrequenciesMap.get(token1).get(token2).put(token3, 1);
            }
        } else if (trigramFrequenciesMap.containsKey(token1) && !trigramFrequenciesMap.get(token1).containsKey(token2)) {
            Map<String, Integer> map = new HashMap<>();
            map.put(token3, 1);
            trigramFrequenciesMap.get(token1).put(token2, map);
        } else {
            Map<String, Integer> map1 = new HashMap<>();
            map1.put(token3, 1);
            Map<String, Map<String, Integer>> map2 = new HashMap<>();
            map2.put(token2, map1);
            trigramFrequenciesMap.put(token1, map2);
        }
    }

    protected void populateTrigramGraph(Map<String, Map<String, Map<String, Integer>>> trigramFrequenciesMap,
                                        Map<String, Map<String, TreeMap<Integer, List<String>>>> trigramGraph) {
        Iterator it = trigramFrequenciesMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String token1 = (String) pair.getKey();
            Map<String, Map<String, Integer>> map1 = (Map<String, Map<String, Integer>>) pair.getValue();
            Iterator<Map.Entry<String, Map<String, Integer>>> iterator = map1.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Map<String, Integer>> pair2 = iterator.next();
                String token2 = pair2.getKey();
                Map<String, Integer> map2 = pair2.getValue();
                Iterator<Map.Entry<String, Integer>> iterator2 = map2.entrySet().iterator();
                while (iterator2.hasNext()) {
                    Map.Entry<String, Integer> pair3 = iterator2.next();
                    String token3 = pair3.getKey();
                    Integer frequency = pair3.getValue();
                    System.out.println("Processing trigram for graph: " + token1 + " " + token2 + " " + token3 + " with frequency: " + frequency);
                    if (trigramGraph.containsKey(token1) && trigramGraph.get(token1).containsKey(token2)) {
                        if (trigramGraph.get(token1).get(token2).containsKey(frequency)) {
                            trigramGraph.get(token1).get(token2).get(frequency).add(token3);
                        } else {
                            List<String> tokens = new ArrayList<>();
                            tokens.add(token3);
                            TreeMap<Integer, List<String>> treeMap = new TreeMap<>();
                            treeMap.put(1, tokens);
                            trigramGraph.get(token1).put(token2, treeMap);
                        }
                    } else {
                        List<String> tokens = new ArrayList<>();
                        tokens.add(token3);
                        TreeMap<Integer, List<String>> treeMap = new TreeMap<>();
                        treeMap.put(1, tokens);
                        Map<String, TreeMap<Integer, List<String>>> map = new HashMap<>();
                        map.put(token2, treeMap);
                        trigramGraph.put(token1, map);
                    }
                }
            }
        }
    }
}
