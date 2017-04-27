package com.markovChatbotTrainer.factory.bigram;

import com.markovChatbotTrainer.tokens.Tokenizer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Oliver on 4/27/2017.
 */
public class MarkovForwardBigramGraphFactoryImpl extends MarkovAbstractBigramGraphFactory implements Runnable {

    private Map<String, Map<String, Integer>> bigramFrequenciesMap = new HashMap<>();

    private Map<String, TreeMap<Integer, List<String>>> bigramGraph = new HashMap<String, TreeMap<Integer, List<String>>>();

    private Tokenizer tokenizer;

    public MarkovForwardBigramGraphFactoryImpl(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    @Override
    public void run() {

    }

    @Override
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
                            populateBigramFrequenciesMap(token1, token2, bigramFrequenciesMap);
                        }
                    }
                }
                trainingDataRow = br.readLine();
            }
            populateBigramGraph(bigramFrequenciesMap, bigramGraph);
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return bigramGraph;
    }


}