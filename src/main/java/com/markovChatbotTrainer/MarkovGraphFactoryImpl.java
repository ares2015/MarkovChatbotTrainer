package com.markovChatbotTrainer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by Oliver on 4/25/2017.
 */
public class MarkovGraphFactoryImpl implements MarkovGraphFactory {

    private Tokenizer tokenizer;

    private Map<String, Map<String, Integer>> bigramFrequenciesMap = new HashMap<String, Map<String, Integer>>();

    private Map<String, TreeMap<Integer, List<String>>> graph = new HashMap<String, TreeMap<Integer, List<String>>>();

    public MarkovGraphFactoryImpl(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    public Map<String, TreeMap<Integer, List<String>>> create() {
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
                            if (token1.contains(".") || token1.contains(",")) {
                                token1 = tokenizer.removeCommaAndDot(token1);
                            }
                            if (token2.contains(".") || token2.contains(",")) {
                                token2 = tokenizer.removeCommaAndDot(token2);
                            }
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
                    }
                }
                trainingDataRow = br.readLine();
            }
            populateGraph();
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return graph;
    }

    private void populateGraph() {
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
                if (graph.containsKey(token1)) {
                    if (graph.get(token1).containsKey(frequency)) {
                        graph.get(token1).get(frequency).add(token2);
                    } else {
                        List<String> tokens = new ArrayList<>();
                        tokens.add(token2);
                        graph.get(token1).put(frequency, tokens);
                    }
                } else {
                    TreeMap<Integer, List<String>> treeMap = new TreeMap<>();
                    List<String> tokens = new ArrayList<>();
                    tokens.add(token2);
                    treeMap.put(frequency, tokens);
                    graph.put(token1, treeMap);
                }
            }
        }
    }

}