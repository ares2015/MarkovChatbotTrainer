package com.markovChatbotTrainer.factory;

import com.markovChatbotTrainer.tokens.Tokenizer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by Oliver on 4/27/2017.
 */
public class MarkovTrigramGraphFactoryImpl implements MarkovTrigramGraphFactory, Runnable {

    private Map<String, Map<String, Map<String, Integer>>> trigramFrequenciesMap = new HashMap<>();

    private Map<String, Map<String, TreeMap<Integer, List<String>>>> trigramGraph = new HashMap<>();

    private Tokenizer tokenizer;

    public MarkovTrigramGraphFactoryImpl(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    @Override
    public void run() {
        create();
    }

    @Override
    public Map<String, Map<String, TreeMap<Integer, List<String>>>> create() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("c:\\Users\\Oliver\\Documents\\NlpTrainingData\\MarkovChatbot\\MarkovChatbotTrainingData.txt"));
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            String trainingDataRow = br.readLine();
            while (trainingDataRow != null) {
                if (!"".equals(trainingDataRow)) {
                    List<String> tokens = tokenizer.splitStringIntoList(trainingDataRow);
                    if (tokens.size() > 1) {
                        for (int i = 0; i < tokens.size() - 1; i++) {
                            String token1 = tokens.get(i);
                            String token2 = tokens.get(i + 1);
                            String token3 = "";
                            if (token1.contains(".") || token1.contains(",")) {
                                token1 = tokenizer.removeCommaAndDot(token1);
                            }
                            if (token2.contains(".") || token2.contains(",")) {
                                token2 = tokenizer.removeCommaAndDot(token2);
                            }
                            if ((tokens.size() - 1) - i >= 2) {
                                token3 = tokens.get(i + 2);
                                if (token3.contains(".") || token3.contains(",")) {
                                    token3 = tokenizer.removeCommaAndDot(token3);
                                }
                                populateTrigramFrequenciesMap(token1, token2, token3);
                            }
                        }
                    }
                }
                trainingDataRow = br.readLine();
            }
            populateTrigramGraph();
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return trigramGraph;
    }

    private void populateTrigramFrequenciesMap(String token1, String token2, String token3) {
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

    private void populateTrigramGraph() {
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
