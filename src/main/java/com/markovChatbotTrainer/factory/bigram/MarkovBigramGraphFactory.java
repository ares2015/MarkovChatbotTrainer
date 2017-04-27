package com.markovChatbotTrainer.factory.bigram;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Oliver on 4/27/2017.
 */
public interface MarkovBigramGraphFactory {

    Map<String, TreeMap<Integer, List<String>>> create();

}
