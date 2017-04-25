package com.markovChatbotTrainer;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Oliver on 4/25/2017.
 */
public interface MarkovGraphFactory {

    Map<String, TreeMap<Integer, List<String>>> create();

}
