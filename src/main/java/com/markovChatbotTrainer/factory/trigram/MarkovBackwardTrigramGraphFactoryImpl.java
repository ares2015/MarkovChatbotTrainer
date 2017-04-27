package com.markovChatbotTrainer.factory.trigram;

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
public class MarkovBackwardTrigramGraphFactoryImpl extends MarkovAbstractTrigramGraphFactory implements Runnable {

    private Map<String, Map<String, Map<String, Integer>>> trigramFrequenciesMap = new HashMap<>();

    private Map<String, Map<String, TreeMap<Integer, List<String>>>> trigramGraph = new HashMap<>();

    private Tokenizer tokenizer;

    public MarkovBackwardTrigramGraphFactoryImpl(Tokenizer tokenizer) {
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
                        for (int i = tokens.size() - 1; i > 0; i--) {
                            String token1 = tokens.get(i);
                            String token2 = tokens.get(i - 1);
                            String token3 = "";
                            if (token1.contains(".") || token1.contains(",")) {
                                token1 = tokenizer.removeCommaAndDot(token1);
                            }
                            if (token2.contains(".") || token2.contains(",")) {
                                token2 = tokenizer.removeCommaAndDot(token2);
                            }
                            if (i >= 2) {
                                token3 = tokens.get(i - 2);
                                if (token3.contains(".") || token3.contains(",")) {
                                    token3 = tokenizer.removeCommaAndDot(token3);
                                }
                                populateTrigramFrequenciesMap(token1, token2, token3, trigramFrequenciesMap);
                            }
                        }
                    }
                }
                trainingDataRow = br.readLine();
            }
            populateTrigramGraph(trigramFrequenciesMap, trigramGraph);
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

}
