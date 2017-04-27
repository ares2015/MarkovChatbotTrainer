package com.markovChatbotTrainer;

import com.markovChatbotTrainer.factory.bigram.MarkovBackwardBigramGraphFactoryImpl;
import com.markovChatbotTrainer.factory.bigram.MarkovBigramGraphFactory;
import com.markovChatbotTrainer.factory.bigram.MarkovForwardBigramGraphFactoryImpl;
import com.markovChatbotTrainer.factory.trigram.MarkovBackwardTrigramGraphFactoryImpl;
import com.markovChatbotTrainer.factory.trigram.MarkovForwardTrigramGraphFactoryImpl;
import com.markovChatbotTrainer.factory.trigram.MarkovTrigramGraphFactory;
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
        String forwardBigramPath = "c:\\Users\\Oliver\\Documents\\NlpTrainingData\\MarkovChatbot\\MarkovForwardBigramChatbotModel.txt";
        String backwardBigramPath = "c:\\Users\\Oliver\\Documents\\NlpTrainingData\\MarkovChatbot\\MarkovBackwardBigramChatbotModel.txt";
        String forwardTrigramPath = "c:\\Users\\Oliver\\Documents\\NlpTrainingData\\MarkovChatbot\\MarkovForwardTrigramChatbotModel.txt";
        String backwardTrigramPath = "c:\\Users\\Oliver\\Documents\\NlpTrainingData\\MarkovChatbot\\MarkovBackwardTrigramChatbotModel.txt";

        long startTime = System.currentTimeMillis();

        Tokenizer tokenizer = new TokenizerImpl();

        MarkovBigramGraphFactory markovForwardBigramGraphFactory = new MarkovForwardBigramGraphFactoryImpl(tokenizer);
        MarkovBigramGraphFactory markovBackwardBigramGraphFactory = new MarkovBackwardBigramGraphFactoryImpl(tokenizer);

        MarkovTrigramGraphFactory markovForwardTrigramGraphFactory = new MarkovForwardTrigramGraphFactoryImpl(tokenizer);
        MarkovTrigramGraphFactory markovBackwardTrigramGraphFactory = new MarkovBackwardTrigramGraphFactoryImpl(tokenizer);

        Map<String, TreeMap<Integer, List<String>>> forwardBigramGraph = markovForwardBigramGraphFactory.create();
        Map<String, TreeMap<Integer, List<String>>> backwardBigramGraph = markovBackwardBigramGraphFactory.create();

        Map<String, Map<String, TreeMap<Integer, List<String>>>> forwardTrigramGraph = markovForwardTrigramGraphFactory.create();
        Map<String, Map<String, TreeMap<Integer, List<String>>>> backwardTrigramGraph = markovBackwardTrigramGraphFactory.create();

        ExecutorService executor = Executors.newFixedThreadPool(4);

        Runnable forwardBigramGraphModelWriter = new BigramGraphModelWriterImpl(forwardBigramPath, forwardBigramGraph);
        Runnable backwardBigramGraphModelWriter = new BigramGraphModelWriterImpl(backwardBigramPath, backwardBigramGraph);

        Runnable forwadTrigramGraphModelWriter = new TrigramGraphModelWriterImpl(forwardTrigramPath, forwardTrigramGraph);
        Runnable backwardTrigramGraphModelWriter = new TrigramGraphModelWriterImpl(backwardTrigramPath, backwardTrigramGraph);

        Future<?> forwardBigramFuture = executor.submit(forwardBigramGraphModelWriter);
        Future<?> backwardBigramFuture = executor.submit(backwardBigramGraphModelWriter);

        Future<?> forwardTrigramFuture = executor.submit(forwadTrigramGraphModelWriter);
        Future<?> backwardTrigramFuture = executor.submit(backwardTrigramGraphModelWriter);


        boolean areModelsWritten = false;
        while (!forwardBigramFuture.isDone() && !backwardBigramFuture.isDone() && !forwardTrigramFuture.isDone() &&
                !backwardTrigramFuture.isDone()) {
        }

        executor.shutdown();
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Training data processed in " + (elapsedTime / 1000) / 60 + " minutes and "
                + +(elapsedTime / 1000) % 60 + " seconds");
    }
}
