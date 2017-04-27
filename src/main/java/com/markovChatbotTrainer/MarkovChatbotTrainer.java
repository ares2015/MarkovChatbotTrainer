package com.markovChatbotTrainer;

import com.markovChatbotTrainer.factory.MarkovBigramGraphFactory;
import com.markovChatbotTrainer.factory.MarkovBigramGraphFactoryImpl;
import com.markovChatbotTrainer.factory.MarkovTrigramGraphFactory;
import com.markovChatbotTrainer.factory.MarkovTrigramGraphFactoryImpl;
import com.markovChatbotTrainer.tokens.Tokenizer;
import com.markovChatbotTrainer.tokens.TokenizerImpl;
import com.markovChatbotTrainer.writer.BigramGraphModelWriterImpl;
import com.markovChatbotTrainer.writer.TrigramGraphModelWriterImpl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Oliver on 4/25/2017.
 */
public class MarkovChatbotTrainer {

    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();

        Tokenizer tokenizer = new TokenizerImpl();

        MarkovBigramGraphFactory markovBigramGraphFactory = new MarkovBigramGraphFactoryImpl(tokenizer);
        MarkovTrigramGraphFactory markovTrigramGraphFactory = new MarkovTrigramGraphFactoryImpl(tokenizer);

        Map<String, TreeMap<Integer, List<String>>> bigramGraph = markovBigramGraphFactory.create();
        Map<String, Map<String, TreeMap<Integer, List<String>>>> trigramGraph = markovTrigramGraphFactory.create();

        ExecutorService executor = Executors.newFixedThreadPool(2);

        Runnable bigramGraphModelWriter = new BigramGraphModelWriterImpl(bigramGraph);
        Runnable trigramGraphModelWriter = new TrigramGraphModelWriterImpl(trigramGraph);

        Future<?> bigramFuture = executor.submit(bigramGraphModelWriter);
        Future<?> trigramFuture = executor.submit(trigramGraphModelWriter);

        boolean areModelsWritten = false;
        while (!bigramFuture.isDone() && !trigramFuture.isDone()) {

        }
        executor.shutdown();
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Training data processed in " + (elapsedTime / 1000) / 60 + " minutes and "
                + +(elapsedTime / 1000) % 60 + " seconds");
    }
}
