package com.markovChatbotTrainer;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Oliver on 4/25/2017.
 */
public class MarkovChatbotTrainer {

    public static void main(String[] args) throws IOException {
        Tokenizer tokenizer = new TokenizerImpl();
        MarkovGraphFactory markovGraphFactory = new MarkovGraphFactoryImpl(tokenizer);
        GraphWriter graphWriter = new GraphWriterImpl();
        Map<String, TreeMap<Integer, List<String>>> graph = markovGraphFactory.create();
        graphWriter.write(graph);
    }
}
