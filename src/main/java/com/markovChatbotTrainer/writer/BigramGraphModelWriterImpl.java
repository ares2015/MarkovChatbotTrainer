package com.markovChatbotTrainer.writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Oliver on 4/25/2017.
 */
public class BigramGraphModelWriterImpl implements BigramGraphModelWriter, Runnable {

    private String path = "c:\\Users\\Oliver\\Documents\\NlpTrainingData\\MarkovChatbot\\MarkovBigramChatbotModel.txt";

    private Map<String, TreeMap<Integer, List<String>>> bigramGraph;

    public BigramGraphModelWriterImpl(Map<String, TreeMap<Integer, List<String>>> bigramGraph) {
        this.bigramGraph = bigramGraph;
    }

    @Override
    public void run() {
        try {
            writeBigramModel();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeBigramModel() throws IOException {
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            fw = new FileWriter(path, true);
            bw = new BufferedWriter(fw);
            Iterator<Map.Entry<String, TreeMap<Integer, List<String>>>> iterator = bigramGraph.entrySet().iterator();
            StringBuilder stringBuilder = new StringBuilder();
            while (iterator.hasNext()) {
                Map.Entry<String, TreeMap<Integer, List<String>>> next = iterator.next();
                String token1 = next.getKey();
                stringBuilder.append(token1);
                stringBuilder.append(">>>>>");
                TreeMap<Integer, List<String>> treeMap = next.getValue();
                Iterator<Map.Entry<Integer, List<String>>> iterator2 = treeMap.entrySet().iterator();
                while (iterator2.hasNext()) {
                    Map.Entry<Integer, List<String>> next2 = iterator2.next();
                    Integer frequency = next2.getKey();
                    stringBuilder.append("#");
                    stringBuilder.append(frequency);
                    stringBuilder.append("@");
                    List<String> tokens = next2.getValue();
                    for (String token : tokens) {
                        stringBuilder.append(token);
                        stringBuilder.append("@");
                    }
                }
                System.out.println(stringBuilder.toString());
                bw.write(stringBuilder.toString());
                bw.newLine();
                stringBuilder.setLength(0);

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}