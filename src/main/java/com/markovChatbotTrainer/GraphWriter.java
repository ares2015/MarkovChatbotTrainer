package com.markovChatbotTrainer;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Oliver on 4/25/2017.
 */
public interface GraphWriter {

    void write(Map<String, TreeMap<Integer, List<String>>> graph) throws IOException;

}
